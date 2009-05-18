package pl.umk.mat.zawodyweb.www;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * This class contains helpful methods that are often used in beans
 */
public class WWWHelper {

    /**
     * Adds faces message to the given context.
     *
     * @param context
     *            Faces context
     * @param severity
     *            Type of message
     * @param comp
     *            Component for which message should be added
     * @param summary
     *            Messages summary
     * @param detail
     *            Messages detail
     */
    public static void AddMessage(FacesContext context, Severity severity, UIComponent comp, String summary,
            String detail) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        context.addMessage(comp.getClientId(context), message);
    }

    /**
     * Adds faces message to the given context.
     *
     * @param context
     *            Faces context
     * @param severity
     *            Type of message
     * @param clientId
     *            Client id of comonent for which message should be added
     * @param summary
     *            Messages summary
     * @param detail
     *            Messages detail
     */
    public static void AddMessage(FacesContext context, Severity severity, String clientId, String summary,
            String detail) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        context.addMessage(clientId, message);
    }

    public static Boolean isPost(FacesContext context){
        HttpServletRequest hsr = (HttpServletRequest) context.getExternalContext().getRequest();
        return hsr.getMethod().equals("POST");
    }
}
