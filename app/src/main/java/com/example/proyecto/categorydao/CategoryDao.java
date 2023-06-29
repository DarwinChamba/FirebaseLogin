package com.example.proyecto.categorydao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proyecto.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(Category category);
    @Delete
    void delete(Category category);
    @Update
    void update(Category category);
    @Query("select * from proyecto  order by prioridad desc")
    LiveData<List<Category>> selectAllCategory();
}
