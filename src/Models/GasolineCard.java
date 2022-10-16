package Models;

import java.time.LocalDate;

public class GasolineCard {

    private int id;
    private String number;
    private double lastBalance;
    private LocalDate lastRechargeDate;

    public GasolineCard() {
    }

    public GasolineCard(int id, String number, double lastBalance, LocalDate lastRechargeDate) {
        this.id = id;
        this.number = number;
        this.lastBalance = lastBalance;
        this.lastRechargeDate = lastRechargeDate;
    }

    public GasolineCard(String number, double lastBalance, LocalDate lastRechargeDate) {
        this.number = number;
        this.lastBalance = lastBalance;
        this.lastRechargeDate = lastRechargeDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getLastBalance() {
        return lastBalance;
    }

    public void setLastBalance(double lastBalance) {
        this.lastBalance = lastBalance;
    }

    public LocalDate getLastRechargeDate() {
        return lastRechargeDate;
    }

    public void setLastRechargeDate(LocalDate lastRechargeDate) {
        this.lastRechargeDate = lastRechargeDate;
    }
}
