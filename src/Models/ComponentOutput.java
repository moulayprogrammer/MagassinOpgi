package Models;

public class ComponentOutput {

    private int id;
    private int idArt;
    private int idOutput;
    private int idStore;
    private int qteDem;
    private int qteServ;

    public ComponentOutput() {
    }

    public ComponentOutput(int id, int idArt, int idOutput, int idStore, int qteDem, int qteServ) {
        this.id = id;
        this.idArt = idArt;
        this.idOutput = idOutput;
        this.idStore = idStore;
        this.qteDem = qteDem;
        this.qteServ = qteServ;
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

    public int getIdOutput() {
        return idOutput;
    }

    public void setIdOutput(int idOutput) {
        this.idOutput = idOutput;
    }

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public int getQteDem() {
        return qteDem;
    }

    public void setQteDem(int qteDem) {
        this.qteDem = qteDem;
    }

    public int getQteServ() {
        return qteServ;
    }

    public void setQteServ(int qteServ) {
        this.qteServ = qteServ;
    }
}
