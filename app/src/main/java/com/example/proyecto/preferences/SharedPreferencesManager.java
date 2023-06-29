package com.example.proyecto.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

        private static final String PREF_NAME = "MyAppPrefs";
        private static SharedPreferences sharedPreferences;
        private static SharedPreferences.Editor editor;

        // Inicializar SharedPreferences
        public static void init(Context context) {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
            }
        }

        // Guardar un valor en SharedPreferences
        public static void saveString(String key, String value) {
            editor.putString(key, value);
            editor.apply();
        }

        // Obtener un valor de SharedPreferences
        public static String getString(String key, String defaultValue) {
            return sharedPreferences.getString(key, defaultValue);
        }


}
