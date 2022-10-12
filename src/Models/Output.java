package Models;

import java.time.LocalDate;

public class Output {

    private int id;
    private int idEmp;
    private String Number;
    private LocalDate date;

    public Output() {
    }

    public Output(int id, int idEmp, String number, LocalDate date) {
        this.id = id;
        this.idEmp = idEmp;
        Number = number;
        this.date = date;
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

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
