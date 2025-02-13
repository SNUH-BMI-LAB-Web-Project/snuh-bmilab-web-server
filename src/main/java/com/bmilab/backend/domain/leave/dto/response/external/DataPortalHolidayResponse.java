package com.bmilab.backend.domain.leave.dto.response.external;

import java.util.List;

public record DataPortalHolidayResponse(
        HolidayResponse response
) {
    public record HolidayResponse(
            HolidayHeader header,
            HolidayBody body
    ) { }

    public record HolidayHeader(
            String resultCode,
            String resultMsg
    ) { }

    public record HolidayBody(
            ItemWrapper items,
            int numOfRows,
            int pageNo,
            int totalCount
    ) { }

    public record ItemWrapper(
            List<HolidayItem> item
    ) { }

    public record HolidayItem(
            String dateKind,
            String dateName,
            String isHoliday,
            int locdate,
            int seq
    ) { }
}
