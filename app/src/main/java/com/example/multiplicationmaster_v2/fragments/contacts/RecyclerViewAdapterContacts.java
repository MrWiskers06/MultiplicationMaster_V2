package com.example.multiplicationmaster_v2.fragments.contacts;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplicationmaster_v2.R;
import com.example.multiplicationmaster_v2.dialogs.AddFavoriteContactDialog;

import java.util.ArrayList;

public class RecyclerViewAdapterContacts extends RecyclerView.Adapter<RecyclerViewAdapterContacts.ViewHolder> {
    private static ArrayList<Contact> contactsList;
    private static FragmentManager fragmentManager; // Recibimos el FragmentManager por parametro para poder desplegar el dialog que nos permitira añadir contactos a favoritos

    // ViewHolder para representar cada elemento de la lista en el RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewMail;
        public ViewHolder(View view) {
            super(view);
            // Inicializar el TextView para mostrar los datos
            textViewName = view.findViewById(R.id.txv_contactsName);
            textViewMail = view.findViewById(R.id.txv_contactsMail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Abrimos el dialog para anadir un contacto favorito
                    Contact contact = new Contact(textViewName.getText().toString(), textViewMail.getText().toString());
                    AddFavoriteContactDialog addFavorite = new AddFavoriteContactDialog(contact);
                    addFavorite.show(fragmentManager, "addFavoriteContactDialog");
                }
            });
        }

        public TextView getTextViewName() {
            return textViewName;
        }
        public TextView getTextViewMail() {
            return textViewMail;
        }
    }

    // Constructor para el adaptador, recibe el conjunto de datos local
    public RecyclerViewAdapterContacts(FragmentManager fragmentManager, ArrayList<Contact> contactsList) {
        RecyclerViewAdapterContacts.fragmentManager = fragmentManager;
        RecyclerViewAdapterContacts.contactsList = contactsList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterContacts.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Crear una nueva vista, que define la interfaz de usuario del elemento de la lista
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_line, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterContacts.ViewHolder holder, int position) {
        // Obtener el elemento del conjunto de datos en esta posición y reemplazar el contenido de la vista con ese elemento
        Contact contact = contactsList.get(position);
        holder.getTextViewName().setText(contact.getName());
        holder.getTextViewMail().setText(contact.getEmail());
    }

    // Devolver el tamaño del conjunto de datos (Lista de contactos)
    @Override
    public int getItemCount() {
        return contactsList.size();
    }
}