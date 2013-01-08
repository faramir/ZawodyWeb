--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: aliases; Type: TABLE; Schema: public; Owner: zawody; Tablespace: 
--

CREATE TABLE aliases (
    id integer NOT NULL,
    name character varying(64),
    ips text DEFAULT ''::text
);


ALTER TABLE public.aliases OWNER TO zawody;

--
-- Name: aliases_id_seq; Type: SEQUENCE; Schema: public; Owner: zawody
--

CREATE SEQUENCE aliases_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.aliases_id_seq OWNER TO zawody;

--
-- Name: aliases_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zawody
--

ALTER SEQUENCE aliases_id_seq OWNED BY aliases.id;


--
-- Name: classes; Type: TABLE; Schema: public; Owner: zawody; Tablespace: 
--

CREATE TABLE classes (
    id integer NOT NULL,
    filename character varying(255),
    version integer,
    description character varying(255),
    code bytea
);


ALTER TABLE public.classes OWNER TO zawody;

--
-- Name: classes_id_seq; Type: SEQUENCE; Schema: public; Owner: zawody
--

CREATE SEQUENCE classes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.classes_id_seq OWNER TO zawody;

--
-- Name: classes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zawody
--

ALTER SEQUENCE classes_id_seq OWNED BY classes.id;


--
-- Name: contests; Type: TABLE; Schema: public; Owner: zawody; Tablespace: 
--

CREATE TABLE contests (
    id integer NOT NULL,
    name character varying(120),
    type integer,
    pass character varying(32),
    startdate timestamp without time zone,
    about text,
    rules text,
    tech text,
    visibility boolean DEFAULT true,
    rankingrefreshrate integer,
    subtype integer DEFAULT 0,
    subtypename character varying(120) DEFAULT ''::character varying,
    email text DEFAULT ''::text
);


ALTER TABLE public.contests OWNER TO zawody;

--
-- Name: contests_id_seq; Type: SEQUENCE; Schema: public; Owner: zawody
--

CREATE SEQUENCE contests_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.contests_id_seq OWNER TO zawody;

--
-- Name: contests_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zawody
--

ALTER SEQUENCE contests_id_seq OWNED BY contests.id;


--
-- Name: languages; Type: TABLE; Schema: public; Owner: zawody; Tablespace: 
--

CREATE TABLE languages (
    id integer NOT NULL,
    name character varying(40),
    extension character varying(8),
    classesid integer
);


ALTER TABLE public.languages OWNER TO zawody;

--
-- Name: languages_id_seq; Type: SEQUENCE; Schema: public; Owner: zawody
--

CREATE SEQUENCE languages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.languages_id_seq OWNER TO zawody;

--
-- Name: languages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zawody
--

ALTER SEQUENCE languages_id_seq OWNED BY languages.id;


--
-- Name: languages_problems; Type: TABLE; Schema: public; Owner: zawody; Tablespace: 
--

CREATE TABLE languages_problems (
    id integer NOT NULL,
    problemsid integer,
    languagesid integer
);


ALTER TABLE public.languages_problems OWNER TO zawody;

--
-- Name: languages_problems_id_seq; Type: SEQUENCE; Schema: public; Owner: zawody
--

CREATE SEQUENCE languages_problems_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.languages_problems_id_seq OWNER TO zawody;

--
-- Name: languages_problems_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zawody
--

ALTER SEQUENCE languages_problems_id_seq OWNED BY languages_problems.id;


--
-- Name: pdf; Type: TABLE; Schema: public; Owner: zawody; Tablespace: 
--

CREATE TABLE pdf (
    id integer NOT NULL,
    pdf bytea
);


ALTER TABLE public.pdf OWNER TO zawody;

--
-- Name: pdf_id_seq; Type: SEQUENCE; Schema: public; Owner: zawody
--

CREATE SEQUENCE pdf_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.pdf_id_seq OWNER TO zawody;

--
-- Name: pdf_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zawody
--

ALTER SEQUENCE pdf_id_seq OWNED BY pdf.id;


--
-- Name: problems; Type: TABLE; Schema: public; Owner: zawody; Tablespace: 
--

CREATE TABLE problems (
    id integer NOT NULL,
    name character varying(80),
    text text,
    abbrev character varying(5),
    memlimit integer,
    config text,
    seriesid integer,
    pdfid integer,
    classesid integer,
    codesize integer,
    visibleinranking boolean DEFAULT true,
    viewpdf boolean DEFAULT false,
    property character varying(4096) DEFAULT ''::character varying
);


ALTER TABLE public.problems OWNER TO zawody;

--
-- Name: problems_id_seq; Type: SEQUENCE; Schema: public; Owner: zawody
--

CREATE SEQUENCE problems_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.problems_id_seq OWNER TO zawody;

--
-- Name: problems_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zawody
--

ALTER SEQUENCE problems_id_seq OWNED BY problems.id;


--
-- Name: questions; Type: TABLE; Schema: public; Owner: zawody; Tablespace: 
--

CREATE TABLE questions (
    id integer NOT NULL,
    subject character varying(50),
    question text,
    visibility integer,
    qtype integer,
    usersid integer,
    contestsid integer,
    tasksid integer,
    qdate timestamp without time zone,
    adate timestamp without time zone
);


ALTER TABLE public.questions OWNER TO zawody;

--
-- Name: questions_id_seq; Type: SEQUENCE; Schema: public; Owner: zawody
--

CREATE SEQUENCE questions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.questions_id_seq OWNER TO zawody;

--
-- Name: questions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zawody
--

ALTER SEQUENCE questions_id_seq OWNED BY questions.id;


--
-- Name: results; Type: TABLE; Schema: public; Owner: zawody; Tablespace: 
--

CREATE TABLE results (
    id integer NOT NULL,
    points integer,
    runtime integer,
    memory integer,
    notes text,
    submitresult integer,
    submitsid integer,
    testsid integer
);


ALTER TABLE public.results OWNER TO zawody;

--
-- Name: results_id_seq; Type: SEQUENCE; Schema: public; Owner: zawody
--

CREATE SEQUENCE results_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.results_id_seq OWNER TO zawody;

--
-- Name: results_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zawody
--

ALTER SEQUENCE results_id_seq OWNED BY results.id;


--
-- Name: roles; Type: TABLE; Schema: public; Owner: zawody; Tablespace: 
--

CREATE TABLE roles (
    id integer NOT NULL,
    name character varying(40),
    edituser boolean,
    addcontest boolean,
    editcontest boolean,
    delcontest boolean,
    addseries boolean,
    editseries boolean,
    delseries boolean,
    addproblem boolean,
    editproblem boolean,
    delproblem boolean,
    canrate boolean,
    contestant boolean,
    contestsid integer,
    seriesid integer
);


ALTER TABLE public.roles OWNER TO zawody;

--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: zawody
--

CREATE SEQUENCE roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.roles_id_seq OWNER TO zawody;

--
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zawody
--

ALTER SEQUENCE roles_id_seq OWNED BY roles.id;


--
-- Name: series; Type: TABLE; Schema: public; Owner: zawody; Tablespace: 
--

CREATE TABLE series (
    id integer NOT NULL,
    name character varying(40),
    startdate timestamp without time zone,
    enddate timestamp without time zone,
    freezedate timestamp without time zone,
    unfreezedate timestamp without time zone,
    penaltytime integer,
    contestsid integer,
    visibleinranking boolean DEFAULT true,
    openips text DEFAULT ''::text,
    hiddenblocked boolean DEFAULT false
);


ALTER TABLE public.series OWNER TO zawody;

--
-- Name: series_id_seq; Type: SEQUENCE; Schema: public; Owner: zawody
--

CREATE SEQUENCE series_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.series_id_seq OWNER TO zawody;

--
-- Name: series_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zawody
--

ALTER SEQUENCE series_id_seq OWNED BY series.id;


--
-- Name: submits; Type: TABLE; Schema: public; Owner: zawody; Tablespace: 
--

CREATE TABLE submits (
    id integer NOT NULL,
    sdate timestamp without time zone,
    result integer,
    code bytea,
    filename character varying(255),
    notes text,
    problemsid integer,
    languagesid integer,
    usersid integer,
    clientip character varying(16) DEFAULT ''::character varying,
    visibleinranking boolean DEFAULT true
);


ALTER TABLE public.submits OWNER TO zawody;

--
-- Name: submits_id_seq; Type: SEQUENCE; Schema: public; Owner: zawody
--

CREATE SEQUENCE submits_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.submits_id_seq OWNER TO zawody;

--
-- Name: submits_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zawody
--

ALTER SEQUENCE submits_id_seq OWNED BY submits.id;


--
-- Name: tests; Type: TABLE; Schema: public; Owner: zawody; Tablespace: 
--

CREATE TABLE tests (
    id integer NOT NULL,
    input text,
    output text,
    timelimit integer,
    maxpoints integer,
    visibility integer,
    testorder integer,
    problemsid integer
);


ALTER TABLE public.tests OWNER TO zawody;

--
-- Name: tests_id_seq; Type: SEQUENCE; Schema: public; Owner: zawody
--

CREATE SEQUENCE tests_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.tests_id_seq OWNER TO zawody;

--
-- Name: tests_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zawody
--

ALTER SEQUENCE tests_id_seq OWNED BY tests.id;


--
-- Name: userlog; Type: TABLE; Schema: public; Owner: zawody; Tablespace: 
--

CREATE TABLE userlog (
    id integer NOT NULL,
    username character varying(64),
    ip character varying(40),
    logdate timestamp without time zone
);


ALTER TABLE public.userlog OWNER TO zawody;

--
-- Name: userlog_id_seq; Type: SEQUENCE; Schema: public; Owner: zawody
--

CREATE SEQUENCE userlog_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.userlog_id_seq OWNER TO zawody;

--
-- Name: userlog_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zawody
--

ALTER SEQUENCE userlog_id_seq OWNED BY userlog.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: zawody; Tablespace: 
--

CREATE TABLE users (
    id integer NOT NULL,
    firstname character varying(40),
    lastname character varying(40),
    email character varying(40),
    birthdate timestamp without time zone,
    login character varying(64),
    pass character varying(40),
    address character varying(80),
    school character varying(80),
    tutor text,
    emailnotification integer,
    schooltype character varying(16) DEFAULT '-'::character varying,
    rdate timestamp without time zone,
    ldate timestamp without time zone,
    fdate timestamp without time zone,
    onlylogin boolean DEFAULT false
);


ALTER TABLE public.users OWNER TO zawody;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: zawody
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO zawody;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zawody
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: users_roles; Type: TABLE; Schema: public; Owner: zawody; Tablespace: 
--

CREATE TABLE users_roles (
    id integer NOT NULL,
    usersid integer,
    rolesid integer
);


ALTER TABLE public.users_roles OWNER TO zawody;

--
-- Name: users_roles_id_seq; Type: SEQUENCE; Schema: public; Owner: zawody
--

CREATE SEQUENCE users_roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.users_roles_id_seq OWNER TO zawody;

--
-- Name: users_roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zawody
--

ALTER SEQUENCE users_roles_id_seq OWNED BY users_roles.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY aliases ALTER COLUMN id SET DEFAULT nextval('aliases_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY classes ALTER COLUMN id SET DEFAULT nextval('classes_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY contests ALTER COLUMN id SET DEFAULT nextval('contests_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY languages ALTER COLUMN id SET DEFAULT nextval('languages_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY languages_problems ALTER COLUMN id SET DEFAULT nextval('languages_problems_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY pdf ALTER COLUMN id SET DEFAULT nextval('pdf_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY problems ALTER COLUMN id SET DEFAULT nextval('problems_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY questions ALTER COLUMN id SET DEFAULT nextval('questions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY results ALTER COLUMN id SET DEFAULT nextval('results_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY roles ALTER COLUMN id SET DEFAULT nextval('roles_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY series ALTER COLUMN id SET DEFAULT nextval('series_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY submits ALTER COLUMN id SET DEFAULT nextval('submits_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY tests ALTER COLUMN id SET DEFAULT nextval('tests_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY userlog ALTER COLUMN id SET DEFAULT nextval('userlog_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY users_roles ALTER COLUMN id SET DEFAULT nextval('users_roles_id_seq'::regclass);


--
-- Name: aliases_pkey; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY aliases
    ADD CONSTRAINT aliases_pkey PRIMARY KEY (id);


--
-- Name: classes_filename_key; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY classes
    ADD CONSTRAINT classes_filename_key UNIQUE (filename);


--
-- Name: classes_pkey; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY classes
    ADD CONSTRAINT classes_pkey PRIMARY KEY (id);


--
-- Name: contests_name_key; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY contests
    ADD CONSTRAINT contests_name_key UNIQUE (name);


--
-- Name: contests_pkey; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY contests
    ADD CONSTRAINT contests_pkey PRIMARY KEY (id);


--
-- Name: languages_name_key; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY languages
    ADD CONSTRAINT languages_name_key UNIQUE (name);


--
-- Name: languages_pkey; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY languages
    ADD CONSTRAINT languages_pkey PRIMARY KEY (id);


--
-- Name: languages_problems_pkey; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY languages_problems
    ADD CONSTRAINT languages_problems_pkey PRIMARY KEY (id);


--
-- Name: pdf_pkey; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY pdf
    ADD CONSTRAINT pdf_pkey PRIMARY KEY (id);


--
-- Name: problems_pkey; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY problems
    ADD CONSTRAINT problems_pkey PRIMARY KEY (id);


--
-- Name: questions_pkey; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY questions
    ADD CONSTRAINT questions_pkey PRIMARY KEY (id);


--
-- Name: results_pkey; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY results
    ADD CONSTRAINT results_pkey PRIMARY KEY (id);


--
-- Name: roles_name_key; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_name_key UNIQUE (name);


--
-- Name: roles_pkey; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: series_pkey; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY series
    ADD CONSTRAINT series_pkey PRIMARY KEY (id);


--
-- Name: submits_pkey; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY submits
    ADD CONSTRAINT submits_pkey PRIMARY KEY (id);


--
-- Name: tests_pkey; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY tests
    ADD CONSTRAINT tests_pkey PRIMARY KEY (id);


--
-- Name: userlog_pkey; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY userlog
    ADD CONSTRAINT userlog_pkey PRIMARY KEY (id);


--
-- Name: users_login_key; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_login_key UNIQUE (login);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: zawody; Tablespace: 
--

ALTER TABLE ONLY users_roles
    ADD CONSTRAINT users_roles_pkey PRIMARY KEY (id);


--
-- Name: languages_classesid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY languages
    ADD CONSTRAINT languages_classesid_fkey FOREIGN KEY (classesid) REFERENCES classes(id);


--
-- Name: languages_problems_languagesid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY languages_problems
    ADD CONSTRAINT languages_problems_languagesid_fkey FOREIGN KEY (languagesid) REFERENCES languages(id);


--
-- Name: languages_problems_problemsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY languages_problems
    ADD CONSTRAINT languages_problems_problemsid_fkey FOREIGN KEY (problemsid) REFERENCES problems(id);


--
-- Name: problems_classesid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY problems
    ADD CONSTRAINT problems_classesid_fkey FOREIGN KEY (classesid) REFERENCES classes(id);


--
-- Name: problems_pdfid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY problems
    ADD CONSTRAINT problems_pdfid_fkey FOREIGN KEY (pdfid) REFERENCES pdf(id);


--
-- Name: problems_seriesid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY problems
    ADD CONSTRAINT problems_seriesid_fkey FOREIGN KEY (seriesid) REFERENCES series(id);


--
-- Name: questions_contestsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY questions
    ADD CONSTRAINT questions_contestsid_fkey FOREIGN KEY (contestsid) REFERENCES contests(id);


--
-- Name: questions_tasksid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY questions
    ADD CONSTRAINT questions_tasksid_fkey FOREIGN KEY (tasksid) REFERENCES problems(id);


--
-- Name: questions_usersid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY questions
    ADD CONSTRAINT questions_usersid_fkey FOREIGN KEY (usersid) REFERENCES users(id);


--
-- Name: results_submitsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY results
    ADD CONSTRAINT results_submitsid_fkey FOREIGN KEY (submitsid) REFERENCES submits(id);


--
-- Name: results_testsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY results
    ADD CONSTRAINT results_testsid_fkey FOREIGN KEY (testsid) REFERENCES tests(id);


--
-- Name: roles_contestsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_contestsid_fkey FOREIGN KEY (contestsid) REFERENCES contests(id);


--
-- Name: roles_seriesid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_seriesid_fkey FOREIGN KEY (seriesid) REFERENCES series(id);


--
-- Name: series_contestsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY series
    ADD CONSTRAINT series_contestsid_fkey FOREIGN KEY (contestsid) REFERENCES contests(id);


--
-- Name: submits_languagesid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY submits
    ADD CONSTRAINT submits_languagesid_fkey FOREIGN KEY (languagesid) REFERENCES languages(id);


--
-- Name: submits_problemsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY submits
    ADD CONSTRAINT submits_problemsid_fkey FOREIGN KEY (problemsid) REFERENCES problems(id);


--
-- Name: submits_usersid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY submits
    ADD CONSTRAINT submits_usersid_fkey FOREIGN KEY (usersid) REFERENCES users(id);


--
-- Name: tests_problemsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY tests
    ADD CONSTRAINT tests_problemsid_fkey FOREIGN KEY (problemsid) REFERENCES problems(id);


--
-- Name: users_roles_rolesid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY users_roles
    ADD CONSTRAINT users_roles_rolesid_fkey FOREIGN KEY (rolesid) REFERENCES roles(id);


--
-- Name: users_roles_usersid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: zawody
--

ALTER TABLE ONLY users_roles
    ADD CONSTRAINT users_roles_usersid_fkey FOREIGN KEY (usersid) REFERENCES users(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

