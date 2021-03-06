package com.sellermatch.process.common.domain;

public class CommonConstant {

    /*성공,실패 result메시지*/
    public final static String SUCCESS= "SUCCESS";
    public final static String ERROR= "ERROR";

    /* 공백 또는 NULL */
    public final static int ERROR_NULL_100 = -100; // 아이디 공백/null
    public final static int ERROR_NULL_101 = -101; // 비밀번호 공백/null
    public final static int ERROR_NULL_105 = -105; // 연락처 공백/null
    public final static int ERROR_NULL_107 = -107; // 회원 유형 공백
    public final static int ERROR_NULL_113 = -113; // 닉네임 공백/null
    public final static int ERROR_NULL_116 = -116; // 이름 공백/null
    public final static int ERROR_NULL_119 = -119; // 국가 공백/null
    public final static int ERROR_NULL_120 = -120; // 지역 공백/null
    public final static int ERROR_NULL_121 = -121; // 판매자/공급자 소개 공백/null
    public final static int ERROR_NULL_123 = -123; // 매출규모(판매자) 공백/null
    public final static int ERROR_NULL_126 = -126; // 등록자 지역 공백/null
    public final static int ERROR_NULL_127 = -127; // 상품분야 공백/null
    public final static int ERROR_NULL_128 = -128; // 사업자번호 공백/null
    public final static int ERROR_NULL_130 = -130; // 사업자유형 공백/null
    public final static int ERROR_NULL_131 = -131; // 이미지 공백/null
    public final static int ERROR_NULL_132 = -132; // 제목 공백/null
    public final static int ERROR_NULL_134 = -134; // 상품단가 공백/null
    public final static int ERROR_NULL_135 = -135; // 판매마진 공백/null
    public final static int ERROR_NULL_136 = -136; // 등록지역 공백/null
    public final static int ERROR_NULL_137 = -137; // 공급방법 공백/null
    public final static int ERROR_NULL_138 = -138; // 판매채널 공백/null
    public final static int ERROR_NULL_139 = -139; // 모집마감일 공백/null
    public final static int ERROR_NULL_140 = -140; // 모집인원 공백/null
    public final static int ERROR_NULL_143 = -143; // 상세설명 공백/null
    public final static int ERROR_NULL_145 = -145; // 탈퇴사유 공백/null
    public final static int ERROR_NULL_147 = -147; // 탈퇴 인증 코드 공백/null
    public final static int ERROR_NULL_149 = -149; // 댓글 공백/null
    public final static int ERROR_NULL_150 = -150; // 검색어 공백/null, 2글자 미만
    public final static int ERROR_NULL_152 = -152; // 매칭 대표이미지 null
    public final static int ERROR_NULL_213 = -213; // 비밀번호확인 null
    public final static int ERROR_NULL_216 = -216; // 제안할 프로제트 조회결과 null
    public final static int ERROR_NULL_221 = -221; // 아이디 조회결과 null
    public final static int ERROR_NULL_225 = -225; // 이용약관 동의 null
    public final static int ERROR_NULL_226 = -226; // 개인정보처리방침 동의 null
    public final static int ERROR_NULL_227 = -227; // 14세 이상 동의 null
    public final static int ERROR_NULL_229 = -229; // 제휴/제안 문의내용 null
    public final static int ERROR_NULL_230 = -230; // 게시판 내용 null
    public final static int ERROR_NULL_231 = -231; // 게시판 유형 null
    public final static int ERROR_NULL_233 = -233; // 이메일 null
    public final static int ERROR_NULL_235 = -235; // 판매경력 null
    public final static int ERROR_NULL_236 = -236; // 판매채널 null

    /* 중복 */
    public final static int ERROR_DUPLICATE_108 = -108; // 이메일 중복
    public final static int ERROR_DUPLICATE_114 = -114; // 닉네임 중복
    public final static int ERROR_DUPLICATE_202 = -202; // 지원하기 중복
    public final static int ERROR_DUPLICATE_205 = -205; // 스크랩 중복
    public final static int ERROR_DUPLICATE_207 = -207; // 제안하기 중복
    public final static int ERROR_DUPLICATE_219 = -219; // 해시태그 중복


    /* 형식 미일치 */
    public final static int ERROR_FORMAT_104 = -104;  // 이메일 형식 미일치
    public final static int ERROR_FORMAT_106 = -106;  // 연락처(휴대폰) 형식 미일치
    public final static int ERROR_FORMAT_111 = -111;  // 비밀번호 형식 미일치
    public final static int ERROR_FORMAT_118 = -118;  // 이름 형식 미일치
    public final static int ERROR_FORMAT_124 = -124;  // 매출규모 형식(숫자만) 미일치
    public final static int ERROR_FORMAT_129 = -129;  // 사업자번호 형식 미일치
    public final static int ERROR_FORMAT_142 = -142;  // 모집인원 형식(숫자만) 미일치

    /* 길이 제한 */
    public final static int ERROR_LENGTH_109 = -109;  // 이메일 길이 제한
    public final static int ERROR_LENGTH_110 = -110;  // 비밀번호 길이 제한
    public final static int ERROR_LENGTH_115 = -115;  // 닉네임 길이 제한
    public final static int ERROR_LENGTH_117 = -117;  // 이름 길이 제한
    public final static int ERROR_LENGTH_122 = -122;  // 판매자/공급자 소개 길이 제한
    public final static int ERROR_LENGTH_125 = -125;  // 매출규모 길이 제한
    public final static int ERROR_LENGTH_133 = -133;  // 제목 길이 제한
    public final static int ERROR_LENGTH_144 = -144;  // 필수조건 길이 제한
    public final static int ERROR_LENGTH_146 = -146;  // 탈퇴사유 내용 길이 제한
    public final static int ERROR_LENGTH_151 = -151;  // 댓글내용 길이 제한
    public final static int ERROR_LENGTH_220 = -220;  // 해시태그 길이 제한
    public final static int ERROR_LENGTH_232 = -151;  // 내용 길이 제한

    /* 숫자(개수) 제한 */
    public final static int ERROR_NUMSIZE_141 = -141;  // 모집인원 숫자 제한
    public final static int ERROR_NUMSIZE_217 = -217;  // 모집인원 0이하
    public final static int ERROR_NUMSIZE_218 = -218;  // 모집인원 최대숫자 초과

    /* 타입 미일치 */
    public final static int ERROR_TYPE_203 = -203;  // 회원타입 미일치 (판매자만 이용가능)
    public final static int ERROR_TYPE_206 = -206;  // 회원타입 미일치 (공급자만 이용가능)

    /* 정보 불일치 */
    public final static int ERROR_MISMATCH_102 = -102;  // 아이디 혹은 비밀번호 미일치
    public final static int ERROR_MISMATCH_103 = -103;  // SNS 가입 미일치
    public final static int ERROR_MISMATCH_214 = -214;  // SNS 계정정보 불일치
    public final static int ERROR_MISMATCH_112 = -112;  // 비밀번호 확인 미일치
    public final static int ERROR_MISMATCH_148 = -148;  // 탈퇴 인증 코드 미일치
    public final static int ERROR_MISMATCH_224 = -224;  // 이미지파일 미일치
    public final static int ERROR_MISMATCH_228 = -228; // 게시물 작성자 미일치

    /* 접근 제한 */
    public final static int ERROR_ACCESS_200 = -200;  // 미로그인 접근불가 페이지 접근
    public final static int ERROR_ACCESS_201 = -201;  // 탈퇴회원으로 로그인
    public final static int ERROR_ACCESS_204 = -204;  // 마감거래 지원
    public final static int ERROR_ACCESS_215 = -215;  // 자신의게시물에 지원, 제안, 스크랩 등
    public final static int ERROR_ACCESS_222 = -222;  // SNS가입계정 비밀번호찾기
    public final static int ERROR_ACCESS_223 = -223;  // (네이버)SNS계정인데 일반이메일 가입한 계정
    public final static int ERROR_ACCESS_234 = -234;  // 비밀 게시물 접근제한


    /* 공통 에러 */
    public final static int ERROR_999 = -999;  // 알수 없는 공통 에러
    public final static int ERROR_998 = -998;  // 컨텐츠 빈 값
}
