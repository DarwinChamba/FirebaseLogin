package com.example.proyecto.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.proyecto.database.CategoryDatabase;
import com.example.proyecto.model.Category;
import com.example.proyecto.repository.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository repository;
    private LiveData<List<Category>> listOfCategory;
    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repository=new CategoryRepository(application);
        listOfCategory=repository.getAllNotes();
    }
    public void insertar(Category notas){
        repository.insert(notas);
    }
    public void delete(Category notas){
        repository.delete(notas);
    }
    public void update(Category notas){
        repository.update(notas);
    }
    public LiveData<List<Category>> getAllNotes(){
        return listOfCategory;
    }


}
