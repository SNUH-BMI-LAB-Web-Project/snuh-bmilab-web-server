package com.bmilab.backend.domain.user.dto.response;

import java.util.List;

public record SignupRequestFindAllResponse(
        List<SignupRequestDetail> requests
) {
    public static SignupRequestFindAllResponse of(List<SignupRequestDetail> requests) {
        return new SignupRequestFindAllResponse(requests);
    }
}
