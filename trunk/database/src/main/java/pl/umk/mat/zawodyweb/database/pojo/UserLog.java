package pl.umk.mat.zawodyweb.database.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author lukash2k
 */
@Entity
@Table(name = "userlog", schema = "public")
@SuppressWarnings("serial")
public class UserLog implements Serializable {

    private Integer id;
    private String username;
    private String ip;
    private Timestamp logdate;

    @Basic
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username", length = 64)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "ip", length = 40)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Basic
    @Column(name = "logdate")
    public Timestamp getLogdate() {
        return logdate;
    }

    public void setLogdate(Timestamp logdate) {
        this.logdate = logdate;
    }
}
