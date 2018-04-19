package fr.igestion.crm.bean.contrat;

import java.util.Date;

public class Adresse {

    private String ID = "";
    private String LIGNE_1 = "";
    private String LIGNE_2 = "";
    private String LIGNE_3 = "";
    private String CODEPOSTAL = "";
    private String LOCALITE = "";
    private String PAYS = "";
    private String TELEPHONEFIXE = "";
    private String TELEPHONEAUTRE = "";
    private String TELECOPIE = "";
    private String COURRIEL = "";
    private String LIGNE_4 = "";
    private Date DEBUT_EFFET = null;
    private Date FIN_EFFET = null;

    public Adresse() {
    }

    public String getCODEPOSTAL() {
        return CODEPOSTAL;
    }

    public void setCODEPOSTAL(String codepostal) {
        CODEPOSTAL = codepostal;
    }

    public String getCOURRIEL() {
        return COURRIEL;
    }

    public void setCOURRIEL(String courriel) {
        COURRIEL = courriel;
    }

    public Date getDEBUT_EFFET() {
        return DEBUT_EFFET;
    }

    public void setDEBUT_EFFET(Date debut_effet) {
        DEBUT_EFFET = debut_effet;
    }

    public Date getFIN_EFFET() {
        return FIN_EFFET;
    }

    public void setFIN_EFFET(Date fin_effet) {
        FIN_EFFET = fin_effet;
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getLIGNE_1() {
        return LIGNE_1;
    }

    public void setLIGNE_1(String ligne_1) {
        LIGNE_1 = ligne_1;
    }

    public String getLIGNE_2() {
        return LIGNE_2;
    }

    public void setLIGNE_2(String ligne_2) {
        LIGNE_2 = ligne_2;
    }

    public String getLIGNE_3() {
        return LIGNE_3;
    }

    public void setLIGNE_3(String ligne_3) {
        LIGNE_3 = ligne_3;
    }

    public String getLIGNE_4() {
        return LIGNE_4;
    }

    public void setLIGNE_4(String ligne_4) {
        LIGNE_4 = ligne_4;
    }

    public String getLOCALITE() {
        return LOCALITE;
    }

    public void setLOCALITE(String localite) {
        LOCALITE = localite;
    }

    public String getPAYS() {
        return PAYS;
    }

    public void setPAYS(String pays) {
        PAYS = pays;
    }

    public String getTELECOPIE() {
        return TELECOPIE;
    }

    public void setTELECOPIE(String telecopie) {
        TELECOPIE = telecopie;
    }

    public String getTELEPHONEAUTRE() {
        return TELEPHONEAUTRE;
    }

    public void setTELEPHONEAUTRE(String telephoneautre) {
        TELEPHONEAUTRE = telephoneautre;
    }

    public String getTELEPHONEFIXE() {
        return TELEPHONEFIXE;
    }

    public void setTELEPHONEFIXE(String telephonefixe) {
        TELEPHONEFIXE = telephonefixe;
    }

}
