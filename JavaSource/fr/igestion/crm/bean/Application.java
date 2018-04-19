package fr.igestion.crm.bean;

public class Application {

    private String ID;
    private String APP_ID;
    private String APP_LIB;
    private String APP_ALIAS;
    private String APP_DESCRIPTION;
    private String APP_CHEMINEXE;
    private String APP_LIBREACCES;
    private String APP_ACTIF;

    public Application() {
    }

    public String getAPP_ACTIF() {
        return APP_ACTIF;
    }

    public void setAPP_ACTIF(String app_actif) {
        APP_ACTIF = app_actif;
    }

    public String getAPP_ALIAS() {
        return APP_ALIAS;
    }

    public void setAPP_ALIAS(String app_alias) {
        APP_ALIAS = app_alias;
    }

    public String getAPP_CHEMINEXE() {
        return APP_CHEMINEXE;
    }

    public void setAPP_CHEMINEXE(String app_cheminexe) {
        APP_CHEMINEXE = app_cheminexe;
    }

    public String getAPP_DESCRIPTION() {
        return APP_DESCRIPTION;
    }

    public void setAPP_DESCRIPTION(String app_description) {
        APP_DESCRIPTION = app_description;
    }

    public String getAPP_ID() {
        return APP_ID;
    }

    public void setAPP_ID(String app_id) {
        APP_ID = app_id;
    }

    public String getAPP_LIB() {
        return APP_LIB;
    }

    public void setAPP_LIB(String app_lib) {
        APP_LIB = app_lib;
    }

    public String getAPP_LIBREACCES() {
        return APP_LIBREACCES;
    }

    public void setAPP_LIBREACCES(String app_libreacces) {
        APP_LIBREACCES = app_libreacces;
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID = id;
    }

}
