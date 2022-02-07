package com.example.housebatch.core.service;

import com.example.housebatch.core.dto.AptDealDto;
import com.example.housebatch.core.dto.AptDto;
import com.example.housebatch.core.entity.Apt;
import com.example.housebatch.core.entity.AptDeal;
import com.example.housebatch.core.repository.AptDealRepository;
import com.example.housebatch.core.repository.AptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AptDealDto에 있는 값을 Apt, AptDeal 엔티티로 저장한다.
 */
@Service
@RequiredArgsConstructor
public class AptDealService {
    private final AptRepository aptRepository;
    private final AptDealRepository aptDealRepository;

    @Transactional
    public void upsert(AptDealDto aptDealDto){
        Apt apt = getAptOrNew(aptDealDto);
        saveAptDeal(aptDealDto, apt);
        //static constructor 이름은 파라미터가 여러개면 of, 파라미터가 하나면 from으로 하자
    }
    private Apt getAptOrNew(AptDealDto aptDealDto){
        Apt apt = aptRepository.findAptByAptNameAndJibun(aptDealDto.getAptName(), aptDealDto.getJibun())
                .orElseGet(() -> Apt.from(aptDealDto));
        return aptRepository.save(apt);
    }
    private void saveAptDeal(AptDealDto aptDealDto, Apt apt){
        AptDeal aptDeal = aptDealRepository.findAptDealByAptAndExclusiveAreaAndDealDateAndDealAmountAndFloor(
                apt,
                aptDealDto.getExclusiveArea(),
                aptDealDto.getDealDate(),
                aptDealDto.getDealAmount(),
                aptDealDto.getFloor()
        ).orElseGet(() -> AptDeal.from(aptDealDto));
        aptDeal.setApt(apt);
        aptDeal.setDealCanceled(aptDealDto.isDealCanceled());
        aptDeal.setDealCanceledDate(aptDealDto.getDealCanceledDate());
        aptDealRepository.save(aptDeal);
    }

    @Transactional
    public List<AptDto> findByGuLawdCdAndDealDate(String guLawdCd, LocalDate dealDate){
        return aptDealRepository.findByDealCanceledIsFalseAndDealDateEquals(dealDate)
                .stream()
                .filter(aptDeal -> aptDeal.getApt().getGuLawdCd().equals(guLawdCd))
                .map(aptDeal -> new AptDto(aptDeal.getApt().getAptName(), aptDeal.getDealAmount()))
                .collect(Collectors.toList());
    }
}
