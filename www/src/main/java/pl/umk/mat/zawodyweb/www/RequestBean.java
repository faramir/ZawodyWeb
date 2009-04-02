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
    private String captcha;
    
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
     * @return the captcha
     */
    public String getCaptcha() {
        return captcha;
    }

    /**
     * @param captcha the captcha to set
     */
    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }


}
