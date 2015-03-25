/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database.pojo;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * <p>
 * Pojo mapping TABLE public.languages</p>
 *
 * <p>
 * Generated at Fri May 08 19:00:59 CEST 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3
 *
 */
@Entity
@Table(name = "languages", schema = "public")
@SuppressWarnings("serial")
public class Languages implements Serializable {

    /**
     * Attribute id.
     */
    private Integer id;
    /**
     * Attribute name.
     */
    private String name;
    /**
     * Attribute extension.
     */
    private String extension;
    /**
     * Config extension.
     */
    private String config;
    /**
     * Attribute classes
     */
    private Classes classes;
    /**
     * List of LanguagesProblems
     */
    private List<LanguagesProblems> languagesProblemss = null;
    /**
     * List of Submits
     */
    private List<Submits> submitss = null;

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
     * @return name
     */
    @Basic
    @Column(name = "name", length = 40)
    public String getName() {
        return name;
    }

    /**
     * @param name new value for name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return extension
     */
    @Basic
    @Column(name = "extension", length = 8)
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension new value for extension
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * @deprecated only for Hibernate. Instead, use loadProperties()
     * @return text
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "config", length = 2147483647)
    public String getConfig() {
        return this.config;
    }

    /**
     * @deprecated only for Hibernate. Instead, use saveProperities()
     * @param config
     */
    public void setConfig(String config) {
        this.config = config;
    }

    public Properties loadProperties() {
        Properties result = new Properties();
        if (config != null) {
            try {
                result.load(new StringReader(config));
            } catch (IOException ex) {
                //This shouldn't happend. Not ever!
                Logger.getLogger(Languages.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * @param text new value for text
     */
    public void saveProperties(Properties properties) {
        if (properties == null) {
            this.config = null;
            return;
        }
        try {
            StringWriter sw = new StringWriter();
            properties.store(sw, new String());
            this.config = sw.toString();
        } catch (IOException ex) {
            //This also shouldn't ever happend.
            Logger.getLogger(Languages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveProperties(String properties) {
        this.config = properties;
    }

    /**
     * get classes
     */
    @ManyToOne
    @JoinColumn(name = "classesid")
    public Classes getClasses() {
        return this.classes;
    }

    /**
     * set classes
     */
    public void setClasses(Classes classes) {
        this.classes = classes;
    }

    /**
     * Get the list of LanguagesProblems
     */
    // languagesProblemsPK
    @OneToMany(mappedBy = "languages")
    public List<LanguagesProblems> getLanguagesProblemss() {
        return this.languagesProblemss;
    }

    /**
     * Set the list of LanguagesProblems
     */
    public void setLanguagesProblemss(List<LanguagesProblems> languagesProblemss) {
        this.languagesProblemss = languagesProblemss;
    }

    /**
     * Get the list of Submits
     */
    // submitsPK
    @OneToMany(mappedBy = "languages")
    public List<Submits> getSubmitss() {
        return this.submitss;
    }

    /**
     * Set the list of Submits
     */
    public void setSubmitss(List<Submits> submitss) {
        this.submitss = submitss;
    }
}
