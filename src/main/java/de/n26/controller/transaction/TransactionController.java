package de.n26.controller.transaction;

import de.n26.data.entity.Transaction;
import de.n26.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/transaction", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    CreateTransactionValidator createTransactionValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // Setting custom validator for CreateTransactionCommand.class
        binder.setValidator(createTransactionValidator);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity createTransaction(@PathVariable("id") Long id, @Valid @RequestBody CreateTransactionCommand cmd, BindingResult result) {
        if (result.hasErrors()) {
            return buildBadRequestResponse(result);
        }
        cmd.setId(id);
        transactionService.createTransaction(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Transaction getTransaction(@PathVariable("id") Long id) {
        return transactionService.getTransaction(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Long> getTransactionIdsByType(@RequestParam("type") String type) {
        return transactionService.getTransactionIdsByType(type);
    }

    @RequestMapping(value = "/sum/{parentId}", method = RequestMethod.GET)
    @ResponseBody
    public SumResponse sumAmountByParentId(@PathVariable("parentId") Long parentId) {
        return new SumResponse(transactionService.sumAmountByParentId(parentId));
    }

    private ResponseEntity<List<String>> buildBadRequestResponse(BindingResult result) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(result.getAllErrors()
                        .stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.toList()));
    }
}
