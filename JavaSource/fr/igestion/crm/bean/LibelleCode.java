package fr.igestion.crm.bean;

import fr.igestion.crm.services.BackOfficeService;

public class LibelleCode {

    private String code = "";
    private String libelle = "";
    private String alias = "";
    private String actif = "";

    public LibelleCode() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

	public String getLibelle() {
		try {
			if (BackOfficeService.libellePourCode(alias) != null) {
				return BackOfficeService.libellePourCode(alias);
			}
		} catch (Exception e) {
		}
		return libelle;
	}

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getActif() {
        return actif;
    }

    public void setActif(String actif) {
        this.actif = actif;
    }
    
    public String getEffet() { 
        try {
			return BackOfficeService.effetPourCode(alias);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }

}
