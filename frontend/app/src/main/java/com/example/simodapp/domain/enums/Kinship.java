package com.example.simodapp.domain.enums; // Mantenha o pacote do seu projeto

public enum Kinship {
    // Constante (Para o Backend) -> Rótulo (Para o Usuário na tela)
    MÃE("Mãe"),
    PAI("Pai"),
    IRMÃO("Irmã(o)"),
    AVÔ("Avô(ó)"),
    TIO("Tio(a)"),
    PRIMO("Primo(a)"),
    CÔNJUGE("Cônjuge"),
    FILHO("Filho(a)"),
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