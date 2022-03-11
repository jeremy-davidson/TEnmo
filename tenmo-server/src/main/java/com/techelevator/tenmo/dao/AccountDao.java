package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

public interface AccountDao {

    Account[] findAll();

    Account findByUserId(long userId);

    boolean performTransferOnAccounts(Transfer transfer);

}
