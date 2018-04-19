package fr.igestion.crm.bean;

public class LigneDVS {

    private String N_DOCUMENT_ID = "";
    private String C_MIME = "";
    private String DATE_INSERTION = "";
    private String PJ = "";
    private byte[] contenuByte;
    
    public LigneDVS() {
    }
    
    public String getPJ() {
        return PJ;
    }

    public void setPJ(String pJ) {
        PJ = pJ;
    }

    public String getN_DOCUMENT_ID() {
        return N_DOCUMENT_ID;
    }

    public void setN_DOCUMENT_ID(String nDOCUMENTID) {
        N_DOCUMENT_ID = nDOCUMENTID;
    }

    public String getC_MIME() {
        return C_MIME;
    }

    public void setC_MIME(String cMIME) {
        C_MIME = cMIME;
    }

    public String getDATE_INSERTION() {
        return DATE_INSERTION;
    }

    public void setDATE_INSERTION(String dATEINSERTION) {
        DATE_INSERTION = dATEINSERTION;
    }

    public byte[] getContenuByte() {
        return contenuByte;
    }

    public void setContenuByte(byte[] contenuByte) {
        this.contenuByte = contenuByte.clone();
    }

}
