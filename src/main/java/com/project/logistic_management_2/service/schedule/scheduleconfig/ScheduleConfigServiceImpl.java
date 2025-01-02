package com.project.logistic_management_2.service.schedule.scheduleconfig;

import com.project.logistic_management_2.dto.schedule.ScheduleConfigDTO;
import com.project.logistic_management_2.entity.ScheduleConfig;
import com.project.logistic_management_2.enums.PermissionKey;
import com.project.logistic_management_2.enums.PermissionType;
import com.project.logistic_management_2.exception.def.InvalidParameterException;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.schedule.ScheduleConfigMapper;
import com.project.logistic_management_2.repository.schedule.scheduleconfig.ScheduleConfigRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.FileFactory;
import com.project.logistic_management_2.utils.ImportConfig;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleConfigServiceImpl extends BaseService implements ScheduleConfigService {
    private final ScheduleConfigRepo scheduleConfigRepo;
    private final ScheduleConfigMapper scheduleConfigMapper;
    private final PermissionType type = PermissionType.CONFIGS;

    @Override
    public List<ScheduleConfigDTO> getAll() {
        checkPermission(type, PermissionKey.VIEW);
        return scheduleConfigRepo.getAll();
    }

    @Override
    public ScheduleConfigDTO getByID(String id) {
        checkPermission(type, PermissionKey.VIEW);
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }
        ScheduleConfig config = scheduleConfigRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin cấu hình lịch trình!"));
        return scheduleConfigMapper.toScheduleConfigDTO(null, config);
    }

    @Override
    public ScheduleConfigDTO create(ScheduleConfigDTO dto) {
        checkPermission(type, PermissionKey.WRITE);
        ScheduleConfig config = scheduleConfigMapper.toScheduleConfig(dto);
        scheduleConfigRepo.save(config);
        return scheduleConfigMapper.toScheduleConfigDTO(dto, config);
    }

    @Override
    public ScheduleConfigDTO update(String id, ScheduleConfigDTO dto) {
        checkPermission(type, PermissionKey.WRITE);
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }

        ScheduleConfig config = scheduleConfigRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin cấu hình lịch trình!"));
        scheduleConfigMapper.updateScheduleConfig(config, dto);

        //Lưu lại cập nhật vào db và trả về dto từ mapper
        return scheduleConfigMapper.toScheduleConfigDTO(dto, scheduleConfigRepo.save(config));
    }

    @Override
    public long deleteByID(String id) {
        checkPermission(type, PermissionKey.DELETE);
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }
        return scheduleConfigRepo.delete(id);
    }

    @Override
    public List<ScheduleConfig> importScheduleConfigData(MultipartFile importFile) {

        checkPermission(type, PermissionKey.WRITE);

        Workbook workbook = FileFactory.getWorkbookStream(importFile);
        List<ScheduleConfigDTO> scheduleConfigDTOList = ExcelUtils.getImportData(workbook, ImportConfig.scheduleConfigImport);

        List<ScheduleConfig> scheduleConfig = scheduleConfigMapper.toScheduleConfigList(scheduleConfigDTOList);

        // Lưu tất cả các thực thể vào cơ sở dữ liệu và trả về danh sách đã lưu
        return scheduleConfigRepo.saveAll(scheduleConfig);
    }
}
