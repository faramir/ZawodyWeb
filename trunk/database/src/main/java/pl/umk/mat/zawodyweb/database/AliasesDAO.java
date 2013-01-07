package pl.umk.mat.zawodyweb.database;

import java.util.List;
import pl.umk.mat.zawodyweb.database.pojo.Aliases;

import pl.umk.mat.zawodyweb.database.pojo.Classes;

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