package com.project.logistic_management_2.service.warehouses;

import com.project.logistic_management_2.dto.warehouse.WarehousesDTO;
import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.entity.warehouse.Warehouses;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.exception.define.NotFoundException;
import com.project.logistic_management_2.mapper.warehouses.WarehousesMapper;
import com.project.logistic_management_2.repository.warehouses.WarehousesRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.ExportConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehousesServiceImpl extends BaseService implements WarehousesService {

    private final WarehousesRepo repository;
    private final WarehousesMapper mapper;
    private final PermissionType type = PermissionType.WAREHOUSES;

    @Override
    public List<WarehousesDTO> getAllWarehouses() {

        checkPermission(type, PermissionKey.VIEW);
        List<WarehousesDTO> warehousesList = repository.getAll();
        return warehousesList;
    }

    @Override
    public ExportExcelResponse exportWarehouses() throws Exception {
        List<Warehouses> warehousesList = repository.findAll();
        List<WarehousesDTO> warehouses = mapper.toWarehouseDTOList(warehousesList);

        if (CollectionUtils.isEmpty(warehouses)) {
            throw new NotFoundException("No data");
        }
        String fileName = "Warehouses Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(warehouses, fileName, ExportConfig.warehouseExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}
