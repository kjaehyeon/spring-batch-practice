package com.example.housebatch.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 아파트 정보와 거래정보가 합쳐진 dto클래스
 */
@AllArgsConstructor
@Getter
public class AptDto {
    private String name;
    private Long price;
}
