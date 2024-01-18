package com.example.multiplicationmaster_v2.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.multiplicationmaster_v2.R;

public class StatisticsEmptyDialog extends DialogFragment {
    private TextView textViewStatsEmpty;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Crear un AlertDialog con las opciones de dificultad
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.tittle_statistics_empty);

        // Configurar un EditText en el AlertDialog
        textViewStatsEmpty = new TextView(requireActivity());
        textViewStatsEmpty.setText(R.string.txv_statistics_empty);
        textViewStatsEmpty.setTextSize(18f);
        textViewStatsEmpty.setPadding(50, 16, 16, 16);
        builder.setView(textViewStatsEmpty);

        // Configurar el botón positivo del diálogo
        builder.setPositiveButton(R.string.btn_sendPassword, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        // Crear y devolver el diálogo configurado
        return builder.create();
    }
}
