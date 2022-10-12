package Models;

public class Employee {

    private int id;
    private int idService;
    private String firstName;
    private String lastName;
    private String function;

    public Employee() {
    }

    public Employee(int id, int idService, String firstName, String lastName, String function) {
        this.id = id;
        this.idService = idService;
        this.firstName = firstName;
        this.lastName = lastName;
        this.function = function;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
