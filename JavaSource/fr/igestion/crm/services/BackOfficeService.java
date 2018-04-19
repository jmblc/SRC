package fr.igestion.crm.services;

import java.util.ArrayList;
import java.util.List;

import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.bean.TeleActeur;
import fr.igestion.crm.bean.parametrage.ListeRegroupements;
import fr.igestion.crm.bean.parametrage.Regroupement;
import fr.igestion.crm.bean.scenario.Campagne;
import fr.igestion.crm.config.IContacts;
import fr.igestion.crm.config.IObjectPersistence;
import fr.igestion.crm.config.ParametrageApp;

public class BackOfficeService extends ParametrageApp {
	
	private static final long serialVersionUID = 1L;

	public static ParametrageApp getParametrage() throws Exception {
		if (instance == null) {
			String nomClassePersistence = IContacts.getInstance().getNOM_CLASSE_PERSISTENCE();
			String nomRepertoireSauvegarde = IContacts.getInstance().getNOM_REPERTOIRE_STOCKAGE();
			IObjectPersistence objectPersistence = (IObjectPersistence) Class.forName(nomClassePersistence).newInstance();
			objectPersistence.initParams(nomRepertoireSauvegarde, ParametrageApp.class);
			ParametrageApp.setParametrageAppPersistence(objectPersistence);
			instance = ParametrageApp.getInstance();
		}
		return instance;
	}

	public static void sauvegarder() throws Exception {
		getParametrage().save();
	}
	
	public static String libellePourCode(String code) throws Exception {
		return getParametrage().getLibellePourCode(code);
	}
	
	public static String effetPourCode(String code) throws Exception {
		return getParametrage().getEffetPourCode(code);
	}
	
	public static void regrouperCampagnesPourStats(String libelle, String... campagnes) throws Exception {
		creerRegroupement(libelle, Regroupement.type_campagnes_stats, campagnes);
	}
	
	public static void regrouperAuteursPourStats(String libelle, String... auteurs) throws Exception {
		creerRegroupement(libelle, Regroupement.type_auteurs_stats, auteurs);
	}
	
	public static Regroupement<String> creerRegroupement(String libelle, String type, String... elements) throws Exception {
		
		ListeRegroupements<String> listeExistante = getListeRegroupements(type);
		Regroupement<String> regroupement = listeExistante.getRegroupementParCodeLibelle(libelle);
		if (regroupement != null) {
			listeExistante.remove(regroupement);
		}
		regroupement = new Regroupement<String>();
		regroupement.setLibelle(libelle);
		regroupement.setType(type);
		for (String element : elements) {
			regroupement.add(element);
		}
		listeExistante.add(regroupement);
		sauvegarder();
		return regroupement;
	}
	
	public static void supprimerRegroupement(String uniqueId) throws Exception {
		ListeRegroupements<String> liste = getListeRegroupements(uniqueId);
		if (liste != null) {
			Regroupement<String> regroupement = liste.getRegroupementParUniqueId(uniqueId);
			if (regroupement != null) {
				liste.remove(regroupement);
				sauvegarder();
			}
		}
	}
	
	public static List<Object> getDetailRegroupement(String uniqueId) throws Exception {
		
		List<Object> result = new ArrayList<Object>();
		if (uniqueId != null) {
			Regroupement<String> regroupement = getRegroupement(uniqueId);
			if (regroupement != null) {
				for (String idDetail : regroupement.getListeDetails()) {
					if (Regroupement.type_campagnes_stats.equals(regroupement.getType())) {
						Campagne campagne = SQLDataService.getCampagneById(idDetail);
						if (campagne != null) {
							result.add(campagne);
						}
					} else if (Regroupement.type_auteurs_stats.equals(regroupement.getType())) {
						TeleActeur teleActeur = SQLDataService.getTeleActeurById(idDetail);
						if (teleActeur != null) {
							result.add(teleActeur);
						}
					}
				}
			}
		}
		return result;
	}
	
	public static ListeRegroupements<String> getListeRegroupements(String type) throws Exception {
		
		ListeRegroupements<String> liste = null;		
		if (type != null && type.startsWith(Regroupement.type_auteurs_stats)) {
			liste = getParametrage().getRegroupementsAuteurs();
			
		} else if (type != null && type.startsWith(Regroupement.type_campagnes_stats)) {
			liste = getParametrage().getRegroupementsCampagnes();
		}		
		return liste;
	}
	
	public static Regroupement<String> getRegroupement(String uniqueId) throws Exception {
		
		Regroupement<String> regroupement = null;		
		ListeRegroupements<String> liste = getListeRegroupements(uniqueId);
		if (liste != null) {
			regroupement = liste.getRegroupementParUniqueId(uniqueId);
		}
		return regroupement;
	}
	
}
