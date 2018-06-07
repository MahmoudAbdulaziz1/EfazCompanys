package com.taj51.efazcompany.pojo;

import com.google.gson.annotations.SerializedName;

public class LoginPOJO {

    @SerializedName("login_id")
    private int login_id;
    @SerializedName("user_email")
    private String user_email;
    @SerializedName("user_password")
    private String user_password;
    @SerializedName("is_active")
    private int is_active;
    @SerializedName("login_type")
    private int login_type;

    public LoginPOJO(int login_id, String user_email, String user_password, int is_active, int login_type) {
        this.login_id = login_id;
        this.user_email = user_email;
        this.user_password = user_password;
        this.is_active = is_active;
        this.login_type = login_type;
    }
    public LoginPOJO(String user_email, String user_password, int is_active, int login_type) {
        this.user_email = user_email;
        this.user_password = user_password;
        this.is_active = is_active;
        this.login_type = login_type;
    }

    public LoginPOJO() {
    }

    public int getLogin_id() {
        return login_id;
    }

    public void setLogin_id(int login_id) {
        this.login_id = login_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getLogin_type() {
        return login_type;
    }

    public void setLogin_type(int login_type) {
        this.login_type = login_type;
    }
}
