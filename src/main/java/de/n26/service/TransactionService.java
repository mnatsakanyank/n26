package de.n26.service;

import de.n26.controller.transaction.CreateTransactionCommand;
import de.n26.data.entity.Transaction;
import de.n26.data.repository.TransactionRepository;
import de.n26.exception.TransactionExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    public void createTransaction(CreateTransactionCommand cmd) {
        validateTransactionId(cmd.getId());
        transactionRepository.save(cmd.toTransaction());
    }

    public Double sumAmountByParentId(Long parentId) {
        List<Transaction> trs = transactionRepository.findByParentId(parentId);
        return trs.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public Transaction getTransaction(Long id) {
        return transactionRepository.findOneWithException(id);
    }

    public List<Long> getTransactionIdsByType(String type) {
        List<Transaction> trs = transactionRepository.findByType(type);
        return trs.stream()
                .map(Transaction::getId)
                .collect(Collectors.toList());
    }

    private void validateTransactionId(Long id) {
        Transaction tr = transactionRepository.findOne(id);
        if (tr != null) {
            throw new TransactionExistException("Transaction with passed id already exists");
        }
    }
}
