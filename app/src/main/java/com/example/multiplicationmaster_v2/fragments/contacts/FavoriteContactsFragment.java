package com.example.multiplicationmaster_v2.fragments.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplicationmaster_v2.MainActivity;
import com.example.multiplicationmaster_v2.database.DatabaseDAO;
import com.example.multiplicationmaster_v2.database.DatabaseDAOImplement;
import com.example.multiplicationmaster_v2.databinding.FragmentFavoriteContactsBinding;

import java.util.ArrayList;

public class FavoriteContactsFragment extends Fragment {
    private static FragmentFavoriteContactsBinding binding;
    private DatabaseDAO databaseDAO;
    private static ArrayList<Contact> favoriteContactsList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoriteContactsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        databaseDAO = new DatabaseDAOImplement(MainActivity.getDatabase());
        favoriteContactsList = databaseDAO.getFavoriteContacts(); // Recupera la lista de contactos favoritos de la base de datos
        initializeRecyclerView(); // Inicializa el RecyclerView de los contactos favoritos

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
        RecyclerView recyclerView = binding.recyclerViewFavoriteContacts;
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter<RecyclerViewAdapterFavContacts.ViewHolder> recyclerViewAdapter = new RecyclerViewAdapterFavContacts(requireActivity().getSupportFragmentManager(), favoriteContactsList); // Configurar el adaptador del RecyclerView con la lista de contactos
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
