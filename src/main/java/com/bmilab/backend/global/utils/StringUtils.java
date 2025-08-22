package com.bmilab.backend.global.utils;

public class StringUtils {
    public static String generateRandomPassword() {

        char[] charSet = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        };

        StringBuilder tempPw = new StringBuilder();

        boolean hasDigit = false;

        for (int i = 0; i < 10; i++) {
            int idx = (int) (charSet.length * Math.random());
            char selectedChar = charSet[idx];
            tempPw.append(selectedChar);

            if (Character.isDigit(selectedChar)) {
                hasDigit = true;
            }
        }

        if (!hasDigit) {
            tempPw.setCharAt((int) (Math.random() * 10), charSet[(int) (Math.random() * 10)]);
        }

        return tempPw.toString();
    }
}
