package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDao implements TransferDao {

    private AccountDao accountDao;
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(AccountDao accountDao, JdbcTemplate jdbcTemplate) {

        this.accountDao = accountDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer sendTransfer(int accountFrom, int accountTo, BigDecimal amount) {

        Transfer newTransfer = null;

        if (accountDao.getBalanceByAccountId(accountFrom).compareTo(amount) >= 0) {

            accountDao.subtractFromBalance(accountFrom, amount);
            accountDao.addToBalance(accountTo, amount);

            newTransfer = new Transfer(accountFrom, accountTo, amount);
            createTransfer(newTransfer);

            updateTransferTypeToSend(newTransfer.getTransferId());
            updateTransferStatusToApproved(newTransfer.getTransferId());


            System.out.println("Transfer successful.");

        } System.out.println("Insufficient funds.");

        return newTransfer;
    }

    @Override
    public List<Transfer> getAllTransfers() {

        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to," +
                "t.amount, tt.transfer_type_desc, ts.transfer_status_desc, " +
                "ut.username AS usernamefrom, uf.username AS usernameto " +
                "FROM transfers t JOIN transfer_types tt ON tt.transfer_type_id = t.transfer_type_id " +
                "JOIN transfer_statuses ts ON ts.transfer_status_id = t.transfer_status_id " +
                "JOIN accounts af ON t.account_from = af.account_id " +
                "JOIN user uf ON af.user_id = uf.user_id " +
                "JOIN accounts at ON t.account_to = at.account_id " +
                "JOIN user ut ON at.user_id = ut.user_id ";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public Transfer getTransferById(int transferId) {

        Transfer transfer = null;
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to," +
                "t.amount, tt.transfer_type_desc, ts.transfer_status_desc, " +
                "ut.username AS usernamefrom, uf.username AS usernameto " +
                "FROM transfers t JOIN transfer_types tt ON tt.transfer_type_id = t.transfer_type_id " +
                "JOIN transfer_statuses ts ON ts.transfer_status_id = t.transfer_status_id " +
                "JOIN accounts af ON t.account_from = af.account_id " +
                "JOIN user uf ON af.user_id = uf.user_id " +
                "JOIN accounts at ON t.account_to = at.account_id " +
                "JOIN user ut ON at.user_id = ut.user_id " +
                "WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public List<Transfer> getAllTransferByUSerId(int userId) {

        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to," +
                "t.amount, tt.transfer_type_desc, ts.transfer_status_desc, " +
                "ut.username AS usernamefrom, uf.username AS usernameto " +
                "FROM transfers t JOIN transfer_types tt ON tt.transfer_type_id = t.transfer_type_id " +
                "JOIN transfer_statuses ts ON ts.transfer_status_id = t.transfer_status_id " +
                "JOIN accounts af ON t.account_from = af.account_id " +
                "JOIN user uf ON af.user_id = uf.user_id " +
                "JOIN accounts at ON t.account_to = at.account_id " +
                "JOIN user ut ON at.user_id = ut.user_id " +
                "WHERE uf.user_id = ? OR ut.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;

    }

    @Override
    public Transfer createTransfer(Transfer newTransfer) {

        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        int newId = jdbcTemplate.queryForObject(sql, int.class, newTransfer.getTransferTypeId(), newTransfer.getTransferStatusId(),
                newTransfer.getAccountFrom(), newTransfer.getAccountTo(), newTransfer.getAmount());

        return getTransferById(newId);
    }

    @Override
    public void updateTransferTypeToRequest(int transferId) {

        String sql = "UPDATE transfers SET transfer_type_id = (SELECT transfer_type_id FROM transfer_statuses WHERE transfer_type_desc = 'Request')" +
                "WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, transferId);
    }

    @Override
    public void updateTransferTypeToSend(int transferId) {

        String sql = "UPDATE transfers SET transfer_type_id = (SELECT transfer_type_id FROM transfer_statuses WHERE transfer_type_desc = 'Send')" +
                "WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, transferId);
    }

    @Override
    public void updateTransferStatusToPending(int transferId) {

        String sql = "UPDATE transfers SET transfer_status_id = (SELECT transfer_status_id FROM transfer_statuses WHERE transfer_status_desc = 'Pending')" +
                "WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, transferId);
    }

    @Override
    public void updateTransferStatusToApproved(int transferId) {

        String sql = "UPDATE transfers SET transfer_status_id = (SELECT transfer_status_id FROM transfer_statuses WHERE transfer_status_desc = 'Approved')" +
                "WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, transferId);
    }

    @Override
    public void updateTransferStatusToRejected(int transferId) {

        String sql = "UPDATE transfers SET transfer_status_id = (SELECT transfer_status_id FROM transfer_statuses WHERE transfer_status_desc = 'Rejected')" +
                "WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, transferId);
    }



    public Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));

        transfer.setUsernameFrom(rowSet.getString("usernamefrom"));
        transfer.setUsernameTo(rowSet.getString("usernameto"));
        transfer.setTransferStatusString(rowSet.getString("transfer_status_desc"));
        transfer.setTransferTypeString(rowSet.getString("transfer_type_desc"));

        return transfer;
    }

}
