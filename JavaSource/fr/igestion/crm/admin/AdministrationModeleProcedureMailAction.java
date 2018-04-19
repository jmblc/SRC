package fr.igestion.crm.admin;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import fr.igestion.crm.CrmUtilSession;
import fr.igestion.crm.IContacts;
import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.UtilHtml;
import fr.igestion.crm.bean.ModeleProcedureMail;
import fr.igestion.crm.bean.document.Document;
import fr.igestion.crm.bean.scenario.InfosScenario;

public class AdministrationModeleProcedureMailAction extends DispatchAction {

    private static final String _mod_prc_mail_id="mod_prc_mail_id";
    private static final String _mod_prc_mail_modele="mod_prc_mail_modele";
    private static final String _mod_prc_mail_type="mod_prc_mail_type";
    private static final String _mod_prc_mail_objet="mod_prc_mail_objet";
    private static final String _mod_prc_mail_invite="mod_prc_mail_invite";
    private static final String _mod_prc_mail_recap_adh="mod_prc_mail_recap_adh";
    private static final String _mod_prc_mail_corps="mod_prc_mail_corps";
    private static final String _mod_prc_mail_signature="mod_prc_mail_signature";
    private static final String _mod_prc_mail_destinataire="mod_prc_mail_destinataire";
    private static final String _mod_prc_mail_recap_centregestion="mod_prc_mail_recap_centregestion";
    private static final String _mod_prc_mail_document_id="mod_prc_mail_document_id";
    
    
    private static final String _var_session_message = "message";
    private static final String _var_session_mod_modeles="mod_modeles";
    private static final String _var_session_scenariiProcedure="scenariiProcedure";
    private static final String _var_session_documents="mod_documents";
    
    private static void resetForm( DynaActionForm daf ){
        daf.set(_mod_prc_mail_id, IContacts._blankSelect);
        daf.set(_mod_prc_mail_modele, "");
        daf.set(_mod_prc_mail_type, "");
        daf.set(_mod_prc_mail_objet, "");
        daf.set(_mod_prc_mail_invite, "");
        daf.set(_mod_prc_mail_recap_adh, Boolean.FALSE);
        daf.set(_mod_prc_mail_corps, "");
        daf.set(_mod_prc_mail_signature, "");
        daf.set(_mod_prc_mail_destinataire, "");
        daf.set(_mod_prc_mail_recap_centregestion, Boolean.FALSE);
        daf.set(_mod_prc_mail_document_id, IContacts._blankSelect);
    }
    
    private static ModeleProcedureMail initModelFromForm( DynaActionForm daf ){
        
        ModeleProcedureMail leModeleProcedureMail = new ModeleProcedureMail();

        leModeleProcedureMail.setId((String) daf.get(_mod_prc_mail_id));
        leModeleProcedureMail.setLibelle((String) daf
                .get(_mod_prc_mail_modele));
        leModeleProcedureMail.setType((String) daf.get(_mod_prc_mail_type));
        leModeleProcedureMail.setMailObjet((String) daf
                .get(_mod_prc_mail_objet));
        leModeleProcedureMail.setMailInvite((String) daf
                .get(_mod_prc_mail_invite));
        leModeleProcedureMail.setRecapAdherent((Boolean) daf
                .get(_mod_prc_mail_recap_adh));
        leModeleProcedureMail.setMailCorps((String) daf
                .get(_mod_prc_mail_corps));
        leModeleProcedureMail.setMailSignature((String) daf
                .get(_mod_prc_mail_signature));
        leModeleProcedureMail.setTypeDestinataire((String) daf
                .get(_mod_prc_mail_destinataire));
        leModeleProcedureMail.setRecapCentreGestion((Boolean) daf
                .get(_mod_prc_mail_recap_centregestion));
        
        if( !((String)daf.get(_mod_prc_mail_document_id)).equalsIgnoreCase(IContacts._blankSelect) ){
            Document unDocument = new Document();
            unDocument.setId((String)daf.get( _mod_prc_mail_document_id ));
            leModeleProcedureMail.setDocument(unDocument);
        }
        
        return leModeleProcedureMail;
    }
    
    public ActionForward init(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        resetForm( daf );
 
        // initialisation liste des modèles
        Collection<ModeleProcedureMail> modelesProceduresMail = SQLDataService
                .getModelesProcedureMail();
        request.getSession().setAttribute(_var_session_mod_modeles, modelesProceduresMail);
        
        // initialisation liste des documents
        Collection<Document> documents = SQLDataService.getDocuments();
        request.getSession().setAttribute(_var_session_documents, documents);

        request.getSession().setAttribute(_var_session_scenariiProcedure, null);

        return mapping.findForward(IContacts._modele_procedure_mail);

    }

    public ActionForward ajouterModeleProcedureMail(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        UtilHtml.setAnchor(request, "Haut");
        
        ModeleProcedureMail leModeleProcedureMail = initModelFromForm( daf );

        Boolean resultat = SQLDataService
                .ajouterModeleProcedureMail(leModeleProcedureMail);

        if (resultat) {

            resetForm( daf );
       
            Collection<ModeleProcedureMail> modelesProceduresMail = SQLDataService
                    .getModelesProcedureMail();
            request.getSession().setAttribute(_var_session_mod_modeles,
                    modelesProceduresMail);

            request.setAttribute(_var_session_message,
                    "Le modèle de procédure mail a été ajouté avec succès.");
        } else {
            request.setAttribute(_var_session_message,
                    "Erreur ajout du modèle de procedure mail.");
        }

        return mapping.findForward(IContacts._modele_procedure_mail);
    }

    public ActionForward modifierModeleProcedureMail(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        UtilHtml.setAnchor(request, "Haut");

        ModeleProcedureMail leModeleProcedureMail = initModelFromForm( daf );

        Boolean resultat = SQLDataService
                .modifierModeleProcedureMail(leModeleProcedureMail);

        if (resultat) {
            request.setAttribute(_var_session_message,
                    "Le paramétrage du modèle de procédure mail a été modifié avec succès.");
        } else {
            request.setAttribute(_var_session_message,
                    "Erreur modification du modèle de procédure mail.");
        }

        return mapping.findForward(IContacts._modele_procedure_mail);
    }

    public ActionForward supprimerModeleProcedureMail(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        UtilHtml.setAnchor(request, "Haut");

        ModeleProcedureMail leModeleProcedureMail = new ModeleProcedureMail();
        leModeleProcedureMail.setId((String) daf.get(_mod_prc_mail_id));

        Boolean resultat = SQLDataService
                .supprimerModeleProcedureMail(leModeleProcedureMail);

        if (resultat) {

            resetForm( daf );
            
            // initialisation liste des modèles
            Collection<ModeleProcedureMail> modelesProceduresMail = SQLDataService
                    .getModelesProcedureMail();
            request.getSession().setAttribute(_var_session_mod_modeles,
                    modelesProceduresMail);

            request.setAttribute(_var_session_message,
                    "Le paramétrage du modèle de procédure mail a été supprimé avec succès.");
        } else {
            request.setAttribute(_var_session_message,
                    "Erreur suppression du modèle de procédure mail.");
        }

        return mapping.findForward(IContacts._modele_procedure_mail);
    }

    public ActionForward selectModeleProcedureMail(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        UtilHtml.setAnchor(request, "Haut");

        ModeleProcedureMail leModeleProcedureMail = new ModeleProcedureMail();
        leModeleProcedureMail.setId((String) daf.get(_mod_prc_mail_id));

        if (!"-1".equalsIgnoreCase(leModeleProcedureMail.getId())) {

            leModeleProcedureMail = SQLDataService
                    .getModeleProcedureMail(leModeleProcedureMail);

            if (leModeleProcedureMail != null) {

                daf.set(_mod_prc_mail_id, leModeleProcedureMail.getId());
                daf.set(_mod_prc_mail_modele, leModeleProcedureMail.getLibelle());
                daf.set(_mod_prc_mail_type, leModeleProcedureMail.getType());
                daf.set(_mod_prc_mail_objet, leModeleProcedureMail.getMailObjet());
                daf.set(_mod_prc_mail_invite, leModeleProcedureMail.getMailInvite());
                daf.set(_mod_prc_mail_recap_adh, leModeleProcedureMail.isRecapAdherent());
                daf.set(_mod_prc_mail_corps, leModeleProcedureMail.getMailCorps());
                daf.set(_mod_prc_mail_signature, leModeleProcedureMail.getMailSignature());
                daf.set(_mod_prc_mail_recap_centregestion, leModeleProcedureMail.isRecapCentreGestion());
                daf.set(_mod_prc_mail_destinataire, leModeleProcedureMail.getTypeDestinataire());
                if( leModeleProcedureMail.getDocument()!= null){
                    daf.set(_mod_prc_mail_document_id, leModeleProcedureMail.getDocument().getId());
                }
                else{
                    daf.set(_mod_prc_mail_document_id,IContacts._blankSelect );
                }
                
                Collection<InfosScenario> scenariiProcedure = SQLDataService
                        .getScenariiModeleProcedure(leModeleProcedureMail);
                request.getSession().setAttribute(_var_session_scenariiProcedure,
                        scenariiProcedure);

            } else {
                request.setAttribute(_var_session_message,
                        "Erreur lecture du modèle de procédure mail.");
            }
        } else {
            resetForm(daf); 
            request.getSession().setAttribute(_var_session_scenariiProcedure,
                    null);
        }

        return mapping.findForward(IContacts._modele_procedure_mail);
    }

}