package com.example.housebatch.core.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class NotificationDto {
    private String email;
    private String guName;
    private Integer count; //총 몇개의 거래가 발생했는지
    private List<AptDto> aptDeals;

    public String toMessage(){
        return String.format("%s 아파트 실거래가 알림\n" +
                "총 %d개 거래가 발생했습니다.\n", guName, count)
                +
                aptDeals.stream()
                        .map(deal -> String.format("- %s : %d만원\n", deal.getName(), deal.getPrice()))
                        .collect(Collectors.joining());
    }
}
