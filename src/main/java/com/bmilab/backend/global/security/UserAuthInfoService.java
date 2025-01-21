package com.bmilab.backend.global.security;

import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.exception.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthInfoService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ApiException(GlobalErrorCode.SECURITY_USER_NOT_FOUND));

            return new UserAuthInfo(user, email);
        } catch(UsernameNotFoundException exception) {
            throw new ApiException(GlobalErrorCode.SECURITY_USER_NOT_FOUND, exception);
        }
    }
}
