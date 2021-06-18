package com.sellermatch.config.constant;

public enum MemberType {
    SELLER("1"),
    PROVIDER("2"),
    ADMIN("3");

    public final String label;

    private MemberType(String label) {
        this.label = label;
    }
}

// 에러코드, 성공코드 => DB 가져올필요가 없음(시스템)
// 1번 DB -> 타입, 카테고리(자주 바뀌거나 증가될때) - 등록, 수정, 삭제, 조회
// 2번 Enum -> 고정(바뀌지않는) - 수정X, 삭제X - 단, 등록, 조회 "1"