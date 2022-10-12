package com.auto1.testdatastorage.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OmniDTO {

    private Long id;
    private String data;
    private Boolean archived;
    private LocalDateTime created;
    private LocalDateTime updated;

}
