package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.leave.dto.response.LeaveDetail;
import com.bmilab.backend.domain.leave.entity.Leave;
import com.bmilab.backend.domain.user.dto.query.UserDetailQueryResult;
import com.bmilab.backend.domain.user.entity.UserEducation;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record CurrentUserDetail(
        UserDetail user,
        List<LeaveDetail> leaves
) {
    public static CurrentUserDetail from(UserDetailQueryResult queryResult, List<Leave> leaves, List<UserEducation> educations) {
        return CurrentUserDetail
                .builder()
                .user(UserDetail.from(queryResult, educations, false))
                .leaves(
                        leaves.stream()
                                .map(LeaveDetail::from)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
