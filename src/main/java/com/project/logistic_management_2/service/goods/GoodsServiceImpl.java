package com.project.logistic_management_2.service.goods;

import com.project.logistic_management_2.dto.request.GoodsDTO;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.repository.goods.GoodsRepo;
import com.project.logistic_management_2.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl extends BaseService implements GoodsService {

    private final GoodsRepo repository;
    private final PermissionType type = PermissionType.GOODS;

    @Override
    public List<GoodsDTO> getGoodsByFilter(String warehouseId) {
        checkPermission(type, PermissionKey.VIEW);
        return repository.getGoodsByFilter(warehouseId);
    }
}
