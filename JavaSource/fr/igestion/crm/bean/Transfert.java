package fr.igestion.crm.bean;

import java.util.ArrayList;
import java.util.Collection;

import fr.igestion.crm.bean.scenario.Campagne;

public class Transfert {

    String TRA_ID;
    String TRA_LIBELLE;
    String TRA_EMAIL;
    Collection<Campagne> campagnesAffectees;

    public Transfert() {

    }

    public String getTRA_ID() {
        return TRA_ID;
    }

    public void setTRA_ID(String tRAID) {
        TRA_ID = tRAID;
    }

    public String getTRA_LIBELLE() {
        return TRA_LIBELLE;
    }

    public void setTRA_LIBELLE(String tRALIBELLE) {
        TRA_LIBELLE = tRALIBELLE;
    }

    public String getTRA_EMAIL() {
        return TRA_EMAIL;
    }

    public void setTRA_EMAIL(String tRAEMAIL) {
        TRA_EMAIL = tRAEMAIL;
    }

	/**
	 * @return the idCampagnesAffectees
	 */
	public Collection<Campagne> getCampagnesAffectees() {
		return campagnesAffectees;
	}
	
	public String getLibellesCampagnes() {
		
		StringBuffer result = new StringBuffer();
		if (campagnesAffectees != null) {
			int index = 0;
			for (Campagne campagne : campagnesAffectees) {
				if (index > 0) {
					result.append(" | ");
				}
				result.append(campagne.getLibelle());
				index++;
			}
		}
		return result.toString();
	}
	
	public String getLibellesCourtsCampagnes() {
		
		String result = getLibellesCampagnes();
		if (result.length() > 40) {
			result = result.substring(0, 37).concat("...");
		}
		return result;
	}

	/**
	 * @param campagnesAffectees the campagnesAffectees to set
	 */
	public void setCampagnesAffectees(Collection<Campagne> campagnesAffectees) {
		this.campagnesAffectees = campagnesAffectees;
	}
	
	public void addCampagneAffectee(Campagne campagne) {
		Collection<Campagne> col = campagnesAffectees == null ? new ArrayList<Campagne>() : campagnesAffectees;
		col.add(campagne);
		campagnesAffectees = col;
	}

}
