package fr.igestion.crm.bean.parametrage;

import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;

public class Regroupement<T> implements Serializable {

	private static final long serialVersionUID = -9121506906236011507L;	
	public static final String type_campagnes_stats = "type_campagnes_stats";
	public static final String type_auteurs_stats = "type_auteurs_stats";
	
	protected int id;
	protected String code;
	protected String libelle;
	protected String type;
	Vector<T> listeDetails = new Vector<T>();
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the libelle
	 */
	public String getLibelle() {
		return libelle;
	}
	/**
	 * @param libelle the libelle to set
	 */
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUniqueId() {
		return (type!=null?type:"") + "_" + id;
	}
	/**
	 * @return the listeDetails
	 */
	public Vector<T> getListeDetails() {
		return listeDetails;
	}
	
	public void add(T detail) {
		this.listeDetails.add(detail);
	}
	
	public void remove(T detail) {
		this.listeDetails.remove(detail);
	}
	
	public int size() {
		return this.listeDetails.size();
	}
	/**
	 * @return
	 * @see java.util.Vector#isEmpty()
	 */
	public boolean isEmpty() {
		return listeDetails.isEmpty();
	}
	/**
	 * @param o
	 * @return
	 * @see java.util.Vector#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return listeDetails.contains(o);
	}
	/**
	 * @param index
	 * @return
	 * @see java.util.Vector#get(int)
	 */
	public T get(int index) {
		return listeDetails.get(index);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.Vector#remove(int)
	 */
	public T remove(int index) {
		return listeDetails.remove(index);
	}
	/**
	 * 
	 * @see java.util.Vector#clear()
	 */
	public void clear() {
		listeDetails.clear();
	}
	/**
	 * @param c
	 * @return
	 * @see java.util.Vector#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> c) {
		return listeDetails.containsAll(c);
	}
	/**
	 * @param c
	 * @return
	 * @see java.util.Vector#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends T> c) {
		return listeDetails.addAll(c);
	}
	/**
	 * @param c
	 * @return
	 * @see java.util.Vector#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> c) {
		return listeDetails.removeAll(c);
	}
	

}
