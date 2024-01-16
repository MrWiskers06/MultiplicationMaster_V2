package com.example.multiplicationmaster_v2.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.multiplicationmaster_v2.fragments.contacts.Contact;

import java.util.ArrayList;

public class DatabaseDAOImplement implements DatabaseDAO {
    private SQLiteDatabase database;
    public DatabaseDAOImplement(SQLiteDatabase database) {
        this.database = database;
    }


    @Override
    public ArrayList<String> getUsers() {
        ArrayList<String> users = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Name FROM USERS", null);
        if (cursor.moveToFirst()) {
            do {
                users.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    @Override
    public void addUser(String newUser) {
        ContentValues values = new ContentValues();
        if (newUser.equalsIgnoreCase("Administrador") || newUser.equalsIgnoreCase("Mama") || newUser.equalsIgnoreCase("Papa")){
            values.put("Name", newUser);
            values.put("Password", "1234");
        }else{
            values.put("Name", newUser);
        }
        database.insert("USERS", null, values);
    }

    @Override
    public void addAdmin() {
        ContentValues values = new ContentValues();
        values.put("Name", "Administrador");
        values.put("Password", "1234");
        database.insert("USERS", null, values);
    }

    @Override
    public String getUserPasswordByName(String userName) {
        String userPassword = "";

        Cursor cursor = database.rawQuery("Select Password from users where name = ?", new String[]{userName});
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            userPassword = cursor.getString(0);
        }

        return userPassword;
    }

    @Override
    public int getUserIdByName(String userName) {
        int idUser;
        Cursor cursor = database.rawQuery("SELECT Id_User FROM USERS WHERE Name = ?", new String[]{userName});

        if (cursor.getCount() == 0) {
            return -1;
        } else {
            cursor.moveToFirst();
            idUser = cursor.getInt(0);
        }

        cursor.close();
        return idUser;
    }
    @Override
    public void addStatistics(String date, String table, String successPercentage, String avatar, String userName, ArrayList<String> mistakes) {
        ContentValues values = new ContentValues();
        values.put("Date", date);
        values.put("Multiplication_Table", table);
        values.put("Success_Percentage", successPercentage);
        values.put("Avatar", avatar);
        values.put("Id_User", getUserIdByName(userName));

        long idStats = database.insert("STATISTICS", null, values);

        addMistake(idStats, mistakes);
    }
    @Override
    public void addMistake(long idStats, ArrayList<String> mistakes) {
        ContentValues values = new ContentValues();
        for (int i = 0; i < mistakes.size(); i++) {
            values.put("Mistake", mistakes.get(i));
            values.put("Id_Stat", idStats);
            database.insert("MISTAKES", null, values);
        }
    }
    @Override
    public void deleteStatistics() {
        database.execSQL("DELETE FROM MISTAKES");
        // Reiniciar el autoincrement en la tabla MISTAKES
        database.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = 'MISTAKES'");

        database.execSQL("DELETE FROM STATISTICS");
        // Reiniciar el autoincrement en la tabla STATISTICS
        database.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = 'STATISTICS'");
    }
    @Override
    public ArrayList<String> getDatesByUserName(String userName) {
        ArrayList<String> datesList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT DISTINCT Date FROM STATISTICS WHERE Id_User = ?",
                new String[]{String.valueOf(getUserIdByName(userName))});

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                datesList.add(cursor.getString(0));
            }
        }

        cursor.close();
        return datesList;
    }
    @Override
    public ArrayList<String []> getTablesByDate(String date, String userName) {
        ArrayList<String []> tablesList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT Id_Stat, Multiplication_Table FROM STATISTICS WHERE Date = ? AND Id_User = ?",
                new String[]{date, String.valueOf(getUserIdByName(userName))});

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String [] table = {cursor.getString(0), cursor.getString(1)};
                tablesList.add(table);
            }
        }

        cursor.close();
        return tablesList;
    }
    @Override
    public ArrayList<String> getMistakesByStatId(String idStat) {
        ArrayList<String> mistakesList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT Mistake FROM MISTAKES WHERE Id_Stat = ?",
                new String[]{idStat});

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                mistakesList.add(cursor.getString(0));
            }
        }

        cursor.close();
        return mistakesList;
    }
    @Override
    public ArrayList<String[]> getMistakesWithStatId() {
        ArrayList<String[]> mistakesListWithStatId = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT Id_Stat, Mistake FROM MISTAKES", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String[] mistake = {cursor.getString(0), cursor.getString(1)};
                mistakesListWithStatId.add(mistake);
            }
        }
        cursor.close();
        return mistakesListWithStatId;
    }
    @Override
    public int getSuccessPercentageByStatId(String idStat) {
        int successPercentage = 0;

        Cursor cursor = database.rawQuery("SELECT Success_Percentage FROM STATISTICS WHERE Id_Stat = ?",
                new String[]{idStat});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            successPercentage = cursor.getInt(0);
        }

        cursor.close();
        return successPercentage;
    }
    @Override
    public ArrayList<String> getAvatarsByUserName(String userName) {
        ArrayList<String> avatarsList = new ArrayList<>();
        String userId = String.valueOf(getUserIdByName(userName));

        Cursor cursor = database.rawQuery("SELECT DISTINCT Avatar FROM STATISTICS WHERE Id_User = ? AND Avatar IS NOT NULL",
                new String[]{userId});

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                avatarsList.add(cursor.getString(0));
            }
        }

        cursor.close();
        return avatarsList;
    }

    @Override
    public void addFavoriteContact(Contact contact) {
        ContentValues values = new ContentValues();
        values.put("Name", contact.getName());
        values.put("Email", contact.getEmail());

        database.insert("FAVORITE_CONTACTS", null, values);
    }

    @Override
    public void deleteFavoriteContact(Contact contact) {
        String name = contact.getName();
        database.execSQL("DELETE FROM FAVORITE_CONTACTS WHERE NAME = ?", new String[]{name});
    }

    @Override
    public ArrayList<Contact> getFavoriteContacts() {
        ArrayList<Contact> favContactsList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM FAVORITE_CONTACTS", null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Contact contact = new Contact();
                contact.setName(cursor.getString(0));
                contact.setEmail(cursor.getString(1));
                favContactsList.add(contact);
            }
        }

        cursor.close();
        return favContactsList;
    }
}
