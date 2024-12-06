package com.project.logistic_management_2.service.goods;

import com.project.logistic_management_2.dto.request.GoodsDTO;
import com.project.logistic_management_2.entity.Goods;
import com.project.logistic_management_2.mapper.goods.GoodsMapper;
import com.project.logistic_management_2.repository.goods.GoodsRepo;
import com.project.logistic_management_2.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl extends BaseService<GoodsRepo, GoodsMapper> implements GoodsService {
    public GoodsServiceImpl(GoodsRepo repo, GoodsMapper mapper) {
        super(repo, mapper);
    }

    @Override
    public List<GoodsDTO> getAllGoods() {
        List<Goods> goodsList = repository.findAll();
        return mapper.toGoodsDTOList(goodsList);
    }
}
