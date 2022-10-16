package Models;

import java.time.LocalDate;

public class RechargeGasoline {

    private int id;
    private LocalDate date;
    private String numberBC;
    private LocalDate dateBC;
    private String numberFact;
    private LocalDate dateFact;
    private Double price;

    public RechargeGasoline() {
    }

    public RechargeGasoline(String numberBC, LocalDate dateBC, String numberFact, LocalDate dateFact, Double price) {
        this.numberBC = numberBC;
        this.dateBC = dateBC;
        this.numberFact = numberFact;
        this.dateFact = dateFact;
        this.price = price;
    }

    public RechargeGasoline(LocalDate date, String numberBC, LocalDate dateBC, String numberFact, LocalDate dateFact, Double price) {
        this.date = date;
        this.numberBC = numberBC;
        this.dateBC = dateBC;
        this.numberFact = numberFact;
        this.dateFact = dateFact;
        this.price = price;
    }

    public RechargeGasoline(int id, Double price) {
        this.id = id;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNumberBC() {
        return numberBC;
    }

    public void setNumberBC(String numberBC) {
        this.numberBC = numberBC;
    }

    public LocalDate getDateBC() {
        return dateBC;
    }

    public void setDateBC(LocalDate dateBC) {
        this.dateBC = dateBC;
    }

    public String getNumberFact() {
        return numberFact;
    }

    public void setNumberFact(String numberFact) {
        this.numberFact = numberFact;
    }

    public LocalDate getDateFact() {
        return dateFact;
    }

    public void setDateFact(LocalDate dateFact) {
        this.dateFact = dateFact;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
