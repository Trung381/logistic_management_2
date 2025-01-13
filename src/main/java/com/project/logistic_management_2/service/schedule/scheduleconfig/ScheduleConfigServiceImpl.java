package com.project.logistic_management_2.service.schedule.scheduleconfig;

import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.schedule.ScheduleConfigDTO;
import com.project.logistic_management_2.entity.ScheduleConfig;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.exception.define.InvalidParameterException;
import com.project.logistic_management_2.exception.define.NotFoundException;
import com.project.logistic_management_2.mapper.schedule.ScheduleConfigMapper;
import com.project.logistic_management_2.repository.schedule.scheduleconfig.ScheduleConfigRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.ExportConfig;
import com.project.logistic_management_2.utils.FileFactory;
import com.project.logistic_management_2.utils.ImportConfig;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.rmi.ServerException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleConfigServiceImpl extends BaseService implements ScheduleConfigService {
    private final ScheduleConfigRepo scheduleConfigRepo;
    private final ScheduleConfigMapper scheduleConfigMapper;
    private final PermissionType type = PermissionType.CONFIGS;

    @Override
    public List<ScheduleConfigDTO> getAll(int page) {
        checkPermission(type, PermissionKey.VIEW);
        if (page <= 0) {
            throw new InvalidParameterException("Vui lòng chọn trang bắt đầu từ 1!");
        }
        return scheduleConfigRepo.getAll(page);
    }

    @Override
    public List<ScheduleConfigDTO> getAll() {
        checkPermission(type, PermissionKey.VIEW);
        return scheduleConfigRepo.getAll();
    }

    @Override
    public ScheduleConfigDTO getByID(String id) {
        checkPermission(type, PermissionKey.VIEW);
        ScheduleConfig config = scheduleConfigRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Cấu hình lịch trình không tồn tại!"));
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

        ScheduleConfig config = scheduleConfigRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Cấu hình lịch trình không tồn tại hoặc đã bị xóa trước đó!"));

        scheduleConfigMapper.updateScheduleConfig(config, dto);
        return scheduleConfigMapper.toScheduleConfigDTO(dto, scheduleConfigRepo.save(config));
    }

    @Override
    public long deleteByID(String id) throws ServerException {
        checkPermission(type, PermissionKey.DELETE);
        if (scheduleConfigRepo.countByID(id) == 0) {
            throw new NotFoundException("Cấu hình lịch trình không tồn tại!");
        }
        long numOfRowsDeleted = scheduleConfigRepo.delete(id);
        if (numOfRowsDeleted == 0) {
            throw new ServerException("Đã có lỗi xảy ra. Vui lòng thử lại sau!");
        }
        return numOfRowsDeleted;
    }

    @Override
    public List<ScheduleConfig> importScheduleConfigData(MultipartFile importFile) {

        checkPermission(type, PermissionKey.WRITE);

        Workbook workbook = FileFactory.getWorkbookStream(importFile);
        List<ScheduleConfigDTO> scheduleConfigDTOList = ExcelUtils.getImportData(workbook, ImportConfig.scheduleConfigImport);

        List<ScheduleConfig> scheduleConfig = scheduleConfigMapper.toScheduleConfigList(scheduleConfigDTOList);

        return scheduleConfigRepo.saveAll(scheduleConfig);
    }

    @Override
    public ExportExcelResponse exportScheduleConfig() throws Exception {
        List<ScheduleConfigDTO> scheduleConfig = scheduleConfigRepo.getAll();

        if (CollectionUtils.isEmpty(scheduleConfig)) {
            throw new NotFoundException("No data");
        }
        String fileName = "ScheduleConfig Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(scheduleConfig, fileName, ExportConfig.scheduleConfigExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}
