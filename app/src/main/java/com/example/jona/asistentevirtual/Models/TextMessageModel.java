package com.example.jona.asistentevirtual.Models;

public class TextMessageModel {

    // Declaración de variables de los mensajes de texto.
    private String message;
    private boolean isSend; // Para saber si es el ChatBot o el usuario.

    // Constructor
    public TextMessageModel() {
    }

    public TextMessageModel(String message, boolean isSend) {
        this.message = message;
        this.isSend = isSend;
    }

    // Getters and Setters
    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
