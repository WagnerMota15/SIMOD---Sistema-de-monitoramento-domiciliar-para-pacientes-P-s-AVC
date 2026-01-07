package com.example.simodapp.login;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {

    private static UserSession instance;
    private final SharedPreferences prefs;

    private UserSession(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
    }

    public static UserSession getInstance(Context context) {
        if (instance == null) {
            instance = new UserSession(context);
        }
        return instance;
    }

    public void saveUser(String id, String nome, String email, String token, String cpf, String telefone) {
        prefs.edit()
                .putString("user_id", id)
                .putString("user_nome", nome)
                .putString("user_email", email)
                .putString("user_cpf", cpf)
                .putString("user_telefone", telefone)
                .putString("user_token", token)
                .putBoolean("is_logged_in", true)
                .apply();
    }

    public String getId() { return prefs.getString("user_id", ""); }
    public String getNome() { return prefs.getString("user_nome", ""); }
    public String getEmail() { return prefs.getString("user_email", ""); }
    public String getCpf() { return prefs.getString("user_cpf", ""); }
    public String getTelefone() { return prefs.getString("user_telefone", ""); }
    public String getToken() { return prefs.getString("user_token", ""); }
    public boolean isLoggedIn() { return prefs.getBoolean("is_logged_in", false); }

    /* Para fazer logout
    public void logout() {
        prefs.edit().clear().apply();
    }
    */
}
