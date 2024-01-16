package com.example.multiplicationmaster_v2.fragments.login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.multiplicationmaster_v2.MainActivity;
import com.example.multiplicationmaster_v2.R;
import com.example.multiplicationmaster_v2.database.DatabaseDAO;
import com.example.multiplicationmaster_v2.database.DatabaseDAOImplement;
import com.example.multiplicationmaster_v2.databinding.ContentMainBinding;
import com.example.multiplicationmaster_v2.fragments.appSettings.SettingsFragment;
import com.example.multiplicationmaster_v2.fragments.statistics.StatisticsFragment;

import java.util.ArrayList;

public class LoginFragment extends Fragment {
    private ContentMainBinding binding;
    private DatabaseDAO databaseDAO;
    private ArrayList<String> users;
    private String userPassword;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = ContentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        databaseDAO = new DatabaseDAOImplement(MainActivity.getDatabase());
        users = databaseDAO.getUsers();

        setUsers(users); // Añade los usuarios a la pantalla del Logger

        userPassword = databaseDAO.getUserPasswordByName("Administrador"); // Obtiene la contraseña del usuario Administrador

        return root;
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Añade los usuarios de la base de datos a la pantalla del Logger y asigna un OnClickListener a cada uno de ellos
    public void setUsers(ArrayList<String> users) {

        if (users.size() == 1) {
            binding.txvUser1.setText(users.get(0));

        } else if (users.size() == 2) {
            binding.txvUser1.setText(users.get(0));
            binding.txvUser2.setText(users.get(1));
        } else if (users.size() == 3) {
            binding.txvUser1.setText(users.get(0));
            binding.txvUser2.setText(users.get(1));
            binding.txvUser3.setText(users.get(2));
        }

        binding.txvUser1.setOnClickListener(this::onClickUser);
        binding.txvUser2.setOnClickListener(this::onClickUser);
        binding.txvUser3.setOnClickListener(this::onClickUser);

    }

    @SuppressLint("InflateParams")
    public void onClickUser(View view) {
        if(view instanceof TextView){
            TextView textView = (TextView) view;
            String userName = textView.getText().toString();

            if (userName.equalsIgnoreCase("administrador")){
                if (!MainActivity.getUserConect().equalsIgnoreCase("Administrador")){
                    showPasswordDialog();
                }
            } else if (userName.equalsIgnoreCase("add user")) {
                showSingUpDialog();
            } else {
                MainActivity.setDrawerNav(userName);
                MainActivity.setUserConect(userName);
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, new SettingsFragment());
                transaction.commit();
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    private void showSingUpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Infla la vista del diálogo desde el diseño XML
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.add_user_dialog, null);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create(); // Crear una instancia del AlertDialog

        // Configura el botón del diálogo para manejar la entrada de contraseña
        dialogView.findViewById(R.id.btn_addUserDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtPassword = dialogView.findViewById(R.id.edt_passwordAddUser);
                String password = edtPassword.getText().toString();

                EditText edtAddUser = dialogView.findViewById(R.id.edt_addUserDialog);
                String newUser = edtAddUser.getText().toString().trim();

                if (password.equalsIgnoreCase(userPassword) && !users.contains(newUser)){
                    databaseDAO.addUser(newUser);
                    Toast.makeText(getContext(), "Usuario añadido con exito", Toast.LENGTH_LONG).show();
                    users = databaseDAO.getUsers();
                    setUsers(users);
                    /*MainActivity.setDrawerNav(newUser);
                    MainActivity.setUserConect(newUser);
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, new SettingsFragment());
                    transaction.commit();*/

                    alertDialog.dismiss();
                }else if(users.contains(newUser)){
                    edtAddUser.setError("El nombre ya existe");
                    edtAddUser.setText("");
                } else if (!password.equalsIgnoreCase(userPassword)) {
                    edtPassword.setError("Contraseña incorrecta");
                    edtPassword.setText("");
                }
            }
        });

        alertDialog.show();
    }

    public void showPasswordDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Infla la vista del diálogo desde el diseño XML
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.password_dialog, null);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create(); // Crear una instancia del AlertDialog

        // Configura el botón del diálogo para manejar la entrada de contraseña
        dialogView.findViewById(R.id.btn_passwordDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtPassword = dialogView.findViewById(R.id.edt_passwordDialog);
                String password = edtPassword.getText().toString();

                if (userPassword.equalsIgnoreCase(password)){
                    MainActivity.setDrawerNav("Administrador");
                    MainActivity.setUserConect("Administrador");
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, new StatisticsFragment());
                    transaction.commit();
                    alertDialog.dismiss();
                }else{
                    edtPassword.setError("La contraseña introducida es erronea");
                    edtPassword.setText("");
                }
            }
        });

        alertDialog.show();
    }
}
