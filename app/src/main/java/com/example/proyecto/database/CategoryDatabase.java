package com.example.proyecto.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.proyecto.categorydao.CategoryDao;
import com.example.proyecto.model.Category;

@Database(entities = Category.class,version = 3)
public abstract  class CategoryDatabase extends RoomDatabase {
    private static CategoryDatabase instance;
    public abstract CategoryDao categoryDao();
    public static synchronized CategoryDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    CategoryDatabase.class,"categoryDatabase").
                    addCallback(callback).
                    fallbackToDestructiveMigration().build();
        }
        return instance;
    }
    private static RoomDatabase.Callback callback=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new Populate(instance).execute();
        }
    };

    private static class Populate extends AsyncTask<Void,Void,Void> {
        private CategoryDao categoryDao;
        private Populate(CategoryDatabase db){
            categoryDao=db.categoryDao();

        }
        @Override
        protected Void doInBackground(Void... voids) {
            categoryDao.insert(new Category("sitios seguros","12:34","2/10/2023",2));
            categoryDao.insert(new Category("trafico","12:34","2/10/2023",3));
            categoryDao.insert(new Category("accidente de trafico","12:34","2/10/2023",2));
            return null;
        }

    }

}
