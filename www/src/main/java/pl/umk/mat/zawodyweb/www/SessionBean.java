/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.www;

import java.util.List;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.openid4java.discovery.Identifier;
import org.restfaces.annotation.HttpAction;
import org.restfaces.annotation.Instance;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.UsersDAO;
import pl.umk.mat.zawodyweb.database.pojo.Users;
import pl.umk.mat.zawodyweb.olat.User;
import pl.umk.mat.zawodyweb.olat.jdbc.Connector;

/**
 *
 * @author slawek
 */
@Instance("#{sessionBean}")
public class SessionBean {

    private final ResourceBundle messages = ResourceBundle.getBundle("pl.umk.mat.zawodyweb.www.Messages");
    private Users currentUser = new Users();
    private Integer currentContestId;
    private boolean loggedIn;
    private Boolean rememberMe;
    /* */
    private boolean showOnlyMySubmissions = true;
    private int submissionsContestId = 0;
    private int submissionsPageIndex = 0;
    private int submissionsUserId = 0;
    private int submissionsProblemId = 0;
    private int submissionsSeriesId = 0;
    private long submissionsLastVisit = 0;
    private OpenIdConsumer openIdConsumer;

    /**
     * @return the currentUser
     */
    public Users getCurrentUser() {
        return currentUser;
    }

    public Integer getCurrentContestId() {
        return currentContestId;
    }

    public void setCurrentContestId(Integer id) {
        currentContestId = id;
    }

    /**
     * @return the isLoggedIn
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Redirect here when using OpenID functionality
     * @return
     */
    @HttpAction(name = "openid", pattern = "openid")
    public String openIdResponse() {
        FacesContext context = FacesContext.getCurrentInstance();
        Identifier identifier = openIdConsumer.verifyResponse((HttpServletRequest) context.getExternalContext().getRequest());
        if (identifier == null) {
            String summary = messages.getString("bad_login_data");
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "login", summary, null);
            return "login";
        } else {
            /* Login using OpenID successful */
            UsersDAO dao = DAOFactory.DEFAULT.buildUsersDAO();
            List<Users> users = dao.findByLogin(openIdConsumer.getLogin());
            Users user = null;
            if (users.isEmpty()) {
                user = new Users();
            } else {
                user = users.get(0);
            }

            user.setLogin(openIdConsumer.getLogin());
            user.setFirstname(openIdConsumer.getFirstname());
            user.setLastname(openIdConsumer.getLastname());
            user.setEmail(openIdConsumer.getEmail());
            user.setPass("OPENID");

            dao.saveOrUpdate(user);

            currentUser = user;
            loggedIn = true;
        }

        return "start";
    }

    /**
     * Inserts or updates user using OLAT data
     * @param dao
     * @param user
     * @param username
     * @return
     */
    public Users olatSaveUser(UsersDAO dao, Users user, String username) {
        User olatUser = Connector.getInstance().getUser(currentUser.getLogin());

        user.setLogin(olatUser.getLogin());
        user.setFirstname(olatUser.getFirstname());
        user.setLastname(olatUser.getLastname());
        user.setEmail(olatUser.getEmail());
        user.setPass("OLAT");

        dao.saveOrUpdate(user);

        return user;
    }

    public String logIn() {
        FacesContext context = FacesContext.getCurrentInstance();

        Cookie cookie = new Cookie("login", currentUser.getLogin());
        if (rememberMe) {
            cookie.setMaxAge(60 * 60 * 24 * 30);
        } else {
            cookie.setMaxAge(0);
        }
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.addCookie(cookie);

        try {
            UsersDAO dao = DAOFactory.DEFAULT.buildUsersDAO();
            List<Users> users = dao.findByLogin(currentUser.getLogin());
            if (users.isEmpty() == false) {
                if ("OLAT".equals(users.get(0).getPass())) {
                    if (Connector.getInstance().checkPassword(currentUser.getLogin(), currentUser.getPass())) {
                        currentUser = olatSaveUser(dao, users.get(0), currentUser.getLogin());
                        loggedIn = true;
                    }
                } else if ("OPENID".equals(users.get(0).getPass())) {
                    String contextPath = ((HttpServletRequest) context.getExternalContext().getRequest()).getRequestURL().toString();
                    contextPath = contextPath.replaceFirst(context.getExternalContext().getRequestServletPath() + ".*$", "");
                    openIdConsumer = new OpenIdConsumer(contextPath + "/openid.html");
                    if (openIdConsumer.authorizationRequest(currentUser.getLogin(), (HttpServletRequest) context.getExternalContext().getRequest(), response) == true) {
                        return null;
                    } else {
                        loggedIn = false;
                    }
                } else if (users.get(0).checkPass(currentUser.getPass())) {
                    currentUser = users.get(0);
                    loggedIn = true;
                }
            } else {
                if (currentUser.getPass() != null && Connector.getInstance().checkPassword(currentUser.getLogin(), currentUser.getPass())) {
                    currentUser = olatSaveUser(dao, new Users(), currentUser.getLogin());
                    loggedIn = true;
                } else {
                    String contextPath = ((HttpServletRequest) context.getExternalContext().getRequest()).getRequestURL().toString();
                    contextPath = contextPath.replaceFirst(context.getExternalContext().getRequestServletPath() + ".*$", "");
                    openIdConsumer = new OpenIdConsumer(contextPath + "/openid.html");
                    if (openIdConsumer.authorizationRequest(currentUser.getLogin(), (HttpServletRequest) context.getExternalContext().getRequest(), response) == true) {
                        return null;
                    } else {
                        loggedIn = false;
                    }
                }
            }
        } catch (Exception e) {
            loggedIn = false;
        }

        if (!loggedIn) {
            String summary = messages.getString("bad_login_data");
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "login", summary, null);
            return null;
        }

        return "start";
    }

    @HttpAction(name = "logout", pattern = "logout")
    public String logOut() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();

        currentUser = new Users();
        loggedIn = false;

        return "start";
    }

    private Cookie getLoginCookie() {
        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = context.getViewRoot().getViewId();
        if (viewId.equals("/login.jspx")) {
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("login")) {
                        return cookie;
                    }
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

    public boolean isShowOnlyMySubmissions() {
        return showOnlyMySubmissions;
    }

    public void setShowOnlyMySubmissions(boolean showOnlyMySubmissions) {
        if (showOnlyMySubmissions != this.showOnlyMySubmissions) {
            this.showOnlyMySubmissions = showOnlyMySubmissions;
            setSubmissionsPageIndex(0);
        }
    }

    /**
     * @return the submissionsContestId
     */
    public int getSubmissionsContestId() {
        return submissionsContestId;
    }

    /**
     * @return the submissionsPageIndex
     */
    public int getSubmissionsPageIndex() {
        return submissionsPageIndex;
    }

    /**
     * @param submissionsContestId the submissionsContestId to set
     */
    public void setSubmissionsContestId(int submissionsContestId) {
        this.submissionsContestId = submissionsContestId;
    }

    /**
     * @param submissionsPageIndex the submissionsPageIndex to set
     */
    public void setSubmissionsPageIndex(int submissionsPageIndex) {
        if (submissionsPageIndex < 0) {
            submissionsPageIndex = 0;
        }
        this.submissionsPageIndex = submissionsPageIndex;
    }

    /**
     * @return the submissionsLastVisit
     */
    public long getSubmissionsLastVisit() {
        return submissionsLastVisit;
    }

    /**
     * @param submissionsLastVisit the submissionsLastVisit to set
     */
    public void setSubmissionsLastVisit(long submissionsLastVisit) {
        this.submissionsLastVisit = submissionsLastVisit;
    }

    /**
     * @return the submissionsUserId
     */
    public int getSubmissionsUserId() {
        return submissionsUserId;
    }

    /**
     * @param submissionsUserId the submissionsUserId to set
     */
    public void setSubmissionsUserId(int submissionsUserId) {
        this.submissionsUserId = submissionsUserId;
    }

    /**
     * @return the submissionsProblemId
     */
    public int getSubmissionsProblemId() {
        return submissionsProblemId;
    }

    /**
     * @param submissionsProblemId the submissionsProblemId to set
     */
    public void setSubmissionsProblemId(int submissionsProblemId) {
        this.submissionsProblemId = submissionsProblemId;
    }

    /**
     * @return the submissionsSeriesId
     */
    public int getSubmissionsSeriesId() {
        return submissionsSeriesId;
    }

    /**
     * @param submissionsSeriesId the submissionsSeriesId to set
     */
    public void setSubmissionsSeriesId(int submissionsSeriesId) {
        this.submissionsSeriesId = submissionsSeriesId;
    }
}
