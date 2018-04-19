package fr.igestion.crm.bean.document;

import java.util.Date;

import org.apache.struts.upload.FormFile;

public class Document {

    private String id; 
    private String libelle;
    private String type;
    private String description;
    private Date debut;
    private Date fin;
    private FormFile fichier;
    private String nomFichier; 
    private byte[] contenu; 
    
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Date getDebut() {
        return debut;
    }
    public void setDebut(Date debut) {
        this.debut = debut;
    }
    public Date getFin() {
        return fin;
    }
    public void setFin(Date fin) {
        this.fin = fin;
    }
    public FormFile getFichier() {
        return fichier;
    }
    public void setFichier(FormFile fichier) {
        this.fichier = fichier;
    }
    public String getNomFichier() {
        return nomFichier;
    }
    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }
    public byte[] getContenu() {
        return contenu;
    }
    public void setContenu(byte[] contenu) {
        this.contenu = contenu;
    }

}
