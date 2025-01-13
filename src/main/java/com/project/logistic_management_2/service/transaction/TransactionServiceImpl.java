package com.project.logistic_management_2.service.transaction;

import com.mysema.commons.lang.Pair;
import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.transaction.TransactionDTO;
import com.project.logistic_management_2.entity.Goods;
import com.project.logistic_management_2.entity.Transaction;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.exception.def.ConflictException;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.transaction.TransactionMapper;
import com.project.logistic_management_2.repository.goods.GoodsRepo;
import com.project.logistic_management_2.repository.transaction.TransactionRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.ExportConfig;
import com.project.logistic_management_2.utils.FileFactory;
import com.project.logistic_management_2.utils.ImportConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.project.logistic_management_2.utils.Utils.parseAndValidateDates;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl extends BaseService implements TransactionService {

    private final GoodsRepo goodsRepo;
    private final TransactionRepo repository;
    private final TransactionMapper mapper;
    private final PermissionType type = PermissionType.TRANSACTIONS;

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {

        checkPermission(type, PermissionKey.WRITE);

        Goods goods = goodsRepo.findById(transactionDTO.getGoodsId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy hàng hóa"));

        if (transactionDTO.getOrigin().getValue()) {
            goods.setQuantity(goods.getQuantity() + transactionDTO.getQuantity());
            goodsRepo.save(goods);
        } else {
            if (goods.getQuantity() < transactionDTO.getQuantity()) {
                throw new ConflictException("Không đủ " + goods.getName() + "trong kho");
            }
            goods.setQuantity(goods.getQuantity() - transactionDTO.getQuantity());
            goodsRepo.save(goods);
        }

        Transaction transaction = mapper.toTransaction(transactionDTO);
        repository.save(transaction);

        return repository.getTransactionsById(transaction.getId()).get();
    }


    @Override
    public Optional<TransactionDTO> updateTransaction(String id, TransactionDTO dto) {
        checkPermission(type, PermissionKey.WRITE);

        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin giao dịch"));

        Goods goods = goodsRepo.findById(transaction.getGoodsId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy hàng hóa"));

        if (dto.getOrigin() != null && !transaction.getOrigin().equals(dto.getOrigin().getValue())) {
            throw new ConflictException("Không được cập nhật kiểu giao dịch");
        }

        if (dto.getQuantity() != null) {
            if (transaction.getOrigin()) {
                goods.setQuantity(goods.getQuantity() - transaction.getQuantity() + dto.getQuantity());
            } else if (goods.getQuantity() < dto.getQuantity()) {
                throw new ConflictException("Không đủ hàng trong kho");
            } else {
                goods.setQuantity(goods.getQuantity() - transaction.getQuantity() + dto.getQuantity());
            }
        }

        goodsRepo.save(goods);

        long updatedCount = repository.updateTransaction(transaction, id, dto);
        if (updatedCount == 0) {
            throw new NotFoundException("Cập nhật giao dịch thất bại");
        }

        return repository.getTransactionsById(id);
    }

    @Override
    public String deleteTransaction(String id) {

        checkPermission(type, PermissionKey.DELETE);

        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy giao dịch"));
        if (repository.deleteTransaction(id) > 0) {
            return "Xóa thành công";
        } else {
            throw new ConflictException("Xóa thất bại");
        }
    }

    @Override
    public TransactionDTO getTransactionById(String id) {

        checkPermission(type, PermissionKey.VIEW);

        return repository.getTransactionsById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin giao dịch!"));
    }

    @Override
    public List<TransactionDTO> getTransactionByFilter(int page, String warehouseId, Boolean origin, String fromDate, String toDate) {

        checkPermission(type, PermissionKey.VIEW);

        Pair<Timestamp, Timestamp> dateRange = parseAndValidateDates(fromDate, toDate);

        return repository.getTransactionByFilter(page, warehouseId, origin, dateRange.getFirst(), dateRange.getSecond());
    }

    @Override
    public List<Transaction> importTransactionData(MultipartFile importFile) {

        checkPermission(type, PermissionKey.WRITE);

        Workbook workbook = FileFactory.getWorkbookStream(importFile);
        List<TransactionDTO> transactionDTOList = ExcelUtils.getImportData(workbook, ImportConfig.transactionImport);

        List<Transaction> transactions = mapper.toTransactions(transactionDTOList);

        for (Transaction transaction : transactions) {
            Goods goods = goodsRepo.findById(transaction.getGoodsId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy hàng hóa"));

            if (transaction.getOrigin()) {
                goods.setQuantity(goods.getQuantity() + transaction.getQuantity());
                goodsRepo.save(goods);
            } else {
                if (goods.getQuantity() < transaction.getQuantity()) {
                    throw new ConflictException("Không đủ " + goods.getName() + "trong kho");
                }
                goods.setQuantity(goods.getQuantity() - transaction.getQuantity());
                goodsRepo.save(goods);
            }
        }
        // Lưu tất cả các thực thể vào cơ sở dữ liệu và trả về danh sách đã lưu
        return repository.saveAll(transactions);
    }

    @Override
    public ExportExcelResponse exportTransaction(int page, String warehouseId, Boolean origin, String fromDate, String toDate) throws Exception {

        Pair<Timestamp, Timestamp> dateRange = parseAndValidateDates(fromDate, toDate);

        List<TransactionDTO> transactions = repository.getTransactionByFilter(page, warehouseId, origin, dateRange.getFirst(), dateRange.getSecond());

        if (CollectionUtils.isEmpty(transactions)) {
            throw new NotFoundException("No data");
        }
        String fileName = "Transactions Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(transactions, fileName, ExportConfig.transactionExport);
        InputStreamResource resource = new InputStreamResource(in);

        return new ExportExcelResponse(fileName, resource);
    }
}
