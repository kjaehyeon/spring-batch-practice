package com.example.housebatch.core.repository;

import com.example.housebatch.core.entity.Apt;
import com.example.housebatch.core.entity.AptDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AptDealRepository extends JpaRepository<AptDeal, Long> {
    Optional<AptDeal> findAptDealByAptAndExclusiveAreaAndDealDateAndDealAmountAndFloor(
            Apt apt, Double exclusiveArea, LocalDate dealDate, Long dealAmount, Integer floor
    );

    @Query("SELECT ad from AptDeal ad join fetch ad.apt where ad.dealCanceled=false and ad.dealDate= :dealDate")
    //그냥 쿼리 메서드 사용하면 n+1 문제 발생해서 join fetch 적용함
    List<AptDeal> findByDealCanceledIsFalseAndDealDateEquals(LocalDate dealDate);
}
