package fr.igestion.crm.bean.scenario;

import java.util.ArrayList;
import java.util.Collection;

import fr.igestion.crm.bean.Message;
import fr.igestion.crm.bean.contrat.Mutuelle;
import fr.igestion.crm.bean.contrat.Camp_EntiteGestion;
public class Campagne {

    private String id = "";
    private String libelle = "";
    private String tel = "";
    private String codeFT = "";
    private String actif = "";
    private Integer FLAG_ENTITE_GESTION=0;

    // Objets
    private Collection<Mutuelle> mutuelles = new ArrayList<Mutuelle>();
    private Collection<Message> messages = new ArrayList<Message>();
    private Collection<Camp_EntiteGestion> Camp_EntiteGestions = new ArrayList<Camp_EntiteGestion>();

    public Campagne() {
    }

    public String getCodeFT() {
        return codeFT;
    }

    public void setCodeFT(String codeFT) {
        this.codeFT = codeFT;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Collection<Mutuelle> getMutuelles() {
        return mutuelles;
    }

    public void setMutuelles(Collection<Mutuelle> mutuelles) {
        this.mutuelles = mutuelles;
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public void setMessages(Collection<Message> messages) {
        this.messages = messages;
    }

    public String getActif() {
        return actif;
    }

    public void setActif(String actif) {
        this.actif = actif;
    }

	public Integer getFLAG_ENTITE_GESTION() {
		return FLAG_ENTITE_GESTION;
	}

	public void setFLAG_ENTITE_GESTION(Integer fLAG_ENTITE_GESTION) {
		FLAG_ENTITE_GESTION = fLAG_ENTITE_GESTION;
	}

	public Collection<Camp_EntiteGestion> getCamp_EntiteGestions() {
		return Camp_EntiteGestions;
	}

	public void setCamp_EntiteGestions(Collection<Camp_EntiteGestion> camp_EntiteGestions) {
		Camp_EntiteGestions = camp_EntiteGestions;
	}

}
