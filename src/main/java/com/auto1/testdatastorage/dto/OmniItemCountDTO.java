package com.auto1.testdatastorage.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OmniItemCountDTO {

    private String dataType;
    private Long itemCount;
    private String meta;
}
