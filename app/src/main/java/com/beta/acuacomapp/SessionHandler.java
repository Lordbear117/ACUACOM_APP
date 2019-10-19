package com.beta.acuacomapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMPTY = "";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    /**
     * Logs in el usuario al guardar los detalles de usuario y configuración de sesión
     *
     * @param username
     * @param fullName
     */
    public void loginUser(String username, String fullName) {
        mEditor.putString(KEY_USERNAME, username);
        mEditor.putString(KEY_FULL_NAME, fullName);
        Date date = new Date();

        // Establece la sesión de usuario para los proximos 7 dias
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    /**
     * Revisa si el usuario está logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* Si shared preferences no tiene un valor
         entonces el usuario no está logged in
         */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* Revisa si la sesión expiró comparando
        la fecha actual y la fecha de expiración de sesión
        */
        return currentDate.before(expiryDate);
    }

    /**
     * Busca y regresa los detalles de usuario
     *
     * @return user details
     */
    public User getUserDetails() {
        //  Revisa si el usuario está logeado primero
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setUsername(mPreferences.getString(KEY_USERNAME, KEY_EMPTY));
        user.setFullName(mPreferences.getString(KEY_FULL_NAME, KEY_EMPTY));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));

        return user;
    }

    /**
     * Logs out al usuario al limpiar la sesión
     */
    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
    }

}
