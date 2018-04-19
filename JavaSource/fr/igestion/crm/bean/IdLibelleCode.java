package fr.igestion.crm.bean;

import org.apache.commons.lang.StringUtils;

public class IdLibelleCode extends LibelleCode implements Comparable<IdLibelleCode> {

	private String id = "";

	public IdLibelleCode() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int compareTo(IdLibelleCode obj) {

		if (obj == null || !obj.getClass().equals(getClass())) {
			return 1;
		}
		if (StringUtils.isBlank(id) || StringUtils.isBlank(obj.getId())) {
			String currentLibelle = getLibelle() != null ? getLibelle().trim() : "";
			String otherLibelle = obj.getLibelle() != null ? obj.getLibelle().trim() : "";
			return currentLibelle.compareToIgnoreCase(otherLibelle);
		} else {
			return id.compareToIgnoreCase(obj.getId());
		}
	}

	public boolean equals(IdLibelleCode obj) {
		return compareTo(obj) == 0;
	}

}
