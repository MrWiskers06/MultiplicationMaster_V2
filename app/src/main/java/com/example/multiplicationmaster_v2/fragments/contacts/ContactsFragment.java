package com.example.multiplicationmaster_v2.fragments.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplicationmaster_v2.databinding.FragmentContactsBinding;

import java.util.ArrayList;

public class ContactsFragment extends Fragment {
    private FragmentContactsBinding binding;
    private static ArrayList<Contact> contactsList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentContactsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        contactsList = findContacts();
        initializeRecyclerView();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Metodo para inicializar el RecyclerView
    private void initializeRecyclerView() {
        // Configurar el RecyclerView con el LinearLayoutManager
        RecyclerView recyclerView = binding.recyclerViewContacts;
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter<RecyclerViewAdapterContacts.ViewHolder> recyclerViewAdapter = new RecyclerViewAdapterContacts(requireActivity().getSupportFragmentManager(), contactsList); // Configurar el adaptador del RecyclerView con la lista de contactos
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    // Metodo para recuperar los contactos
    public ArrayList<Contact> findContacts() {
        contactsList = new ArrayList<>();
        // Define las columnas que se desean recuperar de la tabla de contactos
        String[] projection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Email.ADDRESS
        };

        // Define la condición de selección para obtener solo los contactos con direcciones de correo electrónico
        String filter = ContactsContract.Data.MIMETYPE + " = ?";
        String[] filterArgs = {ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE};

        String sortOrder = ContactsContract.Data.DISPLAY_NAME + " ASC"; // Define el orden de clasificación de los resultados

        // Obtiene el ContentResolver para realizar la consulta
        ContentResolver contentResolver = requireContext().getContentResolver();
        // Realizar la consulta a la tabla de datos de contactos
        Cursor cursor = contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                filter,
                filterArgs,
                sortOrder
        );

        // Procesar los resultados de la consulta
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // Obtener las columnas específicas de cada contacto
                String contactId = cursor.getString(0);
                String name = cursor.getString(1);
                String email = cursor.getString(2);

                Contact contact = new Contact(name, email);
                contactsList.add(contact);

            }
            cursor.close();
        }

        return contactsList;
    }
}

