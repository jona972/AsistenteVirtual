package com.example.jona.asistentevirtual.Class;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConectionToIntenetClass {

    private Context context;

    public ConectionToIntenetClass(Context context) {
        this.context = context;
    }

    public void ConectionToInternet() {
        String connectivityService = context.CONNECTIVITY_SERVICE;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(connectivityService);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Si hay conexión a Internet en este momento
        } else {
            // No hay conexión a Internet en este momento
            Toast.makeText(context, "No hay internet", Toast.LENGTH_SHORT).show();
        }
    }
}
