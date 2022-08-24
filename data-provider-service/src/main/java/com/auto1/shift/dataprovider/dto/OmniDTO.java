package com.auto1.shift.dataprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OmniDTO {

    private Long id;

    private String data;

    private String dataType;

    private Boolean dirty;

    private LocalDateTime created;

    private LocalDateTime updated;

}
