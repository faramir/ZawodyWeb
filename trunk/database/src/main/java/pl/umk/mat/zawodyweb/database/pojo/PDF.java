package pl.umk.mat.zawodyweb.database.pojo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author lukash2k
 */
@Entity
@Table(name = "pdf", schema = "public")
@SuppressWarnings("serial")
public class PDF implements Serializable {

    private Integer id;
    private byte[] pdf;
    List<Problems> problemss = null;

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
    @Column(name = "pdf")
    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    @OneToMany(mappedBy = "pdf")
    public List<Problems> getProblems() {
        return this.problemss;
    }

    public void setProblems(List<Problems> problems) {
        this.problemss = problems;
    }
}
