package fr.igestion.crm.bean.contrat;

import fr.igestion.crm.bean.IdLibelleCode;

public class Camp_EntiteGestion extends IdLibelleCode {
    
    private String campagne_id="";
    private String entite_gestion_id="";
    private String libelle="";
    private String id_scenario="";
    
	public String getCampagne_id() {
		return campagne_id;
	}
	public void setCampagne_id(String id_campagne) {
		this.campagne_id = id_campagne;
	}
	public String getEntite_gestion_id() {
		return entite_gestion_id;
	}
	public void setEntite_gestion_id(String id_entite_gestion) {
		this.entite_gestion_id = id_entite_gestion;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getid_secenario()  {
		return id_scenario;
	}
	public void setid_secenario(String id_scenario) {
		this.id_scenario = id_scenario;
	}
}
