package pl.umk.mat.zawodyweb.database.pojo;

import java.util.List;
import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Embeddable;
import javax.persistence.GenerationType;

/**
 * <p>Pojo mapping TABLE public.questions</p>
 *
 * <p>Generated at Fri May 08 19:00:59 CEST 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "questions", schema = "public")
@SuppressWarnings("serial")
public class Questions implements Serializable {

    /**
     * Attribute id.
     */
    private Integer id;
    private String subject;
    /**
     * Attribute question.
     */
    private String question;
    /**
     * Attribute visibility.
     */
    private Integer visibility;
    /**
     * Attribute qtype.
     */
    private Integer qtype;
    /**
     * Attribute users
     */
    private Users users;
    /**
     * Attribute contests
     */
    private Contests contests;
    /**
     * Attribute problems
     */
    private Problems problems;
    /**
     * Attribute question date.
     */
    private Timestamp qdate;
    /**
     * Attribute answer date.
     */
    private Timestamp adate;

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

    /**
     * @return lastname
     */
    @Basic
    @Column(name = "subject", length = 50)
    public String getSubject() {
        return subject;
    }

    /**
     * @param lastname new value for lastname
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return question
     */
    @Basic
    @Column(name = "question", length = 2147483647)
    public String getQuestion() {
        return question;
    }

    /**
     * @param question new value for question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @return visibility
     */
    @Basic
    @Column(name = "visibility")
    public Integer getVisibility() {
        return visibility;
    }

    /**
     * @param visibility new value for visibility
     */
    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    /**
     * @return qtype
     */
    @Basic
    @Column(name = "qtype")
    public Integer getQtype() {
        return qtype;
    }

    /**
     * @param qtype new value for qtype
     */
    public void setQtype(Integer qtype) {
        this.qtype = qtype;
    }

    /**
     * @return qdate
     */
    @Basic
    @Column(name = "qdate")
    public Timestamp getQdate() {
        return qdate;
    }

    /**
     * @param qdate new value of qdate
     */
    public void setQdate(Timestamp qdate) {
        this.qdate = qdate;
    }

    /**
     * @return adate
     */
    @Basic
    @Column(name = "adate")
    public Timestamp getAdate() {
        return adate;
    }

    /**
     * @param adate new value of adate
     */
    public void setAdate(Timestamp adate) {
        this.adate = adate;
    }

    /**
     * get users
     */
    @ManyToOne
    @JoinColumn(name = "usersid")
    public Users getUsers() {
        return this.users;
    }

    /**
     * set users
     */
    public void setUsers(Users users) {
        this.users = users;
    }

    /**
     * get contests
     */
    @ManyToOne
    @JoinColumn(name = "contestsid")
    public Contests getContests() {
        return this.contests;
    }

    /**
     * set contests
     */
    public void setContests(Contests contests) {
        this.contests = contests;
    }

    /**
     * get problems
     */
    @ManyToOne
    @JoinColumn(name = "tasksid")
    public Problems getProblems() {
        return this.problems;
    }

    /**
     * set problems
     */
    public void setProblems(Problems problems) {
        this.problems = problems;
    }
}
