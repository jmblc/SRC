package fr.igestion.crm.bean;

import java.util.Date;

public class Message {

    private String ID = "";
    private String TITRE = "";
    private String CONTENU = "";
    private String CAMPAGNE_ID = "";
    private Date DATEDEBUT = null;
    private Date DATEFIN = null;
    private String actif = "";

    private String debutTitre = "", debutContenu = "", campagne = "";

    public Message() {
    }

    public String getCAMPAGNE_ID() {
        return CAMPAGNE_ID;
    }

    public void setCAMPAGNE_ID(String campagne_id) {
        CAMPAGNE_ID = campagne_id;
    }

    public String getCONTENU() {
        return CONTENU;
    }

    public void setCONTENU(String contenu) {
        CONTENU = contenu;
    }

    public Date getDATEDEBUT() {
        return DATEDEBUT;
    }

    public void setDATEDEBUT(Date datedebut) {
        DATEDEBUT = datedebut;
    }

    public Date getDATEFIN() {
        return DATEFIN;
    }

    public void setDATEFIN(Date datefin) {
        DATEFIN = datefin;
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getTITRE() {
        return TITRE;
    }

    public void setTITRE(String titre) {
        TITRE = titre;
    }

    public String getActif() {
        return actif;
    }

    public void setActif(String actif) {
        this.actif = actif;
    }

    public String getDebutContenu() {
        return debutContenu;
    }

    public void setDebutContenu(String debutContenu) {
        this.debutContenu = debutContenu;
    }

    public String getDebutTitre() {
        return debutTitre;
    }

    public void setDebutTitre(String debutTitre) {
        this.debutTitre = debutTitre;
    }

    public String getCampagne() {
        return campagne;
    }

    public void setCampagne(String campagne) {
        this.campagne = campagne;
    }

}
