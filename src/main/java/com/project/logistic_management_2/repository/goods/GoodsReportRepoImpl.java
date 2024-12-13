package com.project.logistic_management_2.repository.goods;

import com.project.logistic_management_2.dto.request.GoodsReportDTO;
import com.project.logistic_management_2.entity.GoodsReport;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.List;

import static com.project.logistic_management_2.entity.QGoods.goods;
import static com.project.logistic_management_2.entity.QGoodsReport.goodsReport;
import static com.project.logistic_management_2.entity.QTransaction.transaction;

@Repository
public class GoodsReportRepoImpl extends BaseRepo implements GoodsReportRepoCustom {
    public GoodsReportRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public GoodsReport getGoodReportByYearMonth(String goodsId, YearMonth yearMonth) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(goodsReport.goodsId.eq(goodsId));

        Timestamp startOfMonth = Timestamp.valueOf(yearMonth.atDay(1).atStartOfDay());
        Timestamp endOfMonth = Timestamp.valueOf(yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999));

        builder.and(goodsReport.createdAt.goe(startOfMonth))
                .and(goodsReport.createdAt.loe(endOfMonth));

        return query.from(goodsReport)
                .where(builder)
                .select(goodsReport)
                .fetchOne();
    }

    @Override
    public List<GoodsReportDTO> getGoodReportDTOByYearMonth(YearMonth yearMonth) {
        BooleanBuilder builder = new BooleanBuilder();

        Timestamp startOfMonth = Timestamp.valueOf(yearMonth.atDay(1).atStartOfDay());
        Timestamp endOfMonth = Timestamp.valueOf(yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999));

        builder.and(goodsReport.createdAt.goe(startOfMonth))
                .and(goodsReport.createdAt.loe(endOfMonth));

        ConstructorExpression<GoodsReportDTO> expression = Projections.constructor(GoodsReportDTO.class,
                goodsReport.id.as("id"),
                goodsReport.goodsId.as("goodsId"),
                JPAExpressions.select(goods.name.as("goodsName"))
                        .from(goods)
                        .where(goods.id.eq(goodsReport.goodsId)),
                goodsReport.beginningInventory.as("beginingInventoryQuantity"),
                JPAExpressions.select((goods.amount.multiply(goodsReport.beginningInventory)).as("beginingInventoryTotalAmount"))
                        .from(goods)
                        .where(goods.id.eq(goodsReport.goodsId)),
                goodsReport.endingInventory.as("endingInventoryQuantity"),
                JPAExpressions.select((goods.amount.multiply(goodsReport.endingInventory)).as("endingInventoryTotalAmount"))
                        .from(goods)
                        .where(goods.id.eq(goodsReport.goodsId)),
                JPAExpressions.select((transaction.quantity.sum()).as("inboundTransactionQuantity"))
                        .from(transaction)
                        .where(transaction.origin.eq(true)
                                .and(transaction.goodsId.eq(goodsReport.goodsId))),
                JPAExpressions.select(
                                transaction.quantity.sum()
                                        .multiply(
                                                JPAExpressions.select(goods.amount)
                                                        .from(goods)
                                                        .where(goods.id.eq(transaction.goodsId))
                                        ).as("inboundTransactionTotalAmount")
                        )
                        .from(transaction)
                        .where(transaction.origin.eq(true)
                                .and(transaction.goodsId.eq(goodsReport.goodsId))),
                JPAExpressions.select((transaction.quantity.sum()).as("outboundTransactionQuantity"))
                        .from(transaction)
                        .where(transaction.origin.eq(false)
                                .and(transaction.goodsId.eq(goodsReport.goodsId))),
                JPAExpressions.select(
                                transaction.quantity.sum()
                                        .multiply(
                                                JPAExpressions.select(goods.amount)
                                                        .from(goods)
                                                        .where(goods.id.eq(transaction.goodsId))
                                        ).as("outboundTransactionTotalAmount")
                        )
                        .from(transaction)
                        .where(transaction.origin.eq(false)
                                .and(transaction.goodsId.eq(goodsReport.goodsId)))
                ,
                JPAExpressions.select(goods.amount.as("unitAmount"))
                        .from(goods)
                        .where(goods.id.eq(goodsReport.goodsId)),
                goodsReport.createdAt.as("createdAt")
                );

        return query.from(goodsReport)
                .where(builder)
                .select(expression)
                .fetch();
    }
}
