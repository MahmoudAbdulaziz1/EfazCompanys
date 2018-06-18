package com.taj51.efazcompany.pojo;

public class GetProfilePojo {



    private int company_id;
    private String company_name;
    private String company_logo_image;
    private String company_address;
    private String company_service_desc;
    private String company_link_youtube;
    private String company_website_url;


    public GetProfilePojo(int company_id, String company_name, String company_logo_image, String company_address,
                       String company_service_desc, String company_link_youtube, String company_website_url) {
        this.company_id = company_id;
        this.company_name = company_name;
        this.company_logo_image = company_logo_image;
        this.company_address = company_address;
        this.company_service_desc = company_service_desc;
        this.company_link_youtube = company_link_youtube;
        this.company_website_url = company_website_url;
        //this.login_id_company = login_id_company;
    }
    public GetProfilePojo(String company_name, String company_logo_image, String company_address,
                       String company_service_desc, String company_link_youtube, String company_website_url) {
        this.company_name = company_name;
        this.company_logo_image = company_logo_image;
        this.company_address = company_address;
        this.company_service_desc = company_service_desc;
        this.company_link_youtube = company_link_youtube;
        this.company_website_url = company_website_url;
        //this.login_id_company = login_id_company;
    }

    public GetProfilePojo() {
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_logo_image() {
        return company_logo_image;
    }

    public void setCompany_logo_image(String company_logo_image) {
        this.company_logo_image = company_logo_image;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getCompany_service_desc() {
        return company_service_desc;
    }

    public void setCompany_service_desc(String company_service_desc) {
        this.company_service_desc = company_service_desc;
    }

    public String getCompany_link_youtube() {
        return company_link_youtube;
    }

    public void setCompany_link_youtube(String company_link_youtube) {
        this.company_link_youtube = company_link_youtube;
    }

    public String getCompany_website_url() {
        return company_website_url;
    }

    public void setCompany_website_url(String company_website_url) {
        this.company_website_url = company_website_url;
    }
}
