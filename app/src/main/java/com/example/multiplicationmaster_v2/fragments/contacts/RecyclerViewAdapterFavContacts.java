package com.example.multiplicationmaster_v2.fragments.contacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplicationmaster_v2.MainActivity;
import com.example.multiplicationmaster_v2.R;
import com.example.multiplicationmaster_v2.dialogs.DeleteFavoriteContactDialog;
import com.example.multiplicationmaster_v2.dialogs.StatisticsEmptyDialog;
import com.example.multiplicationmaster_v2.fragments.statistics.StatisticsFragment;

import java.util.ArrayList;

public class RecyclerViewAdapterFavContacts extends RecyclerView.Adapter<RecyclerViewAdapterFavContacts.ViewHolder> {
    private static ArrayList<Contact> contactsList;
    private static FragmentManager fragmentManager; // Recibimos el FragmentManager por parametro para poder desplegar el dialog que nos permitira eliminar contactos de favoritos
    private static ArrayList<String[]> tablesCompleted = new ArrayList<>();
    private static ArrayList<String[]> mistakes = new ArrayList<>();
    private static ArrayList<String> percentegesSuccess = MainActivity.getPercentegesSuccess();

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
                    tablesCompleted  = StatisticsFragment.getTablesList();
                    mistakes = StatisticsFragment.getMistakesListWithStatId();
                    if (tablesCompleted != null && mistakes != null) {
                        if (!tablesCompleted.isEmpty()) {
                            sendMail(v.getContext(), textViewMail.getText().toString().trim());
                        }else {
                            StatisticsEmptyDialog statisticsEmptyDialog = new StatisticsEmptyDialog();
                            statisticsEmptyDialog.show(fragmentManager, "statisticsEmptyDialog");
                            //Toast.makeText(v.getContext(), "No hay estadísticas", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Contact contact = new Contact(textViewName.getText().toString(), textViewMail.getText().toString());
                    DeleteFavoriteContactDialog deleteFavorite = new DeleteFavoriteContactDialog(contact);
                    deleteFavorite.show(fragmentManager, "deleteFavoriteContactDialog");
                    return false;
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
    public RecyclerViewAdapterFavContacts(FragmentManager fragmentManager, ArrayList<Contact> contactsList) {
        RecyclerViewAdapterFavContacts.fragmentManager = fragmentManager;
        RecyclerViewAdapterFavContacts.contactsList = contactsList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterFavContacts.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Crear una nueva vista, que define la interfaz de usuario del elemento de la lista
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_line, parent, false);

        return new RecyclerViewAdapterFavContacts.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterFavContacts.ViewHolder holder, int position) {
        // Obtener el elemento del conjunto de datos en esta posición y reemplazar el contenido de la vista con ese elemento
        Contact contact = contactsList.get(position);
        holder.getTextViewName().setText(contact.getName());
        holder.getTextViewMail().setText(contact.getEmail());
    }

    // Devolver el tamaño del conjunto de datos
    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    // Envia las estadisticas por mail al usuario
    @SuppressLint("IntentReset")
    public static void sendMail(Context context, String mail) {
        Intent intent = new Intent();
        Intent chooser = null;

        intent.setAction(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        String[] posting = {mail};
        intent.putExtra(Intent.EXTRA_EMAIL, posting);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Multiplication Master - Estadísticas");
        intent.putExtra(Intent.EXTRA_TEXT, emailBody());
        intent.setType("message/rfc822");
        chooser = Intent.createChooser(intent, "Enviar Email");
        context.startActivity(intent);
    }

    private static String emailBody() {
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

    public static boolean hasMistakes(int i){
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
