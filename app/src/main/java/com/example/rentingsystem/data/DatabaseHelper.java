package com.example.rentingsystem.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RentingSystem.db";
    private static final int DATABASE_VERSION = 4;

    // Table Names
    public static final String TABLE_USER = "user";
    public static final String TABLE_HOUSE = "house";
    public static final String TABLE_RESERVATION = "reservation";
    public static final String TABLE_APPLICATION = "application";
    public static final String TABLE_CONTRACT = "contract";
    public static final String TABLE_MESSAGE = "message";
    public static final String TABLE_NOTICE = "notice";

    // Create User Table
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " ("
            + "user_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + "username VARCHAR(50) UNIQUE NOT NULL, "
            + "password VARCHAR(100) NOT NULL, "
            + "real_name VARCHAR(20) NOT NULL, "
            + "phone VARCHAR(11) UNIQUE NOT NULL, "
            + "identity_type TINYINT NOT NULL DEFAULT 1, "
            + "create_time DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ");";

    // Create House Table
    private static final String CREATE_TABLE_HOUSE = "CREATE TABLE " + TABLE_HOUSE + " ("
            + "house_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + "landlord_id INTEGER NOT NULL, "
            + "title VARCHAR(100) NOT NULL, "
            + "address VARCHAR(200) NOT NULL, "
            + "rent DECIMAL(10,2) NOT NULL, "
            + "house_type VARCHAR(20) NOT NULL, "
            + "area DECIMAL(8,2) NOT NULL, "
            + "status TINYINT NOT NULL DEFAULT 1, "
            + "image_path VARCHAR(255), "
            + "current_contract_id INTEGER, "
            + "create_time DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + "FOREIGN KEY(landlord_id) REFERENCES " + TABLE_USER + "(user_id), "
            + "FOREIGN KEY(current_contract_id) REFERENCES " + TABLE_CONTRACT + "(contract_id)"
            + ");";

    // Create Reservation Table
    private static final String CREATE_TABLE_RESERVATION = "CREATE TABLE " + TABLE_RESERVATION + " ("
            + "reserve_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + "tenant_id INTEGER NOT NULL, "
            + "house_id INTEGER NOT NULL, "
            + "reserve_time DATETIME NOT NULL, "
            + "remark VARCHAR(200), "
            + "status TINYINT NOT NULL DEFAULT 0, " // 0=Pending
            + "refuse_reason VARCHAR(200), "
            + "create_time DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + "FOREIGN KEY(tenant_id) REFERENCES " + TABLE_USER + "(user_id), "
            + "FOREIGN KEY(house_id) REFERENCES " + TABLE_HOUSE + "(house_id)"
            + ");";

    // Create Application Table
    private static final String CREATE_TABLE_APPLICATION = "CREATE TABLE " + TABLE_APPLICATION + " ("
            + "apply_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + "tenant_id INTEGER NOT NULL, "
            + "house_id INTEGER NOT NULL, "
            + "apply_reason VARCHAR(200), "
            + "start_date DATE, "
            + "end_date DATE, "
            + "status TINYINT NOT NULL DEFAULT 1, "
            + "refuse_reason VARCHAR(200), "
            + "create_time DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + "FOREIGN KEY(tenant_id) REFERENCES " + TABLE_USER + "(user_id), "
            + "FOREIGN KEY(house_id) REFERENCES " + TABLE_HOUSE + "(house_id)"
            + ");";

    // Create Contract Table
    private static final String CREATE_TABLE_CONTRACT = "CREATE TABLE " + TABLE_CONTRACT + " ("
            + "contract_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + "tenant_id INTEGER NOT NULL, "
            + "landlord_id INTEGER NOT NULL, "
            + "house_id INTEGER NOT NULL, "
            + "start_date DATE NOT NULL, "
            + "end_date DATE NOT NULL, "
            + "rent DECIMAL(10,2) NOT NULL, "
            + "status TINYINT NOT NULL DEFAULT 1, "
            + "create_time DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + "FOREIGN KEY(tenant_id) REFERENCES " + TABLE_USER + "(user_id), "
            + "FOREIGN KEY(landlord_id) REFERENCES " + TABLE_USER + "(user_id), "
            + "FOREIGN KEY(house_id) REFERENCES " + TABLE_HOUSE + "(house_id)"
            + ");";

    // Create Message Table
    private static final String CREATE_TABLE_MESSAGE = "CREATE TABLE " + TABLE_MESSAGE + " ("
            + "msg_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + "sender_id INTEGER NOT NULL, "
            + "receiver_id INTEGER NOT NULL, "
            + "content TEXT NOT NULL, "
            + "is_read TINYINT NOT NULL DEFAULT 0, "
            + "create_time DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + "FOREIGN KEY(sender_id) REFERENCES " + TABLE_USER + "(user_id), "
            + "FOREIGN KEY(receiver_id) REFERENCES " + TABLE_USER + "(user_id)"
            + ");";

    // Create Notice Table
    private static final String CREATE_TABLE_NOTICE = "CREATE TABLE " + TABLE_NOTICE + " ("
            + "notice_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + "publisher_id INTEGER NOT NULL, "
            + "title VARCHAR(100) NOT NULL, "
            + "content TEXT NOT NULL, "
            + "receiver_id INTEGER NOT NULL, "
            + "is_read TINYINT NOT NULL DEFAULT 0, "
            + "create_time DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + "FOREIGN KEY(publisher_id) REFERENCES " + TABLE_USER + "(user_id), "
            + "FOREIGN KEY(receiver_id) REFERENCES " + TABLE_USER + "(user_id)"
            + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        // Create Contract before House because House references Contract (circular dependency check needed? SQLite usually allows if table doesn't exist but constraint fails later, but let's see. 
        // House has current_contract_id FK to Contract. Contract has house_id FK to House.
        // SQLite doesn't enforce FK order during creation if FK constraints are not enabled or deferred, but good practice.
        // Actually SQLite enables foreign keys by PRAGMA foreign_keys = ON; usually off by default.
        // I will create tables in order but circular dependency exists.
        // It's safer to create tables then add constraints, but SQLite CREATE TABLE syntax includes constraints.
        // Circular dependency is fine in SQLite creation if we don't insert data yet.
        
        db.execSQL(CREATE_TABLE_CONTRACT); 
        db.execSQL(CREATE_TABLE_HOUSE);
        db.execSQL(CREATE_TABLE_RESERVATION);
        db.execSQL(CREATE_TABLE_APPLICATION);
        db.execSQL(CREATE_TABLE_MESSAGE);
        db.execSQL(CREATE_TABLE_NOTICE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPLICATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTRACT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
    
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
