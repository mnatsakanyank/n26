package de.n26.controller.transaction;

import de.n26.data.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CreateTransactionValidator implements Validator {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateTransactionCommand.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateTransactionCommand cmd = (CreateTransactionCommand) target;

        if (cmd.getType() == null) {
            errors.rejectValue("type", "1", "Transaction type can not be null");
        }

        if (cmd.getAmount() == null) {
            errors.rejectValue("amount", "2", "Transaction amount can not be null");
        }

        if (cmd.getParentId() != null && transactionRepository.findOne(cmd.getParentId()) == null) {
            errors.rejectValue("parentId", "3", "Invalid parent id");
        }
    }
}
