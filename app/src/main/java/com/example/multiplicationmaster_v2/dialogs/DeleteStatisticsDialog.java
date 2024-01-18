package com.example.multiplicationmaster_v2.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.multiplicationmaster_v2.MainActivity;
import com.example.multiplicationmaster_v2.R;
import com.example.multiplicationmaster_v2.database.DatabaseDAO;
import com.example.multiplicationmaster_v2.database.DatabaseDAOImplement;

public class DeleteStatisticsDialog extends DialogFragment {
    private TextView txvDeleteMessage;
    private DatabaseDAO databaseDAO = new DatabaseDAOImplement(MainActivity.getDatabase());

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Crear un AlertDialog con las opciones de dificultad
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.title_deleteStatistics);

        // Configurar un TextView en el AlertDialog
        txvDeleteMessage = new TextView(requireActivity());
        txvDeleteMessage.setText(R.string.txv_deleteStatistics);
        txvDeleteMessage.setTextSize(18f);
        txvDeleteMessage.setPadding(50, 16, 16, 16);
        builder.setView(txvDeleteMessage);

        // Configurar el botón positivo del diálogo
        builder.setPositiveButton(R.string.btn_positiveDelete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseDAO.deleteStatistics(); // Elimina las estadisticas de la base de datos
            }
        });

        builder.setNegativeButton(R.string.btn_negativeDelete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        // Crear y devolver el diálogo configurado
        return builder.create();
    }
}