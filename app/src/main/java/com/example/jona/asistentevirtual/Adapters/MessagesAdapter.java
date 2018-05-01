package com.example.jona.asistentevirtual.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jona.asistentevirtual.Models.TextMessageModel;
import com.example.jona.asistentevirtual.R;
import com.github.library.bubbleview.BubbleTextView;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter {

    // Para saber si cual si el usuario mando mensaje o el ChatBot.
    private static final int VIEW_TYPE_MESSAGE_USER = 1;
    private static final int VIEW_TYPE_MESSAGE_CHATBOT = 2;

    private List<TextMessageModel> listChatModel;
    private Context context;

    // Constructor con los parametros de la lista de mensaje y el contexto de la vista.
    public MessagesAdapter(List<TextMessageModel> listChatModels, Context context) {
        this.listChatModel = listChatModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_USER) { // Si el mensaje es del usuario se añade el view de esta al view de la lista de mensajes.
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_user, parent, false);
            return new UserMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_CHATBOT) { // Si el mensaje es del ChatBot se añade el view de esta al view de la lista de mensajes.
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_chatbot, parent, false);
            return new ChatBotMessageHolder(view);
        }

        return null;
    }

    // Método para pasar el objeto del mensaje a un ViewHolder para que los contenidos puedan vincularse a la IU.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextMessageModel message = listChatModel.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_USER:
                ((UserMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_CHATBOT:
                ((ChatBotMessageHolder) holder).bind(message);
        }
    }

    // Método para obtener la posición de donde se encuentra el mensaje.
    @Override
    public int getItemCount() {
        return listChatModel.size();
    }

    // Método para determinar el ViewType de acuerdo a quien envio el mensaje.
    @Override
    public int getItemViewType(int position) {
        if (listChatModel.get(position).isSend()) {
            // Si el usuario mando el mensaje.
            return VIEW_TYPE_MESSAGE_USER;
        } else {
            // Si el ChatBot envio el mensaje.
            return VIEW_TYPE_MESSAGE_CHATBOT;
        }
    }

    private class UserMessageHolder extends RecyclerView.ViewHolder {
        BubbleTextView messageText;

        UserMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.textMessageUser);
        }

        void bind(TextMessageModel message) {
            messageText.setText(message.getMessage()); // Se envia el mensaje del Usuario.
        }
    }

    private class ChatBotMessageHolder extends RecyclerView.ViewHolder {
        BubbleTextView messageText;

        ChatBotMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.textMessageChatBot);
        }

        void bind(TextMessageModel message) {
            messageText.setText(message.getMessage()); // Se envia el mensaje del ChatBot.
        }
    }
}