package com.hackforchange.teamBsissa.istethofyproject.Model;

public class result2 {

    String id;
    String id_client;
    String id_doc;
    String resultat;

    public result2(String id, String id_client, String id_doc, String resultat) {
        this.id = id;
        this.id_client = id_client;
        this.id_doc = id_doc;
        this.resultat = resultat;
    }

    public result2() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }

    public String getId_doc() {
        return id_doc;
    }

    public void setId_doc(String id_doc) {
        this.id_doc = id_doc;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }
}
