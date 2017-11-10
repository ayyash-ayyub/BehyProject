package adompo.ayyash.behay;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "com.adompo.ayyash.behay.BehyPreference";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_LOGGED_ID = "IsLoggedIn";
    private static final String ACTIVE_EMAIL = "ActiveEmail";

    public PrefManager(Context context) {
        Log.d("PrefManager", "PrefManager contructor is called");
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor = pref.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public void setLoggedIn(boolean loggedIn) {
        editor = pref.edit();
        editor.putBoolean(IS_LOGGED_ID, loggedIn);
        editor.apply();
    }

    public void setActiveEmail(String email) {
        editor = pref.edit();
        editor.putString(ACTIVE_EMAIL, email);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGGED_ID, false);
    }

    public String getActiveEmail() {
        return pref.getString(ACTIVE_EMAIL, "");
    }

}
