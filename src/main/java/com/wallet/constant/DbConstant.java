package com.wallet.constant;

public class DbConstant {

    public static final String FIND_WALLETS_BY_USER_ID = "SELECT w FROM Wallet w WHERE w.user.id = :userId";
    public static final String FIND_HISTORIC_BALANCE_BY_USER_ID_AND_DATE = "SELECT h FROM HistoricBalance h JOIN Wallet " +
            "w on h.wallet.id = w.id WHERE w.user.id = :userId AND h.date BETWEEN :startDate AND :endDate";
    public static final String FIND_USER_FETCH_HISTORIC_BALANCE = "SELECT w FROM Wallet w LEFT JOIN FETCH w.historicBalance WHERE w.user.id = :userId";

    private DbConstant() {
    }
}
