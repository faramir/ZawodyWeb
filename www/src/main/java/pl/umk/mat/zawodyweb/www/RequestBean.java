/*
 * Copyright (c) 2009-2015, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.www;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.custom.datascroller.ScrollerActionEvent;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.Criteria;
import org.hibernate.JDBCException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.restfaces.annotation.HttpAction;
import org.restfaces.annotation.Instance;
import org.restfaces.annotation.Param;
import pl.umk.mat.zawodyweb.database.*;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.*;
import pl.umk.mat.zawodyweb.email.EmailSender;
import pl.umk.mat.zawodyweb.pdf.PdfToImage;
import pl.umk.mat.zawodyweb.www.datamodels.PagedDataModel;
import pl.umk.mat.zawodyweb.www.ranking.Ranking;
import pl.umk.mat.zawodyweb.www.ranking.RankingTable;
import pl.umk.mat.zawodyweb.www.util.ContestsUtils;
import pl.umk.mat.zawodyweb.www.util.ProblemsUtils;
import pl.umk.mat.zawodyweb.www.util.SeriesUtils;
import pl.umk.mat.zawodyweb.www.util.SubmitsUtils;
import pl.umk.mat.zawodyweb.www.zip.ZipFile;
import pl.umk.mat.zawodyweb.www.zip.ZipSolutions;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author slawek
 * @author faramir
 */
@Instance("#{requestBean}")
public class RequestBean {

    private static final Logger logger = Logger.getLogger(RequestBean.class);
    private static final ResourceBundle messages = ResourceBundle.getBundle("pl.umk.mat.zawodyweb.www.Messages");
    private SubmitsDAO submitsDAO = DAOFactory.DEFAULT.buildSubmitsDAO();
    private AliasesDAO aliasesDAO = DAOFactory.DEFAULT.buildAliasesDAO();
    private ClassesDAO classesDAO = DAOFactory.DEFAULT.buildClassesDAO();
    private LanguagesDAO languagesDAO = DAOFactory.DEFAULT.buildLanguagesDAO();
    private LanguagesProblemsDAO languagesProblemsDAO = DAOFactory.DEFAULT.buildLanguagesProblemsDAO();
    private SeriesDAO seriesDAO = DAOFactory.DEFAULT.buildSeriesDAO();
    private ProblemsDAO problemsDAO = DAOFactory.DEFAULT.buildProblemsDAO();
    private QuestionsDAO questionsDAO = DAOFactory.DEFAULT.buildQuestionsDAO();
    private ContestsDAO contestsDAO = DAOFactory.DEFAULT.buildContestsDAO();
    private ResultsDAO resultsDAO = DAOFactory.DEFAULT.buildResultsDAO();
    private TestsDAO testsDAO = DAOFactory.DEFAULT.buildTestsDAO();
    private UsersDAO usersDAO = DAOFactory.DEFAULT.buildUsersDAO();
    private RolesDAO rolesDAO = DAOFactory.DEFAULT.buildRolesDAO();
    private UsersRolesDAO usersRolesDAO = DAOFactory.DEFAULT.buildUsersRolesDAO();
    private FilesDAO filesDAO = DAOFactory.DEFAULT.buildFilesDAO();
    private SessionBean sessionBean;
    private RolesBean rolesBean;
    private Users newUser = new Users();
    private String repPasswd;
    private String actualPasswd;
    private Contests editedContest;
    private Contests currentContest;
    private Series editedSeries;
    private Problems editedProblem;
    private Problems copiedProblem;
    private Users editedUser;
    private Tests editedTest;
    private Submits editedSubmit;
    private Questions editedQuestion;
    private Results editedResult;
    private Aliases editedAlias;
    private Classes editedClass;
    private Languages editedLanguage;
    //    private UsersRoles editedUsersRoles;
    private List<LanguagesProblems> temporaryLanguagesProblems = null;
    private List<Contests> contests = null;
    private List<Series> contestsSeries = null;
    private List<Problems> seriesProblems = null;
    private List<Problems> contestsProblems = null;
    private List<Classes> diffClasses = null;
    private List<Languages> languages = null;
    private List<Series> currentContestSeries = null;
    private List<Questions> currentContestQuestions = null;
    private List<Users> users = null;
    private List<Roles> roles = null;
    private List<Aliases> allAliases = null;
    private List<Classes> allClasses = null;
    private RankingTable currentContestRanking = null;
    private RankingTable currentContestSubranking = null;
    private PagedDataModel submissions = null;
    private Date temporaryRankingDate;
    private Date temporaryDate;
    private Boolean temporaryAdminBoolean;
    private Integer temporaryContestId;
    private Integer temporarySeriesId;
    private Integer temporaryProblemId;
    private Integer temporarySubmitId;
    private Integer temporaryAliasId;
    private Integer temporaryClassId;
    private Integer temporaryTestId;
    private Integer temporaryQuestionId;
    private Integer temporaryUserId;
    private Integer temporaryUsersRolesId;
    private Integer temporaryRoleTypeId;
    private Integer[] temporaryLanguagesIds;
    private Integer temporaryLanguageId;
    private Integer temporaryResultId;
    private Integer temporarySubmitResultId;
    private String temporarySource;
    private Problems currentProblem;
    private Submits currentSubmit;
    private String dummy;
    private List<Problems> submittableProblems;
    private UploadedFile temporaryFile;
    private String answer;
    private boolean publicAnswer;
    private boolean deleteFile;
    private boolean ratingMode = false;
    private Integer ratingEditNote;

    /**
     * @return the sessionBean
     */
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    /**
     * @param sessionBean the sessionBean to set
     */
    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    /**
     * @return the rolesBean
     */
    public RolesBean getRolesBean() {
        return rolesBean;
    }

    /**
     * @param rolesBean the rolesBean to set
     */
    public void setRolesBean(RolesBean rolesBean) {
        this.rolesBean = rolesBean;
    }

    /**
     * @return the copiedProblem
     */
    public Problems getCopiedProblem() {
        if (copiedProblem == null) {
            FacesContext context = FacesContext.getCurrentInstance();

            if (!WWWHelper.isPost(context)) {
                try {
                    temporaryProblemId = Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("id"));
                } catch (Exception e) {
                    return null;
                }
            }

            if (ELFunctions.isNullOrZero(temporaryProblemId)) {
                return null;
            } else {
                Problems problem = problemsDAO.getById(temporaryProblemId);

                copiedProblem = new Problems();

                copiedProblem.setAbbrev(problem.getAbbrev());
                copiedProblem.setName(problem.getName());

                if (!WWWHelper.isPost(context)) {
                    if (problem.getSeries() != null) {
                        temporarySeriesId = problem.getSeries().getId().intValue();
                        temporaryContestId = problem.getSeries().getContests().getId().intValue();
                    }
                }
            }
        }

        return copiedProblem;
    }

    /**
     * @param copiedProblem the copiedProblem to set
     */
    public void setCopiedProblem(Problems copiedProblem) {
        this.copiedProblem = copiedProblem;
    }

    /**
     * @param editedProblem the editedProblem to set
     */
    public void setEditedProblem(Problems editedProblem) {
        this.editedProblem = editedProblem;
    }

    /**
     * @return the newUser
     */
    public Users getNewUser() {
        return newUser;
    }

    public String getRepPasswd() {
        return repPasswd;
    }

    public void setRepPasswd(String repPasswd) {
        this.repPasswd = repPasswd;
    }

    public String getActualPasswd() {
        return actualPasswd;
    }

    public void setActualPasswd(String actualPasswd) {
        this.actualPasswd = actualPasswd;
    }

    public List<Contests> getContests() {
        if (contests == null) {
            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Contests.class);
            c.addOrder(Order.desc("visibility")); // FIXME: niewidoczne *na końcu*, czy pomiędzy?
            c.addOrder(Order.desc("startdate"));
            c.addOrder(Order.desc("id"));

            contests = new ArrayList<Contests>();

            for (Contests contest : (List<Contests>) c.list()) {
                if (rolesBean.canAddProblem(contest.getId(), null)) {
                    contests.add(contest);
                } else if (rolesBean.canEditProblem(contest.getId(), null)) {
                    contests.add(contest);
                } else if (contest.getVisibility()) {
                    contests.add(contest);
                }
            }
        }

        return contests;
    }

    public List<Contests> getContestsWhenAddingProblem() {
        if (contests == null) {
            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Contests.class);
            c.addOrder(Order.desc("startdate"));
            c.addOrder(Order.desc("id"));

            contests = new ArrayList<Contests>();

            for (Contests contest : (List<Contests>) c.list()) {
                if (ELFunctions.isNullOrZero(temporaryProblemId) && rolesBean.canAddProblem(contest.getId(), null)) {
                    contests.add(contest);
                } else if (!ELFunctions.isNullOrZero(temporaryProblemId) && rolesBean.canEditProblem(contest.getId(), null)) {
                    contests.add(contest);
                }
            }
        }

        return contests;
    }

    public List<Roles> getRoles() {
        if (roles == null) {
            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Roles.class);
            c.addOrder(Order.asc("id"));
            roles = c.list();
        }
        return roles;
    }

    public List<Aliases> getAllAliases() {
        if (allAliases == null) {
            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Aliases.class);
            c.addOrder(Order.asc("name"));
            allAliases = c.list();
        }
        return allAliases;
    }

    public List<Classes> getAllClasses() {
        if (allClasses == null) {
            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Classes.class);
            c.addOrder(Order.asc("id"));
            allClasses = c.list();
        }
        return allClasses;
    }

    public List<Classes> getDiffClasses() {
        if (diffClasses == null) {
            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Classes.class);
            c.add(Restrictions.eq("type", ClassesTypeEnum.DIFF.getCode()));
            c.addOrder(Order.asc("id"));
            diffClasses = c.list();
        }

        return diffClasses;
    }

    public List<Classes> getLanguageClasses() {
        if (diffClasses == null) {
            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Classes.class);
            c.add(Restrictions.eq("type", ClassesTypeEnum.LANGUAGE.getCode()));
            c.addOrder(Order.asc("id"));
            diffClasses = c.list();
        }

        return diffClasses;
    }

    public List<Languages> getLanguages() {
        if (languages == null) {
            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Languages.class);
            c.addOrder(Order.asc("id"));
            languages = c.list();
        }

        return languages;
    }

    public List<Series> getContestsSeries() {
        if (contestsSeries == null) {
            if (temporaryContestId == null) {
                temporaryContestId = getTemporaryContestId();
            }

            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Series.class);
            c.createCriteria("contests").add(Restrictions.eq("id", temporaryContestId));
            c.addOrder(Order.asc("startdate"));
            contestsSeries = c.list();
        }

        return contestsSeries;
    }

    public List<Series> getContestsSeriesWhenAddingProblem() {
        if (contestsSeries == null) {
            if (temporaryContestId == null) {
                temporaryContestId = getTemporaryContestId();
            }

            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Series.class);
            c.createCriteria("contests").add(Restrictions.eq("id", temporaryContestId));
            c.addOrder(Order.desc("startdate"));
            c.addOrder(Order.desc("id"));
            contestsSeries = new ArrayList<Series>();
            for (Series serie : (List<Series>) c.list()) {
                if (ELFunctions.isNullOrZero(temporaryProblemId) && rolesBean.canAddProblem(serie.getContests().getId(), serie.getId())) {
                    contestsSeries.add(serie);
                } else if (!ELFunctions.isNullOrZero(temporaryProblemId) && rolesBean.canEditProblem(serie.getContests().getId(), serie.getId())) {
                    contestsSeries.add(serie);
                }
            }

        }

        return contestsSeries;
    }

    public List<Problems> getSeriesProblems() {
        if (seriesProblems == null) {
            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Problems.class);
            c.createCriteria("series").add(Restrictions.eq("id", temporarySeriesId));
            c.addOrder(Order.asc("abbrev"));
            seriesProblems = c.list();
        }

        return seriesProblems;
    }

    public List<Problems> getContestsProblems() {
        if (contestsProblems == null && getCurrentContest() != null) {
            List<Series> series = seriesDAO.findByContestsid(getCurrentContest().getId());
            if (series != null) {
                contestsProblems = new ArrayList<Problems>();
                for (Series s : series) {
                    contestsProblems.addAll(s.getProblemss());
                }
            }
        }

        return contestsProblems;
    }

    public List<Problems> getSubmittableProblems() {
        if (submittableProblems == null) {
            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Problems.class);
            Criteria s = c.createCriteria("series");

            if (getCurrentContest() != null) {
                s.createCriteria("contests").add(Restrictions.eq("id", getCurrentContest().getId()));
            }
            if (getCurrentContest() == null || rolesBean.canEditProblem(getCurrentContest().getId(), null) == false) {
                s.add(Restrictions.or(Restrictions.isNull("enddate"), Restrictions.gt("enddate", new Date())));
                s.add(Restrictions.le("startdate", new Date()));
            }
            s.addOrder(Order.desc("startdate"));
            s.addOrder(Order.desc("id"));
            c.addOrder(Order.asc("abbrev"));

            if (getCurrentContest() == null || rolesBean.canEditProblem(getCurrentContest().getId(), null) == false) {
                String clientIp = getClientIp();
                List<Problems> problems = c.list();
                submittableProblems = new ArrayList<Problems>();
                for (Problems problem : problems) {
                    if (ELFunctions.isValidIP(problem.getSeries().getOpenips(), clientIp)) {
                        submittableProblems.add(problem);
                    }
                }
            } else {
                submittableProblems = c.list();
            }
        }

        return submittableProblems;
    }

    public List<Series> getCurrentContestSeries() {
        if (currentContestSeries == null) {
//            currentContestSeries = seriesDAO.findByContestsid(getCurrentContest().getId());
//            Collections.reverse(currentContestSeries);
            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Series.class);
            c.addOrder(Order.desc("startdate"));
            c.addOrder(Order.asc("enddate"));
            c.addOrder(Order.desc("id"));
            c.add(Restrictions.eq("contests.id", getCurrentContest().getId()));
            if (!rolesBean.canAddProblem(getCurrentContest().getId(), null)) {
                c.add(Restrictions.le("startdate", new Date()));
            }

            List<Series> series = c.list();

            if (!rolesBean.canAddProblem(getCurrentContest().getId(), null)) {
                currentContestSeries = new ArrayList<Series>();

                String clientIp = getClientIp();
                for (Series serie : series) {
                    if (serie.getHiddenblocked() == false || ELFunctions.isValidIP(serie.getOpenips(), clientIp)) {
                        currentContestSeries.add(serie);
                    }
                }
            } else {
                currentContestSeries = series;
            }
        }

        return currentContestSeries;
    }

    public Boolean getHaveUnansweredQuestions() {
        if (getCurrentContest() == null) {
            return false;
        }
        try {
            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Questions.class);
            c.setProjection(Projections.rowCount());
            c.add(Restrictions.eq("contests.id", getCurrentContest().getId()));
            c.add(Restrictions.eq("adate", new Timestamp(0)));

            if (rolesBean.canAddProblem(getCurrentContest().getId(), null) == false) {
                c.add(Restrictions.or(Restrictions.eq("qtype", 1), Restrictions.eq("users.id", sessionBean.getCurrentUser().getId())));
            }

            Number number = (Number) c.uniqueResult();
            return number.intValue() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Questions> getCurrentContestQuestions() {
        if (currentContestQuestions == null) {
            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Questions.class);

            c.add(Restrictions.eq("contests.id", getCurrentContest().getId()));

            if (rolesBean.canAddProblem(getCurrentContest().getId(), null) == true) {
                c.addOrder(Order.asc("adate"));
                c.addOrder(Order.asc("qdate"));
            } else {
                c.addOrder(Order.desc("adate"));
                c.addOrder(Order.desc("qdate"));

                c.add(Restrictions.or(Restrictions.eq("qtype", 1), Restrictions.eq("users.id", sessionBean.getCurrentUser().getId())));
            }

            currentContestQuestions = c.list();
        }

        return currentContestQuestions;
    }

    public String getCurrentContestRankingHtml() {
        RankingTable table = getCurrentContestRanking();

        return table.getHtml(sessionBean.isLoggedIn());
    }

    public RankingTable getCurrentContestRanking() {
        if (currentContestRanking == null && getCurrentContest() != null) {
            Integer contests_id = getCurrentContest().getId();
            Integer type = getCurrentContest().getType();
            Integer rankingRefreshRate = getCurrentContest().getRankingrefreshrate();
            if (rankingRefreshRate == null) {
                rankingRefreshRate = 0;
            }
            Date date = temporaryRankingDate;
            if (date == null) {
                date = new Date();
            }
            if (temporarySeriesId == null) {
                currentContestRanking = Ranking.getInstance().getRanking(contests_id, type, rankingRefreshRate, date, temporaryAdminBoolean);
            } else {
                currentContestRanking = Ranking.getInstance().getRankingForSeries(contests_id, temporarySeriesId, type, rankingRefreshRate, date, temporaryAdminBoolean);
            }
        }

        return currentContestRanking;
    }

    public String getCurrentContestSubrankingHtml() {
        RankingTable table = getCurrentContestSubranking();

        return table.getHtml(sessionBean.isLoggedIn());
    }

    public RankingTable getCurrentContestSubranking() {
        if (currentContestSubranking == null && getCurrentContest() != null) {
            Integer contests_id = getCurrentContest().getId();
            Integer subtype = getCurrentContest().getSubtype();
            Integer rankingRefreshRate = getCurrentContest().getRankingrefreshrate();
            if (rankingRefreshRate == null) {
                rankingRefreshRate = 0;
            }
            Date date = temporaryRankingDate;
            if (date == null) {
                date = new Date();
            }

            if (subtype == 0) {
                currentContestSubranking = getCurrentContestRanking();
            } else {
                currentContestSubranking = Ranking.getInstance().getSubranking(contests_id, subtype, rankingRefreshRate, date, temporaryAdminBoolean);
            }
        }

        return currentContestSubranking;
    }

    public PagedDataModel getSubmissions() {
        if (submissions == null) {
            List<Integer> ratableSeries = null;

            if (!sessionBean.isShowOnlyMySubmissions()) {
                ratableSeries = new ArrayList<>();
                for (Series s : getCurrentContest().getSeriess()) {
                    if (rolesBean.canRate(getCurrentContest().getId(), s.getId())) {
                        ratableSeries.add(s.getId());
                    }
                }
                if (ratableSeries.isEmpty()) {
                    ratableSeries.add(0);
                }
            }

            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Submits.class);
            c.setProjection(Projections.rowCount());
            Criteria criteriaProblems = c.createCriteria("problems");
            Criteria criteriaSeries = criteriaProblems.createCriteria("series");
            criteriaSeries.createCriteria("contests").add(Restrictions.eq("id", getCurrentContest().getId()));

            if (sessionBean.isShowOnlyMySubmissions()) {
                c.createCriteria("users").add(Restrictions.eq("id", sessionBean.getCurrentUser().getId()));
            } else {
                if (sessionBean.getSubmissionsUserId() != 0) {
                    c.createCriteria("users").add(Restrictions.eq("id", sessionBean.getSubmissionsUserId()));
                }
                if (sessionBean.getSubmissionsProblemId() != 0) {
                    criteriaProblems.add(Restrictions.eq("id", sessionBean.getSubmissionsProblemId()));
                }
                if (sessionBean.getSubmissionsSeriesId() != 0) {
                    criteriaSeries.add(Restrictions.eq("id", sessionBean.getSubmissionsSeriesId()));
                }
                criteriaSeries.add(Restrictions.in("id", ratableSeries));
            }
            Number number = (Number) c.uniqueResult();

            Criteria c2 = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Submits.class);
            Criteria criteria2Problems = c2.createCriteria("problems");
            Criteria criteria2Series = criteria2Problems.createCriteria("series");
            criteria2Series.createCriteria("contests").add(Restrictions.eq("id", getCurrentContest().getId()));

            if (sessionBean.isShowOnlyMySubmissions()) {
                c2.createCriteria("users").add(Restrictions.eq("id", sessionBean.getCurrentUser().getId()));
            } else {
                if (sessionBean.getSubmissionsUserId() != 0) {
                    c2.createCriteria("users").add(Restrictions.eq("id", sessionBean.getSubmissionsUserId()));
                }
                if (sessionBean.getSubmissionsProblemId() != 0) {
                    criteria2Problems.add(Restrictions.eq("id", sessionBean.getSubmissionsProblemId()));
                }
                if (sessionBean.getSubmissionsSeriesId() != 0) {
                    criteria2Series.add(Restrictions.eq("id", sessionBean.getSubmissionsSeriesId()));
                }
                criteria2Series.add(Restrictions.in("id", ratableSeries));
            }
            c2.addOrder(Order.desc("sdate"));
            if (sessionBean.isShowOnlyMySubmissions()) {
                c2.setFirstResult(sessionBean.getSubmissionsPageIndex() * getSubmissionsPerPage());
                c2.setMaxResults(getSubmissionsPerPage());
            } else {
                c2.setFirstResult(sessionBean.getSubmissionsPageIndex() * getSubmissionsPerPage());
                c2.setMaxResults(getSubmissionsPerPage());
            }
            submissions = new PagedDataModel(c2.list(), number.intValue());
        }
        return submissions;
    }

    public Integer getSubmissionsPerPage() {
        if (sessionBean.isShowOnlyMySubmissions()) {
            return 15;
        } else {
            return 30;
        }
    }

    public String noop() {
        return null;
    }

    /**
     * @return the editedContest
     */
    public Contests getEditedContest() {
        if (editedContest == null) {
            FacesContext context = FacesContext.getCurrentInstance();

            if (!WWWHelper.isPost(context)) {
                try {
                    temporaryContestId = Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("id"));
                } catch (Exception e) {
                    temporaryContestId = 0;
                }

            }

            if (ELFunctions.isNullOrZero(temporaryContestId)) {
                editedContest = new Contests();
                editedContest.setEmail(sessionBean.getCurrentUser().getEmail());
            } else {
                editedContest = contestsDAO.getById(temporaryContestId);
            }

        }

        return editedContest;
    }

    public Results getEditedResult() {
        if (editedResult == null && !ELFunctions.isNullOrZero(temporaryResultId)) {
            editedResult = resultsDAO.getById(temporaryResultId);
            temporarySubmitResultId = editedResult.getStatus();
        }

        return editedResult;
    }

    public Languages getEditedLanguage() {
        if (editedLanguage == null) {
            FacesContext context = FacesContext.getCurrentInstance();

            if (!WWWHelper.isPost(context)) {
                try {
                    temporaryLanguageId = Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("id"));
                } catch (Exception e) {
                    temporaryLanguageId = 0;
                }
            }

            if (ELFunctions.isNullOrZero(temporaryLanguageId)) {
                editedLanguage = new Languages();
                temporaryClassId = null;
            } else {
                editedLanguage = languagesDAO.getById(temporaryLanguageId);
                temporaryClassId = editedLanguage.getClasses().getId();
            }
        }
        return editedLanguage;
    }

    //    public UsersRoles getEditedUsersRoles() {
//        if (editedUsersRoles == null) {
//            FacesContext context = FacesContext.getCurrentInstance();
//
//            if (!WWWHelper.isPost(context)) {
//                try {
//                    setTemporaryUsersRolesId((Integer) Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("id")));
//                } catch (Exception e) {
//                    setTemporaryUsersRolesId((Integer) 0);
//                }
//            }
//
//            if (ELFunctions.isNullOrZero(getTemporaryUsersRolesId())) {
//                editedUsersRoles = new UsersRoles();
//                editedUsersRoles.setUsers(usersDAO.getById(temporaryUserId));
//            } else {
//                editedUsersRoles = usersRolesDAO.getById(getTemporaryUsersRolesId());
//
//                setTemporaryRoleTypeId(editedUsersRoles.getRoles().getId());
//                if (editedUsersRoles.getContests() != null) {
//                    temporaryContestId = editedUsersRoles.getContests().getId();
//                }
//
//                if (editedUsersRoles.getSeries() != null) {
//                    temporarySeriesId = editedUsersRoles.getSeries().getId();
//                }
//
//            }
//        }
//        return editedUsersRoles;
//    }
    public Aliases getEditedAlias() {
        if (editedAlias == null) {
            FacesContext context = FacesContext.getCurrentInstance();

            if (!WWWHelper.isPost(context) && temporaryAliasId == null) {
                try {
                    temporaryAliasId = Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("id"));
                } catch (Exception e) {
                    temporaryAliasId = 0;
                }
            }

            if (ELFunctions.isNullOrZero(temporaryAliasId)) {
                editedAlias = new Aliases();
            } else {
                editedAlias = aliasesDAO.getById(temporaryAliasId);
            }
        }

        return editedAlias;
    }

    public Classes getEditedClass() {
        if (editedClass == null) {
            FacesContext context = FacesContext.getCurrentInstance();

            if (!WWWHelper.isPost(context) && temporaryClassId == null) {
                try {
                    temporaryClassId = Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("id"));
                } catch (Exception e) {
                    temporaryClassId = 0;
                }
            }

            if (ELFunctions.isNullOrZero(temporaryClassId)) {
                editedClass = new Classes();
            } else {
                editedClass = classesDAO.getById(temporaryClassId);
            }
        }

        return editedClass;
    }

    public Users getEditedUser() {
        if (editedUser == null) {
            FacesContext context = FacesContext.getCurrentInstance();

            if (!WWWHelper.isPost(context) && temporaryUserId == null) {
                try {
                    temporaryUserId = Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("id"));
                } catch (Exception e) {
                    temporaryUserId = 0;
                }
            }

            if (ELFunctions.isNullOrZero(temporaryUserId)) {
                if (sessionBean.getCurrentUser() == null) {
                    return null;
                }
                editedUser = usersDAO.getById(sessionBean.getCurrentUser().getId());
            } else {
                editedUser = usersDAO.getById(temporaryUserId);
            }

//            temporaryUserRolesIds = new Integer[editedUser.getUsersRoless().size()];
//            for (int i = 0; i < editedUser.getUsersRoless().size(); ++i) {
//                temporaryUserRolesIds[i] = editedUser.getUsersRoless().get(i).getRoles().getId();
//            }
        }

        return editedUser;
    }

    public Contests getCurrentContest() {
        if (currentContest == null) {
            if (sessionBean.getCurrentContestId() != null) {
                currentContest = contestsDAO.getById(sessionBean.getCurrentContestId());

                if (currentContest.getVisibility() == false
                        && rolesBean.canAddProblem(currentContest.getId(), null) == false
                        && rolesBean.canEditProblem(currentContest.getId(), null) == false) {
                    currentContest = null;
                }
            }
        }

        return currentContest;
    }

    public Submits getEditedSubmit() {
        if (editedSubmit == null) {
            editedSubmit = new Submits();
        }

        return editedSubmit;
    }

    public Submits getCurrentSubmit() {
        if (currentSubmit == null && !ELFunctions.isNullOrZero(temporarySubmitId)) {
            currentSubmit = submitsDAO.getById(temporarySubmitId);
        }
        return currentSubmit;
    }

    public Questions getEditedQuestion() {
        if (editedQuestion == null) {
            FacesContext context = FacesContext.getCurrentInstance();

            if (!WWWHelper.isPost(context)) {
                try {
                    temporaryQuestionId = Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("id"));
                } catch (Exception e) {
                    temporaryQuestionId = 0;
                }
            }

            if (ELFunctions.isNullOrZero(temporaryQuestionId)) {
                editedQuestion = new Questions();
            } else {
                editedQuestion = questionsDAO.getById(temporaryQuestionId);
                if (editedQuestion != null && !WWWHelper.isPost(context)) {
                    publicAnswer = editedQuestion.getQtype().equals(1);
                }
            }
        }

        return editedQuestion;
    }

    public List<LanguagesProblems> getTemporaryLanguagesProblems() {
        if (temporaryLanguagesProblems == null) {
            temporaryLanguagesProblems = languagesProblemsDAO.findByProblemsid(temporaryProblemId);
        }

        return temporaryLanguagesProblems;
    }

    public Problems getCurrentProblem() {
        return currentProblem;
    }

    public Boolean getTemporaryAdminBoolean() {
        return temporaryAdminBoolean;
    }

    public Integer getTemporaryContestId() {
        return temporaryContestId;
    }

    public void setTemporaryContestId(Integer id) {
        temporaryContestId = id;
    }

    public Integer getTemporarySeriesId() {
        return temporarySeriesId;
    }

    public void setTemporarySeriesId(Integer id) {
        temporarySeriesId = id;
    }

    public Integer getTemporaryProblemId() {
        return temporaryProblemId;
    }

    public void setTemporaryProblemId(Integer temporaryProblemId) {
        this.temporaryProblemId = temporaryProblemId;
    }

    public Integer getTemporaryAliasId() {
        return temporaryAliasId;
    }

    public Integer getTemporaryClassId() {
        return temporaryClassId;
    }

    public void setTemporaryAliasId(Integer temporaryAliasId) {
        this.temporaryAliasId = temporaryAliasId;
    }

    public void setTemporaryClassId(Integer temporaryClassId) {
        this.temporaryClassId = temporaryClassId;
    }

    public Integer getTemporaryTestId() {
        return temporaryTestId;
    }

    public void setTemporaryTestId(Integer temporaryTestId) {
        this.temporaryTestId = temporaryTestId;
    }

    public Integer getTemporaryQuestionId() {
        return temporaryQuestionId;
    }

    public void setTemporaryQuestionId(Integer temporaryQuestionId) {
        this.temporaryQuestionId = temporaryQuestionId;
    }

    //    public Integer[] getTemporaryUserRolesIds() {
//        return temporaryUserRolesIds;
//    }
//
//    public void setTemporaryUserRolesIds(Integer[] temporaryUserRolesIds) {
//        this.temporaryUserRolesIds = temporaryUserRolesIds;
//    }
    public Integer[] getTemporaryLanguagesIds() {
        return temporaryLanguagesIds;
    }

    public void setTemporaryLanguagesIds(Integer[] temporaryLanguagesIds) {
        this.temporaryLanguagesIds = temporaryLanguagesIds;
    }

    public Integer getTemporaryLanguageId() {
        return temporaryLanguageId;
    }

    public void setTemporaryLanguageId(Integer temporaryLanguageId) {
        this.temporaryLanguageId = temporaryLanguageId;
    }

    public Integer getTemporaryResultId() {
        return temporaryResultId;
    }

    public void setTemporaryResultId(Integer temporaryResultId) {
        this.temporaryResultId = temporaryResultId;
    }

    public Integer getTemporarySubmitId() {
        return temporarySubmitId;
    }

    public void setTemporarySubmitId(Integer temporarySubmitId) {
        this.temporarySubmitId = temporarySubmitId;
    }

    public Integer getTemporaryUserId() {
        return temporaryUserId;
    }

    public void setTemporaryUserId(Integer temporaryUserId) {
        this.temporaryUserId = temporaryUserId;
    }

    public UploadedFile getTemporaryFile() {
        return temporaryFile;
    }

    public void setTemporaryFile(UploadedFile temporaryFile) {
        this.temporaryFile = temporaryFile;
    }

    public Boolean getDeleteFile() {
        return deleteFile;
    }

    public void setDeleteFile(Boolean deleteFile) {
        this.deleteFile = deleteFile;
    }

    public boolean isPublicAnswer() {
        return publicAnswer;
    }

    public void setPublicAnswer(boolean publicAnswer) {
        this.publicAnswer = publicAnswer;
    }

    public boolean isShowOnlyMySubmissions() {
        return sessionBean.isShowOnlyMySubmissions();
    }

    public void setShowOnlyMySubmissions(boolean showOnlyMySubmissions) {
        sessionBean.setShowOnlyMySubmissions(showOnlyMySubmissions);
    }

    public boolean isRatingMode() {
        return ratingMode;
    }

    public void setRatingMode(boolean ratingMode) {
        this.ratingMode = ratingMode;
    }

    public String getTemporarySource() {
        return temporarySource;
    }

    public void setTemporarySource(String temporarySource) {
        this.temporarySource = temporarySource;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Series getEditedSeries() {
        if (editedSeries == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (!WWWHelper.isPost(context)) {
                try {
                    temporarySeriesId = Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("id"));
                } catch (Exception e) {
                    temporarySeriesId = 0;
                }
            }

            if (ELFunctions.isNullOrZero(temporarySeriesId)) {
                editedSeries = new Series();
            } else {
                editedSeries = seriesDAO.getById(temporarySeriesId);

                if (!WWWHelper.isPost(context) && editedSeries.getContests() != null) {
                    temporaryContestId = editedSeries.getContests().getId();
                }
            }
        }

        return editedSeries;
    }

    public Problems getEditedProblem() {
        if (editedProblem == null) {
            FacesContext context = FacesContext.getCurrentInstance();

            if (!WWWHelper.isPost(context)) {
                try {
                    temporaryProblemId = Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("id"));
                } catch (Exception e) {
                    temporaryProblemId = 0;
                }
            }

            if (ELFunctions.isNullOrZero(temporaryProblemId)) {
                editedProblem = new Problems();
            } else {
                editedProblem = problemsDAO.getById(temporaryProblemId);

                if (!WWWHelper.isPost(context)) {
                    if (editedProblem.getSeries() != null) {
                        temporarySeriesId = editedProblem.getSeries().getId();
                        temporaryContestId = editedProblem.getSeries().getContests().getId();
                    }
                    if (editedProblem.getClasses() != null) {
                        temporaryClassId = editedProblem.getClasses().getId();
                    }
                    if (editedProblem.getLanguagesProblemss() != null) {
                        temporaryLanguagesIds = new Integer[editedProblem.getLanguagesProblemss().size()];
                        for (int i = 0; i < editedProblem.getLanguagesProblemss().size(); ++i) {
                            temporaryLanguagesIds[i] = editedProblem.getLanguagesProblemss().get(i).getLanguages().getId();
                        }
                    }
                }
            }
        }

        return editedProblem;
    }

    public Tests getEditedTest() {
        if (editedTest == null) {
            FacesContext context = FacesContext.getCurrentInstance();

            if (!WWWHelper.isPost(context)) {
                try {
                    temporaryTestId = Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("id"));
                } catch (Exception e) {
                    temporaryTestId = 0;
                }
            }

            if (ELFunctions.isNullOrZero(temporaryTestId)) {
                editedTest = new Tests();
            } else {
                editedTest = testsDAO.getById(temporaryTestId);

                if (!WWWHelper.isPost(context)) {
                    if (editedTest.getProblems() != null) {
                        temporaryContestId = editedTest.getProblems().getSeries().getContests().getId();
                        temporarySeriesId = editedTest.getProblems().getSeries().getId();
                        temporaryProblemId = editedTest.getProblems().getId();
                    }
                }
            }
        }

        return editedTest;
    }

    public String registerUser() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            try {
                newUser.setLogin(newUser.getLogin().toLowerCase());
                newUser.savePass(newUser.getPass());
                newUser.setRdate(new Timestamp(System.currentTimeMillis()));
                usersDAO.save(newUser);
            } catch (ConstraintViolationException e) {
                String summary = messages.getString("login_exists");
                WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formRegister:login", summary, null);
                newUser.savePass(StringUtils.EMPTY);
                return null;
            }
        } catch (Exception e) {
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formRegister:save", summary, null);
            return null;
        }

        return "login";
    }

    public String saveAlias() {
        if (!rolesBean.canEditAnyProblem()) {
            return null;
        }

        try {
            aliasesDAO.saveOrUpdate(getEditedAlias());
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formSubmit:save", summary, null);
            return null;
        }

        return "listaliases";
    }

    public String saveLanguage() {
        if (!rolesBean.canEditAnyProblem()) {
            return null;
        }

        try {
            getEditedLanguage().setClasses(classesDAO.getById(temporaryClassId));
            languagesDAO.saveOrUpdate(getEditedLanguage());
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditLanguage:save", summary, null);
            return null;
        }

        return "listlanguages";
    }

    public String saveContest() {
        Integer id = getEditedContest().getId();
        if ((ELFunctions.isNullOrZero(id) && !rolesBean.canAddContest(null, null)) || (!ELFunctions.isNullOrZero(id) && !rolesBean.canEditContest(id, null))) {
            return null;
        }

        try {
            contestsDAO.saveOrUpdate(getEditedContest());
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditContest:save", summary, null);
            return null;
        }

        return "start";
    }

    public String saveSeries() {
        Integer id = editedSeries.getId();
        if ((ELFunctions.isNullOrZero(id) && !rolesBean.canAddSeries(temporaryContestId, null)) || (!ELFunctions.isNullOrZero(id) && !rolesBean.canEditSeries(temporaryContestId, id))) {
            return null;
        }

        try {
            editedSeries.setContests(contestsDAO.getById(temporaryContestId));
            seriesDAO.saveOrUpdate(editedSeries);
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditSeries:save", summary, null);
            return null;
        }

        return "problems";
    }

    public String updateUserByAdmin() {
        if (!rolesBean.canEditUsers()) {
            return null;
        }

        try {
            if (repPasswd != null && repPasswd.length() > 0) {
                editedUser.savePass(repPasswd);
            }
//
//            List<UsersRoles> tmpList = usersRolesDAO.findByUsersid(editedUser.getId());
//
//            if (tmpList != null) {
//                for (UsersRoles role : tmpList) {
//                    usersRolesDAO.delete(role);
//                }
//            }
//
//            for (Integer rid : temporaryUserRolesIds) {
//                UsersRoles usersRoles = new UsersRoles();
//                usersRoles.setRoles(rolesDAO.getById(rid));
//                usersRoles.setUsers(editedUser);
//                usersRolesDAO.saveOrUpdate(usersRoles);
//            }

            usersDAO.saveOrUpdate(editedUser);
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formProfile:save", summary, null);
            return null;
        }

        return "listusers";
    }

    public String updateUser() {
        try {
            usersDAO.saveOrUpdate(editedUser);
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formProfile:save", summary, null);
            return null;
        }

        return "start";
    }

    public String updateUsersPasswd() {
        try {
            editedUser.savePass(editedUser.getPass());
            usersDAO.saveOrUpdate(editedUser);
            sessionBean.getCurrentUser().setPass(editedUser.getPass());
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formPasswd:save", summary, null);
            return null;
        }

        return "profil";
    }

    public String sendClassFile() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!rolesBean.canEditAnyProblem()) {
            return null;
        }

        if (temporaryFile == null) {
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formSubmit::classfile", messages.getString("javax.faces.component.UIInput.REQUIRED"), null);
            return null;
        }

        try {
            editedClass.setCode(temporaryFile.getBytes());
            if (editedClass.getVersion() == null) {
                editedClass.setVersion(1);
            } else {
                editedClass.setVersion(editedClass.getVersion() + 1);
            }

            classesDAO.saveOrUpdate(editedClass);

            return "listclasses";
        } catch (Exception e) {
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formSubmit::classfile", summary, null);
            return null;
        }

    }

    public String sendFile() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (temporaryFile == null) {
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formSubmit:sourcefile", messages.getString("javax.faces.component.UIInput.REQUIRED"), null);
            return null;
        }

        return sendSolution(temporaryFile.getBytes(), temporaryFile.getName(), "formSubmit:sendfile");
    }

    public String sendCode() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (temporarySource == null || StringUtils.isEmpty(temporarySource)) {
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formSubmit:sourcecode", messages.getString("javax.faces.component.UIInput.REQUIRED"), null);
            return null;
        }

        return sendSolution(temporarySource.getBytes(), null, "formSubmit:sendcode");
    }

    private void insertContest(Contests contest) {
        contestsDAO.saveOrUpdate(contest);
        for (Series serie : contest.getSeriess()) {
            insertSerie(contest, serie);
        }
    }

    private void insertSerie(Contests contest, Series serie) {
        serie.setContests(contest);

        seriesDAO.saveOrUpdate(serie);
        for (Problems problem : serie.getProblemss()) {
            insertProblem(serie, problem);
        }
    }

    private void insertProblem(Series serie, Problems problem) {
        ProblemsUtils.getInstance().copyProblem(problem, serie, problem.getAbbrev(), problem.getName());
    }

    public String uploadContest() {
        if (!rolesBean.canAddContest(null, null)) {
            return null;
        }
        if (temporaryFile == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditProblem::contestfile", messages.getString("javax.faces.component.UIInput.REQUIRED"), null);
            return null;
        }

        try {
            Contests unzippedContest = ZipFile.unzipContest(temporaryFile.getBytes(), getLanguages(), getDiffClasses());

            insertContest(unzippedContest);

            selectContest(unzippedContest.getId());
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditProblem:save", summary, null);
            return null;
        }
        return "problems";
    }

    public String uploadSerie() {
        if (!rolesBean.canAddSeries(temporaryContestId, null)) {
            return null;
        }
        if (temporaryFile == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditProblem::seriefile", messages.getString("javax.faces.component.UIInput.REQUIRED"), null);
            return null;
        }

        try {
            Series unzippedSerie = ZipFile.unzipSerie(temporaryFile.getBytes(), getLanguages(), getDiffClasses());
            Contests contests = contestsDAO.getById(temporaryContestId);

            insertSerie(contests, unzippedSerie);

            selectContest(temporaryContestId);
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditProblem:save", summary, null);
            return null;
        }
        return "problems";
    }

    public String uploadProblem() {
        if (!rolesBean.canAddProblem(temporaryContestId, temporarySeriesId)) {
            return null;
        }
        if (temporaryFile == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditProblem::problemfile", messages.getString("javax.faces.component.UIInput.REQUIRED"), null);
            return null;
        }

        try {
            Problems unzippedProblem = ZipFile.unzipProblem(temporaryFile.getBytes(), getLanguages(), getDiffClasses());
            Series newSeries = seriesDAO.getById(temporarySeriesId);

            insertProblem(newSeries, unzippedProblem);

            selectContest(newSeries.getContests().getId());
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditProblem:save", summary, null);
            return null;
        }
        return "problems";
    }

    public String uploadTest() {
        if (!rolesBean.canEditProblem(temporaryContestId, temporarySeriesId)) {
            return null;
        }

        if (temporaryFile == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditProblem::testfile", messages.getString("javax.faces.component.UIInput.REQUIRED"), null);
            return null;
        }

        try {
            Problems problem = problemsDAO.getById(temporaryProblemId);
            List<Tests> tests = ZipFile.unzipTests(temporaryFile.getBytes());
            for (Tests test : tests) {
                test.setProblems(problem);
                testsDAO.saveOrUpdate(test);
            }
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditProblem:save", summary, null);
            return null;
        }
        return "problems";
    }

    public String copyProblem() {
        Integer id = temporaryProblemId;
        if ((ELFunctions.isNullOrZero(id) && !rolesBean.canEditProblem(temporaryContestId, temporarySeriesId)) || (!ELFunctions.isNullOrZero(id) && !rolesBean.canEditProblem(temporaryContestId, temporarySeriesId))) {
            return null;
        }
        try {
            Problems oldProblem = problemsDAO.getById(temporaryProblemId);
            Series newSeries = seriesDAO.getById(temporarySeriesId);

            Problems newProblem = ProblemsUtils.getInstance().copyProblem(oldProblem, newSeries, copiedProblem.getAbbrev(), copiedProblem.getName());

            selectContest(newProblem.getSeries().getContests().getId());
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditProblem:save", summary, null);
            return null;
        }

        return "problems";
    }

    public String saveProblem() {
        Integer id = getEditedProblem().getId();
        if ((ELFunctions.isNullOrZero(id) && !rolesBean.canAddProblem(temporaryContestId, temporarySeriesId)) || (!ELFunctions.isNullOrZero(id) && !rolesBean.canEditProblem(temporaryContestId, temporarySeriesId))) {
            return null;
        }

        try {
            Problems tmpProblem = getEditedProblem();

            tmpProblem.setSeries(seriesDAO.getById(temporarySeriesId));
            tmpProblem.setClasses(classesDAO.getById(temporaryClassId));

            List<LanguagesProblems> tmpList = languagesProblemsDAO.findByProblemsid(tmpProblem.getId());

            if (tmpList != null) {
                for (LanguagesProblems lp : tmpList) {
                    languagesProblemsDAO.delete(lp);
                }
            }

            for (Integer lid : temporaryLanguagesIds) {
                LanguagesProblems lp = new LanguagesProblems();
                lp.setLanguages(languagesDAO.getById(lid));
                lp.setProblems(tmpProblem);
                languagesProblemsDAO.saveOrUpdate(lp);
            }

            if (temporaryFile != null) {
                Files current = tmpProblem.getFiles();
                if (current == null) {
                    current = new Files();
                }

                String name = temporaryFile.getName();
                current.setFilename(FilenameUtils.getBaseName(name));
                current.setExtension(FilenameUtils.getExtension(name));
                current.setBytes(temporaryFile.getBytes());
                filesDAO.saveOrUpdate(current);
                tmpProblem.setFiles(current);
            }

            if (deleteFile) {
                //Files tmp = tmpProblem.getFiles();
                //filesDAO.delete(tmp);
                tmpProblem.setFiles(null);
            }

            problemsDAO.saveOrUpdate(tmpProblem);

            selectContest(editedProblem.getSeries().getContests().getId());
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditProblem:save", summary, null);
            return null;
        }

        return "problems";
    }

    public String addQuestion() {
        try {
            Problems problem = problemsDAO.getById(temporaryProblemId);
            Questions question = getEditedQuestion();
            question.setContests(getCurrentContest());
            question.setProblems(problem);
            question.setQtype(0);
            question.setUsers(sessionBean.getCurrentUser());
            question.setQdate(new Timestamp(System.currentTimeMillis()));
            question.setAdate(new Timestamp(0));

            questionsDAO.save(question);

            String emails = getCurrentContest().getEmail();
            if (emails != null && emails.isEmpty() == false) {
                HibernateUtil.getSessionFactory().getCurrentSession().flush();

                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String url = request.getRequestURL().
                        delete(request.getRequestURL().length() - request.getRequestURI().length(), request.getRequestURL().length()).
                        append(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()).
                        append("/question/").
                        append(question.getId()).
                        append("/").
                        append(ELFunctions.filterUri(question.getSubject())).
                        append(".html").
                        toString();

                String subject = messages.getString("email_question_subject").
                        replace("%SUBJECT%", question.getSubject()).
                        replace("%TEXT%", question.getQuestion()).
                        replace("%CONTEST%", question.getContests().getName()).
                        replace("%URL%", url).
                        replace("%PROBLEM%", (problem == null || problem.getId() == 0)
                                ? messages.getString("questions_general")
                                : problem.getName()).
                        trim();

                String text = messages.getString("email_question_text").
                        replace("%SUBJECT%", question.getSubject()).
                        replace("%TEXT%", question.getQuestion()).
                        replace("%CONTEST%", question.getContests().getName()).
                        replace("%URL%", url).
                        replace("%PROBLEM%", (problem == null || problem.getId() == 0)
                                ? messages.getString("questions_general")
                                : problem.getName()).
                        trim();

                EmailSender.send(emails, subject, text);
            }
            return "questions";
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formQuestions:save", summary, null);
            return null;
        }
    }

    public String saveAnswer() {
        try {
            String tmp = getEditedQuestion().getQuestion().replaceAll("\n", "\n> ");
            tmp = ">".concat(tmp).concat("\n\n").concat(answer);
            getEditedQuestion().setQuestion(tmp);
            getEditedQuestion().setQtype(publicAnswer ? 1 : 0);
            getEditedQuestion().setAdate(new Timestamp(System.currentTimeMillis()));
            questionsDAO.saveOrUpdate(getEditedQuestion());
            return "questions";
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formQuestion:save", summary, null);
            return null;
        }
    }

    public String saveResult() {
        Results result = getEditedResult();
        result.setStatus(temporarySubmitResultId);
        resultsDAO.update(result);
        temporaryResultId = null;
        return null;
    }

    public String saveNotes() {
        submitsDAO.update(getCurrentSubmit());
        return null;
    }

    @HttpAction(name = "rejudge_submit", pattern = "rejudge/{id}/submit")
    public String reJudgeSubmit(@Param(name = "id", encode = true) int id) {
        Submits s = submitsDAO.getById(id);

        if (s != null && rolesBean.canRate(s.getProblems().getSeries().getContests().getId(), s.getProblems().getSeries().getId())) {
            SubmitsUtils.getInstance().reJudge(s);
            return "/submissions";
        } else {
            return "/error/404";
        }
    }

    @HttpAction(name = "rejudge_problem", pattern = "rejudge/{id}/problem")
    public String reJudgeProblem(@Param(name = "id", encode = true) int id) {
        Problems p = problemsDAO.getById(id);

        if (p != null && rolesBean.canRate(p.getSeries().getContests().getId(), p.getSeries().getId())) {
            ProblemsUtils.getInstance().reJudge(p);
            return "/problems";
        } else {
            return "/error/404";
        }
    }

    @HttpAction(name = "rejudge_seria", pattern = "rejudge/{id}/seria")
    public String reJudgeSeria(@Param(name = "id", encode = true) int id) {
        Series s = seriesDAO.getById(id);

        if (s != null && rolesBean.canRate(s.getContests().getId(), s.getId())) {
            SeriesUtils.getInstance().reJudge(s);
            return "/problems";
        } else {
            return "/error/404";
        }
    }

    @HttpAction(name = "rejudge_contest", pattern = "rejudge/{id}/contest")
    public String reJudgeContest(@Param(name = "id", encode = true) int id) {
        Contests c = contestsDAO.getById(id);

        if (c != null && rolesBean.canRateContest(c)) {
            ContestsUtils.getInstance().reJudge(c);
            return "/start";
        } else {
            return "/error/404";
        }
    }

    @HttpAction(name = "problems", pattern = "problems/{id}/{title}")
    public String goToProblems(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        selectContest(id);

        if (getCurrentContest() == null) {
            return "/error/404";
        } else {
            return "problems";
        }
    }

    @HttpAction(name = "questions", pattern = "questions/{id}/{title}")
    public String goToQuestions(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        selectContest(id);
        if (getCurrentContest() == null) {
            return "/error/404";
        } else {
            return "questions";
        }
    }

    @HttpAction(name = "ranking", pattern = "ranking/{id}/{title}")
    public String goToRanking(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        selectContest(id);
        if (getCurrentContest() == null) {
            return "/error/404";
        } else {
            temporaryAdminBoolean = false;
            if (rolesBean.canAddProblem(getCurrentContest().getId(), null)) {
                if ("__admin__".equals(dummy)) {
                    temporaryAdminBoolean = true;
                }
            }
            return "ranking";
        }
    }

    @HttpAction(name = "ranking_date", pattern = "ranking/{id}/{title}/{date}")
    public String goToRankingDate(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy, @Param(name = "date", encode = true) String date) {
        selectContest(id);
        if (getCurrentContest() == null) {
            return "/error/404";
        } else {
            temporaryAdminBoolean = false;
            if (rolesBean.canAddProblem(getCurrentContest().getId(), null)) {
                try {
                    temporaryRankingDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                } catch (Exception e) {
                }
                if ("__admin__".equals(dummy)) {
                    temporaryAdminBoolean = true;
                }
            }
            return "ranking";
        }
    }

    @HttpAction(name = "ranking_seria", pattern = "ranking_seria/{id}/{title}")
    public String goToRankingSeria(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        Series serie = seriesDAO.getById(id);
        if (serie == null
                || (ELFunctions.isValidIP(serie.getOpenips(), getClientIp()) == false
                && serie.getHiddenblocked() == true
                && !rolesBean.canAddProblem(serie.getContests().getId(), id))) {
            return "/error/404";
        }

        selectContest(serie.getContests().getId());

        if (getCurrentContest() == null) {
            return "/error/404";
        } else {
            temporarySeriesId = id;
            temporaryAdminBoolean = false;
            if (rolesBean.canAddProblem(getCurrentContest().getId(), id)) {
                if ("__admin__".equals(dummy)) {
                    temporaryAdminBoolean = true;
                }
            }
            return "ranking";
        }
    }

    @HttpAction(name = "ranking_seria_date", pattern = "ranking_seria/{id}/{title}/{date}")
    public String goToRankingSeriaDate(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy, @Param(name = "date", encode = true) String date) {
        Series serie = seriesDAO.getById(id);
        if (serie == null
                || (ELFunctions.isValidIP(serie.getOpenips(), getClientIp()) == false
                && serie.getHiddenblocked() == true
                && !rolesBean.canAddProblem(serie.getContests().getId(), id))) {
            return "/error/404";
        }

        selectContest(serie.getContests().getId());

        if (getCurrentContest() == null) {
            return "/error/404";
        } else {
            temporarySeriesId = id;
            temporaryAdminBoolean = false;
            if (rolesBean.canAddProblem(getCurrentContest().getId(), id)) {
                try {
                    temporaryRankingDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                } catch (Exception e) {
                }
                if ("__admin__".equals(dummy)) {
                    temporaryAdminBoolean = true;
                }
            }
            return "ranking";
        }

    }

    @HttpAction(name = "subranking", pattern = "subranking/{id}/{title}")
    public String goToSubranking(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        selectContest(id);
        if (getCurrentContest() == null) {
            return "/error/404";
        } else {
            temporaryAdminBoolean = false;
            if (rolesBean.canAddProblem(getCurrentContest().getId(), null)) {
                if ("__admin__".equals(dummy)) {
                    temporaryAdminBoolean = true;
                }
            }
            return "subranking";
        }
    }

    @HttpAction(name = "rules", pattern = "rules/{id}/{title}")
    public String goToRules(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        selectContest(id);
        if (getCurrentContest() == null) {
            return "/error/404";
        } else {
            return "rules";
        }
    }

    @HttpAction(name = "submissions", pattern = "submissions/{id}/{title}")
    public String goToSubmissions(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        selectContest(id);

        if (sessionBean.getCurrentContestId() != sessionBean.getSubmissionsContestId() || (new Date().getTime() - sessionBean.getSubmissionsLastVisit()) > 60 * 60 * 1000) {
            sessionBean.setShowOnlyMySubmissions(true);
            sessionBean.setSubmissionsContestId(id);
        }
        sessionBean.setSubmissionsUserId(0);
        sessionBean.setSubmissionsSeriesId(0);
        sessionBean.setSubmissionsProblemId(0);
        sessionBean.setSubmissionsPageIndex(0);
        sessionBean.setSubmissionsLastVisit(new Date().getTime());

        if (getCurrentContest() == null) {
            return "/error/404";
        } else {
            return "submissions";
        }
    }

    @HttpAction(name = "submissions_username", pattern = "submissions_username/{username}")
    public String goToSubmissionsUsername(@Param(name = "username", encode = true) String username) {
        try {
            sessionBean.setSubmissionsUserId(usersDAO.findByLogin(username).get(0).getId());
        } catch (Exception e) {
            sessionBean.setSubmissionsUserId(0);
        }
        if (getCurrentContest() == null || sessionBean.isShowOnlyMySubmissions() == true) {
            return "/error/404";
        } else {
            sessionBean.setSubmissionsPageIndex(0);
            return "submissions";
        }

    }

    @HttpAction(name = "submissions_problem", pattern = "submissions_problem/{id}")
    public String goToSubmissionsProblem(@Param(name = "id", encode = true) Integer id) {
        try {
            Problems p = problemsDAO.getById(id);
            selectContest(p.getSeries().getContests().getId());
            sessionBean.setSubmissionsProblemId(p.getId());
        } catch (Exception e) {
            return "/error/404";
        }
        if (getCurrentContest() == null || sessionBean.isShowOnlyMySubmissions() == true) {
            return "/error/404";
        } else {
            sessionBean.setSubmissionsPageIndex(0);
            return "submissions";
        }

    }

    @HttpAction(name = "submissions_seria", pattern = "submissions_seria/{id}")
    public String goToSubmissionsSeries(@Param(name = "id", encode = true) Integer id) {
        if (getCurrentContest() == null || sessionBean.isShowOnlyMySubmissions() == true) {
            return "/error/404";
        } else {
            try {
                Series s = seriesDAO.getById(id);
                selectContest(s.getContests().getId());
                sessionBean.setSubmissionsSeriesId(s.getId());
            } catch (Exception e) {
                return "/error/404";
            }
            sessionBean.setSubmissionsPageIndex(0);
            return "submissions";
        }

    }

    @HttpAction(name = "submission", pattern = "submission/{id}/{title}")
    public String goToSubmission(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        currentSubmit = submitsDAO.getById(id);

        if (currentSubmit == null) {
            return "/error/404";
        } else {
            selectContest(currentSubmit.getProblems().getSeries().getContests().getId());
            temporarySubmitId = id;
            return "submission";
        }
    }

    @HttpAction(name = "code", pattern = "code/{id}/{title}")
    public String goToViewCode(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        String res = goToSubmission(id, dummy);
        if (res.equals("submission")) {
            res = "view_code";
        }
        return res;
    }

    @HttpAction(name = "rate", pattern = "rate/{id}/{title}")
    public String goToRate(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        String res = goToSubmission(id, dummy);
        if (res.equals("submission")) {
            ratingMode = true;
        }

        return res;
    }

    @HttpAction(name = "problem", pattern = "problem/{id}/{title}")
    public String goToProblem(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        currentProblem = problemsDAO.getById(id);

        if (currentProblem != null) {
            Series serie = currentProblem.getSeries();

            if (((ELFunctions.isValidIP(serie.getOpenips(), getClientIp()) == false
                    && serie.getHiddenblocked() == true)
                    || serie.getStartdate().after(new Date())
                    || serie.getContests().getVisibility() == false)
                    && !rolesBean.canAddProblem(serie.getContests().getId(), serie.getId())) {
                currentProblem = null;
            }
        }

        if (currentProblem == null) {
            return "/error/404";
        } else {
            selectContest(currentProblem.getSeries().getContests().getId());
            return "problem";
        }
    }

    @HttpAction(name = "question", pattern = "question/{id}/{title}")
    public String goToQuestion(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        temporaryQuestionId = id;
        editedQuestion = questionsDAO.getById(id);

        if (editedQuestion == null) {
            return "/error/404";
        } else {
            publicAnswer = editedQuestion.getQtype() == 1;
            selectContest(editedQuestion.getContests().getId());
            return "question";
        }
    }

    @HttpAction(name = "submit", pattern = "submit/{id}/{title}")
    public String goToSubmit(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        currentProblem = problemsDAO.getById(id);

        if (currentProblem == null) {
            return "/error/404";
        } else {
            selectContest(currentProblem.getSeries().getContests().getId());
            temporaryProblemId = id;
            return "submit";
        }
    }

    @HttpAction(name = "delcontest", pattern = "del/{id}/contest")
    public String deleteContest(@Param(name = "id", encode = true) int id) {
        if (rolesBean.canDeleteContest(id, null)) {
            contestsDAO.deleteById(id);
            if (sessionBean.getCurrentContestId() != null && sessionBean.getCurrentContestId().equals(id)) {
                currentContest = null;
                sessionBean.setCurrentContestId(null);
            }
            return "/start";
        } else {
            return "/error/404";
        }
    }

    @HttpAction(name = "dellanguage", pattern = "del/{id}/language")
    public String deleteLanguage(@Param(name = "id", encode = true) int id) {
        if (rolesBean.canEditAnyProblem()) {
            try {
                languagesDAO.deleteById(id);
                HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
                HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            } catch (JDBCException e) {
                e.printStackTrace();
                SQLException ex = e.getSQLException();
                while ((ex = ex.getNextException()) != null) {
                    ex.printStackTrace();
                }
            }

            return "/admin/listlanguages";
        } else {
            return "/error/404";
        }
    }

    @HttpAction(name = "delalias", pattern = "del/{id}/alias")
    public String deleteAliases(@Param(name = "id", encode = true) int id) {
        if (rolesBean.canEditAnyProblem()) {
            aliasesDAO.deleteById(id);
            return "/admin/listaliases";
        } else {
            return "/error/404";
        }
    }

    @HttpAction(name = "delclass", pattern = "del/{id}/class")
    public String deleteClasses(@Param(name = "id", encode = true) int id) {
        if (rolesBean.canEditAnyProblem()) {
            classesDAO.deleteById(id);
            return "/admin/listclasses";
        } else {
            return "/error/404";
        }
    }

    @HttpAction(name = "delsubmit", pattern = "del/{id}/submit")
    public String deleteSubmit(@Param(name = "id", encode = true) int id) {
        Submits s = submitsDAO.getById(id);

        if (s != null && rolesBean.canAddProblem(s.getProblems().getSeries().getContests().getId(), s.getProblems().getSeries().getId())) {
            submitsDAO.deleteById(id);
            return "submissions";
        } else {
            return "/error/404";
        }
    }

    @HttpAction(name = "ghostsubmit", pattern = "ghost/{id}/submit")
    public String ghostSubmit(@Param(name = "id", encode = true) int id) {
        Submits s = submitsDAO.getById(id);

        if (s != null && rolesBean.canRate(s.getProblems().getSeries().getContests().getId(), s.getProblems().getSeries().getId())) {
            s.setVisibleinranking(!s.getVisibleinranking());
            submitsDAO.saveOrUpdate(s);

            return "submissions";
        } else {
            return "/error/404";
        }
    }

    @HttpAction(name = "addseries", pattern = "add/{id}/series")
    public String goToAddseries(@Param(name = "id", encode = true) int id) {
        temporaryContestId = id;
        return "/admin/editseries";
    }

    @HttpAction(name = "delseries", pattern = "del/{id}/series")
    public String deleteSeries(@Param(name = "id", encode = true) int id) {
        Series s = seriesDAO.getById(id);
        if (s != null && rolesBean.canDeleteSeries(s.getContests().getId(), id)) {
            try {
                seriesDAO.delete(s);
                HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
                HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            } catch (JDBCException e) {
                e.printStackTrace();
                SQLException ex = e.getSQLException();
                while ((ex = ex.getNextException()) != null) {
                    ex.printStackTrace();
                }
            }
            return "problems";
        } else {
            return "/error/404";
        }
    }

    @HttpAction(name = "deltest", pattern = "del/{id}/test")
    public String deleteTest(@Param(name = "id", encode = true) int id) {
        Tests s = testsDAO.getById(id);
        if (s != null && rolesBean.canEditProblem(s.getProblems().getSeries().getContests().getId(), s.getProblems().getSeries().getId())) {
            temporaryProblemId = s.getProblems().getId();
            testsDAO.delete(s);
            return "del_test";
        } else {
            return "/error/404";
        }
    }

    @HttpAction(name = "addproblem", pattern = "add/{id}/problem")
    public String goToAddProblem(@Param(name = "id", encode = true) int id) {
        Series s = seriesDAO.getById(id);
        if (s != null) {
            temporarySeriesId = id;
            temporaryContestId = s.getContests().getId();
        }

        return "/admin/editproblem";
    }

    @HttpAction(name = "putcontest", pattern = "put/contest")
    public String goToPutContest() {
        return "/admin/uploadcontest";
    }

    @HttpAction(name = "putserie", pattern = "put/{id}/serie")
    public String goToPutSerie(@Param(name = "id", encode = true) int id) {
        Contests c = contestsDAO.getById(id);
        if (c != null) {
            temporaryContestId = c.getId();
        }

        return "/admin/uploadserie";
    }

    @HttpAction(name = "putproblem", pattern = "put/{id}/problem")
    public String goToPutProblem(@Param(name = "id", encode = true) int id) {
        Series s = seriesDAO.getById(id);
        if (s != null) {
            temporarySeriesId = id;
            temporaryContestId = s.getContests().getId();
        }

        return "/admin/uploadproblem";
    }

    @HttpAction(name = "puttest", pattern = "put/{id}/test")
    public String goToPutTest(@Param(name = "id", encode = true) int id) {
        Problems p = problemsDAO.getById(id);
        if (p != null) {
            temporaryProblemId = id;
            temporarySeriesId = p.getSeries().getId();
            temporaryContestId = p.getSeries().getContests().getId();
        }

        return "/admin/uploadtest";
    }

    @HttpAction(name = "delproblem", pattern = "del/{id}/problem")
    public String deleteProblem(@Param(name = "id", encode = true) int id) {
        Problems p = problemsDAO.getById(id);

        if (p != null && rolesBean.canDeleteProblem(p.getSeries().getContests().getId(), p.getSeries().getId())) {
            problemsDAO.delete(p);
            return "problems";
        } else {
            return "/error/404";
        }
    }

    @HttpAction(name = "addtest", pattern = "add/{id}/test")
    public String goToAddtest(@Param(name = "id", encode = true) int id) {
        Problems p = problemsDAO.getById(id);
        if (p != null) {
            temporaryProblemId = id;
            temporarySeriesId = p.getSeries().getId();
            temporaryContestId = p.getSeries().getContests().getId();
        }

        return "/admin/edittest";
    }

    @HttpAction(name = "edituser", pattern = "edit/{id}/user")
    public String goToEdituser(@Param(name = "id", encode = true) String username) {
        if (!rolesBean.canEditUsers()) {
            return "/error/404";
        }

        try {
            temporaryUserId = usersDAO.findByLogin(username).get(0).getId();
            return "/admin/edituser";
        } catch (Exception ex) {
            return "listusers";
        }
    }

    public String saveUsersRoles() {
        Integer id = getEditedProblem().getId();
        if (!rolesBean.canEditUsers()) {
            return null;
        }

        try {
            UsersRoles tmpUsersRoles;

            if (ELFunctions.isNullOrZero(temporaryUsersRolesId)) {
                tmpUsersRoles = new UsersRoles();
                tmpUsersRoles.setUsers(usersDAO.getById(temporaryUserId));
            } else {
                tmpUsersRoles = usersRolesDAO.getById(temporaryUsersRolesId);
            }

            tmpUsersRoles.setRoles(rolesDAO.getById(temporaryRoleTypeId));
            if (temporaryContestId != null) {
                tmpUsersRoles.setContests(contestsDAO.getById(temporaryContestId));
            }
            if (temporarySeriesId != null) {
                tmpUsersRoles.setSeries(seriesDAO.getById(temporarySeriesId));
            }

            usersRolesDAO.saveOrUpdate(tmpUsersRoles);

            String login = tmpUsersRoles.getUsers().getLogin();

            return goToEdituser(login);
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formProfile:save", summary, null);
            return null;
        }
    }

    @HttpAction(name = "adduserrole", pattern = "add/{id}/userrole")
    public String goToAdduserrole(@Param(name = "id", encode = true) int id) {
        if (!rolesBean.canEditUsers()) {
            return "/error/404";
        }

        try {
            Users user = usersDAO.getById(id);
            if (user == null) {
                return "/error/404";
            }

            temporaryUserId = user.getId();

            return "/admin/edituserrole";
        } catch (Exception ex) {
            return "listusers";
        }
    }

    @HttpAction(name = "edituserrole", pattern = "edit/{id}/userrole")
    public String goToEdituserrole(@Param(name = "id", encode = true) int id) {
        if (!rolesBean.canEditUsers()) {
            return "/error/404";
        }

        try {
            UsersRoles userRole = usersRolesDAO.getById(id);
            if (userRole == null) {
                return "/error/404";
            }

            temporaryUsersRolesId = id;
            temporaryUserId = userRole.getUsers().getId();
            temporaryRoleTypeId = userRole.getRoles().getId();

            if (userRole.getContests() != null) {
                temporaryContestId = userRole.getContests().getId();
            }
            if (userRole.getSeries() != null) {
                temporarySeriesId = userRole.getSeries().getId();
            }

            return "/admin/edituserrole";
        } catch (Exception ex) {
            return "listusers";
        }
    }

    @HttpAction(name = "deluserrole", pattern = "del/{id}/userrole")
    public String deleteUserrole(@Param(name = "id", encode = true) int id) {
        if (!rolesBean.canEditUsers()) {
            return "/error/404";
        }

        UsersRoles userRole = usersRolesDAO.getById(id);
        if (userRole != null) {
//            String login = userRole.getUsers().getLogin();

            usersRolesDAO.delete(userRole);

//            return "/admin/edituser";
            return "del_userrole";
        } else {
            return "/error/404";
        }
    }

    @HttpAction(name = "clock", pattern = "clock/{date}")
    public String goToClock(@Param(name = "date", encode = true) String date) {
        try {
            temporaryDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
        } catch (Exception e) {
        }
        if (temporaryDate == null) {
            try {
                Calendar c = Calendar.getInstance();
                Calendar d = Calendar.getInstance();
                d.setTime(new SimpleDateFormat("HH:mm").parse(date));

                c.set(Calendar.HOUR_OF_DAY, d.get(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, d.get(Calendar.MINUTE));
                c.set(Calendar.SECOND, d.get(Calendar.SECOND));
                c.set(Calendar.MILLISECOND, d.get(Calendar.MILLISECOND));

                temporaryDate = c.getTime();
            } catch (Exception e) {
            }
        }
        if (temporaryDate == null) {
            try {

                temporaryDate = new Date(System.currentTimeMillis() + Integer.parseInt(date) * 1000L);
            } catch (Exception e) {
                temporaryDate = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
            }
        }
        return "clock";
    }

    @HttpAction(name = "viewpdfasimage", pattern = "view/{id}/pdf")
    public String viewPdfAsImage(@Param(name = "id", encode = true) int id) throws IOException {
        try {
            String name = StringUtils.EMPTY;
            String mimetype = StringUtils.EMPTY;
            byte[] content = null;

            Problems problem = problemsDAO.getById(id);

            if (problem != null
                    && (problem.getSeries().getStartdate().after(new Date())
                    || problem.getSeries().getContests().getVisibility() == false)
                    && !rolesBean.canEditProblem(problem.getSeries().getContests().getId(), null)) {
                problem = null;
            }

            if (problem != null && problem.getFiles() != null) {
                name = problem.getName() + ".jpg";
                content = PdfToImage.convertPdf(problem.getFiles().getBytes());
                mimetype = "image/jpeg";
            }

            if (content != null) {
                FacesContext context = FacesContext.getCurrentInstance();

                HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
                response.setContentLength(content.length);
                response.setContentType(mimetype);
                response.setHeader("Content-Description", "File Transfer");
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.getOutputStream().write(content);
                response.getOutputStream().flush();
                response.getOutputStream().close();
                context.responseComplete();
                return null;
            } else {
                return "/error/404";
            }
        } catch (Exception ex) {
            return "/error/404";
        }
    }

    @HttpAction(name = "getfile", pattern = "get/{id}/{type}")
    public String getFile(@Param(name = "id", encode = true) int id, @Param(name = "type", encode = true) String type) throws IOException {
        try {
            String name = StringUtils.EMPTY;
            String mimetype = StringUtils.EMPTY;
            byte[] content = null;
            if ("file".equals(type)) {
                Problems problem = problemsDAO.getById(id);

                if (problem != null
                        && (problem.getSeries().getStartdate().after(new Date())
                            || !problem.getSeries().getContests().getVisibility())
                        && !rolesBean.canEditProblem(problem.getSeries().getContests().getId(), null)) {
                    problem = null;
                }

                if (problem != null && problem.getFiles() != null) {
                    Files file = problem.getFiles();
                    name = problem.getName();
                    if (file.getFilename()!=null && !file.getFilename().isEmpty()) {
                        name = file.getFilename();
                    }
                    if (file.getExtension() != null && !file.getExtension().isEmpty()) {
                        name = name + "." + file.getExtension();
                    }
                    content = file.getBytes();
                    mimetype = "application/" + file.getExtension();
                }
            } else if ("code".equals(type)) {
                Submits s = submitsDAO.getById(id);
                if (s != null
                            && (s.getUsers().getId().equals(sessionBean.getCurrentUser().getId())
                                || rolesBean.canRate(s.getProblems().getSeries().getContests().getId(),
                                                     s.getProblems().getSeries().getId()))) {
                    if (s.getCode() != null) {
                        name = s.getFilename();
                        content = s.getCode();
                        mimetype = "application/force-download";
                    }
                }
            } else if ("class".equals(type)) {
                if (rolesBean.canEditAnyProblem()) {
                    Classes c = classesDAO.getById(id);
                    if (c != null && c.getCode() != null) {
                        name = c.getFilename() + ".class";
                        content = c.getCode();
                        mimetype = "application/force-download";
                    }
                }
            } else if ("contest".equals(type)) {
                Contests contest = contestsDAO.getById(id);

                if (contest != null
                            && rolesBean.canEditContest(contest.getId(), null)) {

                    content = ZipFile.zipContest(contest);
                    name = ELFunctions.filterUri(contest.getName()) + ".zip";
                    mimetype = "application/force-download";
                }
            } else if ("serie".equals(type)) {
                Series serie = seriesDAO.getById(id);

                if (serie != null
                            && rolesBean.canEditSeries(serie.getContests().getId(), serie.getId())) {
                    content = ZipFile.zipSerie(serie);
                    name = ELFunctions.filterUri(serie.getContests().getName()) + "_" + ELFunctions.filterUri(serie.getName()) + ".zip";
                    mimetype = "application/force-download";
                }
            } else if ("problem".equals(type)) {
                Problems problem = problemsDAO.getById(id);

                if (problem != null
                            && rolesBean.canEditProblem(problem.getSeries().getContests().getId(),
                                                        problem.getSeries().getId())) {
                    content = ZipFile.zipProblem(problem);
                    name = ELFunctions.filterUri(problem.getAbbrev()) + "_" + ELFunctions.filterUri(problem.getName()) + ".zip";
                    mimetype = "application/force-download";
                }
            } else if ("test".equals(type)) {
                Tests test = testsDAO.getById(id);
                if (test != null) {
                    Problems problem = test.getProblems();
                    if (rolesBean.canEditProblem(problem.getSeries().getContests().getId(), null)) {
                        content = ZipFile.zipTest(test);
                        name = ELFunctions.filterUri(problem.getAbbrev()) + "_test" + test.getTestorder() + ".zip";
                        mimetype = "application/force-download";
                    }
                }
            } else if ("solutions".equals(type)) {
                Contests contest = contestsDAO.getById(id);

                if (contest != null
                            && rolesBean.canEditContest(contest.getId(), null)) {
                    List<Integer> submissionsList = Ranking.getInstance().getSolutions(id, null, contest.getType(), new Date(), false);
                    ZipSolutions zip = new ZipSolutions();
                    for (int sid : submissionsList) {
                        zip.addSubmit(submitsDAO.getById(sid));
                    }
                    content = zip.finish();
                    name = ELFunctions.filterUri(contest.getName()) + "_solutions.zip";
                    mimetype = "application/force-download";
                }
            }

            if (content != null) {
                FacesContext context = FacesContext.getCurrentInstance();

                HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
                response.setContentLength(content.length);
                response.setContentType(mimetype);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
                response.setHeader("Content-Description", "File Transfer");
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.getOutputStream().write(content);
                response.getOutputStream().flush();
                response.getOutputStream().close();
                context.responseComplete();
                return null;
            } else {
                return "/error/404";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "/error/404";
        }

    }

    public String saveTest() {
        Integer id = getEditedTest().getId();
        if ((ELFunctions.isNullOrZero(id) && !rolesBean.canEditProblem(temporaryContestId, temporarySeriesId)) || (!ELFunctions.isNullOrZero(id) && !rolesBean.canEditProblem(temporaryContestId, temporarySeriesId))) {
            return null;
        }

        try {
            editedTest.setVisibility(1); // FIXME: należy wartość pobrać ze strony!
            editedTest.setProblems(problemsDAO.getById(temporaryProblemId));
            testsDAO.saveOrUpdate(editedTest);
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditTest:save", summary, null);
            return null;
        }

        return "problems";
    }

    public String switchShowOnlyMy() {
        sessionBean.setShowOnlyMySubmissions(!sessionBean.isShowOnlyMySubmissions());
        submissions = null;
        sessionBean.setSubmissionsUserId(0);
        sessionBean.setSubmissionsSeriesId(0);
        sessionBean.setSubmissionsProblemId(0);

        return null;
    }

    /**
     * Validates component with captcha text entered.
     *
     * @param context   faces context in which component resides
     * @param component component to be validated
     * @param obj       data entered in component
     */
    public void validateCaptcha(FacesContext context, UIComponent component, Object obj) {
        String captcha = (String) obj;
        ExternalContext extContext = context.getExternalContext();
        String tmp = (String) extContext.getSessionMap().get("captchaSessionKeyName_captchaKey");

        if (captcha == null || tmp == null || !captcha.toLowerCase().equals(tmp.toLowerCase())) {
            ((HtmlInputText) component).setValid(false);
            String summary = messages.getString("bad_captcha");
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, component, summary, null);

            logger.trace("CAPTCHA incorrect: '" + captcha + "' != '" + tmp + "'");
        }
    }

    public void validateGprd(FacesContext context, UIComponent component, Object obj) {
        if ((Boolean) obj == false) {
            ((HtmlSelectBooleanCheckbox) component).setValid(false);
            String summary = messages.getString("gprd_required");
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, component, summary, null);
        }
    }

    public void validatePasswd(FacesContext context, UIComponent component, Object obj) {
        if (!sessionBean.getCurrentUser().checkPass((String) obj)) {
            ((HtmlInputSecret) component).setValid(false);
            String summary = messages.getString("incorrect_passwd");
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, component, summary, null);
        }
    }

    public void submissionsScrollerAction(ActionEvent event) {
        ScrollerActionEvent scrollerEvent = (ScrollerActionEvent) event;
        if (scrollerEvent.getPageIndex() == -1 && scrollerEvent.getScrollerfacet() != null) {
            if ("next".equals(scrollerEvent.getScrollerfacet())) {
                sessionBean.setSubmissionsPageIndex(sessionBean.getSubmissionsPageIndex() + 1);
            } else if ("previous".equals(scrollerEvent.getScrollerfacet())) {
                sessionBean.setSubmissionsPageIndex(sessionBean.getSubmissionsPageIndex() - 1);
            }
        } else {
            sessionBean.setSubmissionsPageIndex(scrollerEvent.getPageIndex() - 1);
        }
        submissions = null;
    }

    private Problems findProblemFromFilename(String filename) {
        int index;
        int count = 0;
        int longest = -1;
        int min_index = Integer.MAX_VALUE;

        Problems problem = null;

        for (Problems p : getSubmittableProblems()) {
            index = filename.indexOf(p.getAbbrev().toLowerCase());
            if (index >= 0) {
                if (index < min_index || (index == min_index && p.getAbbrev().length() > longest)) {
                    problem = p;
                    min_index = index;
                    longest = p.getAbbrev().length();
                    count = 0;
                } else {
                    ++count;
                }
            }
        }

        if (count > 0) {
            return null;
        } else {
            return problem;
        }
    }

    public String getClientIp() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
    }

    private String sendSolution(byte[] bytes, String fileName, String controlId) {
        FacesContext context = FacesContext.getCurrentInstance();
        long submitTime = System.currentTimeMillis();

        try {
            Problems problem = null;
            Languages language = null;

            if (fileName == null) {
                if (temporaryProblemId == 0) {
                    List<Problems> p = getSubmittableProblems();
                    if (p.size() == 1) {
                        problem = p.get(0);
                    }
                } else {
                    problem = problemsDAO.getById(temporaryProblemId);
                }

                if (temporaryLanguageId == 0) {
                    if (problem != null) {
                        List<LanguagesProblems> lp = problem.getLanguagesProblemss();
                        if (lp.size() == 1) {
                            language = lp.get(0).getLanguages();
                        }
                    }
                } else {
                    language = languagesDAO.getById(temporaryLanguageId);
                }
                if (problem != null && language != null) {
                    fileName = "source_" + problem.getAbbrev().replaceAll("[^A-Za-z0-9_-]", "_") + "." + language.getExtension();
                }
            } else {
                fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                /*
                 * odgadywanie zadania z nazwy
                 */
                if (temporaryProblemId == 0) {
                    String filename_tmp = fileName;

                    int index = filename_tmp.indexOf(".");
                    if (index >= 0) {
                        filename_tmp = filename_tmp.substring(0, index);
                    }

                    problem = findProblemFromFilename(filename_tmp);
                    if (problem == null) {
                        problem = findProblemFromFilename(filename_tmp.toLowerCase());
                    }
                } else {
                    problem = problemsDAO.getById(temporaryProblemId);
                }
                if (temporaryLanguageId == 0) {
                    if (problem != null) {
                        int longest = -1;
                        for (LanguagesProblems lp : problem.getLanguagesProblemss()) {
                            if (fileName.endsWith("." + lp.getLanguages().getExtension()) && lp.getLanguages().getExtension().length() > longest) {
                                language = lp.getLanguages();
                                longest = language.getExtension().length();
                            }
                        }
                    }
                } else {
                    language = languagesDAO.getById(temporaryLanguageId);
                }
            }

            /*
             * upewnijmy się, że rozwiązanie jest wysłane w języku, który jest
             * akceptowany przez zadanie
             */
            if (problem != null && language != null) {
                boolean hack = true;
                int language_id = language.getId();
                for (LanguagesProblems lp : problem.getLanguagesProblemss()) {
                    if (lp.getLanguages().getId() == language_id) {
                        hack = false;
                        break;
                    }
                }
                if (hack == true) {
                    language = null;
                }
            }

            if (problem == null) {
                String summary = String.format("%s", messages.getString("problem_autorecognize_error"));
                WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formSubmit:problem", summary, null);
            }

            if (language == null) {
                String summary = String.format("%s", messages.getString("language_autorecognize_error"));
                WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formSubmit:language", summary, null);
            }

            if (problem == null || language == null) {
                return null;
            }

            if (problem.getCodesize() != null && problem.getCodesize() > 0 && bytes.length > problem.getCodesize() * 1024) {
                String summary = String.format("%s", messages.getString("problem_codesize_error"));
                WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, controlId, summary, null);
                return null;
            }

            String clientIp = getClientIp();
            if (rolesBean.canEditProblem(problem.getSeries().getContests().getId(), problem.getSeries().getId()) == true
                    || (ELFunctions.isValidIP(problem.getSeries().getOpenips(), clientIp)
                    && problem.getSeries().getStartdate().getTime() < submitTime
                    && (problem.getSeries().getEnddate() == null || submitTime < problem.getSeries().getEnddate().getTime()))) {
                Submits submit = new Submits();

                submit.setId(null);
                submit.setFilename(fileName);
                submit.setCode(bytes);
                submit.setState(SubmitsStateEnum.WAIT.getCode());
                submit.setLanguages(language);
                submit.setProblems(problem);
                submit.setSdate(new Timestamp(submitTime));
                submit.setUsers(usersDAO.getById(sessionBean.getCurrentUser().getId()));
                submit.setClientip(clientIp);
                if (rolesBean.canEditProblem(problem.getSeries().getContests().getId(), problem.getSeries().getId()) == true
                        && !(problem.getSeries().getStartdate().getTime() < submitTime
                        && (problem.getSeries().getEnddate() == null || submitTime < problem.getSeries().getEnddate().getTime()))) {
                    submit.setVisibleinranking(false);
                } else {
                    submit.setVisibleinranking(true);
                }
                selectContest(problem.getSeries().getContests().getId());
                submitsDAO.saveOrUpdate(submit);

                HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
                HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

                JudgeManagerConnector.getInstance().sentToJudgeManager(submit.getId());

                return "submissions";
            } else {
                String summary = String.format("%s", messages.getString("submission_not_in_time"));
                WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, controlId, summary, null);
                return null;
            }
        } catch (Exception e) {
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, controlId, summary, null);
            return null;
        }
    }

    private void selectContest(int id) {
        if (getCurrentContest() == null || getCurrentContest().getId() != id) {
            sessionBean.setCurrentContestId(id);
            currentContest = null;
            if (rolesBean.canRateAnySeries(getCurrentContest()) == false) {
                sessionBean.setShowOnlyMySubmissions(true);
            }
        }
    }

    /**
     * @return the dummy
     */
    public String getDummy() {
        return dummy;
    }

    /**
     * @param dummy the dummy to set
     */
    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    /**
     * @return the users
     */
    public List<Users> getUsers() {
        if (users == null) {
            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Users.class);
            c.addOrder(Order.asc("lastname"));
            c.addOrder(Order.asc("firstname"));
            c.addOrder(Order.asc("login"));
            users = c.list();
        }
        return users;
    }

    public Integer getRatingEditNote() {
        return ratingEditNote;
    }

    public void setRatingEditNote(Integer ratingEditNote) {
        this.ratingEditNote = ratingEditNote;
    }

    public Integer getTemporarySubmitResultId() {
        getEditedResult();
        return temporarySubmitResultId;
    }

    public void setTemporarySubmitResultId(Integer id) {
        temporarySubmitResultId = id;
    }

    /**
     * @return the temporaryDate
     */
    public Date getTemporaryDate() {
        if (temporaryDate == null) {
            temporaryDate = new Date();
        }
        return temporaryDate;
    }

    /**
     * @param temporaryDate the temporaryDate to set
     */
    public void setTemporaryDate(Date temporaryDate) {
        this.temporaryDate = temporaryDate;
    }

    public Integer getTemporaryUsersRolesId() {
        return temporaryUsersRolesId;
    }

    public void setTemporaryUsersRolesId(Integer temporaryUsersRolesId) {
        this.temporaryUsersRolesId = temporaryUsersRolesId;
    }

    public Integer getTemporaryRoleTypeId() {
        return temporaryRoleTypeId;
    }

    public void setTemporaryRoleTypeId(Integer temporaryRoleTypeId) {
        this.temporaryRoleTypeId = temporaryRoleTypeId;
    }
}
