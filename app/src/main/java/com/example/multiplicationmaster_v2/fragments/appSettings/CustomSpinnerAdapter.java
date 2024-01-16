package com.example.multiplicationmaster_v2.fragments.appSettings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.multiplicationmaster_v2.R;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    // Arreglos que contienen nombres de avatares y recursos de imágenes asociados
    private String[] avatars;
    private int[] avatarImages;

    // Inflador de diseño para crear vistas personalizadas en el Spinner
    private LayoutInflater inflater;

    // Constructor de la clase
    public CustomSpinnerAdapter(Context context, int spinnerLines, String[] avatarNames, int[] avatarImgs) {
        // Llama al constructor de la clase base de ArrayAdapter
        super(context, spinnerLines, avatarNames);

        // Inicializa los arreglos y el inflador con los valores proporcionados
        avatars = avatarNames;
        avatarImages = avatarImgs;
        inflater = LayoutInflater.from(context);
    }

    // Método para obtener la vista de un elemento en la lista desplegable del Spinner
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        // Llama al método createCustomRow para crear una vista personalizada
        return createCustomRow(position, convertView, parent);
    }

    // Método para obtener la vista de un elemento seleccionado en el Spinner
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Llama al método createCustomRow para crear una vista personalizada
        return createCustomRow(position, convertView, parent);
    }

    // Método para crear una fila personalizada del Spinner
    public View createCustomRow(int position, View convertView, ViewGroup parent) {
        // Infla el diseño personalizado (spinner_lines) para la fila
        View row = inflater.inflate(R.layout.spinner_lines, parent, false);

        // Configura el nombre del avatar y la imagen del avatar en la fila del Spinner
        TextView avatarName = row.findViewById(R.id.nombre);
        avatarName.setText(avatars[position]);

        ImageView avatarImg = row.findViewById(R.id.avatar);
        avatarImg.setImageResource(avatarImages[position]);

        return row;
    }
}
