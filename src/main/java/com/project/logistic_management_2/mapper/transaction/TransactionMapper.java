package com.project.logistic_management_2.mapper.transaction;

import com.project.logistic_management_2.dto.request.TransactionDTO;
import com.project.logistic_management_2.entity.Transaction;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.utils.Utils;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class TransactionMapper {
    public Transaction toTransaction(TransactionDTO dto) {
        if (dto == null) return null;

        return Transaction.builder()
                .id(Utils.genID(IDKey.EXPENSES))
                .refUserId(dto.getRefUserId())
                .customerName(dto.getCustomerName())
                .goodsId(dto.getGoodsId())
                .quantity(dto.getQuantity())
                .transactionTime(dto.getTransactionTime())
                .origin(dto.getOrigin())
                .destination(dto.getDestination())
                .image(dto.getImage())
                .deleted(false)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

    public void updateTransaction(Transaction transaction, TransactionDTO dto) {
        if (transaction == null || dto == null) return;
        transaction.setRefUserId(dto.getRefUserId());
        transaction.setCustomerName(dto.getCustomerName());
        transaction.setGoodsId(dto.getGoodsId());
        transaction.setQuantity(dto.getQuantity());
        transaction.setTransactionTime(dto.getTransactionTime());
        transaction.setDestination(dto.getDestination());
        transaction.setImage(dto.getImage());
        transaction.setUpdatedAt(new Date());
    }

}
