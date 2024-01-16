package com.example.multiplicationmaster_v2.database;

import com.example.multiplicationmaster_v2.fragments.contacts.Contact;

import java.util.ArrayList;

public interface DatabaseDAO {
    /*// Metodo para crear la base de datos
    public void createDatabase(){
        database = openOrCreateDatabase("MultiplicationMaster", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS USERS (" +
                "Id_User INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name VARCHAR(255) NOT NULL," +
                "Password INTEGER);");

        database.execSQL("CREATE TABLE IF NOT EXISTS STATISTICS (" +
                "Id_Stat INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Date VARCHAR(255) not null," +
                "Multiplication_Table VARCHAR(255)," +
                "Success_Percentage VARCHAR(255)," +
                "Avatar VARCHAR(255)," +
                "Id_User INTEGER REFERENCES USERS(Id_User));");

        database.execSQL("CREATE TABLE IF NOT EXISTS MISTAKES (" +
                "Id_Mistake INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Mistake VARCHAR(255)," +
                "Id_Stat INTEGER REFERENCES STATISTICS(Id_Stat));");
    }*/

    ArrayList<String> getUsers();
    void addUser(String newUser);
    void addAdmin();
    String getUserPasswordByName(String userName);
    int getUserIdByName(String userName);
    void addStatistics(String date, String table, String successPercentage, String avatar, String userName, ArrayList<String> mistakes);
    void addMistake(long idStats, ArrayList<String> mistake);
    void deleteStatistics();
    ArrayList<String> getDatesByUserName(String userName);
    ArrayList<String []> getTablesByDate(String date, String userName);
    ArrayList<String> getMistakesByStatId(String idStat);
    ArrayList<String []> getMistakesWithStatId();
    int getSuccessPercentageByStatId(String idStat);
    ArrayList<String> getAvatarsByUserName(String userName);
    void addFavoriteContact(Contact contact);

    void deleteFavoriteContact(Contact contact);

    ArrayList<Contact> getFavoriteContacts();
}
