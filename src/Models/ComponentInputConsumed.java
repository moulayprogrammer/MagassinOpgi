package Models;

public class ComponentInputConsumed {

    private int idInput;
    private String Article;
    private int qte;
    private double price;

    public ComponentInputConsumed() {
    }

    public ComponentInputConsumed(int idInput, String article, int qte, double price) {
        this.idInput = idInput;
        Article = article;
        this.qte = qte;
        this.price = price;
    }

    public ComponentInputConsumed(String article, int qte, double price) {
        Article = article;
        this.qte = qte;
        this.price = price;
    }

    public int getIdInput() {
        return idInput;
    }

    public void setIdInput(int idInput) {
        this.idInput = idInput;
    }

    public String getArticle() {
        return Article;
    }

    public void setArticle(String article) {
        Article = article;
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
