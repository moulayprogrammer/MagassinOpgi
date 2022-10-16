package Models;

import java.time.LocalDate;

public class Decharge {

    private int id;
    private int idEmp;
    private LocalDate date;

    public Decharge() {
    }

    public Decharge(int id, int idEmp, LocalDate date) {
        this.id = id;
        this.idEmp = idEmp;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
