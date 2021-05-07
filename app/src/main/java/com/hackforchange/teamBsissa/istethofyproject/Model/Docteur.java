package com.hackforchange.teamBsissa.istethofyproject.Model;

public class Docteur {
    String id;
    String nom;
    String prenom;
    String email;
    String tel;
    String region;
    String image_prof;
    String adresse;
    String cin;
    String etat;

    public Docteur() {
    }

    public Docteur(String id, String nom, String prenom, String email, String tel, String region, String image_prof, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
        this.region = region;
        this.image_prof = image_prof;
        this.adresse = adresse;
    }

    public Docteur(String id, String nom, String prenom, String email, String tel, String region, String image_prof, String adresse, String cin) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
        this.region = region;
        this.image_prof = image_prof;
        this.adresse = adresse;
        this.cin = cin;
    }

    public Docteur(String id, String nom, String prenom, String email, String tel, String region, String image_prof, String adresse, String cin, String etat) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
        this.region = region;
        this.image_prof = image_prof;
        this.adresse = adresse;
        this.cin = cin;
        this.etat = etat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getImage_prof() {
        return image_prof;
    }

    public void setImage_prof(String image_prof) {
        this.image_prof = image_prof;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
