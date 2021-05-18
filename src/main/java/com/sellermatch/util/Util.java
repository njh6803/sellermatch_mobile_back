package com.sellermatch.util;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Util {
    /*
     * 공백 또는 null 체크
     */
    public static boolean isEmpty(Object obj) {
        if(obj == null) return true;
        if ((obj instanceof String) && (((String)obj).trim().length() == 0)) { return true; }
        if (obj instanceof Map) { return ((Map<?, ?>) obj).isEmpty(); }
        if (obj instanceof Map) { return ((Map<?, ?>)obj).isEmpty(); }
        if (obj instanceof List) { return ((List<?>)obj).isEmpty(); }
        if (obj instanceof Object[]) { return (((Object[])obj).length == 0); }
        return false;
    }
    /**
     * 한글 + 영문 모양에 대한 형식 검사
     * @param str - 검사할 문자열
     * @return boolean - 형식에 맞을 경우 true, 맞지 않을 경우 false
     */
    public static boolean isKorAndEng(String str) {
        boolean result = false;
        result = Pattern.matches("/^[가-힣a-zA-Z]+$/;", str);

        if (Util.isEng(str) || Util.isKor(str)) {
            result = true;
        }
        return result;
    }
    /**
     * 숫자 모양에 대한 형식 검사
     * @param str - 검사할 문자열
     * @return boolean - 형식에 맞을 경우 true, 맞지 않을 경우 false
     */
    public static boolean isNum(String str) {
        boolean result = false;
        result = Pattern.matches("^[0-9]*$", str);
        return result;
    }
    /**
     * 비밀번호 형식 대한 형식 검사
     * @param str - 검사할 문자열
     * @return boolean - 형식에 맞을 경우 true, 맞지 않을 경우 false
     */
    public static boolean isPassword(String str) {
        boolean result = false;
        result = Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{6,}$", str);
        return result;
    }
    /**
     * 문자열 길이에 대한 형식 검사
     * @param str - 검사할 문자열
     * @param min - 최소 길이
     * @param max - 최대 길이
     * @return boolean - 형식에 맞을 경우 true, 맞지 않을 경우 false
     */
    public static boolean isLengthChk(String str, int min, int max) {
        boolean result = false;
        if (isEmpty(str)){
            return false;
        }
        if (str.length() < min || str.length() > max) {
            result = false;
        } else {
            result = true;
        }
        return result;
    }
    /**
     * 이메일 형식에 대한 검사
     * @param str - 검사할 문자열
     * @return boolean - 형식에 맞을 경우 true, 맞지 않을 경우 false
     */
    public static boolean isEmail(String str) {
        boolean result = false;
        result = Pattern.matches(
                "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|'" +
                        "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*')" +
                        "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                        "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:" +
                        "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", str);

        return result;
    }
    /**
     *  "-"없이 핸드폰번호인지에 대한 형식 검사
     * @param str - 검사할 문자열
     * @return boolean - 형식에 맞을 경우 true, 맞지 않을 경우 false
     */
    public static boolean isCellPhone(String str) {
        boolean result = false;
        result = Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", str);
        return result;
    }
    /**
     * "-"없이 전화번호인지에 대한 형식검사. 각 부분에 대한 자리수도 충족시켜야 한다.
     * @param str - 검사할 문자열
     * @return boolean - 형식에 맞을 경우 true, 맞지 않을 경우 false
     */
    public static boolean isTel(String str) {
        if (isEmpty(str)) {
            return false;
        }
        boolean result = false;
        result = Pattern.matches("^\\d{2,3}\\d{3,4}\\d{4}$", str);
        return result;
    }
    /**
     * "-"없이 사업자번호인지에 대한 형식검사. 각 부분에 대한 자리수도 충족시켜야 한다.
     * @param regNum - 사업자번호 문자열
     * @return boolean - 형식에 맞을 경우 true, 맞지 않을 경우 false
     */
    private final static int[] LOGIC_NUM = {1, 3, 7, 1, 3, 7, 1, 3, 5, 1};

    public final static boolean isValid(String regNum) {

        if (!isNumeric(regNum) || regNum.length() != 10)
            return false;

        int sum = 0;
        int j = -1;
        for (int i = 0; i < 9; i++) {
            j = Character.getNumericValue(regNum.charAt(i));
            sum += j * LOGIC_NUM[i];
        }

        sum += (int) (Character.getNumericValue(regNum.charAt(8)) * 5 /10);

        int checkNum = (10 - sum % 10) % 10 ;
        return (checkNum == Character.getNumericValue(regNum.charAt(9)));
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
    /**
     * 영문으로만 구성되었는지에 대한 형식 검사
     * @param str - 검사할 문자열
     * @return boolean - 형식에 맞을 경우 true, 맞지 않을 경우 false
     */
    public static boolean isEng(String str) {
        boolean result = false;
        result = Pattern.matches("^[a-zA-Z]*$", str);
        return result;
    }
    /**
     * 한글로만 구성되었는지에 대한 형식 검사
     * @param str- 검사할 문자열
     * @return boolean - 형식에 맞을 경우 true, 맞이 않을 경우 false
     */
    public static boolean isKor(String str) {
        boolean result = false;
        result = Pattern.matches("^[가-힣]*$", str);
        return result;
    }

    /**
     * 고유ID 값을 생성해준다.
     *
     * @param tableName - 데이터베이스 테이블명
     * @param sortNum - 분류번호
     */
    public static String getUniqueId(String tableName, int sortNum) {
        return String.format("%s%d%d", tableName, sortNum, System.currentTimeMillis());
    }
}
