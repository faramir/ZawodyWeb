/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.www;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;

public class HibernateSessionRequestFilter implements Filter {

    private SessionFactory sf;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            if (req.getContextPath().contains("/error/")) {
                chain.doFilter(request, response);
                return;
            }
        }

        try {
            sf.getCurrentSession().beginTransaction();
            chain.doFilter(request, response);

            sf.getCurrentSession().getTransaction().commit();
        } catch (StaleObjectStateException staleEx) {
            throw staleEx;
        } catch (Throwable ex) {
            try {
                TransactionStatus status = sf.getCurrentSession().getTransaction().getStatus();
                if (status.isOneOf(TransactionStatus.ACTIVE) && status.canRollback()) {
                    sf.getCurrentSession().getTransaction().rollback();
                }
            } catch (Throwable rbEx) {
            }
            throw new ServletException(ex);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        sf = HibernateUtil.getSessionFactory();
    }

    @Override
    public void destroy() {
    }
}
