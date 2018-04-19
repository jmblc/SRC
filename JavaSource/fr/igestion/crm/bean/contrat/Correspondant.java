package fr.igestion.crm.bean.contrat;


public class Correspondant {

    private Adresse adresse = null;
    private Personne personne = null;

    public Correspondant() {
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

}
