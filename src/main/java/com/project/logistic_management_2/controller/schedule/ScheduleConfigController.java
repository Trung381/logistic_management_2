package com.project.logistic_management_2.controller.schedule;

import com.project.logistic_management_2.dto.response.BaseResponse;
import com.project.logistic_management_2.dto.schedule.ScheduleConfigDTO;
import com.project.logistic_management_2.service.schedule.ScheduleConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
