package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    //create
    Long create(Transfer transfer);

    //get transfer
    Transfer findById(long transferId);

    //get all transfers
    List<Transfer> findAllByAccountId(long accountId);

}
