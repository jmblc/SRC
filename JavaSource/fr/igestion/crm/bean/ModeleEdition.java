package fr.igestion.crm.bean;

public class ModeleEdition {

    private String ID = "";
    private String LIBELLE = "";
    private String FICHIER = "";
    private String REPERTOIRE = "";
    private String FONCTION = "";

    public ModeleEdition() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getLIBELLE() {
        return LIBELLE;
    }

    public void setLIBELLE(String lIBELLE) {
        LIBELLE = lIBELLE;
    }

    public String getFICHIER() {
        return FICHIER;
    }

    public void setFICHIER(String fICHIER) {
        FICHIER = fICHIER;
    }

    public String getREPERTOIRE() {
        return REPERTOIRE;
    }

    public void setREPERTOIRE(String rEPERTOIRE) {
        REPERTOIRE = rEPERTOIRE;
    }

    public String getFONCTION() {
        return FONCTION;
    }

    public void setFONCTION(String fONCTION) {
        FONCTION = fONCTION;
    }

}
