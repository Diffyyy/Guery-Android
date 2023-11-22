package com.mobdeve.s13.kok.james.gueryandroid.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class PostItemViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Post>> fragmentData = new MutableLiveData<>();

    public LiveData<ArrayList<Post>> getFragmentData(){
        return fragmentData;
    }

    public void setFragmentData(ArrayList<Post> fragmentData) {
        this.fragmentData.setValue(fragmentData);
    }
}
