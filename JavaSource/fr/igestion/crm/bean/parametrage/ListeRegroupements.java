package fr.igestion.crm.bean.parametrage;

import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;

public class ListeRegroupements<T> implements Serializable {

	private static final long serialVersionUID = 4595432506360880549L;
	
	Vector<Regroupement<T>> regroupements = new Vector<Regroupement<T>>();
	
	private int maxId = 0;
	
	public ListeRegroupements<T> getRegroupementsParType(String type) {
		
		ListeRegroupements<T> result = new ListeRegroupements<T>();		
		for (Regroupement<T> regroupement: regroupements) {
			if (type.equals(regroupement.getType())) {
				result.add(regroupement);
			}
		}
		return result;
	}
	
	public Regroupement<T> getRegroupementParId(long id) {
		
		Regroupement<T> result = null;
		for (Regroupement<T> regroupement: regroupements) {
			if (id == regroupement.getId()) {
				result = regroupement;
				break;
			}
		}
		return result;
	}
	
	public Regroupement<T> getRegroupementParUniqueId(String uniqueId) {
		
		Regroupement<T> result = null;
		for (Regroupement<T> regroupement: regroupements) {
			if (regroupement.getUniqueId().equals(uniqueId)) {
				result = regroupement;
				break;
			}
		}
		return result;
	}
	
	public Regroupement<T> getRegroupementParCodeLibelle(String codeLibelle) {
		
		Regroupement<T> result = null;
		for (Regroupement<T> regroupement: regroupements) {
			if (codeLibelle.equals(regroupement.getCode()) || codeLibelle.equals(regroupement.getLibelle())) {
				result = regroupement;
				break;
			}
		}
		return result;
	}
	
	public boolean add(Regroupement<T> regroupement) {		
		if (maxId < regroupements.size()) {
			for (Regroupement<T> reg : regroupements) {
				if (reg.getId() > maxId) {
					maxId = reg.getId();
				}
			}
		}
		maxId++;
		regroupement.setId(maxId);
		return regroupements.add(regroupement);
	}
	
	public void remove(Regroupement<T> regroupement) {
		regroupements.remove(regroupement);
	}
	

	/**
	 * @return the regroupements
	 */
	public Vector<Regroupement<T>> getListeRegroupements() {
		return regroupements;
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.Vector#remove(int)
	 */
	public Regroupement<T> remove(int index) {
		return regroupements.remove(index);
	}

	/**
	 * 
	 * @see java.util.Vector#clear()
	 */
	public void clear() {
		regroupements.clear();
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.Vector#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends Regroupement<T>> c) {
		return regroupements.addAll(c);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.Vector#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> c) {
		return regroupements.removeAll(c);
	}


}
