/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database.pojo;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.codec.binary.Hex;

/**
 * <p>Pojo mapping TABLE public.users</p>
 *
 * <p>Generated at Fri May 08 19:00:59 CEST 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 *
 */
@Entity
@Table(name = "users", schema = "public")
@SuppressWarnings("serial")
public class Users implements Serializable {

    /**
     * Attribute id.
     */
    private Integer id;
    /**
     * Attribute firstname.
     */
    private String firstname;
    /**
     * Attribute lastname.
     */
    private String lastname;
    /**
     * Attribute email.
     */
    private String email;
    /**
     * Attribute birthdate.
     */
    private Date birthdate;
    /**
     * Attribute login.
     */
    private String login;
    /**
     * Attribute pass.
     */
    private String pass;
    /**
     * Attribute address.
     */
    private String address;
    /**
     * Attribute school.
     */
    private String school;
    /**
     * Attribute tutor.
     */
    private String tutor;
    /**
     * Attribute type.
     */
    private String schooltype;
    /**
     * Attribute emailnotification.
     */
    private Integer emailnotification;
    /**
     * Attribute login date.
     */
    private Timestamp rdate;
    /**
     * Attribute login date.
     */
    private Timestamp ldate;
    /**
     * Attribute login date.
     */
    private Timestamp fdate;
    private Boolean onlylogin = false;
    /**
     * List of Questions
     */
    private List<Questions> questionss = null;
    /**
     * List of UsersRoles
     */
    private List<UsersRoles> usersRoless = null;
    /**
     * List of Submits
     */
    private List<Submits> submitss = null;

    /* liste transiente */
    /**
     * @return id
     */
    @Basic
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    /**
     * @param id new value for id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /* liste transiente */
    /**
     * @return firstname
     */
    @Basic
    @Column(name = "firstname", length = 40)
    public String getFirstname() {
        if (firstname != null) {
            return firstname.trim();
        } else {
            return firstname;
        }
    }

    /**
     * @param firstname new value for firstname
     */
    public void setFirstname(String firstname) {
        if (firstname != null) {
            this.firstname = firstname.trim();
        } else {
            this.firstname = firstname;
        }
    }

    /* liste transiente */
    /**
     * @return lastname
     */
    @Basic
    @Column(name = "lastname", length = 40)
    public String getLastname() {
        if (lastname != null) {
            return lastname.trim();
        } else {
            return lastname;
        }
    }

    /**
     * @param lastname new value for lastname
     */
    public void setLastname(String lastname) {
        if (lastname != null) {
            this.lastname = lastname.trim();
        } else {
            this.lastname = lastname;
        }
    }

    /* liste transiente */
    /**
     * @return email
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "email", length = 40)
    public String getEmail() {
        return email;
    }

    /**
     * @param email new value for email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /* liste transiente */
    /**
     * @return birthTimestamp
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "birthdate")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getBirthdate() {
        return birthdate;
    }

    /**
     * @param birthdate new value for birthdate
     */
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    /* liste transiente */
    /**
     * @return login
     */
    @Basic
    @Column(name = "login", length = 64)
    public String getLogin() {
        if (login != null) {
            return login.trim().toLowerCase();
        } else {
            return login;
        }
    }

    /**
     * @param login new value for login
     */
    public void setLogin(String login) {
        if (login != null) {
            this.login = login.trim().toLowerCase();
        } else {
            this.login = login;
        }
    }

    /* liste transiente */
    /**
     * For comparison between plaintext and database saved password use
     * checkPass(String passToCheck)
     * @return pass hash of the password (<b>NOT</b> plaintext password)
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "pass", length = 40)
    public String getPass() {
        return pass;
    }

    public String hashPass(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update((login + "+" + password).getBytes());
            byte[] digest = md.digest();
            org.apache.commons.codec.binary.Hex hex = new Hex();
            return new String(hex.encode(digest));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean checkPass(String passToCheck) {
        return hashPass(passToCheck).equals(pass);
    }

    /**
     * @deprecated Only for Hibernate -- instead, use savePass(String pass)
     * @param pass new value for pass
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    public void savePass(String pass) {
        this.pass = hashPass(pass);
    }

    /* liste transiente */
    /**
     * @return address
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "address", length = 80)
    public String getAddress() {
        return address;
    }

    /**
     * @param address new value for address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /* liste transiente */
    /**
     * @return school
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "school", length = 80)
    public String getSchool() {
        return school;
    }

    /**
     * @param school new value for school
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /* liste transiente */
    /**
     * @return tutor
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "tutor", length = 2147483647)
    public String getTutor() {
        return tutor;
    }

    /**
     * @param tutor new value for tutor
     */
    public void setTutor(String tutor) {
        this.tutor = tutor;
    }

    /* liste transiente */
    /**
     * @return tutor
     */
    @Basic
    @Column(name = "schooltype", length = 16)
    public String getSchooltype() {
        return schooltype;
    }

    /**
     * @param tutor new value for tutor
     */
    public void setSchooltype(String schooltype) {
        this.schooltype = schooltype;
    }

    /* liste transiente */
    /**
     * @return emailnotification
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "emailnotification")
    public Integer getEmailnotification() {
        return emailnotification;
    }

    /**
     * @param emailnotification new value for emailnotification
     */
    public void setEmailnotification(Integer emailnotification) {
        this.emailnotification = emailnotification;
    }

    /**
     * @return adate
     */
    @Basic
    @Column(name = "rdate")
    public Timestamp getRdate() {
        return rdate;
    }

    /**
     * @param adate new value of adate
     */
    public void setRdate(Timestamp rdate) {
        this.rdate = rdate;
    }

    /**
     * @return adate
     */
    @Basic
    @Column(name = "ldate")
    public Timestamp getLdate() {
        return ldate;
    }

    /**
     * @return adate
     */
    @Basic
    @Column(name = "fdate")
    public Timestamp getFdate() {
        return fdate;
    }

    /**
     * @param adate new value of adate
     */
    public void setFdate(Timestamp fdate) {
        this.fdate = fdate;
    }

    /**
     * @param adate new value of adate
     */
    public void setLdate(Timestamp ldate) {
        this.ldate = ldate;
    }


        /**
     * @return viewPDF
     */
    @Basic
    @Column(name = "onlylogin")
    public Boolean getOnlylogin() {
        return onlylogin;
    }

    /**
     * @param viewpdf new value for viewpdf
     */
    public void setOnlylogin(Boolean onlylogin) {
        this.onlylogin = onlylogin;
    }

    /**
     * Get the list of Questions
     */
    // questionsPK
    @OneToMany(mappedBy = "users")
    public List<Questions> getQuestionss() {
        return this.questionss;
    }

    /**
     * Set the list of Questions
     */
    public void setQuestionss(List<Questions> questionss) {
        this.questionss = questionss;
    }

    /**
     * Get the list of UsersRoles
     */
    // usersRolesPK
    @OneToMany(mappedBy = "users")
    public List<UsersRoles> getUsersRoless() {
        return this.usersRoless;
    }

    /**
     * Get the list of Submits
     */
    // submitsPK
    @OneToMany(mappedBy = "users")
    public List<Submits> getSubmitss() {
        return this.submitss;
    }

    /**
     * Set the list of Submits
     */
    public void setSubmitss(List<Submits> submitss) {
        this.submitss = submitss;
    }

    /**
     * Set the list of UsersRoles
     */
    public void setUsersRoless(List<UsersRoles> usersRoless) {
        this.usersRoless = usersRoless;
    }
}
