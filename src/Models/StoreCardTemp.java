package Models;

import java.time.LocalDate;

public class StoreCardTemp {

    private int id;
    private int idInput;
    private int idArticle;
    private LocalDate dateStore;
    private int qteStored;
    private int qteConsumed;
    private double price;

    public StoreCardTemp() {
    }

    public StoreCardTemp(int id, int idArticle, LocalDate dateStore, int qteStored, int qteConsumed, double price) {
        this.id = id;
        this.idArticle = idArticle;
        this.dateStore = dateStore;
        this.qteStored = qteStored;
        this.qteConsumed = qteConsumed;
        this.price = price;
    }

    public StoreCardTemp(int id, int idArticle, int qteStored, int qteConsumed, double price) {
        this.id = id;
        this.idArticle = idArticle;
        this.qteStored = qteStored;
        this.qteConsumed = qteConsumed;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdInput() {
        return idInput;
    }

    public void setIdInput(int idInput) {
        this.idInput = idInput;
    }

    public int getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }

    public LocalDate getDateStore() {
        return dateStore;
    }

    public void setDateStore(LocalDate dateStore) {
        this.dateStore = dateStore;
    }

    public int getQteStored() {
        return qteStored;
    }

    public void setQteStored(int qteStored) {
        this.qteStored = qteStored;
    }

    public int getQteConsumed() {
        return qteConsumed;
    }

    public void setQteConsumed(int qteConsumed) {
        this.qteConsumed = qteConsumed;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
