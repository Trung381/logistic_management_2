package com.project.logistic_management_2.mapper.transaction;

import com.project.logistic_management_2.dto.request.TransactionDTO;
import com.project.logistic_management_2.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionMapper {
    public Transaction toTransaction(TransactionDTO dto) {
        if (dto == null) return null;

        return Transaction.builder()
                .refUserId(dto.getRefUserId())
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

    public TransactionDTO toTransactionDTO(Transaction transaction) {
        if (transaction == null) return null;

        return TransactionDTO.builder()
                .id(transaction.getId())
                .refUserId(transaction.getRefUserId())
                .goodsId(transaction.getGoodsId())
                .quantity(transaction.getQuantity())
                .transactionTime(transaction.getTransactionTime())
                .origin(transaction.getOrigin())
                .destination(transaction.getDestination())
                .image(transaction.getImage())
                .deleted(transaction.getDeleted())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }

    public List<TransactionDTO> toTransactionDTOList(List<Transaction> transactionList) {
        if (transactionList == null || transactionList.isEmpty()) return null;

        return transactionList.stream().map(transaction ->
                TransactionDTO.builder()
                    .id(transaction.getId())
                    .refUserId(transaction.getRefUserId())
                    .goodsId(transaction.getGoodsId())
                    .quantity(transaction.getQuantity())
                    .transactionTime(transaction.getTransactionTime())
                    .origin(transaction.getOrigin())
                    .destination(transaction.getDestination())
                    .image(transaction.getImage())
                    .deleted(transaction.getDeleted())
                    .createdAt(transaction.getCreatedAt())
                    .updatedAt(transaction.getUpdatedAt())
                    .build()
        ).collect(Collectors.toList());
    }

    public void updateTransaction(Transaction transaction, TransactionDTO dto) {
        if (transaction == null || dto == null) return;
        transaction.setRefUserId(dto.getRefUserId());
        transaction.setGoodsId(dto.getGoodsId());
        transaction.setQuantity(dto.getQuantity());
        transaction.setTransactionTime(dto.getTransactionTime());
        transaction.setDestination(dto.getDestination());
        transaction.setImage(dto.getImage());
        transaction.setUpdatedAt(new Date());
    }

    public void deleteTransaction(Transaction transaction) {
        if (transaction == null) return;
        transaction.setDeleted(true);
    }
}
