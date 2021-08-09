package com.uno.getinline.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Admin {

    private Long id;

    private String email;
    private String nickname;
    private String password;
    private String phoneNumber;
    private String memo;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
