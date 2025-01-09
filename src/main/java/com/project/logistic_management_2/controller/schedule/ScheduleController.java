package com.project.logistic_management_2.controller.schedule;

import com.mysema.commons.lang.Pair;
import com.project.logistic_management_2.dto.BaseResponse;
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
    public ResponseEntity<Object> getSchedule(
            @RequestParam int page,
            @RequestParam(required = false) String driverId,
            @RequestParam(required = false) String truckLicense,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {

        Pair<Timestamp, Timestamp> dateRange = parseAndValidateDates(fromDate, toDate);

        return ResponseEntity.ok(
                BaseResponse.ok(scheduleService.getAll(page, driverId, truckLicense, dateRange.getFirst(), dateRange.getSecond()))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getScheduleByID(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(scheduleService.getByID(id))
        );
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createSchedule(@Valid @RequestBody ScheduleDTO dto) {
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
        return numOfRows != -1 ? ResponseEntity.ok(BaseResponse.ok(numOfRows, "Đã xử lý thành công " + numOfRows + " lịch trình!"))
                                : ResponseEntity.ok(BaseResponse.ok(null, "Lịch trình đã được xử lý trước đó!"));
    }

    @GetMapping("/mark_complete/{id}")
    public ResponseEntity<Object> markComplete(@PathVariable String id) throws ServerException {
        long numOfRows = scheduleService.markComplete(id);
        return
                numOfRows == ScheduleStatus.COMPLETED.getValue()
                ? ResponseEntity.ok(BaseResponse.ok(null, "Chuyến đi đã được đánh dấu là hoàn thành trước đó!"))
                : ResponseEntity.ok(BaseResponse.ok(numOfRows, numOfRows + " lịch trình đã được đánh dấu là hoàn thành!"));
    }

    @GetMapping("/reports")
    public ResponseEntity<Object> exportReport(@RequestParam String license, @RequestParam int year, @RequestParam int month) {
        return ResponseEntity.ok(
                BaseResponse.ok(scheduleService.report(license, year, month))
        );
    }

    //Xuất lương lịch trình của một tài xế trong 1 chu kỳ
    @GetMapping("/reports/salary")
    public ResponseEntity<Object> exportScheduleSalary(@RequestParam String driverId, @RequestParam int year, @RequestParam int month) {
        return ResponseEntity.ok(
                BaseResponse.ok(scheduleService.exportScheduleSalary(driverId, year, month))
        );
    }

    @GetMapping("/export")
    public ResponseEntity<Object> exportSchedule(
            @RequestParam(required = false) String driverId,
            @RequestParam(required = false) String truckLicense,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) throws Exception {
        Pair<Timestamp, Timestamp> dateRange = parseAndValidateDates(fromDate, toDate);

        List<ScheduleDTO> schedule = scheduleService.getAll(driverId, truckLicense, dateRange.getFirst(), dateRange.getSecond());

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

    private Pair<Timestamp, Timestamp> parseAndValidateDates(String fromDate, String toDate) throws IllegalArgumentException {
        Timestamp fromTimestamp = null;
        Timestamp toTimestamp = null;

        if (fromDate != null) {
            try {
                fromTimestamp = Timestamp.valueOf(fromDate.replace("T", " ") + ".000");
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid fromDate format.");
            }
        }

        if (toDate != null) {
            try {
                toTimestamp = Timestamp.valueOf(toDate.replace("T", " ") + ".000");
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid toDate format.");
            }
        }

        return Pair.of(fromTimestamp, toTimestamp);
    }
}
