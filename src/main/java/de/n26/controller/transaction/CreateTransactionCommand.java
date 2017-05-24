package de.n26.controller.transaction;

import de.n26.data.entity.Transaction;

public class CreateTransactionCommand {

    private Long id;
    private Long parentId;
    private Double amount;
    private String type;

    public Transaction toTransaction() {
        Transaction tr = new Transaction();
        tr.setId(this.getId());
        tr.setAmount(this.getAmount());
        tr.setParentId(this.getParentId());
        tr.setType(this.getType());
        return tr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
