package userAuthentication.activities;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String KEY_EMAIL = "epasts";

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
