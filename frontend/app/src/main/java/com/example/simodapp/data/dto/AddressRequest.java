package com.example.simodapp.data.dto;

public class AddressRequest {

    private String cep;
    private String street;
    private String neighborhood;
    private String city;
    private String state;
    private String number;

    public AddressRequest(String cep, String street, String neighborhood, String city, String state,String number) {
        this.cep = cep;
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.number = number;
    }

    public String getCep() {
        return cep;
    }

    public String getStreet() {
        return street;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getNumber() {
        return number;
    }
}
