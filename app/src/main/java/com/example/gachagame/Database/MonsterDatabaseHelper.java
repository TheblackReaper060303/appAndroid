package com.example.gachagame.Database;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gachagame.Models.Joueur;
import com.example.gachagame.Models.Monster;

import java.util.ArrayList;
import java.util.List;

public class MonsterDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ClickerGameMonster";
    private static final String TABLE_MONSTER = "Monster";
    private static final String COLUMN_MONSTER_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_HP = "hp";
    private static final String COLUMN_ATK = "atk";
    private static final String COLUMN_GOLD = "gold";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_ID = "imageId";

    public MonsterDatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "DatabaseHelper.onCreate ...");

        String script = "CREATE TABLE " + TABLE_MONSTER + "("
                + COLUMN_MONSTER_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_HP + " INTEGER,"
                + COLUMN_ATK + " INTEGER,"
                + COLUMN_GOLD + " INTEGER,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_IMAGE_ID + " INTEGER"+")";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG,"DatabaseHelper.onUpgrade ...");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MONSTER);
        onCreate(db);
    }

    public void createDefaultMonsterIfNeed() {
        int count = this.getMonsterCount();
        if(count==0) {

        }
    }

    public void addMonster(Monster monster) {
        Log.i(TAG,"DatabaseHelper.addMonster ...");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME,monster.getName());
        values.put(COLUMN_HP,monster.getHp());
        values.put(COLUMN_ATK,monster.getAtk());
        values.put(COLUMN_GOLD,monster.getGold());
        values.put(COLUMN_DESCRIPTION,monster.getDescription());
        values.put(COLUMN_IMAGE_ID,monster.getImageResourceId());

        db.insert(TABLE_MONSTER,null,values);

        db.close();
    }

    public Monster getMonster(int id) {
        Log.i(TAG, "MonsterDatabaseHelper.getMonster ...");
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MONSTER, new String[]{COLUMN_MONSTER_ID, COLUMN_NAME, COLUMN_HP, COLUMN_ATK, COLUMN_GOLD, COLUMN_DESCRIPTION, COLUMN_IMAGE_ID}, COLUMN_MONSTER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if(cursor!=null)
            cursor.moveToFirst();

        Monster monster = new Monster(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                Integer.parseInt(cursor.getString(2)),
                Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)),
                cursor.getString(5),
                Integer.parseInt(cursor.getString(6)));

        return monster;
    }

    public List<Monster> getAllMonsters() {
        Log.i(TAG,"DatabaseHelper.getAllMonsters ...");

        List<Monster> monsterList = new ArrayList<>();

        String selectQuery = "SELECT * FROM "+ TABLE_MONSTER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()) {
            do {
                Monster monster = new Monster();
                monster.setId(Integer.parseInt(cursor.getString(0)));
                monster.setName(cursor.getString(1));
                monster.setHp(Integer.parseInt(cursor.getString(2)));
                monster.setAtk(Integer.parseInt(cursor.getString(3)));
                monster.setGold(Integer.parseInt(cursor.getString(4)));
                monster.setDescription(cursor.getString(5));
                monster.setImageResourceId(Integer.parseInt(cursor.getString(6)));
                monsterList.add(monster);

            }while (cursor.moveToNext());
        }
        return monsterList;
    }

    public int getMonsterCount() {
        Log.i(TAG, "DatabaseHelper.getMonsterCount ...");

        String countQuery = "SELECT * FROM " + TABLE_MONSTER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

}