package com.example.proyecto.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.proyecto.categorydao.CategoryDao;
import com.example.proyecto.database.CategoryDatabase;
import com.example.proyecto.model.Category;

import java.util.List;

public class CategoryRepository {

    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;



    public  CategoryRepository(Application application){
        CategoryDatabase database=CategoryDatabase.getInstance(application);
        categoryDao= database.categoryDao();
        allCategories=categoryDao.selectAllCategory();
    }
    public void insert(Category category){
        new InsertCategoryAsyncTask(categoryDao).execute(category);
    }
    public void delete(Category category){
        new DeleteNotesAsyncTask(categoryDao).execute(category);
    }
    public void update(Category category){
        new UpdateNotesAsyncTask(categoryDao).execute(category);
    }

    public LiveData<List<Category>> getAllNotes(){
        return allCategories;
    }


    private static class InsertCategoryAsyncTask extends AsyncTask<Category,Void,Void> {
        private CategoryDao categoryDao;
        private InsertCategoryAsyncTask(CategoryDao categoryDao){
            this.categoryDao=categoryDao;
        }
        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.insert(categories[0]);
            return null;
        }
    }
    private static class DeleteNotesAsyncTask extends AsyncTask<Category,Void,Void>{
        private CategoryDao categoryDao;
        private DeleteNotesAsyncTask(CategoryDao categoryDao){
            this.categoryDao=categoryDao;
        }
        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.delete(categories[0]);
            return null;
        }
    }
    private static class UpdateNotesAsyncTask extends AsyncTask<Category,Void,Void>{
        private CategoryDao categoryDao;
        private UpdateNotesAsyncTask(CategoryDao categoryDao){
            this.categoryDao=categoryDao;
        }
        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.update(categories[0]);
            return null;
        }
    }

}

