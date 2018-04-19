package fr.igestion.crm.bean;

import fr.igestion.crm.bean.document.Document;

public class ModeleProcedureMail {

    private String id;
    private String libelle;
    private String type;
    private String mailObjet;
    private String mailInvite;
    private boolean recapAdherent;
    private String mailCorps;
    private String mailSignature;
    private String typeDestinataire;
    private boolean recapCentreGestion;
    private Document document;

    public boolean isRecapAdherent() {
        return recapAdherent;
    }

    public void setRecapAdherent(boolean recapAdherent) {
        this.recapAdherent = recapAdherent;
    }

    public boolean isRecapCentreGestion() {
        return recapCentreGestion;
    }

    public void setRecapCentreGestion(boolean recapCentreGestion) {
        this.recapCentreGestion = recapCentreGestion;
    }

    public String getTypeDestinataire() {
        return typeDestinataire;
    }

    public void setTypeDestinataire(String typeDestinataire) {
        this.typeDestinataire = typeDestinataire;
    }

    public String getMailObjet() {
        return mailObjet;
    }

    public void setMailObjet(String mailObjet) {
        this.mailObjet = mailObjet;
    }

    public String getMailInvite() {
        return mailInvite;
    }

    public void setMailInvite(String mailInvite) {
        this.mailInvite = mailInvite;
    }

    public String getMailCorps() {
        return mailCorps;
    }

    public void setMailCorps(String mailCorps) {
        this.mailCorps = mailCorps;
    }

    public String getMailSignature() {
        return mailSignature;
    }

    public void setMailSignature(String mailSignature) {
        this.mailSignature = mailSignature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    
    
}
