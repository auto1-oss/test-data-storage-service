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
public class CleanOmniDTO {
    String dataType;
    LocalDateTime createdBefore;
}
