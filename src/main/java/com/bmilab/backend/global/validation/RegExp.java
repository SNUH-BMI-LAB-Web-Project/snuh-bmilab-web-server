package com.bmilab.backend.global.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegExp {

    public static final String NAME_EXPRESSION = "^[가-힣a-zA-Z()\\s]{2,}$";
    public static final String NAME_MESSAGE = "이름은 한글, 영문, 괄호, 공백을 포함해 2자 이상 입력해 주세요.";

    public static final String EMAIL_EXPRESSION = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String EMAIL_MESSAGE = "이메일 형식이 올바르지 않습니다.";

    public static final String PASSWORD_EXPRESSION = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$";
    public static final String PASSWORD_MESSAGE = "비밀번호는 8자 이상의 영문자 및 숫자 조합으로 작성해주세요.";

    public static final String PHONE_NUMBER_EXPRESSION = "^(01[016789])-[0-9]{3,4}-[0-9]{4}$";
    public static final String PHONE_NUMBER_MESSAGE = "올바른 전화번호가 아닙니다.";

    public static final String YYYY_MM_DD_EXPRESSION = "^\\d{4}-\\d{2}-\\d{2}$";
    public static final String YYYY_MM_DD_MESSAGE = "올바른 날짜가 아닙니다";

    public static final String HH_MM_EXPRESSION = "^([01]?[0-9]|2[0-3]):([0-5]?[0-9])$";
    public static final String HH_MM_MESSAGE = "올바른 시간이 아닙니다";

    public static final String YYYY_MM_DD_HH_MM_SS_EXPRESSION = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$";
    public static final String YYYY_MM_DD_HH_MM_SS_MESSAGE = "올바른 날짜와 시간이 아닙니다";
}
