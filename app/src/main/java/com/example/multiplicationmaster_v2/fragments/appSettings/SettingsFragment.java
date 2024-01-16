package com.example.multiplicationmaster_v2.fragments.appSettings;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.multiplicationmaster_v2.MainActivity;
import com.example.multiplicationmaster_v2.R;
import com.example.multiplicationmaster_v2.databinding.FragmentSettingsBinding;
import com.example.multiplicationmaster_v2.dialogs.DifficultyDialog;


public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private FragmentSettingsBinding binding; // Clase autogenerada que representa el binding con el layout del fragmento
    private final String[] AVATARS = {"Itachi", "Hinata", "Sasuke", "Kakashi", "Naruto"};
    private final int[] AVATAR_IMAGES = {R.drawable.itachi_9, R.drawable.hinnata_9, R.drawable.sasuke_9, R.drawable.kakashi_9, R.drawable.naruto_9};
    private Button lastSelectedButton = null; // Almacena el último botón de tabla de multiplicar seleccionado
    private int difficultySelected = MainActivity.getDifficultySelected(); // Nivel de dificultad seleccionado

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflar el diseño y obtener la vista raíz
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar el Spinner
        configureSpinner();

        // Añade los botones de las tablas de multiplicar
        addButtons();

        // Configurar el OnClickListener del botón dificultad
        configureDifficultyButton();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Configura el Spinner
    private void configureSpinner() {
        Spinner spinnerAvatar = binding.spinnerAvatar;
        CustomSpinnerAdapter personalSpinner = new CustomSpinnerAdapter(SettingsFragment.super.getContext(), R.layout.spinner_lines, AVATARS, AVATAR_IMAGES);
        spinnerAvatar.setAdapter(personalSpinner);
        spinnerAvatar.setOnItemSelectedListener(this);
    }

    // Añade los botones de las tablas de multiplicar
    private void addButtons() {
        GridLayout buttonsGrid = binding.gridButtons;

        for (int i = 0; i <= 11; i++) {
            Button button = createButton(i);
            buttonsGrid.addView(button, i);
        }
    }

    // Crear un botón con parámetros comunes
    @SuppressLint("SetTextI18n")
    private Button createButton(int i) {
        Button button = new Button(getContext());

        // Establecer los parámetros de diseño del botón
        int buttonSizeInDp = 60; // Tamaño deseado en dp
        int marginInDp = 6; // Márgenes deseados en dp
        int buttonSizeInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, buttonSizeInDp, getResources().getDisplayMetrics());
        int marginInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginInDp, getResources().getDisplayMetrics());

        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(buttonSizeInPx, buttonSizeInPx);
        params.setMargins(marginInPx, marginInPx, marginInPx, marginInPx);
        button.setLayoutParams(params);

        button.setId(View.generateViewId()); // Genera un ID único para el botón
        button.setTextSize(18); // Modifica el tamaño de los números

        if (i == 11) {
            button.setText("?");
        } else {
            button.setText(String.valueOf(i));
        }

        button.setTextColor(Color.BLACK);
        button.setBackgroundResource(R.drawable.round_button_background); // Configurar el fondo para que sea un óvalo (shape oval)
        button.setOnClickListener(this::onClickTableNumber);

        return button;
    }

    // Configurar el botón dificultad
    private void configureDifficultyButton() {
        Button btnSelectDifficulty = binding.btnSelectDifficulty;
        btnSelectDifficulty.setOnClickListener(this::onClickDifficulty);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MainActivity.setAvatarSelected(AVATARS[position]); // Obtiene el nombre del avatar seleccionado
        MainActivity.setAvatarImgSelected(AVATAR_IMAGES[position]); // Obtiene el recurso de la imagen del avatar seleccionado
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Lógica cuando no se selecciona ningún elemento en el Spinner
        // Puedes implementar lógica adicional para manejar este caso
    }
    private void selectUnselectButton(Button button) {
        // Desmarca el último botón seleccionado y restaura su color original
        if (lastSelectedButton != null) {
            lastSelectedButton.setSelected(false); // Desmarca el último botón seleccionado
            lastSelectedButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#c9dddb"))); // Restaura el color original del último botón
        }

        // Selecciona el nuevo botón y establece su color
        button.setSelected(true); // Selecciona el nuevo botón
        button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#73C6B6"))); // Establece el nuevo color del botón
        lastSelectedButton = button; // Actualiza la referencia al último botón seleccionado
    }
    public void onClickTableNumber(View view) {
        if (view instanceof Button) {
            Button button = (Button) view;// Si es un botón, conviértelo a un objeto Button

            // Desmarca el último botón seleccionado y restaura su color original
            selectUnselectButton(button);

            if(button.getText().equals("?")){
                int randomTable = (int) Math.floor(Math.random() * 11);
                MainActivity.setTableSelected(String.valueOf(randomTable));
            }else{
                MainActivity.setTableSelected((String) button.getText()); //Lo utilizaremos para recuperar la tabla seleccionada.
            }
        }
    }
    public void onClickDifficulty(View view) {
        // Diálogo para la selección de dificultad
        DifficultyDialog difficultyDialog = new DifficultyDialog(difficultySelected);
        difficultyDialog.show(requireActivity().getSupportFragmentManager(), "DifficultyDialog");
    }
}
