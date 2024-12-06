package com.project.logistic_management_2.service.transaction;

import com.project.logistic_management_2.dto.request.TransactionDTO;
import com.project.logistic_management_2.entity.Goods;
import com.project.logistic_management_2.entity.Transaction;
import com.project.logistic_management_2.exception.def.ConflictException;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.transaction.TransactionMapper;
import com.project.logistic_management_2.repository.goods.GoodsRepo;
import com.project.logistic_management_2.repository.transaction.TransactionRepo;
import com.project.logistic_management_2.service.BaseService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TransactionServiceImpl extends BaseService<TransactionRepo, TransactionMapper> implements TransactionService {

    private final GoodsRepo goodsRepo;

    public TransactionServiceImpl(TransactionRepo repo, TransactionMapper mapper
                                , GoodsRepo goodsRepo) {
        super(repo, mapper);
        this.goodsRepo = goodsRepo;
    }

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {

        Goods goods = goodsRepo.findById(transactionDTO.getGoodsId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy hàng hóa"));

        if(transactionDTO.getOrigin()) {
            goods.setQuantity(goods.getQuantity() + transactionDTO.getQuantity());
            goodsRepo.save(goods);
        } else {
            if(goods.getQuantity() < transactionDTO.getQuantity()) {
                throw new ConflictException("Không đủ " + goods.getName() + "trong kho");
            }
            goods.setQuantity(goods.getQuantity() - transactionDTO.getQuantity());
            goodsRepo.save(goods);
        }

        Transaction transaction = mapper.toTransaction(transactionDTO);
        repository.save(transaction);

        return mapper.toTransactionDTO(transaction);
    }

    @Override
    public TransactionDTO updateTransaction(String id, TransactionDTO transactionDTO) {

        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin giao dịch"));

        if(transaction.getOrigin() == transactionDTO.getOrigin()){
            Goods goodsOld = goodsRepo.findById(transaction.getGoodsId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy hàng hóa"));

            if(transaction.getOrigin()) {
                goodsOld.setQuantity(goodsOld.getQuantity() - transaction.getQuantity());
                goodsRepo.save(goodsOld);
                Goods goodsNew = goodsRepo.findById(transactionDTO.getGoodsId())
                        .orElseThrow(() -> new NotFoundException("Không tìm thấy hàng hóa"));
                goodsNew.setQuantity(goodsNew.getQuantity() + transaction.getQuantity());
                goodsRepo.save(goodsNew);
            }else {
                goodsOld.setQuantity(goodsOld.getQuantity() + transaction.getQuantity());
                goodsRepo.save(goodsOld);
                Goods goodsNew = goodsRepo.findById(transactionDTO.getGoodsId())
                        .orElseThrow(() -> new NotFoundException("Không tìm thấy hàng hóa"));
                if(goodsNew.getQuantity() < transactionDTO.getQuantity()) {
                    throw new ConflictException("Không đủ " + goodsNew.getName() + "trong kho");
                }
                goodsNew.setQuantity(goodsNew.getQuantity() - transaction.getQuantity());
                goodsRepo.save(goodsNew);
            }
        } else {
            throw new ConflictException("Không được cập nhật kiểu giao dịch");
        }

        mapper.updateTransaction(transaction,transactionDTO);
        repository.save(transaction);

        return mapper.toTransactionDTO(transaction);
    }

    @Override
    public TransactionDTO deleteTransaction(String id) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin giao dịch"));
        mapper.deleteTransaction(transaction);
        repository.save(transaction);
        return mapper.toTransactionDTO(transaction);
    }

    @Override
    public List<TransactionDTO> getTransactionByFilter(String wareHouseId, Boolean origin, Timestamp fromDate, Timestamp toDate) {
        List<Transaction> transactions = repository.getTransactionByFilter(wareHouseId, origin, fromDate, toDate);
        return mapper.toTransactionDTOList(transactions);
    }
}
