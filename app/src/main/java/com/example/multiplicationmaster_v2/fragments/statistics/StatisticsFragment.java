package com.example.multiplicationmaster_v2.fragments.statistics;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.multiplicationmaster_v2.MainActivity;
import com.example.multiplicationmaster_v2.R;
import com.example.multiplicationmaster_v2.database.DatabaseDAO;
import com.example.multiplicationmaster_v2.database.DatabaseDAOImplement;
import com.example.multiplicationmaster_v2.databinding.FragmentStatisticsBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StatisticsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private FragmentStatisticsBinding binding;
    private ImageView imgAvatarHinnata, imgAvatarItachi, imgAvatarKakashi, imgAvatarNaruto, imgAvatarSasuke; // Imagen del avatar seleccionado y conseguido al completo
    private ArrayList<String> datesList; // Fechas de las tablas practicadas
    private static ArrayList<String[]> tablesList = new ArrayList<>(); // Tablas de multiplicar para una fecha con si Id
    private static ArrayList<String> spinnerTablesList = new ArrayList<>(); // Lista de tablas de una fecha para mostrar en el spinner
    private static ArrayList<String> mistakesList = new ArrayList<>(); // Lista de errores de una tabla seleccionada
    private static ArrayList<String[]> mistakesListWithStatId = new ArrayList<>(); // Lista de errores con el Id del stat
    private Spinner spinnerDates;
    private Spinner spinnerTables;
    private Spinner spinnerUsers;
    private String userName;
    private ArrayList<String> usersList;
    private static String mailDate;
    private int currentTableProgress;
    private ProgressBar progressBarPercentageSuccess;
    private TextView textViewPercentageSuccess;
    private GridLayout gridMistakes;
    private DatabaseDAO databaseDAO; // Conexión con la base de datos


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño y obtener la vista raíz
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        databaseDAO = new DatabaseDAOImplement(MainActivity.getDatabase());

        spinnerUsers = binding.spinnerUsers;
        spinnerDates = binding.spinnerDates;
        spinnerTables = binding.spinnerTables;
        gridMistakes = binding.gridMistakes;
        progressBarPercentageSuccess = binding.pgbPercentageSuccess;
        textViewPercentageSuccess = binding.txvPercentage;

        // Recupera los ImageView de los avatares
        imgAvatarHinnata = binding.imgHinnata;
        imgAvatarItachi = binding.imgItachi;
        imgAvatarKakashi = binding.imgKakashi;
        imgAvatarNaruto = binding.imgNaruto;
        imgAvatarSasuke = binding.imgSasuke;
        configureImages(); // Configura el color de las imagenes en blanco y negro

        checkUserConect(); // Verifica el usuario conectado

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void checkUserConect() {
        if (MainActivity.getUserConect().equalsIgnoreCase("Administrador")) {
            usersList = databaseDAO.getUsers(); // Recupera los usuarios de la base de datos
            configureSpinnerUsers(usersList);
        } else {
            spinnerUsers.setVisibility(View.INVISIBLE); // Oculta el spinner de los usuarios
            userName = MainActivity.getUserConect(); // Obtiene el usuario conectado
            datesList = databaseDAO.getDatesByUserName(userName);
            configureSpinnerDate(datesList); // Configura el spinner de las fechas para el usuario conectado
            addAvatars(userName); // Añade los avatares conseguidos para el usuario conectado
        }
    }

    // Configura la imagenes de los avatares en blanco y negro
    private void configureImages() {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f); // Establece la matriz para la saturacion de colores (black&white)

        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix); // Establece el filtro para la imagen en base a la matriz de creada

        imgAvatarHinnata.setColorFilter(colorFilter);
        imgAvatarItachi.setColorFilter(colorFilter);
        imgAvatarKakashi.setColorFilter(colorFilter);
        imgAvatarSasuke.setColorFilter(colorFilter);
        imgAvatarNaruto.setColorFilter(colorFilter);
    }

    // Configura el spinner para los usuarios en la base de datos
    private void configureSpinnerUsers(ArrayList<String> users) {
        users.remove(0); // Elimina el usuario Administrador de la lista
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUsers.setAdapter(adapter);
        spinnerUsers.setOnItemSelectedListener(this);
    }

    // Configura el Spinner de las fechas
    private void configureSpinnerDate(ArrayList<String> datesList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, datesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDates.setAdapter(adapter);
        spinnerDates.setOnItemSelectedListener(this);
    }

    // Configura el Spinner de las tablas de multiplicar
    private void configureSpinnerTables(String selectedDate, String userName) {
        tablesList = databaseDAO.getTablesByDate(selectedDate, userName); // Recupera las tablas de una fecha con su StatId para un usuario concreto
        setTablesList(tablesList);

        // Crea una lista de las tablas de una fecha para mostrar en el spinner
        spinnerTablesList = new ArrayList<>();
        for (String[] table : tablesList) {
            spinnerTablesList.add(table[1]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, spinnerTablesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTables.setAdapter(adapter);
        spinnerTables.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Controla el spinner que se esta usando
        if (parent == spinnerUsers) {
            userName = parent.getItemAtPosition(position).toString(); // Guarda el usuario seleccionado en el spinner
            datesList = databaseDAO.getDatesByUserName(userName); // Recupera las fechas practicadas para el usuario seleccionado y las configura en el spinner
            configureSpinnerDate(datesList); // Configura el Spinner para seleccionar la fecha
            if (datesList.isEmpty()){
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String currentDate = sdf.format(new Date());
                configureSpinnerTables(currentDate, userName);
                gridMistakes.removeAllViews();
                progressBarPercentageSuccess.setProgress(0);
                textViewPercentageSuccess.setText("");
            }
            configureImages(); // Configura el color de las imagenes en Blanco y Negro
            addAvatars(userName); // Añade los avatares conseguidos para el usuario seleccionado
        } else if (parent == spinnerDates) {
            String selectedDate = parent.getItemAtPosition(position).toString();
            setMailDate(selectedDate);// Guarda la fecha seleccionada en el spinner para mostrar en el mail
            //mailDate = selectedDate;
            configureSpinnerTables(selectedDate, userName);
            mistakesListWithStatId = databaseDAO.getMistakesWithStatId();
            setMistakesListWithStatId(mistakesListWithStatId);
        } else if (parent == spinnerTables) {
            addMistakes(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Añade los errores cometidos al GridLayout
    private void addMistakes(int position) {
        gridMistakes.removeAllViews(); // Elimina los errores de la tabla de multiplicar anteriormente seleccionada

        // Obtener el Id_Stat de la tabla y fecha seleccionadas
        String idStat = tablesList.get(position)[0];

        // Obtener los errores y el porcentaje de aciertos usando el Id_Stat
        mistakesList = databaseDAO.getMistakesByStatId(idStat);
        int successPercentage = databaseDAO.getSuccessPercentageByStatId(idStat);

        // Añade los errores a la GridLayout en relación a la tabla de multiplicar seleccionada en el spinner
        if (mistakesList.size() > 0) {
            for (String mistake : mistakesList) {
                TextView textViewMistake = createMistakeTextView(mistake);
                gridMistakes.addView(textViewMistake);
            }
        } else {
            TextView textViewMistake = createMistakeTextView(getString(R.string.message_noMistakes));
            gridMistakes.addView(textViewMistake);
        }

        // Añade los porcentajes de aciertos
        addPercentagesSuccess(successPercentage);
    }

    // Crea el TextView para mostrar en el gridLayout de los errores
    private TextView createMistakeTextView(String text) {
        TextView textViewMistake = new TextView(getContext());
        textViewMistake.setPadding(10, 0, 50, 10);
        textViewMistake.setTextSize(16);
        textViewMistake.setTextColor(Color.BLACK);
        textViewMistake.setText(text);
        return textViewMistake;
    }

    // Añade los porcentajes de aciertos

    @SuppressLint("SetTextI18n")
    private void addPercentagesSuccess(int successPercentage) {
        progressBarPercentageSuccess.setMax(100);
        // Obtiene el TextView que muestra el porcentaje de aciertos

        progressBarPercentageSuccess.setProgress(successPercentage);
        textViewPercentageSuccess.setText(successPercentage + " %");
    }

    // Añade los avatares conseguidos
    private void addAvatars(String userName) {
        // Obtiene la lista de avatares conseguidosal completo para un usuario concreto
        ArrayList<String> avatarsCompleted = databaseDAO.getAvatarsByUserName(userName);

        // Comprueba si hay avatares conseguidos y los muestra
        if (avatarsCompleted.size() > 0) {
            // Muestra el avatar completado en la ImageView correspondiente
            for (String avatarCompleted : avatarsCompleted) {
                switch (avatarCompleted) {
                    case "Hinata":
                        imgAvatarHinnata.clearColorFilter();
                        break;
                    case "Itachi":
                        imgAvatarItachi.clearColorFilter();
                        break;
                    case "Kakashi":
                        imgAvatarKakashi.clearColorFilter();
                        break;
                    case "Naruto":
                        imgAvatarNaruto.clearColorFilter();
                        break;
                    case "Sasuke":
                        imgAvatarSasuke.clearColorFilter();
                        break;
                    case "null":
                        break;
                }
            }
        }

    }

    // Getter and setter para la lista de tablas mostradas en el spinner
    public static ArrayList<String[]> getTablesList() {
        return tablesList;
    }

    public static void setTablesList(ArrayList<String[]> tablesList) {
        StatisticsFragment.tablesList = tablesList;
    }

    // Getter and setter para la lista de errores
    public static ArrayList<String[]> getMistakesListWithStatId() {
        return mistakesListWithStatId;
    }

    public static void setMistakesListWithStatId(ArrayList<String[]> mistakesListWithStatId) {
        StatisticsFragment.mistakesListWithStatId = mistakesListWithStatId;
    }

    // Getter and setter para la fecha para poner en el mail
    public static String getMailDate() {
        return mailDate;
    }

    public static void setMailDate(String mailDate) {
        StatisticsFragment.mailDate = mailDate;
    }
}
