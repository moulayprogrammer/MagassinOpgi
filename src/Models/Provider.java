package Models;

public class Provider {

    private int id;
    private String name;
    private String address;
    private String activity;

    public Provider() {
    }

    public Provider(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Provider(int id, String name, String address, String activity) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.activity = activity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
