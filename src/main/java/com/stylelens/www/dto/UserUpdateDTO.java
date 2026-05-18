package com.stylelens.www.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateDTO implements Serializable {

    private Long id;

    @Size(min = 2, max = 20, message = "用户名长度必须在2-20个字符之间")
    private String username;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Size(max = 20, message = "手机号长度不能超过20个字符")
    private String phone;

    private String avatar;
}
