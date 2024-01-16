package com.example.multiplicationmaster_v2.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.multiplicationmaster_v2.MainActivity;
import com.example.multiplicationmaster_v2.R;
import com.example.multiplicationmaster_v2.fragments.statistics.StatisticsFragment;

import java.util.ArrayList;

public class StatisticsDialog extends DialogFragment {
    private EditText editTextEmail;
    private Boolean control = false;
    private ArrayList<String[]> tablesCompleted = StatisticsFragment.getTablesList();
    private ArrayList<String[]> mistakes = StatisticsFragment.getMistakesListWithStatId();
    private ArrayList<String> percentegesSuccess = MainActivity.getPercentegesSuccess();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Crear un AlertDialog con las opciones de dificultad
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.edt_sendMail);

        // Configurar un EditText en el AlertDialog
        editTextEmail = new EditText(requireActivity());
        editTextEmail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(editTextEmail);

        // Configurar el botón positivo del diálogo
        builder.setPositiveButton(R.string.btn_sendMail, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!tablesCompleted.isEmpty() && editTextEmail.getText().toString().trim().length() > 0) {
                    sendMail();
                } else if (tablesCompleted.isEmpty() && editTextEmail.getText().toString().trim().length() > 0) {
                    Toast.makeText(getActivity(), "No hay estadísticas", Toast.LENGTH_LONG).show();
                } else if (editTextEmail.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), "Ingresa un email", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Crear y devolver el diálogo configurado
        return builder.create();
    }

    // Envia las estadisticas por mail al usuario
    @SuppressLint("IntentReset")
    public void sendMail() {
        Intent intent = new Intent();
        Intent chooser = null;

        intent.setAction(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        String[] posting = {editTextEmail.getText().toString().trim()};
        intent.putExtra(Intent.EXTRA_EMAIL, posting);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Multiplication Master - Estadísticas");
        intent.putExtra(Intent.EXTRA_TEXT, emailBody());
        intent.setType("message/rfc822");
        chooser = Intent.createChooser(intent, "Enviar Email");
        startActivity(intent);

    }

    private String emailBody() {
        // Formatea el texto para el cuerpo del correo
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("Estas son tus estadísticas de Multiplication Master:\n\n");
        emailBody.append("Fecha: ").append(StatisticsFragment.getMailDate()).append("\n\n");
        emailBody.append("Tablas de multiplicar practicadas: \n");
        for (int i = 0; i < tablesCompleted.size(); i++) {
            emailBody.append("\t- ").append(tablesCompleted.get(i)[1]).append("\n");
        }
        // Formatea los errores cometidos
        for (int i = 0; i < tablesCompleted.size(); i++) {
            emailBody.append("\nErrores cometidos en la tabla ").append(tablesCompleted.get(i)[1]).append(": \n\n");

            if (hasMistakes(i)){
                for (int j = 0; j < mistakes.size(); j++) {
                    if (mistakes.get(j)[0].equals(tablesCompleted.get(i)[0])) {
                        emailBody.append("\t- ").append(mistakes.get(j)[1]).append("\n");
                    }
                }
            } else {
                emailBody.append("\t- ¡FELICIDADES! No se registraron errores en esta tabla.\n\n");
            }
        }

        /*// Formatea los porcentajes de aciertos
        emailBody.append("Porcentajes de aciertos: ").append("\n");
        for (int i = 0; i < tablesCompleted.size(); i++) {
            emailBody.append("  - ").append(tablesCompleted.get(i)).append(": ").append(percentegesSuccess.get(i)).append("%\n");
        }*/

        emailBody.append("\n¡¡¡Sigue practicando para conseguir más avatares!!!");

        return emailBody.toString();
    }

    public boolean hasMistakes(int i){
        boolean isMistake = false;

        for (int j = 0; j < mistakes.size(); j++) {
            if (mistakes.get(j)[0].equals(tablesCompleted.get(i)[0])) {
                isMistake = true;
                break;
            }
        }

        return isMistake;
    }
}


