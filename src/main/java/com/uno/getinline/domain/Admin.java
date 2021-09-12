package com.uno.getinline.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@Table(indexes = {
        @Index(columnList = "phoneNumber"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "modifiedAt")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false, unique = true)
    private String nickname;

    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false)
    private String phoneNumber;


    @Setter
    private String memo;


    @Column(nullable = false, insertable = false, updatable = false,
            columnDefinition = "datetime default CURRENT_TIMESTAMP")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false, insertable = false, updatable = false,
            columnDefinition = "datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @LastModifiedDate
    private LocalDateTime modifiedAt;


    protected Admin() {}

    protected Admin(String email, String nickname, String password, String phoneNumber, String memo) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.memo = memo;
    }

    public static Admin of(String email, String nickname, String password, String phoneNumber, String memo) {
        return new Admin(email, nickname, password, phoneNumber, memo);
    }

}
