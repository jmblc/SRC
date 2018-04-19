package fr.igestion.crm.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import fr.igestion.crm.CrmUtil;
import fr.igestion.crm.IContactsException;
import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.UtilDate;
import fr.igestion.crm.bean.Appel;
import fr.igestion.crm.bean.Appelant;
import fr.igestion.crm.bean.LigneExcel;
import fr.igestion.crm.bean.Limite;
import fr.igestion.crm.bean.ModeleEdition;
import fr.igestion.crm.bean.TeleActeur;
import fr.igestion.crm.bean.contrat.Adresse;
import fr.igestion.crm.bean.contrat.Beneficiaire;
import fr.igestion.crm.bean.contrat.Etablissement;
import fr.igestion.crm.bean.contrat.Personne;
import fr.igestion.crm.bean.evenement.Evenement;
import fr.igestion.crm.bean.parametrage.Regroupement;
import fr.igestion.crm.bean.scenario.Campagne;
import fr.igestion.crm.config.IContacts;
import fr.igestion.crm.services.BackOfficeService;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class RechercheEtStatistiques extends HttpServlet {
	
    private static final Logger LOGGER = Logger.getLogger(RechercheEtStatistiques.class);
    
    private static final long serialVersionUID = 1L;

    private static String _idRapport = null;
    private static String _nomFichier = null;

    private String[] _tab_colonnes = null;
    private int _nbr_tab_colonnes = 0;
    private static int limite_lignes = 65500;

    private static String _DATEDEBUT = null;
    private static String _DATEFIN = null;

    private static String _RESOLU = null;
    
    private static String _TELEACTEUR_ID = null;
    private static String[] CAMPAGNE_ID = null;
    private static String REGROUPEMENT_CAMPAGNES = null;
    private static String REGROUPEMENT_AUTEURS = null;
    private static String[] CRITERES_ENREGISTRES = null;


    private static String _MUTUELLE_ID = null;
    private static String _MUTUELLE_TEXT = null;

    private static String _SITE_ID = null;
    private static String _SITE_TEXT = null;

    private static String[] _CREATEUR_ID = null;
    private static String _CREATEUR_TEXT = null;

    private static String _REFERENCE_ID = null;
    private static String _REFERENCE_TEXT = null;

    private static String _STATUT_ID = null;
    private static String _STATUT_TEXT = null;
        

    private WritableFont _font_arial_8_bold_noir = null;
    private WritableCellFormat _wcf_font_arial_8_bold_noir = null;

    private WritableFont _font_arial_8_noir = null;
    private WritableCellFormat _wcf_font_arial_8_noir = null;

    private WritableFont _font_arial_8_noir_bold_fond_bleu = null;
    private WritableCellFormat _wcf_font_arial_8_noir_bold_fond_bleu = null;

    private WritableFont _font_arial_8_noir_fond_bleu_left = null;
    private WritableCellFormat _wcf_font_arial_8_noir_fond_bleu_left = null;

    private WritableFont _font_arial_8_noir_fond_bleu_right = null;
    private WritableCellFormat _wcf_font_arial_8_noir_fond_bleu_right = null;

    private WritableFont _font_arial_8_noir_bold_fond_violet = null;
    private WritableCellFormat _wcf_font_arial_8_noir_bold_fond_violet = null;

    private WritableFont _font_bordure = null;
    private WritableCellFormat _wcf_font_bordure = null;
    private WritableCellFormat _wcf_font_sans_bordure = null;

    private WritableFont _wf_datas = null;
    private WritableCellFormat _wcf_datas = null;
    private WritableCellFormat _wcf_datas_left = null;

    private WritableFont _wf_datas_bold = null;
    private WritableCellFormat _wcf_datas_bold = null;

    private WritableFont _wf_datas_pct = null;
    private WritableCellFormat _wcf_datas_pct = null;

    private WritableFont _wf_datas_flotant = null;
    private WritableCellFormat _wcf_datas_flotant = null;

    private WritableFont _wf_datas_flotant_bold = null;
    private WritableCellFormat _wcf_datas_flotant_bold = null;

    private WritableFont _font_arial_8_noir_bold_fond_gris = null;
    private WritableCellFormat _wcf_font_arial_8_noir_bold_fond_gris = null;
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {

			if ("/GetStatistiquesExcel.show".equals(request.getServletPath())) {
				getStatistiquesExcel(request, response);
			} else if ("/Recherche.show".equals(request.getServletPath())) {
				doRecherche(request, response);
			} else if ("/AfficheResultatRecherche.show".equals(request.getServletPath())) {
				afficheResultatRecherche(request, response);
			}
		} catch (Exception e) {
			throw new ServletException(e.getMessage());
		}
	}
    
	public void afficheResultatRecherche(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String idAppel = (String) request.getParameter("idAppel");
		String modeOuverture = (String) request.getParameter("modeOuverture");
		String appz = (String) request.getParameter("appz");

		String teleacteur_id = "";
		TeleActeur teleActeur = (TeleActeur) request.getSession().getAttribute(IContacts._var_session_teleActeur);
		if (teleActeur != null) {
			teleacteur_id = teleActeur.getId();
		} else {
			teleacteur_id = (String) request.getParameter("teleacteur_id");
			if (teleacteur_id == null) {
				String login = (String) request.getParameter("login");
				teleActeur = SQLDataService.getTeleActeurByLogin(login);
				teleacteur_id = teleActeur.getId();
			}
		}
		
		String contextPath = getServletContext().getContextPath();

		Appel appel = null;
		Collection<Evenement> documents_associes = null;
		boolean b_edition = false;
		// Calculer la valeur d'un booléen pour savoir si on est en édition ou
		// non
		// Si mode_ouverture = 'L', on n'est pas en edition (b_edition = false)
		// Si mode_ouverture = 'E' on essaie de réserver la fiche.
		// Si réservation ok : b_edition = true, sinon = false
		// En fonction de ce booléen b_edition :
		// - on donne la possibilité d'éditer le champ commentaire
		// - on calcule l'affichage de certains boutons (dépend aussi du statut)

		String message_blocage = "", img_blocage = "";
		if ("E".equals(modeOuverture)) {
			// Tentative de réservation de la fiche
			SQLDataService.lockerFicheAppel(teleacteur_id, idAppel);
			appel = SQLDataService.getAppelById(idAppel);

			if (appel.getEDITIONENCOURS().equals("1") && !teleacteur_id.equals(appel.getEDITEUR_ID())) {
				b_edition = false;
				TeleActeur bloqueur = SQLDataService.getTeleActeurById(appel.getEDITEUR_ID());
				if (bloqueur != null) {
					message_blocage = "Fiche en cours d'&eacute;dition par " + bloqueur.getPrenomNom() + " depuis le " + UtilDate.fmtDDMMYYYYHHMMSS(appel.getDATEEDITION());
					img_blocage = "<img src='" + contextPath + "/img/locked.gif'  align='bottom' border='0' class='message_box' id='ib_locked' disposition='right-middle' message='Fiche temporairement non &eacute;ditable.<br>Un utilisateur est d&eacute;j&agrave; sur cette fiche.' />";
				}
			} else {
				b_edition = true;
			}
		} else {
			appel = SQLDataService.getAppelById(idAppel);
		}

		// Infos fiche
		String statut = "", alias_statut = "", libelle_sous_statut = "", date_creation = "", date_modification = "", date_cloture = "", auteur = "", modificateur = "", clotureur = "", transferts = "";
		String type_appelant = "", alias_type_appelant = "";
		// Infos assures
		String prenom_nom_assure = "", numero_adherent_assure = "", qualite_assure = "", adresse_assure = "", telephones_assure = "";
		// Infos bénéficiaire appelant
		String nom_benef_appelant = "", numadh_benef_appelant = "", qualite_benef_appelant = "", adresse_benef_appelant = "", telephone_benef_appelant = "";
		// Infos entreprise
		String libelle_entreprise = "", entite_gestion_entreprise = "", numero_siret_entreprise = "",  telephones_entreprise = "";
		// Infos appelant
		String prenom_nom_appelant = "", etablissement_rs_appelant = "", code_adherent_appelant = "", numero_finess_appelant = "", telephones_appelant = "";
		// Infos scénario
		String campagne = "", mutuelle = "", motif = "", sous_motif = "", point = "", sous_point = "";
		// Infos clôture
		String commentaires = "", satisfaction = "", alias_satisfaction = "", img_satisfaction = "", urgent = "", reclamation = "", a_rappeler = "";
		// Lien vers document généré
		String lien_document_genere = "";
		String resolu = "";

		if (appel != null) {
			statut = appel.getStatut();
			alias_statut = appel.getAliasStatut();

			campagne = appel.getCampagne();
			mutuelle = appel.getMutuelle();
			libelle_sous_statut = appel.getLibelleSousStatut();
			date_creation = UtilDate.fmtDDMMYYYYHHMMSS(appel.getDATEAPPEL());
			date_modification = UtilDate.fmtDDMMYYYYHHMMSS(appel.getDATEMODIFICATION());
			date_cloture = UtilDate.fmtDDMMYYYYHHMMSS(appel.getDATECLOTURE());

			if (!"".equals(appel.getCREATEUR_ID())) {
				TeleActeur teleacteur_auteur = SQLDataService.getTeleActeurById(appel.getCREATEUR_ID());
				if (teleacteur_auteur != null) {
					auteur = " par " + teleacteur_auteur.getPrenomNom();
				}
			}

			if (!"".equals(appel.getMODIFICATEUR_ID())) {
				TeleActeur teleacteur_modificateur = SQLDataService.getTeleActeurById(appel.getMODIFICATEUR_ID());
				if (teleacteur_modificateur != null) {
					modificateur = " par " + teleacteur_modificateur.getPrenomNom();
				}
			}

			if (!"".equals(appel.getCLOTUREUR_ID())) {
				TeleActeur teleacteur_clotureur = SQLDataService.getTeleActeurById(appel.getCLOTUREUR_ID());
				if (teleacteur_clotureur != null) {
					clotureur = " par " + teleacteur_clotureur.getPrenomNom();
				}
			}

			if (!alias_statut.equalsIgnoreCase("HORSCIBLE")) {
				type_appelant = appel.getTypeAppelant();
				alias_type_appelant = appel.getAliasTypAppelant();
				
				// Infos assuré

				String benef_id = appel.getBENEFICIAIRE_ID();
				String benef_appelant_id = appel.getBENEF_APPELANT_ID();
				
				if (StringUtils.isNotBlank(benef_id)) {
					
					Beneficiaire assure = SQLDataService.getBeneficiaireById(benef_id);
					Beneficiaire benef_appelant = null;
					if (StringUtils.isNotBlank(benef_appelant_id) && !benef_appelant_id.equals(benef_id)) {
						benef_appelant = SQLDataService.getBeneficiaireById(benef_appelant_id);
					}
					if (assure != null) {
						numero_adherent_assure = assure.getCODE();
						qualite_assure = assure.getQualite();
						Personne personne_assure = SQLDataService.getPersonneById(assure.getPERSONNE_ID());
						if (personne_assure != null) {
							prenom_nom_assure = personne_assure.getPrenomNom();
							Adresse adresse = SQLDataService.getAdresseById(personne_assure.getADRESSE_ID());
							if (adresse != null) {

								if (!"".equals(adresse.getLIGNE_1())) {
									adresse_assure = adresse.getLIGNE_1() + "&nbsp;&nbsp;";
								}
								if (!"".equals(adresse.getLIGNE_2())) {
									adresse_assure = adresse_assure + adresse.getLIGNE_2() + "&nbsp;&nbsp;";
								}
								if (!"".equals(adresse.getLIGNE_3())) {
									adresse_assure = adresse_assure + adresse.getLIGNE_3() + "&nbsp;&nbsp;";
								}
								if (!"".equals(adresse.getLIGNE_4())) {
									adresse_assure = adresse_assure + adresse.getLIGNE_4() + "&nbsp;&nbsp;";
								}
								if (!"".equals(adresse.getCODEPOSTAL())) {
									adresse_assure = adresse_assure + adresse.getCODEPOSTAL() + "&nbsp;&nbsp;";
								}
								if (!"".equals(adresse.getLOCALITE())) {
									adresse_assure = adresse_assure + adresse.getLOCALITE();
								}
								telephones_assure = adresse.getTELEPHONEFIXE() + ((!"".equals(adresse.getTELEPHONEAUTRE())) ? " " + adresse.getTELEPHONEAUTRE() : "");
							}
						}
					}
					// si le type appelant est ASSURE il faut renseigner systématiquement la partie "bénéficiaire appelant"
					// 1- avec le bénef appelant si différent du bénef
					// 2- avec le bénef lui-même sinon
					// dans le 2nd cas on remet à blanc la partie bénéficiaire puisqu'identique à la partie appelant					
					if (benef_appelant != null) {
						numadh_benef_appelant = benef_appelant.getCODE();
						qualite_benef_appelant = benef_appelant.getQualite();
						Personne personne_assure = SQLDataService.getPersonneById(benef_appelant.getPERSONNE_ID());
						if (personne_assure != null) {
							nom_benef_appelant = personne_assure.getPrenomNom();
							Adresse adresse = SQLDataService.getAdresseById(personne_assure.getADRESSE_ID());
							if (adresse != null) {
								if (!"".equals(adresse.getLIGNE_1())) {
									adresse_benef_appelant = adresse.getLIGNE_1() + "&nbsp;&nbsp;";
								}
								if (!"".equals(adresse.getLIGNE_2())) {
									adresse_benef_appelant = adresse_benef_appelant + adresse.getLIGNE_2() + "&nbsp;&nbsp;";
								}
								if (!"".equals(adresse.getLIGNE_3())) {
									adresse_benef_appelant = adresse_benef_appelant + adresse.getLIGNE_3() + "&nbsp;&nbsp;";
								}
								if (!"".equals(adresse.getLIGNE_4())) {
									adresse_benef_appelant = adresse_benef_appelant + adresse.getLIGNE_4() + "&nbsp;&nbsp;";
								}
								if (!"".equals(adresse.getCODEPOSTAL())) {
									adresse_benef_appelant = adresse_benef_appelant + adresse.getCODEPOSTAL() + "&nbsp;&nbsp;";
								}
								if (!"".equals(adresse.getLOCALITE())) {
									adresse_benef_appelant = adresse_benef_appelant + adresse.getLOCALITE();
								}
								telephone_benef_appelant = adresse.getTELEPHONEFIXE() + ((!"".equals(adresse.getTELEPHONEAUTRE())) ? " " + adresse.getTELEPHONEAUTRE() : "");
							}
						}
						if (StringUtils.isNotBlank(adresse_assure) && adresse_assure.equals(adresse_benef_appelant)) {
							adresse_assure = "identique";
						}
						if (StringUtils.isNotBlank(telephones_assure) && telephones_assure.equals(telephone_benef_appelant)) {
							telephones_assure = "identique";
						}
					} else if ("ASSURE".equalsIgnoreCase(alias_type_appelant)) {
						nom_benef_appelant = prenom_nom_assure;
						numadh_benef_appelant = numero_adherent_assure;
						qualite_benef_appelant = qualite_assure;
						adresse_benef_appelant = adresse_assure;
						telephone_benef_appelant = telephones_assure;
						numero_adherent_assure = "";
					}
					
				}

				// Infos entreprise
				if (alias_type_appelant.equalsIgnoreCase("ENTREPRISE")) {
					Etablissement etablissement = SQLDataService.getEtablissementById(appel.getETABLISSEMENT_ID());
					if (etablissement != null) {
						libelle_entreprise = etablissement.getLIBELLE();
						entite_gestion_entreprise = etablissement.getEntiteGestion();
						numero_siret_entreprise = etablissement.getSIRET();

						Adresse adresse = SQLDataService.getAdresseById(etablissement.getADRESSE_ID());
						if (adresse != null) {
							telephones_entreprise = adresse.getTELEPHONEFIXE() + ((!"".equals(adresse.getTELEPHONEAUTRE())) ? " " + adresse.getTELEPHONEAUTRE() : "");
						}
					}
				}

				// Infos appelant
				else {
					Appelant appelant = SQLDataService.getAppelantById(appel.getAPPELANT_ID());
					if (appelant != null) {
						prenom_nom_appelant = ((!appelant.getPRENOM().equals("")) ? appelant.getPRENOM() + " " : "") + ((!appelant.getNOM().equals("")) ? appelant.getNOM() + " " : "");
						etablissement_rs_appelant = (!appelant.getETABLISSEMENT_RS().equals("")) ? appelant.getETABLISSEMENT_RS() + " " : "";
						telephones_appelant = appelant.getADR_TELEPHONEAUTRE() + ((!"".equals(appelant.getADR_TELEPHONEFIXE())) ? " " + appelant.getADR_TELEPHONEFIXE() : "");
						code_adherent_appelant = appelant.getCODEADHERENT();
						numero_finess_appelant = appelant.getNUMFINESS();
					}
				}

				// Scénario
				motif = appel.getMotif();
				sous_motif = appel.getSousMotif();
				point = appel.getPoint();
				sous_point = appel.getSousPoint();

				// Clôture
				commentaires = appel.getCOMMENTAIRE();
				satisfaction = appel.getSatisfaction();
				alias_satisfaction = appel.getAliasSatisfaction();
				if (alias_satisfaction.equals("INSATISFAIT")) {
					img_satisfaction = "<img src='" + contextPath + "/img/s_insatisfait_2.gif' title='" + satisfaction + "' align='bottom'/>";
				} else if (alias_satisfaction.equals("SATISFAIT")) {
					img_satisfaction = "<img src='" + contextPath + "/img/s_satisfait_2.gif' title='" + satisfaction + "' align='bottom'/>";
				} else if (alias_satisfaction.equals("NEUTRE")) {
					img_satisfaction = "<img src='" + contextPath + "/img/s_neutre_2.gif' title='" + satisfaction + "' align='bottom'/>";
				} else if (alias_satisfaction.equals("DANGER")) {
					img_satisfaction = "<img src='" + contextPath + "/img/s_danger_2.gif' title='" + satisfaction + "' align='bottom'/>";
				}

				urgent = ("1".equals(appel.getTRAITEMENTURGENT())) ? "<label class='bordeau11'>Oui</label>" : "<label class='noir11'>Non</label>";
				reclamation = ("1".equals(appel.getRECLAMATION())) ? "<label class='bordeau11'>Oui</label>" : "<label class='noir11'>Non</label>";
				a_rappeler = (appel.getDATERAPPEL() != null)
						? "<label class='bordeau11'>Oui</label><label class='noir11'> Le " + UtilDate.formatDDMMYYYY(appel.getDATERAPPEL()) + " " + appel.getPeriodeRappel() + "</label>"
						: "<label class='noir11'>Non</label>";

				resolu = ("1".equals(appel.getResolu())) ? "Oui" : "Non";

				transferts = appel.getTRANSFERTS();

				// Document généré
				String nom_document_genere = appel.getNOMDOCUMENTGENERE();
				String id_modele_edition = appel.getMODELE_EDITION_ID();
				if (!"".equals(nom_document_genere) && !"".equals(id_modele_edition)) {
					ModeleEdition modele_edition = SQLDataService.getModeleEditionById(id_modele_edition);
					if (modele_edition != null) {
						String repertoire = modele_edition.getREPERTOIRE();
						String LECTEUR_PARTAGE = (String) request.getSession().getAttribute("LECTEUR_PARTAGE");
						String url_document_genere = LECTEUR_PARTAGE + ":\\" + repertoire + "\\" + nom_document_genere;
						lien_document_genere = "<a target=\"_blank\" href=\"" + url_document_genere + "\"><img src=\"" + contextPath + "/img/FICHIER_BLEU.gif\" border=\"0\"/></a>";
					}
				}

				documents_associes = SQLDataService.getEvenementsAssocies(idAppel);

			}
		}
		
		request.setAttribute("teleacteur_id", teleacteur_id);
		request.setAttribute("idAppel", idAppel);
		request.setAttribute("modeOuverture", modeOuverture);
		request.setAttribute("appz", appz);		
		request.setAttribute("message_blocage", message_blocage);
		request.setAttribute("img_blocage", img_blocage);
		request.setAttribute("appel", appel);
		request.setAttribute("documents_associes", documents_associes);
		request.setAttribute("b_edition", b_edition);
		request.setAttribute("statut", statut);
		request.setAttribute("alias_statut", alias_statut);
		request.setAttribute("libelle_sous_statut", libelle_sous_statut);
		request.setAttribute("date_creation", date_creation);
		request.setAttribute("date_modification", date_modification);
		request.setAttribute("date_cloture", date_cloture);
		request.setAttribute("auteur", auteur);
		request.setAttribute("modificateur", modificateur);
		request.setAttribute("clotureur", clotureur);
		request.setAttribute("transferts", transferts);
		request.setAttribute("type_appelant", type_appelant);
		request.setAttribute("alias_type_appelant", alias_type_appelant);
		
		request.setAttribute("prenom_nom_assure", prenom_nom_assure);
		request.setAttribute("numero_adherent_assure", numero_adherent_assure);
		request.setAttribute("qualite_assure", qualite_assure);
		request.setAttribute("adresse_assure", adresse_assure);
		request.setAttribute("telephones_assure", telephones_assure);
		
		request.setAttribute("nom_benef_appelant", nom_benef_appelant);
		request.setAttribute("numadh_benef_appelant", numadh_benef_appelant);
		request.setAttribute("qualite_benef_appelant", qualite_benef_appelant);
		request.setAttribute("adresse_benef_appelant", adresse_benef_appelant);
		request.setAttribute("telephone_benef_appelant", telephone_benef_appelant);
		
		request.setAttribute("libelle_entreprise", libelle_entreprise);
		request.setAttribute("entite_gestion_entreprise", entite_gestion_entreprise);
		request.setAttribute("numero_siret_entreprise", numero_siret_entreprise);
		request.setAttribute("telephones_entreprise", telephones_entreprise);
		request.setAttribute("prenom_nom_appelant", prenom_nom_appelant);
		request.setAttribute("etablissement_rs_appelant", etablissement_rs_appelant);
		request.setAttribute("code_adherent_appelant", code_adherent_appelant);
		request.setAttribute("numero_finess_appelant", numero_finess_appelant);
		request.setAttribute("telephones_appelant", telephones_appelant);
		request.setAttribute("campagne", campagne);
		request.setAttribute("mutuelle", mutuelle);
		request.setAttribute("motif", motif);
		request.setAttribute("sous_motif", sous_motif);
		request.setAttribute("point", point);
		request.setAttribute("sous_point", sous_point);
		request.setAttribute("commentaires", commentaires);
		boolean readonly = ! (b_edition && !alias_statut.equals("OUVERTE") && !alias_statut.equals("CLOTURE") 
				&& !alias_statut.equals("HORSCIBLE") && !alias_statut.equals("APPELSORTANT"));
		request.setAttribute("readonly", readonly);
		request.setAttribute("satisfaction", satisfaction);
		request.setAttribute("alias_satisfaction", alias_satisfaction);
		request.setAttribute("img_satisfaction", img_satisfaction);
		request.setAttribute("urgent", urgent);
		request.setAttribute("reclamation", reclamation);
		request.setAttribute("a_rappeler", a_rappeler);
		request.setAttribute("lien_document_genere", lien_document_genere);
		request.setAttribute("resolu", resolu);
		
		request.getRequestDispatcher("/popups/ouvrir_fiche_appel_new.jsp").forward(request, response);
		
	}
    
    public void doRecherche(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
    	
    	Map<String, String> parameters = new HashMap<String, String>();

		TeleActeur teleActeur = (TeleActeur) request.getSession().getAttribute(IContacts._var_session_teleActeur);
		parameters.put("teleacteur_id", teleActeur.getId());
		
		@SuppressWarnings("unchecked")
		Map<String, String[]> parameterMap = request.getParameterMap();

		for (String param : parameterMap.keySet()) {
			String value = StringUtils.join(parameterMap.get(param), ",");
			if (StringUtils.isNotBlank(value)) {
				parameters.put(param, value);
			}			
		}		

		request.getSession().setAttribute("sens_tri_fiches_appels", "DESC");
		Collection<?> fiches_appels_recherchees = SQLDataService.rechercheFichesAppels(parameters);
		request.getSession().setAttribute("fiches_appels_recherchees", fiches_appels_recherchees);


		StringBuilder tableau_formate = new StringBuilder("");

		String label = "";
		int nbr = 0;
		if (fiches_appels_recherchees != null && !fiches_appels_recherchees.isEmpty()) {
			nbr = fiches_appels_recherchees.size();
		}
		if (nbr == 0) {
			label = "<br><br><br><label class='noir12'>Aucune fiche d'appel trouv&eacute;e</label>";
		} else if (nbr == 1) {
			Object premier_objet = fiches_appels_recherchees.toArray()[0];
			if (premier_objet instanceof Limite) {
				int taille = ((Limite) premier_objet).getTaille();
				label = "<br><br><br><label class='noir12'>Votre recherche ram&egrave;ne trop de r&eacute;sultats : </labe><label class='bordeau12'>" + taille
						+ "</label><br><br><br><label class='noir12'>Veuillez affiner votre recherche svp.</label>";
			} else {
				tableau_formate = CrmUtil.getTableauFichesAppelsRecherchees(fiches_appels_recherchees);
				label = "<label class='noir12'>1</label> <label class='bleu12'>fiche d'appel trouv&eacute;e</label>";
			}
		} else {
			tableau_formate = CrmUtil.getTableauFichesAppelsRecherchees(fiches_appels_recherchees);
			label = "<label class='noir12'>" + nbr + "</label> " + "<label class='bleu12'>fiches d'appel trouv&eacute;es</label>";
		}

		request.setAttribute("label", label);
		request.setAttribute("tableau_formate", tableau_formate);
		request.getRequestDispatcher("/popups/recherche_fiches_appels_resultats.jsp").forward(request, response);
	}
    
    
    
    
    public void getStatistiquesExcel(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, Exception {
    	
    	String action = (String) request.getParameter("action");
    	if (action != null && "afficherCritere".equals(action)) {
    		getDetailCritere(request, response);
    		return;
    	} else if (action != null && "supprimerCritere".equals(action)) {
    		supprimerCritere(request, response);
    		return;
    	}
    	
        _idRapport = (String) request.getParameter("idRapport");
        
        if (_idRapport == null) {
        	_idRapport = String.valueOf(new Date().getTime()) + (String) request.getSession().getId();
        }
        
        String sr = "";

        _MUTUELLE_ID = (String) request.getParameter("mutuelle_id");
        _MUTUELLE_TEXT = (String) request.getParameter("mutuelle_text");
        
        CRITERES_ENREGISTRES = request.getParameterValues("criteres_enregistres");
        
        CAMPAGNE_ID = request.getParameterValues("campagne_id");
        
        REGROUPEMENT_CAMPAGNES = (String) request.getParameter("regroupement_campagnes");
        REGROUPEMENT_AUTEURS = (String) request.getParameter("regroupement_auteurs");

        _TELEACTEUR_ID = (String) request.getParameter("teleacteur_id");

        _SITE_ID = (String) request.getParameter("site_id");
        _SITE_TEXT = (String) request.getParameter("site_text");

        _REFERENCE_ID = (String) request.getParameter("reference_id");
        _REFERENCE_TEXT = (String) request.getParameter("reference_text");

        _CREATEUR_ID = request.getParameterValues("createur_id");
        _CREATEUR_TEXT = (String) request.getParameter("createur_text");

        _STATUT_ID = (String) request.getParameter("statut_id");
        _STATUT_TEXT = (String) request.getParameter("statut_text");

        _DATEDEBUT = (String) request.getParameter("date_debut");
        _DATEFIN = (String) request.getParameter("date_fin");

        _RESOLU = (String) request.getParameter("resolu");
        
        Workbook modele_wb = null;
        WritableWorkbook wb = null;

        _nomFichier = "Statistiques_HContacts"
                + "_"
                + _DATEDEBUT.replaceAll("/", "")
                + ((!"".equals(_DATEFIN)) ? "_" + _DATEFIN.replaceAll("/", "")
                        : "");
        sr = "Résumé de la demande";
        request.getSession().setAttribute(_idRapport, sr);

        try {
        	OutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + _nomFichier + ".xls");

            initVariablesStatic();

            wb = Workbook.createWorkbook(out);

            this.Traitement(wb, request);

            wb.write();
            wb.close();

            request.getSession().setAttribute(_idRapport, "FINI");

        } catch (Exception e) {

            LOGGER.warn("doGet",e);
            if (e.getClass().getName().indexOf("ClientAbortException") != -1) {
                request.getSession().setAttribute(_idRapport,
                        "ANNULATION_CLIENT");
            } else {
                request.getSession().setAttribute(_idRapport, "ERREUR");
            }
            throw new ServletException("", e);
        } finally {

            if (modele_wb != null) {
                modele_wb.close();
            }
            try {
                if (wb != null) {
                    wb.close();
                }
            } catch (Exception e) {
                LOGGER.warn("doGet",e);
            }

        }

    }

    private void supprimerCritere(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String idCritere = (String) request.getParameter("idCritere");
		if (idCritere != null) {
			BackOfficeService.supprimerRegroupement(idCritere);
		}
		
	}

	private void getDetailCritere(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String idCritere = (String) request.getParameter("idCritere");
		if (idCritere != null) {
			List<Object> detailCritere = BackOfficeService.getDetailRegroupement(idCritere);
			HashMap<String, String> result = new HashMap<String, String>();
			
			if (!detailCritere.isEmpty()) {
				for (Object detail: detailCritere) {
					if (detail instanceof Campagne) {
						String id = ((Campagne) detail).getId();
						String libelle = ((Campagne) detail).getLibelle();
						result.put(id, libelle);
					} else if (detail instanceof TeleActeur) {
						String id = ((TeleActeur) detail).getId();
						String libelle = ((TeleActeur) detail).getNomPrenom();
						result.put(id, libelle);
					}
				}
				JSONObject jsonObject = new JSONObject(result);
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObject.toString());
				response.getWriter().flush();
			}
		}
		
	}

	public void Traitement(WritableWorkbook wb, HttpServletRequest request)
            throws Exception {

        Map<String, String> criteres = new HashMap<String, String>();

        criteres.put("TELEACTEUR_ID", _TELEACTEUR_ID);
        criteres.put("MUTUELLE_ID", _MUTUELLE_ID);
        criteres.put("SITE_ID", _SITE_ID);
        criteres.put("REFERENCE_ID", _REFERENCE_ID);
        criteres.put("STATUT_ID", _STATUT_ID);
        criteres.put("DATEDEBUT", _DATEDEBUT);
        criteres.put("DATEFIN", _DATEFIN);
        criteres.put("RESOLU", _RESOLU);
        
        if (CAMPAGNE_ID != null && StringUtils.isNotBlank(REGROUPEMENT_CAMPAGNES)) {
			BackOfficeService.regrouperCampagnesPourStats(REGROUPEMENT_CAMPAGNES, CAMPAGNE_ID);
		}
        
        if (_CREATEUR_ID != null && StringUtils.isNotBlank(REGROUPEMENT_AUTEURS)) {
    			BackOfficeService.regrouperAuteursPourStats(REGROUPEMENT_AUTEURS, _CREATEUR_ID);
        }
        
        if (CRITERES_ENREGISTRES != null) {
        	for (String groupeCriteresId : CRITERES_ENREGISTRES) {
        		Regroupement<String> groupeCriteres = BackOfficeService.getRegroupement(groupeCriteresId);
				if (groupeCriteres != null) {
					List<String> tmpListe = new ArrayList<String>();
					if (Regroupement.type_campagnes_stats.equals(groupeCriteres.getType())) {
						if (CAMPAGNE_ID != null) {
							tmpListe.addAll(Arrays.asList(CAMPAGNE_ID));
						}
						CAMPAGNE_ID = tmpListe.toArray(CAMPAGNE_ID);
					} else if (Regroupement.type_auteurs_stats.equals(groupeCriteres.getType())) {
						if (_CREATEUR_ID != null) {
							tmpListe.addAll(Arrays.asList(_CREATEUR_ID));
						}
						_CREATEUR_ID = tmpListe.toArray(_CREATEUR_ID);
					}
				}
        	}
        }
        
        String critereCampagne = "";
        if (CAMPAGNE_ID != null) {
        	for (String campagneId : CAMPAGNE_ID) {
				if (StringUtils.isNotBlank(campagneId)) {
					critereCampagne = critereCampagne.concat(campagneId).concat(", ");
				}
        	}
        	if (critereCampagne.endsWith(", ")) {
        		critereCampagne = critereCampagne.substring(0, critereCampagne.length()-2);
			}
        }
        criteres.put("CAMPAGNE_ID", critereCampagne);
        
        String critereAuteur = "";
        if (_CREATEUR_ID != null) {
			for (String auteurId : _CREATEUR_ID) {
				if (StringUtils.isNotBlank(auteurId)) {
					critereAuteur = critereAuteur.concat(auteurId).concat(", ");
				}
			}
			if (critereAuteur.endsWith(", ")) {
				critereAuteur = critereAuteur.substring(0, critereAuteur.length()-2);
			}
        }
        criteres.put("CREATEUR_ID", critereAuteur);

        request.getSession().setAttribute(_idRapport, "Exécution de la requête");

        Collection<LigneExcel> donnees = SQLDataService.getStatistiquesFichesAppels(criteres);

        request.getSession().setAttribute(_idRapport, "Ecriture des données");

        int nbr_onglets = 0;
        if (donnees != null && !donnees.isEmpty()) {
            nbr_onglets = (int) Math.ceil((float) donnees.size() / (float) limite_lignes);
        }

        for (int i = 0; i < nbr_onglets; i++) {
            
            WritableSheet sheet = wb.createSheet("Feuil" + i, i);
            // Résumé de la feuille
            resumeFeuille(sheet);

            ECRIRE_COLONNES(sheet);
            ECRIRE_DONNEES(sheet, donnees, (int) i * limite_lignes,
                    (int) (i + 1) * limite_lignes);

            // Taille des colonnes
            int col = 0;
            sheet.setColumnView(col++, 12);
            sheet.setColumnView(col++, 22);
            sheet.setColumnView(col++, 22);
            sheet.setColumnView(col++, 22);
            sheet.setColumnView(col++, 15);
            sheet.setColumnView(col++, 30);
            sheet.setColumnView(col++, 10);
            sheet.setColumnView(col++, 12);
            
            sheet.setColumnView(col++, 30);
            sheet.setColumnView(col++, 10);

            sheet.setColumnView(col++, 16);
            sheet.setColumnView(col++, 16);

            sheet.setColumnView(col++, 25);
            sheet.setColumnView(col++, 25);

            sheet.setColumnView(col++, 12);
            sheet.setColumnView(col++, 12);

            sheet.setColumnView(col++, 7);
            sheet.setColumnView(col++, 40);
            sheet.setColumnView(col++, 40);
            sheet.setColumnView(col++, 40);
            sheet.setColumnView(col++, 40);
        }

        if (nbr_onglets == 0) {
            WritableSheet sheet = wb.createSheet("Feuil" + 0, 0);
            // Résumé de la feuille
            resumeFeuille(sheet);
            ECRIRE_COLONNES(sheet);
        }

    }

    public void resumeFeuille(WritableSheet sheet) throws Exception {
        
        sheet.addCell(new Label(0, 0, "Date début", _wcf_font_arial_8_bold_noir));
        sheet.addCell(new Label(1, 0, _DATEDEBUT, _wcf_font_arial_8_noir));

        sheet.addCell(new Label(0, 1, "Date fin", _wcf_font_arial_8_bold_noir));
        sheet.addCell(new Label(1, 1, _DATEFIN, _wcf_font_arial_8_noir));

        sheet.addCell(new Label(0, 2, "Site", _wcf_font_arial_8_bold_noir));
        sheet.addCell(new Label(1, 2, _SITE_TEXT, _wcf_font_arial_8_noir));

        sheet.addCell(new Label(0, 3, "Client", _wcf_font_arial_8_bold_noir));
        sheet.addCell(new Label(1, 3, _MUTUELLE_TEXT, _wcf_font_arial_8_noir));

        sheet.addCell(new Label(0, 4, "Auteur", _wcf_font_arial_8_bold_noir));
        sheet.addCell(new Label(1, 4, _CREATEUR_TEXT, _wcf_font_arial_8_noir));

        sheet.addCell(new Label(0, 5, "Type de fiche",
                _wcf_font_arial_8_bold_noir));
        sheet.addCell(new Label(1, 5, _REFERENCE_TEXT, _wcf_font_arial_8_noir));

        sheet.addCell(new Label(0, 6, "Résolu", _wcf_font_arial_8_bold_noir));
        if( "1".equalsIgnoreCase(_RESOLU)){
            sheet.addCell(new Label(1, 6, "Oui", _wcf_font_arial_8_noir));
        }
        else if("0".equalsIgnoreCase(_RESOLU) ){
            sheet.addCell(new Label(1, 6, "Non", _wcf_font_arial_8_noir));
        }
        else{
            sheet.addCell(new Label(1, 6, "- Tous -", _wcf_font_arial_8_noir));
        }
        
        sheet.addCell(new Label(0, 7, "Statut", _wcf_font_arial_8_bold_noir));
        sheet.addCell(new Label(1, 7, _STATUT_TEXT, _wcf_font_arial_8_noir));

    }

	public void ECRIRE_DONNEES(WritableSheet sheet, Collection<LigneExcel> donnees, int borne_inf, int borne_sup) {

		Label l = null;
		int ligne_i = 0;

		Object[] tab_donnees = null;

		try {
			if (donnees != null && !donnees.isEmpty()) {
				LigneExcel ligne_donnees = null;
				tab_donnees = donnees.toArray();

				for (int i = borne_inf; i < borne_sup; i++) {

					if (i < donnees.size()) {
						ligne_donnees = (LigneExcel) tab_donnees[i];
						int col = 0;

						// SITE
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getSITE(), _wcf_datas_left);
						sheet.addCell(l);

						// CAMPAGNE
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getCampagne(), _wcf_datas_left);
						sheet.addCell(l);

						// CLIENT
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getCLIENT(), _wcf_datas_left);
						sheet.addCell(l);

						// TYPE DE FICHE
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getTYPEFICHE(), _wcf_datas_left);
						sheet.addCell(l);

						// TYPE APPELANT
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getTYPEAPPELANT(), _wcf_datas_left);
						sheet.addCell(l);

						// NOM BENEF
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getNomben(), _wcf_datas_left);
						sheet.addCell(l);

						// CODE BENEF
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getCodeben(), _wcf_datas_left);
						sheet.addCell(l);

						// QUALITE
						l = new Label(col++, 10 + ligne_i, ligne_donnees.getQualite(), _wcf_datas_left);
						sheet.addCell(l);

						// NOM APPELANT
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getNOMAPPELANT(), _wcf_datas_left);
						sheet.addCell(l);

						// CODE APPELANT
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getCODE_APPELANT(), _wcf_datas_left);
						sheet.addCell(l);

						// DATE APPEL
						l = new Label(col++, 10 + ligne_i, (String) UtilDate.fmtDDMMYYYYHHMMSS(ligne_donnees.getDATE_APPEL()), _wcf_datas_left);
						sheet.addCell(l);

						// DATE CLOTURE
						l = new Label(col++, 10 + ligne_i, (String) UtilDate.fmtDDMMYYYYHHMMSS(ligne_donnees.getDATE_CLOTURE()), _wcf_datas_left);
						sheet.addCell(l);

						// CREATEUR
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getCREATEUR(), _wcf_datas_left);
						sheet.addCell(l);

						// CLOTUREUR
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getCLOTUREUR(), _wcf_datas_left);
						sheet.addCell(l);

						// STATUT
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getSTATUT(), _wcf_datas_left);
						sheet.addCell(l);

						// IDFICHE
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getIDFICHE(), _wcf_datas_left);
						sheet.addCell(l);

						// RESOLU
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getRESOLU(), _wcf_datas_left);
						sheet.addCell(l);

						// COMMENTAIRE
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getCOMMENTAIRE(), _wcf_datas_left);
						sheet.addCell(l);
						// MOTIF
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getMotif(), _wcf_datas_left);
						sheet.addCell(l);
						// SOUS MOTIF
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getSous_motif(), _wcf_datas_left);
						sheet.addCell(l);
						// ENTITE GESTION
						l = new Label(col++, 10 + ligne_i, (String) ligne_donnees.getLib_entite_gestion(), _wcf_datas_left);
						sheet.addCell(l);
					}

					ligne_i++;
				}
			}
		} catch (Exception e) {
			LOGGER.warn("ECRIRE_DONNEES", e);
			throw new IContactsException(e);
		}
	}

    public void ECRIRE_COLONNES(WritableSheet sheet) {
        try {
            for (int i = 0; i < _nbr_tab_colonnes; i++) {
                sheet.addCell(new Label(i, 9, _tab_colonnes[i],
                        _wcf_font_arial_8_noir_bold_fond_gris));
            }
        } catch (Exception e) {
            LOGGER.warn("ECRIRE_COLONNES",e);
        }
    }

    public void initVariablesStatic() {

        try {
            _font_arial_8_bold_noir = new WritableFont(WritableFont.ARIAL, 8,
                    WritableFont.BOLD);
            _font_arial_8_bold_noir.setColour(Colour.BLACK);
            _wcf_font_arial_8_bold_noir = new WritableCellFormat(
                    _font_arial_8_bold_noir);

            _font_arial_8_noir = new WritableFont(WritableFont.ARIAL, 8);
            _font_arial_8_noir.setColour(Colour.BLACK);
            _wcf_font_arial_8_noir = new WritableCellFormat(_font_arial_8_noir);

            _font_arial_8_noir_bold_fond_bleu = new WritableFont(
                    WritableFont.ARIAL, 8, WritableFont.BOLD);
            _font_arial_8_noir_bold_fond_bleu.setColour(Colour.BLACK);
            _wcf_font_arial_8_noir_bold_fond_bleu = new WritableCellFormat(
                    _font_arial_8_noir_bold_fond_bleu);
            _wcf_font_arial_8_noir_bold_fond_bleu
                    .setBackground(Colour.PALE_BLUE);
            _wcf_font_arial_8_noir_bold_fond_bleu
                    .setAlignment(Alignment.CENTRE);
            _wcf_font_arial_8_noir_bold_fond_bleu
                    .setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_font_arial_8_noir_bold_fond_bleu.setWrap(true);
            _wcf_font_arial_8_noir_bold_fond_bleu.setBorder(Border.ALL,
                    BorderLineStyle.THIN, Colour.BLACK);

            _font_arial_8_noir_fond_bleu_left = new WritableFont(
                    WritableFont.ARIAL, 8);
            _font_arial_8_noir_fond_bleu_left.setColour(Colour.BLACK);
            _wcf_font_arial_8_noir_fond_bleu_left = new WritableCellFormat(
                    _font_arial_8_noir_fond_bleu_left);
            _wcf_font_arial_8_noir_fond_bleu_left
                    .setBackground(Colour.PALE_BLUE);
            _wcf_font_arial_8_noir_fond_bleu_left.setAlignment(Alignment.LEFT);
            _wcf_font_arial_8_noir_fond_bleu_left
                    .setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_font_arial_8_noir_fond_bleu_left.setBorder(Border.ALL,
                    BorderLineStyle.THIN, Colour.BLACK);

            _font_arial_8_noir_fond_bleu_right = new WritableFont(
                    WritableFont.ARIAL, 8);
            _font_arial_8_noir_fond_bleu_right.setItalic(true);
            _font_arial_8_noir_fond_bleu_right.setColour(Colour.BLACK);
            _wcf_font_arial_8_noir_fond_bleu_right = new WritableCellFormat(
                    _font_arial_8_noir_fond_bleu_right);
            _wcf_font_arial_8_noir_fond_bleu_right
                    .setBackground(Colour.PALE_BLUE);
            _wcf_font_arial_8_noir_fond_bleu_right
                    .setAlignment(Alignment.RIGHT);
            _wcf_font_arial_8_noir_fond_bleu_right
                    .setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_font_arial_8_noir_fond_bleu_right.setBorder(Border.ALL,
                    BorderLineStyle.THIN, Colour.BLACK);

            _font_arial_8_noir_bold_fond_violet = new WritableFont(
                    WritableFont.ARIAL, 8, WritableFont.BOLD);
            _font_arial_8_noir_bold_fond_violet.setColour(Colour.BLACK);
            _wcf_font_arial_8_noir_bold_fond_violet = new WritableCellFormat(
                    _font_arial_8_noir_bold_fond_violet);
            _wcf_font_arial_8_noir_bold_fond_violet.setBackground(Colour
                    .getInternalColour(31));
            _wcf_font_arial_8_noir_bold_fond_violet
                    .setAlignment(Alignment.CENTRE);
            _wcf_font_arial_8_noir_bold_fond_violet
                    .setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_font_arial_8_noir_bold_fond_violet.setBorder(Border.ALL,
                    BorderLineStyle.THIN, Colour.BLACK);

            _font_bordure = new WritableFont(WritableFont.ARIAL, 8,
                    WritableFont.BOLD);
            _font_bordure.setColour(Colour.BLUE);
            _wcf_font_bordure = new WritableCellFormat(_font_bordure);
            _wcf_font_bordure.setAlignment(Alignment.CENTRE);
            _wcf_font_bordure.setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_font_bordure.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);

            _wf_datas = new WritableFont(WritableFont.ARIAL, 8);
            _wf_datas.setColour(Colour.BLACK);

            // Centrée
            _wcf_datas = new WritableCellFormat(_wf_datas,
                    NumberFormats.DEFAULT);
            _wcf_datas.setAlignment(Alignment.CENTRE);
            _wcf_datas.setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_datas
                    .setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
            // A gauche
            _wcf_datas_left = new WritableCellFormat(_wf_datas,
                    NumberFormats.DEFAULT);
            _wcf_datas_left.setAlignment(Alignment.LEFT);
            _wcf_datas_left.setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_datas_left.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);

            _wcf_font_sans_bordure = new WritableCellFormat(_wf_datas);
            _wcf_font_sans_bordure.setAlignment(Alignment.CENTRE);
            _wcf_font_sans_bordure
                    .setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_font_sans_bordure.setBorder(Border.NONE, BorderLineStyle.THIN,
                    Colour.BLACK);

            _wf_datas_bold = new WritableFont(WritableFont.ARIAL, 8,
                    WritableFont.BOLD);
            _wf_datas_bold.setColour(Colour.BLACK);
            _wcf_datas_bold = new WritableCellFormat(_wf_datas_bold,
                    NumberFormats.DEFAULT);
            _wcf_datas_bold.setAlignment(Alignment.CENTRE);
            _wcf_datas_bold.setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_datas_bold.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);

            _wf_datas_pct = new WritableFont(WritableFont.ARIAL, 8);
            _wf_datas_pct.setColour(Colour.BLUE);
            _wcf_datas_pct = new WritableCellFormat(_wf_datas_pct);
            _wcf_datas_pct.setAlignment(Alignment.CENTRE);
            _wcf_datas_pct.setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_datas_pct.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);

            _wf_datas_flotant = new WritableFont(WritableFont.ARIAL, 8);
            _wf_datas_flotant.setColour(Colour.BLACK);
            _wcf_datas_flotant = new WritableCellFormat(_wf_datas_flotant,
                    NumberFormats.FLOAT);
            _wcf_datas_flotant.setAlignment(Alignment.CENTRE);
            _wcf_datas_flotant.setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_datas_flotant.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);

            _wf_datas_flotant_bold = new WritableFont(WritableFont.ARIAL, 8,
                    WritableFont.BOLD);
            _wf_datas_flotant_bold.setColour(Colour.BLACK);
            _wcf_datas_flotant_bold = new WritableCellFormat(
                    _wf_datas_flotant_bold, NumberFormats.FLOAT);
            _wcf_datas_flotant_bold.setAlignment(Alignment.CENTRE);
            _wcf_datas_flotant_bold
                    .setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_datas_flotant_bold.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);

            _font_arial_8_noir_bold_fond_gris = new WritableFont(
                    WritableFont.ARIAL, 8, WritableFont.BOLD);
            _wcf_font_arial_8_noir_bold_fond_gris = new WritableCellFormat(
                    _font_arial_8_noir_bold_fond_gris);
            _wcf_font_arial_8_noir_bold_fond_gris.setBackground(Colour.GRAY_25);
            _wcf_font_arial_8_noir_bold_fond_gris
                    .setAlignment(Alignment.CENTRE);
            _wcf_font_arial_8_noir_bold_fond_gris
                    .setVerticalAlignment(VerticalAlignment.CENTRE);
            _wcf_font_arial_8_noir_bold_fond_gris.setBorder(Border.ALL,
                    BorderLineStyle.THIN, Colour.BLACK);

            _tab_colonnes = new String[] { "SITE", "CAMPAGNE", "CLIENT", "TYPE DE FICHE",
                    "TYPE D'APPELANT", "BENEFICIAIRE", "CODE BENEF", "QUALITE", "NOM APPELANT", "CODE APPELANT",
                    "DATE APPEL", "DATE CLOTURE", "CREATEUR", "CLOTUREUR",
                    "STATUT", "ID FICHE", "RESOLU", "COMMENTAIRE","MOTIF","SOUS MOTIF","ENTITE GESTION"};
            _nbr_tab_colonnes = _tab_colonnes.length;

        } catch (Exception e) {
            LOGGER.warn("initVariablesStatic",e);
        }

    }

}
