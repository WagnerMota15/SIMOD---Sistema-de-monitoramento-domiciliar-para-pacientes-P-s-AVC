package com.example.simodapp.data.dto;

public class AddressRequest {

    private String cep;
    private String street;
    private String neighborhood;
    private String city;
    private String state;

    public AddressRequest(String cep, String street, String neighborhood, String city, String state) {
        this.cep = cep;
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
    }
}
