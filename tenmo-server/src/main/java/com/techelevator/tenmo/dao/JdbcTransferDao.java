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
    public boolean create(Transfer transfer) {
        String sql = ""
       return false;
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
