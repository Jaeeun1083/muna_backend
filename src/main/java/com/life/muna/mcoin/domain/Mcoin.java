package com.life.muna.mcoin.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
@Getter
public class Mcoin {
        private Long userCode;
        private int cashCoin;
        private int cashCoinUsed;
        private int normalCoin;
        private int normalCoinUsed;
        private int totalAmount;
        private Date insertDate;
        private Date updateDate;
        @Builder
        public Mcoin(Long userCode, int cashCoin, int cashCoinUsed, int normalCoin, int normalCoinUsed, int totalAmount, Date insertDate, Date updateDate) {
            this.userCode = userCode;
            this.cashCoin = cashCoin;
            this.cashCoinUsed = cashCoinUsed;
            this.normalCoin = normalCoin;
            this.normalCoinUsed = normalCoinUsed;
            this.totalAmount = totalAmount;
            this.insertDate = insertDate;
            this.updateDate = updateDate;
        }

    public void decreaseMcoin(int useCashCoin, int useNormalCoin) {
        this.cashCoin -= useCashCoin;
        this.cashCoinUsed += useCashCoin;
        this.normalCoin -= useNormalCoin;
        this.normalCoinUsed += useNormalCoin;
        this.totalAmount -= (useCashCoin + useNormalCoin);
    }

    public void increaseMcoin(int normalCoinPlus) {
        this.normalCoin += normalCoinPlus;
        this.totalAmount += normalCoinPlus;
    }

    private void setCashCoin(int cashCoin) {
        this.cashCoin = cashCoin;
    }

    private void setCashCoinUsed(int cashCoinUsed) {
        this.cashCoinUsed = cashCoinUsed;
    }

    private void setNormalCoin(int normalCoin) {
        this.normalCoin = normalCoin;
    }

    private void setNormalCoinUsed(int normalCoinUsed) {
        this.normalCoinUsed = normalCoinUsed;
    }
}
