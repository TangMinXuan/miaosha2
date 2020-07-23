package com.tmx.miaosha2.controller.VO;

import com.tmx.miaosha2.service.validator.PasswordNotEmpty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class LoginVO {

    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;

    @PasswordNotEmpty
    private String password;

    @Override
    public String toString() {
        return "LoginVO{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
