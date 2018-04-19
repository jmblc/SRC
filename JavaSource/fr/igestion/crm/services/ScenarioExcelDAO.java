package fr.igestion.crm.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import fr.igestion.common.bureautique.ExcelComposerPOI;
import fr.igestion.common.bureautique.ExcelComposerPOI.EnteteColonne;
import fr.igestion.common.bureautique.ExcelReaderPOI;
import fr.igestion.crm.bean.scenario.Campagne;
import fr.igestion.crm.bean.scenario.Motif;
import fr.igestion.crm.bean.scenario.Point;
import fr.igestion.crm.bean.scenario.Scenario;
import fr.igestion.crm.bean.scenario.SousMotif;
import fr.igestion.crm.bean.scenario.SousPoint;

public class ScenarioExcelDAO {
	
	private final static String ENTETE_MOTIF = "MOTIFS", 
								ENTETE_SOUSMOTIF = "SOUS MOTIFS", 
								ENTETE_POINT = "POINTS", 
								ENTETE_SOUSPOINT = "SOUS POINTS", 
								ENTETE_CONSIGNE = "CONSIGNES", 
								ENTETE_DISCOURS = "DISCOURS";
	private final static String[] ENTETES = 
		{ENTETE_MOTIF, ENTETE_SOUSMOTIF, ENTETE_POINT, ENTETE_SOUSPOINT, ENTETE_CONSIGNE, ENTETE_DISCOURS};
	private final static int[] TAILLES_COL = {20, 35, 35, 25, 45, 45};
	
	private Campagne campagne;
	private Scenario scenario;
	private File fichier;
	private OutputStream out;
	private InputStream input;
	private ExcelComposerPOI composer = null;
	private ExcelReaderPOI reader = null;
	private String errorMessage = null;
	private String infoMessage = null;
	
	public ScenarioExcelDAO(File pfichier) {
		fichier = pfichier;
	}
	
	public ScenarioExcelDAO(InputStream pin) {
		input = pin;
	}
	
	public ScenarioExcelDAO(Campagne pcampagne, Scenario pscenario, File pfichier) {
		campagne = pcampagne;
		scenario = pscenario;
		fichier = pfichier;
	}
	
	public ScenarioExcelDAO(Campagne pcampagne, Scenario pscenario, OutputStream pout) {
		campagne = pcampagne;
		scenario = pscenario;
		this.out =pout;
	}
	
	public ScenarioExcelDAO(Campagne pcampagne, Scenario pscenario, InputStream pin) {
		campagne = pcampagne;
		scenario = pscenario;
		this.input = pin;
	}
	
	private void checkComposer() throws IOException {
		
		if (composer == null) {

			if (fichier != null) {
				composer = new ExcelComposerPOI(fichier);
			} else if (out != null) {
				composer = new ExcelComposerPOI(out);
			}
		}
	}
	
	private void checkReader() throws Exception {
		
		if (reader == null) {
			if (fichier != null) {
				reader = new ExcelReaderPOI(fichier.getAbsolutePath());
			} else if (input != null) {
				reader = new ExcelReaderPOI(input);
			}
		}
	}
	
	public boolean feuilleSuivante() throws Exception {
		
		checkReader();
		return reader != null && reader.feuilleSuivante(); 
	}
	
	public boolean saveScenario() {
		
		boolean result = true;

		if (scenario != null && campagne != null) {
			
			
			try {
				checkComposer();
			} catch (IOException e) {				
				e.printStackTrace();
				errorMessage = "Impossible d'accéder au fichier Excel " + fichier;
				composer = null;
				result = false;
			}

			if (composer != null) {

				String libelleScenario = scenario.getLIBELLE();
				String libelleCampagne = campagne.getLibelle();
				String mutuelle = campagne.getMutuelles().iterator().next().getLibelle();
				String consignes = scenario.getCONSIGNES();
				String discours = scenario.getDISCOURS();

				String strMotif;
				String strSousMotif;
				String strPoint;
				String strSousPoint;
				
				composer.ecrireDonnee("SCENARIO", true);
				composer.colonneSuivante();
				composer.ecrireDonnee(libelleScenario);
				composer.ligneSuivante();
				composer.ecrireDonnee("CAMPAGNE", true);
				composer.colonneSuivante();
				composer.ecrireDonnee(libelleCampagne);
				composer.ligneSuivante();
				composer.ecrireDonnee("MUTUELLE", true);
				composer.colonneSuivante();
				composer.ecrireDonnee(mutuelle);
				composer.ligneSuivante();
				composer.ecrireDonnee("Consignes : ", true);
				composer.colonneSuivante();
				composer.ecrireDonnee(consignes);
				composer.ligneSuivante();
				composer.ecrireDonnee("Discours : ", true);
				composer.colonneSuivante();
				composer.ecrireDonnee(discours);
				composer.ligneSuivante();
				composer.ligneSuivante();				
			
				EnteteColonne[] entetes = composer.creerEntetes(ENTETES);
				composer.dimensionnerEntetes(entetes, TAILLES_COL);

				ArrayList<HashMap<String, String>> datas = new ArrayList<HashMap<String, String>>();

				for (Motif motif : scenario.getMotifs()) {

					boolean hasSousMotif = false;
					strMotif = motif.getLibelle();
					strSousMotif = "";
					strPoint = "";
					strSousPoint = "";
					consignes = motif.getCONSIGNES();
					discours = motif.getDISCOURS();

					for (SousMotif sousMotif : motif.getSousMotifs()) {

						hasSousMotif = true;
						boolean hasPoint = false;
						strSousMotif = sousMotif.getLibelle();
						strPoint = "";
						strSousPoint = "";
						consignes = sousMotif.getCONSIGNES();
						discours = sousMotif.getDISCOURS();

						for (Point point : sousMotif.getPoints()) {

							hasPoint = true;
							boolean hasSousPoint = false;
							strPoint = point.getLibelle();
							strSousPoint = "";
							consignes = point.getCONSIGNES();
							discours = point.getDISCOURS();

							for (SousPoint sousPoint : point.getSousPoints()) {

								hasSousPoint = true;
								strSousPoint = sousPoint.getLibelle();
								consignes = sousPoint.getCONSIGNES();
								discours = sousPoint.getDISCOURS();

								datas.add(
										getLigne(strMotif, strSousMotif, strPoint, strSousPoint, consignes, discours));

							}
							// ajout ligne correspondant au point si pas de sous point
							if (!hasSousPoint) {
								datas.add(
										getLigne(strMotif, strSousMotif, strPoint, strSousPoint, consignes, discours));
							}
						}
						// ajout ligne correspondant au sous motif si pas de point
						if (!hasPoint) {
							datas.add(getLigne(strMotif, strSousMotif, strPoint, strSousPoint, consignes, discours));
						}
					}
					// ajout ligne correspondant au motif si pas de sous motif
					if (!hasSousMotif) {
						datas.add(getLigne(strMotif, strSousMotif, strPoint, strSousPoint, consignes, discours));
					}
				}

				try {
					composer.ecrireDonnees(datas, entetes);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					result = false;
					errorMessage = "Impossible d'écrire les données du scénario dans le fichier \n" + e.getMessage();					
				}
				composer.terminer();
			}

		} else {
			result = false;
			errorMessage = "La campagne et le scénario ne sont pas correctement définis";		
		}
		return result;
	}

	private HashMap<String, String> getLigne(String... donnees) {
		
		HashMap<String, String> ligne = new HashMap<String, String>();
		int i = 0;
		for (String cle : ENTETES) {
			try {
				ligne.put(cle, donnees[i]);
				i++;
			} catch (Exception e) {
				break;
			}
		}
		return ligne;

	}
	
	public String loadLibelleCampagne() throws Exception {
		
		checkReader();
		
		if (reader != null) {
			
			if (reader.trouver("^[Cc](AMPAGNE|ampagne) *")) {
				reader.colonneSuivante();
				String libelle = (String) reader.lireCellule();
				return libelle;
			}
		}
		return null;
	}
	
	public String loadLibelleMutuelle() throws Exception {
		
		checkReader();
		
		if (reader != null) {
			if (reader.trouver("^[Mm](UTUELLE|utuelle) *")) {
				reader.colonneSuivante();
				String libelle = (String) reader.lireCellule();
				return libelle;
			}
		}
		return null;
	}
	
	
	public Scenario loadScenario() throws Exception {
		
		Scenario scenario = null;
		
		checkReader();
		
		if (reader != null) {
			
			int ligneEntete = 0;
			int numl = 0;
			reader.atteindreLigne(1);

			int colMotif = 0, colSousMotif = 0, colPoint = 0, colSousPoint = 0, colConsignes = 0, colDiscours = 0;
			
			// On identifie la ligne entete du fichier excel
			while (ligneEntete == 0) {

				colMotif = colSousMotif = colPoint = colSousPoint = colConsignes = colDiscours = 0;

				if (reader.trouverSuivant("^" + ENTETE_MOTIF + "( .)*")) {
					numl = reader.getNumligne();
					colMotif = reader.getNumcol();
				} else {
					break;
				}
				if (reader.trouverSuivant("^" + ENTETE_SOUSMOTIF + "( .)*")) {
					if (numl != reader.getNumligne()) {
						continue;
					} else {
						colSousMotif = reader.getNumcol();
					}
				} else {
					break;
				}
				if (reader.trouverSuivant("^" + ENTETE_POINT + "( .)*")) {
					if (numl != reader.getNumligne()) {
						continue;
					} else {
						colPoint = reader.getNumcol();
					}
				} else {
					break;
				}
				if (reader.trouverSuivant("^" + ENTETE_SOUSPOINT + "( .)*")) {
					if (numl != reader.getNumligne()) {
						continue;
					} else {
						colSousPoint = reader.getNumcol();
					}
				}				
				if (reader.trouverSuivant("^" + ENTETE_CONSIGNE + "( .)*")) {
					if (numl != reader.getNumligne()) {
						continue;
					} else {
						colConsignes = reader.getNumcol();
					}
				} else {
					break;
				}
				if (reader.trouverSuivant("^" + ENTETE_DISCOURS + "( .)*")) {
					if (numl != reader.getNumligne()) {
						continue;
					} else {
						colDiscours = reader.getNumcol();
					}
				}
				ligneEntete = numl;
			}
			
			// si ligne entete identifiée on traite chaque ligne détail en suivant
			if (ligneEntete > 0) {
				
				// on clone le scénario existant s'il y en a un et on réinitialise les motifs
				scenario = this.scenario != null ? this.scenario.clone() : new Scenario();
				scenario.setCAMPAGNE_ID(campagne != null ? campagne.getId() : null);
				
				scenario.setMotifs(new ArrayList<Motif>());
				
				if (reader.trouver("^[Ss](CENARIO|cenario).*") && reader.getNumligne() < ligneEntete) {
					reader.colonneSuivante();
					scenario.setLIBELLE((String) reader.lireCellule());
				}

				if (reader.trouver("^[Cc](ONSIGNE|onsigne).*") && reader.getNumligne() < ligneEntete) {
					reader.colonneSuivante();
					scenario.setCONSIGNES((String) reader.lireCellule());
				}
				if (reader.trouver("^[Dd](ISCOURS|iscours).*")  && reader.getNumligne() < ligneEntete) {
					reader.colonneSuivante();
					scenario.setDISCOURS((String) reader.lireCellule());
				}
				
				reader.atteindreLigne(ligneEntete);
				
				String precMotif = null, precSousMotif = null, precPoint = null, precSousPoint = null;
				Motif motif = null ;
				SousMotif sousMotif = null;
				Point point = null;
				SousPoint sousPoint = null;
				
				while (reader.ligneSuivante()) {
					
					int numligne = reader.getNumligne();
					
					String libMotif = colMotif > 0 ? (String) reader.lireCellule(numligne, colMotif) : null;
					String libSousMotif = colSousMotif > 0 ? (String) reader.lireCellule(numligne, colSousMotif) : null;
					String libPoint = colPoint > 0 ? (String) reader.lireCellule(numligne, colPoint) : null;
					String libSousPoint = colSousPoint > 0 ? (String) reader.lireCellule(numligne, colSousPoint) : null;
					String consignes = colConsignes > 0 ? (String) reader.lireCellule(numligne, colConsignes) : null;
					String discours = colDiscours > 0 ? (String) reader.lireCellule(numligne, colDiscours) : null;
					
					if (StringUtils.isNotBlank(libMotif) && !libMotif.equals(precMotif)) {
						motif = new Motif();
						motif.setLibelle(libMotif);
						scenario.getMotifs().add(motif);						
						precMotif = libMotif;
						precSousMotif = precPoint = precSousPoint = null;
						sousMotif = null;
						point = null;
						sousPoint = null;
					}
					if (motif != null && StringUtils.isNotBlank(libSousMotif) && !libSousMotif.equals(precSousMotif)) {
						sousMotif = new SousMotif();
						sousMotif.setLibelle(libSousMotif);
						sousMotif.setMOTIF_ID(motif.getId());
						motif.addSousMotif(sousMotif);						
						precSousMotif = libSousMotif;
						precPoint = precSousPoint = null;
						point = null;
						sousPoint = null;
					}
					if (sousMotif != null && StringUtils.isNotBlank(libPoint) && !libPoint.equals(precPoint)) {
						point = new Point();
						point.setLibelle(libPoint);
						point.setSousMotifId(sousMotif.getId());
						sousMotif.addPoint(point);						
						precPoint = libPoint;
						precSousPoint = null;
						sousPoint = null;
					}
					if (point != null && StringUtils.isNotBlank(libSousPoint) && !libSousPoint.equals(precSousPoint)) {
						sousPoint = new SousPoint();
						sousPoint.setLibelle(libSousPoint);
						sousPoint.setPointId(point.getId());
						point.addSousPoint(sousPoint);						
						precSousPoint = libSousPoint;
					}
					
					if (sousPoint != null) {
						sousPoint.setCONSIGNES(consignes);
						sousPoint.setDISCOURS(discours);						
					} else if (point != null) {
						point.setCONSIGNES(consignes);
						point.setDISCOURS(discours);						
					} else if (sousMotif != null) {
						sousMotif.setCONSIGNES(consignes);
						sousMotif.setDISCOURS(discours);						
					} else if (motif != null) {
						motif.setCONSIGNES(consignes);
						motif.setDISCOURS(discours);						
					}					
				}
			}		
		}
		
		return scenario;
	}
	
	/**
	 * @return the campagne
	 */
	public Campagne getCampagne() {
		return campagne;
	}

	/**
	 * @param campagne
	 *            the campagne to set
	 */
	public void setCampagne(Campagne campagne) {
		this.campagne = campagne;
	}

	/**
	 * @return the scenario
	 */
	public Scenario getScenario() {
		return scenario;
	}

	/**
	 * @param scenario
	 *            the scenario to set
	 */
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}
	
	public void terminer() {
		if (composer != null) {
			composer.terminer();
		}
		if (reader != null) {
			reader.sortir();
		}
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the infoMessage
	 */
	public String getInfoMessage() {
		return infoMessage;
	}

	/**
	 * @param infoMessage the infoMessage to set
	 */
	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

}
