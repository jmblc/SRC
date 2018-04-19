package fr.igestion.crm;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;

import common.Logger;

import fr.igestion.crm.bean.LibelleCode;
import fr.igestion.crm.bean.TeleActeur;
import fr.igestion.crm.bean.contrat.Mutuelle;
import fr.igestion.crm.bean.pec.DemandePec;
import fr.igestion.crm.bean.scenario.Campagne;
import fr.igestion.crm.bean.scenario.Motif;
import fr.igestion.crm.bean.scenario.Scenario;

public class CrmForms {

    private static final Logger LOGGER = Logger.getLogger(CrmForms.class);
    
    public static final String _campagne_id="campagne_id";
    public static final String _mutuelle_id="mutuelle_id";
    public static final String _teleacteur_id ="teleacteur_id";
    public static final String _id_objet ="id_objet";
    public static final String _cle_recherche ="cle_recherche";              
    public static final String _appelant_code ="appelant_code";
    public static final String _appelant_libelle ="appelant_libelle";     
    public static final String _motif_id ="motif_id";
    public static final String _sous_motif_id ="sous_motif_id";
    public static final String _point_id ="point_id";
    public static final String _sous_point_id ="sous_point_id";
    public static final String _type_dossier ="type_dossier";
    public static final String _commentaires ="commentaires";
    public static final String _regime_id ="regime_id";
    public static final String _satisfaction_code ="satisfaction_code";
    public static final String _reclamation ="reclamation";
    public static final String _traitement_urgent ="traitement_urgent";
    public static final String _date_rappel ="date_rappel";
    public static final String _numero_rappel ="numero_rappel";
    public static final String _periode_rappel ="periode_rappel";
    public static final String _cloture_code ="cloture_code";
    public static final String _transferer_fiche ="transferer_fiche";
    public static final String _destinataire_transfert ="destinataire_transfert";
    public static final String _texte_generique ="texte_generique"; 
    public static final String _id_generique ="id_generique";   
    public static final String _prestations_beneficiaire_id ="prestations_beneficiaire_id";
    public static final String _prestations_code_acte ="prestations_code_acte";
    public static final String _prestations_decompte_date ="prestations_decompte_date";
    public static final String _email_confirmation ="email_confirmation";
    public static final String _salaries_contrat_id ="salaries_contrat_id";
    public static final String _procedure_mail ="procedure_mail";
    public static final String _procedure_mail_dest ="procedure_mail_dest";
    public static final String _procedure_mail_cree = "procedure_mail_cree";
    public static final String _procedure_mail_id="procedure_mail_id";
    public static final String _demande_pec_cree ="demande_pec_cree";
        
    
    private CrmForms(){
        
    }
    
    public static void initialiserFicheAppelForm(ActionMapping mapping,
            HttpServletRequest request, Campagne campagne,
            Collection<Mutuelle> mutuelles) {

        DynaActionForm ficheAppelForm = (DynaActionForm) request.getSession()
                .getAttribute(IContacts._FicheAppelForm);
        if (ficheAppelForm == null) {
            
            ModuleConfig moduleConfig = mapping.getModuleConfig();
            FormBeanConfig formBeanConfig = moduleConfig
                    .findFormBeanConfig(IContacts._FicheAppelForm);
            DynaActionFormClass dynaClass = DynaActionFormClass
                    .createDynaActionFormClass(formBeanConfig);
            try{
                ficheAppelForm = (DynaActionForm) dynaClass.newInstance();
            } catch(Exception e){
                LOGGER.error("initialiserFicheAppelForm",e);
                throw new IContactsException(e);
            }
            
        }

        Mutuelle mutuelle = null;
        String consignes = "", discours = "";
        Collection<Motif> motifs = null;

        if (!mutuelles.isEmpty() && mutuelles.size() == 1) {
            mutuelle = (Mutuelle) mutuelles.toArray()[0];
            String logo = UtilHtml.getLogoMutuelle(mutuelle, request);
            mutuelle.setLogo(logo);

            ficheAppelForm.set(_mutuelle_id, mutuelle.getId());

            // Types de dossiers pour H.Courriers
            Collection<LibelleCode> types_dossiers = SQLDataService
                    .getTypesDossiers(mutuelle.getId());
            request.getSession().setAttribute(FicheAppelAction._var_session_types_dossiers, types_dossiers);
            ficheAppelForm.set(_type_dossier, IContacts._blankSelect);

            Scenario scenario = SQLDataService.getScenarioByCampagneMutuelle(
                    campagne.getId(), mutuelle.getId());
            if (scenario != null) {
                consignes = scenario.getCONSIGNES();
                discours = scenario.getDISCOURS();
                request.getSession().setAttribute(FicheAppelAction._var_session_scenario, scenario);
                String scenario_id = scenario.getID();
                motifs = SQLDataService.getMotifsByScenarioId(scenario_id);
            }
        } else {
            ficheAppelForm.set(_mutuelle_id, IContacts._blankSelect);
        }

        request.getSession().setAttribute(FicheAppelAction._var_session_mutuelle, mutuelle);

        request.getSession().setAttribute(FicheAppelAction._var_session_motifs, motifs);
        ficheAppelForm.set(_motif_id, IContacts._blankSelect);
        request.getSession().setAttribute(FicheAppelAction._var_session_sous_motifs, null);
        ficheAppelForm.set(_sous_motif_id, IContacts._blankSelect);
        request.getSession().setAttribute(FicheAppelAction._var_session_points, null);
        ficheAppelForm.set(_point_id, IContacts._blankSelect);
        request.getSession().setAttribute(FicheAppelAction._var_session_sous_points, null);
        ficheAppelForm.set(_sous_point_id, IContacts._blankSelect);
        ficheAppelForm.set(_procedure_mail_id,IContacts._blankSelect);
        ficheAppelForm.set(_procedure_mail, Boolean.FALSE);
        ficheAppelForm.set(_procedure_mail_dest, "");
        ficheAppelForm.set(_commentaires, "");
        ficheAppelForm.set(_satisfaction_code, IContacts._blankSelect);
        ficheAppelForm.set(_reclamation, null);
        ficheAppelForm.set(_traitement_urgent, "0");
        ficheAppelForm.set(_periode_rappel, IContacts._blankSelect);
        ficheAppelForm.set(_date_rappel, "");
        ficheAppelForm.set(_numero_rappel, "");
        ficheAppelForm.set(_cloture_code, IContacts._blankSelect);

        ficheAppelForm.set(_transferer_fiche, "0");
        ficheAppelForm.set(_destinataire_transfert, "");

        ficheAppelForm.set(_texte_generique, "");
        ficheAppelForm.set(_id_generique, "");
        ficheAppelForm.set(_cle_recherche, "");
        ficheAppelForm.set(_prestations_beneficiaire_id, "");
        ficheAppelForm.set(_prestations_code_acte, "");
        ficheAppelForm.set(_prestations_decompte_date, "");
        
        ficheAppelForm.set(_procedure_mail_cree, Boolean.FALSE);
        ficheAppelForm.set(_demande_pec_cree, Boolean.FALSE);
        request.getSession().setAttribute(FicheAppelAction._var_session_lst_pec_encours,new ArrayList<DemandePec>());
        
        
        request.getSession().setAttribute(FicheAppelAction._var_session_consignes, consignes);
        request.getSession().setAttribute(FicheAppelAction._var_session_discours, discours);
        request.getSession().setAttribute(FicheAppelAction._var_session_pec, null);

        TeleActeur teleActeur = (TeleActeur) request.getSession().getAttribute(
                IContacts._var_session_teleActeur);
        ficheAppelForm.set(_teleacteur_id, teleActeur.getId());

        String code_appelant_assure = SQLDataService
                .getCodeAssureTypeAppelant();
        ficheAppelForm.set(_appelant_code, code_appelant_assure);

        request.getSession().setAttribute(IContacts._FicheAppelForm, ficheAppelForm);

    }

    public static void initialiserAccueilForm(ActionMapping mapping,
            HttpServletRequest request) {

        DynaActionForm accueilForm = (DynaActionForm) request.getSession()
                .getAttribute(IContacts._AccueilForm);
        if (accueilForm == null) {
            
            ModuleConfig moduleConfig = mapping.getModuleConfig();
            FormBeanConfig formBeanConfig = moduleConfig
                    .findFormBeanConfig(IContacts._AccueilForm);
            DynaActionFormClass dynaClass = DynaActionFormClass
                    .createDynaActionFormClass(formBeanConfig);
            try{
                accueilForm = (DynaActionForm) dynaClass.newInstance();
            } catch(Exception e ) {
                LOGGER.error("initialiserAccueilForm",e);
                throw new IContactsException(e); 
            }
        }

        accueilForm.set(_campagne_id, IContacts._blankSelect);
        accueilForm.set("modeCreation", "E");
        request.getSession().setAttribute(IContacts._AccueilForm, accueilForm);
    }

}
