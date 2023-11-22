package com.kcr.domain.type;

public enum RoleType {
    NOT_PERMITTED(Authority.NOT_PERMITTED),
    USER(Authority.USER),  // 사용자 권한
    ADMIN(Authority.ADMIN);  // 관리자 권한

    private final String authority;

    RoleType(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String NOT_PERMITTED = "ROLE_NOT_PERMITTED";
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
