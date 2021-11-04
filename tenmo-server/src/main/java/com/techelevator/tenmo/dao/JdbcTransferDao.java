package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void balanceTransfer(int accountFrom, int accountTo, BigDecimal amount) {

        BigDecimal accountBalanceFrom = BigDecimal.ZERO;
        BigDecimal accountBalanceTo = BigDecimal.ZERO;

        String sqlBalanceFrom = "SELECT a.balance FROM accounts a JOIN transfer t ON a.account_id = t.account_from WHERE" +
                "account_from = ?;";
        SqlRowSet resultsFrom = jdbcTemplate.queryForRowSet(sqlBalanceFrom, accountFrom);
        if (resultsFrom.next()) {
            accountBalanceFrom = resultsFrom.getBigDecimal("balance");
        }

        String sqlBalanceTo = "SELECT a.balance FROM accounts a JOIN transfer t ON a.account_id = t.account_to WHERE" +
                "account_to = ?;";
        SqlRowSet resultsTo = jdbcTemplate.queryForRowSet(sqlBalanceTo, accountTo);
        if (resultsFrom.next()) {
            accountBalanceTo = resultsTo.getBigDecimal("balance");
        }

        if (accountBalanceFrom.compareTo(amount) > 0) {

            accountBalanceFrom = accountBalanceFrom.subtract(amount);
            accountBalanceTo = accountBalanceTo.add(amount);

            updateAccountBalance(accountBalanceFrom, accountFrom);
            updateAccountBalance(accountBalanceTo, accountTo);

            Transfer newTransfer = new Transfer();

            createTransfer(newTransfer);
            updateTransferStatus(newTransfer.getTransferId());
        }
    }

    @Override
    public List<Transfer> getTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to," +
                "amount FROM transfers;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to," +
                "amount FROM transfers WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public Transfer createTransfer(Transfer newTransfer) {

        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        int newId = jdbcTemplate.queryForObject(sql, int.class, newTransfer.getTransferTypeId(), newTransfer.getTransferStatusId(),
                newTransfer.getAccountFrom(), newTransfer.getAccountTo(), newTransfer.getAmount());

        return getTransferById(newId);
    }

    public Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setAccountFrom(rowSet.getInt("transfer_type_id"));
        transfer.setAccountTo(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }

    public void updateAccountBalance(BigDecimal newBalance, int accountId) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, newBalance, accountId);
    }
    
    public void updateTransferStatus(int transferId){
        String sql = "UPDATE transfers SET transfer_status_id = (SELECT transfer_status_id FROM transfer_statuses WHERE transfer_status_desc = 'Approved')" +
                "WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, transferId);
    }
}
