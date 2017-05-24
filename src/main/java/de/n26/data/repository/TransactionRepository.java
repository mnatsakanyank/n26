package de.n26.data.repository;

import de.n26.data.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.NoSuchElementException;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findByParentId(Long parentId);

    List<Transaction> findByType(String type);

    default Transaction findOneWithException(Long id) {
        Transaction tr = findOne(id);
        if (tr == null) {
            throw new NoSuchElementException("Cant find transaction with id: " + id);
        }
        return tr;
    }
}
