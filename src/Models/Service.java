package Models;

public class Service {

    private int id;
    private int idDep;
    private String name;
    private String nameDep;

    public Service() {
    }

    public Service(int id, int idDep, String name) {
        this.id = id;
        this.idDep = idDep;
        this.name = name;
    }

    public Service(int idDep, String name) {
        this.idDep = idDep;
        this.name = name;
    }

    public Service(int idDep, String name, String nameDep) {
        this.idDep = idDep;
        this.name = name;
        this.nameDep = nameDep;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDep() {
        return idDep;
    }

    public void setIdDep(int idDep) {
        this.idDep = idDep;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameDep() {
        return nameDep;
    }

    public void setNameDep(String nameDep) {
        this.nameDep = nameDep;
    }
}
