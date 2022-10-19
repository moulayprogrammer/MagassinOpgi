package Models;

import java.time.LocalDate;

public class RechargeGasolineCard {

    private int id;
    private int idEmp;
    private int idGasolineCard;
    private String Number;
    private String NumberNaftal;
    private LocalDate date;
    private double price;

    public RechargeGasolineCard() {
    }

    public RechargeGasolineCard(int id, int idEmp, String number, LocalDate date) {
        this.id = id;
        this.idEmp = idEmp;
        Number = number;
        this.date = date;
    }

    public RechargeGasolineCard(int id, int idEmp, int idGasolineCard, String number, LocalDate date, double price) {
        this.id = id;
        this.idEmp = idEmp;
        this.idGasolineCard = idGasolineCard;
        Number = number;
        this.date = date;
        this.price = price;
    }

    public RechargeGasolineCard(int id, int idEmp, int idGasolineCard, String number, String numberNaftal, LocalDate date, double price) {
        this.id = id;
        this.idEmp = idEmp;
        this.idGasolineCard = idGasolineCard;
        Number = number;
        NumberNaftal = numberNaftal;
        this.date = date;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEmp() {
        return idEmp;
    }

    public void setIdEmp(int idEmp) {
        this.idEmp = idEmp;
    }

    public int getIdGasolineCard() {
        return idGasolineCard;
    }

    public void setIdGasolineCard(int idGasolineCard) {
        this.idGasolineCard = idGasolineCard;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getNumberNaftal() {
        return NumberNaftal;
    }

    public void setNumberNaftal(String numberNaftal) {
        NumberNaftal = numberNaftal;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
