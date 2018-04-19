package fr.igestion.crm.bean;

import java.util.Date;

public class VersionGarantie {

    private String PLV_ID;
    private String PLV_PLA_ID;
    private Date PLV_DATE_INSERTION;
    private Date PLV_DATE_EFFET;
    private Date PLV_DATE_FIN;
    private byte[] PLV_BLOB;
    private String PLV_NOM_FICHIER;
    private String PLV_COMMENTAIRES;
    private String PLV_CREATEUR_ID;

    private String NOM_PRENOM_CREATEUR = "";

    public VersionGarantie() {
    }

    public byte[] getPLV_BLOB() {
        return PLV_BLOB;
    }

    public void setPLV_BLOB(byte[] plv_blob) {
        PLV_BLOB = plv_blob.clone();
    }

    public String getPLV_COMMENTAIRES() {
        return PLV_COMMENTAIRES;
    }

    public void setPLV_COMMENTAIRES(String plv_commentaires) {
        PLV_COMMENTAIRES = plv_commentaires;
    }

    public Date getPLV_DATE_EFFET() {
        return PLV_DATE_EFFET;
    }

    public void setPLV_DATE_EFFET(Date plv_date_effet) {
        PLV_DATE_EFFET = plv_date_effet;
    }

    public Date getPLV_DATE_FIN() {
        return PLV_DATE_FIN;
    }

    public void setPLV_DATE_FIN(Date plv_date_fin) {
        PLV_DATE_FIN = plv_date_fin;
    }

    public Date getPLV_DATE_INSERTION() {
        return PLV_DATE_INSERTION;
    }

    public void setPLV_DATE_INSERTION(Date plv_date_insertion) {
        PLV_DATE_INSERTION = plv_date_insertion;
    }

    public String getPLV_ID() {
        return PLV_ID;
    }

    public void setPLV_ID(String plv_id) {
        PLV_ID = plv_id;
    }

    public String getPLV_NOM_FICHIER() {
        return PLV_NOM_FICHIER;
    }

    public void setPLV_NOM_FICHIER(String plv_nom_fichier) {
        PLV_NOM_FICHIER = plv_nom_fichier;
    }

    public String getPLV_PLA_ID() {
        return PLV_PLA_ID;
    }

    public void setPLV_PLA_ID(String plv_pla_id) {
        PLV_PLA_ID = plv_pla_id;
    }

    public String getPLV_CREATEUR_ID() {
        return PLV_CREATEUR_ID;
    }

    public void setPLV_CREATEUR_ID(String plv_createur_id) {
        PLV_CREATEUR_ID = plv_createur_id;
    }

    public String getNOM_PRENOM_CREATEUR() {
        return NOM_PRENOM_CREATEUR;
    }

    public void setNOM_PRENOM_CREATEUR(String nom_prenom_createur) {
        NOM_PRENOM_CREATEUR = nom_prenom_createur;
    }

}