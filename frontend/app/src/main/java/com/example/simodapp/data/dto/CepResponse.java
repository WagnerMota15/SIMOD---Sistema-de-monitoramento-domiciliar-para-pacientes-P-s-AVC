package com.example.simodapp.data.dto;

public class CepResponse {

    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    private boolean erro;

    public String getLogradouro() { return logradouro; }
    public String getBairro() { return bairro; }
    public String getLocalidade() { return localidade; }
    public String getUf() { return uf; }
    public boolean isErro() { return erro;}
}
