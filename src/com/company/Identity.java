package com.company;

public class Identity {
    private String user;
    private String password;
    public Identity(String user, String password) {
        super();
        this.user = user;
        this.password = password;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "Identity [user=" + user + ", password=" + password + "]";
    }

}
