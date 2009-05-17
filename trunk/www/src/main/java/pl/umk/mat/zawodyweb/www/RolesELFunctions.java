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

    public static Boolean canDeleteContest(RolesBean rolesBean, Integer contestId, Integer seriesId) {
        return rolesBean.canDeleteContest(contestId, seriesId);
    }

    public static Boolean canAddSeries(RolesBean rolesBean, Integer contestId, Integer seriesId) {
        return rolesBean.canAddSeries(contestId, seriesId);
    }

    public static Boolean canEditSeries(RolesBean rolesBean, Integer contestId, Integer seriesId) {
        return rolesBean.canEditSeries(contestId, seriesId);
    }

    public static Boolean canDeleteSeries(RolesBean rolesBean, Integer contestId, Integer seriesId) {
        return rolesBean.canDeleteSeries(contestId, seriesId);
    }

    public static Boolean canAddProblem(RolesBean rolesBean, Integer contestId, Integer seriesId) {
        return rolesBean.canAddProblem(contestId, seriesId);
    }

    public static Boolean canEditProblem(RolesBean rolesBean, Integer contestId, Integer seriesId) {
        return rolesBean.canEditProblem(contestId, seriesId);
    }

    public static Boolean canDeleteProblem(RolesBean rolesBean, Integer contestId, Integer seriesId) {
        return rolesBean.canDeleteProblem(contestId, seriesId);
    }

    public static Boolean canRate(RolesBean rolesBean, Integer contestId, Integer seriesId) {
        return rolesBean.canRate(contestId, seriesId);
    }

    public static Boolean isContestant(RolesBean rolesBean, Integer contestId, Integer seriesId) {
        return rolesBean.isContestant(contestId, seriesId);
    }
}
