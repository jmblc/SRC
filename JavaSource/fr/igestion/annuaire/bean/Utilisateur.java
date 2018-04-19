package fr.igestion.annuaire.bean;

public class Utilisateur {

    private String UTL_ID = "";
    private String UTL_PRS_ID = "";
    private String UTL_LOGIN = "";
    private String UTL_PASSWORD = "";

    private Personne personne = null;

    public Utilisateur() {
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public String getUTL_ID() {
        return UTL_ID;
    }

    public void setUTL_ID(String utl_id) {
        UTL_ID = utl_id;
    }

    public String getUTL_LOGIN() {
        return UTL_LOGIN;
    }

    public void setUTL_LOGIN(String utl_login) {
        UTL_LOGIN = utl_login;
    }

    public String getUTL_PASSWORD() {
        return UTL_PASSWORD;
    }

    public void setUTL_PASSWORD(String utl_password) {
        UTL_PASSWORD = utl_password;
    }

    public String getUTL_PRS_ID() {
        return UTL_PRS_ID;
    }

    public void setUTL_PRS_ID(String utl_prs_id) {
        UTL_PRS_ID = utl_prs_id;
    }

}
