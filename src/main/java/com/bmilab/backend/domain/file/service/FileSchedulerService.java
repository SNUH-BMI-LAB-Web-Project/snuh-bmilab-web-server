package com.bmilab.backend.domain.file.service;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.repository.FileInformationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileSchedulerService {

    private final FileInformationRepository fileInformationRepository;

    //매일 오전 4시에 임시 파일들 삭제
    @Transactional
    @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul")
    public void deleteAllTempFiles() {
        log.info("임시 파일 삭제 시작");
        List<FileInformation> files = fileInformationRepository.findAllByDomainType(FileDomainType.TEMP);
        log.info("[삭제할 파일 목록]");
        files.forEach(file -> log.info("- fileId={}, fileName={}", file.getId(), file.getName()));
        fileInformationRepository.deleteAll(files);
        log.info("임시 파일 삭제 완료");
    }
}
