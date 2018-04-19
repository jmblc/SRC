package fr.igestion.crm.bean.scenario;

import java.util.ArrayList;
import java.util.Collection;

public class Motif extends ItemScenario {
	
	private Collection<SousMotif> sousMotifs;

    public Motif() {
    	
    }

	/**
	 * @return the sousMotifs
	 */
	public Collection<SousMotif> getSousMotifs() {
		return sousMotifs;
	}

	/**
	 * @param sousMotifs the sousMotifs to set
	 */
	public void setSousMotifs(Collection<SousMotif> sousMotifs) {
		this.sousMotifs = sousMotifs;
	}
	
	public void addSousMotif(SousMotif sousMotif) {
		if (sousMotifs == null) {
			sousMotifs = new ArrayList<SousMotif>();
		}
		sousMotifs.add(sousMotif);
	}
  

}
