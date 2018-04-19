package fr.igestion.crm.bean.scenario;

import java.util.ArrayList;
import java.util.Collection;

public class Point extends ItemScenario {

    private String sous_motif_id = "";
    private Collection<SousPoint> sousPoints;

    public Point() {
    }
 
    public String getSousMotifId() {
        return sous_motif_id;
    }

    public void setSousMotifId(String sousMotifId) {
        sous_motif_id = sousMotifId;
    }

	/**
	 * @return the sousPoints
	 */
	public Collection<SousPoint> getSousPoints() {
		return sousPoints;
	}

	/**
	 * @param sousPoints the sousPoints to set
	 */
	public void setSousPoints(Collection<SousPoint> sousPoints) {
		this.sousPoints = sousPoints;
	}
	
	public void addSousPoint(SousPoint sousPoint) {
		if (sousPoints == null) {
			sousPoints = new ArrayList<SousPoint>();
		}
		sousPoints.add(sousPoint);
	}
 
}
