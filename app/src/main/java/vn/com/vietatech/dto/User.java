package vn.com.vietatech.dto;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private String phone;

    public User(String _username, String _password) {
        username = _username;
        password = _password;
    }

    public User(String _username, String _password, String _role, String _phone) {
        username = _username;
        password = _password;
        role = _role;
        setPhone(_phone);
    }

    public User() {
        id = 0;
        username = "";
        password = "";
        setRole("User");
        phone = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
