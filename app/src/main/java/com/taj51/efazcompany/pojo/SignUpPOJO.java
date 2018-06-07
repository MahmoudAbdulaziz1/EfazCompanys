package com.taj51.efazcompany.pojo;

public class SignUpPOJO {


    private int registration_id;
    private String registeration_email;
    private String registeration_password;
    private String registeration_username;
    private String registeration_phone_number;
    private String registration_organization_name;
    private String registration_address_desc;
    private String registration_website_url;
    private int registration_is_school;
    private int registration_isActive; //for confirmation


    public SignUpPOJO(int registration_id, String registeration_email, String registeration_password, String registeration_username,
                             String registeration_phone_number, String registration_organization_name, String registration_address_desc, String registration_website_url, int registration_is_school, int registration_isActive) {
        this.registration_id = registration_id;
        this.registeration_email = registeration_email;
        this.registeration_password = registeration_password;
        this.registeration_username = registeration_username;
        this.registeration_phone_number = registeration_phone_number;
        this.registration_organization_name = registration_organization_name;
        this.registration_address_desc = registration_address_desc;
        this.registration_website_url = registration_website_url;
        this.registration_is_school = registration_is_school;
        this.registration_isActive = registration_isActive;
    }

    public SignUpPOJO(String registeration_email, String registeration_password, String registeration_username,
                             String registeration_phone_number, String registration_organization_name,
                             String registration_address_desc, String registration_website_url, int registration_is_school,
                             int registration_isActive) {
        this.registeration_email = registeration_email;
        this.registeration_password = registeration_password;
        this.registeration_username = registeration_username;
        this.registeration_phone_number = registeration_phone_number;
        this.registration_organization_name = registration_organization_name;
        this.registration_address_desc = registration_address_desc;
        this.registration_website_url = registration_website_url;
        this.registration_is_school = registration_is_school;
        this.registration_isActive = registration_isActive;
    }

    public SignUpPOJO() {
    }

    public int getRegistration_id() {
        return registration_id;
    }

    public void setRegistration_id(int registration_id) {
        this.registration_id = registration_id;
    }

    public String getRegisteration_email() {
        return registeration_email;
    }

    public void setRegisteration_email(String registeration_email) {
        this.registeration_email = registeration_email;
    }

    public String getRegisteration_password() {
        return registeration_password;
    }

    public void setRegisteration_password(String registeration_password) {
        this.registeration_password = registeration_password;
    }

    public String getRegisteration_username() {
        return registeration_username;
    }

    public void setRegisteration_username(String registeration_username) {
        this.registeration_username = registeration_username;
    }

    public String getRegisteration_phone_number() {
        return registeration_phone_number;
    }

    public void setRegisteration_phone_number(String registeration_phone_number) {
        this.registeration_phone_number = registeration_phone_number;
    }

    public String getRegistration_organization_name() {
        return registration_organization_name;
    }

    public void setRegistration_organization_name(String registration_organization_name) {
        this.registration_organization_name = registration_organization_name;
    }

    public String getRegistration_address_desc() {
        return registration_address_desc;
    }

    public void setRegistration_address_desc(String registration_address_desc) {
        this.registration_address_desc = registration_address_desc;
    }

    public String getRegistration_website_url() {
        return registration_website_url;
    }

    public void setRegistration_website_url(String registration_website_url) {
        this.registration_website_url = registration_website_url;
    }

    public int getRegistration_is_school() {
        return registration_is_school;
    }

    public void setRegistration_is_school(int registration_is_school) {
        this.registration_is_school = registration_is_school;
    }

    public int getRegistration_isActive() {
        return registration_isActive;
    }

    public void setRegistration_isActive(int registration_isActive) {
        this.registration_isActive = registration_isActive;
    }

}
