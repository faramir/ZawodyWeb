/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.www;

import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.UsersDAO;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Users;

/**
 *
 * @author slawek
 */
public class RequestBean {

    private Users newUser = new Users();
    private String repPasswd;
    private String captchaSessionKey;
    
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

    public String registerUser() {
        Transaction t = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        try{
            UsersDAO dao = DAOFactory.DEFAULT.buildUsersDAO();
            dao.save(newUser);
            t.commit();
        }catch(Exception e){
            t.rollback();
            return null;
        }

        return "login";
    }

    /**
     * @return the captchaSessionKey
     */
    public String getCaptchaSessionKey() {
        return captchaSessionKey;
    }

    /**
     * @param captchaSessionKey the captchaSessionKey to set
     */
    public void setCaptchaSessionKey(String captchaSessionKey) {
        this.captchaSessionKey = captchaSessionKey;
    }
}
