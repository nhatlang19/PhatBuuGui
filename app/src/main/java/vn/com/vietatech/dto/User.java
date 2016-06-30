package vn.com.vietatech.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {
    private int id;
    private String name;
    private String username;
    private String password;
    private String role;
    private String phone;

    public User(String _username) {
        id = 0;
        username = _username;
    }

    public User(String _username, String _password) {
        id = 0;
        username = _username;
        password = _password;
        setRole("User");
    }

    public User(int _id, String _name, String _username, String _password, String _role, String _phone) {
        id = _id;
        name = _name;
        username = _username;
        password = _password;
        role = _role;
        setPhone(_phone);
    }

    public User(String _name, String _username, String _password, String _role, String _phone) {
        id = 0;
        name = _name;
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
        name = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
