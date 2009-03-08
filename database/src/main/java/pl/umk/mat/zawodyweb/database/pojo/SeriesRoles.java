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

/**
 * <p>Pojo mapping TABLE public.series_roles</p>
 *
 * <p>Generated at Sun Mar 08 19:45:31 CET 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "series_roles", schema = "public")
@SuppressWarnings("serial")
public class SeriesRoles implements Serializable {

	/**
	 * Attribute id.
	 */
	private Integer id;
	
	/**
	 * Attribute series
	 */
	 private Series series;	

	/**
	 * Attribute roles
	 */
	 private Roles roles;	

	
	/**
	 * @return id
	 */
	@Basic
	@Id
	@GeneratedValue
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
	 * get series
	 */
	@ManyToOne
	@JoinColumn(name = "seriesid")
	public Series getSeries() {
		return this.series;
	}
	
	/**
	 * set series
	 */
	public void setSeries(Series series) {
		this.series = series;
	}

	/**
	 * get roles
	 */
	@ManyToOne
	@JoinColumn(name = "rolesid")
	public Roles getRoles() {
		return this.roles;
	}
	
	/**
	 * set roles
	 */
	public void setRoles(Roles roles) {
		this.roles = roles;
	}



}