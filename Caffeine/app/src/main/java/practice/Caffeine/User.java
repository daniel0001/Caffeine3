package practice.Caffeine;

/**
 * Created by Daniel on 11/03/2017.
 */

public class User {

    private int id;
    private String username;
    private int userID;
    private String name;
    private String password;
    private String phone;
    private String email;
    private String location;

    public User() {
    }

    public User(int id, String username, int userID, String name, String password, String phone, String email, String location) {
        super();
        this.id = id;
        this.username = username;
        this.userID = userID;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.location = location;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userID=" + userID +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", location='" + location + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
