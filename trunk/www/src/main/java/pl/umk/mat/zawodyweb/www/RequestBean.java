package pl.umk.mat.zawodyweb.www;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
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
import pl.umk.mat.zawodyweb.database.SeriesDAO;
import pl.umk.mat.zawodyweb.database.SubmitsDAO;
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
import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.database.pojo.Submits;
import pl.umk.mat.zawodyweb.database.pojo.Tests;
import pl.umk.mat.zawodyweb.database.pojo.Users;
import pl.umk.mat.zawodyweb.www.datamodels.PagedDataModel;

/**
 *
 * @author slawek
 */
@Instance("#{requestBean}")
public class RequestBean {

    private final ResourceBundle messages = ResourceBundle.getBundle("pl.umk.mat.zawodyweb.www.Messages");
    private SessionBean sessionBean;
    private RolesBean rolesBean;
    private Users newUser = new Users();
    private String repPasswd;
    private Contests editedContest;
    private Series editedSeries;
    private Problems editedProblem;
    private Tests editedTest;
    private Submits editedSubmit;
    private Questions editedQuestion;
    private List<LanguagesProblems> temporaryLanguagesProblems = null;
    private List<Contests> contests = null;
    private List<Series> contestsSeries = null;
    private List<Problems> seriesProblems = null;
    private List<Problems> contestsProblems = null;
    private List<Classes> diffClasses = null;
    private List<Languages> languages = null;
    private List<Series> currentContestSeries = null;
    private List<Questions> currentContestQuestions = null;
    private PagedDataModel submissions = null;
    private Integer temporaryContestId;
    private Integer temporarySeriesId;
    private Integer temporaryProblemId;
    private Integer temporaryClassId;
    private Integer temporaryTestId;
    private Integer temporaryQuestionId;
    private Integer[] temporaryLanguagesIds;
    private Integer temporaryLanguageId;
    private int temporaryPageIndex = 0;
    private String temporarySource;
    private Problems currentProblem;
    private String dummy;
    private List<Problems> submittableProblems;
    private UploadedFile temporaryFile;
    private String answer;
    private boolean publicAnswer;
    private boolean deletePdf;

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

    /**
     * @return the repPasswd
     */
    public String getRepPasswd() {
        return repPasswd;
    }

    /**
     * @param repPasswd the repPasswd to set
     */
    public void setRepPasswd(String repPasswd) {
        this.repPasswd = repPasswd;
    }

    public List<Contests> getContests() {
        if (contests == null) {
            ContestsDAO dao = DAOFactory.DEFAULT.buildContestsDAO();
            contests = dao.findAll();
        }

        return contests;
    }

    public List<Classes> getDiffClasses() {
        if (diffClasses == null) {
            ClassesDAO dao = DAOFactory.DEFAULT.buildClassesDAO();
            LanguagesDAO ldao = DAOFactory.DEFAULT.buildLanguagesDAO();

            diffClasses = dao.findAll();
            List<Languages> tmp = ldao.findAll();
            for (Languages l : tmp) {
                diffClasses.remove(l.getClasses());
            }
        }

        return diffClasses;
    }

    public List<Languages> getLanguages() {
        if (languages == null) {
            LanguagesDAO dao = DAOFactory.DEFAULT.buildLanguagesDAO();
            languages = dao.findAll();
        }

        return languages;
    }

    public List<Series> getContestsSeries() {
        if (contestsSeries == null) {
            SeriesDAO dao = DAOFactory.DEFAULT.buildSeriesDAO();
            if (temporaryContestId == null) {
                temporaryContestId = getTemporaryContestId();
            }
            contestsSeries = dao.findByContestsid(temporaryContestId);
        }

        return contestsSeries;
    }

    public List<Problems> getSeriesProblems() {
        if (seriesProblems == null) {
            ProblemsDAO dao = DAOFactory.DEFAULT.buildProblemsDAO();
            seriesProblems = dao.findBySeriesid(temporarySeriesId);
        }

        return seriesProblems;
    }

    public List<Problems> getContestsProblems() {
        if (contestsProblems == null && sessionBean.getCurrentContest() != null) {
            SeriesDAO sdao = DAOFactory.DEFAULT.buildSeriesDAO();

            List<Series> series = sdao.findByContestsid(sessionBean.getCurrentContest().getId());
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
            ProblemsDAO dao = DAOFactory.DEFAULT.buildProblemsDAO();
            submittableProblems = dao.findAll();
        }

        return submittableProblems;
    }

    public List<Series> getCurrentContestSeries() {
        if (currentContestSeries == null) {
            SeriesDAO dao = DAOFactory.DEFAULT.buildSeriesDAO();
            currentContestSeries = dao.findByContestsid(sessionBean.getCurrentContest().getId());
            Collections.reverse(currentContestSeries);
        }

        return currentContestSeries;
    }

    public List<Questions> getCurrentContestQuestions() {
        if (currentContestQuestions == null) {
            QuestionsDAO dao = DAOFactory.DEFAULT.buildQuestionsDAO();
            currentContestQuestions = dao.findByCriteria(Restrictions.eq("contests.id", sessionBean.getCurrentContest().getId()),
                    Restrictions.or(Restrictions.eq("qtype", 1), Restrictions.eq("users.id", sessionBean.getCurrentUser().getId())));
        }

        return currentContestQuestions;
    }

    public PagedDataModel getSubmissions() {
        if (submissions == null) {
            Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Submits.class);
            c.setProjection(Projections.rowCount());
            c.createCriteria("problems").createCriteria("series").createCriteria("contests").add(Restrictions.eq("id", sessionBean.getCurrentContest().getId()));
            c.createCriteria("users").add(Restrictions.eq("id", sessionBean.getCurrentUser().getId()));
            Number number = (Number) c.uniqueResult();

            Criteria c2 = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Submits.class);
            c2.createCriteria("problems").createCriteria("series").createCriteria("contests").add(Restrictions.eq("id", sessionBean.getCurrentContest().getId()));
            c2.createCriteria("users").add(Restrictions.eq("id", sessionBean.getCurrentUser().getId()));
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
                ContestsDAO dao = DAOFactory.DEFAULT.buildContestsDAO();
                editedContest = dao.getById(temporaryContestId);
            }
        }

        return editedContest;
    }

    public Submits getEditedSubmit() {
        if (editedSubmit == null) {
            editedSubmit = new Submits();
        }

        return editedSubmit;
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
                QuestionsDAO dao = DAOFactory.DEFAULT.buildQuestionsDAO();
                editedQuestion = dao.getById(temporaryQuestionId);
                if (editedQuestion != null && !WWWHelper.isPost(context)) {
                    publicAnswer = editedQuestion.getQtype().equals(1);
                }
            }
        }

        return editedQuestion;
    }

    public List<LanguagesProblems> getTemporaryLanguagesProblems() {
        if (temporaryLanguagesProblems == null) {
            LanguagesProblemsDAO dao = DAOFactory.DEFAULT.buildLanguagesProblemsDAO();
            temporaryLanguagesProblems = dao.findByProblemsid(temporaryProblemId);
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
                SeriesDAO dao = DAOFactory.DEFAULT.buildSeriesDAO();
                editedSeries = dao.getById(temporarySeriesId);

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
                ProblemsDAO dao = DAOFactory.DEFAULT.buildProblemsDAO();
                editedProblem = dao.getById(temporaryProblemId);

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
                TestsDAO dao = DAOFactory.DEFAULT.buildTestsDAO();
                editedTest = dao.getById(temporaryTestId);

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
                UsersDAO dao = DAOFactory.DEFAULT.buildUsersDAO();
                dao.save(newUser);
            } catch (ConstraintViolationException e) {
                String summary = messages.getString("login_exists");
                WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formRegister:login", summary, null);
                newUser.setPass(StringUtils.EMPTY);
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
        Integer id = editedContest.getId();
        if ((ELFunctions.isNullOrZero(id) && !rolesBean.canAddContest(null, null)) || (!ELFunctions.isNullOrZero(id) && !rolesBean.canEditContest(id, null))) {
            return null;
        }

        try {
            ContestsDAO dao = DAOFactory.DEFAULT.buildContestsDAO();
            dao.saveOrUpdate(editedContest);
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
            ContestsDAO cdao = DAOFactory.DEFAULT.buildContestsDAO();
            editedSeries.setContests(cdao.getById(temporaryContestId));

            SeriesDAO dao = DAOFactory.DEFAULT.buildSeriesDAO();
            dao.saveOrUpdate(editedSeries);
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditSeries:save", summary, null);
            return null;
        }

        return "start";
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
        try {
            ProblemsDAO dao = DAOFactory.DEFAULT.buildProblemsDAO();
            SeriesDAO sdao = DAOFactory.DEFAULT.buildSeriesDAO();
            LanguagesDAO ldao = DAOFactory.DEFAULT.buildLanguagesDAO();
            LanguagesProblemsDAO lpdao = DAOFactory.DEFAULT.buildLanguagesProblemsDAO();
            ClassesDAO cdao = DAOFactory.DEFAULT.buildClassesDAO();
            PDFDAO pdao = DAOFactory.DEFAULT.buildPDFDAO();

            if (temporaryFile != null) {
                PDF current = getEditedProblem().getPDF();
                if (current == null) {
                    current = new PDF();
                }

                current.setPdf(temporaryFile.getBytes());
                pdao.saveOrUpdate(current);
                getEditedProblem().setPDF(current);
            }

            if (deletePdf) {
                PDF tmp = getEditedProblem().getPDF();
                getEditedProblem().setPDF(null);
                pdao.delete(tmp);
            }

            getEditedProblem().setSeries(sdao.getById(temporarySeriesId));
            getEditedProblem().setClasses(cdao.getById(temporaryClassId));

            List<LanguagesProblems> tmpList = lpdao.findByProblemsid(getEditedProblem().getId());

            if (tmpList != null) {
                for (LanguagesProblems lp : tmpList) {
                    lpdao.delete(lp);
                }
            }

            for (Integer lid : temporaryLanguagesIds) {
                LanguagesProblems lp = new LanguagesProblems();
                lp.setLanguages(ldao.getById(lid));
                lp.setProblems(getEditedProblem());
                lpdao.saveOrUpdate(lp);
            }

            dao.saveOrUpdate(getEditedProblem());
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditProblem:save", summary, null);
            return null;
        }

        sessionBean.selectContest(editedProblem.getSeries().getContests().getId());
        return "problems";
    }

    public String addQuestion() {
        try {
            QuestionsDAO dao = DAOFactory.DEFAULT.buildQuestionsDAO();
            ProblemsDAO pdao = DAOFactory.DEFAULT.buildProblemsDAO();
            UsersDAO udao = DAOFactory.DEFAULT.buildUsersDAO();

            getEditedQuestion().setContests(sessionBean.getCurrentContest());
            getEditedQuestion().setProblems(pdao.getById(temporaryProblemId));
            getEditedQuestion().setQtype(0);
            getEditedQuestion().setUsers(sessionBean.getCurrentUser());

            dao.save(getEditedQuestion());

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
            QuestionsDAO dao = DAOFactory.DEFAULT.buildQuestionsDAO();
            String tmp = getEditedQuestion().getQuestion().replaceAll("\n", "\n> ");
            tmp = ">".concat(tmp).concat("\n\n").concat(answer);
            getEditedQuestion().setQuestion(tmp);
            getEditedQuestion().setQtype(publicAnswer ? 1 : 0);
            dao.saveOrUpdate(getEditedQuestion());
            return "questions";
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formQuestion:save", summary, null);
            return null;
        }
    }

    @HttpAction(name = "problems", pattern = "problems/{id}/{title}")
    public String goToProblems(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        sessionBean.selectContest(id);
        if (sessionBean.getCurrentContest() == null) {
            return "/error/404";
        } else {
            return "problems";
        }
    }

    @HttpAction(name = "questions", pattern = "questions/{id}/{title}")
    public String goToQuestions(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        sessionBean.selectContest(id);
        if (sessionBean.getCurrentContest() == null) {
            return "/error/404";
        } else {
            return "questions";
        }
    }

    @HttpAction(name = "submissions", pattern = "submissions/{id}/{title}")
    public String goToSubmissions(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        sessionBean.selectContest(id);
        if (sessionBean.getCurrentContest() == null) {
            return "/error/404";
        } else {
            return "submissions";
        }
    }

    @HttpAction(name = "problem", pattern = "problem/{id}/{title}")
    public String goToProblem(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        ProblemsDAO dao = DAOFactory.DEFAULT.buildProblemsDAO();
        currentProblem = dao.getById(id);

        if (currentProblem == null) {
            return "/error/404";
        } else {
            sessionBean.selectContest(currentProblem.getSeries().getContests().getId());
            return "problem";
        }
    }

    @HttpAction(name = "question", pattern = "question/{id}/{title}")
    public String goToQuestion(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        QuestionsDAO dao = DAOFactory.DEFAULT.buildQuestionsDAO();
        temporaryQuestionId = id;
        editedQuestion = dao.getById(id);

        if (editedQuestion == null) {
            return "/error/404";
        } else {
            publicAnswer = editedQuestion.getQtype() == 1;
            sessionBean.selectContest(editedQuestion.getContests().getId());
            return "question";
        }
    }

    @HttpAction(name = "submit", pattern = "submit/{id}/{title}")
    public String goToSubmit(@Param(name = "id", encode = true) int id, @Param(name = "title", encode = true) String dummy) {
        ProblemsDAO dao = DAOFactory.DEFAULT.buildProblemsDAO();
        currentProblem = dao.getById(id);

        if (currentProblem == null) {
            return "/error/404";
        } else {
            sessionBean.selectContest(currentProblem.getSeries().getContests().getId());
            temporaryProblemId = id;
            return "submit";
        }
    }

    @HttpAction(name = "addseries", pattern = "add/{id}/series")
    public String goToAddseries(@Param(name = "id", encode = true) int id) {
        temporaryContestId = id;
        return "/admin/editseries";
    }

    @HttpAction(name = "addproblem", pattern = "add/{id}/problem")
    public String goToAddproblem(@Param(name = "id", encode = true) int id) {
        SeriesDAO dao = DAOFactory.DEFAULT.buildSeriesDAO();
        Series s = dao.getById(id);
        if (s != null) {
            temporarySeriesId = id;
            temporaryContestId = s.getContests().getId();
        }

        return "/admin/editproblem";
    }

    @HttpAction(name = "addtest", pattern = "add/{id}/test")
    public String goToAddtest(@Param(name = "id", encode = true) int id) {
        ProblemsDAO dao = DAOFactory.DEFAULT.buildProblemsDAO();
        Problems p = dao.getById(id);
        if (p != null) {
            temporaryProblemId = id;
            temporarySeriesId= p.getSeries().getId();
            temporaryContestId = p.getSeries().getContests().getId();
        }

        return "/admin/edittest";
    }

    public String saveTest() {
        try {
            TestsDAO dao = DAOFactory.DEFAULT.buildTestsDAO();
            ProblemsDAO pdao = DAOFactory.DEFAULT.buildProblemsDAO();

            editedTest.setProblems(pdao.getById(temporaryProblemId));
            dao.saveOrUpdate(editedTest);
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, "formEditTest:save", summary, null);
            return null;
        }

        return "problems";
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

    public void submissionsScrollerAction(ActionEvent event) {
        ScrollerActionEvent scrollerEvent = (ScrollerActionEvent) event;
        temporaryPageIndex = scrollerEvent.getPageIndex() - 1;
        submissions = null;
    }

    private String sendSolution(byte[] bytes, String fileName, String controlId) {
        FacesContext context = FacesContext.getCurrentInstance();

        LanguagesDAO ldao = DAOFactory.DEFAULT.buildLanguagesDAO();
        ProblemsDAO pdao = DAOFactory.DEFAULT.buildProblemsDAO();
        SubmitsDAO dao = DAOFactory.DEFAULT.buildSubmitsDAO();
        UsersDAO udao = DAOFactory.DEFAULT.buildUsersDAO();

        try {
            Submits submit = new Submits();

            submit.setId(null);
            submit.setFilename(fileName);
            submit.setCode(bytes);
            submit.setLanguages(ldao.getById(temporaryLanguageId));
            submit.setProblems(pdao.getById(temporaryProblemId));
            submit.setSdate(new Timestamp(Calendar.getInstance().getTime().getTime()));
            submit.setUsers(udao.getById(sessionBean.getCurrentUser().getId()));

            dao.saveOrUpdate(submit);
            return "submissions";
        } catch (Exception e) {
            String summary = String.format("%s: %s", messages.getString("unexpected_error"), e.getLocalizedMessage());
            WWWHelper.AddMessage(context, FacesMessage.SEVERITY_ERROR, controlId, summary, null);
            return null;
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
