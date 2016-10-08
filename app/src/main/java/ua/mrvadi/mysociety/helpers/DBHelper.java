package ua.mrvadi.mysociety.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ua.mrvadi.mysociety.R;
import ua.mrvadi.mysociety.models.Admin;
import ua.mrvadi.mysociety.models.Contact;

/**
 * Created by mrvadi on 03.10.16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mySocietyDB";
    private static final String TABLE_ADMIN = "users";
    private static final String TABLE_CONTACTS = "contacts";

    //Common Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_DISPLAY_NAME = "display_name";
    private static final String KEY_PHONE_NUMBERS = "phone_numbers";
    private static final String KEY_PHOTO = "photo";

    // Contacts Table Columns names
    private static final String KEY_COLOR = "color";

    // Admin Table Columns names
    private static final String KEY_LOGIN = "login";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_DATE_OF_BIRTH = "date_of_birth";
    private static final String KEY_INFO = "info";
    private static final String KEY_LINKED_IN = "linked_in";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_LOGINED = "logined";

    private static final String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_FIRST_NAME + " TEXT,"
            + KEY_LAST_NAME + " TEXT," + KEY_DISPLAY_NAME + " TEXT," + KEY_PHONE_NUMBERS
            + " TEXT," + KEY_PHOTO + " BLOB," + KEY_COLOR + " TEXT" + ")";

    private static final String CREATE_ADMIN_TABLE = "CREATE TABLE " + TABLE_ADMIN + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LOGIN + " TEXT UNIQUE,"
            + KEY_PASSWORD + " TEXT," + KEY_FIRST_NAME + " TEXT," + KEY_LAST_NAME + " TEXT,"
            + KEY_DATE_OF_BIRTH + " TEXT," + KEY_PHONE_NUMBERS + " TEXT,"
            + KEY_PHOTO + " BLOB," + KEY_INFO + " TEXT," + KEY_LINKED_IN + " TEXT,"
            + KEY_EMAIL + " TEXT," + KEY_LOGINED + " INTEGER" + ")";

    public static synchronized void initInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
    }
    public static DBHelper getInstance() {
        return sInstance;
    }

    private DBHelper(Context context) {
        super(context, "MyDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ADMIN_TABLE);
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

    }

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_ID, contact.getId());
        values.put(KEY_FIRST_NAME, contact.getFirstName());
        values.put(KEY_LAST_NAME, contact.getLastName());
        values.put(KEY_DISPLAY_NAME, contact.getDisplayName());
        values.put(KEY_PHONE_NUMBERS, contact.getPhoneNumber());
        values.put(KEY_PHOTO, contact.getPhoto());
        values.put(KEY_COLOR, contact.getColor());

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {
                KEY_ID, KEY_FIRST_NAME, KEY_LAST_NAME, KEY_DISPLAY_NAME,
                KEY_PHONE_NUMBERS, KEY_PHOTO, KEY_COLOR }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        Contact contact = null;
        if (cursor != null) {
            cursor.moveToFirst();

//            List<String> numbersList = new Gson()
//                    .fromJson(cursor.getString(4),  new TypeToken<List<String>>(){}.getType());

            contact = new Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getBlob(5), cursor.getString(6));
            cursor.close();
        }
        // return alarm
        return contact;
    }

    public void changeContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, contact.getId());
        values.put(KEY_FIRST_NAME, contact.getFirstName());
        values.put(KEY_LAST_NAME, contact.getLastName());
        values.put(KEY_PHONE_NUMBERS, contact.getPhoneNumber());
        values.put(KEY_PHOTO, contact.getPhoto());

        db.update(TABLE_CONTACTS, values, "id = ?",
                new String[] { String.valueOf(contact.getId()) });
    }

    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactsList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS
                + " ORDER BY " + KEY_DISPLAY_NAME + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(cursor.getInt(0));
                contact.setFirstName(cursor.getString(1));
                contact.setLastName(cursor.getString(2));
                contact.setDisplayName(cursor.getString(3));

//                List<String> numbersList = new Gson()
//                        .fromJson(cursor.getString(4),  new TypeToken<List<String>>(){}.getType());

                contact.setPhoneNumber(cursor.getString(4));
                contact.setPhoto(cursor.getBlob(5));
                contact.setColor(cursor.getString(6));
                // Adding contact to list
                contactsList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contacts list
        return contactsList;
    }

    public void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public void deleteAllContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, null, null);
        db.close();
    }


    public int getContactsCount() {
        String countQuery = "SELECT FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void createAdminInfo(Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, Admin.DEFAULT_ID);
        values.put(KEY_LOGIN, Admin.DEFAULT_LOGIN);
        values.put(KEY_PASSWORD, Admin.DEFAULT_PASSWORD);
        values.put(KEY_PHONE_NUMBERS, Admin.DEFAULT_PHONE_NUM);
        values.put(KEY_LINKED_IN, Admin.DEFAULT_LINKED_IN);
        values.put(KEY_EMAIL, Admin.DEFAULT_EMAIL);

        values.put(KEY_FIRST_NAME, context.getString(R.string.admin_first_name));
        values.put(KEY_LAST_NAME, context.getString(R.string.admin_last_name));
        values.put(KEY_DATE_OF_BIRTH, context.getString(R.string.admin_date_of_birth));
        values.put(KEY_INFO, context.getString(R.string.admin_info));
        values.put(KEY_LOGINED, 0);

        values.put(KEY_PHOTO, ImageHelper.getBytesFromRes(context, R.drawable.about_photo));

        // Inserting Row
        db.insert(TABLE_ADMIN, null, values);
        db.close(); // Closing database connection
    }

    public Admin getAdminInfo() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ADMIN, new String[] { KEY_ID, KEY_LOGIN, KEY_PASSWORD,
                KEY_FIRST_NAME, KEY_LAST_NAME, KEY_DATE_OF_BIRTH, KEY_PHONE_NUMBERS, KEY_PHOTO,
                KEY_INFO, KEY_LINKED_IN, KEY_EMAIL, KEY_LOGINED }, KEY_ID + "=?",
                new String[] { String.valueOf(Admin.DEFAULT_ID) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Admin admin = null;
        if (cursor != null) {
            admin = new Admin(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getBlob(7), cursor.getString(8), cursor.getString(9),
                    cursor.getString(10), cursor.getInt(11));
            cursor.close();
        }
        return admin;
    }

    public void setLogined(boolean activated) {
        SQLiteDatabase db = this.getWritableDatabase();

        int intActivated;
        if (activated)
            intActivated = 1;
        else
            intActivated = 0;

        ContentValues values = new ContentValues();
        values.put(KEY_LOGINED, intActivated);

        db.update(TABLE_ADMIN, values, "id = ?",
                new String[] { String.valueOf(Admin.DEFAULT_ID) });
    }

}