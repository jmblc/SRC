package fr.igestion.crm.bean;

import java.sql.Blob;

public class StreamFile {

    private String nomFichier = "";
    private String extension = "";
    private byte[] contenuByte = null;
    private Blob blob = null;

    public StreamFile() {

    }

    public Blob getBlob() {
        return blob;
    }

    public void setBlob(Blob blob) {
        this.blob = blob;
    }

    public byte[] getContenuByte() {
        return contenuByte;
    }

    public void setContenuByte(byte[] contenuByte) {
        this.contenuByte = contenuByte.clone();
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

}
