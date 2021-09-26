package com.uno.getinline.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "createdAt"),
        @Index(columnList = "modifiedAt")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class AdminPlaceMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Setter
    @ManyToOne(optional = false)
    private Admin admin;

    @Setter
    @ManyToOne(optional = false)
    private Place place;


    @Column(nullable = false, insertable = false, updatable = false,
            columnDefinition = "datetime default CURRENT_TIMESTAMP")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false, insertable = false, updatable = false,
            columnDefinition = "datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @LastModifiedDate
    private LocalDateTime modifiedAt;


    protected AdminPlaceMap() {}

    protected AdminPlaceMap(Admin admin, Place place) {
        this.admin = admin;
        this.place = place;
    }

    public static AdminPlaceMap of(Admin admin, Place place) {
        return new AdminPlaceMap(admin, place);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return id != null && id.equals(((AdminPlaceMap) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(place, admin, createdAt, modifiedAt);
    }

}
