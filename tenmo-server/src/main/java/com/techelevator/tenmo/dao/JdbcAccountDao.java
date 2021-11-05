package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccount(int accountId) {
        Account account = null;
        String sql = "SELECT a.account_id, a.user_id, a.balance, u.username FROM accounts a " +
                "JOIN users u ON a.user_id = u.user_id " +
                "WHERE a.account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT a.account_id, a.user_id, a.balance, u.username FROM accounts a" +
                "JOIN users u ON a.user_id = u.user_id;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            accounts.add(mapRowToAccount(results));
        }
        return accounts;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        Account account = null;
        String sql = "SELECT a.account_id, a.user_id, a.balance, u.username FROM accounts a " +
                "JOIN users u ON a.user_id = u.user_id " +
                "WHERE u.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public BigDecimal getBalanceByAccountId(int accountId) {
        BigDecimal balance = BigDecimal.ZERO;
        String sql = "SELECT balance WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            balance = results.getBigDecimal("account_id");
        }
        return balance;
    }

    @Override
    public BigDecimal getBalanceByUserId(int userId) {
        BigDecimal balance = BigDecimal.ZERO;
        String sql = "SELECT balance WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            balance = results.getBigDecimal("user_id");
        }
        return balance;
    }

//    public void updateAccountBalance(BigDecimal newBalance, int userId) {
//        String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?;";
//        jdbcTemplate.update(sql, newBalance, userId);
//    }

    @Override
    public BigDecimal addToBalance(int userid, BigDecimal amount) {

        Account userAccount = getAccount(userid);

        BigDecimal updatedBalance = userAccount.getBalance().add(amount);

        String sql = "UPDATE accounts SET balance =? WHERE user_id =?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userid);

        return userAccount.getBalance();

    }

    @Override
    public BigDecimal subtractFromBalance(int userid, BigDecimal amount) {

        Account userAccount = getAccount(userid);

        BigDecimal updatedBalance = userAccount.getBalance().subtract(amount);

        String sql = "UPDATE accounts SET balance =? WHERE user_id =?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userid);

        return userAccount.getBalance();

    }


    public Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setUsername(rowSet.getString("username"));
        return account;
    }
}
