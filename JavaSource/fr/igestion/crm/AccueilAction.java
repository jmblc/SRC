package fr.igestion.crm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import fr.igestion.crm.bean.Appel;
import fr.igestion.crm.bean.Message;
import fr.igestion.crm.bean.ModeleProcedureMail;
import fr.igestion.crm.bean.ObjetAppelant;
import fr.igestion.crm.bean.TeleActeur;
import fr.igestion.crm.bean.contrat.Mutuelle;
import fr.igestion.crm.bean.scenario.Campagne;

public class AccueilAction extends DispatchAction {

    private static final String _campagne_id="campagne_id";
    private static final String _modeCreation="modeCreation";
    
    
    private static final String _var_session_campagne="campagne";
    private static final String _var_session_mutuelle="mutuelle";
    private static final String _var_session_objet_appelant="objet_appelant";
    private static final String _var_session_appel="appel";
    
    public ActionForward creerFiche(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        
        request.getSession().removeAttribute(IContacts._var_session_FicheAppelForm);
        
        TeleActeur teleacteur = (TeleActeur) request.getSession().getAttribute(
                IContacts._var_session_teleActeur);
        String teleacteur_id = teleacteur.getId();

        String campagne_id = (String) daf.get(_campagne_id);
        String modeCreation = (String) daf.get(_modeCreation);

        if ("L".equals(modeCreation)) {
            request.getSession().setAttribute(_modeCreation, "L");
        } else {
            request.getSession().setAttribute(_modeCreation, "E");
            Appel appel = SQLDataService.creerAppel(teleacteur_id, campagne_id);
            if (appel == null) {
                request.setAttribute(IContacts._message,
                        "Attention : la fiche d'appel n'a pas pu être créée!");

                // Préparer le formulaire de la page d'accueil
                CrmForms.initialiserAccueilForm(mapping, request);

                return mapping.findForward(IContacts._accueil);
            }
            request.getSession().setAttribute(_var_session_appel, appel);
        }

        Campagne campagne = SQLDataService.getCampagneById(campagne_id);
        Collection<Mutuelle> mutuelles = SQLDataService
                .getCampagneMutuelles(campagne_id);
        campagne.setMutuelles(mutuelles);
        Collection<Message> messages = SQLDataService
                .getCampagneMessage(campagne_id);
        campagne.setMessages(messages);
        campagne.setMutuelles(mutuelles);
        request.getSession().setAttribute(_var_session_campagne, campagne);
        
        CrmForms.initialiserFicheAppelForm(mapping, request, campagne,
                mutuelles);
   
        ObjetAppelant objet_appelant = new ObjetAppelant();
        objet_appelant.setType("Assuré");
        objet_appelant.setObjet(null);
        objet_appelant.setOngletCourant(null);
        objet_appelant.setDetailObjet(null);
        objet_appelant.setHistorique(null);
        objet_appelant.setObjetPrestations(null);

        request.getSession().setAttribute(_var_session_objet_appelant, objet_appelant);
        
        request.getSession().setAttribute(FicheAppelAction._var_session_lst_modele_procedureMail,new ArrayList<ModeleProcedureMail>());
        request.getSession().setAttribute(FicheAppelAction._var_session_selected_procedureMails,new ArrayList<ModeleProcedureMail>());
        
        String id_objet = (String) request.getSession().getAttribute("assure_id");
        if (HermesAction._hermes_orig.equals(request.getSession().getAttribute("origine")) && id_objet != null) {
        	try {
        		request.getSession().removeAttribute("assure_id");
        		response.sendRedirect("FicheAppel.do?method=setAssure&id_objet=" + id_objet);
        		return null;
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward remonterFiche(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        request.getSession().setAttribute(_modeCreation, "E");

        return mapping.findForward(IContacts._ficheAppel);
    }

    public ActionForward retourAccueil(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        request.getSession().setAttribute(_var_session_mutuelle, null);

        CrmForms.initialiserAccueilForm(mapping, request);

        return mapping.findForward(IContacts._accueil);
    }

}