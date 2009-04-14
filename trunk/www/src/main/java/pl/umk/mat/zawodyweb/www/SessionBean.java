/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.www;

import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Transaction;
import org.restfaces.annotation.HttpAction;
import org.restfaces.annotation.Instance;
import org.restfaces.annotation.Param;
import pl.umk.mat.zawodyweb.database.ContestsDAO;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.UsersDAO;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Contests;
import pl.umk.mat.zawodyweb.database.pojo.Users;

/**
 *
 * @author slawek
 */
@Instance("#{sessionBean}")
public class SessionBean {

    private final ResourceBundle messages = ResourceBundle.getBundle("pl.umk.mat.zawodyweb.www.Messages");
    private Users currentUser = new Users();
    private Contests currentContest;
    private boolean isLoggedIn;
    private Boolean rememberMe;

    /**
     * @return the currentUser
     */
    public Users getCurrentUser() {
        return currentUser;
    }

    /**
     * @return the currentContest
     */
    public Contests getCurrentContest() {
        return currentContest;
    }

    /**
     * @return the isLoggedIn
     */
    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public String logIn() {
        FacesContext context = FacesContext.getCurrentInstance();

        Cookie cookie = new Cookie("login", currentUser.getLogin());
        if (rememberMe) {
            cookie.setMaxAge(999);
        } else {
            cookie.setMaxAge(0);
        }
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.addCookie(cookie);


        Transaction t = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        try {
            UsersDAO dao = DAOFactory.DEFAULT.buildUsersDAO();
            Users user = dao.findByLogin(currentUser.getLogin()).get(0);
            if (user.getPass().equals(currentUser.getPass())) {
                isLoggedIn = true;
                currentUser = user;
            }
            t.commit();
        } catch (Exception e) {
            isLoggedIn = false;
            t.rollback();
        }

        if (!isLoggedIn) {
            String summary = messages.getString("bad_login_data");
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formLogin:login", summary, null);
            return null;
        }

        return "start";
    }

    @HttpAction(name = "contest", pattern = "contest/{id}/{title}")
    public String selectContest(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        Transaction t = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        try {
            ContestsDAO dao = DAOFactory.DEFAULT.buildContestsDAO();
            currentContest = dao.getById(id);
            t.commit();
        } catch (Exception e) {
            //TODO: jakis komunikat wywalic
            t.rollback();
        }

        return "start";
    }

    private Cookie getLoginCookie() {
        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = context.getViewRoot().getViewId();
        if (viewId.equals("/login.jspx")) {
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("login")) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public Boolean getRememberMe() {
        return getLoginCookie() != null;
    }

    public void setRememberMe(Boolean value) {
        rememberMe = value;
    }

    public String getLogin() {
        String result;
        try {
            result = getLoginCookie().getValue();
        } catch (NullPointerException e) {
            result = StringUtils.EMPTY;
        }

        return result;
    }

    public void setLogin(String value) {
        currentUser.setLogin(value);
    }
}
