package com.project.logistic_management_2.mapper.transaction;

import com.project.logistic_management_2.dto.transaction.TransactionDTO;
import com.project.logistic_management_2.entity.Transaction;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Component
public class TransactionMapper {
    public Transaction toTransaction(TransactionDTO dto) {
        if (dto == null) return null;

        return Transaction.builder()
                .id(Utils.genID(IDKey.TRANSACTION))
                .refUserId(dto.getRefUserId())
                .customerName(dto.getCustomerName())
                .goodsId(dto.getGoodsId())
                .quantity(dto.getQuantity())
                .transactionTime(dto.getTransactionTime())
                .origin(dto.getOrigin().getValue())
                .destination(dto.getDestination())
                .image(dto.getImage())
                .deleted(false)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

    public List<Transaction> toTransactions(List<TransactionDTO> dtos) {
        if(dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }

        return dtos.stream().map(dto ->
            Transaction.builder()
                    .id(Utils.genID(IDKey.TRANSACTION))
                    .refUserId(dto.getRefUserId())
                    .customerName(dto.getCustomerName())
                    .goodsId(dto.getGoodsId())
                    .quantity(dto.getQuantity())
                    .transactionTime(dto.getTransactionTime())
                    .origin(
                            "Nhập hàng".equals(dto.getOriginDescription()) ? true :
                            "Xuất hàng".equals(dto.getOriginDescription()) ? false : null
                    )
                    .destination(dto.getDestination())
                    .image(dto.getImage())
                    .deleted(false)
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .build()
        ).collect(Collectors.toList());
    }


    public void updateTransaction(Transaction transaction, TransactionDTO dto) {
        if (transaction == null || dto == null) return;
        Optional.ofNullable(dto.getRefUserId()).ifPresent(transaction::setRefUserId);
        Optional.ofNullable(dto.getCustomerName()).ifPresent(transaction::setCustomerName);
        Optional.ofNullable(dto.getGoodsId()).ifPresent(transaction::setGoodsId);
        Optional.ofNullable(dto.getQuantity()).ifPresent(transaction::setQuantity);
        Optional.ofNullable(dto.getTransactionTime()).ifPresent(transaction::setTransactionTime);
        Optional.ofNullable(dto.getOrigin()).ifPresent(transaction::setOrigin);
        Optional.ofNullable(dto.getDestination()).ifPresent(transaction::setDestination);
        Optional.ofNullable(dto.getImage()).ifPresent(transaction::setImage);
        transaction.setUpdatedAt(new Date());
    }

}
