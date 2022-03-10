package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    public List<Transfer> findAllByAccountId(long accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * " +
                     "FROM transfer " +
                     "WHERE account_from = ? " +
                     "OR account_to = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while(rowSet.next()){
            transfers.add(mapRowToTransfer(rowSet));
        }
        return null;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet){
        Transfer t = new Transfer();
        t.setTransferId(rowSet.getLong("transfer_id"));
        t.setAccountTo(rowSet.getLong("account_to"));
        t.setAccountFrom(rowSet.getLong("account_from"));
        t.setTransferStatus(rowSet.getInt("transfer_status_id"));
        t.setTransferType(rowSet.getInt("transfer_type_id"));
        t.setAmount(rowSet.getBigDecimal("amount"));
        return t;
    }
}
