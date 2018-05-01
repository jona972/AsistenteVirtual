package com.example.jona.asistentevirtual.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.jona.asistentevirtual.Adapters.MessagesAdapter;
import com.example.jona.asistentevirtual.Models.TextMessageModel;
import com.example.jona.asistentevirtual.R;

import java.util.ArrayList;
import java.util.List;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.android.AIService;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;


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
    private Button btnSendMessage;
    private EditText txtMessageUserSend;
    private MessagesAdapter messagesAdapter;

    // Declaración de variable para interactuar con Dialogflow.
    private AIService aiService;
    private AIDataService aiDataService;
    private static final String ACCESS_CLIENT_TOKEN = "118cb22bf2054babbe581439d0483a77";

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
        rvListMessages.setLayoutManager(linearLayoutManager);

        messagesAdapter = new MessagesAdapter(listMessagesText, view.getContext());
        rvListMessages.setAdapter(messagesAdapter);

        ConfigurationDialogflow(view); // Para configurar el API de Dialogflow.

        // Acción del boton para Enviar Mensaje.
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateMessage(txtMessageUserSend.getText().toString(), view);
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

    public void setScrollbarChat(){
        rvListMessages.scrollToPosition(messagesAdapter.getItemCount()-1);
    }

    public void CreateMessage(String message, final View view) {
        TextMessageModel textMessageModel = new TextMessageModel(message, true); // Inicializamos la clase con el mensaje y quien envia el mensaje.
        listMessagesText.add(textMessageModel);

        SendMessageToDialogflow(view); // Para enviar un mensaje a Dialogflow y para recibir el mensaje.
        txtMessageUserSend.setText(""); // Para limpiar el texto al momento de que envia un mensaje el usuario.
        messagesAdapter.notifyDataSetChanged();
        setScrollbarChat();
    }

    // Metodo de configuración para conectarse con Dialogflow.
    public void ConfigurationDialogflow(View view) {
        final AIConfiguration configurationAI = new AIConfiguration(ACCESS_CLIENT_TOKEN,
                AIConfiguration.SupportedLanguages.Spanish,
                AIConfiguration.RecognitionEngine.System);

        // Agregar la configuración para conectarse con Dialogflow
        aiService = AIService.getService(view.getContext(), configurationAI);
        aiDataService = new AIDataService(configurationAI);
    }

    // Método para enviar el mensaje a Dialogflow.
    public void SendMessageToDialogflow(final View view) {
        final AIRequest aiRequest = new AIRequest();
        final String message = txtMessageUserSend.getText().toString();
        aiRequest.setQuery(message); // Enviamos la pregunta del usuario a Dialogflow.

        new AsyncTask<AIRequest, Void, AIResponse>() {

            @Override
            protected AIResponse doInBackground(AIRequest... aiRequests) {
                final AIRequest request = aiRequests[0];
                try {
                    final AIResponse response = aiDataService.request(aiRequest);
                    return response;
                } catch (AIServiceException e) {
                }
                return null;
            }

            // Método para recibir la respuesta de Dialogflow.
            @Override
            protected void onPostExecute(AIResponse response) {
                if (response != null) {
                    Result result = response.getResult();
                    String speech = result.getFulfillment().getSpeech();

                    TextMessageModel textMessageModel = new TextMessageModel(speech, false);
                    listMessagesText.add(textMessageModel);

                    MessagesAdapter messagesAdapter = new MessagesAdapter(listMessagesText, view.getContext());
                    rvListMessages.setAdapter(messagesAdapter);
                    messagesAdapter.notifyDataSetChanged();
                    setScrollbarChat();
                }
            }
        }.execute(aiRequest);
    }
}
