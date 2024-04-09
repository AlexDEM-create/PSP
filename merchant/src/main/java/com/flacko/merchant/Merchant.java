package com.flacko.merchant;

public class Merchant {
    private int merchantId;
    private String name;

    // report on payments
    public Report generateReport(Date fromDate, Date toDate) {
        // logic to generate a report
    }

    // view balance in USDT
    public BigDecimal getBalance() {
        // logic to get balance
    }

    // withdraw USDT to wallet
    public Withdrawal initiateSettlement(BigDecimal amount) {
        // logic to initiate settlement
    }
}

