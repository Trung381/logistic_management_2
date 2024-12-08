package com.project.logistic_management_2.controller.schedule;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.service.schedule.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping()
    public ResponseEntity<Object> getSchedule() {
        return ResponseEntity.ok(
                BaseResponse.ok(scheduleService.getAll())
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
    public ResponseEntity<Object> updateSchedule(@PathVariable String id, @Valid @RequestBody ScheduleDTO dto) {
        return ResponseEntity.ok(
                BaseResponse.ok(scheduleService.update(id, dto))
        );
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Object> deleteScheduleByID(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(scheduleService.deleteByID(id))
        );
    }

    @GetMapping("/approve/{id}")
    public ResponseEntity<Object> approveScheduleByID(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(scheduleService.approveByID(id))
        );
    }

    @GetMapping("/mark_complete/{id}")
    public ResponseEntity<Object> markComplete(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(scheduleService.markComplete(id))
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
}
