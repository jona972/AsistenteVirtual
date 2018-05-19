package com.example.jona.asistentevirtual.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.example.jona.asistentevirtual.R;

public class DialogSignOffFragement extends DialogFragment {

    /* La actividad que crea una instancia de este fragmento de diálogo debe
       implementar esta interfaz para recibir devoluciones de llamadas de eventos.
       Cada método pasa por el DialogFragment en caso de que el host necesite consultarlo.
    */
    public interface NoticeDialogListener {
        void onDialogSigOffClick(DialogFragment dialog);
        void onDialogCancelClick(DialogFragment dialog);
    }

    // Se usa esta instancia de la interfaz para entregar eventos de acción.
    NoticeDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Se crea el diálogo y se configura los manejadores de clic del botón.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Se le añaden el titulo y el mensaje que va a tener el dialogo.
        builder.setTitle("Cerrar Sesión");
        builder.setMessage("¿Desea cerrar la sesión?");

        // Se añaden las acciones de los botones.
        builder.setPositiveButton(R.string.dialog_sign_off_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Se envia el evento del botón salir a la actividad principal.
                mListener.onDialogSigOffClick(DialogSignOffFragement.this);
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Se enviaa el evento del botón cancelar a la actividad principal.
                mListener.onDialogCancelClick(DialogSignOffFragement.this);
            }
        });

        return builder.create();
    }

    // Este método invalida el Fragment.onAttach() para crear una instancia del NoticeDialogListener.
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verifica que la actividad del host implemente la interfaz de devolución de llamada.
        try {
            // Crea una instancia del NoticeDialogListener para que podamos enviar eventos al host.
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // La actividad no implementa la interfaz, en este caso se debe arrojar una excepción.
            throw new ClassCastException(activity.toString()
                    + " se debe implementar con NoticeDialogListener.");
        }
    }
}
