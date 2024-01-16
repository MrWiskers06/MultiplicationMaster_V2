package com.example.multiplicationmaster_v2.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.multiplicationmaster_v2.MainActivity;
import com.example.multiplicationmaster_v2.R;
import com.example.multiplicationmaster_v2.database.DatabaseDAO;
import com.example.multiplicationmaster_v2.database.DatabaseDAOImplement;
import com.example.multiplicationmaster_v2.fragments.contacts.Contact;
import com.example.multiplicationmaster_v2.fragments.contacts.ContactsFragment;

import java.util.ArrayList;

public class AddFavoriteContactDialog extends DialogFragment {
    private TextView txvAddFavorite;
    private Contact contact;
    private DatabaseDAO databaseDAO;

    public AddFavoriteContactDialog(Contact contact) {
        this.contact = contact;
        databaseDAO = new DatabaseDAOImplement(MainActivity.getDatabase());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Crear un AlertDialog con las opciones de dificultad
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.tittle_addFavoriteContact);

        // Configurar un TextView en el AlertDialog
        txvAddFavorite = new TextView(requireActivity());
        txvAddFavorite.setText(R.string.txv_addFavoriteContact);
        txvAddFavorite.setTextSize(18f);
        txvAddFavorite.setPadding(50, 0, 0, 0);
        builder.setView(txvAddFavorite);

        // Configurar el botón positivo del diálogo
        builder.setPositiveButton(R.string.btn_sendPassword, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Añade el contacto a la tabla de contactos favoritos
                databaseDAO.addFavoriteContact(contact);
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
