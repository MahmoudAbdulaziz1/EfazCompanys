package com.taj51.efazcompany.pojo;

import java.sql.Timestamp;

public class GetCompanyOfferPOJO {


    private int offer_id;
    private String offer_logo;
    private String offer_title;
    private String offer_explaination;
    private double offer_cost;
    private String offer_display_date;
    private String offer_expired_date;
    private String offer_deliver_date;
    private int company_id;

    public GetCompanyOfferPOJO() {
    }

    public GetCompanyOfferPOJO(int offer_id, String offer_logo, String offer_title, String offer_explaination, double offer_cost,
                               String offer_display_date, String offer_expired_date, String offer_deliver_date, int company_id) {
        this.offer_id = offer_id;
        this.offer_logo = offer_logo;
        this.offer_title = offer_title;
        this.offer_explaination = offer_explaination;
        this.offer_cost = offer_cost;
        this.offer_display_date = offer_display_date;
        this.offer_expired_date = offer_expired_date;
        this.offer_deliver_date = offer_deliver_date;
        this.company_id = company_id;

    }

    public GetCompanyOfferPOJO(String offer_logo, String offer_title, String offer_explaination, double offer_cost,
                               String offer_display_date, String offer_expired_date, String offer_deliver_date, int company_id) {
        this.offer_logo = offer_logo;
        this.offer_title = offer_title;
        this.offer_explaination = offer_explaination;
        this.offer_cost = offer_cost;
        this.offer_display_date = offer_display_date;
        this.offer_expired_date = offer_expired_date;
        this.offer_deliver_date = offer_deliver_date;
        this.company_id = company_id;

    }

    public int getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(int offer_id) {
        this.offer_id = offer_id;
    }

    public String getOffer_logo() {
        return offer_logo;
    }

    public void setOffer_logo(String offer_logo) {
        this.offer_logo = offer_logo;
    }

    public String getOffer_title() {
        return offer_title;
    }

    public void setOffer_title(String offer_title) {
        this.offer_title = offer_title;
    }

    public String getOffer_explaination() {
        return offer_explaination;
    }

    public void setOffer_explaination(String offer_explaination) {
        this.offer_explaination = offer_explaination;
    }

    public double getOffer_cost() {
        return offer_cost;
    }

    public void setOffer_cost(double offer_cost) {
        this.offer_cost = offer_cost;
    }

    public String getOffer_display_date() {
        return offer_display_date;
    }

    public void setOffer_display_date(String offer_display_date) {
        this.offer_display_date = offer_display_date;
    }

    public String getOffer_expired_date() {
        return offer_expired_date;
    }

    public void setOffer_expired_date(String offer_expired_date) {
        this.offer_expired_date = offer_expired_date;
    }

    public String getOffer_deliver_date() {
        return offer_deliver_date;
    }

    public void setOffer_deliver_date(String offer_deliver_date) {
        this.offer_deliver_date = offer_deliver_date;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

}
