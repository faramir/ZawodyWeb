package pl.umk.mat.zawodyweb.www;

import com.sun.facelets.FaceletViewHandler;
import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$ Date: $Date$
 */
public class FaceletErrorHandler extends FaceletViewHandler {

    private static Log logger = LogFactory.getLog(FaceletErrorHandler.class);

    public FaceletErrorHandler(ViewHandler handler) {
        super(handler);
    }

    @Override
    protected void handleRenderException(FacesContext context, Exception ex) throws IOException, FacesException {
        try {
            logger.error("Handled RenderException:", ex);

            if (context.getExternalContext().getResponse() instanceof HttpServletResponse) {
                ((HttpServletResponse) context.getExternalContext().getResponse()).sendRedirect(context.getExternalContext().getRequestContextPath()+"/error500.jsp");
            }
        } catch (IOException e) {
            logger.fatal("Could not process redirect to handle application error", e);
        }
    }
}
