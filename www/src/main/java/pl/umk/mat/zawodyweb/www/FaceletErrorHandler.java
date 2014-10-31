/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.www;

import com.sun.facelets.FaceletViewHandler;
import java.io.IOException;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
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
                ((HttpServletResponse) context.getExternalContext().getResponse()).sendRedirect(context.getExternalContext().getRequestContextPath() + "/error500.jsp");
            }
        } catch (IOException e) {
            logger.fatal("Could not process redirect to handle application error", e);
        }
    }

    @Override
    public String getActionURL(FacesContext context, String viewId) {
        String result = super.getActionURL(context, viewId);

        if (result.contains("#{")) {
            ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
            ValueExpression valueExpression = expressionFactory.createValueExpression(context.getELContext(), viewId, String.class);
            ExternalContext externalContext = context.getExternalContext();
            String contextPath = externalContext.getRequestContextPath();
            result = contextPath + ((String) valueExpression.getValue(context.getELContext()));
        }

        return result;
    }
}
