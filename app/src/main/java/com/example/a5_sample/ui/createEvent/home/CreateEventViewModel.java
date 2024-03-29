package com.example.a5_sample.ui.createEvent.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateEventViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CreateEventViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}