package com.example.simodapp.domain.enums;

public enum StrokeTypes {
    ISQUEMICO("Isquêmico"),
    HEMORRAGICO("Hemorrágico"),
    ATAQUE_ISQUEMICO_TRANSITORIO("Ataque Isquêmico Transitório(AIT)"),
    OUTROS("Outro");


    private final String type;
    StrokeTypes(String type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return type;
    }
}
