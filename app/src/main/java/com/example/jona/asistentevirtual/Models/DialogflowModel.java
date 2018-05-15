package com.example.jona.asistentevirtual.Models;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.example.jona.asistentevirtual.Adapters.MessagesAdapter;

import java.util.List;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class DialogflowModel {

    // Declaración de variables para interactuar con Dialogflow.
    private AIDataService aiDataService;
    private static final String ACCESS_CLIENT_TOKEN = "118cb22bf2054babbe581439d0483a77";

    private final int SPEECH_RECOGNITION_CODE = 1;

    // Declaración de variables para inicializar este modelo.
    private View view;
    private List<TextMessageModel> listMessagesText;
    private RecyclerView rvListMessages;
    private MessagesAdapter messagesAdapter;
    private EditText txtMessageUserSend;

    public DialogflowModel(View view, List<TextMessageModel> listMessagesText, RecyclerView rvListMessages, MessagesAdapter messagesAdapter, EditText txtMessageUserSend) {
        this.view = view;
        this.listMessagesText = listMessagesText;
        this.rvListMessages = rvListMessages;
        this.messagesAdapter = messagesAdapter;
        this.txtMessageUserSend = txtMessageUserSend;
    }

    // Metodo de configuración para conectarse con Dialogflow.
    public void ConfigurationDialogflow() {
        final AIConfiguration configurationAI = new AIConfiguration(ACCESS_CLIENT_TOKEN,
                AIConfiguration.SupportedLanguages.Spanish,
                AIConfiguration.RecognitionEngine.System);

        // Agregar la configuración para conectarse con Dialogflow
        aiDataService = new AIDataService(configurationAI);
    }

    // Método para enviar el mensaje a Dialogflow.
    public void SendMessageTextToDialogflow(final String message) {
        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(message); // Enviamos la pregunta del usuario a Dialogflow.

        new AsyncTask<AIRequest, Void, AIResponse>() {

            @Override
            protected AIResponse doInBackground(AIRequest... aiRequests) {
                final AIRequest request = aiRequests[0];
                try {
                    final AIResponse response = aiDataService.request(aiRequest);
                    return response;
                } catch (AIServiceException e) {
                    e.printStackTrace();
                }
                return null;
            }

            // Método para recibir la respuesta de Dialogflow.
            @Override
            protected void onPostExecute(AIResponse response) {
                if (response != null) {
                    ResponseToDialogflow(response);
                }
            }
        }.execute(aiRequest);
    }

    // Método para posicionar el último elemento del Recycle View.
    public void setScrollbarChat() {
        rvListMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);
    }

    // Método para crear un nuevo mensaje del usuario y del ChatBot.
    public void CreateMessage(String message) {
        if (!message.equals("")) { // Se valida el mensaje que sea diferente de nulo para que no envie un texto vació a Dialogflow ni al chat.
            TextMessageModel textMessageModel = new TextMessageModel(message, true); // Inicializamos la clase con el mensaje y quien envia el mensaje.
            listMessagesText.add(textMessageModel);
        }

        SendMessageTextToDialogflow(message); // Para enviar un mensaje a Dialogflow y para recibir el mensaje.
        txtMessageUserSend.setText(""); // Para limpiar el texto al momento de que envia un mensaje el usuario.
        messagesAdapter.notifyDataSetChanged();
        setScrollbarChat();
    }

    // Método para enviar la respuesta
    private void ResponseToDialogflow(AIResponse response) {
        if (response != null) {
            Result result = response.getResult();
            String action = result.getAction(); // Variable para reconocer la acción según la pregunta del usuario.

            if(action.equals("weatherAction")) {

                final WeatherModel weatherModel = new WeatherModel(view);
                weatherModel.CurrentWeather(new WeatherModel.WeatherCallback() {
                    @Override
                    public void getResponseWeather(String response) {
                        MessageSendToDialogflow(response);
                    }
                });
            } else {

                String speech = result.getFulfillment().getSpeech();
                MessageSendToDialogflow(speech);
            }
        }
    }

    // Método para enviar la respuesta al usuario.
    private void MessageSendToDialogflow(String message) {
        TextMessageModel textMessageModel = new TextMessageModel(message, false);
        listMessagesText.add(textMessageModel);

        MessagesAdapter messagesAdapter = new MessagesAdapter(listMessagesText, view.getContext());
        rvListMessages.setAdapter(messagesAdapter);
        messagesAdapter.notifyDataSetChanged();
        setScrollbarChat();
    }
}
