package com.bmilab.backend.domain.user.event.listener;

import com.bmilab.backend.domain.user.entity.UserEducation;
import com.bmilab.backend.domain.user.entity.UserInfo;
import com.bmilab.backend.domain.user.event.UserEducationUpdateEvent;
import com.bmilab.backend.domain.user.exception.UserErrorCode;
import com.bmilab.backend.domain.user.repository.UserEducationRepository;
import com.bmilab.backend.domain.user.repository.UserInfoRepository;
import com.bmilab.backend.global.exception.ApiException;
import java.util.Comparator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserEducationEventListener {
    private final UserInfoRepository userInfoRepository;
    private final UserEducationRepository userEducationRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserEducationUpdate(UserEducationUpdateEvent event) {
        Long userId = event.userId();
        Optional<UserEducation> finalEducation = userEducationRepository.findAllByUserId(userId)
                .stream()
                .filter(e -> e.getStartYearMonth() != null)
                .max(Comparator.comparing(UserEducation::getStartYearMonth));

        if (finalEducation.isPresent()) {
            UserInfo userInfo = userInfoRepository.findByUserId(userId)
                    .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

            userInfo.updateEducation(finalEducation.get().toFinalEducationString());
            userInfoRepository.save(userInfo);
        }
    }
}
