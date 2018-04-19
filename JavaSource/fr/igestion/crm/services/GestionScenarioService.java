package fr.igestion.crm.services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import fr.igestion.common.services.IService;
import fr.igestion.common.services.IServiceResult;
import fr.igestion.common.services.ResultImpl;
import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.bean.contrat.Mutuelle;
import fr.igestion.crm.bean.scenario.Campagne;
import fr.igestion.crm.bean.scenario.ItemScenario;
import fr.igestion.crm.bean.scenario.Motif;
import fr.igestion.crm.bean.scenario.Point;
import fr.igestion.crm.bean.scenario.Scenario;
import fr.igestion.crm.bean.scenario.SousMotif;
import fr.igestion.crm.bean.scenario.SousPoint;

public class GestionScenarioService implements IService<String> {
	
	public static final String OP_EXPORT = "Export";
	public static final String OP_IMPORT = "Import";
	public static final String VAR_MESSAGE = "message";
	
	private Campagne campagne;
	private Scenario scenario;
	private Mutuelle mutuelle;
	
	
	@Override
	public IServiceResult<String> execute(Object... params) throws Exception {

		ResultImpl<String> result = new ResultImpl<String>();

		if (params == null || params.length < 1) {
			result.set(VAR_MESSAGE, "ERREUR >> Il faut renseigner au moins 1 paramètre");
			return result;
		}

		String operation = null;
		InputStream xlsInput = null;
		OutputStream xlsOutput = null;
		String campagneId = null, mutuelleId = null, entiteId = null;
		String msg = null;

		Object param = params[0];
		try {
			operation = (String) param;
		} catch (ClassCastException e1) {
			try {
				xlsInput = (InputStream) param;
				operation = OP_IMPORT;
			} catch (ClassCastException e2) {
				try {
					xlsOutput = (OutputStream) param;
					operation = OP_EXPORT;
				} catch (ClassCastException e3) {
					result.set(VAR_MESSAGE, "ERREUR >> 1er paramètre invalide : il doit être de type String, InputStream ou OutputStream");
					return result;
				}
			}
		}

		int nextParamIndex = 1;

		if (OP_EXPORT.equals(operation)) {
			if (xlsOutput == null) {
				xlsOutput = (OutputStream) params[nextParamIndex];
				nextParamIndex++;
			}
			campagneId = (String) params[nextParamIndex];
			nextParamIndex++;
			if (nextParamIndex < params.length) {
				mutuelleId = (String) params[nextParamIndex];
			}
			msg = exporter(xlsOutput, campagneId, mutuelleId, entiteId);

		} else if (OP_IMPORT.equals(operation)) {
			if (xlsInput == null) {
				xlsInput = (InputStream) params[1];
			}
			msg = importer(xlsInput);

		} else {
			msg = "Aucune opération n'a été effectuée avec ces paramètres \n" + params;
		}
		
		if (msg == null) {
			msg = operation + " bien effectué";
		}

		result.set(VAR_MESSAGE, msg);

		return result;
	}
	
	public String exporter(OutputStream xlsOut, String campagneId, String mutuelleId, String entiteId) throws Exception {

		scenario = null;

		SQLDataService.begin();
		
		if (campagne == null || !campagne.getId().equals(campagneId)) {
			campagne = chargerCampagne(campagneId, mutuelleId, entiteId);
		}
		if (campagne != null) {
			scenario = chargerScenarioCampagne(campagne);
		} else {
			return "Export impossible : campagne " + campagneId + " non trouvée";
		}
		SQLDataService.end();

		if (scenario != null) {
			ScenarioExcelDAO exceldao = new ScenarioExcelDAO(campagne, scenario, xlsOut);
			if (exceldao.saveScenario()) {
				return null;
			} else {
				return "Export impossible : " + exceldao.getErrorMessage();
			}
		} else {
			return "Export impossible : scénario non trouvé pour la campagne " + campagneId;
		}

	}
	
	public String importer(InputStream xlsIn) {

		String msg = null;

		ScenarioExcelDAO exceldao = new ScenarioExcelDAO(xlsIn);

		try {

			SQLDataService.begin();

			while (exceldao.feuilleSuivante()) {

				campagne = null;
				mutuelle = null;
				scenario = null;
				Scenario nouveauScenario = null;

				String libelleCampagne = exceldao.loadLibelleCampagne();
				String libelleMutuelle = exceldao.loadLibelleMutuelle();

				if (libelleCampagne != null) {
					campagne = SQLDataService.getCampagneByLibelle(libelleCampagne);
				} else { 
					msg = "Import impossible : la campagne n'est pas renseignée";
				}
				
				if (libelleMutuelle != null) {
					mutuelle = SQLDataService.getMutuelleByLibelle(libelleMutuelle);					
				} else { 
					msg = "Import impossible : la mutuelle n'est pas renseignée";
				}
				
				if (campagne != null && mutuelle != null) {
					campagne = chargerCampagne(campagne.getId(), mutuelle.getId(), null);
					
				} else if (campagne == null) {
					msg = "Import impossible : la campagne [" + libelleCampagne + "] n'a pas été trouvée";
					
				} else if (mutuelle == null) {
					msg = "Import impossible : la mutuelle [" + libelleMutuelle + "] n'a pas été trouvée";
				}

				if (msg == null) {
					
					if (campagne != null) {
						
						String mutuelleID = campagne.getMutuelles().iterator().next().getId();
						scenario = SQLDataService.getScenarioByCampagneMutuelle(campagne.getId(), mutuelleID);
						exceldao.setCampagne(campagne);
						exceldao.setScenario(scenario);
						nouveauScenario = exceldao.loadScenario();
						nouveauScenario.setMUTUELLE_ID(mutuelleID);

						if (scenario != null) {
							scenario = chargerScenarioCampagne(campagne);
						}
						scenario = mettreAJourScenario(scenario, nouveauScenario);
						enregisterScenario(scenario);

					} else {
						msg = "Import impossible : la mutuelle [" + libelleMutuelle + "] ne correspond pas à la campagne [" + libelleCampagne + "]";

					}
				}
			}

			SQLDataService.end();
			exceldao.terminer();

		} catch (Exception e) {
			e.printStackTrace();
			msg = "Import impossible : erreur technique " + e.getMessage();
		}

		return msg;

	}
	
	public void enregisterScenario(Scenario scenario) throws Exception {

		if (scenario != null) {
			
			SQLDataService.begin();

			ItemScenario item = null;
			boolean ok = true;

			if (StringUtils.isBlank(scenario.getID())) {
				ok = SQLDataService.ajouterScenario(scenario);
				if (ok) {
					item = SQLDataService.getItemScenario(SQLDataService.NIVEAU_SCENARIO, scenario.getCAMPAGNE_ID(),
							scenario.getMUTUELLE_ID());
					scenario.setID(item.getId());
				}
			} else {
				SQLDataService.modifierScenario(scenario);
			}

			if (ok && scenario.getMotifs() != null) {
				
				for (Motif motif : scenario.getMotifs()) {

					if (ItemScenario.ACTION_INSERT.equals(motif.getActionAFaire()) || ItemScenario.ACTION_INSERT.equals(scenario.getActionAFaire())) {
						SQLDataService.ajouterItemScenario(motif, scenario.getID());
						
					} else if (ItemScenario.ACTION_DELETE.equals(motif.getActionAFaire())) {
						SQLDataService.supprimerItemScenario(motif);
						
					} else if (ItemScenario.ACTION_UPDATE.equals(motif.getActionAFaire())) {
						SQLDataService.modifierConsignesDiscours(SQLDataService.NIVEAU_MOTIF, motif.getId(), motif.getCONSIGNES(), motif.getDISCOURS());
						
						if (motif.getSousMotifs() != null) {
							
							for (SousMotif sousMotif : motif.getSousMotifs()) {

								if (ItemScenario.ACTION_INSERT.equals(sousMotif.getActionAFaire())) {
									SQLDataService.ajouterItemScenario(sousMotif, motif.getId());
									
								} else if (ItemScenario.ACTION_DELETE.equals(sousMotif.getActionAFaire())) {
									SQLDataService.supprimerItemScenario(sousMotif);
									
								} else if (ItemScenario.ACTION_UPDATE.equals(sousMotif.getActionAFaire())) {
									SQLDataService.modifierConsignesDiscours(SQLDataService.NIVEAU_SOUSMOTIF, sousMotif.getId(), sousMotif.getCONSIGNES(), sousMotif.getDISCOURS());
									
									if (sousMotif.getPoints() != null) {
										
										for (Point point : sousMotif.getPoints()) {

											if (ItemScenario.ACTION_INSERT.equals(point.getActionAFaire())) {
												SQLDataService.ajouterItemScenario(point, sousMotif.getId());
												
											} else if (ItemScenario.ACTION_DELETE.equals(point.getActionAFaire())) {
												SQLDataService.supprimerItemScenario(point);
												
											} else if (ItemScenario.ACTION_UPDATE.equals(point.getActionAFaire())) {
												SQLDataService.modifierConsignesDiscours(SQLDataService.NIVEAU_POINT, point.getId(), point.getCONSIGNES(), point.getDISCOURS());
												
												if (point.getSousPoints() != null) {
													
													for (SousPoint sousPoint : point.getSousPoints()) {

														if (ItemScenario.ACTION_INSERT.equals(sousPoint.getActionAFaire())) {
															SQLDataService.ajouterItemScenario(sousPoint, point.getId());
															
														} else if (ItemScenario.ACTION_DELETE.equals(sousPoint.getActionAFaire())) {
															SQLDataService.supprimerItemScenario(sousPoint);
															
														} else if (ItemScenario.ACTION_UPDATE.equals(sousPoint.getActionAFaire())) {
															SQLDataService.modifierConsignesDiscours(SQLDataService.NIVEAU_SOUSPOINT, sousPoint.getId(), sousPoint.getCONSIGNES(), sousPoint.getDISCOURS());

														} 
													}
												}
											} 
										}
									}
								} 
							}
						}
					} 
				}
			}
			
			SQLDataService.commit();
			
		}

	}
	
	public Scenario mettreAJourScenario(Scenario ancienScenario, Scenario nouveauScenario) {

		Scenario retour = null;

		if (ancienScenario == null) {
			nouveauScenario.setActionAFaire(ItemScenario.ACTION_INSERT);
			retour = nouveauScenario;

		} else if (nouveauScenario != null) {
			
			retour = ancienScenario;

			if (nouveauScenario.getLIBELLE() != null) {
				ancienScenario.setLIBELLE(nouveauScenario.getLIBELLE());
			}
			
			ancienScenario.setCONSIGNES(nouveauScenario.getCONSIGNES());
			ancienScenario.setDISCOURS(nouveauScenario.getDISCOURS());

			// mise à jour des anciennes infos avec les nouveaux libellé,
			// consignes et discours
			for (Motif motif : ancienScenario.getMotifs()) {
				Motif nouvMotif = (Motif) getItemDansListe(motif, nouveauScenario.getMotifs());

				if (nouvMotif != null) {
					motif.setLibelle(nouvMotif.getLibelle());
					motif.setCONSIGNES(nouvMotif.getCONSIGNES());
					motif.setDISCOURS(nouvMotif.getDISCOURS());
					motif.setActionAFaire(ItemScenario.ACTION_UPDATE);

					for (SousMotif sousMotif : motif.getSousMotifs()) {
						SousMotif nouvSousMotif = (SousMotif) getItemDansListe(sousMotif, nouvMotif.getSousMotifs());

						if (nouvSousMotif != null) {
							sousMotif.setLibelle(nouvSousMotif.getLibelle());
							sousMotif.setCONSIGNES(nouvSousMotif.getCONSIGNES());
							sousMotif.setDISCOURS(nouvSousMotif.getDISCOURS());
							sousMotif.setActionAFaire(ItemScenario.ACTION_UPDATE);

							for (Point point : sousMotif.getPoints()) {
								Point nouvPoint = (Point) getItemDansListe(point, nouvSousMotif.getPoints());

								if (nouvPoint != null) {
									point.setLibelle(nouvPoint.getLibelle());
									point.setCONSIGNES(nouvPoint.getCONSIGNES());
									point.setDISCOURS(nouvPoint.getDISCOURS());
									point.setActionAFaire(ItemScenario.ACTION_UPDATE);

									for (SousPoint sousPoint : point.getSousPoints()) {
										SousPoint nouvSousPoint = (SousPoint) getItemDansListe(sousPoint,
												nouvPoint.getSousPoints());
										
										if (nouvSousPoint != null) {
											sousPoint.setLibelle(nouvSousPoint.getLibelle());
											sousPoint.setCONSIGNES(nouvSousPoint.getCONSIGNES());
											sousPoint.setDISCOURS(nouvSousPoint.getDISCOURS());
											sousPoint.setActionAFaire(ItemScenario.ACTION_UPDATE);
										} else {
											sousPoint.setActionAFaire(ItemScenario.ACTION_DELETE);
										}
									}
								} else {
									point.setActionAFaire(ItemScenario.ACTION_DELETE);
								}
							}
						} else {
							sousMotif.setActionAFaire(ItemScenario.ACTION_DELETE);
						}
					}
				} else {
					motif.setActionAFaire(ItemScenario.ACTION_DELETE);
				}
			}

			// ajout des nouvelles infos inexistantes jusqu'alors
			
			if (nouveauScenario.getMotifs() != null) {
				
				for (Motif nouvMotif : nouveauScenario.getMotifs()) {
					Motif motif = (Motif) getItemDansListe(nouvMotif, ancienScenario.getMotifs());

					if (motif == null) {
						nouvMotif.setActionAFaire(ItemScenario.ACTION_INSERT);
						ancienScenario.getMotifs().add(nouvMotif);
						
					} else if (nouvMotif.getSousMotifs() != null) {

						for (SousMotif nouvSousMotif : nouvMotif.getSousMotifs()) {
							SousMotif sousMotif = (SousMotif) getItemDansListe(nouvSousMotif, motif.getSousMotifs());

							if (sousMotif == null) {
								nouvSousMotif.setActionAFaire(ItemScenario.ACTION_INSERT);
								motif.getSousMotifs().add(nouvSousMotif);
								
							} else if (nouvSousMotif.getPoints() != null) {
								
								for (Point nouvPoint : nouvSousMotif.getPoints()) {
									Point point = (Point) getItemDansListe(nouvPoint, sousMotif.getPoints());

									if (point == null) {
										nouvPoint.setActionAFaire(ItemScenario.ACTION_INSERT);
										sousMotif.getPoints().add(nouvPoint);
										
									} else if (nouvPoint.getSousPoints() != null) {
										for (SousPoint nouvSousPoint : nouvPoint.getSousPoints()) {
											SousPoint sousPoint = (SousPoint) getItemDansListe(nouvSousPoint,
													point.getSousPoints());

											if (sousPoint == null) {
												nouvSousPoint.setActionAFaire(ItemScenario.ACTION_INSERT);
												point.getSousPoints().add(nouvSousPoint);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return retour;
	}
	
	private ItemScenario getItemDansListe(ItemScenario item, Collection<? extends ItemScenario> liste) {
		
		ItemScenario retour = null;
		
		if (item != null && liste != null) {
			for (ItemScenario itemScenario : liste) {
				if (item.equals(itemScenario)) {
					retour = itemScenario;
					break;
				}
			}
		}
		
		return retour;
		
	}
	
	
	public Scenario chargerScenarioCampagne(Campagne campagne) throws Exception {
		
		Scenario scenario = null;

		if (campagne != null) {
			
			for (Mutuelle mutuelle : campagne.getMutuelles()) {
				scenario = SQLDataService.getScenarioByCampagneMutuelle(campagne.getId(), mutuelle.getId());
				
				if (scenario != null) {
					Collection<Motif> motifs = SQLDataService.getMotifsByScenarioId(scenario.getID(), true);
					scenario.setMotifs(motifs);					
					
					for (Motif motif : motifs) {						
						Collection<SousMotif> sousMotifs = SQLDataService.getMotifSousMotifs(motif.getId());
						motif.setSousMotifs(sousMotifs);	
						
						for (SousMotif sousMotif : sousMotifs) {							
							Collection<Point> points = SQLDataService.getSousMotifPoints(sousMotif.getId());
							sousMotif.setPoints(points);
							
							for (Point point : points) {
								Collection<SousPoint> sousPoints = SQLDataService.getPointSousPoints(point.getId());
								point.setSousPoints(sousPoints);								
							}
						}
					}
					break;
				}
			}
		}
		
		return scenario;		
	}
	
	public Campagne chargerCampagne(String campagneId, String mutuelleId, String entiteId) {

		campagne = SQLDataService.getCampagneById(campagneId);

		if (campagne != null) {
			
			Collection<Mutuelle> mutuelles = SQLDataService.getCampagneMutuelles(campagneId);
			if (mutuelleId == null) {
				campagne.setMutuelles(mutuelles);
			} else {
				for (Mutuelle mutuelle : mutuelles) {
					if (mutuelleId.equals(mutuelle.getId())) {
						campagne.setMutuelles(Arrays.asList(mutuelle));
						break;
					}
				}
			}
			
			if (campagne.getMutuelles() == null || campagne.getMutuelles().isEmpty()) {
				campagne = null;
			}
		}
		return campagne;
	}
	
	public static void main(String[] args) throws Exception {
		
		GestionScenarioService service = new GestionScenarioService();

		String nomFichier = "C:/dev/dev-igestion/_doc-projet/HContacts - scénario excel/FORTEGO MAJ 03-11 - Scénario Campagne H CONTACTS.xlsx";
		
		IServiceResult<String> result = service.execute(new FileInputStream(nomFichier));
		System.out.println(result.get(VAR_MESSAGE));
		
		result = service.execute(new FileOutputStream("c:/dev/scenario.xlsx"), "822");
		System.out.println(result.get(VAR_MESSAGE));


	}

	/**
	 * @return the campagne
	 */
	public Campagne getCampagne() {
		return campagne;
	}

	/**
	 * @param campagne the campagne to set
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
	 * @param scenario the scenario to set
	 */
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	/**
	 * @return the mutuelle
	 */
	public Mutuelle getMutuelle() {
		return mutuelle;
	}

	/**
	 * @param mutuelle the mutuelle to set
	 */
	public void setMutuelle(Mutuelle mutuelle) {
		this.mutuelle = mutuelle;
	}
		
		

}
