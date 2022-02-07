package com.example.housebatch.core.entity;

import com.example.housebatch.core.dto.AptDealDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "apt_deal")
@EntityListeners(AuditingEntityListener.class)
public class AptDeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aptDealId;

    @ManyToOne
    @JoinColumn(name = "apt_id")
    private Apt apt;

    @Column(nullable = false)
    private Double exclusiveArea;

    @Column(nullable = false)
    private LocalDate dealDate;

    @Column(nullable = false)
    private Long dealAmount;

    @Column(nullable = false)
    private Integer floor;

    @Column(nullable = false)
    private boolean dealCanceled;

    @Column
    private LocalDate dealCanceledDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;



    public static AptDeal from(AptDealDto aptDealDto){
        return AptDeal.builder()
                .exclusiveArea(aptDealDto.getExclusiveArea())
                .dealDate(aptDealDto.getDealDate())
                .dealAmount(aptDealDto.getDealAmount())
                .floor(aptDealDto.getFloor())
                .dealCanceled(aptDealDto.isDealCanceled())
                .build();
    }
}
