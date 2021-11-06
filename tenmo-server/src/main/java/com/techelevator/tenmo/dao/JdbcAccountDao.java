package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;


    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Account getAccount(int userId) {

        Account account = null;

        String sql = "SELECT a.account_id , a.user_id, a.balance, u.username FROM accounts a " +
                "JOIN users u ON u.user_id = a.user_id " +
                "WHERE u.user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()){
            account = mapRowToAccount(results);
        } //catch an exception??

        return account;
    }

    public BigDecimal getBalance(int userId){

        BigDecimal balance = null;

        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        SqlRowSet results  = jdbcTemplate.queryForRowSet(sql, userId);

        if (results.next()){
            balance = results.getBigDecimal("balance");
        } //catch an exception??

        return balance;
    }


    public void addToBalance(BigDecimal addedAmount, int userId){
        Account userAccount = getAccount(userId);

        BigDecimal updatedBalance = userAccount.getBalance().add(addedAmount);

        String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";

        jdbcTemplate.update(sql, updatedBalance, userId);
    }

    public void subtractFromBalance(BigDecimal subtractedAmount, int userId){
        Account userAccount = getAccount(userId);

        BigDecimal updatedBalance = userAccount.getBalance().subtract(subtractedAmount);

        String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";

        jdbcTemplate.update(sql, updatedBalance, userId);
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setUsername(rs.getString("username"));

        return account;
    }

}