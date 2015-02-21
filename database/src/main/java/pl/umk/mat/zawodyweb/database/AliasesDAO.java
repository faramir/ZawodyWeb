/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database;

import java.util.List;
import pl.umk.mat.zawodyweb.database.pojo.Aliases;

/**
 *
 * @author faramir
 * @see http://www.hibernate.org/328.html
 */
public interface AliasesDAO extends GenericDAO<Aliases, Integer> {

    /*
     * TODO : Add specific businesses daos here.
     * These methods will be overwrited if you re-generate this interface.
     * You might want to extend this interface and to change the dao factory to return
     * an instance of the new implemenation in buildClassesDAO()
     */
    /**
     * Find Classes by filename
     */
    public List<Aliases> findByName(String name);
}
