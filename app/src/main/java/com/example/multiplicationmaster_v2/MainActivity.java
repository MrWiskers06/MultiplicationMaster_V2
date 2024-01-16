package com.example.multiplicationmaster_v2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.multiplicationmaster_v2.database.DatabaseDAO;
import com.example.multiplicationmaster_v2.database.DatabaseDAOImplement;
import com.example.multiplicationmaster_v2.databinding.ActivityMainBinding;
import com.example.multiplicationmaster_v2.dialogs.DeleteStatisticsDialog;
import com.example.multiplicationmaster_v2.dialogs.DifficultyDialogListener;
import com.example.multiplicationmaster_v2.dialogs.StatisticsDialog;
import com.example.multiplicationmaster_v2.fragments.appSettings.SettingsFragment;
import com.example.multiplicationmaster_v2.fragments.contacts.ContactsFragment;
import com.example.multiplicationmaster_v2.fragments.contacts.FavoriteContactsFragment;
import com.example.multiplicationmaster_v2.fragments.login.LoginFragment;
import com.example.multiplicationmaster_v2.fragments.statistics.StatisticsFragment;
import com.example.multiplicationmaster_v2.fragments.train.TrainFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DifficultyDialogListener {

    private static ActivityMainBinding binding; // Enlace de datos
    private FragmentTransaction transaction;
    private static final int[] AVATAR_ITACHI = {
            R.drawable.itachi_1, R.drawable.itachi_2, R.drawable.itachi_3,
            R.drawable.itachi_4, R.drawable.itachi_5, R.drawable.itachi_6,
            R.drawable.itachi_7, R.drawable.itachi_8,
            R.drawable.itachi_9, R.drawable.itachi_9
    };

    private static final int[] AVATAR_HINATA = {
            R.drawable.hinnata_1, R.drawable.hinnata_2, R.drawable.hinnata_3,
            R.drawable.hinnata_4, R.drawable.hinnata_5, R.drawable.hinnata_6,
            R.drawable.hinnata_7, R.drawable.hinnata_8,
            R.drawable.hinnata_9, R.drawable.hinnata_9
    };

    private static final int[] AVATAR_NARUTO = {
            R.drawable.naruto_1, R.drawable.naruto_2, R.drawable.naruto_3,
            R.drawable.naruto_4, R.drawable.naruto_5, R.drawable.naruto_6,
            R.drawable.naruto_7, R.drawable.naruto_8,
            R.drawable.naruto_9, R.drawable.naruto_9
    };

    private static final int[] AVATAR_SASUKE = {
            R.drawable.sasuke_1, R.drawable.sasuke_2, R.drawable.sasuke_3,
            R.drawable.sasuke_4, R.drawable.sasuke_5, R.drawable.sasuke_6,
            R.drawable.sasuke_7, R.drawable.sasuke_8,
            R.drawable.sasuke_9, R.drawable.sasuke_9
    };

    private static final int[] AVATAR_KAKASHI = {
            R.drawable.kakashi_1, R.drawable.kakashi_2, R.drawable.kakashi_3,
            R.drawable.kakashi_4, R.drawable.kakashi_5, R.drawable.kakashi_6,
            R.drawable.kakashi_7, R.drawable.kakashi_8,
            R.drawable.kakashi_9, R.drawable.kakashi_9
    };

    private static String avatarSelected;
    private static int avatarImgSelected;
    private static String tableSelected; // Recupera la tabla de multiplicar seleccionada
    private static int difficultySelected = 0; // Nivel de dificultad seleccionado por defecto
    private static ArrayList<String> percentegesSuccess = new ArrayList<>(); // Porcentajes de aciertos de las tablas completadas
    private static SQLiteDatabase database;
    private DatabaseDAO databaseDAO;
    private static String userConect;
    private ActivityResultLauncher<String> requestReadContactsPermissionLauncher; // Permisos de lectura de contactos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // Inflar el diseño y obtener la vista raíz
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Desactivar el modo noche

        initializeLuncherPermission(); //Define el launcher para el permiso de lectura de contactos
        requestReadPermission(); // Llama al launcher para solicitar el permiso de lectura de contactos

        binding.navView.getMenu().findItem(R.id.navigation_settings).setVisible(false);
        binding.navView.getMenu().findItem(R.id.navigation_train).setVisible(false);
        binding.navView.getMenu().findItem(R.id.navigation_statistics).setVisible(false);
        binding.navView.getMenu().findItem(R.id.navigation_contacts).setVisible(false);
        binding.navView.getMenu().findItem(R.id.navigation_favoriteContacts).setVisible(false);
        binding.navView.getMenu().findItem(R.id.navigation_sendStatistics).setVisible(false);
        binding.navView.getMenu().findItem(R.id.navigation_deleteStatistics).setVisible(false);

        userConect = String.valueOf(binding.drawerLayout.findViewById(R.id.txv_userConect));

        database = openOrCreateDatabase("MultiplicationMaster", MODE_PRIVATE, null); // Abre o crea la base de datos
        createDatabase(); // Crear la base de datos
        databaseDAO = new DatabaseDAOImplement(database); // Vaiable para implementar el DAO contra la base de datos

        createDefaultAdmin(); // Si no hay ningun usuario, crea un usuario admin por defecto

        // Cargar el fragmento de la pantalla principal con el Logger por defecto
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, new LoginFragment());
        transaction.commit();

        configureDrawerLayoutAndNavegationView(); // Configurar el DrawerLayout y el NavigationView
    }

    // Configuración del DrawerLayout y el NavigationView
    private void configureDrawerLayoutAndNavegationView() {
        setSupportActionBar(binding.appBarMain.toolbar); // Configurar la barra de herramientas

        // Configurar el Navigation Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.appBarMain.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        binding.drawerLayout.addDrawerListener(toggle); // Añadir el listener al Navigation Drawer
        toggle.syncState(); // Sincronizar el estado del Navigation Drawer con el icono de la barra de herramientas

        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                transaction = getSupportFragmentManager().beginTransaction(); // Iniciar la transacción
                int id = item.getItemId(); // Obtener el id del item seleccionado

                // Dependiendo del item seleccionado, se reemplaza el fragmento
                if (id == R.id.navigation_settings) {
                    transaction.replace(R.id.fragmentContainer, new SettingsFragment());
                    transaction.commit();
                } else if (id == R.id.navigation_train) {
                    transaction.replace(R.id.fragmentContainer, new TrainFragment());
                    transaction.commit();
                } else if (id == R.id.navigation_statistics) {
                    transaction.replace(R.id.fragmentContainer, new StatisticsFragment());
                    transaction.commit();
                } else if (id == R.id.navigation_sendStatistics) {
                    StatisticsDialog statisticsDialog = new StatisticsDialog();
                    statisticsDialog.show(getSupportFragmentManager(), "statisticsDialog");
                } else if (id == R.id.navigation_deleteStatistics) {
                    DeleteStatisticsDialog deleteStatisticsDialog = new DeleteStatisticsDialog();
                    deleteStatisticsDialog.show(getSupportFragmentManager(), "deleteStatisticsdialog");
                } else if (id == R.id.navigation_logger) {
                    transaction.replace(R.id.fragmentContainer, new LoginFragment());
                    transaction.commit();
                }else if (id == R.id.navigation_contacts) {
                    transaction.replace(R.id.fragmentContainer, new ContactsFragment());
                    transaction.commit();
                }else if (id == R.id.navigation_favoriteContacts) {
                    transaction.replace(R.id.fragmentContainer, new FavoriteContactsFragment());
                    transaction.commit();
                }

                // Cerrar el DrawerLayout después de seleccionar un elemento
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    // Inicializa el launcher para solicitar permisos de lectura de contactos
    private void initializeLuncherPermission() {

        requestReadContactsPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isReadContactsGranted -> {
            if (isReadContactsGranted) {
                // Si se otorgan los permisos, solicitar la información de contactos
                requestReadPermission();
            } else {
                Log.d("ContactsFragment", "Permission denied, cannot display application content");
            }
        });
    }
    // Metodo para verificar y solicitar permisos de lectura de contactos si no se tienen
    private void requestReadPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestReadContactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
        }
    }

    //Getter and setter for table selection
    public static String getTableSelected() {
        return tableSelected;
    }
    public static void setTableSelected(String tableSelected) {
        MainActivity.tableSelected = tableSelected;
    }

    // Getter and setter for Difficulty
    public static int getDifficultySelected() {
        return difficultySelected;
    }
    public static void setDifficultySelected(int difficultySelected) {
        MainActivity.difficultySelected = difficultySelected;
    }

    //Getter and setter para el avatar y avatarImage
    public static String getAvatarSelected() {
        return avatarSelected;
    }
    public static void setAvatarSelected(String avatarSelected) {
        MainActivity.avatarSelected = avatarSelected;
    }
    public static int getAvatarImgSelected() {
        return avatarImgSelected;
    }
    public static void setAvatarImgSelected(int avatarImgSelected) {
        MainActivity.avatarImgSelected = avatarImgSelected;
    }
    public static int[] getAvatarProgressImages(String avatarName) {
        switch (avatarName) {
            case "Itachi":
                return AVATAR_ITACHI;
            case "Hinata":
                return AVATAR_HINATA;
            case "Naruto":
                return AVATAR_NARUTO;
            case "Sasuke":
                return AVATAR_SASUKE;
            case "Kakashi":
                return AVATAR_KAKASHI;
            default:
                return null;
        }
    }

    //Getter and setter para los porcentajes de aciertos
    public static ArrayList<String> getPercentegesSuccess() {
        return percentegesSuccess;
    }
    public static void setPercentegesSuccess(ArrayList<String> percentegesSuccess) {
        MainActivity.percentegesSuccess = percentegesSuccess;
    }

    // Getter and setter para la base de datos
    public static SQLiteDatabase getDatabase() {
        return database;
    }
    public static void setDatabase(SQLiteDatabase database) {
        MainActivity.database = database;
    }

    // Getter and setter para el usuario conectado
    public static String getUserConect() {
        return userConect;
    }
    public static void setUserConect(String userConect) {
        MainActivity.userConect = userConect;
        TextView txvUserConected = binding.drawerLayout.findViewById(R.id.txv_userConect);
        txvUserConected.setText(userConect);
    }
    //Implementacion de la interfaz para la dificultad
    @Override
    public void onChangeDifficulty(int level) {
        difficultySelected = level;
    }

    // Metodo para crear la base de datos
    public void createDatabase(){
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

        database.execSQL("CREATE TABLE IF NOT EXISTS FAVORITE_CONTACTS (" +
                "Name VARCHAR(255) PRIMARY KEY," +
                "Email VARCHAR(255));");
    }

    // Crea un usuario administrador por defecto en caso de no haber ningun usuario en la base de datos
    public void createDefaultAdmin(){
        Cursor cursor = database.rawQuery("SELECT * FROM USERS", null);
        int users = cursor.getCount();

        if (users == 0){
            databaseDAO.addAdmin();
        }
    }
    // Modifica el DrawerLayout para el usuario seleccionado
    public static void setDrawerNav(String userName) {
        if (userName.equalsIgnoreCase("administrador")) {
            binding.navView.getMenu().findItem(R.id.navigation_settings).setVisible(false);
            binding.navView.getMenu().findItem(R.id.navigation_train).setVisible(false);
            binding.navView.getMenu().findItem(R.id.navigation_statistics).setVisible(true);
            binding.navView.getMenu().findItem(R.id.navigation_contacts).setVisible(true);
            binding.navView.getMenu().findItem(R.id.navigation_favoriteContacts).setVisible(true);
            binding.navView.getMenu().findItem(R.id.navigation_sendStatistics).setVisible(true);
            binding.navView.getMenu().findItem(R.id.navigation_deleteStatistics).setVisible(true);
        }else {
            binding.navView.getMenu().findItem(R.id.navigation_settings).setVisible(true);
            binding.navView.getMenu().findItem(R.id.navigation_train).setVisible(true);
            binding.navView.getMenu().findItem(R.id.navigation_statistics).setVisible(true);
            binding.navView.getMenu().findItem(R.id.navigation_contacts).setVisible(false);
            binding.navView.getMenu().findItem(R.id.navigation_favoriteContacts).setVisible(false);
            binding.navView.getMenu().findItem(R.id.navigation_sendStatistics).setVisible(false);
            binding.navView.getMenu().findItem(R.id.navigation_deleteStatistics).setVisible(false);
        }

    }
}