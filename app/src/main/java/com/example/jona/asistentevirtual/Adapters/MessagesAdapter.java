package com.example.jona.asistentevirtual.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jona.asistentevirtual.Models.TextMessageModel;
import com.example.jona.asistentevirtual.R;
import com.github.library.bubbleview.BubbleTextView;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter {

    // Para saber que vista se debe adaptar al RecycleView.
    private static final int VIEW_TYPE_MESSAGE_USER = 1;
    private static final int VIEW_TYPE_MESSAGE_CHATBOT = 2;
    private static final int VIEW_TYPE_MESSAGE_CHATBOT_TYPING = 3;
    private static final int VIEW_TYPE_MESSAGE_ATTRACTIVE_CHATBOT = 4;

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
        } else if (viewType == VIEW_TYPE_MESSAGE_CHATBOT_TYPING) { // Si el mensaje es del ChatBotTyping se añade el view de esta al view de la lista de mensajes.
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chatbot_is_typing, parent, false);
            return new ChatBotTypingHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_ATTRACTIVE_CHATBOT) { // Si el mensaje es sobre la informacion de un atractivo turistico.
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.attractive_information, parent, false);
            return new AttractiveMessageHolder(view);
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
                break;
            case VIEW_TYPE_MESSAGE_CHATBOT_TYPING:
                ((ChatBotTypingHolder) holder).bind();
                break;
            case VIEW_TYPE_MESSAGE_ATTRACTIVE_CHATBOT:
                AttractiveAdpater attractiveAdpater = new AttractiveAdpater(context, listChatModel.get(position).getListImagesURL());
                ((AttractiveMessageHolder) holder).viewPager.setAdapter(attractiveAdpater);
                ((AttractiveMessageHolder) holder).bind(message);
                break;
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
        if (listChatModel.get(position).getViewTypeMessage() == 1) {
            // Si el usuario mando el mensaje.
            return VIEW_TYPE_MESSAGE_USER;
        } else if (listChatModel.get(position).getViewTypeMessage() == 2) {
            // Si el ChatBot envio el mensaje.
            return VIEW_TYPE_MESSAGE_CHATBOT;
        } else if (listChatModel.get(position).getViewTypeMessage() == 3) {
            // Si el ChatBot esta escribiendo el mensaje.
            return VIEW_TYPE_MESSAGE_CHATBOT_TYPING;
        } else {
            // El chatbot envia un mensaje con informacion de un atractivo turistico
            return VIEW_TYPE_MESSAGE_ATTRACTIVE_CHATBOT;
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

    private class ChatBotTypingHolder extends RecyclerView.ViewHolder {

        ChatBotTypingHolder(View itemView) {
            super(itemView);
        }

        void bind() {}
    }

    // Clase para manipular la informacion de los atractivos y ponerles en sus respectivos componetes para agregarles en el RecycleView.
    private class AttractiveMessageHolder extends RecyclerView.ViewHolder {
        // Declaracion de varaibles de acuerdo a los componentes que comprenden el layout: attractive_information.xml
        TextView txtNameAttractive, txtDescriptionAttractive, txtCategoryAttractive;
        ViewPager viewPager;

        AttractiveMessageHolder(View itemView) {
            super(itemView);

            txtDescriptionAttractive = itemView.findViewById(R.id.txtDescriptionPlacesInformation);
            txtNameAttractive = itemView.findViewById(R.id.txtTitlePlacesInformation);
            txtCategoryAttractive = itemView.findViewById(R.id.txtCategoryPlacesInformation);
            viewPager = itemView.findViewById(R.id.vpPlacesInformation);
        }

        void bind(TextMessageModel message) { // Se asigna la informacion consultada a los TextViews.
            // Se envia el nombre del atractivo a su respetivo TextView.
            txtNameAttractive.setText(message.getNameAttractive());

            // Se envia la descripcion del atractivo a su respetivo TextView.
            txtDescriptionAttractive.setText(Html.fromHtml("<b>Descripción: </b>" + message.getDescriptionAttractive()));

            // Se envia la categoria del atractivo a su respetivo TextView.
            txtCategoryAttractive.setText(Html.fromHtml("<b>Categoría: </b>" + message.getCategoryAttactive()));
        }
    }
}