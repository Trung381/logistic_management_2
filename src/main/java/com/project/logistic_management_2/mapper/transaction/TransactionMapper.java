package com.project.logistic_management_2.mapper.transaction;

import com.project.logistic_management_2.dto.transaction.TransactionDTO;
import com.project.logistic_management_2.entity.transaction.Transaction;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TransactionMapper {
    public Transaction toTransaction(TransactionDTO dto) {
        if (dto == null) return null;
        return createTransaction(dto);
    }

    public List<Transaction> toTransactions(List<TransactionDTO> dtos) {
        if(dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::createTransaction).collect(Collectors.toList());
    }

    private Transaction createTransaction(TransactionDTO dto) {
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
}
