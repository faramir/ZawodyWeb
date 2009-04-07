/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.www;

import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.UsersDAO;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Users;

/**
 *
 * @author slawek
 */
public class SessionBean {

    private final ResourceBundle messages = ResourceBundle.getBundle("pl.umk.mat.zawodyweb.www.Messages");
    private Users currentUser = new Users();
    private boolean isLoggedIn;

    /**
     * @return the currentUser
     */
    public Users getCurrentUser() {
        return currentUser;
    }

    /**
     * @return the isLoggedIn
     */
    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public String logIn() {
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
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = messages.getString("bad_login_data");
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formLogin:login", summary, null);
            return null;
        }

        return "index";
    }
}
