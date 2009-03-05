/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.umk.mat.zawodyweb.www;

import pl.umk.mat.zawodyweb.database.pojo.Users;

/**
 *
 * @author slawek
 */
public class RequestBean {
    private Users newUser = new Users();

    private String repPasswd;
    
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
}
