package fr.igestion.crm.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import fr.igestion.crm.CrmUtilSession;
import fr.igestion.crm.SQLDataService;
import fr.igestion.crm.bean.ComparateurMessage;
import fr.igestion.crm.bean.Message;
import fr.igestion.crm.bean.scenario.Campagne;
import fr.igestion.crm.config.IContacts;

public class AdministrationMessagesAction extends DispatchAction {

    private static final String _message_id = "message_id";
    private static final String _campagne_id = "campagne_id";
    private static final String _titre = "titre";
    private static final String _contenu = "contenu";
    private static final String _dateDebut = "dateDebut";
    private static final String _dateFin = "dateFin";
    private static final String _texte_generique = "texte_generique";
    
    private static final String _var_session_admin_messages = "admin_messages";
    private static final String _var_session_admin_campagnes="admin_campagnes";
    private static final String _var_session_message = "message";
    private static final String _var_session_sens_tri_messages="sens_tri_messages";
    
    private static void initSessionMessages(HttpServletRequest request){
        Collection<Message> admin_messages = (Collection<Message>) SQLDataService
                .getMessages();
        request.getSession().setAttribute(_var_session_admin_messages, admin_messages);    
    }
    
    private void resetForm( DynaActionForm daf ){
        daf.set(_message_id, null);
        daf.set(IContacts._struts_method, null);
    }
    
    private static boolean creerMessage(DynaActionForm daf ){
        
        return SQLDataService.creerMessage( 
                    (String) daf.get(_campagne_id),
                    (String) daf.get(_titre), 
                    (String) daf.get(_contenu), 
                    (String) daf.get(_dateDebut), 
                    (String) daf.get(_dateFin));
    }
    
    
    private static boolean modifierMessage(DynaActionForm daf ){
    
    return SQLDataService.modifierMessage( 
            (String) daf.get(_message_id), 
            (String) daf.get(_campagne_id),
            (String) daf.get(_titre), 
            (String) daf.get(_contenu), 
            (String) daf.get(_dateDebut), 
            (String) daf.get(_dateFin));
    }
    
    public ActionForward init(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
        DynaActionForm daf = (DynaActionForm) form;
        
        // Lister les campagnes
        Collection<Campagne> admin_campagnes = SQLDataService.getCampagnes("0");
        request.getSession().setAttribute(_var_session_admin_campagnes, admin_campagnes);

        // Lister les messages
        initSessionMessages(request);
        
        resetForm( daf );
        
        return mapping.findForward(IContacts._message);
    }

    public ActionForward supprimerMessage(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        
        DynaActionForm daf = (DynaActionForm) form;

        String message_id = (String) daf.get(_message_id);

        boolean res = SQLDataService.supprimerMessage(message_id);
        
        String message = "";
        if (res) {
            message = "Le message a été supprimé avec succès.";
        } else {
            message = "Attention! Le message n'a pas été supprimé!";
        }
        request.setAttribute(_var_session_message, message);

        resetForm( daf );

        initSessionMessages(request);
        
        return mapping.findForward(IContacts._message);
    }

    public ActionForward modifierMessage(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        DynaActionForm daf = (DynaActionForm) form;
        
        boolean res = modifierMessage( daf );
        
        String message = "";
        if (res) {
            message = "Le message a été modifié avec succès.";
        } else {
            message = "Attention! Le message n'a pas été modifié!";
        }
        request.setAttribute(_var_session_message, message);

        resetForm( daf );

        initSessionMessages(request);
        
        return mapping.findForward(IContacts._message);

    }

    public ActionForward creerMessage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        DynaActionForm daf = (DynaActionForm) form;

        boolean res = creerMessage( daf );
        
        String message = "";
        if (res) {
            message = "Le message a été créé avec succès.";
        } else {
            message = "Attention! Le message n'a pas été créé!";
        }
        request.setAttribute(_var_session_message, message);

        
        initSessionMessages(request);
        
        daf.set(IContacts._struts_method, null);
        
        return mapping.findForward( IContacts._message );

    }

    public ActionForward trierMessages(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (!CrmUtilSession.isSessionActive(request.getSession())) {
            return mapping.findForward(IContacts._expirationSession);
        }

        DynaActionForm daf = (DynaActionForm) form;
        String col_de_tri_messages = (String) daf.get(_texte_generique);

        String sens_tri_messages = (String) request.getSession().getAttribute(
                _var_session_sens_tri_messages);
        if ( IContacts._ASC.equalsIgnoreCase(sens_tri_messages) ) {
            sens_tri_messages = IContacts._DESC;
        } else {
            sens_tri_messages = IContacts._ASC;
        }
        request.getSession().setAttribute(_var_session_sens_tri_messages,
                sens_tri_messages);

        Collection<Message> admin_messages = (Collection<Message>) request
                .getSession().getAttribute(_var_session_admin_messages);
        List<Message> liste = new ArrayList<Message>(admin_messages);
        ComparateurMessage comparateur = new ComparateurMessage(
                sens_tri_messages, col_de_tri_messages);
        Collections.sort(liste, comparateur);
        request.getSession().setAttribute(_var_session_admin_messages, liste);

        return mapping.findForward( IContacts._message );
    }

}