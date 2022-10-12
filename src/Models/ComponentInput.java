package Models;

public class ComponentInput {

    private int idInput;
    private int idArticle;
    private int qte;
    private double price;

    public ComponentInput() {
    }

    public ComponentInput(int idInput, int idArticle, int qte, double price) {
        this.idInput = idInput;
        this.idArticle = idArticle;
        this.qte = qte;
        this.price = price;
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

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
