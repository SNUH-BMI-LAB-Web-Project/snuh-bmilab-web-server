package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.leave.dto.response.LeaveDetail;
import com.bmilab.backend.domain.leave.entity.Leave;
import com.bmilab.backend.domain.leave.entity.UserLeave;
import com.bmilab.backend.domain.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record CurrentUserDetail(
        UserDetail user,
        List<LeaveDetail> leaves
) {
    public static CurrentUserDetail from(User user, UserLeave userLeave, List<Leave> leaves) {
        return CurrentUserDetail
                .builder()
                .user(UserDetail.from(user, userLeave))
                .leaves(
                        leaves.stream()
                                .map(LeaveDetail::from)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
