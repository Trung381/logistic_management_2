package com.project.logistic_management_2.service.truck;

import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.dto.truck.TruckDTO;
import com.project.logistic_management_2.entity.schedule.Schedule;
import com.project.logistic_management_2.entity.truck.Truck;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.enums.role.UserRole;
import com.project.logistic_management_2.enums.truck.TruckType;
import com.project.logistic_management_2.exception.define.ConflictException;
import com.project.logistic_management_2.exception.define.NotFoundException;
import com.project.logistic_management_2.mapper.schedule.ScheduleMapper;
import com.project.logistic_management_2.mapper.truck.TruckMapper;
import com.project.logistic_management_2.repository.schedule.schedule.ScheduleRepo;
import com.project.logistic_management_2.repository.truck.TruckRepo;
import com.project.logistic_management_2.repository.user.UserRepo;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TruckServiceImpl extends BaseService implements TruckService {

    private final TruckRepo repository;
    private final TruckMapper mapper;
    private final PermissionType type = PermissionType.TRUCKS;
    private final UserRepo userRepo;
    private final ScheduleRepo scheduleRepo;
    private final ScheduleMapper scheduleMapper;

    @Override
    public TruckDTO createTruck(TruckDTO truckDTO) {
        checkPermission(type, PermissionKey.WRITE);
        checkDriverRole(userRepo.getUserById(truckDTO.getDriverId()).getRoleId());

        Truck truck = mapper.toTruck(truckDTO);
        repository.save(truck);
        return repository.getTruckById(truck.getId()).get();
    }

    @Override
    public List<TruckDTO> getAllTrucks() {
        checkPermission(type, PermissionKey.VIEW);
        return repository.getAllTrucks();
    }

    @Override
    public List<TruckDTO> getTrucksByType(TruckType truckType) {
        checkPermission(type, PermissionKey.VIEW);
        List<TruckDTO> trucks = repository.getTrucksByType(truckType.getValue());
        if (trucks.isEmpty()) {
            throw new NotFoundException("Loại xe " + truckType.getDescription() + " không tồn tại!");
        }
        return trucks;
    }

    @Override
    public TruckDTO getTruckByLicensePlate(String licensePlate) {
        checkPermission(type, PermissionKey.VIEW);
        return repository.getTruckByLicense(licensePlate)
                .orElseThrow(() -> new NotFoundException("Xe có biển số " + licensePlate + " không tồn tại!"));
    }

@Override
@Transactional
public TruckDTO updateTruck(Integer id, TruckDTO dto) {
    checkPermission(type, PermissionKey.WRITE);
    // Lấy thông tin xe hiện tại
    Truck existingTruck = mapper.toTruck(repository.getTruckById(id)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin xe cần tìm!")));
    // Cập nhật các trường từ body (chỉ cập nhật nếu không null)
    if (dto.getDriverId() != null) {
        existingTruck.setDriverId(dto.getDriverId());
    }
    if (dto.getLicensePlate() != null) {
        // Kiểm tra xem license_plate có bị trùng không
        if (repository.existsByLicensePlate(dto.getLicensePlate()) &&
                !existingTruck.getLicensePlate().equals(dto.getLicensePlate())) {
            throw new IllegalArgumentException("Biển số xe đã tồn tại!");
        }
        String oldLicense = existingTruck.getLicensePlate();
        existingTruck.setLicensePlate(dto.getLicensePlate());

        List<ScheduleDTO> schedules = scheduleRepo.findByLicensePlate(oldLicense);
        if (!schedules.isEmpty()) {
            for (ScheduleDTO scheduleDTO : schedules) {
                // Lấy bản ghi Schedule hiện tại từ DB
                Schedule existingSchedule = scheduleRepo.findById(scheduleDTO.getId())
                        .orElseThrow(() -> new NotFoundException("Không tìm thấy lịch trình với ID: " + scheduleDTO.getId()));
                if(existingSchedule.getTruckLicense().equals(oldLicense)) {
                    existingSchedule.setTruckLicense(dto.getLicensePlate());
                }
                if(existingSchedule.getMoocLicense().equals(oldLicense)) {
                    existingSchedule.setMoocLicense(dto.getLicensePlate());
                }
                scheduleRepo.save(existingSchedule);
            }
        }
    }
    if (dto.getCapacity() != null) {
        existingTruck.setCapacity(dto.getCapacity());
    }
    if (dto.getNote() != null) {
        existingTruck.setNote(dto.getNote());
    }
    if (dto.getType() != null) {
        existingTruck.setType(dto.getType().getValue());
    }
    if (dto.getStatus() != null) {
        existingTruck.setStatus(dto.getStatus().getValue());
    }
    existingTruck.setUpdatedAt(new Date());
    repository.save(existingTruck);
    return mapper.toTruckDTO(existingTruck);
}


    @Override
    public long deleteTruck(Integer id) {
        checkPermission(type, PermissionKey.DELETE);
        TruckDTO truck = repository.getTruckById(id)
                .orElseThrow(() -> new NotFoundException("Xe cần xóa không tồn tại!"));
        return repository.delete(id);
    }

    @Override
    public List<Truck> importTruckData(MultipartFile importFile) {

        checkPermission(type, PermissionKey.WRITE);

        Workbook workbook = FileFactory.getWorkbookStream(importFile);
        List<TruckDTO> truckDTOList = ExcelUtils.getImportData(workbook, ImportConfig.truckImport);

        for(TruckDTO truckDTO : truckDTOList) {
            checkDriverRole(userRepo.getUserById(truckDTO.getDriverId()).getRoleId());
        }

        List<Truck> trucks = mapper.toTruckList(truckDTOList);

        // Lưu tất cả các thực thể vào cơ sở dữ liệu và trả về danh sách đã lưu
        return repository.saveAll(trucks);
    }

    private void checkDriverRole(int roleID) {
        if (!UserRole.DRIVER.getId().equals(roleID)) {
            throw new ConflictException("Người chịu trách nhiệm phải là tài xế");
        }
    }

    @Override
    public ExportExcelResponse exportTruckData() throws Exception {
        List<TruckDTO> trucks = repository.getAllTrucks();

        if (CollectionUtils.isEmpty(trucks)) {
            throw new NotFoundException("No data");
        }
        String fileName = "Trucks Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(trucks, fileName, ExportConfig.truckExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}

