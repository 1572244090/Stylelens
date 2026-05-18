package com.stylelens.www.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserVO implements Serializable {

    private Long id;

    private String username;

    private String email;

    private String phone;

    private String avatar;

    private LocalDateTime createTime;
}
