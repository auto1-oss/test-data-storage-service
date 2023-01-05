package com.auto1.testdatastorage.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OmniTypeDTO {

    private Long id;
    private String dataType;
    private Long count;
    private String meta;
    private LocalDateTime created;
    private LocalDateTime updated;

}
