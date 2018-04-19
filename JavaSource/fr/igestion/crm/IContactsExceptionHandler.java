package fr.igestion.crm;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

public class IContactsExceptionHandler extends ExceptionHandler {

    private static final Logger LOGGER = Logger
            .getLogger(IContactsExceptionHandler.class);

    public ActionForward execute(Exception ex, ExceptionConfig ae,
            ActionMapping mapping, ActionForm formInstance,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        LOGGER.error(formInstance.getClass().getName(), ex);

        return super.execute(ex, ae, mapping, formInstance, request, response);
    }

}
