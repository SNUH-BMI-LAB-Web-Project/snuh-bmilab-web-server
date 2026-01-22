package com.bmilab.backend.domain.research.service;

import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.exception.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorSyncService {

    private final UserRepository userRepository;

    /**
     * Author 엔티티를 생성하고 저장하는 공통 메서드
     *
     * @param authorRequests Author 요청 DTO 리스트
     * @param userIdExtractor DTO에서 userId를 추출하는 Function
     * @param roleExtractor DTO에서 role을 추출하는 Function
     * @param authorBuilder User와 role로 Author 엔티티를 생성하는 BiFunction
     * @param <R> Author 요청 DTO 타입
     * @param <A> Author 엔티티 타입
     * @return 생성된 Author 엔티티 리스트
     */
    public <R, A> List<A> syncAuthors(
            List<R> authorRequests,
            Function<R, Long> userIdExtractor,
            Function<R, String> roleExtractor,
            BiFunction<User, String, A> authorBuilder
    ) {
        if (authorRequests == null || authorRequests.isEmpty()) {
            return new ArrayList<>();
        }

        // 1. User ID 추출
        List<Long> userIds = authorRequests.stream()
                .map(userIdExtractor)
                .collect(Collectors.toList());

        // 2. User 조회 및 검증
        List<User> users = userRepository.findAllById(userIds);
        if (users.size() != userIds.size()) {
            throw new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND);
        }

        // 3. User Map 생성
        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        // 4. Author 엔티티 생성
        List<A> authors = new ArrayList<>();
        for (R authorRequest : authorRequests) {
            User user = userMap.get(userIdExtractor.apply(authorRequest));
            String role = roleExtractor.apply(authorRequest);
            A author = authorBuilder.apply(user, role);
            authors.add(author);
        }

        return authors;
    }
}
