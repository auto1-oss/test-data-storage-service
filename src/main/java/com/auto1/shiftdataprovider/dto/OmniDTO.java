package com.auto1.shiftdataprovider.dto;

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
    private String dataType;
    private Boolean dirty;
    private LocalDateTime created;
    private LocalDateTime updated;

}
