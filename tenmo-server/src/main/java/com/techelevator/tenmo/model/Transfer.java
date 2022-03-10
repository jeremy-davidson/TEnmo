package com.techelevator.tenmo.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class Transfer {
    //Instance Properties
    private long transferId;

    @Min(1)
    @Max(2)
    private int transferType;

    @Min(1)
    @Max(3)
    private int transferStatus;
    private long accountFrom;
    private long accountTo;

    @DecimalMin("0.01")
    private BigDecimal amount;

    //Getters and Setters
    public long getTransferId() {
        return transferId;
    }

    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }

    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }

    public int getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(int transferStatus) {
        this.transferStatus = transferStatus;
    }

    public long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(long accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
