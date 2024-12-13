package com.project.logistic_management_2.repository.goods;

import com.project.logistic_management_2.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsRepo extends JpaRepository<Goods, String>, GoodsRepoCustom {

}
