package com.auto1.testdatastorage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArchiveOmniDTO {

    String dataType;
    LocalDateTime createdBefore;

}
