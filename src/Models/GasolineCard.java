package Models;

import java.time.LocalDate;

public class GasolineCard {

    private int id;
    private String number;
    private double balance;
    private LocalDate lastRechargeDate;
    private LocalDate lastConsumptionDate;

    public GasolineCard() {
    }

    public GasolineCard(String number, double balance, LocalDate lastRechargeDate, LocalDate lastConsumptionDate) {
        this.number = number;
        this.balance = balance;
        this.lastRechargeDate = lastRechargeDate;
        this.lastConsumptionDate = lastConsumptionDate;
    }

    public GasolineCard(int id, String number, double balance, LocalDate lastRechargeDate, LocalDate lastConsumptionDate) {
        this.id = id;
        this.number = number;
        this.balance = balance;
        this.lastRechargeDate = lastRechargeDate;
        this.lastConsumptionDate = lastConsumptionDate;
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDate getLastRechargeDate() {
        return lastRechargeDate;
    }

    public void setLastRechargeDate(LocalDate lastRechargeDate) {
        this.lastRechargeDate = lastRechargeDate;
    }

    public LocalDate getLastConsumptionDate() {
        return lastConsumptionDate;
    }

    public void setLastConsumptionDate(LocalDate lastConsumptionDate) {
        this.lastConsumptionDate = lastConsumptionDate;
    }
}
