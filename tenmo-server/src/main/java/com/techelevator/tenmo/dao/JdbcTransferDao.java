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
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";
        Transfer transferById = jdbcTemplate.queryForObject(sql, Transfer.class, transferId);

        return transferById;
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
        return transfers;
    }

    @Override
    public List<Transfer> getHistoryByAccountId(long accountId){
        List<Transfer> list = new ArrayList<>();
        String sql = "SELECT transfer.transfer_id, " +
                    "tenmo_user.username AS from_user, " +
                    "tenmo_user2.username AS to_user, " +
                    "transfer.amount " +
                    "FROM transfer " +
                    "JOIN account ON account.account_id = transfer.account_from " +
                    "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                    "JOIN account account2 ON account2.account_id = transfer.account_to " +
                    "JOIN tenmo_user tenmo_user2 ON tenmo_user2.user_id = account2.user_id " +
                    "WHERE transfer.account_from = ? " +
                    "OR transfer.account_to = ? " +
                    "ORDER BY transfer.transfer_id;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while(rowSet.next()){
            list.add(mapRowToTransferHistoryItem(rowSet));
        }
        return list;
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

    private Transfer mapRowToTransferHistoryItem(SqlRowSet rowSet){
        Transfer t = new Transfer();
        t.setTransferId(rowSet.getLong("transfer_id"));
        t.setUserNameFrom(rowSet.getString("from_user"));
        t.setUserNameTo(rowSet.getString("to_user"));
        t.setAmount(rowSet.getBigDecimal("amount"));
        return t;
    }
}
