package com.flacko.merchant;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class Merchant implements MerchantBuilder{
    private String id;
    private String name;
    private String userid;
    private Instant createdDate;
    private Instant updatedDate;

    //TODO добавить геттеры + userid

//     report on payments
//    public Report generateReport(Date fromDate, Date toDate) {
//        // logic to generate a report
//        System.out.println("Merchants Turnover = 15000 USDT since"+fromDate+ "till"+ toDate);
//
//    }
//
//    // view balance in USDT
//    public BigDecimal getBalance() {
//        // logic to get balance
//        System.out.println("Merchants balance = 5000 USDT");
//    }
//
//    // withdraw USDT to wallet
//    public Withdrawal initiateSettlement(BigDecimal amount) {
//        // logic to initiate settlement
//    }


