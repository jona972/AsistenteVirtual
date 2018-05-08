package com.example.jona.asistentevirtual.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.jona.asistentevirtual.Adapters.MessagesAdapter;
import com.example.jona.asistentevirtual.Models.DialogflowModel;
import com.example.jona.asistentevirtual.Models.TextMessageModel;
import com.example.jona.asistentevirtual.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatTextFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatTextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatTextFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Declaración de variables para poder controlar los mensajes del usuario y del ChatBot.
    private RecyclerView rvListMessages;
    private List<TextMessageModel> listMessagesText;
    private FloatingActionButton btnSendMessage;
    private EditText txtMessageUserSend;
    private MessagesAdapter messagesAdapter;

    private OnFragmentInteractionListener mListener;

    public ChatTextFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatTextFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatTextFragment newInstance(String param1, String param2) {
        ChatTextFragment fragment = new ChatTextFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listMessagesText = new ArrayList<>(); // Incializar la lista de los mensajes de texto.

        final View view = inflater.inflate(R.layout.fragment_chat_text, container, false);

        rvListMessages = view.findViewById(R.id.listOfMessages); // Instanciar la variable con el Id del RecicleView.

        btnSendMessage = view.findViewById(R.id.btnSendMessage); // Instanciar la varibale con el id del Button.

        txtMessageUserSend = view .findViewById(R.id.txtUserMessageSend); // Instanciar la varibale con el id del Edit Text.

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setStackFromEnd(true);
        rvListMessages.setLayoutManager(linearLayoutManager);

        ValidateAudioRecord(view); // Permitimos la entrada de audio al chat.

        messagesAdapter = new MessagesAdapter(listMessagesText, view.getContext());
        rvListMessages.setAdapter(messagesAdapter);

        final DialogflowModel dialogflowModel = new DialogflowModel(view, listMessagesText, rvListMessages, messagesAdapter, txtMessageUserSend);
        dialogflowModel.ConfigurationDialogflow(); // Para configurar el API de Dialogflow.

        ChangeIconButton(); // Se llama al método de cambiar de icono.

        // Acción del boton para Enviar Mensaje.
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogflowModel.CreateMessage(txtMessageUserSend.getText().toString()); // Para enviar un mensaje del usuario o de Dialogflow.
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // Método para cambiar el icono del boton segun la longitud del texto del usuario.
    private void ChangeIconButton() {
        txtMessageUserSend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtMessageUserSend.getText().toString().trim().length() != 0) {
                    btnSendMessage.setImageResource(R.drawable.ic_menu_send);
                } else {
                    btnSendMessage.setImageResource(R.drawable.ic_keyboard_voice);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Método para permitir la entrada de audio.
    private void ValidateAudioRecord(View view) {
        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
    }
}
