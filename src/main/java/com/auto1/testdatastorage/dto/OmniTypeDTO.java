package com.auto1.testdatastorage.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OmniTypeDTO {

    Long id;
    String dataType;
    String meta;
    LocalDateTime created;
    LocalDateTime updated;

}
