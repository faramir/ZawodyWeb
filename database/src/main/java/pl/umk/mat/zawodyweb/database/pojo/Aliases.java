package pl.umk.mat.zawodyweb.database.pojo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>Pojo mapping TABLE public.aliases</p>
 *
 * @author faramir
 *
 */
@Entity
@Table(name = "aliases", schema = "public")
@SuppressWarnings("serial")
public class Aliases implements Serializable {

    /**
     * Attribute id.
     */
    private Integer id;
    /**
     * Attribute filename.
     */
    private String name;
    /**
     * Attribute description.
     */
    private String ips;
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
     * @return filename
     */
    @Basic
    @Column(name = "name", length = 64)
    public String getName() {
        return name;
    }

    /**
     * @param name new value for filename
     */
    public void setName(String name) {
        this.name = name;
    }

    /* liste transiente */
    /**
     * @return ips
     */
    @Basic
    @Column(name = "ips", length = 2147483647)
    public String getIps() {
        return ips;
    }

    /**
     * @param ips new value for ips
     */
    public void setIps(String ips) {
        this.ips = ips;
    }
}