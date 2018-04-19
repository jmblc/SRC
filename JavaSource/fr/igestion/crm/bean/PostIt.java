package fr.igestion.crm.bean;

import java.util.Date;

public class PostIt {

    private Date DATE_CREATION = null;
    private Date DATE_MAJ = null;
    private String CREATEUR_ID = "";
    private String USERMAJ_ID = "";
    private String CONTENU = "";

    public PostIt() {
    }

    public String getCONTENU() {
        return CONTENU;
    }

    public void setCONTENU(String contenu) {
        CONTENU = contenu;
    }

    public String getCREATEUR_ID() {
        return CREATEUR_ID;
    }

    public void setCREATEUR_ID(String createur_id) {
        CREATEUR_ID = createur_id;
    }

    public Date getDATE_CREATION() {
        return DATE_CREATION;
    }

    public void setDATE_CREATION(Date date_creation) {
        DATE_CREATION = date_creation;
    }

    public Date getDATE_MAJ() {
        return DATE_MAJ;
    }

    public void setDATE_MAJ(Date date_maj) {
        DATE_MAJ = date_maj;
    }

    public String getUSERMAJ_ID() {
        return USERMAJ_ID;
    }

    public void setUSERMAJ_ID(String usermaj_id) {
        USERMAJ_ID = usermaj_id;
    }

}
