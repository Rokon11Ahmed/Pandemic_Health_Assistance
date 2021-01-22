package com.hemontosoftware.pandemichealthkit.uitlity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessaionManager {
    private SharedPreferences prefs;

    public SessaionManager(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public void set_user_login_status(String loginStatus){
        prefs.edit().putString("loginStatus", loginStatus).commit();
    }
    public String get_user_login_status(){
        return prefs.getString("loginStatus", "false");
    }
    public void set_user_number(String phoneNumber){
        prefs.edit().putString("phoneNumber", phoneNumber).commit();
    }
    public String get_user_number(){
        return prefs.getString("phoneNumber", "0");
    }

    public void set_user_name(String name){
        prefs.edit().putString("name", name).commit();
    }
    public String get_user_name(){
        return prefs.getString("name", "0");
    }

    public void set_user_age(String age){
        prefs.edit().putString("age", age).commit();
    }
    public String get_user_age(){
        return prefs.getString("age", "0");
    }

    public void set_user_blood_group(String blood_group){
        prefs.edit().putString("blood_group", blood_group).commit();
    }
    public String get_user_blood_group(){
        return prefs.getString("blood_group", "0");
    }
    public void set_user_address(String address){
        prefs.edit().putString("address", address).commit();
    }
    public String get_user_address(){
        return prefs.getString("address", "0");
    }

    public void set_user_profile_update_status(String profileUpdateStatus){
        prefs.edit().putString("profileUpdateStatus", profileUpdateStatus).commit();
    }
    public String get_user_profile_update_status(){
        return prefs.getString("profileUpdateStatus", "false");
    }

    public void setNextReportTime(String nextReportTime){
        prefs.edit().putString("nextReportTime", nextReportTime).commit();
    }
    public String getNextReportTime(){
        return prefs.getString("nextReportTime", "0");
    }
}
