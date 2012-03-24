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
                if (sf.getCurrentSession().getTransaction().isActive()) {
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
