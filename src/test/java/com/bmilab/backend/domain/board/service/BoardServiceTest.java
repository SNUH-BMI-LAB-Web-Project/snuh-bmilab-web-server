package com.bmilab.backend.domain.board.service;

import com.bmilab.backend.domain.board.dto.request.BoardPinRequest;
import com.bmilab.backend.domain.board.entity.Board;
import com.bmilab.backend.domain.board.entity.BoardCategory;
import com.bmilab.backend.domain.board.exception.BoardErrorCode;
import com.bmilab.backend.domain.board.repository.BoardRepository;
import com.bmilab.backend.domain.file.service.FileService;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.service.UserService;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.external.s3.S3Service;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * BoardService updateBoardPinStatus 동시성 및 락 동작을 검증하는 테스트 클래스
 *
 * 전제 조건:
 * - ActiveProfile이 local이어야 함
 * - 로컬 DB(MySQL 등)에 id=1인 User, id=1인 BoardCategory 데이터가 있어야 함
 * - 이 데이터가 없으면 FK 제약으로 인해 테스트 실패
 */
@DataJpaTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({BoardService.class, BoardServiceTest.StubBeans.class})
class BoardServiceTest {

    @Autowired
    BoardService boardService;
    @Autowired BoardRepository boardRepository;
    @Autowired EntityManager em;
    @Autowired PlatformTransactionManager txManager;

    /**
     * QueryDSL 사용을 위한 JPAQueryFactory 빈 설정
     */
    @TestConfiguration
    static class TestJpaConfig {
        @Bean JPAQueryFactory jpaQueryFactory(EntityManager em) {
            return new JPAQueryFactory(em);
        }
    }

    /**
     * 테스트 시 실제 서비스 의존성을 모두 Mock으로 대체
     * - UserService, BoardCategoryService, FileService, S3Service
     */
    @TestConfiguration
    static class StubBeans {
        @Bean UserService userService() { return Mockito.mock(UserService.class); }
        @Bean BoardCategoryService boardCategoryService() { return Mockito.mock(BoardCategoryService.class); }
        @Bean FileService fileService() { return Mockito.mock(FileService.class); }
        @Bean S3Service s3Service() { return Mockito.mock(S3Service.class); }
    }

    /**
     * 각 테스트 시작 전 Board 테이블 데이터를 비워 테스트 간 간섭을 방지
     */
    @BeforeEach
    void clean() {
        boardRepository.deleteAll();
    }

    /**
     * 트랜잭션 안에서 주어진 작업을 실행하기 위한 유틸 메서드
     * (FOR UPDATE 쿼리 같은 트랜잭션 내에서만 가능한 검증 로직 실행 시 사용)
     */
    private <T> T inTx(Supplier<T> work) {
        return new TransactionTemplate(txManager).execute(status -> work.get());
    }

    /**
     * 새 게시글(Board) 엔티티를 생성하고 저장
     *
     * 주의: 로컬 DB에 id=1 User, id=1 BoardCategory가 있어야 함
     */
    private Board newBoard(String title, boolean pinned) {
        User authorRef = em.getReference(User.class, 1L); // 기존 로컬 DB User 참조
        BoardCategory categoryRef = em.getReference(BoardCategory.class, 1L); // 기존 로컬 DB 카테고리 참조

        Board b = Board.builder()
                .title(title)
                .content("test")
                .isPinned(pinned)
                .author(authorRef)
                .category(categoryRef)
                .build();
        return boardRepository.save(b);
    }

    /**
     * 시나리오:
     * - 이미 4개의 게시글이 고정된 상태에서
     * - 두 명의 관리자가 동시에 서로 다른 게시글을 고정하려고 시도
     *
     * 기대:
     * - 둘 중 하나만 성공, 다른 하나는 BOARD_PIN_LIMIT_EXCEEDED 에러
     * - 최종 고정 개수는 5개 유지
     *
     * @Transactional(propagation = NOT_SUPPORTED) :
     *   테스트 메서드 자체 트랜잭션을 끄고, 서비스 메서드 트랜잭션과 완전히 분리
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    void pinLimit_concurrentPins_onlyOneSucceeds_whenAlready4Pinned() throws InterruptedException {
        // given: 이미 4개가 고정된 상태
        for (int i = 0; i < 4; i++) {
            newBoard("pinned-" + i, true);
        }
        Board c1 = newBoard("candidate-1", false);
        Board c2 = newBoard("candidate-2", false);

        CountDownLatch start = new CountDownLatch(1);
        ExecutorService es = Executors.newFixedThreadPool(2);
        List<Future<String>> results = new ArrayList<>();

        // 첫 번째 스레드: c1 게시글 고정 시도
        Callable<String> t1 = () -> {
            start.await();
            try {
                boardService.updateBoardPinStatus(c1.getId(), new BoardPinRequest(true));
                return "OK";
            } catch (ApiException ex) {
                return ex.getErrorCode().name();
            }
        };

        // 두 번째 스레드: c2 게시글 고정 시도
        // ApiException 발생 시 해당 에러 코드 이름 반환
        Callable<String> t2 = () -> {
            start.await();
            try {
                boardService.updateBoardPinStatus(c2.getId(), new BoardPinRequest(true));
                return "OK";
            } catch (ApiException ex) {
                return ex.getErrorCode().name();
            }
        };

        results.add(es.submit(t1));
        results.add(es.submit(t2));

        // when: 두 스레드 동시 시작
        start.countDown();
        es.shutdown();
        boolean done = es.awaitTermination(10, TimeUnit.SECONDS);
        assertThat(done).isTrue();

        // then: 둘 중 하나만 성공해야 함
        String r1 = get(results.get(0));
        String r2 = get(results.get(1));
        assertThat(List.of(r1, r2))
                .containsExactlyInAnyOrder("OK", BoardErrorCode.BOARD_PIN_LIMIT_EXCEEDED.name());

        // 최종 고정 개수는 5개여야 함
        long pinnedCount = inTx(() -> boardRepository.findAllPinnedForUpdate().size());
        assertThat(pinnedCount).isEqualTo(5);
    }

    /**
     * 시나리오:
     * - 하나의 게시글에 대해 동시에 PIN, UNPIN 요청 발생
     *
     * 기대:
     * - 요청이 직렬화되어 실행되어야 함
     * - 마지막으로 완료된 작업 상태와 실제 DB 상태가 일치해야 함
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    void samePost_concurrentPinAndUnpin_areSerialized_noLostUpdate() throws InterruptedException, ExecutionException {
        // given: 초기 상태는 UNPIN
        Board target = newBoard("target", false);

        CountDownLatch start = new CountDownLatch(1);
        ExecutorService es = Executors.newFixedThreadPool(2);
        java.util.concurrent.ConcurrentLinkedQueue<String> order = new java.util.concurrent.ConcurrentLinkedQueue<>();

        // 핀 작업
        Callable<Void> pinTask = () -> {
            start.await();
            boardService.updateBoardPinStatus(target.getId(), new BoardPinRequest(true));
            order.add("PIN");
            return null;
        };

        // 언핀 작업
        Callable<Void> unpinTask = () -> {
            start.await();
            boardService.updateBoardPinStatus(target.getId(), new BoardPinRequest(false));
            order.add("UNPIN");
            return null;
        };

        Future<Void> f1 = es.submit(pinTask);
        Future<Void> f2 = es.submit(unpinTask);

        // when
        start.countDown();
        f1.get();
        f2.get();
        es.shutdown();

        // then: 예외 없이 직렬화 처리, 마지막 작업 결과와 DB 상태 일치
        Board reloaded = inTx(() -> boardRepository.findByIdWithPessimisticLock(target.getId()).orElseThrow());
        String lastDone = null;
        for (String s : order) lastDone = s; // 마지막으로 완료된 작업
        assertThat(lastDone).isIn("PIN", "UNPIN");
        if ("PIN".equals(lastDone)) {
            assertThat(reloaded.isPinned()).isTrue();
        } else {
            assertThat(reloaded.isPinned()).isFalse();
        }
    }

    /**
     * Future 결과를 안전하게 가져오기 위한 유틸
     */
    private static <T> T get(Future<T> f) {
        try { return f.get(); }
        catch (Exception e) { throw new RuntimeException(e); }
    }
}