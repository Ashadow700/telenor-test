package com.chas.telenortest.dataObjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//Developer's notes for the test: This class should probably have been turned into an interface.
// I decided not to however, as I would only implement it twice for this test, and the instructions said I should do
// the absolute minimum work required.

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String type;
    private String property;
    private double price;
    private String addressOfSale;
    private String cityOfSale;

    public Product(String type, String property, double price, String locationOfSale) {
        this.type = type;
        this.property = property;
        this.price = price;
        this.addressOfSale = locationOfSale;
        this.cityOfSale = locationOfSale;
    }

    public Product(String type, String properties, double price, String addressOfSale, String cityOfSale) {
        this.type = type;
        this.property = properties;
        this.price = price;
        this.addressOfSale = addressOfSale;
        this.cityOfSale = cityOfSale;
    }

    public Product(String type) {
        this.type = type;
    }

    public Product() {
        //Dummy constructor used for serialization
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getProperty() {
        return property;
    }

    public double getPrice() {
        return price;
    }

    @JsonIgnore
    public String getAddressOfSale() {
        return addressOfSale;
    }

    @JsonIgnore
    public String getCityOfSale() {
        return cityOfSale;
    }

    @JsonProperty("store_address")
    public String getLocationOfSale() {
        return addressOfSale + ", " + cityOfSale;
    }
}
