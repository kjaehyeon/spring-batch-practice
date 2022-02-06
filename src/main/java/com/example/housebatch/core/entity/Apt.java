package com.example.housebatch.core.entity;

import com.example.housebatch.core.dto.AptDealDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@Table(name="apt")
@EntityListeners(AuditingEntityListener.class)
public class Apt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aptId;

    @Column(nullable = false)
    private String aptName;

    @Column(nullable = false)
    private String jibun;

    @Column(nullable = false)
    private String dong;

    @Column(nullable = false)
    private String guLawdCd;

    @Column(nullable = false)
    private Integer builtYear;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Apt of(AptDealDto aptDealDto){
        return Apt.builder()
                .aptName(aptDealDto.getAptName().trim())
                .jibun(aptDealDto.getJibun().trim())
                .dong(aptDealDto.getDong().trim())
                .guLawdCd(aptDealDto.getRegionalCode().trim())
                .builtYear(aptDealDto.getBuiltYear())
                .build();
    }

}
