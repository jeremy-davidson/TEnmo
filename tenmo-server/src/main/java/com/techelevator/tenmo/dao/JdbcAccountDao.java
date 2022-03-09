package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
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
    public Account findByUserId(int userId) {
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        Account account = null;

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId);
        if (sqlRowSet.next()) {
            account = mapRowToAccount(sqlRowSet);
        }
        return account;
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        return new Account(rowSet.getLong("account_id"), rowSet.getLong("user_id"), rowSet.getBigDecimal("balance"));
    }
}
