package com.example.simodapp.domain.state;

public abstract class FinalProfessionalUiState {

    public static class Idle extends FinalProfessionalUiState {}

    public static class Loading extends FinalProfessionalUiState {}

    public static class Approved extends FinalProfessionalUiState {}

    public static class Rejected extends FinalProfessionalUiState {
        public final String message;

        public Rejected(String message) {
            this.message = message;
        }
    }
}
