package com.team14.backend.model;

public enum UserRoleEnum {
    USER(Authority.USER), //==ROLE_USER
    ADMIN(Authority.ADMIN); //==ROLE_ADMIN

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    //Enum 상수에 쓸 권한명을 클래스내 클래스변수로 만들어서 쓴다
    public static class Authority{
        //클래스변수
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
