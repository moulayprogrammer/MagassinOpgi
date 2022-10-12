package Models;

import java.time.LocalDate;

public class Input {

    private int id;
    private int idProvider;
    private String number;
    private LocalDate date;
    private String numberBC;
    private LocalDate dateBC;
    private String numberFact;
    private LocalDate dateFact;

    public Input() {
    }

    public Input(int id, int idProvider, String number, LocalDate date, String numberBC, LocalDate dateBC, String numberFact, LocalDate dateFact) {
        this.id = id;
        this.idProvider = idProvider;
        this.number = number;
        this.date = date;
        this.numberBC = numberBC;
        this.dateBC = dateBC;
        this.numberFact = numberFact;
        this.dateFact = dateFact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProvider() {
        return idProvider;
    }

    public void setIdProvider(int idProvider) {
        this.idProvider = idProvider;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
}
