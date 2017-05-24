package de.n26.controller.transaction;

import de.n26.data.entity.Transaction;
import de.n26.data.repository.TransactionRepository;
import de.n26.exception.TransactionExistException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionControllerTest {

    @Autowired
    TransactionController transactionController;

    @Autowired
    TransactionRepository transactionRepository;

    @Before
    public void setup() {
        buildAndSaveTransaction(1L, null, "testType", 10.0);
        buildAndSaveTransaction(2L, null, "testType", 20.0);
        buildAndSaveTransaction(3L, 1L, "testType", 30.0);
        buildAndSaveTransaction(4L, 1L, "testType1", 40.0);
    }

    @Test(expected = NoSuchElementException.class)
    public void getTransaction_invalidId_throwsException() {
        transactionController.getTransaction(10L);
    }

    @Test
    public void getTransaction_validId_returnsTransaction() {
        Transaction t = transactionController.getTransaction(1L);

        assertThat(t.getAmount()).isEqualTo(10.0);
    }

    @Test(expected = TransactionExistException.class)
    public void createTransaction_existingId_throwsException() {
        CreateTransactionCommand cmd = buildCreateTransactionCommand(1L, 10.0, 2L, "testType3");
        BindingResult errors = new BeanPropertyBindingResult(cmd, "createTransactionCommand");
        transactionController.createTransaction(1L, cmd, errors);
    }

    @Test
    public void createTransaction_properCommand_successfullySaved() {
        CreateTransactionCommand cmd = buildCreateTransactionCommand(12L, 10.0, 3L, "testTypeWhatever");
        BindingResult errors = new BeanPropertyBindingResult(cmd, "createTransactionCommand");

        ResponseEntity rs = transactionController.createTransaction(cmd.getId(), cmd, errors);
        assertThat(rs.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Transaction tr = transactionController.getTransaction(12L);

        assertThat(tr.getAmount()).isEqualTo(cmd.getAmount());
        assertThat(tr.getId()).isEqualTo(cmd.getId());
        assertThat(tr.getParentId()).isEqualTo(cmd.getParentId());
        assertThat(tr.getType()).isEqualTo(cmd.getType());

    }


    @Test
    public void getTransactionIdsByType_validTypes_returnsList() {
        List<Long> ids = transactionController.getTransactionIdsByType("testType");
        assertThat(ids).hasSize(3).contains(1L, 2L, 3L);

        ids = transactionController.getTransactionIdsByType("testType1");
        assertThat(ids).hasSize(1).contains(4L);

        ids = transactionController.getTransactionIdsByType("wrongType");
        assertThat(ids).hasSize(0);
    }

    @Test
    public void getTransactionIdsByType_invalidType_returnsEmptyList() {
        List<Long> ids = transactionController.getTransactionIdsByType("wrongType");

        assertThat(ids).hasSize(0);
    }

    @Test
    public void sumAmountByParentId_validParentId_returnsAmountSum() {
        SumResponse response = transactionController.sumAmountByParentId(1L);

        assertThat(response.getSum()).isEqualTo(70.0);
    }

    @Test
    public void sumAmountByParentId_invalidParentId_returns0() {
        SumResponse response = transactionController.sumAmountByParentId(2L);

        assertThat(response.getSum()).isEqualTo(0);
    }

    private void buildAndSaveTransaction(Long id, Long parentId, String type, Double amount) {
        Transaction tr = new Transaction();
        tr.setId(id);
        tr.setParentId(parentId);
        tr.setAmount(amount);
        tr.setType(type);
        transactionRepository.save(tr);
    }

    private CreateTransactionCommand buildCreateTransactionCommand(Long id, Double amount, Long parentId, String type) {
        CreateTransactionCommand cmd = new CreateTransactionCommand();
        cmd.setId(id);
        cmd.setAmount(amount);
        cmd.setParentId(parentId);
        cmd.setType(type);
        return cmd;
    }
}