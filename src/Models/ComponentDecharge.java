package Models;

public class ComponentDecharge {

    private int id;
    private int idArt;
    private int idDecharge;
    private int idStore;
    private int qte;

    public ComponentDecharge() {
    }

    public ComponentDecharge(int id, int idArt, int idDecharge, int idStore, int qte) {
        this.id = id;
        this.idArt = idArt;
        this.idDecharge = idDecharge;
        this.idStore = idStore;
        this.qte = qte;
    }

    public ComponentDecharge(int idArt, int idDecharge, int idStore, int qte) {
        this.idArt = idArt;
        this.idDecharge = idDecharge;
        this.idStore = idStore;
        this.qte = qte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdArt() {
        return idArt;
    }

    public void setIdArt(int idArt) {
        this.idArt = idArt;
    }

    public int getIdDecharge() {
        return idDecharge;
    }

    public void setIdDecharge(int idDecharge) {
        this.idDecharge = idDecharge;
    }

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }
}
