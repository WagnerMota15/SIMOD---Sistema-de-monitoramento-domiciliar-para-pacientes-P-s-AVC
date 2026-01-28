package com.example.simodapp.domain.enums; // Mantenha o pacote do seu projeto

public enum Kinship {
    // Constante (Para o Backend) -> Rótulo (Para o Usuário na tela)
    MÃE("Mãe"),
    PAI("Pai"),
    IRMAO("Irmão"),
    IRMA("Irmã"),
    AVÔ("Avô"),
    AVÓ("Avó"),
    TIO("Tio"),
    TIA("Tia"),
    PRIMO("Primo"),
    PRIMA("Prima"),
    CONJUGE("Cônjuge"),
    FILHO("Filho"),
    FILHA("Filha"),
    AMIGO("Amigo(a)"),
    OUTRO("Outro");

    private final String kinship;

    Kinship(String kinship) {
        this.kinship = kinship;
    }

    @Override
    public String toString() {
        return kinship;
    }
}