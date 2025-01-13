package com.project.logistic_management_2.service.expenses.expensesconfig;

import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.expenses.ExpensesConfigDTO;
import com.project.logistic_management_2.entity.ExpensesConfig;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.exception.define.InvalidParameterException;
import com.project.logistic_management_2.exception.define.NotFoundException;
import com.project.logistic_management_2.mapper.expenses.ExpensesConfigMapper;
import com.project.logistic_management_2.repository.expenses.expensesconfig.ExpensesConfigRepo;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpensesConfigServiceImpl extends BaseService implements ExpensesConfigService {
    private final ExpensesConfigRepo expensesConfigRepo;
    private final ExpensesConfigMapper expensesConfigMapper;
    private final PermissionType type = PermissionType.CONFIGS;

    /**
     * Get all expenses configurations
     *
     * @return all expenses configs
     */
    @Override
    public List<ExpensesConfigDTO> getAll() {
        checkPermission(type, PermissionKey.VIEW);
        return expensesConfigRepo.getAll();
    }

    /**
     * Get expenses configuration by id
     *
     * @param id: String
     * @return expenses with id
     */
    @Override
    public ExpensesConfigDTO getByID(String id) {
        checkPermission(type, PermissionKey.VIEW);
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }
        ExpensesConfig config = expensesConfigRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin cấu hình chi phí!"));
        return expensesConfigMapper.toExpensesConfigDTO(null, config);
    }

    @Override
    public ExpensesConfigDTO create(ExpensesConfigDTO dto) {
        checkPermission(type, PermissionKey.WRITE);
        ExpensesConfig config = expensesConfigMapper.toExpensesConfig(dto);
        expensesConfigRepo.save(config);
        return expensesConfigMapper.toExpensesConfigDTO(dto, config);
    }

    @Override
    public ExpensesConfigDTO update(String id, ExpensesConfigDTO dto) {
        checkPermission(type, PermissionKey.WRITE);

        ExpensesConfig config = expensesConfigRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Cấu hình chi phí không tồn tại!"));

        expensesConfigMapper.updateExpensesConfig(config, dto);

        //save to DB
        return expensesConfigMapper.toExpensesConfigDTO(dto, expensesConfigRepo.save(config));
    }

    @Override
    public long deleteByID(String id) {
        checkPermission(type, PermissionKey.DELETE);

        long numOfRowDeleted = expensesConfigRepo.delete(id);
        if (numOfRowDeleted == 0) {
            throw new NotFoundException("Cấu hình chi phí không tồn tại!");
        }
        return numOfRowDeleted;
    }

    @Override
    public List<ExpensesConfig> importExpensesConfigData(MultipartFile importFile) {

        checkPermission(type, PermissionKey.WRITE);

        Workbook workbook = FileFactory.getWorkbookStream(importFile);
        List<ExpensesConfigDTO> expensesConfigDTOList = ExcelUtils.getImportData(workbook, ImportConfig.expensesConfigImport);

        List<ExpensesConfig> expensesConfigs = expensesConfigMapper.toExpensesConfigList(expensesConfigDTOList);

        // Lưu tất cả các thực thể vào cơ sở dữ liệu và trả về danh sách đã lưu
        return expensesConfigRepo.saveAll(expensesConfigs);
    }

    @Override
    public ExportExcelResponse exportExpensesConfig() throws Exception {
        List<ExpensesConfigDTO> expensesConfig = expensesConfigRepo.getAll();

        if (!CollectionUtils.isEmpty(expensesConfig)) {
            String fileName = "ExpensesConfig Export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.export(expensesConfig, fileName, ExportConfig.expensesConfigExport);

            InputStreamResource inputStreamResource = new InputStreamResource(in);
            return new ExportExcelResponse(fileName, inputStreamResource);
        } else {
            throw new NotFoundException("No data");
        }
    }
}
