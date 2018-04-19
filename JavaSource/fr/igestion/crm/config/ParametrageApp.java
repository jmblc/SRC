package fr.igestion.crm.config;

import java.io.Serializable;
import java.util.Hashtable;

import fr.igestion.crm.bean.parametrage.ListeRegroupements;

public class ParametrageApp implements Serializable {

	private static final long serialVersionUID = 5812199384916354837L;
	private static IObjectPersistence parametrageAppPersistence;
	public static final String effet_deverrouillage = "effet_deverrouillage";
	public static final String id_sauvegarde = "ParametrageApp.save"; 
	
	protected static ParametrageApp instance;
	
	private Hashtable<String, String> adaptateur_codes_libelles;
	private Hashtable<String, String> adaptateur_codes_effets;
	private ListeRegroupements<String> regroupementsCampagnes;
	private ListeRegroupements<String> regroupementsAuteurs;	

	protected ParametrageApp() {}
	
	protected static ParametrageApp getInstance() {
		
		if (instance == null) {
			try {
				instance = (ParametrageApp) parametrageAppPersistence.loadObject(id_sauvegarde);
			} catch (Exception e) {
				e.printStackTrace();
				instance = new ParametrageApp();
			} 
			instance.init();
		}
		return instance;
	}
	
	public boolean save() {
		boolean resultat = true;		
		try {
			parametrageAppPersistence.saveObject(id_sauvegarde, this);
		} catch (Exception e) {
			resultat = false;
			e.printStackTrace();
		}		
		return resultat;
	}
	
	public void finalize() {
		save();		
	}
	
	private void init() {
		
		if (adaptateur_codes_libelles == null) {
			adaptateur_codes_libelles = new Hashtable<String, String>();
		}
		adaptateur_codes_libelles.put("ATRAITER", "H.Courriers N2");
		adaptateur_codes_libelles.put("CLOTURE", "Traité N1");
		adaptateur_codes_libelles.put("TRANSFERE_A", "Mail interne");
		adaptateur_codes_libelles.put("TRANSFERE_EX", "Mail externe");		
		
		if (adaptateur_codes_effets == null) {
			adaptateur_codes_effets = new Hashtable<String, String>();
		}
		adaptateur_codes_effets.put("ATRAITER", effet_deverrouillage);
		
		if (regroupementsCampagnes == null) {
			regroupementsCampagnes = new ListeRegroupements<String>();
		}
		if (regroupementsAuteurs == null) {
			regroupementsAuteurs = new ListeRegroupements<String>();
		}
	}
	
	public String getLibellePourCode(String code) {
		return this.adaptateur_codes_libelles.get(code);
	}
	
	public String getEffetPourCode(String code) {
		return this.adaptateur_codes_effets.get(code);
	}

	/**
	 * @return the regroupementsCampagnes
	 */
	public ListeRegroupements<String> getRegroupementsCampagnes() {
		return regroupementsCampagnes;
	}

	/**
	 * @return the regroupementsAuteurs
	 */
	public ListeRegroupements<String> getRegroupementsAuteurs() {
		return regroupementsAuteurs;
	}

	/**
	 * @param parametrageAppPersistence the parametrageAppPersistence to set
	 */
	public static void setParametrageAppPersistence(IObjectPersistence parametrageAppPersistence) {
		ParametrageApp.parametrageAppPersistence = parametrageAppPersistence;
	}

}
