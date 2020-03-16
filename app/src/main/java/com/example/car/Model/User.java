package com.example.car.Model;
public class User {
    private String username;
    private String password;
    private String fName;
    private String sName;
    private String mail;
    private String fullName;

    public User() {
    }

    public User(String username, String password, String fName, String sName, String mail) {
        this.username = username;
        this.password = password;
        this.fName = fName;
        this.sName = sName;
        this.fullName = fName + " " + sName;
        this.mail = mail;
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

    public void setFirstName(String fName) {
            this.fName = fName;
    }

    public String getFirstName() {
        return fName;
    }

    public void setLastName(String sName) {
            this.sName = sName;
    }

    public String getLastName() {
        return sName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
            this.mail = mail;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void updateUser(String fName, String lName, String mail) {
        setFirstName(fName);
        setLastName(lName);
        setMail(mail);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) &&
                password.equals(user.password) &&
                fName.equals(user.fName) &&
                sName.equals(user.sName) &&
                mail.equals(user.mail) &&
                fullName.equals(user.fullName);
    }

}
