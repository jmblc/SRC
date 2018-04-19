package fr.igestion.crm.bean.contrat;

import fr.igestion.crm.bean.IdLibelleCode;

public class Mutuelle extends IdLibelleCode {
    
    private String logo = "";
    private String actif = "";

    public Mutuelle() {
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getActif() {
        return actif;
    }

    public void setActif(String actif) {
        this.actif = actif;
    }

}
