package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account[] findAll() {
        String sql = "SELECT * " +
                     "FROM account";
        return null;
    }

    @Override
    public Account findByUserId(long userId) {
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        Account account = null;

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId);
        if (sqlRowSet.next()) {
            account = mapRowToAccount(sqlRowSet);
        }
        return account;
    }

    @Override
    public boolean performTransferOnAccounts(Transfer transfer){
        String sql = "BEGIN; " +
                     "UPDATE account SET balance = balance - ? " +
                     "WHERE account_id = ?; " +
                     "UPDATE account SET balance = balance + ? " +
                     "WHERE account_id = ?; " +
                     "UPDATE transfer SET transfer_status_id = 2 " +
                     "WHERE transfer_id = ?; " +
                     "COMMIT;";
        jdbcTemplate.update(sql, transfer.getAmount(),
                transfer.getAccountFrom(),
                transfer.getAmount(),
                transfer.getAccountTo(),
                transfer.getTransferId());
        return true;
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        return new Account(rowSet.getLong("account_id"), rowSet.getLong("user_id"), rowSet.getBigDecimal("balance"));
    }
}
