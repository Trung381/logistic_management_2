package com.project.logistic_management_2.service.goods;

import com.project.logistic_management_2.dto.request.GoodsDTO;
import com.project.logistic_management_2.entity.Goods;
import com.project.logistic_management_2.mapper.goods.GoodsMapper;
import com.project.logistic_management_2.repository.goods.GoodsRepo;
import com.project.logistic_management_2.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl extends BaseService implements GoodsService {

    private final GoodsRepo repository;
    private final GoodsMapper mapper;

    @Override
    public List<GoodsDTO> getAllGoods() {
        List<Goods> goodsList = repository.findAll();
        return mapper.toGoodsDTOList(goodsList);
    }
}
