package com.project.logistic_management_2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamResource;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExportExcelResponse {
    private String fileName;
    private InputStreamResource resource;
}
