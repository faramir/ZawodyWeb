package pl.umk.mat.zawodyweb.www;

import java.util.List;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.UsersRolesDAO;
import pl.umk.mat.zawodyweb.database.pojo.Contests;
import pl.umk.mat.zawodyweb.database.pojo.Roles;
import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.database.pojo.UsersRoles;

/**
 *
 * @author slawek
 */
public class RolesBean {

    private SessionBean sessionBean;
    private List<UsersRoles> userRoles = null;
    private Boolean rolesRead = false;

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    private List<UsersRoles> getUserRoles() {
        if (!rolesRead) {
            UsersRolesDAO dao = DAOFactory.DEFAULT.buildUsersRolesDAO();
            if (sessionBean.getCurrentUser() != null) {
                userRoles = dao.findByUsersid(sessionBean.getCurrentUser().getId());
            }

            rolesRead = true;
        }

        return userRoles;
    }

    private Boolean isOk(Roles role, Integer contestId, Integer seriesId) {
        return (role.getContests() == null || role.getContests().getId().equals(contestId)) &&
                (role.getSeries() == null || role.getSeries().getId().equals(seriesId));
    }

    public Boolean canEditUsers() {
        if (getUserRoles() == null) {
            return false;
        }

        for (UsersRoles ur : getUserRoles()) {
            if (ur.getRoles().getEdituser()) {
                return true;
            }
        }

        return false;
    }

    public Boolean canRateAnySeries(Contests contest) {
        if (contest == null) {
            return false;
        }

        for (Series s : contest.getSeriess()) {
            if (canRate(contest.getId(), s.getId())) {
                return true;
            }
        }
        return false;
    }

    public Boolean canAddContest(Integer contestId, Integer seriesId) {
        if (getUserRoles() == null) {
            return false;
        }

        for (UsersRoles ur : getUserRoles()) {
            Roles role = ur.getRoles();
            if (isOk(role, contestId, seriesId) && role.getAddcontest()) {
                return true;
            }
        }

        return false;
    }

    public Boolean canEditContest(Integer contestId, Integer seriesId) {
        if (getUserRoles() == null) {
            return false;
        }

        for (UsersRoles ur : getUserRoles()) {
            Roles role = ur.getRoles();
            if (isOk(role, contestId, seriesId) && role.getEditcontest()) {
                return true;
            }
        }

        return false;
    }

    public Boolean canDeleteContest(Integer contestId, Integer seriesId) {
        if (getUserRoles() == null) {
            return false;
        }

        for (UsersRoles ur : getUserRoles()) {
            Roles role = ur.getRoles();
            if (isOk(role, contestId, seriesId) && role.getDelcontest()) {
                return true;
            }
        }

        return false;
    }

    public Boolean canAddSeries(Integer contestId, Integer seriesId) {
        if (getUserRoles() == null) {
            return false;
        }

        for (UsersRoles ur : getUserRoles()) {
            Roles role = ur.getRoles();
            if (isOk(role, contestId, seriesId) && role.getAddseries()) {
                return true;
            }
        }

        return false;
    }

    public Boolean canEditSeries(Integer contestId, Integer seriesId) {
        if (getUserRoles() == null) {
            return false;
        }

        for (UsersRoles ur : getUserRoles()) {
            Roles role = ur.getRoles();
            if (isOk(role, contestId, seriesId) && role.getEditseries()) {
                return true;
            }
        }

        return false;
    }

    public Boolean canDeleteSeries(Integer contestId, Integer seriesId) {
        if (getUserRoles() == null) {
            return false;
        }

        for (UsersRoles ur : getUserRoles()) {
            Roles role = ur.getRoles();
            if (isOk(role, contestId, seriesId) && role.getDelseries()) {
                return true;
            }
        }

        return false;
    }

    public Boolean canAddProblem(Integer contestId, Integer seriesId) {
        if (getUserRoles() == null) {
            return false;
        }

        for (UsersRoles ur : getUserRoles()) {
            Roles role = ur.getRoles();
            if (isOk(role, contestId, seriesId) && role.getAddproblem()) {
                return true;
            }
        }

        return false;
    }

    public Boolean canEditProblem(Integer contestId, Integer seriesId) {
        if (getUserRoles() == null) {
            return false;
        }

        for (UsersRoles ur : getUserRoles()) {
            Roles role = ur.getRoles();
            if (isOk(role, contestId, seriesId) && role.getEditproblem()) {
                return true;
            }
        }

        return false;
    }

    public Boolean canDeleteProblem(Integer contestId, Integer seriesId) {
        if (getUserRoles() == null) {
            return false;
        }

        for (UsersRoles ur : getUserRoles()) {
            Roles role = ur.getRoles();
            if (isOk(role, contestId, seriesId) && role.getDelproblem()) {
                return true;
            }
        }

        return false;
    }

    public Boolean canRate(Integer contestId, Integer seriesId) {
        if (getUserRoles() == null) {
            return false;
        }

        for (UsersRoles ur : getUserRoles()) {
            Roles role = ur.getRoles();
            if (isOk(role, contestId, seriesId) && role.getCanrate()) {
                return true;
            }
        }

        return false;
    }

    public Boolean isContestant(Integer contestId, Integer seriesId) {
        if (getUserRoles() == null) {
            return false;
        }

        for (UsersRoles ur : getUserRoles()) {
            Roles role = ur.getRoles();
            if (isOk(role, contestId, seriesId) && role.getContestant()) {
                return true;
            }
        }

        return false;
    }

    public Boolean canEditAnyProblem() {
        if (getUserRoles() == null) {
            return false;
        }

        for (UsersRoles ur : getUserRoles()) {
            if (ur.getRoles().getEditproblem()) {
                return true;
            }
        }
        return false;
    }
}
