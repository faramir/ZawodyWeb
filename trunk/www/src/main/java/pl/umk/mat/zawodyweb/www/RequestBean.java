/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.www;

import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Transaction;
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
public class RequestBean {

    private final ResourceBundle messages = ResourceBundle.getBundle("pl.umk.mat.zawodyweb.www.Messages");
    private Users newUser = new Users();
    private String repPasswd;
    private Contests editedContest;

    /**
     * @return the newUser
     */
    public Users getNewUser() {
        return newUser;
    }

    /**
     * @return the repPasswd
     */
    public String getRepPasswd() {
        return repPasswd;
    }

    /**
     * @param repPasswd the repPasswd to set
     */
    public void setRepPasswd(String repPasswd) {
        this.repPasswd = repPasswd;
    }

    /**
     * @return the editedContest
     */
    public Contests getEditedContest() {
        FacesContext context = FacesContext.getCurrentInstance();
        int contestId = 0;
        try {
            contestId = Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("id"));
        } catch (Exception e) {
            contestId = 0;
        }

        if (editedContest != null) {
            return editedContest;
        }

        if (contestId == 0) {
            editedContest = new Contests();
        } else {
            Transaction t = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            ContestsDAO dao = DAOFactory.DEFAULT.buildContestsDAO();
            editedContest = dao.getById(contestId);
            t.commit();
        }

        return editedContest;
    }

    public String registerUser() {
        Transaction t = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        try {
            UsersDAO dao = DAOFactory.DEFAULT.buildUsersDAO();
            dao.save(newUser);
            t.commit();
        } catch (Exception e) {
            t.rollback();
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = messages.getString("login_exists");
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formRegister:login", summary, null);
            newUser.setPass(StringUtils.EMPTY);
            return null;
        }

        return "login";
    }

    public String saveContest() {
        Transaction t = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        try {
            ContestsDAO dao = DAOFactory.DEFAULT.buildContestsDAO();
            if(editedContest.getId() == 0)
                editedContest.setId(null);
            
            dao.saveOrUpdate(editedContest);
            t.commit();
        } catch (Exception e) {
            t.rollback();
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"),  e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditContest:save", summary, null);
            return null;
        }

        return "start";
    }

    /**
     * Validates component with captcha text entered.
     *
     * @param context
     *            faces context in which component resides
     * @param component
     *            component to be validated
     * @param obj
     *            data entered in component
     */
    public void validateCaptcha(FacesContext context, UIComponent component, Object obj) {
        String captcha = (String) obj;
        ExternalContext extContext = context.getExternalContext();
        String tmp = (String) extContext.getSessionMap().get("captchaKey");

        if (!captcha.toLowerCase().equals(tmp.toLowerCase())) {
            ((HtmlInputText) component).setValid(false);
            String summary = messages.getString("bad_captcha");
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, component, summary, null);
        }
    }
}
