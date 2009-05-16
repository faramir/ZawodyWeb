package pl.umk.mat.zawodyweb.www;

/**
 *
 * @author slawek
 */
public class RolesELFunctions {

    public static Boolean canAddContest(RolesBean rolesBean, Integer contestId, Integer seriesId) {
        return rolesBean.canAddContest(contestId, seriesId);
    }

    public static Boolean canEditContest(RolesBean rolesBean, Integer contestId, Integer seriesId) {
        return rolesBean.canEditContest(contestId, seriesId);
    }
}
