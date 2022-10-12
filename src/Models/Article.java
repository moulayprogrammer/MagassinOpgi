package Models;


public class Article {

    private int id;
    private int idCategory;
    private int idUnit;
    private String name;
    private int qteAlert;
    private int qte;
    private String cat;
    private String unit;
    private int archive;


    public Article() {
    }

    public Article(int idCategory, int idUnit, String name, int qteAlert) {
        this.idCategory = idCategory;
        this.idUnit = idUnit;
        this.name = name;
        this.qteAlert = qteAlert;
    }

    public Article(int idCategory, int idUnit, String name, int qteAlert, int archive) {
        this.idCategory = idCategory;
        this.idUnit = idUnit;
        this.name = name;
        this.qteAlert = qteAlert;
        this.archive = archive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public int getIdUnit() {
        return idUnit;
    }

    public void setIdUnit(int idUnit) {
        this.idUnit = idUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQteAlert() {
        return qteAlert;
    }

    public void setQteAlert(int qteAlert) {
        this.qteAlert = qteAlert;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getArchive() {
        return archive;
    }

    public void setArchive(int archive) {
        this.archive = archive;
    }
}
