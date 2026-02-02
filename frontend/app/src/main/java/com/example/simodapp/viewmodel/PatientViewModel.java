package com.example.simodapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.simodapp.domain.enums.StrokeTypes;

public class PatientViewModel extends ViewModel {

    private final MutableLiveData<StrokeTypes> strokeType = new MutableLiveData<>();

    public void setStrokeType(StrokeTypes type) {
        strokeType.setValue(type);
    }

    public StrokeTypes getStrokeType() {
        return strokeType.getValue();
    }

}
