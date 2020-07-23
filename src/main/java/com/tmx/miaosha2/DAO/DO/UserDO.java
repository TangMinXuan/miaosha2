package com.tmx.miaosha2.DAO.DO;

public class UserDO {

    private Long userId;
    private String userEmail;
    private String userPassword;
    private Integer userSalt;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Integer getUserSalt() {
        return userSalt;
    }

    public void setUserSalt(Integer userSalt) {
        this.userSalt = userSalt;
    }
}
