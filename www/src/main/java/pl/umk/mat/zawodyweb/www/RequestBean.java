package pl.umk.mat.zawodyweb.www;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.datascroller.ScrollerActionEvent;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.restfaces.annotation.HttpAction;
import org.restfaces.annotation.Instance;
import org.restfaces.annotation.Param;
import pl.umk.mat.zawodyweb.database.ClassesDAO;
import pl.umk.mat.zawodyweb.database.ContestsDAO;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.LanguagesDAO;
import pl.umk.mat.zawodyweb.database.LanguagesProblemsDAO;
import pl.umk.mat.zawodyweb.database.PDFDAO;
import pl.umk.mat.zawodyweb.database.ProblemsDAO;
import pl.umk.mat.zawodyweb.database.QuestionsDAO;
import pl.umk.mat.zawodyweb.database.ResultsDAO;
import pl.umk.mat.zawodyweb.database.SeriesDAO;
import pl.umk.mat.zawodyweb.database.SubmitsDAO;
import pl.umk.mat.zawodyweb.database.SubmitsResultEnum;
import pl.umk.mat.zawodyweb.database.TestsDAO;
import pl.umk.mat.zawodyweb.database.UsersDAO;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Classes;
import pl.umk.mat.zawodyweb.database.pojo.Contests;
import pl.umk.mat.zawodyweb.database.pojo.Languages;
import pl.umk.mat.zawodyweb.database.pojo.LanguagesProblems;
import pl.umk.mat.zawodyweb.database.pojo.PDF;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Questions;
import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.database.pojo.Submits;
import pl.umk.mat.zawodyweb.database.pojo.Tests;
import pl.umk.mat.zawodyweb.database.pojo.Users;
import pl.umk.mat.zawodyweb.www.datamodels.PagedDataModel;
import pl.umk.mat.zawodyweb.www.ranking.Ranking;
import pl.umk.mat.zawodyweb.www.ranking.RankingTable;

/**
 *
 * @author slawek
 */
@Instance("#{requestBean}")
public class RequestBean {

    private final ResourceBundle messages = ResourceBundle.getBundle("pl.umk.mat.zawodyweb.www.Messages");
    private SubmitsDAO submitsDAO = DAOFactory.DEFAULT.buildSubmitsDAO();
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
    private PDFDAO pdfDAO = DAOFactory.DEFAULT.buildPDFDAO();
    private SessionBean sessionBean;
    private RolesBean rolesBean;
    private Users newUser = new Users();
    private String repPasswd;
    private String actualPasswd;
    private Contests editedContest;
    private Contests currentContest;
    private Series editedSeries;
    private Problems editedProblem;
    private Users editedUser;
    private Tests editedTest;
    private Submits editedSubmit;
    private Questions editedQuestion;
    private Results editedResult;
    private List<LanguagesProblems> temporaryLanguagesProblems = null;
    private List<Contests> contests = null;
    private List<Series> contestsSeries = null;
    private List<Problems> seriesProblems = null;
    private List<Problems> contestsProblems = null;
    private List<Classes> diffClasses = null;
    private List<Languages> languages = null;
    private List<Series> currentContestSeries = null;
    private List<Questions> currentContestQuestions = null;
    private RankingTable currentContestRanking = null;
    private PagedDataModel submissions = null;
    private Integer temporaryContestId;
    private Integer temporarySeriesId;
    private Integer temporaryProblemId;
    private Integer temporarySubmitId;
    private Integer temporaryClassId;
    private Integer temporaryTestId;
    private Integer temporaryQuestionId;
    private Integer[] temporaryLanguagesIds;
    private Integer temporaryLanguageId;
    private Integer temporaryResultId;
    private int temporaryPageIndex = 0;
    private String temporarySource;
    private Problems currentProblem;
    private Submits currentSubmit;
    private String dummy;
    private List<Problems> submittableProblems;
    private UploadedFile temporaryFile;
    private String answer;
    private boolean publicAnswer;
    private boolean deletePdf;
    private boolean showOnlyMySubmissions = true;
    private boolean ratingMode = false;

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
            c.addOrder(Order.desc("startdate"));
            contests = c.list();
        }

        return contests;
    }

    public List<Classes> getDiffClasses() {
        if (diffClasses == null) {
            diffClasses = classesDAO.findAll();
            List<Languages> tmp = languagesDAO.findAll();
            for (Languages l : tmp) {
                diffClasses.remove(l.getClasses());
            }
        }

        return diffClasses;
    }

    public List<Languages> getLanguages() {
        if (languages == null) {
            languages = languagesDAO.findAll();
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
            s.add(Restrictions.or(Restrictions.isNull("enddate"), Restrictions.gt("enddate", new Date())));
            submittableProblems = c.list();
        }

        return submittableProblems;
    }

    public List<Series> getCurrentContestSeries() {
        if (currentContestSeries == null) {
            currentContestSeries = seriesDAO.findByContestsid(getCurrentContest().getId());
            Collections.reverse(currentContestSeries);
        }

        return currentContestSeries;
    }

    public List<Questions> getCurrentContestQuestions() {
        if (currentContestQuestions == null) {
            currentContestQuestions = questionsDAO.findByCriteria(Restrictions.eq("contests.id", getCurrentContest().getId()),
                    Restrictions.or(Restrictions.eq("qtype", 1), Restrictions.eq("users.id", sessionBean.getCurrentUser().getId())));
        }

        return currentContestQuestions;
    }

    public RankingTable getCurrentContestRanking() {
        if (currentContestRanking == null && getCurrentContest() != null) {
            Integer contests_id = getCurrentContest().getId();
            Integer type = getCurrentContest().getType();
            Date date = new Date();
            currentContestRanking = Ranking.getInstance().getRanking(contests_id, type, date, false);
        }

        return currentContestRanking;
    }

    public PagedDataModel getSubmissions() {
        if (submissions == null) {
            List<Integer> ratableSeries = null;

            if (!isShowOnlyMySubmissions()) {
                ratableSeries = new ArrayList<Integer>();
                for (Series s : getCurrentContest().getSeriess()) {
                    if (rolesBean.canRate(getCurrentContest().getId(), s.getId())) {
                        ratableSeries.add(s.getId());
                    }
                }
            }

            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Submits.class);
            c.setProjection(Projections.rowCount());
            Criteria criteriaSeries = c.createCriteria("problems").createCriteria("series");
            criteriaSeries.createCriteria("contests").add(Restrictions.eq("id", getCurrentContest().getId()));

            if (isShowOnlyMySubmissions()) {
                c.createCriteria("users").add(Restrictions.eq("id", sessionBean.getCurrentUser().getId()));
            } else {
                criteriaSeries.add(Restrictions.in("id", ratableSeries));
            }
            Number number = (Number) c.uniqueResult();

            Criteria c2 = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Submits.class);
            Criteria criteria2Series = c2.createCriteria("problems").createCriteria("series");
            criteria2Series.createCriteria("contests").add(Restrictions.eq("id", getCurrentContest().getId()));
            if (isShowOnlyMySubmissions()) {
                c2.createCriteria("users").add(Restrictions.eq("id", sessionBean.getCurrentUser().getId()));
            } else {
                criteria2Series.add(Restrictions.in("id", ratableSeries));
            }
            c2.addOrder(Order.desc("sdate"));
            c2.setFirstResult(temporaryPageIndex * 10);
            c2.setMaxResults(10);
            submissions = new PagedDataModel(c2.list(), number.intValue());
        }

        return submissions;
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
            } else {
                editedContest = contestsDAO.getById(temporaryContestId);
            }
        }

        return editedContest;
    }

    public Results getEditedResult() {
        if (editedResult == null && !ELFunctions.isNullOrZero(temporaryResultId)) {
            editedResult = resultsDAO.getById(temporaryResultId);
        }

        return editedResult;
    }

    public Users getEditedUser() {
        if (editedUser == null) {
            editedUser = usersDAO.getById(sessionBean.getCurrentUser().getId());
        }

        return editedUser;
    }

    public Contests getCurrentContest() {
        if (currentContest == null) {
            if (sessionBean.getCurrentContestId() != null) {
                currentContest = contestsDAO.getById(sessionBean.getCurrentContestId());
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

    public Integer getTemporaryClassId() {
        return temporaryClassId;
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

    public UploadedFile getTemporaryFile() {
        return temporaryFile;
    }

    public void setTemporaryFile(UploadedFile temporaryFile) {
        this.temporaryFile = temporaryFile;
    }

    public Boolean getDeletePdf() {
        return deletePdf;
    }

    public void setDeletePdf(Boolean deletePdf) {
        this.deletePdf = deletePdf;
    }

    public boolean isPublicAnswer() {
        return publicAnswer;
    }

    public void setPublicAnswer(boolean publicAnswer) {
        this.publicAnswer = publicAnswer;
    }

    public boolean isShowOnlyMySubmissions() {
        return showOnlyMySubmissions;
    }

    public void setShowOnlyMySubmissions(boolean showOnlyMySubmissions) {
        this.showOnlyMySubmissions = showOnlyMySubmissions;
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
                newUser.savePass(newUser.getPass());
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

        return "start";
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
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formPasswd:save", summary, null);
            return null;
        }

        return "profil";
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

        return sendSolution(temporarySource.getBytes(), "source", "formSubmit:sendcode");
    }

    public String saveProblem() {
        Integer id = getEditedProblem().getId();
        if ((ELFunctions.isNullOrZero(id) && !rolesBean.canAddProblem(temporaryContestId, temporarySeriesId)) || (!ELFunctions.isNullOrZero(id) && !rolesBean.canEditProblem(temporaryContestId, temporarySeriesId))) {
            return null;
        }

        try {
            if (temporaryFile != null) {
                PDF current = getEditedProblem().getPDF();
                if (current == null) {
                    current = new PDF();
                }

                current.setPdf(temporaryFile.getBytes());
                pdfDAO.saveOrUpdate(current);
                getEditedProblem().setPDF(current);
            }

            if (deletePdf) {
                PDF tmp = getEditedProblem().getPDF();
                getEditedProblem().setPDF(null);
                pdfDAO.delete(tmp);
            }

            getEditedProblem().setSeries(seriesDAO.getById(temporarySeriesId));
            getEditedProblem().setClasses(classesDAO.getById(temporaryClassId));

            List<LanguagesProblems> tmpList = languagesProblemsDAO.findByProblemsid(getEditedProblem().getId());

            if (tmpList != null) {
                for (LanguagesProblems lp : tmpList) {
                    languagesProblemsDAO.delete(lp);
                }
            }

            for (Integer lid : temporaryLanguagesIds) {
                LanguagesProblems lp = new LanguagesProblems();
                lp.setLanguages(languagesDAO.getById(lid));
                lp.setProblems(getEditedProblem());
                languagesProblemsDAO.saveOrUpdate(lp);
            }

            problemsDAO.saveOrUpdate(getEditedProblem());
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditProblem:save", summary, null);
            return null;
        }

        selectContest(editedProblem.getSeries().getContests().getId());
        return "problems";
    }

    public String addQuestion() {
        try {
            getEditedQuestion().setContests(getCurrentContest());
            getEditedQuestion().setProblems(problemsDAO.getById(temporaryProblemId));
            getEditedQuestion().setQtype(0);
            getEditedQuestion().setUsers(sessionBean.getCurrentUser());

            questionsDAO.save(getEditedQuestion());

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
        resultsDAO.update(getEditedResult());
        temporaryResultId = null;
        return null;
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
            return "ranking";
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
        if (getCurrentContest() == null) {
            return "/error/404";
        } else {
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
            return null;
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
            seriesDAO.delete(s);
            return "problems";
        } else {
            return null;
        }
    }

    @HttpAction(name = "deltest", pattern = "del/{id}/test")
    public String deleteTest(@Param(name = "id", encode = true) int id) {
        Tests s = testsDAO.getById(id);
        if (s != null && rolesBean.canEditProblem(s.getProblems().getId(), s.getProblems().getSeries().getId())) {
            testsDAO.delete(s);
            return "problems";
        } else {
            return null;
        }
    }

    @HttpAction(name = "addproblem", pattern = "add/{id}/problem")
    public String goToAddproblem(@Param(name = "id", encode = true) int id) {
        Series s = seriesDAO.getById(id);
        if (s != null) {
            temporarySeriesId = id;
            temporaryContestId = s.getContests().getId();
        }

        return "/admin/editproblem";
    }

    @HttpAction(name = "delproblem", pattern = "del/{id}/problem")
    public String deleteProblem(@Param(name = "id", encode = true) int id) {
        Problems p = problemsDAO.getById(id);

        if (p != null && rolesBean.canDeleteProblem(p.getSeries().getContests().getId(), p.getSeries().getId())) {
            problemsDAO.delete(p);
            return "problems";
        } else {
            return null;
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

    @HttpAction(name = "getfile", pattern = "get/{id}/{type}")
    public String getFile(@Param(name = "id", encode = true) int id, @Param(name = "type", encode = true) String type) throws IOException {
        String name = StringUtils.EMPTY;
        String mimetype = StringUtils.EMPTY;
        byte[] content = null;

        if (type.equals("pdf")) {
            Problems p = problemsDAO.getById(id);
            if (p != null && p.getPDF() != null) {
                name = p.getName() + ".pdf";
                content = p.getPDF().getPdf();
                mimetype = "application/pdf";
            }
        } else if (type.equals("code")) {
            Submits s = submitsDAO.getById(id);
            if (s != null && s.getCode() != null) {
                name = s.getFilename();
                content = s.getCode();
                mimetype = "application/octet-stream";
            }
        }

        if (content != null) {
            FacesContext context = FacesContext.getCurrentInstance();

            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.setContentType(mimetype);
            response.setHeader("Content-Disposition", "attachment;filename=\"" + name + "\"");
            response.getOutputStream().write(content);
            response.getOutputStream().flush();
            response.getOutputStream().close();
            context.responseComplete();
            return null;
        } else {
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
        showOnlyMySubmissions = !showOnlyMySubmissions;
        submissions = null;
        return null;
    }

    /**
     * Validates component with captcha text entered.
     *
     * @param context
     *            faces context in which component resides
     * @param component
     *            component to be validated
     * @param obj
     *            data entered in component
     */
    public void validateCaptcha(FacesContext context, UIComponent component, Object obj) {
        String captcha = (String) obj;
        ExternalContext extContext = context.getExternalContext();
        String tmp = (String) extContext.getSessionMap().get("captchaKey");

        if (!captcha.toLowerCase().equals(tmp.toLowerCase())) {
            ((HtmlInputText) component).setValid(false);
            String summary = messages.getString("bad_captcha");
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
        temporaryPageIndex = scrollerEvent.getPageIndex() - 1;
        submissions = null;
    }

    private String sendSolution(byte[] bytes, String fileName, String controlId) {
        FacesContext context = FacesContext.getCurrentInstance();

        fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
        if (temporaryLanguageId.equals(-1)) {
            // TODO: tutaj należałoby dodać obliczanie temporaryLanguageId z nazwy pliku
            // a do submit.jspx dodać "< auto >" i "-1" jako wartość lub inaczej to rozwiązać (np. puste i nie required)
        }

        try {
            Problems problem = problemsDAO.getById(temporaryProblemId);
            if (problem.getSeries().getEnddate().after(new Date())) {
                Submits submit = new Submits();

                submit.setId(null);
                submit.setFilename(fileName);
                submit.setCode(bytes);
                submit.setResult(SubmitsResultEnum.WAIT.getCode());
                submit.setLanguages(languagesDAO.getById(temporaryLanguageId));
                submit.setProblems(problem);
                submit.setSdate(new Timestamp(System.currentTimeMillis()));
                submit.setUsers(usersDAO.getById(sessionBean.getCurrentUser().getId()));
                selectContest(problem.getSeries().getContests().getId());
                submitsDAO.saveOrUpdate(submit);

                JudgeManagerConnector.getInstance().sentToJudgeManager(submit.getId());
            }

            return "submissions";
        } catch (Exception e) {
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, controlId, summary, null);
            return null;
        }
    }

    private void selectContest(int id) {
        if (getCurrentContest() == null || !getCurrentContest().getId().equals(id)) {
            sessionBean.setCurrentContestId(id);
            currentContest = null;
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
}
