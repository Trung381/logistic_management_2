package com.project.logistic_management_2.controller.schedule;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.schedule.ScheduleConfigDTO;
import com.project.logistic_management_2.service.schedule.ScheduleConfigService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.ExportConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleConfigController {
    private final ScheduleConfigService scheduleConfigService;

    @GetMapping("/configs")
    public ResponseEntity<Object> getScheduleConfigs() {
        return ResponseEntity.ok(
                BaseResponse.ok(scheduleConfigService.getAll())
        );
    }

    @GetMapping("/configs/{id}")
    public ResponseEntity<Object> getScheduleConfigByID(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(scheduleConfigService.getByID(id))
        );
    }

    @PostMapping("/configs/create")
    public ResponseEntity<Object> createScheduleConfig(@Valid @RequestBody ScheduleConfigDTO dto) {
        return new ResponseEntity<>(
                BaseResponse.ok(scheduleConfigService.create(dto)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/configs/update/{id}")
    public ResponseEntity<Object> updateScheduleConfig(@PathVariable String id, @Valid @RequestBody ScheduleConfigDTO dto) {
        return ResponseEntity.ok(
                BaseResponse.ok(scheduleConfigService.update(id, dto))
        );
    }

    @GetMapping("/configs/delete/{id}")
    public ResponseEntity<Object> deleteScheduleConfigByID(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(scheduleConfigService.deleteByID(id))
        );
    }

    @GetMapping("/configs/export")
    public ResponseEntity<Object> exportScheduleConfig() throws Exception {

        List<ScheduleConfigDTO> scheduleConfig = scheduleConfigService.getAll();


        if (!CollectionUtils.isEmpty(scheduleConfig)) {
            String fileName = "ScheduleConfig Export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.export(scheduleConfig, fileName, ExportConfig.scheduleConfigExport);

            InputStreamResource inputStreamResource = new InputStreamResource(in);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    )
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                    .body(inputStreamResource);
        } else {
            throw new Exception("No data");

        }
    }



    @PostMapping("/configs/import")
    public ResponseEntity<Object> importScheduleConfigData(@RequestParam("file") MultipartFile importFile) {
        return new ResponseEntity<>(
                BaseResponse.ok(scheduleConfigService.importScheduleConfigData(importFile)),
                HttpStatus.CREATED
        );
    }
}
