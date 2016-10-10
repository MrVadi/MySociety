package ua.mrvadi.mysociety;

import android.app.Application;
import android.content.SharedPreferences;

import ua.mrvadi.mysociety.constants.Consts;
import ua.mrvadi.mysociety.helpers.DBHelper;
import ua.mrvadi.mysociety.models.Admin;
import ua.mrvadi.mysociety.models.Contact;

/**
 * Created by mrvadi on 03.10.16.
 */
public class MySocietyApp extends Application {

    DBHelper dbHelper;
    Admin admin;
    SharedPreferences prefs;
    boolean isFirstTime;

    @Override
    public void onCreate() {
        super.onCreate();
        initSingletons();
        manageFirstRun();
    }

    private void initSingletons() {
        DBHelper.initInstance(this);
    }

    private void manageFirstRun() {
        prefs = getSharedPreferences(Consts.PACKAGE_NAME, MODE_PRIVATE);
        isFirstTime = prefs.getBoolean(Consts.FIRST_RUN, true);
        if (isFirstTime) {
            dbHelper = DBHelper.getInstance();
            dbHelper.createAdminInfo(getApplicationContext());
            admin = dbHelper.getAdminInfo();
            dbHelper.addContact(myContact());
            prefs.edit().putBoolean(Consts.FIRST_RUN, false).apply();
        }

    }
    private Contact myContact() {
        Contact mrVadi = new Contact();
        mrVadi.setFirstName(admin.getFirstName());
        mrVadi.setLastName(admin.getLastName());
        mrVadi.setPhoneNumber(admin.getPhoneNumber());
        mrVadi.setPhoto(admin.getPhoto());
        return mrVadi;
    }
}