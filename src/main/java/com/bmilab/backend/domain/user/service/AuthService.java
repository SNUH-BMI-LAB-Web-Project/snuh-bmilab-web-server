package com.bmilab.backend.domain.user.service;

import com.bmilab.backend.domain.user.dto.request.ApproveSignupRequest;
import com.bmilab.backend.domain.user.dto.request.LoginRequest;
import com.bmilab.backend.domain.user.dto.request.SignupRequest;
import com.bmilab.backend.domain.user.dto.response.LoginResponse;
import com.bmilab.backend.domain.user.dto.response.SignupRequestDetail;
import com.bmilab.backend.domain.user.dto.response.SignupRequestFindAllResponse;
import com.bmilab.backend.domain.user.entity.SignupRequestInfo;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.enums.Role;
import com.bmilab.backend.domain.user.exception.UserErrorCode;
import com.bmilab.backend.domain.user.repository.SignupRequestInfoRepository;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.jwt.TokenProvider;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final SignupRequestInfoRepository signupRequestInfoRepository;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ApiException(UserErrorCode.PASSWORD_MISMATCH);
        }

        String accessToken = tokenProvider.generateToken(user, Duration.ofDays(30));

        return LoginResponse.of(accessToken);
    }

    @Transactional
    public void requestSignup(SignupRequest request) {
        SignupRequestInfo signupRequestInfo = SignupRequestInfo
                .builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .department(request.department())
                .isApproved(false)
                .build();

        signupRequestInfoRepository.save(signupRequestInfo);
    }

    @Transactional
    public void approveSignup(ApproveSignupRequest request) {
        SignupRequestInfo signupRequestInfo = signupRequestInfoRepository.findById(request.requestId())
                .orElseThrow(() -> new ApiException(UserErrorCode.SIGNUP_REQUEST_NOT_FOUND));

        signupRequestInfo.approve();

        User user = User.builder()
                .name(signupRequestInfo.getName())
                .email(signupRequestInfo.getEmail())
                .password(signupRequestInfo.getPassword())
                .department(signupRequestInfo.getDepartment())
                .leaveCount(request.leaveCount())
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void rejectSignup(Long requestId) {
        SignupRequestInfo signupRequestInfo = signupRequestInfoRepository.findById(requestId)
                .orElseThrow(() -> new ApiException(UserErrorCode.SIGNUP_REQUEST_NOT_FOUND));

        signupRequestInfo.reject();
    }

    public SignupRequestFindAllResponse getAllSignupRequests(boolean isPending) {
        List<SignupRequestDetail> requests = signupRequestInfoRepository.findAll()
                .stream()
                .filter((signupRequest) -> isPending == signupRequest.isPending())
                .map(SignupRequestDetail::from)
                .toList();

        return SignupRequestFindAllResponse.of(requests);
    }
}
