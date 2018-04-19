package fr.igestion.crm.bean.contrat;

import java.util.Date;

public class AbonnementService {

    private String    libelle;
    private String    debut;
    private String    fin;
    private String    actif;
    
    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDebut() {
        return debut;
    }

    public void setDebut(String debut) {
        this.debut = debut;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public String getActif() {
        return actif;
    }

    public void setActif(String actif) {
        this.actif = actif;
    }
    
    public AbonnementService() {
    }

}
