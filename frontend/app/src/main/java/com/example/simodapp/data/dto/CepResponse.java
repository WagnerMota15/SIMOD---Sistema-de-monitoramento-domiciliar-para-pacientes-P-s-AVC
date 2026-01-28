package com.example.simodapp.data.dto;

public class CepResponse {

    private String publicPlace;
    private String city;
    private String state;
    private boolean error;

    public String getPublicPlace() {
        return publicPlace;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public boolean isError() {
        return error;
    }
}
