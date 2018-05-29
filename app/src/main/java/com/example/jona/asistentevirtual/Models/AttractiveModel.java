package com.example.jona.asistentevirtual.Models;

import com.google.gson.JsonElement;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ai.api.model.Result;

public class AttractiveModel {

    private boolean state;
    private String nameAttractive;
    private String category;
    private String description;
    private List<String> imagenURL;
    private double latitude;
    private double longitude;

    private JsonElement result;
    private JsonElement gallery;
    private JsonElement position;

    public AttractiveModel() { imagenURL = new ArrayList<>(); }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getNameAttractive() {
        return nameAttractive;
    }

    public void setNameAttractive(String nameAttractive) {
        this.nameAttractive = nameAttractive;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public JsonElement getResult() {
        return result;
    }

    public void setResult(JsonElement result) {
        this.result = result;
    }

    public JsonElement getGallery() {
        return gallery;
    }

    public void setGallery(JsonElement gallery) {
        this.gallery = gallery;
    }

    public JsonElement getPosition() {
        return position;
    }

    public void setPosition(JsonElement position) {
        this.position = position;
    }

    public List<String> getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(List<String> imagenURL) {
        this.imagenURL = imagenURL;
    }

    // Método para leer el JSON de atractivos consultados que se obtiene de Dialogflow.
    public void readJSONDialogflow(Result resultAI) {
        final Map<String, JsonElement> JSONDialogflowResult = resultAI.getFulfillment().getData();
        if (JSONDialogflowResult != null && !JSONDialogflowResult.isEmpty()) {

            setState(Boolean.valueOf(JSONDialogflowResult.get("estado").toString()));

            setResult(JSONDialogflowResult.get("resultado"));

            setDescription(getResult().getAsJsonObject().get("descripcion").toString().replace("\"", ""));
            setNameAttractive(getResult().getAsJsonObject().get("nombre").toString().replace("\"", ""));
            setCategory(getResult().getAsJsonObject().get("categoria").toString().replace("\"", ""));

            setGallery(getResult().getAsJsonObject().get("galeria"));

            setPosition(getResult().getAsJsonObject().get("posicion"));
            setLatitude(Double.parseDouble(getPosition().getAsJsonObject().get("lat").toString()));
            setLongitude(Double.parseDouble(getPosition().getAsJsonObject().get("lng").toString()));
        } else {
            setState(false); // Para saber si el JSON esta vacio.
        }
    }

    // Método para guardar todas las imagenes en un ArrayList.
    public List<String> getListImages() {
        try {
            JSONObject jsonObject = new JSONObject(getGallery().getAsJsonObject().toString());
            Iterator<?> keys = jsonObject.keys();
            while(keys.hasNext() ) {
                String key = (String)keys.next();
                if (jsonObject.get(key) instanceof JSONObject) {
                    JSONObject jsonResult = new JSONObject(jsonObject.get(key).toString());
                    imagenURL.add(jsonResult.getString("imagenURL"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getImagenURL();
    }
}
