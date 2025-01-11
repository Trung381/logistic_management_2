package com.project.logistic_management_2.controller.schedule;

import com.mysema.commons.lang.Pair;
import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.attached.AttachedImagePathsDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.enums.schedule.ScheduleStatus;
import com.project.logistic_management_2.service.schedule.schedule.ScheduleService;
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
import java.rmi.ServerException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping()
    public ResponseEntity<Object> getSchedules(
            @RequestParam Integer page,
            @RequestParam(required = false) String driverId,
            @RequestParam(required = false) String truckLicense,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {

        return ResponseEntity.ok(
                BaseResponse.ok(scheduleService.getAll(page, driverId, truckLicense, fromDate, toDate))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getScheduleByID(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(scheduleService.getByID(id))
        );
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createSchedule(@Valid @RequestBody ScheduleDTO dto) throws ServerException {
        return new ResponseEntity<>(
                BaseResponse.ok(scheduleService.create(dto)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateSchedule(@PathVariable String id, @RequestBody ScheduleDTO dto) {
        return ResponseEntity.ok(
                BaseResponse.ok(scheduleService.update(id, dto))
        );
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Object> deleteScheduleByID(@PathVariable String id) throws ServerException {
        long numOfRows = scheduleService.deleteByID(id);
        return ResponseEntity.ok(
                BaseResponse.ok(numOfRows, "Đã xóa thành công " + numOfRows + " lịch trình!")
        );
    }

    @GetMapping("/approve/{id}")
    public ResponseEntity<Object> approveScheduleByID(@PathVariable String id, @RequestParam boolean approved) throws ServerException {
        long numOfRows = scheduleService.approveByID(id, approved);
        return ResponseEntity.ok(
                BaseResponse.ok(numOfRows, "Đã xử lý thành công " + numOfRows + " lịch trình!")
        );
    }

    @PostMapping("/mark_complete/{id}")
    public ResponseEntity<Object> markComplete(@PathVariable String id, @Valid @RequestBody AttachedImagePathsDTO attachedImagePathsDTO) throws ServerException {
        long numOfRows = scheduleService.markComplete(id, attachedImagePathsDTO);
        return ResponseEntity.ok(
                BaseResponse.ok(numOfRows, numOfRows + " lịch trình đã được đánh dấu là hoàn thành!")
        );
    }

    @GetMapping("/reports")
    public ResponseEntity<Object> exportReport(@RequestParam String license, @RequestParam String period) {
        return ResponseEntity.ok(
                BaseResponse.ok(scheduleService.report(license, period))
        );
    }

    //Xuất lương lịch trình của một tài xế trong 1 chu kỳ
    @GetMapping("/reports/salary")
    public ResponseEntity<Object> exportScheduleSalary(@RequestParam String driverId, @RequestParam String period) {
        return ResponseEntity.ok(
                BaseResponse.ok(scheduleService.exportScheduleSalary(driverId, period))
        );
    }

    @GetMapping("/export")
    public ResponseEntity<Object> exportSchedule(
            @RequestParam(required = false) String driverId,
            @RequestParam(required = false) String truckLicense,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate
    ) throws Exception {

        List<ScheduleDTO> schedule = scheduleService.getAll(null, driverId, truckLicense, fromDate, toDate);

        if (!CollectionUtils.isEmpty(schedule)) {
            String fileName = "Schedule Export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.export(schedule, fileName, ExportConfig.scheduleExport);
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

    @PostMapping("/import")
    public ResponseEntity<Object> importScheduleData(@RequestParam("file") MultipartFile importFile) {
        return new ResponseEntity<>(
                BaseResponse.ok(scheduleService.importScheduleData(importFile)),
                HttpStatus.CREATED
        );
    }
}
