package de.n26.controller.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SumResponse {

    @JsonProperty
    private Double sum;

    public SumResponse(Double sum) {
        this.sum = sum;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }
}
