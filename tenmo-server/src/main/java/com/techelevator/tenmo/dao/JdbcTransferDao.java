package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long create(Transfer transfer) {
        Long transferID = new Long(-1);
        String sql = "INSERT INTO transfer(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                     "VALUES(2, 2, ?, ?, ?) " +
                     "RETURNING transfer_id;";
        transferID = jdbcTemplate.queryForObject(sql, Long.class, transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        return transferID;
    }

    @Override
    public Transfer findById(long transferId) {
        return null;
    }

    @Override
    public List<Transfer> findByAccountId(long accountId) {
        return null;
    }
}
