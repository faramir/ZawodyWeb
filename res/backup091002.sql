--
-- PostgreSQL database dump
--

-- Started on 2009-10-02 21:59:55 CEST

SET statement_timeout = 0;
SET client_encoding = 'LATIN2';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 1934 (class 1262 OID 25089)
-- Name: zawodyweb; Type: DATABASE; Schema: -; Owner: -
--

-- CREATE DATABASE zawodyweb WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'pl_PL' LC_CTYPE = 'pl_PL';


\connect zawodyweb

SET statement_timeout = 0;
SET client_encoding = 'LATIN2';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 1560 (class 1259 OID 25203)
-- Dependencies: 3
-- Name: classes; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE classes (
    id integer NOT NULL,
    filename character varying(255),
    version integer,
    description character varying(255),
    code bytea
);


--
-- TOC entry 1559 (class 1259 OID 25201)
-- Dependencies: 1560 3
-- Name: classes_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE classes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1937 (class 0 OID 0)
-- Dependencies: 1559
-- Name: classes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE classes_id_seq OWNED BY classes.id;


--
-- TOC entry 1938 (class 0 OID 0)
-- Dependencies: 1559
-- Name: classes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('classes_id_seq', 11, true);


--
-- TOC entry 1546 (class 1259 OID 25125)
-- Dependencies: 3
-- Name: contests; Type: TABLE; Schema: public; Owner: -; Tablespace: 
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
    visibility integer
);


--
-- TOC entry 1545 (class 1259 OID 25123)
-- Dependencies: 3 1546
-- Name: contests_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE contests_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1939 (class 0 OID 0)
-- Dependencies: 1545
-- Name: contests_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE contests_id_seq OWNED BY contests.id;


--
-- TOC entry 1940 (class 0 OID 0)
-- Dependencies: 1545
-- Name: contests_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('contests_id_seq', 1, false);


--
-- TOC entry 1558 (class 1259 OID 25193)
-- Dependencies: 3
-- Name: languages; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE languages (
    id integer NOT NULL,
    name character varying(40),
    extension character varying(8),
    classesid integer
);


--
-- TOC entry 1557 (class 1259 OID 25191)
-- Dependencies: 3 1558
-- Name: languages_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE languages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1941 (class 0 OID 0)
-- Dependencies: 1557
-- Name: languages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE languages_id_seq OWNED BY languages.id;


--
-- TOC entry 1942 (class 0 OID 0)
-- Dependencies: 1557
-- Name: languages_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('languages_id_seq', 12, true);


--
-- TOC entry 1566 (class 1259 OID 25325)
-- Dependencies: 3
-- Name: languages_problems; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE languages_problems (
    id integer NOT NULL,
    problemsid integer,
    languagesid integer
);


--
-- TOC entry 1565 (class 1259 OID 25323)
-- Dependencies: 1566 3
-- Name: languages_problems_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE languages_problems_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1943 (class 0 OID 0)
-- Dependencies: 1565
-- Name: languages_problems_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE languages_problems_id_seq OWNED BY languages_problems.id;


--
-- TOC entry 1944 (class 0 OID 0)
-- Dependencies: 1565
-- Name: languages_problems_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('languages_problems_id_seq', 1, false);


--
-- TOC entry 1562 (class 1259 OID 25216)
-- Dependencies: 3
-- Name: pdf; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE pdf (
    id integer NOT NULL,
    pdf bytea
);


--
-- TOC entry 1561 (class 1259 OID 25214)
-- Dependencies: 3 1562
-- Name: pdf_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE pdf_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1945 (class 0 OID 0)
-- Dependencies: 1561
-- Name: pdf_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE pdf_id_seq OWNED BY pdf.id;


--
-- TOC entry 1946 (class 0 OID 0)
-- Dependencies: 1561
-- Name: pdf_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('pdf_id_seq', 1, false);


--
-- TOC entry 1550 (class 1259 OID 25149)
-- Dependencies: 3
-- Name: problems; Type: TABLE; Schema: public; Owner: -; Tablespace: 
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
    classesid integer
);


--
-- TOC entry 1549 (class 1259 OID 25147)
-- Dependencies: 3 1550
-- Name: problems_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE problems_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1947 (class 0 OID 0)
-- Dependencies: 1549
-- Name: problems_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE problems_id_seq OWNED BY problems.id;


--
-- TOC entry 1948 (class 0 OID 0)
-- Dependencies: 1549
-- Name: problems_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('problems_id_seq', 1, false);


--
-- TOC entry 1552 (class 1259 OID 25160)
-- Dependencies: 3
-- Name: questions; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE questions (
    id integer NOT NULL,
    subject character varying(50),
    question text,
    visibility integer,
    qtype integer,
    usersid integer,
    contestsid integer,
    tasksid integer
);


--
-- TOC entry 1551 (class 1259 OID 25158)
-- Dependencies: 1552 3
-- Name: questions_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE questions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1949 (class 0 OID 0)
-- Dependencies: 1551
-- Name: questions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE questions_id_seq OWNED BY questions.id;


--
-- TOC entry 1950 (class 0 OID 0)
-- Dependencies: 1551
-- Name: questions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('questions_id_seq', 1, false);


--
-- TOC entry 1554 (class 1259 OID 25171)
-- Dependencies: 3
-- Name: results; Type: TABLE; Schema: public; Owner: -; Tablespace: 
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


--
-- TOC entry 1553 (class 1259 OID 25169)
-- Dependencies: 3 1554
-- Name: results_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE results_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1951 (class 0 OID 0)
-- Dependencies: 1553
-- Name: results_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE results_id_seq OWNED BY results.id;


--
-- TOC entry 1952 (class 0 OID 0)
-- Dependencies: 1553
-- Name: results_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('results_id_seq', 1, false);


--
-- TOC entry 1542 (class 1259 OID 25107)
-- Dependencies: 3
-- Name: roles; Type: TABLE; Schema: public; Owner: -; Tablespace: 
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


--
-- TOC entry 1541 (class 1259 OID 25105)
-- Dependencies: 1542 3
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1953 (class 0 OID 0)
-- Dependencies: 1541
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE roles_id_seq OWNED BY roles.id;


--
-- TOC entry 1954 (class 0 OID 0)
-- Dependencies: 1541
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('roles_id_seq', 3, true);


--
-- TOC entry 1544 (class 1259 OID 25117)
-- Dependencies: 3
-- Name: series; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE series (
    id integer NOT NULL,
    name character varying(40),
    startdate timestamp without time zone,
    enddate timestamp without time zone,
    freezedate timestamp without time zone,
    unfreezedate timestamp without time zone,
    penaltytime integer,
    contestsid integer
);


--
-- TOC entry 1543 (class 1259 OID 25115)
-- Dependencies: 3 1544
-- Name: series_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE series_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1955 (class 0 OID 0)
-- Dependencies: 1543
-- Name: series_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE series_id_seq OWNED BY series.id;


--
-- TOC entry 1956 (class 0 OID 0)
-- Dependencies: 1543
-- Name: series_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('series_id_seq', 1, false);


--
-- TOC entry 1548 (class 1259 OID 25138)
-- Dependencies: 3
-- Name: submits; Type: TABLE; Schema: public; Owner: -; Tablespace: 
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
    usersid integer
);


--
-- TOC entry 1547 (class 1259 OID 25136)
-- Dependencies: 1548 3
-- Name: submits_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE submits_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1957 (class 0 OID 0)
-- Dependencies: 1547
-- Name: submits_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE submits_id_seq OWNED BY submits.id;


--
-- TOC entry 1958 (class 0 OID 0)
-- Dependencies: 1547
-- Name: submits_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('submits_id_seq', 1, false);


--
-- TOC entry 1556 (class 1259 OID 25182)
-- Dependencies: 3
-- Name: tests; Type: TABLE; Schema: public; Owner: -; Tablespace: 
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


--
-- TOC entry 1555 (class 1259 OID 25180)
-- Dependencies: 3 1556
-- Name: tests_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE tests_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1959 (class 0 OID 0)
-- Dependencies: 1555
-- Name: tests_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE tests_id_seq OWNED BY tests.id;


--
-- TOC entry 1960 (class 0 OID 0)
-- Dependencies: 1555
-- Name: tests_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('tests_id_seq', 1, false);


--
-- TOC entry 1540 (class 1259 OID 25092)
-- Dependencies: 3
-- Name: users; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE users (
    id integer NOT NULL,
    firstname character varying(40),
    lastname character varying(40),
    email character varying(40),
    birthdate timestamp without time zone,
    login character varying(20),
    pass character varying(40),
    address character varying(80),
    school character varying(80),
    tutor text,
    emailnotification integer
);


--
-- TOC entry 1539 (class 1259 OID 25090)
-- Dependencies: 1540 3
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1961 (class 0 OID 0)
-- Dependencies: 1539
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- TOC entry 1962 (class 0 OID 0)
-- Dependencies: 1539
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('users_id_seq', 1, true);


--
-- TOC entry 1564 (class 1259 OID 25307)
-- Dependencies: 3
-- Name: users_roles; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE users_roles (
    id integer NOT NULL,
    usersid integer,
    rolesid integer
);


--
-- TOC entry 1563 (class 1259 OID 25305)
-- Dependencies: 1564 3
-- Name: users_roles_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE users_roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1963 (class 0 OID 0)
-- Dependencies: 1563
-- Name: users_roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE users_roles_id_seq OWNED BY users_roles.id;


--
-- TOC entry 1964 (class 0 OID 0)
-- Dependencies: 1563
-- Name: users_roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('users_roles_id_seq', 1, true);


--
-- TOC entry 1854 (class 2604 OID 25206)
-- Dependencies: 1559 1560 1560
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE classes ALTER COLUMN id SET DEFAULT nextval('classes_id_seq'::regclass);


--
-- TOC entry 1847 (class 2604 OID 25128)
-- Dependencies: 1546 1545 1546
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE contests ALTER COLUMN id SET DEFAULT nextval('contests_id_seq'::regclass);


--
-- TOC entry 1853 (class 2604 OID 25196)
-- Dependencies: 1558 1557 1558
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE languages ALTER COLUMN id SET DEFAULT nextval('languages_id_seq'::regclass);


--
-- TOC entry 1857 (class 2604 OID 25328)
-- Dependencies: 1566 1565 1566
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE languages_problems ALTER COLUMN id SET DEFAULT nextval('languages_problems_id_seq'::regclass);


--
-- TOC entry 1855 (class 2604 OID 25219)
-- Dependencies: 1562 1561 1562
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE pdf ALTER COLUMN id SET DEFAULT nextval('pdf_id_seq'::regclass);


--
-- TOC entry 1849 (class 2604 OID 25152)
-- Dependencies: 1549 1550 1550
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE problems ALTER COLUMN id SET DEFAULT nextval('problems_id_seq'::regclass);


--
-- TOC entry 1850 (class 2604 OID 25163)
-- Dependencies: 1552 1551 1552
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE questions ALTER COLUMN id SET DEFAULT nextval('questions_id_seq'::regclass);


--
-- TOC entry 1851 (class 2604 OID 25174)
-- Dependencies: 1554 1553 1554
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE results ALTER COLUMN id SET DEFAULT nextval('results_id_seq'::regclass);


--
-- TOC entry 1845 (class 2604 OID 25110)
-- Dependencies: 1542 1541 1542
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE roles ALTER COLUMN id SET DEFAULT nextval('roles_id_seq'::regclass);


--
-- TOC entry 1846 (class 2604 OID 25120)
-- Dependencies: 1544 1543 1544
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE series ALTER COLUMN id SET DEFAULT nextval('series_id_seq'::regclass);


--
-- TOC entry 1848 (class 2604 OID 25141)
-- Dependencies: 1547 1548 1548
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE submits ALTER COLUMN id SET DEFAULT nextval('submits_id_seq'::regclass);


--
-- TOC entry 1852 (class 2604 OID 25185)
-- Dependencies: 1555 1556 1556
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE tests ALTER COLUMN id SET DEFAULT nextval('tests_id_seq'::regclass);


--
-- TOC entry 1844 (class 2604 OID 25095)
-- Dependencies: 1540 1539 1540
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- TOC entry 1856 (class 2604 OID 25310)
-- Dependencies: 1564 1563 1564
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE users_roles ALTER COLUMN id SET DEFAULT nextval('users_roles_id_seq'::regclass);


--
-- TOC entry 1928 (class 0 OID 25203)
-- Dependencies: 1560
-- Data for Name: classes; Type: TABLE DATA; Schema: public; Owner: -
--

COPY classes (id, filename, version, description, code) FROM stdin;
1	pl.umk.mat.zawodyweb.compiler.classes.LanguageTXT	1	TXT Compiler	\\312\\376\\272\\276\\000\\000\\0002\\000/\\012\\000\\007\\000&\\007\\000'\\012\\000\\002\\000(\\007\\000)\\012\\000\\004\\000*\\007\\000+\\007\\000,\\007\\000-\\001\\000\\006<init>\\001\\000\\003()V\\001\\000\\004Code\\001\\000\\017LineNumberTable\\001\\000\\022LocalVariableTable\\001\\000\\004this\\001\\0003Lpl/umk/mat/zawodyweb/compiler/classes/LanguageTXT;\\001\\000\\007runTest\\001\\000e(Ljava/lang/String;Lpl/umk/mat/zawodyweb/checker/TestInput;)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\004path\\001\\000\\022Ljava/lang/String;\\001\\000\\005input\\001\\000(Lpl/umk/mat/zawodyweb/checker/TestInput;\\001\\000\\012precompile\\001\\000\\006([B)[B\\001\\000\\004code\\001\\000\\002[B\\001\\000\\007compile\\001\\000\\026([B)Ljava/lang/String;\\001\\000\\013postcompile\\001\\000&(Ljava/lang/String;)Ljava/lang/String;\\001\\000\\015setProperties\\001\\000\\031(Ljava/util/Properties;)V\\001\\000\\012properties\\001\\000\\026Ljava/util/Properties;\\001\\000\\014closeProgram\\001\\000\\025(Ljava/lang/String;)V\\001\\000\\012SourceFile\\001\\000\\020LanguageTXT.java\\014\\000\\011\\000\\012\\001\\000'pl/umk/mat/zawodyweb/checker/TestOutput\\014\\000\\011\\000#\\001\\000\\020java/lang/String\\014\\000\\011\\000.\\001\\0001pl/umk/mat/zawodyweb/compiler/classes/LanguageTXT\\001\\000\\020java/lang/Object\\001\\000/pl/umk/mat/zawodyweb/compiler/CompilerInterface\\001\\000\\005([B)V\\000!\\000\\006\\000\\007\\000\\001\\000\\010\\000\\000\\000\\007\\000\\001\\000\\011\\000\\012\\000\\001\\000\\013\\000\\000\\0003\\000\\001\\000\\001\\000\\000\\000\\005*\\267\\000\\001\\261\\000\\000\\000\\002\\000\\014\\000\\000\\000\\012\\000\\002\\000\\000\\000\\017\\000\\004\\000\\020\\000\\015\\000\\000\\000\\014\\000\\001\\000\\000\\000\\005\\000\\016\\000\\017\\000\\000\\000\\001\\000\\020\\000\\021\\000\\001\\000\\013\\000\\000\\000G\\000\\003\\000\\003\\000\\000\\000\\011\\273\\000\\002Y+\\267\\000\\003\\260\\000\\000\\000\\002\\000\\014\\000\\000\\000\\006\\000\\001\\000\\000\\000\\024\\000\\015\\000\\000\\000 \\000\\003\\000\\000\\000\\011\\000\\016\\000\\017\\000\\000\\000\\000\\000\\011\\000\\022\\000\\023\\000\\001\\000\\000\\000\\011\\000\\024\\000\\025\\000\\002\\000\\001\\000\\026\\000\\027\\000\\001\\000\\013\\000\\000\\0006\\000\\001\\000\\002\\000\\000\\000\\002+\\260\\000\\000\\000\\002\\000\\014\\000\\000\\000\\006\\000\\001\\000\\000\\000\\031\\000\\015\\000\\000\\000\\026\\000\\002\\000\\000\\000\\002\\000\\016\\000\\017\\000\\000\\000\\000\\000\\002\\000\\030\\000\\031\\000\\001\\000\\001\\000\\032\\000\\033\\000\\001\\000\\013\\000\\000\\000=\\000\\003\\000\\002\\000\\000\\000\\011\\273\\000\\004Y+\\267\\000\\005\\260\\000\\000\\000\\002\\000\\014\\000\\000\\000\\006\\000\\001\\000\\000\\000\\036\\000\\015\\000\\000\\000\\026\\000\\002\\000\\000\\000\\011\\000\\016\\000\\017\\000\\000\\000\\000\\000\\011\\000\\030\\000\\031\\000\\001\\000\\001\\000\\034\\000\\035\\000\\001\\000\\013\\000\\000\\0006\\000\\001\\000\\002\\000\\000\\000\\002+\\260\\000\\000\\000\\002\\000\\014\\000\\000\\000\\006\\000\\001\\000\\000\\000#\\000\\015\\000\\000\\000\\026\\000\\002\\000\\000\\000\\002\\000\\016\\000\\017\\000\\000\\000\\000\\000\\002\\000\\022\\000\\023\\000\\001\\000\\001\\000\\036\\000\\037\\000\\001\\000\\013\\000\\000\\0005\\000\\000\\000\\002\\000\\000\\000\\001\\261\\000\\000\\000\\002\\000\\014\\000\\000\\000\\006\\000\\001\\000\\000\\000(\\000\\015\\000\\000\\000\\026\\000\\002\\000\\000\\000\\001\\000\\016\\000\\017\\000\\000\\000\\000\\000\\001\\000 \\000!\\000\\001\\000\\001\\000"\\000#\\000\\001\\000\\013\\000\\000\\0005\\000\\000\\000\\002\\000\\000\\000\\001\\261\\000\\000\\000\\002\\000\\014\\000\\000\\000\\006\\000\\001\\000\\000\\000,\\000\\015\\000\\000\\000\\026\\000\\002\\000\\000\\000\\001\\000\\016\\000\\017\\000\\000\\000\\000\\000\\001\\000\\022\\000\\023\\000\\001\\000\\001\\000$\\000\\000\\000\\002\\000%
2	pl.umk.mat.zawodyweb.compiler.classes.LanguagePAS	1	PAS Compiler	\\312\\376\\272\\276\\000\\000\\0002\\001\\222\\012\\000w\\000\\315\\011\\000u\\000\\316\\007\\000\\317\\012\\000\\003\\000\\315\\011\\000u\\000\\320\\011\\000u\\000\\321\\007\\000\\322\\012\\000\\007\\000\\323\\012\\000\\007\\000\\324\\012\\000\\003\\000\\325\\012\\000\\007\\000\\326\\012\\000\\327\\000\\330\\012\\000\\331\\000\\332\\010\\000\\333\\012\\000\\327\\000\\334\\012\\000\\003\\000\\335\\010\\000\\336\\012\\000\\003\\000\\337\\010\\000\\340\\010\\000\\341\\007\\000\\342\\012\\000\\025\\000\\315\\010\\000\\343\\012\\000\\025\\000\\344\\012\\000\\345\\000\\346\\012\\000\\025\\000\\347\\010\\000\\350\\012\\000\\025\\000\\351\\011\\000u\\000\\352\\010\\000\\353\\010\\000\\354\\012\\000\\355\\000\\356\\007\\000\\357\\012\\000!\\000\\315\\007\\000\\360\\012\\000#\\000\\361\\012\\000#\\000\\362\\007\\000\\363\\012\\000&\\000\\315\\012\\000&\\000\\364\\012\\000\\365\\000\\366\\012\\000\\345\\000\\367\\012\\000!\\000\\370\\007\\000\\371\\007\\000\\372\\012\\000\\373\\000\\374\\012\\000-\\000\\375\\012\\000,\\000\\376\\007\\000\\377\\007\\001\\000\\012\\000\\373\\001\\001\\012\\0002\\001\\002\\012\\0001\\001\\003\\012\\000\\345\\001\\004\\012\\0001\\001\\005\\012\\0001\\001\\006\\010\\001\\007\\012\\000\\025\\001\\010\\010\\001\\011\\012\\000\\355\\001\\012\\012\\000\\373\\001\\013\\007\\001\\014\\012\\000\\373\\001\\015\\012\\000!\\001\\016\\012\\000\\373\\001\\017\\010\\001\\020\\010\\001\\021\\012\\000\\007\\001\\022\\012\\000,\\001\\023\\012\\000\\007\\001\\024\\007\\001\\025\\012\\000G\\001\\026\\012\\000\\003\\001\\027\\010\\001\\030\\010\\001\\031\\012\\000\\003\\001\\032\\012\\000\\003\\001\\033\\012\\000\\025\\001\\034\\010\\001\\035\\010\\001\\036\\010\\001\\037\\012\\000\\003\\001 \\010\\001!\\010\\001"\\012\\001#\\000\\334\\010\\001$\\010\\001%\\010\\001&\\010\\001'\\010\\001(\\010\\001)\\011\\000q\\001*\\010\\001+\\010\\001,\\011\\000u\\001-\\010\\001.\\010\\001/\\007\\0010\\012\\000b\\000\\323\\012\\0011\\0012\\012\\0011\\001\\006\\010\\0013\\010\\0014\\010\\0015\\010\\0016\\010\\0017\\012\\000#\\0018\\010\\0019\\010\\001:\\010\\001;\\012\\001<\\001=\\012\\000,\\001\\006\\007\\001>\\012\\000q\\000\\323\\012\\000q\\001?\\012\\000G\\001@\\007\\001A\\012\\000\\355\\001B\\007\\001C\\007\\001D\\001\\000\\006logger\\001\\000\\031Lorg/apache/log4j/Logger;\\001\\000\\012properties\\001\\000\\026Ljava/util/Properties;\\001\\000\\005ofile\\001\\000\\022Ljava/lang/String;\\001\\000\\015compileResult\\001\\000\\001I\\001\\000\\013compileDesc\\001\\000\\006<init>\\001\\000\\003()V\\001\\000\\004Code\\001\\000\\017LineNumberTable\\001\\000\\022LocalVariableTable\\001\\000\\004this\\001\\0003Lpl/umk/mat/zawodyweb/compiler/classes/LanguagePAS;\\001\\000\\015setProperties\\001\\000\\031(Ljava/util/Properties;)V\\001\\000\\007runTest\\001\\000e(Ljava/lang/String;Lpl/umk/mat/zawodyweb/checker/TestInput;)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\014outputStream\\001\\000\\030Ljava/io/BufferedWriter;\\001\\000\\002ex\\001\\000 Ljava/lang/InterruptedException;\\001\\000\\005timer\\001\\000+Lpl/umk/mat/zawodyweb/judge/InterruptTimer;\\001\\000\\001p\\001\\000\\023Ljava/lang/Process;\\001\\000\\004time\\001\\000\\001J\\001\\000\\013currentTime\\001\\000\\012outputText\\001\\000\\004line\\001\\000\\025Ljava/lang/Exception;\\001\\000\\004path\\001\\000\\005input\\001\\000(Lpl/umk/mat/zawodyweb/checker/TestInput;\\001\\000\\006output\\001\\000)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\013inputStream\\001\\000\\030Ljava/io/BufferedReader;\\001\\000\\007command\\001\\000\\020Ljava/util/List;\\001\\000\\026LocalVariableTypeTable\\001\\000$Ljava/util/List<Ljava/lang/String;>;\\001\\000\\015StackMapTable\\007\\000\\322\\007\\001E\\007\\001A\\007\\000\\317\\007\\001F\\007\\000\\357\\007\\001G\\007\\001\\014\\007\\000\\371\\007\\001\\025\\001\\000\\012precompile\\001\\000\\006([B)[B\\001\\000\\001i\\001\\000\\004code\\001\\000\\002[B\\001\\000\\003str\\001\\000\\016forbiddenCalls\\001\\000\\022strWithoutComments\\001\\000\\003len\\001\\000\\012regexp1_on\\007\\000\\265\\001\\000\\007compile\\001\\000\\026([B)Ljava/lang/String;\\001\\000\\010codefile\\001\\000\\013compileddir\\001\\000\\007codedir\\001\\000\\002is\\001\\000\\026Ljava/io/OutputStream;\\001\\000\\003err\\001\\000\\013compilefile\\007\\001H\\001\\000\\013postcompile\\001\\000&(Ljava/lang/String;)Ljava/lang/String;\\001\\000\\014closeProgram\\001\\000\\025(Ljava/lang/String;)V\\001\\000\\010<clinit>\\001\\000\\012SourceFile\\001\\000\\020LanguagePAS.java\\014\\000\\202\\000\\203\\014\\000\\177\\000\\200\\001\\000\\020java/lang/String\\014\\000\\201\\000~\\014\\000{\\000|\\001\\000'pl/umk/mat/zawodyweb/checker/TestOutput\\014\\000\\202\\000\\311\\014\\001I\\001J\\014\\001K\\001L\\014\\001M\\000\\311\\007\\001N\\014\\001O\\000\\203\\007\\001P\\014\\001Q\\001R\\001\\000\\007os.name\\014\\001S\\000\\307\\014\\001T\\001U\\001\\000\\017(?s).*windows.*\\014\\001V\\001W\\001\\000\\004bash\\001\\000\\002-c\\001\\000\\027java/lang/StringBuilder\\001\\000\\012ulimit -v \\014\\001X\\001Y\\007\\001F\\014\\001Z\\001[\\014\\001X\\001\\\\\\001\\000\\004 && \\014\\001]\\001U\\014\\000y\\000z\\001\\000\\021OS without bash: \\001\\000\\034. Memory Limit check is off.\\007\\001^\\014\\001_\\001`\\001\\000)pl/umk/mat/zawodyweb/judge/InterruptTimer\\001\\000\\030java/lang/ProcessBuilder\\014\\000\\202\\001a\\014\\001b\\001c\\001\\000\\016java/util/Date\\014\\001d\\001e\\007\\001f\\014\\001g\\001h\\014\\001i\\001[\\014\\001j\\001k\\001\\000\\026java/io/BufferedReader\\001\\000\\031java/io/InputStreamReader\\007\\001G\\014\\001l\\001m\\014\\000\\202\\001n\\014\\000\\202\\001o\\001\\000\\026java/io/BufferedWriter\\001\\000\\032java/io/OutputStreamWriter\\014\\001p\\001q\\014\\000\\202\\001r\\014\\000\\202\\001s\\014\\001t\\001U\\014\\001u\\000\\311\\014\\001v\\000\\203\\001\\000\\032Waiting for program after \\014\\001X\\001w\\001\\000\\003ms.\\014\\001x\\001`\\014\\001y\\001[\\001\\000\\036java/lang/InterruptedException\\014\\001z\\000\\203\\014\\001{\\000\\203\\014\\001|\\001[\\001\\000+Abnormal Program termination.\\012Exit status: \\001\\000\\001\\012\\014\\001}\\001J\\014\\001~\\001U\\014\\001\\177\\000\\311\\001\\000\\023java/lang/Exception\\014\\001\\200\\001U\\014\\000\\202\\001\\201\\001\\000\\016.*uses\\\\s+crt.*\\001\\006\\306append asm assign blockread blockwrite close eof eoln erase filepos filesize flush getdir mkdir reset rewrite rmdir seek seekeof seekeoln seekof seekoln truncate uses access acct alarm ork chdir chown chroot clearerr clearerr_unlocked close confstr crypt ctermid daemon dup2 dup encrypt endusershell euidaccess execl execle execlp execv execve execvp _exit fchdir fchown fcloseall fclose fdatasync fdopen feof feof_unlocked ferror ferror_unlocked fexecve fflush fflush_unlocked fgetc fgetc_unlocked fgetpos64 fgetpos fgets fgets_unlocked fileno fileno_unlocked flockfile fmemopen fopen64 fopen fopencookie fork fpathconf fprintf fputc fputc_unlocked fputs fputs_unlocked fread fread_unlocked freopen64 freopen fscanf fseek fseeko64 fseeko fsetpos64 fsetpos ftell ftello64 ftello ftruncate64 ftruncate ftrylockfile funlockfile fwrite fwrite_unlocked getc getc_unlocked get_current_dir_name getcwd __getdelim getdelim getdomainname getegid geteuid getgid getgroups gethostid gethostname getline getlogin getlogin_r getpagesize getpass __getpgid getpgid getpgrp getpid getppid getsid getuid getusershell getw getwd group_member isatty lchown link lockf64 lockf lseek nice __off64t open open_memstream pathconf pause pclose pipe popen pread64 pread profil pthread_atfork pthread_ putc putc_unlocked putw pwrite64 pwrite readlink remove rename revoke rewind rmdir sbrk setbuf setbuffer setdomainname setegid seteuid setgid sethostid sethostname setlinebuf setlogin setpgid setpgrp setregid setreuid setsid setuid setusershell setvbuf signal sleep swab symlink sync sysconf tcgetpgrp tcsetpgrp tempnam tmpfile64 tmpfile tmpnam tmpnam_r truncate64 truncate ttyname ttyname_r ttyslot ualarm ungetc unlink usleep vfork vfprintf vfscanf vhangup\\014\\001\\202\\001[\\014\\001\\203\\001\\204\\014\\001X\\001\\205\\001\\000\\011(?s).*\\\\W(\\001\\000\\001 \\001\\000\\001|\\014\\001\\206\\001\\207\\001\\000\\005)\\\\W.*\\001\\000\\021COMPILED_FILENAME\\007\\001\\210\\001\\000\\015CODE_FILENAME\\001\\000\\014COMPILED_DIR\\001\\000\\010CODE_DIR\\001\\000\\006\\\\.pas$\\001\\000\\000\\001\\000\\006\\\\.exe$\\014\\001\\211\\000~\\001\\000\\001$\\001\\000\\002.o\\014\\000}\\000~\\001\\000\\004.pas\\001\\000\\004.exe\\001\\000\\030java/io/FileOutputStream\\007\\001H\\014\\001u\\001\\201\\001\\000\\003fpc\\001\\000\\003-O2\\001\\000\\003-XS\\001\\000\\003-Xt\\001\\000\\002-o\\014\\000\\202\\001\\212\\001\\000\\020No ppc386 found.\\001\\000\\017No ppc386 found\\001\\000\\017COMPILE_TIMEOUT\\007\\001\\213\\014\\001\\214\\001\\215\\001\\000\\014java/io/File\\014\\001\\216\\001L\\014\\001\\217\\000\\203\\001\\0001pl/umk/mat/zawodyweb/compiler/classes/LanguagePAS\\014\\001\\220\\001\\221\\001\\000\\020java/lang/Object\\001\\000/pl/umk/mat/zawodyweb/compiler/CompilerInterface\\001\\000\\016java/util/List\\001\\000&pl/umk/mat/zawodyweb/checker/TestInput\\001\\000\\021java/lang/Process\\001\\000\\024java/io/OutputStream\\001\\000\\011setResult\\001\\000\\004(I)V\\001\\000\\007isEmpty\\001\\000\\003()Z\\001\\000\\015setResultDesc\\001\\000\\020java/lang/System\\001\\000\\002gc\\001\\000\\020java/util/Arrays\\001\\000\\006asList\\001\\000%([Ljava/lang/Object;)Ljava/util/List;\\001\\000\\013getProperty\\001\\000\\013toLowerCase\\001\\000\\024()Ljava/lang/String;\\001\\000\\007matches\\001\\000\\025(Ljava/lang/String;)Z\\001\\000\\006append\\001\\000-(Ljava/lang/String;)Ljava/lang/StringBuilder;\\001\\000\\016getMemoryLimit\\001\\000\\003()I\\001\\000\\034(I)Ljava/lang/StringBuilder;\\001\\000\\010toString\\001\\000\\027org/apache/log4j/Logger\\001\\000\\005error\\001\\000\\025(Ljava/lang/Object;)V\\001\\000\\023(Ljava/util/List;)V\\001\\000\\005start\\001\\000\\025()Ljava/lang/Process;\\001\\000\\007getTime\\001\\000\\003()J\\001\\000\\020java/lang/Thread\\001\\000\\015currentThread\\001\\000\\024()Ljava/lang/Thread;\\001\\000\\014getTimeLimit\\001\\000\\010schedule\\001\\000\\026(Ljava/lang/Thread;J)V\\001\\000\\016getInputStream\\001\\000\\027()Ljava/io/InputStream;\\001\\000\\030(Ljava/io/InputStream;)V\\001\\000\\023(Ljava/io/Reader;)V\\001\\000\\017getOutputStream\\001\\000\\030()Ljava/io/OutputStream;\\001\\000\\031(Ljava/io/OutputStream;)V\\001\\000\\023(Ljava/io/Writer;)V\\001\\000\\007getText\\001\\000\\005write\\001\\000\\005close\\001\\000\\034(J)Ljava/lang/StringBuilder;\\001\\000\\005debug\\001\\000\\007waitFor\\001\\000\\007destroy\\001\\000\\006cancel\\001\\000\\011exitValue\\001\\000\\012setRuntime\\001\\000\\010readLine\\001\\000\\007setText\\001\\000\\012getMessage\\001\\000\\005([B)V\\001\\000\\006length\\001\\000\\006charAt\\001\\000\\004(I)C\\001\\000\\034(C)Ljava/lang/StringBuilder;\\001\\000\\012replaceAll\\001\\0008(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;\\001\\000\\024java/util/Properties\\001\\000\\011separator\\001\\000\\026([Ljava/lang/String;)V\\001\\000\\021java/lang/Integer\\001\\000\\010parseInt\\001\\000\\025(Ljava/lang/String;)I\\001\\000\\006delete\\001\\000\\017printStackTrace\\001\\000\\011getLogger\\001\\000,(Ljava/lang/Class;)Lorg/apache/log4j/Logger;\\000!\\000u\\000w\\000\\001\\000x\\000\\005\\000\\031\\000y\\000z\\000\\000\\000\\000\\000{\\000|\\000\\000\\000\\000\\000}\\000~\\000\\000\\000\\000\\000\\177\\000\\200\\000\\000\\000\\000\\000\\201\\000~\\000\\000\\000\\010\\000\\001\\000\\202\\000\\203\\000\\001\\000\\204\\000\\000\\000G\\000\\003\\000\\001\\000\\000\\000\\025*\\267\\000\\001*\\002\\265\\000\\002*\\273\\000\\003Y\\267\\000\\004\\265\\000\\005\\261\\000\\000\\000\\002\\000\\205\\000\\000\\000\\016\\000\\003\\000\\000\\000\\033\\000\\004\\000 \\000\\011\\000!\\000\\206\\000\\000\\000\\014\\000\\001\\000\\000\\000\\025\\000\\207\\000\\210\\000\\000\\000\\001\\000\\211\\000\\212\\000\\001\\000\\204\\000\\000\\000>\\000\\002\\000\\002\\000\\000\\000\\006*+\\265\\000\\006\\261\\000\\000\\000\\002\\000\\205\\000\\000\\000\\012\\000\\002\\000\\000\\000%\\000\\005\\000&\\000\\206\\000\\000\\000\\026\\000\\002\\000\\000\\000\\006\\000\\207\\000\\210\\000\\000\\000\\000\\000\\006\\000{\\000|\\000\\001\\000\\001\\000\\213\\000\\214\\000\\001\\000\\204\\000\\000\\003\\370\\000\\006\\000\\016\\000\\000\\001\\351\\273\\000\\007Y\\001\\267\\000\\010N*\\264\\000\\002\\002\\237\\000\\037-*\\264\\000\\002\\266\\000\\011*\\264\\000\\005\\266\\000\\012\\232\\000\\013-*\\264\\000\\005\\266\\000\\013-\\260\\270\\000\\014\\004\\275\\000\\003Y\\003+S\\270\\000\\015:\\005\\022\\016\\270\\000\\017\\266\\000\\020\\022\\021\\266\\000\\022\\232\\000;\\006\\275\\000\\003Y\\003\\022\\023SY\\004\\022\\024SY\\005\\273\\000\\025Y\\267\\000\\026\\022\\027\\266\\000\\030,\\266\\000\\031\\266\\000\\032\\022\\033\\266\\000\\030+\\266\\000\\030\\266\\000\\034S\\270\\000\\015:\\005\\247\\000%\\262\\000\\035\\273\\000\\025Y\\267\\000\\026\\022\\036\\266\\000\\030\\022\\016\\270\\000\\017\\266\\000\\030\\022\\037\\266\\000\\030\\266\\000\\034\\266\\000 \\273\\000!Y\\267\\000":\\006\\273\\000#Y\\031\\005\\267\\000$\\266\\000%:\\007\\273\\000&Y\\267\\000'\\266\\000(7\\010\\031\\006\\270\\000),\\266\\000*\\205\\266\\000+\\273\\000,Y\\273\\000-Y\\031\\007\\266\\000.\\267\\000/\\267\\0000:\\004\\273\\0001Y\\273\\0002Y\\031\\007\\266\\0003\\267\\0004\\267\\0005:\\012\\031\\012,\\266\\0006\\266\\0007\\031\\012\\266\\0008\\262\\000\\035\\273\\000\\025Y\\267\\000\\026\\0229\\266\\000\\030\\273\\000&Y\\267\\000'\\266\\000(\\026\\010e\\266\\000:\\022;\\266\\000\\030\\266\\000\\034\\266\\000<\\031\\007\\266\\000=W\\247\\000\\021:\\012\\031\\007\\266\\000?-\\005\\266\\000\\011-\\260\\273\\000&Y\\267\\000'\\266\\000(7\\012\\031\\006\\266\\000@\\031\\007\\266\\000A\\231\\000*-\\007\\266\\000\\011-\\273\\000\\025Y\\267\\000\\026\\022B\\266\\000\\030\\031\\007\\266\\000A\\266\\000\\032\\022C\\266\\000\\030\\266\\000\\034\\266\\000\\013-\\260-\\026\\012\\026\\010e\\210\\266\\000D\\273\\000\\003Y\\267\\000\\004:\\014\\031\\004\\266\\000EY:\\015\\306\\000!\\273\\000\\025Y\\267\\000\\026\\031\\014\\266\\000\\030\\031\\015\\266\\000\\030\\022C\\266\\000\\030\\266\\000\\034:\\014\\247\\377\\332-\\031\\014\\266\\000F\\031\\007\\266\\000?\\247\\000\\020:\\006\\262\\000\\035\\031\\006\\266\\000H\\266\\000 -\\260\\000\\004\\000\\327\\001?\\001B\\000>\\000\\247\\001O\\001\\332\\000G\\001P\\001\\217\\001\\332\\000G\\001\\220\\001\\327\\001\\332\\000G\\000\\004\\000\\205\\000\\000\\000\\252\\000*\\000\\000\\000*\\000\\011\\000+\\000\\021\\000,\\000\\031\\000-\\000#\\000.\\000+\\0000\\000-\\0003\\0000\\0004\\000=\\0005\\000M\\0006\\000\\205\\0008\\000\\247\\000;\\000\\260\\000<\\000\\276\\000=\\000\\312\\000>\\000\\327\\000@\\000\\354\\000A\\001\\001\\000B\\001\\012\\000D\\001\\017\\000E\\0019\\000F\\001?\\000K\\001B\\000G\\001D\\000H\\001I\\000I\\001N\\000J\\001P\\000L\\001\\\\\\000M\\001a\\000O\\001i\\000P\\001n\\000Q\\001\\216\\000R\\001\\220\\000T\\001\\232\\000U\\001\\243\\000W\\001\\256\\000X\\001\\314\\000Z\\001\\322\\000[\\001\\327\\000^\\001\\332\\000\\\\\\001\\334\\000]\\001\\347\\000_\\000\\206\\000\\000\\000\\230\\000\\017\\001\\001\\000>\\000\\215\\000\\216\\000\\012\\001D\\000\\014\\000\\217\\000\\220\\000\\012\\000\\260\\001'\\000\\221\\000\\222\\000\\006\\000\\276\\001\\031\\000\\223\\000\\224\\000\\007\\000\\312\\001\\015\\000\\225\\000\\226\\000\\010\\001\\\\\\000{\\000\\227\\000\\226\\000\\012\\001\\243\\0004\\000\\230\\000~\\000\\014\\001\\253\\000,\\000\\231\\000~\\000\\015\\001\\334\\000\\013\\000\\217\\000\\232\\000\\006\\000\\000\\001\\351\\000\\207\\000\\210\\000\\000\\000\\000\\001\\351\\000\\233\\000~\\000\\001\\000\\000\\001\\351\\000\\234\\000\\235\\000\\002\\000\\011\\001\\340\\000\\236\\000\\237\\000\\003\\000\\354\\000\\375\\000\\240\\000\\241\\000\\004\\000=\\001\\254\\000\\242\\000\\243\\000\\005\\000\\244\\000\\000\\000\\014\\000\\001\\000=\\001\\254\\000\\242\\000\\245\\000\\005\\000\\246\\000\\000\\000}\\000\\013\\374\\000+\\007\\000\\247\\001\\375\\000W\\000\\007\\000\\250!\\377\\000\\232\\000\\011\\007\\000\\251\\007\\000\\252\\007\\000\\253\\007\\000\\247\\000\\007\\000\\250\\007\\000\\254\\007\\000\\255\\004\\000\\001\\007\\000\\256\\377\\000\\015\\000\\011\\007\\000\\251\\007\\000\\252\\007\\000\\253\\007\\000\\247\\007\\000\\257\\007\\000\\250\\007\\000\\254\\007\\000\\255\\004\\000\\000\\374\\000?\\004\\374\\000\\022\\007\\000\\252\\374\\000(\\007\\000\\252\\377\\000\\015\\000\\006\\007\\000\\251\\007\\000\\252\\007\\000\\253\\007\\000\\247\\000\\007\\000\\250\\000\\001\\007\\000\\260\\014\\000\\001\\000\\261\\000\\262\\000\\001\\000\\204\\000\\000\\001\\335\\000\\004\\000\\007\\000\\000\\000\\343\\273\\000\\003Y+\\267\\000IM,\\022J\\266\\000\\022\\231\\000\\011*\\020\\006\\265\\000\\002\\022KN\\273\\000\\003Y\\267\\000\\004:\\004,\\266\\000L\\004d6\\005\\0036\\006\\025\\006\\025\\005\\242\\000{,\\025\\006\\266\\000M\\020{\\240\\000\\027,\\025\\006\\266\\000M\\020}\\237\\000\\011\\204\\006\\001\\247\\377\\362\\204\\006\\001,\\025\\006\\266\\000M\\020(\\240\\0001,\\025\\006\\004`\\266\\000M\\020*\\240\\000$,\\025\\006\\266\\000M\\020*\\240\\000\\020,\\025\\006\\004`\\266\\000M\\020)\\237\\000\\011\\204\\006\\001\\247\\377\\345\\204\\006\\002\\273\\000\\025Y\\267\\000\\026\\031\\004\\266\\000\\030,\\025\\006\\266\\000M\\266\\000N\\266\\000\\034:\\004\\204\\006\\001\\247\\377\\204\\031\\004M\\273\\000\\025Y\\267\\000\\026\\022O\\266\\000\\030-\\022P\\022Q\\266\\000R\\266\\000\\030\\022S\\266\\000\\030\\266\\000\\034:\\006,\\031\\006\\266\\000\\022\\231\\000\\011*\\020\\006\\265\\000\\002+\\260\\000\\000\\000\\003\\000\\205\\000\\000\\000Z\\000\\026\\000\\000\\000d\\000\\011\\000e\\000\\022\\000f\\000\\030\\000h\\000\\033\\000{\\000$\\000|\\000,\\000}\\0006\\000~\\000A\\000\\177\\000L\\000\\200\\000R\\000\\202\\000U\\000\\204\\000m\\000\\205\\000\\205\\000\\206\\000\\213\\000\\210\\000\\216\\000\\212\\000\\250\\000}\\000\\256\\000\\214\\000\\261\\000\\215\\000\\322\\000\\216\\000\\333\\000\\217\\000\\341\\000\\221\\000\\206\\000\\000\\000R\\000\\010\\000/\\000\\177\\000\\263\\000\\200\\000\\006\\000\\000\\000\\343\\000\\207\\000\\210\\000\\000\\000\\000\\000\\343\\000\\264\\000\\265\\000\\001\\000\\011\\000\\332\\000\\266\\000~\\000\\002\\000\\033\\000\\310\\000\\267\\000~\\000\\003\\000$\\000\\277\\000\\270\\000~\\000\\004\\000,\\000\\267\\000\\271\\000\\200\\000\\005\\000\\322\\000\\021\\000\\272\\000~\\000\\006\\000\\246\\000\\000\\0000\\000\\013\\374\\000\\030\\007\\000\\252\\377\\000\\026\\000\\007\\007\\000\\251\\007\\000\\273\\007\\000\\252\\007\\000\\252\\007\\000\\252\\001\\001\\000\\000\\021\\020\\002\\027\\027\\005\\002\\372\\000\\037\\374\\0002\\007\\000\\252\\000\\001\\000\\274\\000\\275\\000\\001\\000\\204\\000\\000\\004\\202\\000\\007\\000\\014\\000\\000\\002\\034\\001M*\\264\\000\\002\\002\\237\\000\\013\\273\\000\\003Y\\267\\000\\004\\260*\\264\\000\\006\\022T\\266\\000UM*\\264\\000\\006\\022V\\266\\000U:\\004*\\264\\000\\006\\022W\\266\\000U:\\005*\\264\\000\\006\\022X\\266\\000U:\\006\\031\\004\\022Y\\022Z\\266\\000R:\\004,\\022[\\022Z\\266\\000RM\\031\\006\\273\\000\\025Y\\267\\000\\026\\262\\000\\\\\\266\\000\\030\\022]\\266\\000\\030\\266\\000\\034\\022Z\\266\\000R:\\006\\031\\005\\273\\000\\025Y\\267\\000\\026\\262\\000\\\\\\266\\000\\030\\022]\\266\\000\\030\\266\\000\\034\\022Z\\266\\000R:\\005*\\273\\000\\025Y\\267\\000\\026\\031\\005\\266\\000\\030\\262\\000\\\\\\266\\000\\030\\031\\004\\266\\000\\030\\022^\\266\\000\\030\\266\\000\\034\\265\\000_\\273\\000\\025Y\\267\\000\\026\\031\\006\\266\\000\\030\\262\\000\\\\\\266\\000\\030\\031\\004\\266\\000\\030\\022`\\266\\000\\030\\266\\000\\034:\\004\\273\\000\\025Y\\267\\000\\026\\031\\005\\266\\000\\030\\262\\000\\\\\\266\\000\\030,\\266\\000\\030\\022a\\266\\000\\030\\266\\000\\034M\\273\\000bY\\031\\004\\267\\000c:\\007\\031\\007+\\266\\000d\\031\\007\\266\\000e\\270\\000\\014\\001:\\010\\273\\000#Y\\020\\006\\275\\000\\003Y\\003\\022fSY\\004\\022gSY\\005\\022hSY\\006\\022iSY\\007\\273\\000\\025Y\\267\\000\\026\\022j\\266\\000\\030,\\266\\000\\030\\266\\000\\034SY\\010\\031\\004S\\267\\000k\\266\\000%:\\010\\247\\000\\033:\\011\\262\\000\\035\\022l\\266\\000 *\\020\\010\\265\\000\\002*\\022m\\265\\000\\005,\\260\\273\\000,Y\\273\\000-Y\\031\\010\\266\\000.\\267\\000/\\267\\0000:\\011\\273\\000!Y\\267\\000":\\012\\031\\012\\270\\000)*\\264\\000\\006\\022n\\266\\000U\\270\\000o\\205\\266\\000+\\031\\010\\266\\000=W\\247\\000\\022:\\013\\031\\010\\266\\000?*\\020\\007\\265\\000\\002,\\260\\031\\012\\266\\000@\\031\\010\\266\\000A\\231\\000D*\\006\\265\\000\\002\\0046\\013\\031\\011\\266\\000EYN\\306\\000-\\025\\013\\007\\244\\000!*\\273\\000\\025Y\\267\\000\\026*\\264\\000\\005\\266\\000\\030-\\266\\000\\030\\022C\\266\\000\\030\\266\\000\\034\\265\\000\\005\\204\\013\\001\\247\\377\\317\\031\\011\\266\\000p\\273\\000qY\\031\\004\\267\\000r\\266\\000sW\\031\\010\\266\\000?\\247\\000\\010N-\\266\\000t,\\260\\000\\005\\001\\014\\001L\\001O\\000G\\001\\232\\001\\240\\001\\243\\000>\\000\\022\\001f\\002\\025\\000G\\001g\\001\\261\\002\\025\\000G\\001\\262\\002\\022\\002\\025\\000G\\000\\003\\000\\205\\000\\000\\000\\312\\0002\\000\\000\\000\\226\\000\\002\\000\\227\\000\\012\\000\\230\\000\\022\\000\\234\\000\\034\\000\\235\\000'\\000\\236\\0002\\000\\237\\000=\\000\\240\\000H\\000\\241\\000Q\\000\\242\\000o\\000\\243\\000\\215\\000\\244\\000\\260\\000\\245\\000\\321\\000\\246\\000\\360\\000\\247\\000\\373\\000\\250\\001\\001\\000\\251\\001\\006\\000\\252\\001\\011\\000\\253\\001\\014\\000\\255\\001L\\000\\263\\001O\\000\\256\\001Q\\000\\257\\001Y\\000\\260\\001_\\000\\261\\001e\\000\\262\\001g\\000\\264\\001|\\000\\265\\001\\205\\000\\266\\001\\232\\000\\270\\001\\240\\000\\275\\001\\243\\000\\271\\001\\245\\000\\272\\001\\252\\000\\273\\001\\260\\000\\274\\001\\262\\000\\276\\001\\267\\000\\300\\001\\277\\000\\302\\001\\304\\000\\303\\001\\307\\000\\304\\001\\321\\000\\305\\001\\327\\000\\306\\001\\365\\000\\310\\001\\373\\000\\312\\002\\000\\000\\314\\002\\015\\000\\315\\002\\022\\000\\320\\002\\025\\000\\316\\002\\026\\000\\317\\002\\032\\000\\321\\000\\206\\000\\000\\000\\230\\000\\017\\001Q\\000\\026\\000\\217\\000\\232\\000\\011\\001\\245\\000\\015\\000\\217\\000\\220\\000\\013\\001\\307\\0009\\000\\263\\000\\200\\000\\013\\001\\316\\0002\\000\\231\\000~\\000\\003\\000'\\001\\353\\000\\276\\000~\\000\\004\\0002\\001\\340\\000\\277\\000~\\000\\005\\000=\\001\\325\\000\\300\\000~\\000\\006\\000\\373\\001\\027\\000\\301\\000\\302\\000\\007\\001\\014\\001\\006\\000\\223\\000\\224\\000\\010\\001|\\000\\226\\000\\234\\000\\241\\000\\011\\001\\205\\000\\215\\000\\221\\000\\222\\000\\012\\002\\026\\000\\004\\000\\303\\000\\232\\000\\003\\000\\000\\002\\034\\000\\207\\000\\210\\000\\000\\000\\000\\002\\034\\000\\264\\000\\265\\000\\001\\000\\002\\002\\032\\000\\304\\000~\\000\\002\\000\\246\\000\\000\\000\\276\\000\\013\\374\\000\\022\\007\\000\\252\\377\\001<\\000\\011\\007\\000\\251\\007\\000\\273\\007\\000\\252\\000\\007\\000\\252\\007\\000\\252\\007\\000\\252\\007\\000\\305\\007\\000\\255\\000\\001\\007\\000\\260\\027\\377\\000;\\000\\013\\007\\000\\251\\007\\000\\273\\007\\000\\252\\000\\007\\000\\252\\007\\000\\252\\007\\000\\252\\007\\000\\305\\007\\000\\255\\007\\000\\257\\007\\000\\254\\000\\001\\007\\000\\256\\016\\374\\000\\024\\001\\377\\000-\\000\\014\\007\\000\\251\\007\\000\\273\\007\\000\\252\\007\\000\\252\\007\\000\\252\\007\\000\\252\\007\\000\\252\\007\\000\\305\\007\\000\\255\\007\\000\\257\\007\\000\\254\\001\\000\\000\\005\\377\\000\\004\\000\\013\\007\\000\\251\\007\\000\\273\\007\\000\\252\\000\\007\\000\\252\\007\\000\\252\\007\\000\\252\\007\\000\\305\\007\\000\\255\\007\\000\\257\\007\\000\\254\\000\\000\\377\\000\\024\\000\\003\\007\\000\\251\\007\\000\\273\\007\\000\\252\\000\\001\\007\\000\\260\\004\\000\\001\\000\\306\\000\\307\\000\\001\\000\\204\\000\\000\\000]\\000\\003\\000\\002\\000\\000\\000\\030*\\264\\000_\\306\\000\\022\\273\\000qY*\\264\\000_\\267\\000r\\266\\000sW+\\260\\000\\000\\000\\003\\000\\205\\000\\000\\000\\016\\000\\003\\000\\000\\000\\326\\000\\007\\000\\327\\000\\026\\000\\331\\000\\206\\000\\000\\000\\026\\000\\002\\000\\000\\000\\030\\000\\207\\000\\210\\000\\000\\000\\000\\000\\030\\000\\233\\000~\\000\\001\\000\\246\\000\\000\\000\\003\\000\\001\\026\\000\\001\\000\\310\\000\\311\\000\\001\\000\\204\\000\\000\\000Y\\000\\003\\000\\002\\000\\000\\000\\024+\\266\\000\\012\\232\\000\\017\\273\\000qY+\\267\\000r\\266\\000sW\\261\\000\\000\\000\\003\\000\\205\\000\\000\\000\\016\\000\\003\\000\\000\\000\\336\\000\\007\\000\\337\\000\\023\\000\\341\\000\\206\\000\\000\\000\\026\\000\\002\\000\\000\\000\\024\\000\\207\\000\\210\\000\\000\\000\\000\\000\\024\\000\\233\\000~\\000\\001\\000\\246\\000\\000\\000\\003\\000\\001\\023\\000\\010\\000\\312\\000\\203\\000\\001\\000\\204\\000\\000\\000"\\000\\001\\000\\000\\000\\000\\000\\012\\023\\000u\\270\\000v\\263\\000\\035\\261\\000\\000\\000\\001\\000\\205\\000\\000\\000\\006\\000\\001\\000\\000\\000\\035\\000\\001\\000\\313\\000\\000\\000\\002\\000\\314
3	pl.umk.mat.zawodyweb.compiler.classes.LanguageC	1	C Compiler	\\312\\376\\272\\276\\000\\000\\0002\\001\\222\\012\\000w\\000\\314\\011\\000u\\000\\315\\007\\000\\316\\012\\000\\003\\000\\314\\011\\000u\\000\\317\\011\\000u\\000\\320\\007\\000\\321\\012\\000\\007\\000\\322\\012\\000\\007\\000\\323\\012\\000\\003\\000\\324\\012\\000\\007\\000\\325\\012\\000\\326\\000\\327\\012\\000\\330\\000\\331\\010\\000\\332\\012\\000\\326\\000\\333\\012\\000\\003\\000\\334\\010\\000\\335\\012\\000\\003\\000\\336\\010\\000\\337\\010\\000\\340\\007\\000\\341\\012\\000\\025\\000\\314\\010\\000\\342\\012\\000\\025\\000\\343\\012\\000\\344\\000\\345\\012\\000\\025\\000\\346\\010\\000\\347\\012\\000\\025\\000\\350\\011\\000u\\000\\351\\010\\000\\352\\010\\000\\353\\012\\000\\354\\000\\355\\007\\000\\356\\012\\000!\\000\\314\\007\\000\\357\\012\\000#\\000\\360\\012\\000#\\000\\361\\007\\000\\362\\012\\000&\\000\\314\\012\\000&\\000\\363\\012\\000\\364\\000\\365\\012\\000\\344\\000\\366\\012\\000!\\000\\367\\007\\000\\370\\007\\000\\371\\012\\000\\372\\000\\373\\012\\000-\\000\\374\\012\\000,\\000\\375\\007\\000\\376\\007\\000\\377\\012\\000\\372\\001\\000\\012\\0002\\001\\001\\012\\0001\\001\\002\\012\\000\\344\\001\\003\\012\\0001\\001\\004\\012\\0001\\001\\005\\010\\001\\006\\012\\000\\025\\001\\007\\010\\001\\010\\012\\000\\354\\001\\011\\012\\000\\372\\001\\012\\007\\001\\013\\012\\000\\372\\001\\014\\010\\001\\015\\012\\000!\\001\\016\\012\\000\\372\\001\\017\\010\\001\\020\\010\\001\\021\\012\\000\\007\\001\\022\\012\\000,\\001\\023\\012\\000\\007\\001\\024\\007\\001\\025\\012\\000H\\001\\026\\012\\000\\003\\001\\027\\010\\001\\030\\012\\000\\003\\001\\031\\012\\000\\003\\001\\032\\012\\000\\025\\001\\033\\010\\001\\034\\010\\001\\035\\010\\001\\036\\012\\000\\003\\001\\037\\010\\001 \\010\\001!\\012\\001"\\000\\333\\010\\001#\\010\\001$\\010\\001%\\010\\001&\\010\\001'\\010\\001(\\011\\000q\\001)\\010\\001*\\010\\001+\\010\\001,\\007\\001-\\012\\000`\\000\\322\\012\\001.\\001/\\012\\001.\\001\\005\\010\\0010\\010\\0011\\010\\0012\\010\\0013\\010\\0014\\012\\000#\\0015\\010\\0016\\010\\0017\\012\\000\\372\\0018\\010\\0019\\012\\001:\\001;\\010\\001<\\012\\000,\\001\\005\\007\\001=\\012\\000q\\000\\322\\012\\000q\\001>\\012\\000H\\001?\\007\\001@\\012\\000\\354\\001A\\007\\001B\\007\\001C\\001\\000\\006logger\\001\\000\\031Lorg/apache/log4j/Logger;\\001\\000\\012properties\\001\\000\\026Ljava/util/Properties;\\001\\000\\015compileResult\\001\\000\\001I\\001\\000\\013compileDesc\\001\\000\\022Ljava/lang/String;\\001\\000\\006<init>\\001\\000\\003()V\\001\\000\\004Code\\001\\000\\017LineNumberTable\\001\\000\\022LocalVariableTable\\001\\000\\004this\\001\\0001Lpl/umk/mat/zawodyweb/compiler/classes/LanguageC;\\001\\000\\015setProperties\\001\\000\\031(Ljava/util/Properties;)V\\001\\000\\007runTest\\001\\000e(Ljava/lang/String;Lpl/umk/mat/zawodyweb/checker/TestInput;)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\014outputStream\\001\\000\\030Ljava/io/BufferedWriter;\\001\\000\\002ex\\001\\000 Ljava/lang/InterruptedException;\\001\\000\\005timer\\001\\000+Lpl/umk/mat/zawodyweb/judge/InterruptTimer;\\001\\000\\001p\\001\\000\\023Ljava/lang/Process;\\001\\000\\004time\\001\\000\\001J\\001\\000\\013currentTime\\001\\000\\004line\\001\\000\\025Ljava/lang/Exception;\\001\\000\\004path\\001\\000\\005input\\001\\000(Lpl/umk/mat/zawodyweb/checker/TestInput;\\001\\000\\006output\\001\\000)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\012outputText\\001\\000\\013inputStream\\001\\000\\030Ljava/io/BufferedReader;\\001\\000\\007command\\001\\000\\020Ljava/util/List;\\001\\000\\026LocalVariableTypeTable\\001\\000$Ljava/util/List<Ljava/lang/String;>;\\001\\000\\015StackMapTable\\007\\000\\321\\007\\000\\316\\007\\001D\\007\\001@\\007\\001E\\007\\000\\356\\007\\001F\\007\\001\\013\\007\\000\\370\\007\\001\\025\\001\\000\\012precompile\\001\\000\\006([B)[B\\001\\000\\001i\\001\\000\\004code\\001\\000\\002[B\\001\\000\\003str\\001\\000\\016forbiddenCalls\\001\\000\\022strWithoutComments\\001\\000\\003len\\001\\000\\012regexp1_on\\007\\000\\264\\001\\000\\007compile\\001\\000\\026([B)Ljava/lang/String;\\001\\000\\010codefile\\001\\000\\013compileddir\\001\\000\\007codedir\\001\\000\\002is\\001\\000\\026Ljava/io/OutputStream;\\001\\000\\003err\\001\\000\\013compilefile\\007\\001G\\001\\000\\013postcompile\\001\\000&(Ljava/lang/String;)Ljava/lang/String;\\001\\000\\014closeProgram\\001\\000\\025(Ljava/lang/String;)V\\001\\000\\010<clinit>\\001\\000\\012SourceFile\\001\\000\\016LanguageC.java\\014\\000\\201\\000\\202\\014\\000}\\000~\\001\\000\\020java/lang/String\\014\\000\\177\\000\\200\\014\\000{\\000|\\001\\000'pl/umk/mat/zawodyweb/checker/TestOutput\\014\\000\\201\\000\\310\\014\\001H\\001I\\014\\001J\\001K\\014\\001L\\000\\310\\007\\001M\\014\\001N\\000\\202\\007\\001O\\014\\001P\\001Q\\001\\000\\007os.name\\014\\001R\\000\\306\\014\\001S\\001T\\001\\000\\017(?s).*windows.*\\014\\001U\\001V\\001\\000\\004bash\\001\\000\\002-c\\001\\000\\027java/lang/StringBuilder\\001\\000\\012ulimit -v \\014\\001W\\001X\\007\\001E\\014\\001Y\\001Z\\014\\001W\\001[\\001\\000\\004 && \\014\\001\\\\\\001T\\014\\000y\\000z\\001\\000\\021OS without bash: \\001\\000\\034. Memory Limit check is off.\\007\\001]\\014\\001^\\001_\\001\\000)pl/umk/mat/zawodyweb/judge/InterruptTimer\\001\\000\\030java/lang/ProcessBuilder\\014\\000\\201\\001`\\014\\001a\\001b\\001\\000\\016java/util/Date\\014\\001c\\001d\\007\\001e\\014\\001f\\001g\\014\\001h\\001Z\\014\\001i\\001j\\001\\000\\026java/io/BufferedReader\\001\\000\\031java/io/InputStreamReader\\007\\001F\\014\\001k\\001l\\014\\000\\201\\001m\\014\\000\\201\\001n\\001\\000\\026java/io/BufferedWriter\\001\\000\\032java/io/OutputStreamWriter\\014\\001o\\001p\\014\\000\\201\\001q\\014\\000\\201\\001r\\014\\001s\\001T\\014\\001t\\000\\310\\014\\001u\\000\\202\\001\\000\\032Waiting for program after \\014\\001W\\001v\\001\\000\\003ms.\\014\\001w\\001_\\014\\001x\\001Z\\001\\000\\036java/lang/InterruptedException\\014\\001y\\000\\202\\001\\000\\012TLE after \\014\\001z\\000\\202\\014\\001{\\001Z\\001\\000+Abnormal Program termination.\\012Exit status: \\001\\000\\001\\012\\014\\001|\\001I\\014\\001}\\001T\\014\\001~\\000\\310\\001\\000\\023java/lang/Exception\\014\\001\\177\\001T\\014\\000\\201\\001\\200\\001\\0062__asm__ __asm asm access acct alarm brk chdir chown chroot clearerr clearerr_unlocked closeconfstr crypt ctermid daemon dup2 dup encrypt endusershell euidaccess execl execle execlpexecv execve execvp _exit fchdir fchown fcloseall fclose fdatasync fdopen feof feof_unlockedferror ferror_unlocked fexecve fflush fflush_unlocked fgetc fgetc_unlocked fgetpos64 fgetposfgets fgets_unlocked fileno fileno_unlocked flockfile fmemopen fopen64 fopen fopencookie forkfpathconf fprintf fputc fputc_unlocked fputs fputs_unlocked fread fread_unlocked freopen64freopen fscanf fseek fseeko64 fseeko fsetpos64 fsetpos ftell ftello64 ftello ftruncate64ftruncate ftrylockfile funlockfile fwrite fwrite_unlocked getc getc_unlockedget_current_dir_name getcwd __getdelim getdelim getdomainname getegid geteuid getgid getgroupsgethostid gethostname getline getlogin getlogin_r getpagesize getpass __getpgid getpgidgetpgrp getpid getppid getsid getuid getusershell getw getwd group_member isatty lchown linklockf64 lockf lseek nice __off64t open open_memstream pathconf pause pclose pipe popen pread64pread profil pthread_atfork pthread_ putc putc_unlocked putw pwrite64 pwrite read readlinkremove rename revoke rewind rmdir sbrk setbuf setbuffer setdomainname setegid seteuid setgidsethostid sethostname setlinebuf setlogin setpgid setpgrp setregid setreuid setsid setuidsetusershell setvbuf signal sleep swab symlink sync sysconf tcgetpgrp tcsetpgrp tempnamtmpfile64 tmpfile tmpnam tmpnam_r truncate64 truncate ttyname ttyname_r ttyslot ualarm ungetcunlink usleep vfork vfprintf vfscanf vhangup write system\\014\\001\\201\\001Z\\014\\001\\202\\001\\203\\014\\001W\\001\\204\\001\\000\\011(?s).*\\\\W(\\001\\000\\001 \\001\\000\\001|\\014\\001\\205\\001\\206\\001\\000\\005)\\\\W.*\\001\\000\\021COMPILED_FILENAME\\007\\001\\207\\001\\000\\015CODE_FILENAME\\001\\000\\014COMPILED_DIR\\001\\000\\010CODE_DIR\\001\\000\\004\\\\.c$\\001\\000\\000\\001\\000\\006\\\\.exe$\\014\\001\\210\\000\\200\\001\\000\\001$\\001\\000\\002.c\\001\\000\\004.exe\\001\\000\\030java/io/FileOutputStream\\007\\001G\\014\\001t\\001\\200\\001\\000\\003gcc\\001\\000\\003-O2\\001\\000\\007-static\\001\\000\\002-o\\001\\000\\003-lm\\014\\000\\201\\001\\211\\001\\000\\015No gcc found.\\001\\000\\014No gcc found\\014\\001\\212\\001l\\001\\000\\017COMPILE_TIMEOUT\\007\\001\\213\\014\\001\\214\\001\\215\\001\\000\\003^.*\\001\\000\\014java/io/File\\014\\001\\216\\001K\\014\\001\\217\\000\\202\\001\\000/pl/umk/mat/zawodyweb/compiler/classes/LanguageC\\014\\001\\220\\001\\221\\001\\000\\020java/lang/Object\\001\\000/pl/umk/mat/zawodyweb/compiler/CompilerInterface\\001\\000\\016java/util/List\\001\\000&pl/umk/mat/zawodyweb/checker/TestInput\\001\\000\\021java/lang/Process\\001\\000\\024java/io/OutputStream\\001\\000\\011setResult\\001\\000\\004(I)V\\001\\000\\007isEmpty\\001\\000\\003()Z\\001\\000\\015setResultDesc\\001\\000\\020java/lang/System\\001\\000\\002gc\\001\\000\\020java/util/Arrays\\001\\000\\006asList\\001\\000%([Ljava/lang/Object;)Ljava/util/List;\\001\\000\\013getProperty\\001\\000\\013toLowerCase\\001\\000\\024()Ljava/lang/String;\\001\\000\\007matches\\001\\000\\025(Ljava/lang/String;)Z\\001\\000\\006append\\001\\000-(Ljava/lang/String;)Ljava/lang/StringBuilder;\\001\\000\\016getMemoryLimit\\001\\000\\003()I\\001\\000\\034(I)Ljava/lang/StringBuilder;\\001\\000\\010toString\\001\\000\\027org/apache/log4j/Logger\\001\\000\\005error\\001\\000\\025(Ljava/lang/Object;)V\\001\\000\\023(Ljava/util/List;)V\\001\\000\\005start\\001\\000\\025()Ljava/lang/Process;\\001\\000\\007getTime\\001\\000\\003()J\\001\\000\\020java/lang/Thread\\001\\000\\015currentThread\\001\\000\\024()Ljava/lang/Thread;\\001\\000\\014getTimeLimit\\001\\000\\010schedule\\001\\000\\026(Ljava/lang/Thread;J)V\\001\\000\\016getInputStream\\001\\000\\027()Ljava/io/InputStream;\\001\\000\\030(Ljava/io/InputStream;)V\\001\\000\\023(Ljava/io/Reader;)V\\001\\000\\017getOutputStream\\001\\000\\030()Ljava/io/OutputStream;\\001\\000\\031(Ljava/io/OutputStream;)V\\001\\000\\023(Ljava/io/Writer;)V\\001\\000\\007getText\\001\\000\\005write\\001\\000\\005close\\001\\000\\034(J)Ljava/lang/StringBuilder;\\001\\000\\005debug\\001\\000\\007waitFor\\001\\000\\007destroy\\001\\000\\006cancel\\001\\000\\011exitValue\\001\\000\\012setRuntime\\001\\000\\010readLine\\001\\000\\007setText\\001\\000\\012getMessage\\001\\000\\005([B)V\\001\\000\\006length\\001\\000\\006charAt\\001\\000\\004(I)C\\001\\000\\034(C)Ljava/lang/StringBuilder;\\001\\000\\012replaceAll\\001\\0008(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;\\001\\000\\024java/util/Properties\\001\\000\\011separator\\001\\000\\026([Ljava/lang/String;)V\\001\\000\\016getErrorStream\\001\\000\\021java/lang/Integer\\001\\000\\010parseInt\\001\\000\\025(Ljava/lang/String;)I\\001\\000\\006delete\\001\\000\\017printStackTrace\\001\\000\\011getLogger\\001\\000,(Ljava/lang/Class;)Lorg/apache/log4j/Logger;\\000!\\000u\\000w\\000\\001\\000x\\000\\004\\000\\031\\000y\\000z\\000\\000\\000\\000\\000{\\000|\\000\\000\\000\\000\\000}\\000~\\000\\000\\000\\000\\000\\177\\000\\200\\000\\000\\000\\010\\000\\001\\000\\201\\000\\202\\000\\001\\000\\203\\000\\000\\000G\\000\\003\\000\\001\\000\\000\\000\\025*\\267\\000\\001*\\002\\265\\000\\002*\\273\\000\\003Y\\267\\000\\004\\265\\000\\005\\261\\000\\000\\000\\002\\000\\204\\000\\000\\000\\016\\000\\003\\000\\000\\000\\033\\000\\004\\000\\037\\000\\011\\000 \\000\\205\\000\\000\\000\\014\\000\\001\\000\\000\\000\\025\\000\\206\\000\\207\\000\\000\\000\\001\\000\\210\\000\\211\\000\\001\\000\\203\\000\\000\\000>\\000\\002\\000\\002\\000\\000\\000\\006*+\\265\\000\\006\\261\\000\\000\\000\\002\\000\\204\\000\\000\\000\\012\\000\\002\\000\\000\\000$\\000\\005\\000%\\000\\205\\000\\000\\000\\026\\000\\002\\000\\000\\000\\006\\000\\206\\000\\207\\000\\000\\000\\000\\000\\006\\000{\\000|\\000\\001\\000\\001\\000\\212\\000\\213\\000\\001\\000\\203\\000\\000\\0043\\000\\006\\000\\016\\000\\000\\002\\031\\273\\000\\007Y\\273\\000\\003Y\\267\\000\\004\\267\\000\\010N\\273\\000\\003Y\\267\\000\\004:\\004*\\264\\000\\002\\002\\237\\000\\037-*\\264\\000\\002\\266\\000\\011*\\264\\000\\005\\266\\000\\012\\232\\000\\013-*\\264\\000\\005\\266\\000\\013-\\260\\270\\000\\014\\004\\275\\000\\003Y\\003+S\\270\\000\\015:\\006\\022\\016\\270\\000\\017\\266\\000\\020\\022\\021\\266\\000\\022\\232\\000;\\006\\275\\000\\003Y\\003\\022\\023SY\\004\\022\\024SY\\005\\273\\000\\025Y\\267\\000\\026\\022\\027\\266\\000\\030,\\266\\000\\031\\266\\000\\032\\022\\033\\266\\000\\030+\\266\\000\\030\\266\\000\\034S\\270\\000\\015:\\006\\247\\000%\\262\\000\\035\\273\\000\\025Y\\267\\000\\026\\022\\036\\266\\000\\030\\022\\016\\270\\000\\017\\266\\000\\030\\022\\037\\266\\000\\030\\266\\000\\034\\266\\000 \\273\\000!Y\\267\\000":\\007\\273\\000#Y\\031\\006\\267\\000$\\266\\000%:\\010\\273\\000&Y\\267\\000'\\266\\000(7\\011\\031\\007\\270\\000),\\266\\000*\\205\\266\\000+\\273\\000,Y\\273\\000-Y\\031\\010\\266\\000.\\267\\000/\\267\\0000:\\005\\273\\0001Y\\273\\0002Y\\031\\010\\266\\0003\\267\\0004\\267\\0005:\\013\\031\\013,\\266\\0006\\266\\0007\\031\\013\\266\\0008\\262\\000\\035\\273\\000\\025Y\\267\\000\\026\\0229\\266\\000\\030\\273\\000&Y\\267\\000'\\266\\000(\\026\\011e\\266\\000:\\022;\\266\\000\\030\\266\\000\\034\\266\\000<\\031\\010\\266\\000=W\\247\\000;:\\013\\031\\010\\266\\000?-\\005\\266\\000\\011\\262\\000\\035\\273\\000\\025Y\\267\\000\\026\\022@\\266\\000\\030\\273\\000&Y\\267\\000'\\266\\000(\\026\\011e\\266\\000:\\022;\\266\\000\\030\\266\\000\\034\\266\\000<-\\260\\273\\000&Y\\267\\000'\\266\\000(7\\013\\031\\007\\266\\000A\\031\\010\\266\\000B\\231\\000*-\\007\\266\\000\\011-\\273\\000\\025Y\\267\\000\\026\\022C\\266\\000\\030\\031\\010\\266\\000B\\266\\000\\032\\022D\\266\\000\\030\\266\\000\\034\\266\\000\\013-\\260-\\026\\013\\026\\011e\\210\\266\\000E\\031\\005\\266\\000FY:\\015\\306\\000!\\273\\000\\025Y\\267\\000\\026\\031\\004\\266\\000\\030\\031\\015\\266\\000\\030\\022D\\266\\000\\030\\266\\000\\034:\\004\\247\\377\\332-\\031\\004\\266\\000G\\031\\010\\266\\000?\\247\\000\\020:\\007\\262\\000\\035\\031\\007\\266\\000I\\266\\000 -\\260\\000\\004\\000\\346\\001N\\001Q\\000>\\000\\266\\001\\210\\002\\012\\000H\\001\\211\\001\\310\\002\\012\\000H\\001\\311\\002\\007\\002\\012\\000H\\000\\004\\000\\204\\000\\000\\000\\256\\000+\\000\\000\\000)\\000\\017\\000*\\000\\030\\000+\\000 \\000,\\000(\\000-\\0002\\000.\\000:\\0000\\000<\\0003\\000?\\0004\\000L\\0005\\000\\\\\\0006\\000\\224\\0008\\000\\266\\000;\\000\\277\\000<\\000\\315\\000=\\000\\331\\000>\\000\\346\\000@\\000\\373\\000A\\001\\020\\000B\\001\\031\\000D\\001\\036\\000E\\001H\\000F\\001N\\000L\\001Q\\000G\\001S\\000H\\001X\\000I\\001]\\000J\\001\\207\\000K\\001\\211\\000M\\001\\225\\000N\\001\\232\\000P\\001\\242\\000Q\\001\\247\\000R\\001\\307\\000S\\001\\311\\000U\\001\\323\\000W\\001\\336\\000X\\001\\374\\000Z\\002\\002\\000[\\002\\007\\000^\\002\\012\\000\\\\\\002\\014\\000]\\002\\027\\000_\\000\\205\\000\\000\\000\\230\\000\\017\\001\\020\\000>\\000\\214\\000\\215\\000\\013\\001S\\0006\\000\\216\\000\\217\\000\\013\\000\\277\\001H\\000\\220\\000\\221\\000\\007\\000\\315\\001:\\000\\222\\000\\223\\000\\010\\000\\331\\001.\\000\\224\\000\\225\\000\\011\\001\\225\\000r\\000\\226\\000\\225\\000\\013\\001\\333\\000,\\000\\227\\000\\200\\000\\015\\002\\014\\000\\013\\000\\216\\000\\230\\000\\007\\000\\000\\002\\031\\000\\206\\000\\207\\000\\000\\000\\000\\002\\031\\000\\231\\000\\200\\000\\001\\000\\000\\002\\031\\000\\232\\000\\233\\000\\002\\000\\017\\002\\012\\000\\234\\000\\235\\000\\003\\000\\030\\002\\001\\000\\236\\000\\200\\000\\004\\000\\373\\001\\036\\000\\237\\000\\240\\000\\005\\000L\\001\\315\\000\\241\\000\\242\\000\\006\\000\\243\\000\\000\\000\\014\\000\\001\\000L\\001\\315\\000\\241\\000\\244\\000\\006\\000\\245\\000\\000\\000\\204\\000\\013\\375\\000:\\007\\000\\246\\007\\000\\247\\001\\375\\000W\\000\\007\\000\\250!\\377\\000\\232\\000\\012\\007\\000\\251\\007\\000\\247\\007\\000\\252\\007\\000\\246\\007\\000\\247\\000\\007\\000\\250\\007\\000\\253\\007\\000\\254\\004\\000\\001\\007\\000\\255\\377\\0007\\000\\012\\007\\000\\251\\007\\000\\247\\007\\000\\252\\007\\000\\246\\007\\000\\247\\007\\000\\256\\007\\000\\250\\007\\000\\253\\007\\000\\254\\004\\000\\000\\374\\000?\\004\\011\\374\\000(\\007\\000\\247\\377\\000\\015\\000\\007\\007\\000\\251\\007\\000\\247\\007\\000\\252\\007\\000\\246\\007\\000\\247\\000\\007\\000\\250\\000\\001\\007\\000\\257\\014\\000\\001\\000\\260\\000\\261\\000\\001\\000\\203\\000\\000\\001\\313\\000\\004\\000\\007\\000\\000\\000\\337\\273\\000\\003Y+\\267\\000JM\\022KN\\273\\000\\003Y\\267\\000\\004:\\004,\\266\\000L\\004d6\\005\\0036\\006\\025\\006\\025\\005\\242\\000\\206,\\025\\006\\266\\000M\\020/\\240\\000/,\\025\\006\\266\\000M\\020*\\240\\000$,\\025\\006\\266\\000M\\020*\\240\\000\\020,\\025\\006\\004`\\266\\000M\\020/\\237\\000\\011\\204\\006\\001\\247\\377\\345\\204\\006\\002,\\025\\006\\266\\000M\\020/\\240\\000$,\\025\\006\\004`\\266\\000M\\020/\\240\\000\\027,\\025\\006\\266\\000M\\020\\012\\237\\000\\011\\204\\006\\001\\247\\377\\362\\204\\006\\001\\273\\000\\025Y\\267\\000\\026\\031\\004\\266\\000\\030,\\025\\006\\266\\000M\\266\\000N\\266\\000\\034:\\004\\204\\006\\001\\247\\377y\\031\\004M\\273\\000\\025Y\\267\\000\\026\\022O\\266\\000\\030-\\022P\\022Q\\266\\000R\\266\\000\\030\\022S\\266\\000\\030\\266\\000\\034:\\006,\\031\\006\\266\\000\\022\\231\\000\\011*\\020\\006\\265\\000\\002+\\260\\000\\000\\000\\003\\000\\204\\000\\000\\000R\\000\\024\\000\\000\\000d\\000\\011\\000e\\000\\014\\000w\\000\\025\\000x\\000\\035\\000y\\000'\\000z\\000=\\000{\\000U\\000|\\000[\\000~\\000^\\000\\200\\000v\\000\\201\\000\\201\\000\\202\\000\\207\\000\\204\\000\\212\\000\\206\\000\\244\\000y\\000\\252\\000\\210\\000\\255\\000\\211\\000\\316\\000\\212\\000\\327\\000\\213\\000\\335\\000\\215\\000\\205\\000\\000\\000R\\000\\010\\000 \\000\\212\\000\\262\\000~\\000\\006\\000\\000\\000\\337\\000\\206\\000\\207\\000\\000\\000\\000\\000\\337\\000\\263\\000\\264\\000\\001\\000\\011\\000\\326\\000\\265\\000\\200\\000\\002\\000\\014\\000\\323\\000\\266\\000\\200\\000\\003\\000\\025\\000\\312\\000\\267\\000\\200\\000\\004\\000\\035\\000\\302\\000\\270\\000~\\000\\005\\000\\316\\000\\021\\000\\271\\000\\200\\000\\006\\000\\245\\000\\000\\000*\\000\\012\\377\\000 \\000\\007\\007\\000\\251\\007\\000\\272\\007\\000\\247\\007\\000\\247\\007\\000\\247\\001\\001\\000\\000\\034\\027\\005\\002\\027\\020\\002\\372\\000\\037\\374\\0002\\007\\000\\247\\000\\001\\000\\273\\000\\274\\000\\001\\000\\203\\000\\000\\004N\\000\\006\\000\\014\\000\\000\\002\\003\\001M*\\264\\000\\002\\002\\237\\000\\013\\273\\000\\003Y\\267\\000\\004\\260*\\264\\000\\006\\022T\\266\\000UM*\\264\\000\\006\\022V\\266\\000U:\\004*\\264\\000\\006\\022W\\266\\000U:\\005*\\264\\000\\006\\022X\\266\\000U:\\006\\031\\004\\022Y\\022Z\\266\\000R:\\004,\\022[\\022Z\\266\\000RM\\031\\006\\273\\000\\025Y\\267\\000\\026\\262\\000\\\\\\266\\000\\030\\022]\\266\\000\\030\\266\\000\\034\\022Z\\266\\000R:\\006\\031\\005\\273\\000\\025Y\\267\\000\\026\\262\\000\\\\\\266\\000\\030\\022]\\266\\000\\030\\266\\000\\034\\022Z\\266\\000R:\\005\\273\\000\\025Y\\267\\000\\026\\031\\006\\266\\000\\030\\262\\000\\\\\\266\\000\\030\\031\\004\\266\\000\\030\\022^\\266\\000\\030\\266\\000\\034:\\004\\273\\000\\025Y\\267\\000\\026\\031\\005\\266\\000\\030\\262\\000\\\\\\266\\000\\030,\\266\\000\\030\\022_\\266\\000\\030\\266\\000\\034M\\273\\000`Y\\031\\004\\267\\000a:\\007\\031\\007+\\266\\000b\\031\\007\\266\\000c\\270\\000\\014\\001:\\010\\273\\000#Y\\020\\007\\275\\000\\003Y\\003\\022dSY\\004\\022eSY\\005\\022fSY\\006\\022gSY\\007,SY\\010\\031\\004SY\\020\\006\\022hS\\267\\000i\\266\\000%:\\010\\247\\000\\033:\\011\\262\\000\\035\\022j\\266\\000 *\\020\\010\\265\\000\\002*\\022k\\265\\000\\005,\\260\\273\\000,Y\\273\\000-Y\\031\\010\\266\\000l\\267\\000/\\267\\0000:\\011\\273\\000!Y\\267\\000":\\012\\031\\012\\270\\000)*\\264\\000\\006\\022m\\266\\000U\\270\\000n\\205\\266\\000+\\031\\010\\266\\000=W\\247\\000\\022:\\013\\031\\010\\266\\000?*\\020\\007\\265\\000\\002,\\260\\031\\012\\266\\000A\\031\\010\\266\\000B\\231\\000Z*\\006\\265\\000\\002\\031\\011\\266\\000FYN\\306\\000F-\\273\\000\\025Y\\267\\000\\026\\022o\\266\\000\\030\\031\\004\\266\\000\\030\\266\\000\\034*\\264\\000\\006\\022V\\266\\000U\\266\\000RN*\\273\\000\\025Y\\267\\000\\026*\\264\\000\\005\\266\\000\\030-\\266\\000\\030\\022D\\266\\000\\030\\266\\000\\034\\265\\000\\005\\247\\377\\266\\031\\011\\266\\000p\\273\\000qY\\031\\004\\267\\000r\\266\\000sW\\031\\010\\266\\000?\\247\\000\\010N-\\266\\000t,\\260\\000\\005\\000\\351\\001\\035\\001 \\000H\\001k\\001q\\001t\\000>\\000\\022\\0017\\001\\374\\000H\\0018\\001\\202\\001\\374\\000H\\001\\203\\001\\371\\001\\374\\000H\\000\\003\\000\\204\\000\\000\\000\\276\\000/\\000\\000\\000\\222\\000\\002\\000\\223\\000\\012\\000\\224\\000\\022\\000\\230\\000\\034\\000\\231\\000'\\000\\232\\0002\\000\\233\\000=\\000\\234\\000H\\000\\235\\000Q\\000\\236\\000o\\000\\237\\000\\215\\000\\240\\000\\256\\000\\241\\000\\315\\000\\242\\000\\330\\000\\243\\000\\336\\000\\244\\000\\343\\000\\245\\000\\346\\000\\246\\000\\351\\000\\250\\001\\035\\000\\256\\001 \\000\\251\\001"\\000\\252\\001*\\000\\253\\0010\\000\\254\\0016\\000\\255\\0018\\000\\257\\001M\\000\\260\\001V\\000\\261\\001k\\000\\263\\001q\\000\\270\\001t\\000\\264\\001v\\000\\265\\001{\\000\\266\\001\\201\\000\\267\\001\\203\\000\\271\\001\\210\\000\\273\\001\\220\\000\\275\\001\\225\\000\\276\\001\\237\\000\\277\\001\\301\\000\\300\\001\\342\\000\\302\\001\\347\\000\\304\\001\\364\\000\\305\\001\\371\\000\\310\\001\\374\\000\\306\\001\\375\\000\\307\\002\\001\\000\\311\\000\\205\\000\\000\\000\\216\\000\\016\\001"\\000\\026\\000\\216\\000\\230\\000\\011\\001v\\000\\015\\000\\216\\000\\217\\000\\013\\001\\234\\000K\\000\\227\\000\\200\\000\\003\\000'\\001\\322\\000\\275\\000\\200\\000\\004\\0002\\001\\307\\000\\276\\000\\200\\000\\005\\000=\\001\\274\\000\\277\\000\\200\\000\\006\\000\\330\\001!\\000\\300\\000\\301\\000\\007\\000\\351\\001\\020\\000\\222\\000\\223\\000\\010\\001M\\000\\254\\000\\232\\000\\240\\000\\011\\001V\\000\\243\\000\\220\\000\\221\\000\\012\\001\\375\\000\\004\\000\\302\\000\\230\\000\\003\\000\\000\\002\\003\\000\\206\\000\\207\\000\\000\\000\\000\\002\\003\\000\\263\\000\\264\\000\\001\\000\\002\\002\\001\\000\\303\\000\\200\\000\\002\\000\\245\\000\\000\\000\\271\\000\\012\\374\\000\\022\\007\\000\\247\\377\\001\\015\\000\\011\\007\\000\\251\\007\\000\\272\\007\\000\\247\\000\\007\\000\\247\\007\\000\\247\\007\\000\\247\\007\\000\\304\\007\\000\\254\\000\\001\\007\\000\\257\\027\\377\\000;\\000\\013\\007\\000\\251\\007\\000\\272\\007\\000\\247\\000\\007\\000\\247\\007\\000\\247\\007\\000\\247\\007\\000\\304\\007\\000\\254\\007\\000\\256\\007\\000\\253\\000\\001\\007\\000\\255\\016\\021\\377\\000L\\000\\013\\007\\000\\251\\007\\000\\272\\007\\000\\247\\007\\000\\247\\007\\000\\247\\007\\000\\247\\007\\000\\247\\007\\000\\304\\007\\000\\254\\007\\000\\256\\007\\000\\253\\000\\000\\377\\000\\004\\000\\013\\007\\000\\251\\007\\000\\272\\007\\000\\247\\000\\007\\000\\247\\007\\000\\247\\007\\000\\247\\007\\000\\304\\007\\000\\254\\007\\000\\256\\007\\000\\253\\000\\000\\377\\000\\024\\000\\003\\007\\000\\251\\007\\000\\272\\007\\000\\247\\000\\001\\007\\000\\257\\004\\000\\001\\000\\305\\000\\306\\000\\001\\000\\203\\000\\000\\0006\\000\\001\\000\\002\\000\\000\\000\\002+\\260\\000\\000\\000\\002\\000\\204\\000\\000\\000\\006\\000\\001\\000\\000\\000\\316\\000\\205\\000\\000\\000\\026\\000\\002\\000\\000\\000\\002\\000\\206\\000\\207\\000\\000\\000\\000\\000\\002\\000\\231\\000\\200\\000\\001\\000\\001\\000\\307\\000\\310\\000\\001\\000\\203\\000\\000\\000Y\\000\\003\\000\\002\\000\\000\\000\\024+\\266\\000\\012\\232\\000\\017\\273\\000qY+\\267\\000r\\266\\000sW\\261\\000\\000\\000\\003\\000\\204\\000\\000\\000\\016\\000\\003\\000\\000\\000\\323\\000\\007\\000\\324\\000\\023\\000\\326\\000\\205\\000\\000\\000\\026\\000\\002\\000\\000\\000\\024\\000\\206\\000\\207\\000\\000\\000\\000\\000\\024\\000\\231\\000\\200\\000\\001\\000\\245\\000\\000\\000\\003\\000\\001\\023\\000\\010\\000\\311\\000\\202\\000\\001\\000\\203\\000\\000\\000"\\000\\001\\000\\000\\000\\000\\000\\012\\023\\000u\\270\\000v\\263\\000\\035\\261\\000\\000\\000\\001\\000\\204\\000\\000\\000\\006\\000\\001\\000\\000\\000\\035\\000\\001\\000\\312\\000\\000\\000\\002\\000\\313
4	pl.umk.mat.zawodyweb.compiler.classes.LanguageCPP	1	CPP Compiler	\\312\\376\\272\\276\\000\\000\\0002\\001\\220\\012\\000v\\000\\313\\011\\000t\\000\\314\\007\\000\\315\\012\\000\\003\\000\\313\\011\\000t\\000\\316\\011\\000t\\000\\317\\007\\000\\320\\012\\000\\007\\000\\321\\012\\000\\007\\000\\322\\012\\000\\003\\000\\323\\012\\000\\007\\000\\324\\012\\000\\325\\000\\326\\012\\000\\327\\000\\330\\010\\000\\331\\012\\000\\325\\000\\332\\012\\000\\003\\000\\333\\010\\000\\334\\012\\000\\003\\000\\335\\010\\000\\336\\010\\000\\337\\007\\000\\340\\012\\000\\025\\000\\313\\010\\000\\341\\012\\000\\025\\000\\342\\012\\000\\343\\000\\344\\012\\000\\025\\000\\345\\010\\000\\346\\012\\000\\025\\000\\347\\011\\000t\\000\\350\\010\\000\\351\\010\\000\\352\\012\\000\\353\\000\\354\\007\\000\\355\\012\\000!\\000\\313\\007\\000\\356\\012\\000#\\000\\357\\012\\000#\\000\\360\\007\\000\\361\\012\\000&\\000\\313\\012\\000&\\000\\362\\012\\000\\363\\000\\364\\012\\000\\343\\000\\365\\012\\000!\\000\\366\\007\\000\\367\\007\\000\\370\\012\\000\\371\\000\\372\\012\\000-\\000\\373\\012\\000,\\000\\374\\007\\000\\375\\007\\000\\376\\012\\000\\371\\000\\377\\012\\0002\\001\\000\\012\\0001\\001\\001\\012\\000\\343\\001\\002\\012\\0001\\001\\003\\012\\0001\\001\\004\\010\\001\\005\\012\\000\\025\\001\\006\\010\\001\\007\\012\\000\\353\\001\\010\\012\\000\\371\\001\\011\\007\\001\\012\\012\\000\\371\\001\\013\\012\\000!\\001\\014\\012\\000\\371\\001\\015\\010\\001\\016\\010\\001\\017\\012\\000\\007\\001\\020\\012\\000,\\001\\021\\012\\000\\007\\001\\022\\007\\001\\023\\012\\000G\\001\\024\\012\\000\\003\\001\\025\\010\\001\\026\\012\\000\\003\\001\\027\\012\\000\\003\\001\\030\\012\\000\\025\\001\\031\\010\\001\\032\\010\\001\\033\\010\\001\\034\\012\\000\\003\\001\\035\\010\\001\\036\\010\\001\\037\\012\\001 \\000\\332\\010\\001!\\010\\001"\\010\\001#\\010\\001$\\010\\001%\\010\\001&\\011\\000p\\001'\\010\\001(\\010\\001)\\010\\001*\\007\\001+\\012\\000_\\000\\321\\012\\001,\\001-\\012\\001,\\001\\004\\010\\001.\\010\\001/\\010\\0010\\010\\0011\\010\\0012\\012\\000#\\0013\\010\\0014\\010\\0015\\012\\000\\371\\0016\\010\\0017\\012\\0018\\0019\\010\\001:\\012\\000,\\001\\004\\007\\001;\\012\\000p\\000\\321\\012\\000p\\001<\\012\\000G\\001=\\007\\001>\\012\\000\\353\\001?\\007\\001@\\007\\001A\\001\\000\\006logger\\001\\000\\031Lorg/apache/log4j/Logger;\\001\\000\\012properties\\001\\000\\026Ljava/util/Properties;\\001\\000\\015compileResult\\001\\000\\001I\\001\\000\\013compileDesc\\001\\000\\022Ljava/lang/String;\\001\\000\\006<init>\\001\\000\\003()V\\001\\000\\004Code\\001\\000\\017LineNumberTable\\001\\000\\022LocalVariableTable\\001\\000\\004this\\001\\0003Lpl/umk/mat/zawodyweb/compiler/classes/LanguageCPP;\\001\\000\\015setProperties\\001\\000\\031(Ljava/util/Properties;)V\\001\\000\\007runTest\\001\\000e(Ljava/lang/String;Lpl/umk/mat/zawodyweb/checker/TestInput;)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\014outputStream\\001\\000\\030Ljava/io/BufferedWriter;\\001\\000\\002ex\\001\\000 Ljava/lang/InterruptedException;\\001\\000\\005timer\\001\\000+Lpl/umk/mat/zawodyweb/judge/InterruptTimer;\\001\\000\\001p\\001\\000\\023Ljava/lang/Process;\\001\\000\\004time\\001\\000\\001J\\001\\000\\013currentTime\\001\\000\\012outputText\\001\\000\\004line\\001\\000\\025Ljava/lang/Exception;\\001\\000\\004path\\001\\000\\005input\\001\\000(Lpl/umk/mat/zawodyweb/checker/TestInput;\\001\\000\\006output\\001\\000)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\013inputStream\\001\\000\\030Ljava/io/BufferedReader;\\001\\000\\007command\\001\\000\\020Ljava/util/List;\\001\\000\\026LocalVariableTypeTable\\001\\000$Ljava/util/List<Ljava/lang/String;>;\\001\\000\\015StackMapTable\\007\\000\\320\\007\\001B\\007\\001>\\007\\000\\315\\007\\001C\\007\\000\\355\\007\\001D\\007\\001\\012\\007\\000\\367\\007\\001\\023\\001\\000\\012precompile\\001\\000\\006([B)[B\\001\\000\\001i\\001\\000\\004code\\001\\000\\002[B\\001\\000\\003str\\001\\000\\016forbiddenCalls\\001\\000\\022strWithoutComments\\001\\000\\003len\\001\\000\\012regexp1_on\\007\\000\\263\\001\\000\\007compile\\001\\000\\026([B)Ljava/lang/String;\\001\\000\\010codefile\\001\\000\\013compileddir\\001\\000\\007codedir\\001\\000\\002is\\001\\000\\026Ljava/io/OutputStream;\\001\\000\\003err\\001\\000\\013compilefile\\007\\001E\\001\\000\\013postcompile\\001\\000&(Ljava/lang/String;)Ljava/lang/String;\\001\\000\\014closeProgram\\001\\000\\025(Ljava/lang/String;)V\\001\\000\\010<clinit>\\001\\000\\012SourceFile\\001\\000\\020LanguageCPP.java\\014\\000\\200\\000\\201\\014\\000|\\000}\\001\\000\\020java/lang/String\\014\\000~\\000\\177\\014\\000z\\000{\\001\\000'pl/umk/mat/zawodyweb/checker/TestOutput\\014\\000\\200\\000\\307\\014\\001F\\001G\\014\\001H\\001I\\014\\001J\\000\\307\\007\\001K\\014\\001L\\000\\201\\007\\001M\\014\\001N\\001O\\001\\000\\007os.name\\014\\001P\\000\\305\\014\\001Q\\001R\\001\\000\\017(?s).*windows.*\\014\\001S\\001T\\001\\000\\004bash\\001\\000\\002-c\\001\\000\\027java/lang/StringBuilder\\001\\000\\012ulimit -v \\014\\001U\\001V\\007\\001C\\014\\001W\\001X\\014\\001U\\001Y\\001\\000\\004 && \\014\\001Z\\001R\\014\\000x\\000y\\001\\000\\021OS without bash: \\001\\000\\034. Memory Limit check is off.\\007\\001[\\014\\001\\\\\\001]\\001\\000)pl/umk/mat/zawodyweb/judge/InterruptTimer\\001\\000\\030java/lang/ProcessBuilder\\014\\000\\200\\001^\\014\\001_\\001`\\001\\000\\016java/util/Date\\014\\001a\\001b\\007\\001c\\014\\001d\\001e\\014\\001f\\001X\\014\\001g\\001h\\001\\000\\026java/io/BufferedReader\\001\\000\\031java/io/InputStreamReader\\007\\001D\\014\\001i\\001j\\014\\000\\200\\001k\\014\\000\\200\\001l\\001\\000\\026java/io/BufferedWriter\\001\\000\\032java/io/OutputStreamWriter\\014\\001m\\001n\\014\\000\\200\\001o\\014\\000\\200\\001p\\014\\001q\\001R\\014\\001r\\000\\307\\014\\001s\\000\\201\\001\\000\\032Waiting for program after \\014\\001U\\001t\\001\\000\\003ms.\\014\\001u\\001]\\014\\001v\\001X\\001\\000\\036java/lang/InterruptedException\\014\\001w\\000\\201\\014\\001x\\000\\201\\014\\001y\\001X\\001\\000+Abnormal Program termination.\\012Exit status: \\001\\000\\001\\012\\014\\001z\\001G\\014\\001{\\001R\\014\\001|\\000\\307\\001\\000\\023java/lang/Exception\\014\\001}\\001R\\014\\000\\200\\001~\\001\\0062__asm__ __asm asm access acct alarm brk chdir chown chroot clearerr clearerr_unlocked closeconfstr crypt ctermid daemon dup2 dup encrypt endusershell euidaccess execl execle execlpexecv execve execvp _exit fchdir fchown fcloseall fclose fdatasync fdopen feof feof_unlockedferror ferror_unlocked fexecve fflush fflush_unlocked fgetc fgetc_unlocked fgetpos64 fgetposfgets fgets_unlocked fileno fileno_unlocked flockfile fmemopen fopen64 fopen fopencookie forkfpathconf fprintf fputc fputc_unlocked fputs fputs_unlocked fread fread_unlocked freopen64freopen fscanf fseek fseeko64 fseeko fsetpos64 fsetpos ftell ftello64 ftello ftruncate64ftruncate ftrylockfile funlockfile fwrite fwrite_unlocked getc getc_unlockedget_current_dir_name getcwd __getdelim getdelim getdomainname getegid geteuid getgid getgroupsgethostid gethostname getline getlogin getlogin_r getpagesize getpass __getpgid getpgidgetpgrp getpid getppid getsid getuid getusershell getw getwd group_member isatty lchown linklockf64 lockf lseek nice __off64t open open_memstream pathconf pause pclose pipe popen pread64pread profil pthread_atfork pthread_ putc putc_unlocked putw pwrite64 pwrite read readlinkremove rename revoke rewind rmdir sbrk setbuf setbuffer setdomainname setegid seteuid setgidsethostid sethostname setlinebuf setlogin setpgid setpgrp setregid setreuid setsid setuidsetusershell setvbuf signal sleep swab symlink sync sysconf tcgetpgrp tcsetpgrp tempnamtmpfile64 tmpfile tmpnam tmpnam_r truncate64 truncate ttyname ttyname_r ttyslot ualarm ungetcunlink usleep vfork vfprintf vfscanf vhangup write system\\014\\001\\177\\001X\\014\\001\\200\\001\\201\\014\\001U\\001\\202\\001\\000\\011(?s).*\\\\W(\\001\\000\\001 \\001\\000\\001|\\014\\001\\203\\001\\204\\001\\000\\005)\\\\W.*\\001\\000\\021COMPILED_FILENAME\\007\\001\\205\\001\\000\\015CODE_FILENAME\\001\\000\\014COMPILED_DIR\\001\\000\\010CODE_DIR\\001\\000\\006\\\\.cpp$\\001\\000\\000\\001\\000\\006\\\\.exe$\\014\\001\\206\\000\\177\\001\\000\\001$\\001\\000\\004.cpp\\001\\000\\004.exe\\001\\000\\030java/io/FileOutputStream\\007\\001E\\014\\001r\\001~\\001\\000\\003g++\\001\\000\\003-O2\\001\\000\\007-static\\001\\000\\002-o\\001\\000\\003-lm\\014\\000\\200\\001\\207\\001\\000\\015No g++ found.\\001\\000\\014No g++ found\\014\\001\\210\\001j\\001\\000\\017COMPILE_TIMEOUT\\007\\001\\211\\014\\001\\212\\001\\213\\001\\000\\003^.*\\001\\000\\014java/io/File\\014\\001\\214\\001I\\014\\001\\215\\000\\201\\001\\0001pl/umk/mat/zawodyweb/compiler/classes/LanguageCPP\\014\\001\\216\\001\\217\\001\\000\\020java/lang/Object\\001\\000/pl/umk/mat/zawodyweb/compiler/CompilerInterface\\001\\000\\016java/util/List\\001\\000&pl/umk/mat/zawodyweb/checker/TestInput\\001\\000\\021java/lang/Process\\001\\000\\024java/io/OutputStream\\001\\000\\011setResult\\001\\000\\004(I)V\\001\\000\\007isEmpty\\001\\000\\003()Z\\001\\000\\015setResultDesc\\001\\000\\020java/lang/System\\001\\000\\002gc\\001\\000\\020java/util/Arrays\\001\\000\\006asList\\001\\000%([Ljava/lang/Object;)Ljava/util/List;\\001\\000\\013getProperty\\001\\000\\013toLowerCase\\001\\000\\024()Ljava/lang/String;\\001\\000\\007matches\\001\\000\\025(Ljava/lang/String;)Z\\001\\000\\006append\\001\\000-(Ljava/lang/String;)Ljava/lang/StringBuilder;\\001\\000\\016getMemoryLimit\\001\\000\\003()I\\001\\000\\034(I)Ljava/lang/StringBuilder;\\001\\000\\010toString\\001\\000\\027org/apache/log4j/Logger\\001\\000\\005error\\001\\000\\025(Ljava/lang/Object;)V\\001\\000\\023(Ljava/util/List;)V\\001\\000\\005start\\001\\000\\025()Ljava/lang/Process;\\001\\000\\007getTime\\001\\000\\003()J\\001\\000\\020java/lang/Thread\\001\\000\\015currentThread\\001\\000\\024()Ljava/lang/Thread;\\001\\000\\014getTimeLimit\\001\\000\\010schedule\\001\\000\\026(Ljava/lang/Thread;J)V\\001\\000\\016getInputStream\\001\\000\\027()Ljava/io/InputStream;\\001\\000\\030(Ljava/io/InputStream;)V\\001\\000\\023(Ljava/io/Reader;)V\\001\\000\\017getOutputStream\\001\\000\\030()Ljava/io/OutputStream;\\001\\000\\031(Ljava/io/OutputStream;)V\\001\\000\\023(Ljava/io/Writer;)V\\001\\000\\007getText\\001\\000\\005write\\001\\000\\005close\\001\\000\\034(J)Ljava/lang/StringBuilder;\\001\\000\\005debug\\001\\000\\007waitFor\\001\\000\\007destroy\\001\\000\\006cancel\\001\\000\\011exitValue\\001\\000\\012setRuntime\\001\\000\\010readLine\\001\\000\\007setText\\001\\000\\012getMessage\\001\\000\\005([B)V\\001\\000\\006length\\001\\000\\006charAt\\001\\000\\004(I)C\\001\\000\\034(C)Ljava/lang/StringBuilder;\\001\\000\\012replaceAll\\001\\0008(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;\\001\\000\\024java/util/Properties\\001\\000\\011separator\\001\\000\\026([Ljava/lang/String;)V\\001\\000\\016getErrorStream\\001\\000\\021java/lang/Integer\\001\\000\\010parseInt\\001\\000\\025(Ljava/lang/String;)I\\001\\000\\006delete\\001\\000\\017printStackTrace\\001\\000\\011getLogger\\001\\000,(Ljava/lang/Class;)Lorg/apache/log4j/Logger;\\000!\\000t\\000v\\000\\001\\000w\\000\\004\\000\\031\\000x\\000y\\000\\000\\000\\000\\000z\\000{\\000\\000\\000\\000\\000|\\000}\\000\\000\\000\\000\\000~\\000\\177\\000\\000\\000\\010\\000\\001\\000\\200\\000\\201\\000\\001\\000\\202\\000\\000\\000G\\000\\003\\000\\001\\000\\000\\000\\025*\\267\\000\\001*\\002\\265\\000\\002*\\273\\000\\003Y\\267\\000\\004\\265\\000\\005\\261\\000\\000\\000\\002\\000\\203\\000\\000\\000\\016\\000\\003\\000\\000\\000\\033\\000\\004\\000\\037\\000\\011\\000 \\000\\204\\000\\000\\000\\014\\000\\001\\000\\000\\000\\025\\000\\205\\000\\206\\000\\000\\000\\001\\000\\207\\000\\210\\000\\001\\000\\202\\000\\000\\000>\\000\\002\\000\\002\\000\\000\\000\\006*+\\265\\000\\006\\261\\000\\000\\000\\002\\000\\203\\000\\000\\000\\012\\000\\002\\000\\000\\000$\\000\\005\\000%\\000\\204\\000\\000\\000\\026\\000\\002\\000\\000\\000\\006\\000\\205\\000\\206\\000\\000\\000\\000\\000\\006\\000z\\000{\\000\\001\\000\\001\\000\\211\\000\\212\\000\\001\\000\\202\\000\\000\\003\\370\\000\\006\\000\\016\\000\\000\\001\\351\\273\\000\\007Y\\001\\267\\000\\010N*\\264\\000\\002\\002\\237\\000\\037-*\\264\\000\\002\\266\\000\\011*\\264\\000\\005\\266\\000\\012\\232\\000\\013-*\\264\\000\\005\\266\\000\\013-\\260\\270\\000\\014\\004\\275\\000\\003Y\\003+S\\270\\000\\015:\\005\\022\\016\\270\\000\\017\\266\\000\\020\\022\\021\\266\\000\\022\\232\\000;\\006\\275\\000\\003Y\\003\\022\\023SY\\004\\022\\024SY\\005\\273\\000\\025Y\\267\\000\\026\\022\\027\\266\\000\\030,\\266\\000\\031\\266\\000\\032\\022\\033\\266\\000\\030+\\266\\000\\030\\266\\000\\034S\\270\\000\\015:\\005\\247\\000%\\262\\000\\035\\273\\000\\025Y\\267\\000\\026\\022\\036\\266\\000\\030\\022\\016\\270\\000\\017\\266\\000\\030\\022\\037\\266\\000\\030\\266\\000\\034\\266\\000 \\273\\000!Y\\267\\000":\\006\\273\\000#Y\\031\\005\\267\\000$\\266\\000%:\\007\\273\\000&Y\\267\\000'\\266\\000(7\\010\\031\\006\\270\\000),\\266\\000*\\205\\266\\000+\\273\\000,Y\\273\\000-Y\\031\\007\\266\\000.\\267\\000/\\267\\0000:\\004\\273\\0001Y\\273\\0002Y\\031\\007\\266\\0003\\267\\0004\\267\\0005:\\012\\031\\012,\\266\\0006\\266\\0007\\031\\012\\266\\0008\\262\\000\\035\\273\\000\\025Y\\267\\000\\026\\0229\\266\\000\\030\\273\\000&Y\\267\\000'\\266\\000(\\026\\010e\\266\\000:\\022;\\266\\000\\030\\266\\000\\034\\266\\000<\\031\\007\\266\\000=W\\247\\000\\021:\\012\\031\\007\\266\\000?-\\005\\266\\000\\011-\\260\\273\\000&Y\\267\\000'\\266\\000(7\\012\\031\\006\\266\\000@\\031\\007\\266\\000A\\231\\000*-\\007\\266\\000\\011-\\273\\000\\025Y\\267\\000\\026\\022B\\266\\000\\030\\031\\007\\266\\000A\\266\\000\\032\\022C\\266\\000\\030\\266\\000\\034\\266\\000\\013-\\260-\\026\\012\\026\\010e\\210\\266\\000D\\273\\000\\003Y\\267\\000\\004:\\014\\031\\004\\266\\000EY:\\015\\306\\000!\\273\\000\\025Y\\267\\000\\026\\031\\014\\266\\000\\030\\031\\015\\266\\000\\030\\022C\\266\\000\\030\\266\\000\\034:\\014\\247\\377\\332-\\031\\014\\266\\000F\\031\\007\\266\\000?\\247\\000\\020:\\006\\262\\000\\035\\031\\006\\266\\000H\\266\\000 -\\260\\000\\004\\000\\327\\001?\\001B\\000>\\000\\247\\001O\\001\\332\\000G\\001P\\001\\217\\001\\332\\000G\\001\\220\\001\\327\\001\\332\\000G\\000\\004\\000\\203\\000\\000\\000\\252\\000*\\000\\000\\000)\\000\\011\\000*\\000\\021\\000+\\000\\031\\000,\\000#\\000-\\000+\\000/\\000-\\0002\\0000\\0003\\000=\\0004\\000M\\0005\\000\\205\\0007\\000\\247\\000:\\000\\260\\000;\\000\\276\\000<\\000\\312\\000=\\000\\327\\000?\\000\\354\\000@\\001\\001\\000A\\001\\012\\000C\\001\\017\\000D\\0019\\000E\\001?\\000J\\001B\\000F\\001D\\000G\\001I\\000H\\001N\\000I\\001P\\000K\\001\\\\\\000L\\001a\\000N\\001i\\000O\\001n\\000P\\001\\216\\000Q\\001\\220\\000S\\001\\232\\000T\\001\\243\\000V\\001\\256\\000W\\001\\314\\000Y\\001\\322\\000Z\\001\\327\\000]\\001\\332\\000[\\001\\334\\000\\\\\\001\\347\\000^\\000\\204\\000\\000\\000\\230\\000\\017\\001\\001\\000>\\000\\213\\000\\214\\000\\012\\001D\\000\\014\\000\\215\\000\\216\\000\\012\\000\\260\\001'\\000\\217\\000\\220\\000\\006\\000\\276\\001\\031\\000\\221\\000\\222\\000\\007\\000\\312\\001\\015\\000\\223\\000\\224\\000\\010\\001\\\\\\000{\\000\\225\\000\\224\\000\\012\\001\\243\\0004\\000\\226\\000\\177\\000\\014\\001\\253\\000,\\000\\227\\000\\177\\000\\015\\001\\334\\000\\013\\000\\215\\000\\230\\000\\006\\000\\000\\001\\351\\000\\205\\000\\206\\000\\000\\000\\000\\001\\351\\000\\231\\000\\177\\000\\001\\000\\000\\001\\351\\000\\232\\000\\233\\000\\002\\000\\011\\001\\340\\000\\234\\000\\235\\000\\003\\000\\354\\000\\375\\000\\236\\000\\237\\000\\004\\000=\\001\\254\\000\\240\\000\\241\\000\\005\\000\\242\\000\\000\\000\\014\\000\\001\\000=\\001\\254\\000\\240\\000\\243\\000\\005\\000\\244\\000\\000\\000}\\000\\013\\374\\000+\\007\\000\\245\\001\\375\\000W\\000\\007\\000\\246!\\377\\000\\232\\000\\011\\007\\000\\247\\007\\000\\250\\007\\000\\251\\007\\000\\245\\000\\007\\000\\246\\007\\000\\252\\007\\000\\253\\004\\000\\001\\007\\000\\254\\377\\000\\015\\000\\011\\007\\000\\247\\007\\000\\250\\007\\000\\251\\007\\000\\245\\007\\000\\255\\007\\000\\246\\007\\000\\252\\007\\000\\253\\004\\000\\000\\374\\000?\\004\\374\\000\\022\\007\\000\\250\\374\\000(\\007\\000\\250\\377\\000\\015\\000\\006\\007\\000\\247\\007\\000\\250\\007\\000\\251\\007\\000\\245\\000\\007\\000\\246\\000\\001\\007\\000\\256\\014\\000\\001\\000\\257\\000\\260\\000\\001\\000\\202\\000\\000\\001\\313\\000\\004\\000\\007\\000\\000\\000\\337\\273\\000\\003Y+\\267\\000IM\\022JN\\273\\000\\003Y\\267\\000\\004:\\004,\\266\\000K\\004d6\\005\\0036\\006\\025\\006\\025\\005\\242\\000\\206,\\025\\006\\266\\000L\\020/\\240\\000/,\\025\\006\\266\\000L\\020*\\240\\000$,\\025\\006\\266\\000L\\020*\\240\\000\\020,\\025\\006\\004`\\266\\000L\\020/\\237\\000\\011\\204\\006\\001\\247\\377\\345\\204\\006\\002,\\025\\006\\266\\000L\\020/\\240\\000$,\\025\\006\\004`\\266\\000L\\020/\\240\\000\\027,\\025\\006\\266\\000L\\020\\012\\237\\000\\011\\204\\006\\001\\247\\377\\362\\204\\006\\001\\273\\000\\025Y\\267\\000\\026\\031\\004\\266\\000\\030,\\025\\006\\266\\000L\\266\\000M\\266\\000\\034:\\004\\204\\006\\001\\247\\377y\\031\\004M\\273\\000\\025Y\\267\\000\\026\\022N\\266\\000\\030-\\022O\\022P\\266\\000Q\\266\\000\\030\\022R\\266\\000\\030\\266\\000\\034:\\006,\\031\\006\\266\\000\\022\\231\\000\\011*\\020\\006\\265\\000\\002+\\260\\000\\000\\000\\003\\000\\203\\000\\000\\000R\\000\\024\\000\\000\\000c\\000\\011\\000d\\000\\014\\000v\\000\\025\\000w\\000\\035\\000x\\000'\\000y\\000=\\000z\\000U\\000{\\000[\\000}\\000^\\000\\177\\000v\\000\\200\\000\\201\\000\\201\\000\\207\\000\\203\\000\\212\\000\\205\\000\\244\\000x\\000\\252\\000\\207\\000\\255\\000\\210\\000\\316\\000\\211\\000\\327\\000\\212\\000\\335\\000\\214\\000\\204\\000\\000\\000R\\000\\010\\000 \\000\\212\\000\\261\\000}\\000\\006\\000\\000\\000\\337\\000\\205\\000\\206\\000\\000\\000\\000\\000\\337\\000\\262\\000\\263\\000\\001\\000\\011\\000\\326\\000\\264\\000\\177\\000\\002\\000\\014\\000\\323\\000\\265\\000\\177\\000\\003\\000\\025\\000\\312\\000\\266\\000\\177\\000\\004\\000\\035\\000\\302\\000\\267\\000}\\000\\005\\000\\316\\000\\021\\000\\270\\000\\177\\000\\006\\000\\244\\000\\000\\000*\\000\\012\\377\\000 \\000\\007\\007\\000\\247\\007\\000\\271\\007\\000\\250\\007\\000\\250\\007\\000\\250\\001\\001\\000\\000\\034\\027\\005\\002\\027\\020\\002\\372\\000\\037\\374\\0002\\007\\000\\250\\000\\001\\000\\272\\000\\273\\000\\001\\000\\202\\000\\000\\004N\\000\\006\\000\\014\\000\\000\\002\\003\\001M*\\264\\000\\002\\002\\237\\000\\013\\273\\000\\003Y\\267\\000\\004\\260*\\264\\000\\006\\022S\\266\\000TM*\\264\\000\\006\\022U\\266\\000T:\\004*\\264\\000\\006\\022V\\266\\000T:\\005*\\264\\000\\006\\022W\\266\\000T:\\006\\031\\004\\022X\\022Y\\266\\000Q:\\004,\\022Z\\022Y\\266\\000QM\\031\\006\\273\\000\\025Y\\267\\000\\026\\262\\000[\\266\\000\\030\\022\\\\\\266\\000\\030\\266\\000\\034\\022Y\\266\\000Q:\\006\\031\\005\\273\\000\\025Y\\267\\000\\026\\262\\000[\\266\\000\\030\\022\\\\\\266\\000\\030\\266\\000\\034\\022Y\\266\\000Q:\\005\\273\\000\\025Y\\267\\000\\026\\031\\006\\266\\000\\030\\262\\000[\\266\\000\\030\\031\\004\\266\\000\\030\\022]\\266\\000\\030\\266\\000\\034:\\004\\273\\000\\025Y\\267\\000\\026\\031\\005\\266\\000\\030\\262\\000[\\266\\000\\030,\\266\\000\\030\\022^\\266\\000\\030\\266\\000\\034M\\273\\000_Y\\031\\004\\267\\000`:\\007\\031\\007+\\266\\000a\\031\\007\\266\\000b\\270\\000\\014\\001:\\010\\273\\000#Y\\020\\007\\275\\000\\003Y\\003\\022cSY\\004\\022dSY\\005\\022eSY\\006\\022fSY\\007,SY\\010\\031\\004SY\\020\\006\\022gS\\267\\000h\\266\\000%:\\010\\247\\000\\033:\\011\\262\\000\\035\\022i\\266\\000 *\\020\\010\\265\\000\\002*\\022j\\265\\000\\005,\\260\\273\\000,Y\\273\\000-Y\\031\\010\\266\\000k\\267\\000/\\267\\0000:\\011\\273\\000!Y\\267\\000":\\012\\031\\012\\270\\000)*\\264\\000\\006\\022l\\266\\000T\\270\\000m\\205\\266\\000+\\031\\010\\266\\000=W\\247\\000\\022:\\013\\031\\010\\266\\000?*\\020\\007\\265\\000\\002,\\260\\031\\012\\266\\000@\\031\\010\\266\\000A\\231\\000Z*\\006\\265\\000\\002\\031\\011\\266\\000EYN\\306\\000F-\\273\\000\\025Y\\267\\000\\026\\022n\\266\\000\\030\\031\\004\\266\\000\\030\\266\\000\\034*\\264\\000\\006\\022U\\266\\000T\\266\\000QN*\\273\\000\\025Y\\267\\000\\026*\\264\\000\\005\\266\\000\\030-\\266\\000\\030\\022C\\266\\000\\030\\266\\000\\034\\265\\000\\005\\247\\377\\266\\031\\011\\266\\000o\\273\\000pY\\031\\004\\267\\000q\\266\\000rW\\031\\010\\266\\000?\\247\\000\\010N-\\266\\000s,\\260\\000\\005\\000\\351\\001\\035\\001 \\000G\\001k\\001q\\001t\\000>\\000\\022\\0017\\001\\374\\000G\\0018\\001\\202\\001\\374\\000G\\001\\203\\001\\371\\001\\374\\000G\\000\\003\\000\\203\\000\\000\\000\\276\\000/\\000\\000\\000\\221\\000\\002\\000\\222\\000\\012\\000\\223\\000\\022\\000\\227\\000\\034\\000\\230\\000'\\000\\231\\0002\\000\\232\\000=\\000\\233\\000H\\000\\234\\000Q\\000\\235\\000o\\000\\236\\000\\215\\000\\237\\000\\256\\000\\240\\000\\315\\000\\241\\000\\330\\000\\242\\000\\336\\000\\243\\000\\343\\000\\244\\000\\346\\000\\245\\000\\351\\000\\247\\001\\035\\000\\255\\001 \\000\\250\\001"\\000\\251\\001*\\000\\252\\0010\\000\\253\\0016\\000\\254\\0018\\000\\256\\001M\\000\\257\\001V\\000\\260\\001k\\000\\262\\001q\\000\\267\\001t\\000\\263\\001v\\000\\264\\001{\\000\\265\\001\\201\\000\\266\\001\\203\\000\\270\\001\\210\\000\\272\\001\\220\\000\\274\\001\\225\\000\\275\\001\\237\\000\\276\\001\\301\\000\\277\\001\\342\\000\\301\\001\\347\\000\\303\\001\\364\\000\\304\\001\\371\\000\\307\\001\\374\\000\\305\\001\\375\\000\\306\\002\\001\\000\\310\\000\\204\\000\\000\\000\\216\\000\\016\\001"\\000\\026\\000\\215\\000\\230\\000\\011\\001v\\000\\015\\000\\215\\000\\216\\000\\013\\001\\234\\000K\\000\\227\\000\\177\\000\\003\\000'\\001\\322\\000\\274\\000\\177\\000\\004\\0002\\001\\307\\000\\275\\000\\177\\000\\005\\000=\\001\\274\\000\\276\\000\\177\\000\\006\\000\\330\\001!\\000\\277\\000\\300\\000\\007\\000\\351\\001\\020\\000\\221\\000\\222\\000\\010\\001M\\000\\254\\000\\232\\000\\237\\000\\011\\001V\\000\\243\\000\\217\\000\\220\\000\\012\\001\\375\\000\\004\\000\\301\\000\\230\\000\\003\\000\\000\\002\\003\\000\\205\\000\\206\\000\\000\\000\\000\\002\\003\\000\\262\\000\\263\\000\\001\\000\\002\\002\\001\\000\\302\\000\\177\\000\\002\\000\\244\\000\\000\\000\\271\\000\\012\\374\\000\\022\\007\\000\\250\\377\\001\\015\\000\\011\\007\\000\\247\\007\\000\\271\\007\\000\\250\\000\\007\\000\\250\\007\\000\\250\\007\\000\\250\\007\\000\\303\\007\\000\\253\\000\\001\\007\\000\\256\\027\\377\\000;\\000\\013\\007\\000\\247\\007\\000\\271\\007\\000\\250\\000\\007\\000\\250\\007\\000\\250\\007\\000\\250\\007\\000\\303\\007\\000\\253\\007\\000\\255\\007\\000\\252\\000\\001\\007\\000\\254\\016\\021\\377\\000L\\000\\013\\007\\000\\247\\007\\000\\271\\007\\000\\250\\007\\000\\250\\007\\000\\250\\007\\000\\250\\007\\000\\250\\007\\000\\303\\007\\000\\253\\007\\000\\255\\007\\000\\252\\000\\000\\377\\000\\004\\000\\013\\007\\000\\247\\007\\000\\271\\007\\000\\250\\000\\007\\000\\250\\007\\000\\250\\007\\000\\250\\007\\000\\303\\007\\000\\253\\007\\000\\255\\007\\000\\252\\000\\000\\377\\000\\024\\000\\003\\007\\000\\247\\007\\000\\271\\007\\000\\250\\000\\001\\007\\000\\256\\004\\000\\001\\000\\304\\000\\305\\000\\001\\000\\202\\000\\000\\0006\\000\\001\\000\\002\\000\\000\\000\\002+\\260\\000\\000\\000\\002\\000\\203\\000\\000\\000\\006\\000\\001\\000\\000\\000\\315\\000\\204\\000\\000\\000\\026\\000\\002\\000\\000\\000\\002\\000\\205\\000\\206\\000\\000\\000\\000\\000\\002\\000\\231\\000\\177\\000\\001\\000\\001\\000\\306\\000\\307\\000\\001\\000\\202\\000\\000\\000Y\\000\\003\\000\\002\\000\\000\\000\\024+\\266\\000\\012\\232\\000\\017\\273\\000pY+\\267\\000q\\266\\000rW\\261\\000\\000\\000\\003\\000\\203\\000\\000\\000\\016\\000\\003\\000\\000\\000\\322\\000\\007\\000\\323\\000\\023\\000\\325\\000\\204\\000\\000\\000\\026\\000\\002\\000\\000\\000\\024\\000\\205\\000\\206\\000\\000\\000\\000\\000\\024\\000\\231\\000\\177\\000\\001\\000\\244\\000\\000\\000\\003\\000\\001\\023\\000\\010\\000\\310\\000\\201\\000\\001\\000\\202\\000\\000\\000"\\000\\001\\000\\000\\000\\000\\000\\012\\023\\000t\\270\\000u\\263\\000\\035\\261\\000\\000\\000\\001\\000\\203\\000\\000\\000\\006\\000\\001\\000\\000\\000\\035\\000\\001\\000\\311\\000\\000\\000\\002\\000\\312
5	pl.umk.mat.zawodyweb.compiler.classes.LanguageJAVA	1	JAVA Compiler	\\312\\376\\272\\276\\000\\000\\0002\\001t\\012\\000i\\000\\271\\011\\000g\\000\\272\\007\\000\\273\\012\\000\\003\\000\\271\\011\\000g\\000\\274\\011\\000g\\000\\275\\007\\000\\276\\012\\000\\007\\000\\277\\010\\000\\300\\012\\000\\301\\000\\302\\012\\000\\007\\000\\303\\012\\000\\003\\000\\304\\012\\000\\007\\000\\305\\012\\000\\306\\000\\307\\007\\000\\310\\010\\000\\311\\007\\000\\312\\012\\000\\021\\000\\271\\010\\000\\313\\012\\000\\021\\000\\314\\012\\000\\315\\000\\316\\012\\000\\021\\000\\317\\010\\000\\320\\012\\000\\021\\000\\321\\010\\000\\322\\010\\000\\323\\012\\000\\324\\000\\325\\012\\000\\017\\000\\326\\010\\000\\327\\012\\000\\017\\000\\330\\010\\000\\331\\010\\000\\332\\011\\000c\\000\\333\\012\\000\\003\\000\\334\\012\\000\\003\\000\\335\\010\\000\\336\\007\\000\\337\\012\\000%\\000\\271\\007\\000\\340\\012\\000'\\000\\341\\012\\000'\\000\\342\\007\\000\\343\\012\\000*\\000\\271\\012\\000*\\000\\344\\012\\000\\345\\000\\346\\012\\000\\315\\000\\347\\012\\000%\\000\\350\\007\\000\\351\\007\\000\\352\\012\\000\\353\\000\\354\\012\\0001\\000\\355\\012\\0000\\000\\356\\007\\000\\357\\007\\000\\360\\012\\000\\353\\000\\361\\012\\0006\\000\\362\\012\\0005\\000\\363\\012\\000\\315\\000\\364\\012\\0005\\000\\365\\012\\0005\\000\\366\\011\\000g\\000\\367\\010\\000\\370\\012\\000\\021\\000\\371\\010\\000\\372\\012\\000\\373\\000\\374\\012\\000\\353\\000\\375\\007\\000\\376\\012\\000C\\000\\377\\012\\000\\353\\001\\000\\012\\000%\\001\\001\\012\\000\\353\\001\\002\\010\\001\\003\\010\\001\\004\\012\\000\\007\\001\\005\\012\\0000\\001\\006\\012\\000\\007\\001\\007\\007\\001\\010\\012\\000M\\001\\011\\012\\000\\373\\001\\012\\010\\001\\013\\010\\001\\014\\010\\001\\015\\010\\001\\016\\012\\000\\003\\001\\017\\010\\001\\020\\010\\001\\021\\007\\001\\022\\012\\000W\\000\\277\\012\\001\\023\\001\\024\\012\\001\\023\\000\\366\\012\\001\\025\\001\\026\\007\\001\\027\\012\\000\\\\\\000\\271\\007\\001\\030\\012\\000\\003\\001\\031\\012\\000^\\001\\032\\013\\001\\033\\001\\034\\012\\000\\\\\\000\\321\\007\\001\\035\\012\\000c\\000\\277\\012\\000c\\001\\036\\010\\001\\037\\007\\001 \\012\\000\\373\\001!\\007\\001"\\007\\001#\\001\\000\\006logger\\001\\000\\031Lorg/apache/log4j/Logger;\\001\\000\\012properties\\001\\000\\026Ljava/util/Properties;\\001\\000\\015compileResult\\001\\000\\001I\\001\\000\\013compileDesc\\001\\000\\022Ljava/lang/String;\\001\\000\\006<init>\\001\\000\\003()V\\001\\000\\004Code\\001\\000\\017LineNumberTable\\001\\000\\022LocalVariableTable\\001\\000\\004this\\001\\0004Lpl/umk/mat/zawodyweb/compiler/classes/LanguageJAVA;\\001\\000\\015setProperties\\001\\000\\031(Ljava/util/Properties;)V\\001\\000\\007runTest\\001\\000e(Ljava/lang/String;Lpl/umk/mat/zawodyweb/checker/TestInput;)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\014outputStream\\001\\000\\030Ljava/io/BufferedWriter;\\001\\000\\002ex\\001\\000 Ljava/lang/InterruptedException;\\001\\000\\005timer\\001\\000+Lpl/umk/mat/zawodyweb/judge/InterruptTimer;\\001\\000\\001p\\001\\000\\023Ljava/lang/Process;\\001\\000\\004time\\001\\000\\001J\\001\\000\\013currentTime\\001\\000\\012outputText\\001\\000\\004line\\001\\000\\025Ljava/lang/Exception;\\001\\000\\004path\\001\\000\\005input\\001\\000(Lpl/umk/mat/zawodyweb/checker/TestInput;\\001\\000\\006output\\001\\000)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\010security\\001\\000\\013inputStream\\001\\000\\030Ljava/io/BufferedReader;\\001\\000\\007command\\001\\000\\022Ljava/util/Vector;\\001\\000\\026LocalVariableTypeTable\\001\\000&Ljava/util/Vector<Ljava/lang/String;>;\\001\\000\\015StackMapTable\\007\\000\\276\\007\\000\\273\\007\\000\\310\\007\\001 \\007\\001$\\007\\000\\337\\007\\001%\\007\\000\\376\\007\\000\\351\\007\\001\\010\\001\\000\\012precompile\\001\\000\\006([B)[B\\001\\000\\004code\\001\\000\\002[B\\001\\000\\007compile\\001\\000\\026([B)Ljava/lang/String;\\001\\000\\007codedir\\001\\000\\002is\\001\\000\\026Ljava/io/OutputStream;\\001\\000\\010compiler\\001\\000\\032Ljavax/tools/JavaCompiler;\\001\\000\\003err\\001\\000\\037Ljava/io/ByteArrayOutputStream;\\001\\000\\003out\\001\\000\\010codefile\\001\\000\\013postcompile\\001\\000&(Ljava/lang/String;)Ljava/lang/String;\\001\\000\\014closeProgram\\001\\000\\025(Ljava/lang/String;)V\\001\\000\\010<clinit>\\001\\000\\012SourceFile\\001\\000\\021LanguageJAVA.java\\014\\000s\\000t\\014\\000o\\000p\\001\\000\\020java/lang/String\\014\\000q\\000r\\014\\000m\\000n\\001\\000'pl/umk/mat/zawodyweb/checker/TestOutput\\014\\000s\\000\\265\\001\\000\\013JAVA_POLICY\\007\\001&\\014\\001'\\000\\263\\014\\001(\\001)\\014\\001*\\001+\\014\\001,\\000\\265\\007\\001-\\014\\001.\\000t\\001\\000\\020java/util/Vector\\001\\000\\004java\\001\\000\\027java/lang/StringBuilder\\001\\000\\004-Xmx\\014\\001/\\0010\\007\\001$\\014\\0011\\0012\\014\\001/\\0013\\001\\000\\001k\\014\\0014\\0015\\001\\000\\004-Xms\\001\\000\\004-Xss\\007\\0016\\014\\0017\\0018\\014\\000s\\0019\\001\\000\\027-Djava.security.manager\\014\\001:\\001;\\001\\000\\027-Djava.security.policy=\\001\\000\\003-cp\\014\\001<\\000r\\014\\001=\\001>\\014\\001?\\001@\\001\\000\\001.\\001\\000)pl/umk/mat/zawodyweb/judge/InterruptTimer\\001\\000\\030java/lang/ProcessBuilder\\014\\000s\\001A\\014\\001B\\001C\\001\\000\\016java/util/Date\\014\\001D\\001E\\007\\001F\\014\\001G\\001H\\014\\001I\\0012\\014\\001J\\001K\\001\\000\\026java/io/BufferedReader\\001\\000\\031java/io/InputStreamReader\\007\\001%\\014\\001L\\001M\\014\\000s\\001N\\014\\000s\\001O\\001\\000\\026java/io/BufferedWriter\\001\\000\\032java/io/OutputStreamWriter\\014\\001P\\001Q\\014\\000s\\001R\\014\\000s\\001S\\014\\001T\\0015\\014\\001U\\000\\265\\014\\001V\\000t\\014\\000k\\000l\\001\\000\\032Waiting for program after \\014\\001/\\001W\\001\\000\\003ms.\\007\\001X\\014\\001Y\\001Z\\014\\001[\\0012\\001\\000\\036java/lang/InterruptedException\\014\\001\\\\\\000t\\014\\001]\\000t\\014\\001^\\000t\\014\\001_\\0012\\001\\000+Abnormal Program termination.\\012Exit status: \\001\\000\\001\\012\\014\\001`\\001)\\014\\001a\\0015\\014\\001b\\000\\265\\001\\000\\023java/lang/Exception\\014\\001c\\0015\\014\\001d\\001Z\\001\\000\\015CODE_FILENAME\\001\\000\\010CODE_DIR\\001\\000\\007\\\\.java$\\001\\000\\000\\014\\001e\\001f\\001\\000\\001$\\001\\000\\005.java\\001\\000\\030java/io/FileOutputStream\\007\\001g\\014\\001U\\001h\\007\\001i\\014\\001j\\001k\\001\\000\\035java/io/ByteArrayOutputStream\\001\\000\\034java/io/ByteArrayInputStream\\014\\001l\\001m\\014\\000s\\001h\\007\\001n\\014\\001o\\001p\\001\\000\\014java/io/File\\014\\001q\\001+\\001\\000\\006.class\\001\\0002pl/umk/mat/zawodyweb/compiler/classes/LanguageJAVA\\014\\001r\\001s\\001\\000\\020java/lang/Object\\001\\000/pl/umk/mat/zawodyweb/compiler/CompilerInterface\\001\\000&pl/umk/mat/zawodyweb/checker/TestInput\\001\\000\\021java/lang/Process\\001\\000\\024java/util/Properties\\001\\000\\013getProperty\\001\\000\\011setResult\\001\\000\\004(I)V\\001\\000\\007isEmpty\\001\\000\\003()Z\\001\\000\\015setResultDesc\\001\\000\\020java/lang/System\\001\\000\\002gc\\001\\000\\006append\\001\\000-(Ljava/lang/String;)Ljava/lang/StringBuilder;\\001\\000\\016getMemoryLimit\\001\\000\\003()I\\001\\000\\034(I)Ljava/lang/StringBuilder;\\001\\000\\010toString\\001\\000\\024()Ljava/lang/String;\\001\\000\\020java/util/Arrays\\001\\000\\006asList\\001\\000%([Ljava/lang/Object;)Ljava/util/List;\\001\\000\\031(Ljava/util/Collection;)V\\001\\000\\003add\\001\\000\\025(Ljava/lang/Object;)Z\\001\\000\\011separator\\001\\000\\013lastIndexOf\\001\\000\\025(Ljava/lang/String;)I\\001\\000\\011substring\\001\\000\\026(II)Ljava/lang/String;\\001\\000\\023(Ljava/util/List;)V\\001\\000\\005start\\001\\000\\025()Ljava/lang/Process;\\001\\000\\007getTime\\001\\000\\003()J\\001\\000\\020java/lang/Thread\\001\\000\\015currentThread\\001\\000\\024()Ljava/lang/Thread;\\001\\000\\014getTimeLimit\\001\\000\\010schedule\\001\\000\\026(Ljava/lang/Thread;J)V\\001\\000\\016getInputStream\\001\\000\\027()Ljava/io/InputStream;\\001\\000\\030(Ljava/io/InputStream;)V\\001\\000\\023(Ljava/io/Reader;)V\\001\\000\\017getOutputStream\\001\\000\\030()Ljava/io/OutputStream;\\001\\000\\031(Ljava/io/OutputStream;)V\\001\\000\\023(Ljava/io/Writer;)V\\001\\000\\007getText\\001\\000\\005write\\001\\000\\005close\\001\\000\\034(J)Ljava/lang/StringBuilder;\\001\\000\\027org/apache/log4j/Logger\\001\\000\\005debug\\001\\000\\025(Ljava/lang/Object;)V\\001\\000\\007waitFor\\001\\000\\017printStackTrace\\001\\000\\007destroy\\001\\000\\006cancel\\001\\000\\011exitValue\\001\\000\\012setRuntime\\001\\000\\010readLine\\001\\000\\007setText\\001\\000\\012getMessage\\001\\000\\005error\\001\\000\\012replaceAll\\001\\0008(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;\\001\\000\\024java/io/OutputStream\\001\\000\\005([B)V\\001\\000\\030javax/tools/ToolProvider\\001\\000\\025getSystemJavaCompiler\\001\\000\\034()Ljavax/tools/JavaCompiler;\\001\\000\\010getBytes\\001\\000\\004()[B\\001\\000\\030javax/tools/JavaCompiler\\001\\000\\003run\\001\\000W(Ljava/io/InputStream;Ljava/io/OutputStream;Ljava/io/OutputStream;[Ljava/lang/String;)I\\001\\000\\006delete\\001\\000\\011getLogger\\001\\000,(Ljava/lang/Class;)Lorg/apache/log4j/Logger;\\000!\\000g\\000i\\000\\001\\000j\\000\\004\\000\\031\\000k\\000l\\000\\000\\000\\000\\000m\\000n\\000\\000\\000\\000\\000o\\000p\\000\\000\\000\\000\\000q\\000r\\000\\000\\000\\010\\000\\001\\000s\\000t\\000\\001\\000u\\000\\000\\000G\\000\\003\\000\\001\\000\\000\\000\\025*\\267\\000\\001*\\002\\265\\000\\002*\\273\\000\\003Y\\267\\000\\004\\265\\000\\005\\261\\000\\000\\000\\002\\000v\\000\\000\\000\\016\\000\\003\\000\\000\\000\\035\\000\\004\\000!\\000\\011\\000"\\000w\\000\\000\\000\\014\\000\\001\\000\\000\\000\\025\\000x\\000y\\000\\000\\000\\001\\000z\\000{\\000\\001\\000u\\000\\000\\000>\\000\\002\\000\\002\\000\\000\\000\\006*+\\265\\000\\006\\261\\000\\000\\000\\002\\000v\\000\\000\\000\\012\\000\\002\\000\\000\\000&\\000\\005\\000'\\000w\\000\\000\\000\\026\\000\\002\\000\\000\\000\\006\\000x\\000y\\000\\000\\000\\000\\000\\006\\000m\\000n\\000\\001\\000\\001\\000|\\000}\\000\\001\\000u\\000\\000\\004}\\000\\007\\000\\017\\000\\000\\002I\\273\\000\\007Y\\001\\267\\000\\010N*\\264\\000\\006\\022\\011\\266\\000\\012:\\004*\\264\\000\\002\\002\\237\\000\\037-*\\264\\000\\002\\266\\000\\013*\\264\\000\\005\\266\\000\\014\\232\\000\\013-*\\264\\000\\005\\266\\000\\015-\\260\\270\\000\\016\\273\\000\\017Y\\007\\275\\000\\003Y\\003\\022\\020SY\\004\\273\\000\\021Y\\267\\000\\022\\022\\023\\266\\000\\024,\\266\\000\\025\\266\\000\\026\\022\\027\\266\\000\\024\\266\\000\\030SY\\005\\273\\000\\021Y\\267\\000\\022\\022\\031\\266\\000\\024,\\266\\000\\025\\266\\000\\026\\022\\027\\266\\000\\024\\266\\000\\030SY\\006\\273\\000\\021Y\\267\\000\\022\\022\\032\\266\\000\\024,\\266\\000\\025\\266\\000\\026\\022\\027\\266\\000\\024\\266\\000\\030S\\270\\000\\033\\267\\000\\034:\\006\\031\\004\\266\\000\\014\\232\\000%\\031\\006\\022\\035\\266\\000\\036W\\031\\006\\273\\000\\021Y\\267\\000\\022\\022\\037\\266\\000\\024\\031\\004\\266\\000\\024\\266\\000\\030\\266\\000\\036W\\031\\006\\022 \\266\\000\\036W\\031\\006+\\003+\\262\\000!\\266\\000"\\266\\000#\\266\\000\\036W\\031\\006++\\262\\000!\\266\\000"\\004`+\\022$\\266\\000"\\266\\000#\\266\\000\\036W\\273\\000%Y\\267\\000&:\\007\\273\\000'Y\\031\\006\\267\\000(\\266\\000):\\010\\273\\000*Y\\267\\000+\\266\\000,7\\011\\031\\007\\270\\000-,\\266\\000.\\205\\266\\000/\\273\\0000Y\\273\\0001Y\\031\\010\\266\\0002\\267\\0003\\267\\0004:\\005\\273\\0005Y\\273\\0006Y\\031\\010\\266\\0007\\267\\0008\\267\\0009:\\013\\031\\013,\\266\\000:\\266\\000;\\031\\013\\266\\000<\\262\\000=\\273\\000\\021Y\\267\\000\\022\\022>\\266\\000\\024\\273\\000*Y\\267\\000+\\266\\000,\\026\\011e\\266\\000?\\022@\\266\\000\\024\\266\\000\\030\\266\\000A\\031\\010\\266\\000BW\\247\\000\\026:\\013\\031\\013\\266\\000D\\031\\010\\266\\000E-\\005\\266\\000\\013-\\260\\273\\000*Y\\267\\000+\\266\\000,7\\013\\031\\007\\266\\000F\\031\\010\\266\\000G\\231\\000*-\\007\\266\\000\\013-\\273\\000\\021Y\\267\\000\\022\\022H\\266\\000\\024\\031\\010\\266\\000G\\266\\000\\026\\022I\\266\\000\\024\\266\\000\\030\\266\\000\\015-\\260-\\026\\013\\026\\011e\\210\\266\\000J\\273\\000\\003Y\\267\\000\\004:\\015\\031\\005\\266\\000KY:\\016\\306\\000!\\273\\000\\021Y\\267\\000\\022\\031\\015\\266\\000\\024\\031\\016\\266\\000\\024\\022I\\266\\000\\024\\266\\000\\030:\\015\\247\\377\\332-\\031\\015\\266\\000L\\247\\000\\020:\\007\\262\\000=\\031\\007\\266\\000N\\266\\000O-\\260\\000\\004\\0017\\001\\237\\001\\242\\000C\\001\\007\\001\\264\\002:\\000M\\001\\265\\001\\364\\002:\\000M\\001\\365\\0027\\002:\\000M\\000\\004\\000v\\000\\000\\000\\272\\000.\\000\\000\\000+\\000\\011\\000,\\000\\024\\000-\\000\\034\\000.\\000$\\000/\\000.\\0000\\0006\\0002\\0008\\0005\\000;\\0006\\000\\252\\0008\\000\\262\\0009\\000\\272\\000:\\000\\324\\000<\\000\\334\\000=\\000\\356\\000>\\001\\007\\000@\\001\\020\\000A\\001\\036\\000B\\001*\\000C\\0017\\000E\\001L\\000F\\001a\\000G\\001j\\000I\\001o\\000J\\001\\231\\000K\\001\\237\\000Q\\001\\242\\000L\\001\\244\\000M\\001\\251\\000N\\001\\256\\000O\\001\\263\\000P\\001\\265\\000R\\001\\301\\000S\\001\\306\\000U\\001\\316\\000V\\001\\323\\000W\\001\\363\\000X\\001\\365\\000Z\\001\\377\\000[\\002\\010\\000]\\002\\023\\000^\\0021\\000`\\0027\\000c\\002:\\000a\\002<\\000b\\002G\\000d\\000w\\000\\000\\000\\242\\000\\020\\001a\\000>\\000~\\000\\177\\000\\013\\001\\244\\000\\021\\000\\200\\000\\201\\000\\013\\001\\020\\001'\\000\\202\\000\\203\\000\\007\\001\\036\\001\\031\\000\\204\\000\\205\\000\\010\\001*\\001\\015\\000\\206\\000\\207\\000\\011\\001\\301\\000v\\000\\210\\000\\207\\000\\013\\002\\010\\000/\\000\\211\\000r\\000\\015\\002\\020\\000'\\000\\212\\000r\\000\\016\\002<\\000\\013\\000\\200\\000\\213\\000\\007\\000\\000\\002I\\000x\\000y\\000\\000\\000\\000\\002I\\000\\214\\000r\\000\\001\\000\\000\\002I\\000\\215\\000\\216\\000\\002\\000\\011\\002@\\000\\217\\000\\220\\000\\003\\000\\024\\0025\\000\\221\\000r\\000\\004\\001L\\000\\375\\000\\222\\000\\223\\000\\005\\000\\252\\001\\237\\000\\224\\000\\225\\000\\006\\000\\226\\000\\000\\000\\014\\000\\001\\000\\252\\001\\237\\000\\224\\000\\227\\000\\006\\000\\230\\000\\000\\000\\210\\000\\012\\375\\0006\\007\\000\\231\\007\\000\\232\\001\\375\\000\\233\\000\\007\\000\\233\\377\\000\\315\\000\\012\\007\\000\\234\\007\\000\\232\\007\\000\\235\\007\\000\\231\\007\\000\\232\\000\\007\\000\\233\\007\\000\\236\\007\\000\\237\\004\\000\\001\\007\\000\\240\\377\\000\\022\\000\\012\\007\\000\\234\\007\\000\\232\\007\\000\\235\\007\\000\\231\\007\\000\\232\\007\\000\\241\\007\\000\\233\\007\\000\\236\\007\\000\\237\\004\\000\\000\\374\\000?\\004\\374\\000\\022\\007\\000\\232\\374\\000(\\007\\000\\232\\377\\000\\010\\000\\007\\007\\000\\234\\007\\000\\232\\007\\000\\235\\007\\000\\231\\007\\000\\232\\000\\007\\000\\233\\000\\001\\007\\000\\242\\014\\000\\001\\000\\243\\000\\244\\000\\001\\000u\\000\\000\\0006\\000\\001\\000\\002\\000\\000\\000\\002+\\260\\000\\000\\000\\002\\000v\\000\\000\\000\\006\\000\\001\\000\\000\\000i\\000w\\000\\000\\000\\026\\000\\002\\000\\000\\000\\002\\000x\\000y\\000\\000\\000\\000\\000\\002\\000\\245\\000\\246\\000\\001\\000\\001\\000\\247\\000\\250\\000\\001\\000u\\000\\000\\001\\322\\000\\010\\000\\010\\000\\000\\000\\346\\001M*\\264\\000\\002\\002\\237\\000\\013\\273\\000\\003Y\\267\\000\\004\\260*\\264\\000\\006\\022P\\266\\000\\012M*\\264\\000\\006\\022Q\\266\\000\\012N,\\022R\\022S\\266\\000TM-\\273\\000\\021Y\\267\\000\\022\\262\\000!\\266\\000\\024\\022U\\266\\000\\024\\266\\000\\030\\022S\\266\\000TN\\273\\000\\021Y\\267\\000\\022-\\266\\000\\024\\262\\000!\\266\\000\\024,\\266\\000\\024\\022V\\266\\000\\024\\266\\000\\030M\\273\\000WY,\\267\\000X:\\004\\031\\004+\\266\\000Y\\031\\004\\266\\000Z\\270\\000\\016\\270\\000[:\\005\\273\\000\\\\Y\\267\\000]:\\006\\273\\000\\\\Y\\267\\000]:\\007\\031\\005\\273\\000^Y\\273\\000\\003Y\\267\\000\\004\\266\\000_\\267\\000`\\031\\007\\031\\006\\004\\275\\000\\003Y\\003,S\\271\\000a\\005\\000\\231\\000\\021*\\031\\006\\266\\000b\\265\\000\\005*\\006\\265\\000\\002\\247\\000\\004N\\273\\000cY,\\267\\000d\\266\\000eW,\\022R\\022f\\266\\000T\\260\\000\\001\\000\\022\\000\\315\\000\\320\\000M\\000\\003\\000v\\000\\000\\000Z\\000\\026\\000\\000\\000n\\000\\002\\000o\\000\\012\\000p\\000\\022\\000t\\000\\034\\000u\\000&\\000v\\000/\\000w\\000K\\000x\\000i\\000y\\000s\\000z\\000y\\000{\\000~\\000|\\000\\201\\000}\\000\\206\\000~\\000\\217\\000\\177\\000\\230\\000\\200\\000\\277\\000\\202\\000\\310\\000\\203\\000\\315\\000\\206\\000\\320\\000\\205\\000\\321\\000\\207\\000\\335\\000\\210\\000w\\000\\000\\000\\\\\\000\\011\\000&\\000\\247\\000\\251\\000r\\000\\003\\000s\\000Z\\000\\252\\000\\253\\000\\004\\000\\206\\000G\\000\\254\\000\\255\\000\\005\\000\\217\\000>\\000\\256\\000\\257\\000\\006\\000\\230\\0005\\000\\260\\000\\257\\000\\007\\000\\321\\000\\000\\000\\256\\000\\213\\000\\003\\000\\000\\000\\346\\000x\\000y\\000\\000\\000\\000\\000\\346\\000\\245\\000\\246\\000\\001\\000\\002\\000\\344\\000\\261\\000r\\000\\002\\000\\230\\000\\000\\000\\020\\000\\004\\374\\000\\022\\007\\000\\232\\373\\000\\272B\\007\\000\\242\\000\\000\\001\\000\\262\\000\\263\\000\\001\\000u\\000\\000\\0006\\000\\001\\000\\002\\000\\000\\000\\002+\\260\\000\\000\\000\\002\\000v\\000\\000\\000\\006\\000\\001\\000\\000\\000\\215\\000w\\000\\000\\000\\026\\000\\002\\000\\000\\000\\002\\000x\\000y\\000\\000\\000\\000\\000\\002\\000\\214\\000r\\000\\001\\000\\001\\000\\264\\000\\265\\000\\001\\000u\\000\\000\\000Y\\000\\003\\000\\002\\000\\000\\000\\024+\\266\\000\\014\\232\\000\\017\\273\\000cY+\\267\\000d\\266\\000eW\\261\\000\\000\\000\\003\\000v\\000\\000\\000\\016\\000\\003\\000\\000\\000\\222\\000\\007\\000\\223\\000\\023\\000\\225\\000w\\000\\000\\000\\026\\000\\002\\000\\000\\000\\024\\000x\\000y\\000\\000\\000\\000\\000\\024\\000\\214\\000r\\000\\001\\000\\230\\000\\000\\000\\003\\000\\001\\023\\000\\010\\000\\266\\000t\\000\\001\\000u\\000\\000\\000"\\000\\001\\000\\000\\000\\000\\000\\012\\023\\000g\\270\\000h\\263\\000=\\261\\000\\000\\000\\001\\000v\\000\\000\\000\\006\\000\\001\\000\\000\\000\\037\\000\\001\\000\\267\\000\\000\\000\\002\\000\\270
6	pl.umk.mat.zawodyweb.compiler.classes.LanguageACM	1	ACM Compiler	\\312\\376\\272\\276\\000\\000\\0002\\001\\263\\012\\000\\215\\000\\353\\011\\000\\214\\000\\354\\007\\000\\355\\012\\000\\003\\000\\356\\010\\000\\357\\010\\000\\360\\012\\000\\361\\000\\362\\010\\000\\363\\007\\000\\364\\012\\000\\011\\000\\353\\007\\000\\365\\012\\000\\013\\000\\356\\012\\000\\011\\000\\366\\010\\000\\367\\010\\000\\370\\012\\000\\371\\000\\372\\012\\000\\011\\000\\373\\012\\000\\011\\000\\374\\012\\000\\013\\000\\375\\007\\000\\376\\012\\000\\003\\000\\377\\012\\000\\024\\001\\000\\012\\000\\003\\001\\001\\010\\001\\002\\012\\000\\003\\001\\003\\012\\000\\013\\001\\004\\007\\001\\005\\012\\000\\033\\001\\000\\010\\001\\006\\007\\001\\007\\007\\001\\010\\010\\001\\011\\012\\000\\037\\001\\012\\012\\000\\036\\001\\013\\007\\001\\014\\007\\001\\015\\012\\000$\\000\\353\\007\\001\\016\\010\\001\\017\\012\\000&\\001\\020\\012\\000$\\001\\021\\010\\001\\022\\012\\000\\036\\001\\023\\010\\001\\024\\012\\000\\212\\001\\025\\010\\001\\026\\010\\001\\027\\010\\001\\030\\012\\000\\212\\001\\031\\010\\001\\032\\010\\001\\033\\010\\001\\034\\010\\001\\035\\010\\001\\036\\010\\001\\037\\007\\001 \\010\\001!\\012\\0008\\000\\356\\010\\001"\\012\\0008\\001#\\012\\000$\\001$\\007\\000\\301\\012\\0008\\001%\\012\\0008\\001\\004\\010\\001&\\010\\001'\\010\\001(\\012\\000\\212\\001)\\010\\001*\\010\\001+\\010\\001,\\010\\001-\\010\\001.\\010\\001/\\010\\0010\\010\\0011\\010\\0012\\010\\0013\\010\\0014\\012\\0015\\0016\\010\\0017\\010\\000\\341\\010\\0018\\010\\0019\\012\\0008\\001:\\012\\001;\\001<\\010\\001=\\012\\000\\212\\001>\\012\\000\\212\\001?\\012\\001@\\001A\\007\\001B\\010\\001C\\012\\001D\\001E\\010\\001F\\005\\000\\000\\000\\000\\000\\000\\033X\\012\\001G\\001H\\007\\001I\\012\\000b\\001\\000\\010\\001J\\007\\001K\\012\\000e\\000\\353\\010\\001L\\012\\000e\\001M\\012\\000\\212\\001N\\012\\000e\\001O\\010\\001P\\010\\001Q\\010\\001R\\010\\001S\\012\\000\\003\\001T\\010\\001U\\010\\001V\\010\\001W\\010\\001X\\010\\001Y\\012\\0015\\001Z\\010\\001[\\012\\000\\212\\001\\\\\\012\\000\\003\\001]\\010\\001^\\010\\001_\\010\\001`\\010\\001a\\010\\001b\\010\\001c\\010\\001d\\010\\001e\\010\\001f\\010\\000\\313\\010\\001g\\010\\001h\\010\\000\\302\\010\\001i\\010\\001j\\010\\001k\\010\\001l\\007\\001m\\012\\000\\212\\001n\\007\\001o\\007\\001p\\007\\001q\\001\\000\\012properties\\001\\000\\026Ljava/util/Properties;\\001\\000\\006<init>\\001\\000\\003()V\\001\\000\\004Code\\001\\000\\017LineNumberTable\\001\\000\\022LocalVariableTable\\001\\000\\004this\\001\\0003Lpl/umk/mat/zawodyweb/compiler/classes/LanguageACM;\\001\\000\\015setProperties\\001\\000\\031(Ljava/util/Properties;)V\\001\\000\\007runTest\\001\\000e(Ljava/lang/String;Lpl/umk/mat/zawodyweb/checker/TestInput;)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\001e\\001\\000-Lorg/apache/commons/httpclient/HttpException;\\001\\000\\025Ljava/io/IOException;\\001\\000&Ljava/io/UnsupportedEncodingException;\\001\\000\\004name\\001\\000\\022Ljava/lang/String;\\001\\000\\005value\\001\\000\\002ex\\001\\000!Ljava/lang/NumberFormatException;\\001\\000\\010location\\001\\000 Ljava/lang/InterruptedException;\\001\\000\\005split\\001\\000\\023[Ljava/lang/String;\\001\\000\\004path\\001\\000\\005input\\001\\000(Lpl/umk/mat/zawodyweb/checker/TestInput;\\001\\000\\006result\\001\\000)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\007acmSite\\001\\000\\005login\\001\\000\\010password\\001\\000\\006client\\001\\000*Lorg/apache/commons/httpclient/HttpClient;\\001\\000\\007logging\\001\\0001Lorg/apache/commons/httpclient/methods/GetMethod;\\001\\000\\010firstGet\\001\\000\\025Ljava/io/InputStream;\\001\\000\\006params\\001\\0007Lorg/apache/commons/httpclient/params/HttpClientParams;\\001\\000\\002br\\001\\000\\030Ljava/io/BufferedReader;\\001\\000\\004line\\001\\000\\017vectorLoginData\\001\\000\\022Ljava/util/Vector;\\001\\000\\012sendAnswer\\001\\0002Lorg/apache/commons/httpclient/methods/PostMethod;\\001\\000\\011loginData\\001\\000.[Lorg/apache/commons/httpclient/NameValuePair;\\001\\000\\004lang\\001\\000\\016dataSendAnswer\\001\\000\\002id\\001\\000\\001I\\001\\000\\005limit\\001\\000\\012limitstart\\001\\000\\012statusSite\\001\\000\\004stat\\001\\000\\004time\\001\\000\\006logout\\001\\000\\026LocalVariableTypeTable\\001\\000ALjava/util/Vector<Lorg/apache/commons/httpclient/NameValuePair;>;\\001\\000\\015StackMapTable\\007\\001o\\007\\001m\\007\\001r\\007\\000\\355\\007\\000\\364\\007\\000\\365\\007\\001s\\007\\001t\\007\\000\\376\\007\\001\\005\\007\\001\\007\\007\\001\\014\\007\\001\\015\\007\\001 \\007\\001B\\007\\001I\\001\\000\\012precompile\\001\\000\\006([B)[B\\001\\000\\004code\\001\\000\\002[B\\001\\000\\007compile\\001\\000\\026([B)Ljava/lang/String;\\001\\000\\013postcompile\\001\\000&(Ljava/lang/String;)Ljava/lang/String;\\001\\000\\014closeProgram\\001\\000\\025(Ljava/lang/String;)V\\001\\000\\012SourceFile\\001\\000\\020LanguageACM.java\\014\\000\\221\\000\\222\\014\\000\\217\\000\\220\\001\\000'pl/umk/mat/zawodyweb/checker/TestOutput\\014\\000\\221\\000\\350\\001\\000\\033http://uva.onlinejudge.org/\\001\\000\\015acm_uva.login\\007\\001u\\014\\001v\\000\\346\\001\\000\\020acm_uva.password\\001\\000(org/apache/commons/httpclient/HttpClient\\001\\000/org/apache/commons/httpclient/methods/GetMethod\\014\\001w\\001x\\001\\000\\016http.useragent\\001\\000/Opera/9.64 (Windows NT 6.0; U; pl) Presto/2.1.1\\007\\001t\\014\\001y\\001z\\014\\001{\\001|\\014\\001}\\001~\\014\\001\\177\\001\\200\\001\\000+org/apache/commons/httpclient/HttpException\\014\\001\\201\\001\\202\\014\\001\\203\\001\\204\\014\\001\\205\\000\\350\\001\\000\\015HttpException\\014\\001\\206\\000\\350\\014\\001\\207\\000\\222\\001\\000\\023java/io/IOException\\001\\000\\013IOException\\001\\000\\026java/io/BufferedReader\\001\\000\\031java/io/InputStreamReader\\001\\000\\005UTF-8\\014\\000\\221\\001\\210\\014\\000\\221\\001\\211\\001\\000$java/io/UnsupportedEncodingException\\001\\000\\020java/util/Vector\\001\\000+org/apache/commons/httpclient/NameValuePair\\001\\000\\010username\\014\\000\\221\\001\\212\\014\\001\\213\\001\\214\\001\\000\\006passwd\\014\\001\\215\\001\\204\\001\\000\\025.*class="mod_login".*\\014\\001\\216\\001\\217\\001\\000\\025(?i).*submit.*login.*\\001\\000\\033.*hidden.*name=".*value=".*\\001\\000\\006name="\\014\\000\\247\\001\\220\\001\\000\\001"\\001\\000\\007value="\\001\\000\\010remember\\001\\000\\003yes\\001\\000\\006Submit\\001\\000\\005Login\\001\\0000org/apache/commons/httpclient/methods/PostMethod\\001\\000Fhttp://uva.onlinejudge.org/index.php?option=com_comprofiler&task=login\\001\\000\\007Referer\\014\\001\\221\\001\\212\\014\\001\\222\\001\\223\\014\\001\\224\\001\\225\\001\\000Zhttp://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=25&page=save_submission\\001\\000\\022CODEFILE_EXTENSION\\001\\000\\001c\\014\\001\\226\\001\\227\\001\\000\\0011\\001\\000\\004java\\001\\000\\0012\\001\\000\\003cpp\\001\\000\\0013\\001\\000\\003pas\\001\\000\\0014\\001\\000\\011problemid\\001\\000\\000\\001\\000\\010category\\001\\000\\007localid\\007\\001r\\014\\001\\230\\001\\204\\001\\000\\010language\\001\\000\\006submit\\001\\000\\010Location\\014\\001\\231\\001\\232\\007\\001\\233\\014\\001\\234\\001\\204\\001\\000\\001+\\014\\001\\235\\001\\236\\014\\001\\237\\001\\240\\007\\001\\241\\014\\001\\242\\001\\236\\001\\000\\037java/lang/NumberFormatException\\001\\000\\001=\\007\\001\\243\\014\\001\\244\\001\\245\\001\\000\\025NumberFormatException\\007\\001\\246\\014\\001\\247\\001\\250\\001\\000\\036java/lang/InterruptedException\\001\\000\\024InterruptedException\\001\\000\\027java/lang/StringBuilder\\001\\000Yhttp://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=9&limit=50&limitstart=\\014\\001\\251\\001\\252\\014\\001\\253\\001\\240\\014\\001\\254\\001\\204\\001\\000\\006.*<td>\\001\\000\\007</td>.*\\001\\000\\011.*</tr>.*\\001\\000\\023(<td[^>]*>)|(</td>)\\014\\001\\255\\001\\202\\001\\000\\010Received\\001\\000\\007Running\\001\\000\\015Sent to judge\\001\\000\\016In judge queue\\001\\000\\014.*Accepted.*\\014\\001\\256\\001\\257\\001\\000\\002\\\\.\\014\\001\\260\\001\\245\\014\\001\\261\\001\\202\\001\\000\\025.*Compilation error.*\\001\\000\\026.*Presentation error.*\\001\\000\\020.*Wrong answer.*\\001\\000\\027.*Time limit exceeded.*\\001\\000\\031.*Memory limit exceeded.*\\001\\000\\021.*Runtime error.*\\001\\000\\021Unknown status: "\\001\\0002http://uva.onlinejudge.org/index.php?option=logout\\001\\000\\003op2\\001\\000\\006return\\001\\000\\032http://uva.onlinejudge.org\\001\\000\\007english\\001\\000\\007message\\001\\000\\0010\\001\\000\\006Logout\\001\\000\\020java/lang/String\\014\\000\\221\\001\\262\\001\\0001pl/umk/mat/zawodyweb/compiler/classes/LanguageACM\\001\\000\\020java/lang/Object\\001\\000/pl/umk/mat/zawodyweb/compiler/CompilerInterface\\001\\000&pl/umk/mat/zawodyweb/checker/TestInput\\001\\000\\023java/io/InputStream\\001\\0005org/apache/commons/httpclient/params/HttpClientParams\\001\\000\\024java/util/Properties\\001\\000\\013getProperty\\001\\000\\011getParams\\001\\0009()Lorg/apache/commons/httpclient/params/HttpClientParams;\\001\\000\\014setParameter\\001\\000'(Ljava/lang/String;Ljava/lang/Object;)V\\001\\000\\011setParams\\001\\000:(Lorg/apache/commons/httpclient/params/HttpClientParams;)V\\001\\000\\015executeMethod\\001\\000-(Lorg/apache/commons/httpclient/HttpMethod;)I\\001\\000\\027getResponseBodyAsStream\\001\\000\\027()Ljava/io/InputStream;\\001\\000\\011setResult\\001\\000\\004(I)V\\001\\000\\012getMessage\\001\\000\\024()Ljava/lang/String;\\001\\000\\015setResultDesc\\001\\000\\007setText\\001\\000\\021releaseConnection\\001\\000*(Ljava/io/InputStream;Ljava/lang/String;)V\\001\\000\\023(Ljava/io/Reader;)V\\001\\000'(Ljava/lang/String;Ljava/lang/String;)V\\001\\000\\012addElement\\001\\000\\025(Ljava/lang/Object;)V\\001\\000\\010readLine\\001\\000\\007matches\\001\\000\\025(Ljava/lang/String;)Z\\001\\000'(Ljava/lang/String;)[Ljava/lang/String;\\001\\000\\020setRequestHeader\\001\\000\\007toArray\\001\\000(([Ljava/lang/Object;)[Ljava/lang/Object;\\001\\000\\016setRequestBody\\001\\0001([Lorg/apache/commons/httpclient/NameValuePair;)V\\001\\000\\006equals\\001\\000\\025(Ljava/lang/Object;)Z\\001\\000\\007getText\\001\\000\\021getResponseHeader\\001\\000:(Ljava/lang/String;)Lorg/apache/commons/httpclient/Header;\\001\\000$org/apache/commons/httpclient/Header\\001\\000\\010getValue\\001\\000\\013lastIndexOf\\001\\000\\025(Ljava/lang/String;)I\\001\\000\\011substring\\001\\000\\025(I)Ljava/lang/String;\\001\\000\\021java/lang/Integer\\001\\000\\010parseInt\\001\\000\\023java/net/URLDecoder\\001\\000\\006decode\\001\\0008(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;\\001\\000\\020java/lang/Thread\\001\\000\\005sleep\\001\\000\\004(J)V\\001\\000\\006append\\001\\000-(Ljava/lang/String;)Ljava/lang/StringBuilder;\\001\\000\\007valueOf\\001\\000\\010toString\\001\\000\\011setPoints\\001\\000\\014getMaxPoints\\001\\000\\003()I\\001\\000\\012replaceAll\\001\\000\\012setRuntime\\001\\000\\005([B)V\\000!\\000\\214\\000\\215\\000\\001\\000\\216\\000\\001\\000\\002\\000\\217\\000\\220\\000\\000\\000\\007\\000\\001\\000\\221\\000\\222\\000\\001\\000\\223\\000\\000\\000/\\000\\001\\000\\001\\000\\000\\000\\005*\\267\\000\\001\\261\\000\\000\\000\\002\\000\\224\\000\\000\\000\\006\\000\\001\\000\\000\\000\\036\\000\\225\\000\\000\\000\\014\\000\\001\\000\\000\\000\\005\\000\\226\\000\\227\\000\\000\\000\\001\\000\\230\\000\\231\\000\\001\\000\\223\\000\\000\\000>\\000\\002\\000\\002\\000\\000\\000\\006*+\\265\\000\\002\\261\\000\\000\\000\\002\\000\\224\\000\\000\\000\\012\\000\\002\\000\\000\\000$\\000\\005\\000%\\000\\225\\000\\000\\000\\026\\000\\002\\000\\000\\000\\006\\000\\226\\000\\227\\000\\000\\000\\000\\000\\006\\000\\217\\000\\220\\000\\001\\000\\001\\000\\232\\000\\233\\000\\001\\000\\223\\000\\000\\017\\177\\000\\007\\000\\034\\000\\000\\006\\326\\273\\000\\003Y\\001\\267\\000\\004N\\022\\005:\\004*\\264\\000\\002\\022\\006\\266\\000\\007:\\005*\\264\\000\\002\\022\\010\\266\\000\\007:\\006\\273\\000\\011Y\\267\\000\\012:\\007\\273\\000\\013Y\\031\\004\\267\\000\\014:\\010\\001:\\011\\031\\007\\266\\000\\015:\\012\\031\\012\\022\\016\\022\\017\\266\\000\\020\\031\\007\\031\\012\\266\\000\\021\\031\\007\\031\\010\\266\\000\\022W\\031\\010\\266\\000\\023:\\011\\247\\000=:\\013-\\002\\266\\000\\025-\\031\\013\\266\\000\\026\\266\\000\\027-\\022\\030\\266\\000\\031\\031\\010\\266\\000\\032-\\260:\\013-\\002\\266\\000\\025-\\031\\013\\266\\000\\034\\266\\000\\027-\\022\\035\\266\\000\\031\\031\\010\\266\\000\\032-\\260\\001:\\013\\273\\000\\036Y\\273\\000\\037Y\\031\\011\\022 \\267\\000!\\267\\000":\\013\\247\\000\\005:\\014\\273\\000$Y\\267\\000%:\\017\\031\\017\\273\\000&Y\\022'\\031\\005\\267\\000(\\266\\000)\\031\\017\\273\\000&Y\\022*\\031\\006\\267\\000(\\266\\000)\\031\\013\\266\\000+:\\014\\031\\014\\306\\000\\027\\031\\014\\022,\\266\\000-\\232\\000\\015\\031\\013\\266\\000+:\\014\\247\\377\\352\\031\\014\\306\\000U\\031\\014\\022.\\266\\000-\\232\\000K\\031\\014\\022/\\266\\000-\\231\\0007\\031\\014\\0220\\266\\0001\\0042\\0222\\266\\0001\\0032:\\015\\031\\014\\0223\\266\\0001\\0042\\0222\\266\\0001\\0032:\\016\\031\\017\\273\\000&Y\\031\\015\\031\\016\\267\\000(\\266\\000)\\031\\013\\266\\000+:\\014\\247\\377\\254\\031\\017\\273\\000&Y\\0224\\0225\\267\\000(\\266\\000)\\031\\017\\273\\000&Y\\0226\\0227\\267\\000(\\266\\000)\\247\\000 :\\020-\\002\\266\\000\\025-\\031\\020\\266\\000\\034\\266\\000\\027-\\022\\035\\266\\000\\031\\031\\010\\266\\000\\032-\\260\\031\\010\\266\\000\\032\\273\\0008Y\\0229\\267\\000::\\020\\031\\020\\022;\\031\\004\\266\\000<\\003\\275\\000&:\\021\\031\\017\\031\\021\\266\\000=\\300\\000>:\\021\\031\\020\\031\\021\\266\\000?\\031\\007\\031\\020\\266\\000\\022W\\247\\000=:\\022-\\002\\266\\000\\025-\\031\\022\\266\\000\\026\\266\\000\\027-\\022\\030\\266\\000\\031\\031\\020\\266\\000@-\\260:\\022-\\002\\266\\000\\025-\\031\\022\\266\\000\\034\\266\\000\\027-\\022\\035\\266\\000\\031\\031\\020\\266\\000@-\\260\\031\\020\\266\\000@\\273\\0008Y\\022A\\267\\000::\\020*\\264\\000\\002\\022B\\266\\000\\007:\\022\\031\\022\\022C\\266\\000D\\231\\000\\012\\022E:\\022\\247\\0003\\031\\022\\022F\\266\\000D\\231\\000\\012\\022G:\\022\\247\\000"\\031\\022\\022H\\266\\000D\\231\\000\\012\\022I:\\022\\247\\000\\021\\031\\022\\022J\\266\\000D\\231\\000\\007\\022K:\\022\\020\\006\\275\\000&Y\\003\\273\\000&Y\\022L\\022M\\267\\000(SY\\004\\273\\000&Y\\022N\\022M\\267\\000(SY\\005\\273\\000&Y\\022O,\\266\\000P\\267\\000(SY\\006\\273\\000&Y\\022Q\\031\\022\\267\\000(SY\\007\\273\\000&Y\\022R+\\267\\000(SY\\010\\273\\000&Y\\022S\\0226\\267\\000(S:\\023\\031\\020\\031\\023\\266\\000?\\031\\007\\031\\020\\266\\000\\022W\\031\\020\\022T\\266\\000U\\266\\000V:\\025\\031\\025\\031\\025\\022W\\266\\000X\\004`\\266\\000Y\\270\\000Z6\\024\\247\\000.:\\026-\\002\\266\\000\\025-\\031\\025\\031\\025\\022\\\\\\266\\000X\\004`\\266\\000Y\\022 \\270\\000]\\266\\000\\027-\\022^\\266\\000\\031\\031\\020\\266\\000@-\\260\\247\\000=:\\025-\\002\\266\\000\\025-\\031\\025\\266\\000\\026\\266\\000\\027-\\022\\030\\266\\000\\031\\031\\020\\266\\000@-\\260:\\025-\\002\\266\\000\\025-\\031\\025\\266\\000\\034\\266\\000\\027-\\022\\035\\266\\000\\031\\031\\020\\266\\000@-\\260\\031\\020\\266\\000@\\02026\\025\\0036\\026\\022M:\\027\\022M:\\030\\022M:\\031\\024\\000_\\270\\000a\\247\\000\\033:\\032-\\002\\266\\000\\025-\\031\\032\\266\\000c\\266\\000\\027-\\022d\\266\\000\\031-\\260\\273\\000\\013Y\\273\\000eY\\267\\000f\\022g\\266\\000h\\025\\026\\270\\000i\\266\\000h\\266\\000j\\267\\000\\014:\\010\\001:\\011\\031\\007\\031\\010\\266\\000\\022W\\031\\010\\266\\000\\023:\\011\\247\\000=:\\032-\\002\\266\\000\\025-\\031\\032\\266\\000\\026\\266\\000\\027-\\022\\030\\266\\000\\031\\031\\010\\266\\000\\032-\\260:\\032-\\002\\266\\000\\025-\\031\\032\\266\\000\\034\\266\\000\\027-\\022\\035\\266\\000\\031\\031\\010\\266\\000\\032-\\260\\273\\000\\036Y\\273\\000\\037Y\\031\\011\\022 \\267\\000!\\267\\000":\\013\\247\\000\\005:\\032\\031\\013\\266\\000+Y:\\014\\306\\000v\\031\\014\\273\\000eY\\267\\000f\\022k\\266\\000h\\025\\024\\270\\000i\\266\\000h\\022l\\266\\000h\\266\\000j\\266\\000-\\231\\377\\324\\031\\014:\\027\\031\\013\\266\\000+:\\014\\031\\014\\022m\\266\\000-\\232\\000#\\273\\000eY\\267\\000f\\031\\027\\266\\000h\\031\\014\\266\\000h\\266\\000j:\\027\\031\\013\\266\\000+:\\014\\247\\377\\331\\031\\027\\022n\\266\\0001:\\032\\031\\032\\020\\0072:\\030\\031\\032\\020\\0132:\\031\\247\\377\\205\\247\\000 :\\032-\\002\\266\\000\\025-\\031\\032\\266\\000\\034\\266\\000\\027-\\022\\035\\266\\000\\031\\031\\010\\266\\000\\032-\\260\\031\\030\\022M\\266\\000D\\232\\001\\177\\031\\031\\022M\\266\\000D\\232\\001u-\\003\\266\\000o\\031\\030\\022p\\266\\000D\\232\\001E\\031\\030\\022q\\266\\000D\\232\\001;\\031\\030\\022r\\266\\000D\\232\\0011\\031\\030\\022s\\266\\000D\\232\\001'\\031\\030\\022t\\266\\000-\\231\\000#-\\003\\266\\000\\025-,\\266\\000u\\266\\000o-\\031\\031\\022v\\022M\\266\\000w\\270\\000Z\\266\\000x\\247\\001(\\031\\030\\022y\\266\\000-\\231\\000\\033-\\006\\266\\000\\025-\\031\\031\\022v\\022M\\266\\000w\\270\\000Z\\266\\000x\\247\\001\\006\\031\\030\\022z\\266\\000-\\231\\000#-\\003\\266\\000\\025-,\\266\\000u\\266\\000o-\\031\\031\\022v\\022M\\266\\000w\\270\\000Z\\266\\000x\\247\\000\\334\\031\\030\\022{\\266\\000-\\231\\000\\033-\\004\\266\\000\\025-\\031\\031\\022v\\022M\\266\\000w\\270\\000Z\\266\\000x\\247\\000\\272\\031\\030\\022|\\266\\000-\\231\\000\\033-\\005\\266\\000\\025-\\031\\031\\022v\\022M\\266\\000w\\270\\000Z\\266\\000x\\247\\000\\230\\031\\030\\022}\\266\\000-\\231\\000\\033-\\010\\266\\000\\025-\\031\\031\\022v\\022M\\266\\000w\\270\\000Z\\266\\000x\\247\\000v\\031\\030\\022~\\266\\000-\\231\\000\\033-\\007\\266\\000\\025-\\031\\031\\022v\\022M\\266\\000w\\270\\000Z\\266\\000x\\247\\000T-\\020\\010\\266\\000\\025-\\273\\000eY\\267\\000f\\022\\177\\266\\000h\\031\\030\\266\\000h\\0222\\266\\000h\\266\\000j\\266\\000\\027\\247\\000.\\024\\000_\\270\\000a\\247\\375o:\\032-\\002\\266\\000\\025-\\031\\032\\266\\000c\\266\\000\\027-\\022d\\266\\000\\031-\\260\\025\\026\\025\\025`6\\026\\247\\375M\\031\\010\\266\\000\\032\\273\\0008Y\\022\\200\\267\\000::\\020\\010\\275\\000&Y\\003\\273\\000&Y\\022\\201\\022\\202\\267\\000(SY\\004\\273\\000&Y\\022\\203\\022\\204\\267\\000(SY\\005\\273\\000&Y\\022\\205\\022\\206\\267\\000(SY\\006\\273\\000&Y\\022\\207\\022\\210\\267\\000(SY\\007\\273\\000&Y\\0226\\022\\211\\267\\000(S:\\032\\031\\020\\031\\032\\266\\000?\\031\\007\\031\\020\\266\\000\\022W\\247\\000\\024:\\033\\031\\020\\266\\000@\\247\\000\\012:\\033\\031\\020\\266\\000@\\031\\020\\266\\000@-\\260\\000\\021\\000Q\\000`\\000c\\000\\024\\000Q\\000`\\000\\200\\000\\033\\000\\240\\000\\264\\000\\267\\000#\\000\\342\\001y\\001|\\000\\033\\001\\313\\001\\323\\001\\326\\000\\024\\001\\313\\001\\323\\001\\363\\000\\033\\002\\343\\002\\366\\002\\371\\000[\\002\\317\\003#\\003'\\000\\024\\002\\317\\003#\\003D\\000\\033\\003y\\003\\177\\003\\202\\000b\\003\\275\\003\\314\\003\\317\\000\\024\\003\\275\\003\\314\\003\\354\\000\\033\\004\\011\\004\\035\\004 \\000#\\004"\\004\\240\\004\\243\\000\\033\\006%\\006+\\006.\\000b\\006\\263\\006\\273\\006\\276\\000\\024\\006\\263\\006\\273\\006\\310\\000\\033\\000\\004\\000\\224\\000\\000\\003>\\000\\317\\000\\000\\000)\\000\\011\\000*\\000\\015\\000,\\000\\030\\000-\\000#\\000/\\000,\\0000\\0007\\0001\\000:\\0003\\000A\\0004\\000J\\0005\\000Q\\0008\\000Y\\0009\\000`\\000F\\000c\\000:\\000e\\000;\\000j\\000<\\000s\\000=\\000y\\000>\\000~\\000?\\000\\200\\000@\\000\\202\\000A\\000\\207\\000B\\000\\220\\000C\\000\\226\\000D\\000\\233\\000E\\000\\235\\000G\\000\\240\\000I\\000\\264\\000K\\000\\267\\000J\\000\\271\\000M\\000\\302\\000N\\000\\322\\000O\\000\\342\\000Q\\000\\351\\000R\\000\\370\\000S\\001\\002\\000U\\001\\021\\000V\\001\\033\\000W\\001-\\000X\\001?\\000Y\\001O\\000[\\001Y\\000]\\001i\\000^\\001y\\000e\\001|\\000_\\001~\\000`\\001\\203\\000a\\001\\214\\000b\\001\\222\\000c\\001\\227\\000d\\001\\231\\000f\\001\\236\\000g\\001\\251\\000h\\001\\262\\000i\\001\\270\\000j\\001\\304\\000k\\001\\313\\000m\\001\\323\\000z\\001\\326\\000n\\001\\330\\000o\\001\\335\\000p\\001\\346\\000q\\001\\354\\000r\\001\\361\\000s\\001\\363\\000t\\001\\365\\000u\\001\\372\\000v\\002\\003\\000w\\002\\011\\000x\\002\\016\\000y\\002\\020\\000{\\002\\025\\000|\\002 \\000}\\002+\\000~\\0025\\000\\177\\002<\\000\\200\\002F\\000\\201\\002M\\000\\202\\002W\\000\\203\\002^\\000\\204\\002h\\000\\205\\002l\\000\\207\\002\\310\\000\\217\\002\\317\\000\\223\\002\\327\\000\\224\\002\\343\\000\\226\\002\\366\\000\\235\\002\\371\\000\\227\\002\\373\\000\\230\\003\\000\\000\\231\\003\\027\\000\\232\\003\\035\\000\\233\\003"\\000\\234\\003$\\000\\252\\003'\\000\\236\\003)\\000\\237\\003.\\000\\240\\0037\\000\\241\\003=\\000\\242\\003B\\000\\243\\003D\\000\\244\\003F\\000\\245\\003K\\000\\246\\003T\\000\\247\\003Z\\000\\250\\003_\\000\\251\\003a\\000\\253\\003f\\000\\254\\003j\\000\\255\\003m\\000\\256\\003q\\000\\257\\003y\\000\\261\\003\\177\\000\\267\\003\\202\\000\\262\\003\\204\\000\\263\\003\\211\\000\\264\\003\\222\\000\\265\\003\\230\\000\\266\\003\\232\\000\\271\\003\\272\\000\\272\\003\\275\\000\\274\\003\\305\\000\\275\\003\\314\\000\\312\\003\\317\\000\\276\\003\\321\\000\\277\\003\\326\\000\\300\\003\\337\\000\\301\\003\\345\\000\\302\\003\\352\\000\\303\\003\\354\\000\\304\\003\\356\\000\\305\\003\\363\\000\\306\\003\\374\\000\\307\\004\\002\\000\\310\\004\\007\\000\\311\\004\\011\\000\\314\\004\\035\\000\\316\\004 \\000\\315\\004"\\000\\320\\004-\\000\\321\\004Q\\000\\322\\004U\\000\\323\\004\\\\\\000\\324\\004f\\000\\325\\004|\\000\\326\\004\\206\\000\\330\\004\\217\\000\\331\\004\\226\\000\\332\\004\\235\\000\\333\\004\\240\\000\\343\\004\\243\\000\\335\\004\\245\\000\\336\\004\\252\\000\\337\\004\\263\\000\\340\\004\\271\\000\\341\\004\\276\\000\\342\\004\\300\\000\\344\\004\\324\\000\\345\\004\\331\\000\\346\\005\\001\\000\\347\\005\\013\\000\\350\\005\\020\\000\\351\\005\\030\\000\\352\\005+\\000\\353\\0055\\000\\354\\005:\\000\\355\\005M\\000\\356\\005W\\000\\357\\005\\\\\\000\\360\\005d\\000\\361\\005w\\000\\362\\005\\201\\000\\363\\005\\206\\000\\364\\005\\231\\000\\365\\005\\243\\000\\366\\005\\250\\000\\367\\005\\273\\000\\370\\005\\305\\000\\371\\005\\312\\000\\372\\005\\335\\000\\373\\005\\347\\000\\374\\005\\354\\000\\375\\005\\377\\000\\377\\006\\005\\001\\000\\006"\\001\\002\\006%\\001\\005\\006+\\001\\013\\006.\\001\\006\\0060\\001\\007\\0065\\001\\010\\006>\\001\\011\\006D\\001\\012\\006F\\001\\016\\006M\\001\\020\\006P\\001\\021\\006U\\001\\022\\006`\\001\\023\\006\\254\\001\\032\\006\\263\\001\\034\\006\\273\\001!\\006\\276\\001\\035\\006\\300\\001\\036\\006\\305\\001!\\006\\310\\001\\037\\006\\312\\001 \\006\\317\\001"\\006\\324\\001#\\000\\225\\000\\000\\001\\316\\000.\\000e\\000\\033\\000\\234\\000\\235\\000\\013\\000\\202\\000\\033\\000\\234\\000\\236\\000\\013\\000\\271\\000\\000\\000\\234\\000\\237\\000\\014\\001-\\000"\\000\\240\\000\\241\\000\\015\\001?\\000\\020\\000\\242\\000\\241\\000\\016\\001~\\000\\033\\000\\234\\000\\236\\000\\020\\001\\330\\000\\033\\000\\234\\000\\235\\000\\022\\001\\365\\000\\033\\000\\234\\000\\236\\000\\022\\002\\373\\000)\\000\\243\\000\\244\\000\\026\\002\\343\\000A\\000\\245\\000\\241\\000\\025\\003)\\000\\033\\000\\234\\000\\235\\000\\025\\003F\\000\\033\\000\\234\\000\\236\\000\\025\\003\\204\\000\\026\\000\\234\\000\\246\\000\\032\\003\\321\\000\\033\\000\\234\\000\\235\\000\\032\\003\\356\\000\\033\\000\\234\\000\\236\\000\\032\\004"\\000\\000\\000\\243\\000\\237\\000\\032\\004\\217\\000\\016\\000\\247\\000\\250\\000\\032\\004\\245\\000\\033\\000\\234\\000\\236\\000\\032\\0060\\000\\026\\000\\234\\000\\246\\000\\032\\006\\300\\000\\005\\000\\234\\000\\235\\000\\033\\006\\312\\000\\005\\000\\234\\000\\236\\000\\033\\000\\000\\006\\326\\000\\226\\000\\227\\000\\000\\000\\000\\006\\326\\000\\251\\000\\241\\000\\001\\000\\000\\006\\326\\000\\252\\000\\253\\000\\002\\000\\011\\006\\315\\000\\254\\000\\255\\000\\003\\000\\015\\006\\311\\000\\256\\000\\241\\000\\004\\000\\030\\006\\276\\000\\257\\000\\241\\000\\005\\000#\\006\\263\\000\\260\\000\\241\\000\\006\\000,\\006\\252\\000\\261\\000\\262\\000\\007\\0007\\006\\237\\000\\263\\000\\264\\000\\010\\000:\\006\\234\\000\\265\\000\\266\\000\\011\\000A\\006\\225\\000\\267\\000\\270\\000\\012\\000\\240\\0066\\000\\271\\000\\272\\000\\013\\000\\351\\005\\355\\000\\273\\000\\241\\000\\014\\000\\302\\006\\024\\000\\274\\000\\275\\000\\017\\001\\251\\005-\\000\\276\\000\\277\\000\\020\\001\\270\\005\\036\\000\\300\\000\\301\\000\\021\\002+\\004\\253\\000\\302\\000\\241\\000\\022\\002\\310\\004\\016\\000\\303\\000\\301\\000\\023\\002\\366\\003\\340\\000\\304\\000\\305\\000\\024\\003j\\003l\\000\\306\\000\\305\\000\\025\\003m\\003i\\000\\307\\000\\305\\000\\026\\003q\\003e\\000\\310\\000\\241\\000\\027\\003u\\003a\\000\\311\\000\\241\\000\\030\\003y\\003]\\000\\312\\000\\241\\000\\031\\006\\254\\000*\\000\\313\\000\\301\\000\\032\\000\\314\\000\\000\\000\\014\\000\\001\\000\\302\\006\\024\\000\\274\\000\\315\\000\\017\\000\\316\\000\\000\\002\\345\\0001\\377\\000c\\000\\013\\007\\000\\317\\007\\000\\320\\007\\000\\321\\007\\000\\322\\007\\000\\320\\007\\000\\320\\007\\000\\320\\007\\000\\323\\007\\000\\324\\007\\000\\325\\007\\000\\326\\000\\001\\007\\000\\327\\\\\\007\\000\\330\\034\\377\\000\\031\\000\\014\\007\\000\\317\\007\\000\\320\\007\\000\\321\\007\\000\\322\\007\\000\\320\\007\\000\\320\\007\\000\\320\\007\\000\\323\\007\\000\\324\\007\\000\\325\\007\\000\\326\\007\\000\\331\\000\\001\\007\\000\\332\\001\\377\\000/\\000\\020\\007\\000\\317\\007\\000\\320\\007\\000\\321\\007\\000\\322\\007\\000\\320\\007\\000\\320\\007\\000\\320\\007\\000\\323\\007\\000\\324\\007\\000\\325\\007\\000\\326\\007\\000\\331\\007\\000\\320\\000\\000\\007\\000\\333\\000\\000\\030\\373\\000L\\011\\377\\000"\\000\\020\\007\\000\\317\\007\\000\\320\\007\\000\\321\\007\\000\\322\\007\\000\\320\\007\\000\\320\\007\\000\\320\\007\\000\\323\\007\\000\\324\\007\\000\\325\\007\\000\\326\\007\\000\\331\\000\\000\\000\\007\\000\\333\\000\\001\\007\\000\\330\\377\\000\\034\\000\\020\\007\\000\\317\\007\\000\\320\\007\\000\\321\\007\\000\\322\\007\\000\\320\\007\\000\\320\\007\\000\\320\\007\\000\\323\\007\\000\\324\\007\\000\\325\\007\\000\\326\\007\\000\\331\\007\\000\\320\\000\\000\\007\\000\\333\\000\\000\\377\\000<\\000\\022\\007\\000\\317\\007\\000\\320\\007\\000\\321\\007\\000\\322\\007\\000\\320\\007\\000\\320\\007\\000\\320\\007\\000\\323\\007\\000\\324\\007\\000\\325\\007\\000\\326\\007\\000\\331\\007\\000\\320\\000\\000\\007\\000\\333\\007\\000\\334\\007\\000>\\000\\001\\007\\000\\327\\\\\\007\\000\\330\\034\\374\\000+\\007\\000\\320\\020\\020\\015\\377\\000\\214\\000\\026\\007\\000\\317\\007\\000\\320\\007\\000\\321\\007\\000\\322\\007\\000\\320\\007\\000\\320\\007\\000\\320\\007\\000\\323\\007\\000\\324\\007\\000\\325\\007\\000\\326\\007\\000\\331\\007\\000\\320\\000\\000\\007\\000\\333\\007\\000\\334\\007\\000>\\007\\000\\320\\007\\000>\\000\\007\\000\\320\\000\\001\\007\\000\\335\\377\\000*\\000\\025\\007\\000\\317\\007\\000\\320\\007\\000\\321\\007\\000\\322\\007\\000\\320\\007\\000\\320\\007\\000\\320\\007\\000\\323\\007\\000\\324\\007\\000\\325\\007\\000\\326\\007\\000\\331\\007\\000\\320\\000\\000\\007\\000\\333\\007\\000\\334\\007\\000>\\007\\000\\320\\007\\000>\\001\\000\\000\\377\\000\\002\\000\\024\\007\\000\\317\\007\\000\\320\\007\\000\\321\\007\\000\\322\\007\\000\\320\\007\\000\\320\\007\\000\\320\\007\\000\\323\\007\\000\\324\\007\\000\\325\\007\\000\\326\\007\\000\\331\\007\\000\\320\\000\\000\\007\\000\\333\\007\\000\\334\\007\\000>\\007\\000\\320\\007\\000>\\000\\001\\007\\000\\327\\\\\\007\\000\\330\\374\\000\\034\\001\\377\\000 \\000\\032\\007\\000\\317\\007\\000\\320\\007\\000\\321\\007\\000\\322\\007\\000\\320\\007\\000\\320\\007\\000\\320\\007\\000\\323\\007\\000\\324\\007\\000\\325\\007\\000\\326\\007\\000\\331\\007\\000\\320\\000\\000\\007\\000\\333\\007\\000\\334\\007\\000>\\007\\000\\320\\007\\000>\\001\\001\\001\\007\\000\\320\\007\\000\\320\\007\\000\\320\\000\\001\\007\\000\\336\\027t\\007\\000\\327\\\\\\007\\000\\330\\034V\\007\\000\\332\\0019)\\031B\\007\\000\\330\\034\\373\\000j!)!!!!%H\\007\\000\\336\\027\\011\\377\\000m\\000\\033\\007\\000\\317\\007\\000\\320\\007\\000\\321\\007\\000\\322\\007\\000\\320\\007\\000\\320\\007\\000\\320\\007\\000\\323\\007\\000\\324\\007\\000\\325\\007\\000\\326\\007\\000\\331\\007\\000\\320\\000\\000\\007\\000\\333\\007\\000\\334\\007\\000>\\007\\000\\320\\007\\000>\\001\\001\\001\\007\\000\\320\\007\\000\\320\\007\\000\\320\\007\\000>\\000\\001\\007\\000\\327I\\007\\000\\330\\006\\000\\001\\000\\337\\000\\340\\000\\001\\000\\223\\000\\000\\0006\\000\\001\\000\\002\\000\\000\\000\\002+\\260\\000\\000\\000\\002\\000\\224\\000\\000\\000\\006\\000\\001\\000\\000\\001)\\000\\225\\000\\000\\000\\026\\000\\002\\000\\000\\000\\002\\000\\226\\000\\227\\000\\000\\000\\000\\000\\002\\000\\341\\000\\342\\000\\001\\000\\001\\000\\343\\000\\344\\000\\001\\000\\223\\000\\000\\000=\\000\\003\\000\\002\\000\\000\\000\\011\\273\\000\\212Y+\\267\\000\\213\\260\\000\\000\\000\\002\\000\\224\\000\\000\\000\\006\\000\\001\\000\\000\\001.\\000\\225\\000\\000\\000\\026\\000\\002\\000\\000\\000\\011\\000\\226\\000\\227\\000\\000\\000\\000\\000\\011\\000\\341\\000\\342\\000\\001\\000\\001\\000\\345\\000\\346\\000\\001\\000\\223\\000\\000\\0006\\000\\001\\000\\002\\000\\000\\000\\002+\\260\\000\\000\\000\\002\\000\\224\\000\\000\\000\\006\\000\\001\\000\\000\\0013\\000\\225\\000\\000\\000\\026\\000\\002\\000\\000\\000\\002\\000\\226\\000\\227\\000\\000\\000\\000\\000\\002\\000\\251\\000\\241\\000\\001\\000\\001\\000\\347\\000\\350\\000\\001\\000\\223\\000\\000\\0005\\000\\000\\000\\002\\000\\000\\000\\001\\261\\000\\000\\000\\002\\000\\224\\000\\000\\000\\006\\000\\001\\000\\000\\0018\\000\\225\\000\\000\\000\\026\\000\\002\\000\\000\\000\\001\\000\\226\\000\\227\\000\\000\\000\\000\\000\\001\\000\\251\\000\\241\\000\\001\\000\\001\\000\\351\\000\\000\\000\\002\\000\\352
7	pl.umk.mat.zawodyweb.compiler.classes.LanguageOPSS	1	OPSS Compiler	\\312\\376\\272\\276\\000\\000\\0002\\001e\\012\\000h\\000\\305\\011\\000g\\000\\306\\007\\000\\307\\012\\000\\003\\000\\310\\010\\000\\311\\010\\000\\312\\012\\000\\313\\000\\314\\010\\000\\315\\007\\000\\316\\012\\000\\011\\000\\305\\012\\000\\011\\000\\317\\010\\000\\320\\010\\000\\321\\012\\000\\322\\000\\323\\012\\000\\011\\000\\324\\007\\000\\325\\012\\000\\020\\000\\310\\007\\000\\326\\010\\000\\327\\010\\000\\330\\012\\000\\022\\000\\331\\010\\000\\332\\010\\000\\333\\012\\000\\020\\000\\334\\012\\000\\011\\000\\335\\007\\000\\336\\012\\000\\003\\000\\337\\012\\000\\032\\000\\340\\012\\000\\003\\000\\341\\010\\000\\342\\012\\000\\003\\000\\343\\012\\000\\020\\000\\344\\007\\000\\345\\012\\000!\\000\\340\\010\\000\\346\\010\\000\\347\\010\\000\\350\\010\\000\\351\\010\\000\\352\\010\\000\\353\\012\\000\\354\\000\\355\\010\\000\\356\\010\\000\\357\\010\\000\\360\\010\\000\\361\\010\\000\\362\\012\\000\\020\\000\\363\\007\\000\\364\\007\\000\\365\\010\\000\\366\\012\\0001\\000\\367\\012\\0000\\000\\370\\007\\000\\371\\010\\000\\372\\012\\000\\373\\000\\374\\012\\0000\\000\\375\\012\\000\\373\\000\\376\\012\\000\\377\\001\\000\\010\\001\\001\\012\\000\\377\\001\\002\\010\\001\\003\\012\\000e\\001\\004\\005\\000\\000\\000\\000\\000\\000\\033X\\012\\001\\005\\001\\006\\007\\001\\007\\012\\000B\\000\\340\\010\\001\\010\\007\\001\\011\\007\\001\\012\\012\\000F\\000\\305\\010\\001\\013\\012\\000F\\001\\014\\012\\000F\\001\\015\\012\\000E\\000\\310\\012\\000E\\000\\363\\012\\000E\\000\\344\\010\\001\\016\\010\\001\\017\\012\\000\\003\\001\\020\\010\\001\\021\\012\\000e\\001\\022\\012\\000\\354\\001\\023\\010\\001\\024\\012\\000e\\001\\025\\012\\001\\026\\001\\027\\012\\000\\003\\001\\030\\010\\001\\031\\012\\000\\003\\001\\032\\010\\001\\033\\010\\001\\034\\010\\001\\035\\010\\001\\036\\010\\001\\037\\010\\001 \\010\\001!\\010\\001"\\010\\001#\\010\\001$\\010\\001%\\007\\001&\\012\\000e\\001'\\007\\001(\\007\\001)\\007\\001*\\001\\000\\012properties\\001\\000\\026Ljava/util/Properties;\\001\\000\\006<init>\\001\\000\\003()V\\001\\000\\004Code\\001\\000\\017LineNumberTable\\001\\000\\022LocalVariableTable\\001\\000\\004this\\001\\0004Lpl/umk/mat/zawodyweb/compiler/classes/LanguageOPSS;\\001\\000\\015setProperties\\001\\000\\031(Ljava/util/Properties;)V\\001\\000\\007runTest\\001\\000e(Ljava/lang/String;Lpl/umk/mat/zawodyweb/checker/TestInput;)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\001e\\001\\000-Lorg/apache/commons/httpclient/HttpException;\\001\\000\\025Ljava/io/IOException;\\001\\000&Ljava/io/UnsupportedEncodingException;\\001\\000\\001p\\001\\000\\031Ljava/util/regex/Pattern;\\001\\000\\001m\\001\\000\\031Ljava/util/regex/Matcher;\\001\\000\\002p1\\001\\000\\002m1\\001\\000 Ljava/lang/InterruptedException;\\001\\000\\001i\\001\\000\\001I\\001\\000\\002p2\\001\\000\\002m2\\001\\000\\006answer\\001\\0001Lorg/apache/commons/httpclient/methods/GetMethod;\\001\\000\\014answerStream\\001\\000\\025Ljava/io/InputStream;\\001\\000\\003row\\001\\000\\022Ljava/lang/String;\\001\\000\\003col\\001\\000\\023[Ljava/lang/String;\\001\\000\\004path\\001\\000\\005input\\001\\000(Lpl/umk/mat/zawodyweb/checker/TestInput;\\001\\000\\006result\\001\\000)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\010loginUrl\\001\\000\\005login\\001\\000\\010password\\001\\000\\006client\\001\\000*Lorg/apache/commons/httpclient/HttpClient;\\001\\000\\006params\\001\\0007Lorg/apache/commons/httpclient/params/HttpClientParams;\\001\\000\\007logging\\001\\0002Lorg/apache/commons/httpclient/methods/PostMethod;\\001\\000\\013dataLogging\\001\\000.[Lorg/apache/commons/httpclient/NameValuePair;\\001\\000\\012sendAnswer\\001\\000\\016dataSendAnswer\\001\\000\\006status\\001\\000\\010submitId\\001\\000\\002br\\001\\000\\030Ljava/io/BufferedReader;\\001\\000\\004line\\001\\000\\006logout\\001\\000\\015StackMapTable\\007\\001(\\007\\001&\\007\\001+\\007\\000\\307\\007\\000\\316\\007\\001,\\007\\000\\325\\007\\000\\235\\007\\000\\336\\007\\000\\345\\007\\001-\\007\\000\\364\\007\\000\\371\\007\\001.\\007\\001/\\007\\001\\007\\007\\001\\011\\007\\000\\215\\001\\000\\012precompile\\001\\000\\006([B)[B\\001\\000\\004code\\001\\000\\002[B\\001\\000\\007compile\\001\\000\\026([B)Ljava/lang/String;\\001\\000\\013postcompile\\001\\000&(Ljava/lang/String;)Ljava/lang/String;\\001\\000\\014closeProgram\\001\\000\\025(Ljava/lang/String;)V\\001\\000\\012SourceFile\\001\\000\\021LanguageOPSS.java\\014\\000l\\000m\\014\\000j\\000k\\001\\000'pl/umk/mat/zawodyweb/checker/TestOutput\\014\\000l\\000\\302\\001\\000\\037http://opss.assecobs.pl/?&login\\001\\000\\012opss.login\\007\\0010\\014\\0011\\000\\300\\001\\000\\015opss.password\\001\\000(org/apache/commons/httpclient/HttpClient\\014\\0012\\0013\\001\\000\\016http.useragent\\001\\000/Opera/9.64 (Windows NT 6.0; U; pl) Presto/2.1.1\\007\\001,\\014\\0014\\0015\\014\\0016\\0017\\001\\0000org/apache/commons/httpclient/methods/PostMethod\\001\\000+org/apache/commons/httpclient/NameValuePair\\001\\000\\021login_form_submit\\001\\000\\0011\\014\\000l\\0018\\001\\000\\020login_form_login\\001\\000\\017login_form_pass\\014\\0019\\001:\\014\\001;\\001<\\001\\000+org/apache/commons/httpclient/HttpException\\014\\001=\\001>\\014\\001?\\001@\\014\\001A\\000\\302\\001\\000\\015HttpException\\014\\001B\\000\\302\\014\\001C\\000m\\001\\000\\023java/io/IOException\\001\\000\\013IOException\\001\\0002http://opss.assecobs.pl/?menu=comp&sub=send&comp=0\\001\\000\\020form_send_submit\\001\\000\\016form_send_comp\\001\\000\\000\\001\\000\\021form_send_problem\\007\\001+\\014\\001D\\001@\\001\\000\\016form_send_lang\\001\\000\\022CODEFILE_EXTENSION\\001\\000\\024form_send_sourcetext\\001\\000\\023form_send_submittxt\\001\\000\\013Wy\\305\\233lij kod\\014\\001E\\001F\\001\\000\\026java/io/BufferedReader\\001\\000\\031java/io/InputStreamReader\\001\\000\\012iso-8859-2\\014\\000l\\001G\\014\\000l\\001H\\001\\000$java/io/UnsupportedEncodingException\\001\\000*<a[^>]*href=[^>]*sub=stat[^>]*id=\\\\d*[^>]*>\\007\\001.\\014\\000\\275\\001I\\014\\001J\\001@\\014\\001K\\001L\\007\\001/\\014\\001M\\001N\\001\\000\\006id=\\\\d*\\014\\001O\\001@\\001\\000\\001=\\014\\001P\\001Q\\007\\001R\\014\\001S\\001T\\001\\000\\036java/lang/InterruptedException\\001\\000\\024InterruptedException\\001\\000/org/apache/commons/httpclient/methods/GetMethod\\001\\000\\027java/lang/StringBuilder\\001\\0006http://opss.assecobs.pl/?menu=comp&sub=stat&comp=0&id=\\014\\001U\\001V\\014\\001W\\001@\\001\\000<(<tr class=row0>)|(<tr class=stat_ac>)|(<tr class=stat_run>)\\001\\000\\021(<td>)|(</table>)\\014\\001X\\001>\\001\\000\\031.*Program zaakceptowany.*\\014\\001Y\\001Z\\014\\001[\\001\\\\\\001\\000\\002\\\\.\\014\\001]\\001^\\007\\001_\\014\\001`\\001a\\014\\001b\\001>\\001\\000\\002\\\\s\\014\\001c\\001>\\001\\000\\025.*B\\305\\202\\304\\205d kompilacji.*\\001\\000\\024.*B\\305\\202\\304\\205d wykonania.*\\001\\000\\034.*Limit czasu przekroczony.*\\001\\000\\037.*Limit pami\\304\\231ci przekroczony.*\\001\\000\\027.*B\\305\\202\\304\\231dna odpowied\\305\\272.*\\001\\000\\030.*Niedozwolona funkcja.*\\001\\000\\020.*Uruchamianie.*\\001\\000\\016.*Kompilacja.*\\001\\000\\021Unknown status: "\\001\\000\\001"\\001\\000\\037http://opss.assecobs.pl/?logoff\\001\\000\\020java/lang/String\\014\\000l\\001d\\001\\0002pl/umk/mat/zawodyweb/compiler/classes/LanguageOPSS\\001\\000\\020java/lang/Object\\001\\000/pl/umk/mat/zawodyweb/compiler/CompilerInterface\\001\\000&pl/umk/mat/zawodyweb/checker/TestInput\\001\\0005org/apache/commons/httpclient/params/HttpClientParams\\001\\000\\023java/io/InputStream\\001\\000\\027java/util/regex/Pattern\\001\\000\\027java/util/regex/Matcher\\001\\000\\024java/util/Properties\\001\\000\\013getProperty\\001\\000\\011getParams\\001\\0009()Lorg/apache/commons/httpclient/params/HttpClientParams;\\001\\000\\014setParameter\\001\\000'(Ljava/lang/String;Ljava/lang/Object;)V\\001\\000\\011setParams\\001\\000:(Lorg/apache/commons/httpclient/params/HttpClientParams;)V\\001\\000'(Ljava/lang/String;Ljava/lang/String;)V\\001\\000\\016setRequestBody\\001\\0001([Lorg/apache/commons/httpclient/NameValuePair;)V\\001\\000\\015executeMethod\\001\\000-(Lorg/apache/commons/httpclient/HttpMethod;)I\\001\\000\\011setResult\\001\\000\\004(I)V\\001\\000\\012getMessage\\001\\000\\024()Ljava/lang/String;\\001\\000\\015setResultDesc\\001\\000\\007setText\\001\\000\\021releaseConnection\\001\\000\\007getText\\001\\000\\027getResponseBodyAsStream\\001\\000\\027()Ljava/io/InputStream;\\001\\000*(Ljava/io/InputStream;Ljava/lang/String;)V\\001\\000\\023(Ljava/io/Reader;)V\\001\\000-(Ljava/lang/String;)Ljava/util/regex/Pattern;\\001\\000\\010readLine\\001\\000\\007matcher\\001\\0003(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;\\001\\000\\004find\\001\\000\\003()Z\\001\\000\\005group\\001\\000\\005split\\001\\000'(Ljava/lang/String;)[Ljava/lang/String;\\001\\000\\020java/lang/Thread\\001\\000\\005sleep\\001\\000\\004(J)V\\001\\000\\006append\\001\\000-(Ljava/lang/String;)Ljava/lang/StringBuilder;\\001\\000\\010toString\\001\\000\\011setPoints\\001\\000\\007matches\\001\\000\\025(Ljava/lang/String;)Z\\001\\000\\014getMaxPoints\\001\\000\\003()I\\001\\000\\012replaceAll\\001\\0008(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;\\001\\000\\021java/lang/Integer\\001\\000\\010parseInt\\001\\000\\025(Ljava/lang/String;)I\\001\\000\\012setRuntime\\001\\000\\012setMemUsed\\001\\000\\005([B)V\\000!\\000g\\000h\\000\\001\\000i\\000\\001\\000\\002\\000j\\000k\\000\\000\\000\\007\\000\\001\\000l\\000m\\000\\001\\000n\\000\\000\\000/\\000\\001\\000\\001\\000\\000\\000\\005*\\267\\000\\001\\261\\000\\000\\000\\002\\000o\\000\\000\\000\\006\\000\\001\\000\\000\\000\\036\\000p\\000\\000\\000\\014\\000\\001\\000\\000\\000\\005\\000q\\000r\\000\\000\\000\\001\\000s\\000t\\000\\001\\000n\\000\\000\\000>\\000\\002\\000\\002\\000\\000\\000\\006*+\\265\\000\\002\\261\\000\\000\\000\\002\\000o\\000\\000\\000\\012\\000\\002\\000\\000\\000$\\000\\005\\000%\\000p\\000\\000\\000\\026\\000\\002\\000\\000\\000\\006\\000q\\000r\\000\\000\\000\\000\\000\\006\\000j\\000k\\000\\001\\000\\001\\000u\\000v\\000\\001\\000n\\000\\000\\013D\\000\\010\\000\\027\\000\\000\\004\\271\\273\\000\\003Y\\001\\267\\000\\004N\\022\\005:\\004*\\264\\000\\002\\022\\006\\266\\000\\007:\\005*\\264\\000\\002\\022\\010\\266\\000\\007:\\006\\273\\000\\011Y\\267\\000\\012:\\007\\031\\007\\266\\000\\013:\\010\\031\\010\\022\\014\\022\\015\\266\\000\\016\\031\\007\\031\\010\\266\\000\\017\\273\\000\\020Y\\031\\004\\267\\000\\021:\\011\\006\\275\\000\\022Y\\003\\273\\000\\022Y\\022\\023\\022\\024\\267\\000\\025SY\\004\\273\\000\\022Y\\022\\026\\031\\005\\267\\000\\025SY\\005\\273\\000\\022Y\\022\\027\\031\\006\\267\\000\\025S:\\012\\031\\011\\031\\012\\266\\000\\030\\031\\007\\031\\011\\266\\000\\031W\\247\\000=:\\013-\\002\\266\\000\\033-\\031\\013\\266\\000\\034\\266\\000\\035-\\022\\036\\266\\000\\037\\031\\011\\266\\000 -\\260:\\013-\\002\\266\\000\\033-\\031\\013\\266\\000"\\266\\000\\035-\\022#\\266\\000\\037\\031\\011\\266\\000 -\\260\\031\\011\\266\\000 \\273\\000\\020Y\\022$\\267\\000\\021:\\013\\020\\006\\275\\000\\022Y\\003\\273\\000\\022Y\\022%\\022\\024\\267\\000\\025SY\\004\\273\\000\\022Y\\022&\\022'\\267\\000\\025SY\\005\\273\\000\\022Y\\022(,\\266\\000)\\267\\000\\025SY\\006\\273\\000\\022Y\\022**\\264\\000\\002\\022+\\266\\000\\007\\267\\000\\025SY\\007\\273\\000\\022Y\\022,+\\267\\000\\025SY\\010\\273\\000\\022Y\\022-\\022.\\267\\000\\025S:\\014\\031\\013\\031\\014\\266\\000\\030\\001:\\015\\031\\007\\031\\013\\266\\000\\031W\\031\\013\\266\\000/:\\015\\247\\000=:\\016-\\002\\266\\000\\033-\\031\\016\\266\\000\\034\\266\\000\\035-\\022\\036\\266\\000\\037\\031\\013\\266\\000 -\\260:\\016-\\002\\266\\000\\033-\\031\\016\\266\\000"\\266\\000\\035-\\022#\\266\\000\\037\\031\\013\\266\\000 -\\260\\001:\\016\\001:\\017\\273\\0000Y\\273\\0001Y\\031\\015\\0222\\267\\0003\\267\\0004:\\017\\247\\000\\005:\\020\\0226\\270\\0007:\\021\\031\\017\\266\\0008:\\020\\031\\021\\031\\020\\266\\0009:\\022\\031\\022\\266\\000:\\232\\000\\033\\031\\020\\306\\000\\026\\031\\017\\266\\0008:\\020\\031\\021\\031\\020\\266\\0009:\\022\\247\\377\\343\\022;\\270\\0007:\\023\\031\\023\\031\\022\\266\\000<\\266\\0009:\\024\\031\\024\\266\\000:W\\031\\024\\266\\000<\\022=\\266\\000>\\0042:\\016\\247\\000 :\\021-\\002\\266\\000\\033-\\031\\021\\266\\000"\\266\\000\\035-\\022#\\266\\000\\037\\031\\013\\266\\000 -\\260\\031\\013\\266\\000 \\024\\000?\\270\\000A\\247\\000\\033:\\021-\\002\\266\\000\\033-\\031\\021\\266\\000C\\266\\000\\035-\\022D\\266\\000\\037-\\260\\273\\000EY\\273\\000FY\\267\\000G\\022H\\266\\000I\\031\\016\\266\\000I\\266\\000J\\267\\000K:\\021\\001:\\022\\031\\007\\031\\021\\266\\000\\031W\\031\\021\\266\\000L:\\022\\247\\000=:\\023-\\002\\266\\000\\033-\\031\\023\\266\\000\\034\\266\\000\\035-\\022\\036\\266\\000\\037\\031\\021\\266\\000M-\\260:\\023-\\002\\266\\000\\033-\\031\\023\\266\\000"\\266\\000\\035-\\022#\\266\\000\\037\\031\\021\\266\\000M-\\260\\273\\0000Y\\273\\0001Y\\031\\022\\0222\\267\\0003\\267\\0004:\\017\\247\\000\\005:\\023\\022':\\023\\031\\017\\266\\0008:\\020\\022N\\270\\0007:\\024\\031\\024\\031\\020\\266\\0009:\\025\\031\\025\\266\\000:\\232\\000\\033\\031\\020\\306\\000\\026\\031\\017\\266\\0008:\\020\\031\\024\\031\\020\\266\\0009:\\025\\247\\377\\343\\022':\\023\\0036\\026\\025\\026\\020\\023\\242\\000&\\273\\000FY\\267\\000G\\031\\023\\266\\000I\\031\\020\\266\\000I\\266\\000J:\\023\\031\\017\\266\\0008:\\020\\204\\026\\001\\247\\377\\331\\247\\000 :\\024-\\002\\266\\000\\033-\\031\\024\\266\\000"\\266\\000\\035-\\022#\\266\\000\\037\\031\\013\\266\\000 -\\260\\031\\023\\022O\\266\\000>:\\024-\\003\\266\\000P\\031\\024\\020\\0062\\022Q\\266\\000R\\231\\000<-\\003\\266\\000\\033-,\\266\\000S\\266\\000P-\\020\\012\\031\\024\\020\\0102\\022T\\022'\\266\\000U\\270\\000Vh\\266\\000W-\\031\\024\\020\\0112\\022X\\022'\\266\\000U\\270\\000V\\266\\000Y\\247\\000\\323\\031\\024\\020\\0062\\022Z\\266\\000R\\231\\000\\013-\\006\\266\\000\\033\\247\\000\\276\\031\\024\\020\\0062\\022[\\266\\000R\\231\\000\\013-\\007\\266\\000\\033\\247\\000\\251\\031\\024\\020\\0062\\022\\\\\\266\\000R\\231\\000\\013-\\005\\266\\000\\033\\247\\000\\224\\031\\024\\020\\0062\\022]\\266\\000R\\231\\000\\013-\\010\\266\\000\\033\\247\\000\\177\\031\\024\\020\\0062\\022^\\266\\000R\\231\\000\\013-\\004\\266\\000\\033\\247\\000j\\031\\024\\020\\0062\\022_\\266\\000R\\231\\000\\014-\\020\\006\\266\\000\\033\\247\\000L\\031\\024\\020\\0062\\022`\\266\\000R\\231\\000\\006\\247\\000<\\031\\024\\020\\0062\\022a\\266\\000R\\231\\000\\006\\247\\000,-\\020\\010\\266\\000\\033-\\273\\000FY\\267\\000G\\022b\\266\\000I\\031\\024\\020\\0062\\266\\000I\\022c\\266\\000I\\266\\000J\\266\\000\\035\\247\\000\\013\\031\\021\\266\\000M\\247\\375\\255\\273\\000EY\\022d\\267\\000K:\\021\\031\\007\\031\\021\\266\\000\\031W\\247\\000\\024:\\022\\031\\021\\266\\000M\\247\\000\\012:\\022\\031\\021\\266\\000M\\031\\021\\266\\000M-\\260\\000\\015\\000\\205\\000\\215\\000\\220\\000\\032\\000\\205\\000\\215\\000\\255\\000!\\001G\\001V\\001Y\\000\\032\\001G\\001V\\001v\\000!\\001\\231\\001\\255\\001\\260\\0005\\001\\262\\002\\020\\002\\023\\000!\\0025\\002;\\002>\\000B\\002v\\002\\205\\002\\210\\000\\032\\002v\\002\\205\\002\\245\\000!\\002\\302\\002\\326\\002\\331\\0005\\002\\337\\003G\\003J\\000!\\004\\226\\004\\236\\004\\241\\000\\032\\004\\226\\004\\236\\004\\253\\000!\\000\\003\\000o\\000\\000\\002r\\000\\234\\000\\000\\000)\\000\\011\\000+\\000\\015\\000,\\000\\030\\000-\\000#\\000/\\000,\\0001\\0003\\0002\\000<\\0003\\000C\\0005\\000N\\0006\\000~\\000;\\000\\205\\000=\\000\\215\\000J\\000\\220\\000>\\000\\222\\000?\\000\\227\\000@\\000\\240\\000A\\000\\246\\000B\\000\\253\\000C\\000\\255\\000D\\000\\257\\000E\\000\\264\\000F\\000\\275\\000G\\000\\303\\000H\\000\\310\\000I\\000\\312\\000K\\000\\317\\000L\\000\\332\\000M\\001=\\000U\\001D\\000V\\001G\\000X\\001O\\000Y\\001V\\000f\\001Y\\000Z\\001[\\000[\\001`\\000\\\\\\001i\\000]\\001o\\000^\\001t\\000_\\001v\\000`\\001x\\000a\\001}\\000b\\001\\206\\000c\\001\\214\\000d\\001\\221\\000e\\001\\223\\000g\\001\\226\\000h\\001\\231\\000j\\001\\255\\000l\\001\\260\\000k\\001\\262\\000o\\001\\271\\000p\\001\\300\\000q\\001\\311\\000r\\001\\326\\000s\\001\\335\\000t\\001\\351\\000v\\001\\360\\000w\\001\\374\\000x\\002\\002\\000y\\002\\020\\000\\200\\002\\023\\000z\\002\\025\\000{\\002\\032\\000|\\002#\\000}\\002)\\000~\\002.\\000\\177\\0020\\000\\201\\0025\\000\\204\\002;\\000\\212\\002>\\000\\205\\002@\\000\\206\\002E\\000\\207\\002N\\000\\210\\002T\\000\\211\\002V\\000\\213\\002s\\000\\214\\002v\\000\\216\\002~\\000\\217\\002\\205\\000\\234\\002\\210\\000\\220\\002\\212\\000\\221\\002\\217\\000\\222\\002\\230\\000\\223\\002\\236\\000\\224\\002\\243\\000\\225\\002\\245\\000\\226\\002\\247\\000\\227\\002\\254\\000\\230\\002\\265\\000\\231\\002\\273\\000\\232\\002\\300\\000\\233\\002\\302\\000\\236\\002\\326\\000\\240\\002\\331\\000\\237\\002\\333\\000\\241\\002\\337\\000\\243\\002\\346\\000\\244\\002\\355\\000\\245\\002\\366\\000\\246\\003\\003\\000\\247\\003\\012\\000\\250\\003\\026\\000\\252\\003\\032\\000\\253\\003$\\000\\254\\003:\\000\\255\\003A\\000\\253\\003G\\000\\265\\003J\\000\\257\\003L\\000\\260\\003Q\\000\\261\\003Z\\000\\262\\003`\\000\\263\\003e\\000\\264\\003g\\000\\266\\003p\\000\\267\\003u\\000\\270\\003\\202\\000\\271\\003\\207\\000\\272\\003\\217\\000\\273\\003\\245\\000\\274\\003\\270\\000\\275\\003\\273\\000\\276\\003\\310\\000\\277\\003\\315\\000\\300\\003\\320\\000\\301\\003\\335\\000\\302\\003\\342\\000\\303\\003\\345\\000\\304\\003\\362\\000\\305\\003\\367\\000\\306\\003\\372\\000\\307\\004\\007\\000\\310\\004\\014\\000\\311\\004\\017\\000\\312\\004\\034\\000\\313\\004!\\000\\314\\004$\\000\\315\\0041\\000\\316\\004:\\000\\317\\004J\\000\\320\\004Z\\000\\322\\004`\\000\\323\\004\\200\\000\\324\\004\\203\\000\\326\\004\\210\\000\\327\\004\\213\\000\\330\\004\\226\\000\\332\\004\\236\\000\\337\\004\\241\\000\\333\\004\\243\\000\\334\\004\\250\\000\\337\\004\\253\\000\\335\\004\\255\\000\\336\\004\\262\\000\\340\\004\\267\\000\\341\\000p\\000\\000\\001\\246\\000*\\000\\222\\000\\033\\000w\\000x\\000\\013\\000\\257\\000\\033\\000w\\000y\\000\\013\\001[\\000\\033\\000w\\000x\\000\\016\\001x\\000\\033\\000w\\000y\\000\\016\\001\\262\\000\\000\\000w\\000z\\000\\020\\001\\271\\000W\\000{\\000|\\000\\021\\001\\311\\000G\\000}\\000~\\000\\022\\001\\360\\000 \\000\\177\\000|\\000\\023\\001\\374\\000\\024\\000\\200\\000~\\000\\024\\002\\025\\000\\033\\000w\\000y\\000\\021\\002@\\000\\026\\000w\\000\\201\\000\\021\\002\\212\\000\\033\\000w\\000x\\000\\023\\002\\247\\000\\033\\000w\\000y\\000\\023\\002\\333\\000\\000\\000w\\000z\\000\\023\\003\\035\\000*\\000\\202\\000\\203\\000\\026\\002\\355\\000Z\\000\\204\\000|\\000\\024\\002\\366\\000Q\\000\\205\\000~\\000\\025\\003L\\000\\033\\000w\\000y\\000\\024\\002s\\002\\025\\000\\206\\000\\207\\000\\021\\002v\\002\\022\\000\\210\\000\\211\\000\\022\\002\\337\\001\\251\\000\\212\\000\\213\\000\\023\\003p\\001\\030\\000\\214\\000\\215\\000\\024\\004\\243\\000\\005\\000w\\000x\\000\\022\\004\\255\\000\\005\\000w\\000y\\000\\022\\000\\000\\004\\271\\000q\\000r\\000\\000\\000\\000\\004\\271\\000\\216\\000\\213\\000\\001\\000\\000\\004\\271\\000\\217\\000\\220\\000\\002\\000\\011\\004\\260\\000\\221\\000\\222\\000\\003\\000\\015\\004\\254\\000\\223\\000\\213\\000\\004\\000\\030\\004\\241\\000\\224\\000\\213\\000\\005\\000#\\004\\226\\000\\225\\000\\213\\000\\006\\000,\\004\\215\\000\\226\\000\\227\\000\\007\\0003\\004\\206\\000\\230\\000\\231\\000\\010\\000N\\004k\\000\\232\\000\\233\\000\\011\\000~\\004;\\000\\234\\000\\235\\000\\012\\000\\332\\003\\337\\000\\236\\000\\233\\000\\013\\001=\\003|\\000\\237\\000\\235\\000\\014\\001G\\003r\\000\\240\\000\\211\\000\\015\\001\\226\\003#\\000\\241\\000\\213\\000\\016\\001\\231\\003 \\000\\242\\000\\243\\000\\017\\001\\300\\002\\371\\000\\244\\000\\213\\000\\020\\004\\226\\000#\\000\\245\\000\\207\\000\\021\\000\\246\\000\\000\\001\\355\\000(\\377\\000\\220\\000\\013\\007\\000\\247\\007\\000\\250\\007\\000\\251\\007\\000\\252\\007\\000\\250\\007\\000\\250\\007\\000\\250\\007\\000\\253\\007\\000\\254\\007\\000\\255\\007\\000\\256\\000\\001\\007\\000\\257\\\\\\007\\000\\260\\034\\377\\000\\216\\000\\016\\007\\000\\247\\007\\000\\250\\007\\000\\251\\007\\000\\252\\007\\000\\250\\007\\000\\250\\007\\000\\250\\007\\000\\253\\007\\000\\254\\007\\000\\255\\007\\000\\256\\007\\000\\255\\007\\000\\256\\007\\000\\261\\000\\001\\007\\000\\257\\\\\\007\\000\\260\\034\\377\\000\\034\\000\\020\\007\\000\\247\\007\\000\\250\\007\\000\\251\\007\\000\\252\\007\\000\\250\\007\\000\\250\\007\\000\\250\\007\\000\\253\\007\\000\\254\\007\\000\\255\\007\\000\\256\\007\\000\\255\\007\\000\\256\\007\\000\\261\\007\\000\\250\\007\\000\\262\\000\\001\\007\\000\\263\\001\\376\\000\\026\\007\\000\\250\\007\\000\\264\\007\\000\\265\\037\\377\\000)\\000\\020\\007\\000\\247\\007\\000\\250\\007\\000\\251\\007\\000\\252\\007\\000\\250\\007\\000\\250\\007\\000\\250\\007\\000\\253\\007\\000\\254\\007\\000\\255\\007\\000\\256\\007\\000\\255\\007\\000\\256\\007\\000\\261\\007\\000\\250\\007\\000\\262\\000\\001\\007\\000\\260\\374\\000\\034\\007\\000\\250\\004H\\007\\000\\266\\027\\377\\0001\\000\\023\\007\\000\\247\\007\\000\\250\\007\\000\\251\\007\\000\\252\\007\\000\\250\\007\\000\\250\\007\\000\\250\\007\\000\\253\\007\\000\\254\\007\\000\\255\\007\\000\\256\\007\\000\\255\\007\\000\\256\\007\\000\\261\\007\\000\\250\\007\\000\\262\\007\\000\\250\\007\\000\\267\\007\\000\\261\\000\\001\\007\\000\\257\\\\\\007\\000\\260\\034V\\007\\000\\263\\001\\376\\000\\032\\007\\000\\250\\007\\000\\264\\007\\000\\265\\037\\374\\000\\006\\001\\370\\000)B\\007\\000\\260\\034\\374\\000S\\007\\000\\270\\024\\024\\024\\024\\024\\025\\017\\017(\\377\\000\\007\\000\\021\\007\\000\\247\\007\\000\\250\\007\\000\\251\\007\\000\\252\\007\\000\\250\\007\\000\\250\\007\\000\\250\\007\\000\\253\\007\\000\\254\\007\\000\\255\\007\\000\\256\\007\\000\\255\\007\\000\\256\\007\\000\\261\\007\\000\\250\\007\\000\\262\\007\\000\\250\\000\\000\\377\\000\\025\\000\\022\\007\\000\\247\\007\\000\\250\\007\\000\\251\\007\\000\\252\\007\\000\\250\\007\\000\\250\\007\\000\\250\\007\\000\\253\\007\\000\\254\\007\\000\\255\\007\\000\\256\\007\\000\\255\\007\\000\\256\\007\\000\\261\\007\\000\\250\\007\\000\\262\\007\\000\\250\\007\\000\\267\\000\\001\\007\\000\\257I\\007\\000\\260\\006\\000\\001\\000\\271\\000\\272\\000\\001\\000n\\000\\000\\0006\\000\\001\\000\\002\\000\\000\\000\\002+\\260\\000\\000\\000\\002\\000o\\000\\000\\000\\006\\000\\001\\000\\000\\000\\346\\000p\\000\\000\\000\\026\\000\\002\\000\\000\\000\\002\\000q\\000r\\000\\000\\000\\000\\000\\002\\000\\273\\000\\274\\000\\001\\000\\001\\000\\275\\000\\276\\000\\001\\000n\\000\\000\\000=\\000\\003\\000\\002\\000\\000\\000\\011\\273\\000eY+\\267\\000f\\260\\000\\000\\000\\002\\000o\\000\\000\\000\\006\\000\\001\\000\\000\\000\\353\\000p\\000\\000\\000\\026\\000\\002\\000\\000\\000\\011\\000q\\000r\\000\\000\\000\\000\\000\\011\\000\\273\\000\\274\\000\\001\\000\\001\\000\\277\\000\\300\\000\\001\\000n\\000\\000\\0006\\000\\001\\000\\002\\000\\000\\000\\002+\\260\\000\\000\\000\\002\\000o\\000\\000\\000\\006\\000\\001\\000\\000\\000\\360\\000p\\000\\000\\000\\026\\000\\002\\000\\000\\000\\002\\000q\\000r\\000\\000\\000\\000\\000\\002\\000\\216\\000\\213\\000\\001\\000\\001\\000\\301\\000\\302\\000\\001\\000n\\000\\000\\0005\\000\\000\\000\\002\\000\\000\\000\\001\\261\\000\\000\\000\\002\\000o\\000\\000\\000\\006\\000\\001\\000\\000\\000\\365\\000p\\000\\000\\000\\026\\000\\002\\000\\000\\000\\001\\000q\\000r\\000\\000\\000\\000\\000\\001\\000\\216\\000\\213\\000\\001\\000\\001\\000\\303\\000\\000\\000\\002\\000\\304
8	pl.umk.mat.zawodyweb.checker.classes.NormalDiff	1	Normal Diff	\\312\\376\\272\\276\\000\\000\\0002\\000n\\012\\000\\025\\000:\\012\\000;\\000<\\012\\000;\\000=\\012\\000>\\000?\\012\\000@\\000A\\012\\000B\\000C\\007\\000D\\012\\000B\\000E\\012\\000\\007\\000F\\012\\000\\007\\000:\\012\\000B\\000G\\012\\000\\024\\000H\\012\\000\\007\\000I\\012\\000J\\000K\\012\\000\\007\\000L\\012\\000B\\000M\\012\\000\\007\\000N\\012\\000B\\000O\\012\\000\\007\\000P\\007\\000Q\\007\\000R\\007\\000S\\001\\000\\006<init>\\001\\000\\003()V\\001\\000\\004Code\\001\\000\\017LineNumberTable\\001\\000\\022LocalVariableTable\\001\\000\\004this\\001\\0001Lpl/umk/mat/zawodyweb/checker/classes/NormalDiff;\\001\\000\\004diff\\001\\000'(Ljava/lang/String;Ljava/lang/String;)I\\001\\000\\010codeText\\001\\000\\022Ljava/lang/String;\\001\\000\\011rightText\\001\\000\\001i\\001\\000\\001I\\001\\000\\001j\\001\\000\\017rightTextLength\\001\\000\\016codeTextLength\\001\\000\\015StackMapTable\\007\\000Q\\007\\000T\\001\\000\\005check\\001\\000\\246(Lpl/umk/mat/zawodyweb/compiler/Program;Lpl/umk/mat/zawodyweb/checker/TestInput;Lpl/umk/mat/zawodyweb/checker/TestOutput;)Lpl/umk/mat/zawodyweb/checker/CheckerResult;\\001\\000\\007program\\001\\000'Lpl/umk/mat/zawodyweb/compiler/Program;\\001\\000\\005input\\001\\000(Lpl/umk/mat/zawodyweb/checker/TestInput;\\001\\000\\006output\\001\\000)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\012codeOutput\\001\\000\\006result\\001\\000,Lpl/umk/mat/zawodyweb/checker/CheckerResult;\\007\\000U\\007\\000D\\001\\000\\012SourceFile\\001\\000\\017NormalDiff.java\\014\\000\\027\\000\\030\\007\\000T\\014\\000V\\000W\\014\\000X\\000Y\\007\\000Z\\014\\000[\\000\\\\\\007\\000]\\014\\000^\\000_\\007\\000U\\014\\000`\\000W\\001\\000*pl/umk/mat/zawodyweb/checker/CheckerResult\\014\\000a\\000b\\014\\000\\027\\000c\\014\\000d\\000b\\014\\000\\036\\000\\037\\014\\000e\\000f\\007\\000g\\014\\000h\\000W\\014\\000i\\000f\\014\\000j\\000W\\014\\000k\\000f\\014\\000l\\000W\\014\\000m\\000f\\001\\000/pl/umk/mat/zawodyweb/checker/classes/NormalDiff\\001\\000\\020java/lang/Object\\001\\000-pl/umk/mat/zawodyweb/checker/CheckerInterface\\001\\000\\020java/lang/String\\001\\000'pl/umk/mat/zawodyweb/checker/TestOutput\\001\\000\\006length\\001\\000\\003()I\\001\\000\\006charAt\\001\\000\\004(I)C\\001\\000\\023java/lang/Character\\001\\000\\014isWhitespace\\001\\000\\004(C)Z\\001\\000%pl/umk/mat/zawodyweb/compiler/Program\\001\\000\\007runTest\\001\\000S(Lpl/umk/mat/zawodyweb/checker/TestInput;)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\011getResult\\001\\000\\015getResultDesc\\001\\000\\024()Ljava/lang/String;\\001\\000\\026(ILjava/lang/String;)V\\001\\000\\007getText\\001\\000\\011setResult\\001\\000\\004(I)V\\001\\000&pl/umk/mat/zawodyweb/checker/TestInput\\001\\000\\014getMaxPoints\\001\\000\\011setPoints\\001\\000\\012getRuntime\\001\\000\\012setRuntime\\001\\000\\012getMemUsed\\001\\000\\012setMemUsed\\000!\\000\\024\\000\\025\\000\\001\\000\\026\\000\\000\\000\\003\\000\\001\\000\\027\\000\\030\\000\\001\\000\\031\\000\\000\\000/\\000\\001\\000\\001\\000\\000\\000\\005*\\267\\000\\001\\261\\000\\000\\000\\002\\000\\032\\000\\000\\000\\006\\000\\001\\000\\000\\000\\013\\000\\033\\000\\000\\000\\014\\000\\001\\000\\000\\000\\005\\000\\034\\000\\035\\000\\000\\000\\002\\000\\036\\000\\037\\000\\001\\000\\031\\000\\000\\002\\032\\000\\003\\000\\007\\000\\000\\001!\\003>\\0036\\004,\\266\\000\\0026\\005+\\266\\000\\0026\\006\\035\\025\\005\\242\\000\\212\\025\\004\\025\\006\\242\\000\\203,\\035\\266\\000\\003\\270\\000\\004\\231\\000\\017+\\025\\004\\266\\000\\003\\270\\000\\004\\232\\000#\\035\\232\\000\\017+\\025\\004\\266\\000\\003\\270\\000\\004\\232\\000\\023\\025\\004\\232\\000>,\\035\\266\\000\\003\\270\\000\\004\\231\\0003\\035\\025\\005\\242\\000\\024,\\035\\266\\000\\003\\270\\000\\004\\231\\000\\011\\204\\003\\001\\247\\377\\354\\025\\004\\025\\006\\242\\377\\241+\\025\\004\\266\\000\\003\\270\\000\\004\\231\\377\\225\\204\\004\\001\\247\\377\\352,\\035\\266\\000\\003+\\025\\004\\266\\000\\003\\240\\000\\014\\204\\003\\001\\204\\004\\001\\247\\377x\\004\\254\\035\\025\\005\\240\\000\\014\\025\\004\\025\\006\\240\\000\\005\\003\\254\\035\\025\\005\\240\\000(+\\025\\004\\266\\000\\003\\270\\000\\004\\231\\000\\034\\025\\004\\025\\006\\242\\000>+\\025\\004\\266\\000\\003\\270\\000\\004\\231\\0002\\204\\004\\001\\247\\377\\352\\025\\004\\025\\006\\240\\000%,\\035\\266\\000\\003\\270\\000\\004\\231\\000\\032\\035\\025\\005\\242\\000\\024,\\035\\266\\000\\003\\270\\000\\004\\231\\000\\011\\204\\003\\001\\247\\377\\354\\035\\025\\005\\240\\000\\014\\025\\004\\025\\006\\240\\000\\005\\003\\254\\035\\025\\005\\237\\000\\012\\025\\004\\025\\006\\240\\000\\005\\004\\254\\002\\254\\000\\000\\000\\003\\000\\032\\000\\000\\000n\\000\\033\\000\\000\\000\\016\\000\\002\\000\\017\\000\\005\\000\\020\\000\\013\\000\\021\\000\\021\\000\\022\\000\\036\\000\\023\\000U\\000\\031\\000f\\000\\033\\000l\\000\\036\\000\\177\\000 \\000\\205\\000#\\000\\223\\000$\\000\\226\\000%\\000\\234\\000'\\000\\236\\000*\\000\\253\\000+\\000\\255\\000,\\000\\277\\000.\\000\\322\\0000\\000\\330\\0002\\000\\352\\0004\\000\\373\\0006\\001\\001\\0009\\001\\016\\000:\\001\\020\\000;\\001\\035\\000<\\001\\037\\000>\\000\\033\\000\\000\\000H\\000\\007\\000\\000\\001!\\000\\034\\000\\035\\000\\000\\000\\000\\001!\\000 \\000!\\000\\001\\000\\000\\001!\\000"\\000!\\000\\002\\000\\002\\001\\037\\000#\\000$\\000\\003\\000\\005\\001\\034\\000%\\000$\\000\\004\\000\\013\\001\\026\\000&\\000$\\000\\005\\000\\021\\001\\020\\000'\\000$\\000\\006\\000(\\000\\000\\000%\\000\\020\\377\\000\\021\\000\\007\\007\\000)\\007\\000*\\007\\000*\\001\\001\\001\\001\\000\\000#\\017\\017\\026\\030\\026\\001\\016\\021\\030\\021\\026\\016\\014\\001\\000\\001\\000+\\000,\\000\\001\\000\\031\\000\\000\\0017\\000\\004\\000\\010\\000\\000\\000x+,\\266\\000\\005:\\004\\031\\004\\266\\000\\006\\002\\237\\000\\025\\273\\000\\007Y\\031\\004\\266\\000\\006\\031\\004\\266\\000\\010\\267\\000\\011\\260\\273\\000\\007Y\\267\\000\\012:\\005\\031\\004\\266\\000\\013:\\006-\\266\\000\\013:\\007*\\031\\006\\031\\007\\267\\000\\014\\232\\000\\025\\031\\005\\003\\266\\000\\015\\031\\005,\\266\\000\\016\\266\\000\\017\\247\\000\\017\\031\\005\\004\\266\\000\\015\\031\\005\\003\\266\\000\\017\\031\\005\\031\\004\\266\\000\\020\\266\\000\\021\\031\\005\\031\\004\\266\\000\\022\\266\\000\\023\\031\\005\\260\\000\\000\\000\\003\\000\\032\\000\\000\\000:\\000\\016\\000\\000\\000D\\000\\007\\000E\\000\\020\\000F\\000"\\000H\\000+\\000I\\0002\\000J\\0008\\000K\\000C\\000L\\000I\\000M\\000U\\000P\\000[\\000Q\\000a\\000T\\000k\\000U\\000u\\000V\\000\\033\\000\\000\\000R\\000\\010\\000\\000\\000x\\000\\034\\000\\035\\000\\000\\000\\000\\000x\\000-\\000.\\000\\001\\000\\000\\000x\\000/\\0000\\000\\002\\000\\000\\000x\\0001\\0002\\000\\003\\000\\007\\000q\\0003\\0002\\000\\004\\000+\\000M\\0004\\0005\\000\\005\\0002\\000F\\000 \\000!\\000\\006\\0008\\000@\\000"\\000!\\000\\007\\000(\\000\\000\\000\\025\\000\\003\\374\\000"\\007\\0006\\376\\0002\\007\\0007\\007\\000*\\007\\000*\\013\\000\\001\\0008\\000\\000\\000\\002\\0009
9	pl.umk.mat.zawodyweb.checker.classes.ExactDiff	1	Exact Diff	\\312\\376\\272\\276\\000\\000\\0002\\000e\\012\\000\\024\\0006\\012\\0007\\0008\\012\\0009\\000:\\007\\000;\\012\\0009\\000<\\012\\000\\004\\000=\\012\\000\\004\\0006\\012\\0009\\000>\\012\\000\\023\\000?\\012\\000\\004\\000@\\012\\000A\\000B\\012\\000\\004\\000C\\012\\0009\\000D\\012\\000\\004\\000E\\012\\0009\\000F\\012\\000\\004\\000G\\012\\000H\\000I\\012\\000H\\000J\\007\\000K\\007\\000L\\007\\000M\\001\\000\\006<init>\\001\\000\\003()V\\001\\000\\004Code\\001\\000\\017LineNumberTable\\001\\000\\022LocalVariableTable\\001\\000\\004this\\001\\0000Lpl/umk/mat/zawodyweb/checker/classes/ExactDiff;\\001\\000\\005check\\001\\000\\246(Lpl/umk/mat/zawodyweb/compiler/Program;Lpl/umk/mat/zawodyweb/checker/TestInput;Lpl/umk/mat/zawodyweb/checker/TestOutput;)Lpl/umk/mat/zawodyweb/checker/CheckerResult;\\001\\000\\007program\\001\\000'Lpl/umk/mat/zawodyweb/compiler/Program;\\001\\000\\005input\\001\\000(Lpl/umk/mat/zawodyweb/checker/TestInput;\\001\\000\\006output\\001\\000)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\012codeOutput\\001\\000\\006result\\001\\000,Lpl/umk/mat/zawodyweb/checker/CheckerResult;\\001\\000\\010codeText\\001\\000\\022Ljava/lang/String;\\001\\000\\011rightText\\001\\000\\015StackMapTable\\007\\000N\\007\\000;\\007\\000O\\001\\000\\004diff\\001\\000'(Ljava/lang/String;Ljava/lang/String;)I\\001\\000\\001i\\001\\000\\001I\\001\\000\\016codeTextLength\\001\\000\\012SourceFile\\001\\000\\016ExactDiff.java\\014\\000\\026\\000\\027\\007\\000P\\014\\000Q\\000R\\007\\000N\\014\\000S\\000T\\001\\000*pl/umk/mat/zawodyweb/checker/CheckerResult\\014\\000U\\000V\\014\\000\\026\\000W\\014\\000X\\000V\\014\\000/\\0000\\014\\000Y\\000Z\\007\\000[\\014\\000\\\\\\000T\\014\\000]\\000Z\\014\\000^\\000T\\014\\000_\\000Z\\014\\000`\\000T\\014\\000a\\000Z\\007\\000O\\014\\000b\\000T\\014\\000c\\000d\\001\\000.pl/umk/mat/zawodyweb/checker/classes/ExactDiff\\001\\000\\020java/lang/Object\\001\\000-pl/umk/mat/zawodyweb/checker/CheckerInterface\\001\\000'pl/umk/mat/zawodyweb/checker/TestOutput\\001\\000\\020java/lang/String\\001\\000%pl/umk/mat/zawodyweb/compiler/Program\\001\\000\\007runTest\\001\\000S(Lpl/umk/mat/zawodyweb/checker/TestInput;)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\011getResult\\001\\000\\003()I\\001\\000\\015getResultDesc\\001\\000\\024()Ljava/lang/String;\\001\\000\\026(ILjava/lang/String;)V\\001\\000\\007getText\\001\\000\\011setResult\\001\\000\\004(I)V\\001\\000&pl/umk/mat/zawodyweb/checker/TestInput\\001\\000\\014getMaxPoints\\001\\000\\011setPoints\\001\\000\\012getRuntime\\001\\000\\012setRuntime\\001\\000\\012getMemUsed\\001\\000\\012setMemUsed\\001\\000\\006length\\001\\000\\006charAt\\001\\000\\004(I)C\\000!\\000\\023\\000\\024\\000\\001\\000\\025\\000\\000\\000\\003\\000\\001\\000\\026\\000\\027\\000\\001\\000\\030\\000\\000\\000/\\000\\001\\000\\001\\000\\000\\000\\005*\\267\\000\\001\\261\\000\\000\\000\\002\\000\\031\\000\\000\\000\\006\\000\\001\\000\\000\\000\\016\\000\\032\\000\\000\\000\\014\\000\\001\\000\\000\\000\\005\\000\\033\\000\\034\\000\\000\\000\\001\\000\\035\\000\\036\\000\\001\\000\\030\\000\\000\\0017\\000\\004\\000\\010\\000\\000\\000x+,\\266\\000\\002:\\004\\031\\004\\266\\000\\003\\002\\237\\000\\025\\273\\000\\004Y\\031\\004\\266\\000\\003\\031\\004\\266\\000\\005\\267\\000\\006\\260\\273\\000\\004Y\\267\\000\\007:\\005\\031\\004\\266\\000\\010:\\006-\\266\\000\\010:\\007*\\031\\006\\031\\007\\267\\000\\011\\232\\000\\025\\031\\005\\003\\266\\000\\012\\031\\005,\\266\\000\\013\\266\\000\\014\\247\\000\\017\\031\\005\\004\\266\\000\\012\\031\\005\\003\\266\\000\\014\\031\\005\\031\\004\\266\\000\\015\\266\\000\\016\\031\\005\\031\\004\\266\\000\\017\\266\\000\\020\\031\\005\\260\\000\\000\\000\\003\\000\\031\\000\\000\\000:\\000\\016\\000\\000\\000\\022\\000\\007\\000\\023\\000\\020\\000\\024\\000"\\000\\026\\000+\\000\\027\\0002\\000\\030\\0008\\000\\031\\000C\\000\\032\\000I\\000\\033\\000U\\000\\035\\000[\\000\\036\\000a\\000!\\000k\\000"\\000u\\000#\\000\\032\\000\\000\\000R\\000\\010\\000\\000\\000x\\000\\033\\000\\034\\000\\000\\000\\000\\000x\\000\\037\\000 \\000\\001\\000\\000\\000x\\000!\\000"\\000\\002\\000\\000\\000x\\000#\\000$\\000\\003\\000\\007\\000q\\000%\\000$\\000\\004\\000+\\000M\\000&\\000'\\000\\005\\0002\\000F\\000(\\000)\\000\\006\\0008\\000@\\000*\\000)\\000\\007\\000+\\000\\000\\000\\025\\000\\003\\374\\000"\\007\\000,\\376\\0002\\007\\000-\\007\\000.\\007\\000.\\013\\000\\002\\000/\\0000\\000\\001\\000\\030\\000\\000\\000\\264\\000\\003\\000\\005\\000\\000\\0004+\\266\\000\\021,\\266\\000\\021\\237\\000\\005\\004\\254+\\266\\000\\021>\\0036\\004\\025\\004\\035\\242\\000\\032+\\025\\004\\266\\000\\022,\\025\\004\\266\\000\\022\\237\\000\\005\\004\\254\\204\\004\\001\\247\\377\\346\\003\\254\\000\\000\\000\\003\\000\\031\\000\\000\\000"\\000\\010\\000\\000\\000'\\000\\013\\000(\\000\\015\\000*\\000\\022\\000+\\000\\033\\000,\\000*\\000-\\000,\\000+\\0002\\0000\\000\\032\\000\\000\\0004\\000\\005\\000\\025\\000\\035\\0001\\0002\\000\\004\\000\\000\\0004\\000\\033\\000\\034\\000\\000\\000\\000\\0004\\000(\\000)\\000\\001\\000\\000\\0004\\000*\\000)\\000\\002\\000\\022\\000"\\0003\\0002\\000\\003\\000+\\000\\000\\000\\014\\000\\004\\015\\375\\000\\007\\001\\001\\026\\372\\000\\005\\000\\001\\0004\\000\\000\\000\\002\\0005
10	pl.umk.mat.zawodyweb.checker.classes.TrailingDiff	1	Trailing Diff	\\312\\376\\272\\276\\000\\000\\0002\\000\\200\\012\\000\\033\\000E\\012\\000F\\000G\\012\\000H\\000I\\007\\000J\\012\\000H\\000K\\012\\000\\004\\000L\\012\\000\\004\\000E\\012\\000H\\000M\\012\\000\\032\\000N\\012\\000\\004\\000O\\012\\000P\\000Q\\012\\000\\004\\000R\\007\\000S\\012\\000H\\000T\\012\\000\\004\\000U\\012\\000H\\000V\\012\\000\\004\\000W\\007\\000X\\007\\000Y\\012\\000\\023\\000Z\\012\\000\\022\\000[\\012\\000\\022\\000\\\\\\012\\000]\\000^\\012\\000]\\000_\\012\\000]\\000`\\007\\000a\\007\\000b\\007\\000c\\001\\000\\006<init>\\001\\000\\003()V\\001\\000\\004Code\\001\\000\\017LineNumberTable\\001\\000\\022LocalVariableTable\\001\\000\\004this\\001\\0003Lpl/umk/mat/zawodyweb/checker/classes/TrailingDiff;\\001\\000\\005check\\001\\000\\246(Lpl/umk/mat/zawodyweb/compiler/Program;Lpl/umk/mat/zawodyweb/checker/TestInput;Lpl/umk/mat/zawodyweb/checker/TestOutput;)Lpl/umk/mat/zawodyweb/checker/CheckerResult;\\001\\000\\002ex\\001\\000\\025Ljava/io/IOException;\\001\\000\\007program\\001\\000'Lpl/umk/mat/zawodyweb/compiler/Program;\\001\\000\\005input\\001\\000(Lpl/umk/mat/zawodyweb/checker/TestInput;\\001\\000\\006output\\001\\000)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\012codeOutput\\001\\000\\006result\\001\\000,Lpl/umk/mat/zawodyweb/checker/CheckerResult;\\001\\000\\010codeText\\001\\000\\022Ljava/lang/String;\\001\\000\\011rightText\\001\\000\\015StackMapTable\\007\\000d\\007\\000J\\007\\000e\\007\\000S\\001\\000\\004diff\\001\\000'(Ljava/lang/String;Ljava/lang/String;)I\\001\\000\\005right\\001\\000\\030Ljava/io/BufferedReader;\\001\\000\\004code\\001\\000\\010codeLine\\001\\000\\011rightLine\\007\\000a\\007\\000X\\001\\000\\012Exceptions\\001\\000\\012SourceFile\\001\\000\\021TrailingDiff.java\\014\\000\\035\\000\\036\\007\\000f\\014\\000g\\000h\\007\\000d\\014\\000i\\000j\\001\\000*pl/umk/mat/zawodyweb/checker/CheckerResult\\014\\000k\\000l\\014\\000\\035\\000m\\014\\000n\\000l\\014\\0009\\000:\\014\\000o\\000p\\007\\000q\\014\\000r\\000j\\014\\000s\\000p\\001\\000\\023java/io/IOException\\014\\000t\\000j\\014\\000u\\000p\\014\\000v\\000j\\014\\000w\\000p\\001\\000\\026java/io/BufferedReader\\001\\000\\024java/io/StringReader\\014\\000\\035\\000x\\014\\000\\035\\000y\\014\\000z\\000l\\007\\000e\\014\\000{\\000l\\014\\000|\\000}\\014\\000~\\000\\177\\001\\0001pl/umk/mat/zawodyweb/checker/classes/TrailingDiff\\001\\000\\020java/lang/Object\\001\\000-pl/umk/mat/zawodyweb/checker/CheckerInterface\\001\\000'pl/umk/mat/zawodyweb/checker/TestOutput\\001\\000\\020java/lang/String\\001\\000%pl/umk/mat/zawodyweb/compiler/Program\\001\\000\\007runTest\\001\\000S(Lpl/umk/mat/zawodyweb/checker/TestInput;)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\011getResult\\001\\000\\003()I\\001\\000\\015getResultDesc\\001\\000\\024()Ljava/lang/String;\\001\\000\\026(ILjava/lang/String;)V\\001\\000\\007getText\\001\\000\\011setResult\\001\\000\\004(I)V\\001\\000&pl/umk/mat/zawodyweb/checker/TestInput\\001\\000\\014getMaxPoints\\001\\000\\011setPoints\\001\\000\\012getRuntime\\001\\000\\012setRuntime\\001\\000\\012getMemUsed\\001\\000\\012setMemUsed\\001\\000\\025(Ljava/lang/String;)V\\001\\000\\023(Ljava/io/Reader;)V\\001\\000\\010readLine\\001\\000\\004trim\\001\\000\\011compareTo\\001\\000\\025(Ljava/lang/String;)I\\001\\000\\007isEmpty\\001\\000\\003()Z\\000!\\000\\032\\000\\033\\000\\001\\000\\034\\000\\000\\000\\003\\000\\001\\000\\035\\000\\036\\000\\001\\000\\037\\000\\000\\000/\\000\\001\\000\\001\\000\\000\\000\\005*\\267\\000\\001\\261\\000\\000\\000\\002\\000 \\000\\000\\000\\006\\000\\001\\000\\000\\000\\021\\000!\\000\\000\\000\\014\\000\\001\\000\\000\\000\\005\\000"\\000#\\000\\000\\000\\001\\000$\\000%\\000\\001\\000\\037\\000\\000\\001[\\000\\004\\000\\011\\000\\000\\000}+,\\266\\000\\002:\\004\\031\\004\\266\\000\\003\\002\\237\\000\\025\\273\\000\\004Y\\031\\004\\266\\000\\003\\031\\004\\266\\000\\005\\267\\000\\006\\260\\273\\000\\004Y\\267\\000\\007:\\005\\031\\004\\266\\000\\010:\\006-\\266\\000\\010:\\007*\\031\\006\\031\\007\\267\\000\\011\\232\\000\\025\\031\\005\\003\\266\\000\\012\\031\\005,\\266\\000\\013\\266\\000\\014\\247\\000\\017\\031\\005\\004\\266\\000\\012\\031\\005\\003\\266\\000\\014\\247\\000\\005:\\010\\031\\005\\031\\004\\266\\000\\016\\266\\000\\017\\031\\005\\031\\004\\266\\000\\020\\266\\000\\021\\031\\005\\260\\000\\001\\0008\\000a\\000d\\000\\015\\000\\003\\000 \\000\\000\\000B\\000\\020\\000\\000\\000\\025\\000\\007\\000\\026\\000\\020\\000\\027\\000"\\000\\031\\000+\\000\\032\\0002\\000\\033\\0008\\000\\035\\000C\\000\\036\\000I\\000\\037\\000U\\000!\\000[\\000"\\000a\\000&\\000d\\000$\\000f\\000'\\000p\\000(\\000z\\000)\\000!\\000\\000\\000\\\\\\000\\011\\000f\\000\\000\\000&\\000'\\000\\010\\000\\000\\000}\\000"\\000#\\000\\000\\000\\000\\000}\\000(\\000)\\000\\001\\000\\000\\000}\\000*\\000+\\000\\002\\000\\000\\000}\\000,\\000-\\000\\003\\000\\007\\000v\\000.\\000-\\000\\004\\000+\\000R\\000/\\0000\\000\\005\\0002\\000K\\0001\\0002\\000\\006\\0008\\000E\\0003\\0002\\000\\007\\0004\\000\\000\\000\\032\\000\\005\\374\\000"\\007\\0005\\376\\0002\\007\\0006\\007\\0007\\007\\0007\\013B\\007\\0008\\001\\000\\002\\0009\\000:\\000\\002\\000\\037\\000\\000\\001z\\000\\005\\000\\007\\000\\000\\000\\245\\273\\000\\022Y\\273\\000\\023Y,\\267\\000\\024\\267\\000\\025N\\273\\000\\022Y\\273\\000\\023Y+\\267\\000\\024\\267\\000\\025:\\004\\031\\004\\266\\000\\026:\\005-\\266\\000\\026:\\006\\031\\005\\306\\000*\\031\\006\\306\\000%\\031\\005\\266\\000\\027\\031\\006\\266\\000\\027\\266\\000\\030\\231\\000\\005\\004\\254\\031\\004\\266\\000\\026:\\005-\\266\\000\\026:\\006\\247\\377\\327\\031\\005\\306\\000\\035\\031\\005\\306\\000\\030\\031\\005\\266\\000\\027\\266\\000\\031\\231\\000\\015\\031\\004\\266\\000\\026:\\005\\247\\377\\351\\031\\006\\306\\000\\034\\031\\006\\306\\000\\027\\031\\006\\266\\000\\027\\266\\000\\031\\231\\000\\014-\\266\\000\\026:\\006\\247\\377\\352\\031\\005\\307\\000\\012\\031\\006\\307\\000\\005\\003\\254\\004\\254\\000\\000\\000\\003\\000 \\000\\000\\000J\\000\\022\\000\\000\\000-\\000\\020\\000.\\000!\\000/\\000(\\0000\\000.\\0001\\0008\\0002\\000H\\0003\\000J\\0005\\000Q\\0006\\000Z\\0008\\000_\\0009\\000o\\000:\\000y\\000=\\000~\\000>\\000\\216\\000?\\000\\227\\000B\\000\\241\\000C\\000\\243\\000E\\000!\\000\\000\\000H\\000\\007\\000\\000\\000\\245\\000"\\000#\\000\\000\\000\\000\\000\\245\\0001\\0002\\000\\001\\000\\000\\000\\245\\0003\\0002\\000\\002\\000\\020\\000\\225\\000;\\000<\\000\\003\\000!\\000\\204\\000=\\000<\\000\\004\\000(\\000}\\000>\\0002\\000\\005\\000.\\000w\\000?\\0002\\000\\006\\0004\\000\\000\\000%\\000\\010\\377\\000.\\000\\007\\007\\000@\\007\\0007\\007\\0007\\007\\000A\\007\\000A\\007\\0007\\007\\0007\\000\\000\\033\\017\\004\\031\\004\\030\\013\\000B\\000\\000\\000\\004\\000\\001\\000\\015\\000\\001\\000C\\000\\000\\000\\002\\000D
11	pl.umk.mat.zawodyweb.checker.classes.HTTPsubmit	1	HTTP Submit	\\312\\376\\272\\276\\000\\000\\0002\\000J\\012\\000\\020\\000&\\012\\000'\\000(\\007\\000)\\012\\000\\003\\000&\\012\\000*\\000+\\012\\000\\003\\000,\\012\\000*\\000-\\012\\000\\003\\000.\\012\\000*\\000/\\012\\000\\003\\0000\\012\\000*\\0001\\012\\000\\003\\0002\\012\\000*\\0003\\012\\000\\003\\0004\\007\\0005\\007\\0006\\007\\0007\\001\\000\\006<init>\\001\\000\\003()V\\001\\000\\004Code\\001\\000\\017LineNumberTable\\001\\000\\022LocalVariableTable\\001\\000\\004this\\001\\0001Lpl/umk/mat/zawodyweb/checker/classes/HTTPsubmit;\\001\\000\\005check\\001\\000\\246(Lpl/umk/mat/zawodyweb/compiler/Program;Lpl/umk/mat/zawodyweb/checker/TestInput;Lpl/umk/mat/zawodyweb/checker/TestOutput;)Lpl/umk/mat/zawodyweb/checker/CheckerResult;\\001\\000\\007program\\001\\000'Lpl/umk/mat/zawodyweb/compiler/Program;\\001\\000\\005input\\001\\000(Lpl/umk/mat/zawodyweb/checker/TestInput;\\001\\000\\006output\\001\\000)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000\\002to\\001\\000\\006result\\001\\000,Lpl/umk/mat/zawodyweb/checker/CheckerResult;\\001\\000\\012SourceFile\\001\\000\\017HTTPsubmit.java\\014\\000\\022\\000\\023\\007\\0008\\014\\0009\\000:\\001\\000*pl/umk/mat/zawodyweb/checker/CheckerResult\\007\\000;\\014\\000<\\000=\\014\\000>\\000?\\014\\000@\\000=\\014\\000A\\000?\\014\\000B\\000=\\014\\000C\\000?\\014\\000D\\000E\\014\\000F\\000G\\014\\000H\\000=\\014\\000I\\000?\\001\\000/pl/umk/mat/zawodyweb/checker/classes/HTTPsubmit\\001\\000\\020java/lang/Object\\001\\000-pl/umk/mat/zawodyweb/checker/CheckerInterface\\001\\000%pl/umk/mat/zawodyweb/compiler/Program\\001\\000\\007runTest\\001\\000S(Lpl/umk/mat/zawodyweb/checker/TestInput;)Lpl/umk/mat/zawodyweb/checker/TestOutput;\\001\\000'pl/umk/mat/zawodyweb/checker/TestOutput\\001\\000\\011getResult\\001\\000\\003()I\\001\\000\\011setResult\\001\\000\\004(I)V\\001\\000\\012getMemUsed\\001\\000\\012setMemUsed\\001\\000\\012getRuntime\\001\\000\\012setRuntime\\001\\000\\015getResultDesc\\001\\000\\024()Ljava/lang/String;\\001\\000\\015setDecription\\001\\000\\025(Ljava/lang/String;)V\\001\\000\\011getPoints\\001\\000\\011setPoints\\000!\\000\\017\\000\\020\\000\\001\\000\\021\\000\\000\\000\\002\\000\\001\\000\\022\\000\\023\\000\\001\\000\\024\\000\\000\\000/\\000\\001\\000\\001\\000\\000\\000\\005*\\267\\000\\001\\261\\000\\000\\000\\002\\000\\025\\000\\000\\000\\006\\000\\001\\000\\000\\000\\021\\000\\026\\000\\000\\000\\014\\000\\001\\000\\000\\000\\005\\000\\027\\000\\030\\000\\000\\000\\001\\000\\031\\000\\032\\000\\001\\000\\024\\000\\000\\000\\275\\000\\002\\000\\006\\000\\000\\000E+,\\266\\000\\002:\\004\\273\\000\\003Y\\267\\000\\004:\\005\\031\\005\\031\\004\\266\\000\\005\\266\\000\\006\\031\\005\\031\\004\\266\\000\\007\\266\\000\\010\\031\\005\\031\\004\\266\\000\\011\\266\\000\\012\\031\\005\\031\\004\\266\\000\\013\\266\\000\\014\\031\\005\\031\\004\\266\\000\\015\\266\\000\\016\\031\\005\\260\\000\\000\\000\\002\\000\\025\\000\\000\\000"\\000\\010\\000\\000\\000\\025\\000\\007\\000\\026\\000\\020\\000\\027\\000\\032\\000\\030\\000$\\000\\031\\000.\\000\\032\\0008\\000\\033\\000B\\000\\034\\000\\026\\000\\000\\000>\\000\\006\\000\\000\\000E\\000\\027\\000\\030\\000\\000\\000\\000\\000E\\000\\033\\000\\034\\000\\001\\000\\000\\000E\\000\\035\\000\\036\\000\\002\\000\\000\\000E\\000\\037\\000 \\000\\003\\000\\007\\000>\\000!\\000 \\000\\004\\000\\020\\0005\\000"\\000#\\000\\005\\000\\001\\000$\\000\\000\\000\\002\\000%
\.


--
-- TOC entry 1921 (class 0 OID 25125)
-- Dependencies: 1546
-- Data for Name: contests; Type: TABLE DATA; Schema: public; Owner: -
--

COPY contests (id, name, type, pass, startdate, about, rules, tech, visibility) FROM stdin;
\.


--
-- TOC entry 1927 (class 0 OID 25193)
-- Dependencies: 1558
-- Data for Name: languages; Type: TABLE DATA; Schema: public; Owner: -
--

COPY languages (id, name, extension, classesid) FROM stdin;
1	TXT	txt	1
2	Pascal	pas	2
3	C	c	3
4	CPP	cpp	4
5	JAVA	java	5
8	ACM java	java	6
6	ACM c	c	6
7	ACM cpp	cpp	6
9	ACM pas	pas	6
10	OPSS c	c	7
11	OPSS cpp	cpp	7
12	OPSS pas	pas	7
\.


--
-- TOC entry 1931 (class 0 OID 25325)
-- Dependencies: 1566
-- Data for Name: languages_problems; Type: TABLE DATA; Schema: public; Owner: -
--

COPY languages_problems (id, problemsid, languagesid) FROM stdin;
\.


--
-- TOC entry 1929 (class 0 OID 25216)
-- Dependencies: 1562
-- Data for Name: pdf; Type: TABLE DATA; Schema: public; Owner: -
--

COPY pdf (id, pdf) FROM stdin;
\.


--
-- TOC entry 1923 (class 0 OID 25149)
-- Dependencies: 1550
-- Data for Name: problems; Type: TABLE DATA; Schema: public; Owner: -
--

COPY problems (id, name, text, abbrev, memlimit, config, seriesid, pdfid, classesid) FROM stdin;
\.


--
-- TOC entry 1924 (class 0 OID 25160)
-- Dependencies: 1552
-- Data for Name: questions; Type: TABLE DATA; Schema: public; Owner: -
--

COPY questions (id, subject, question, visibility, qtype, usersid, contestsid, tasksid) FROM stdin;
\.


--
-- TOC entry 1925 (class 0 OID 25171)
-- Dependencies: 1554
-- Data for Name: results; Type: TABLE DATA; Schema: public; Owner: -
--

COPY results (id, points, runtime, memory, notes, submitresult, submitsid, testsid) FROM stdin;
\.


--
-- TOC entry 1919 (class 0 OID 25107)
-- Dependencies: 1542
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: -
--

COPY roles (id, name, edituser, addcontest, editcontest, delcontest, addseries, editseries, delseries, addproblem, editproblem, delproblem, canrate, contestant, contestsid, seriesid) FROM stdin;
1	superadmin	t	t	t	t	t	t	t	t	t	t	t	t	\N	\N
2	admin	f	t	t	t	t	t	t	t	t	t	t	t	\N	\N
3	autor	f	f	f	f	t	t	t	t	t	t	t	t	\N	\N
\.


--
-- TOC entry 1920 (class 0 OID 25117)
-- Dependencies: 1544
-- Data for Name: series; Type: TABLE DATA; Schema: public; Owner: -
--

COPY series (id, name, startdate, enddate, freezedate, unfreezedate, penaltytime, contestsid) FROM stdin;
\.


--
-- TOC entry 1922 (class 0 OID 25138)
-- Dependencies: 1548
-- Data for Name: submits; Type: TABLE DATA; Schema: public; Owner: -
--

COPY submits (id, sdate, result, code, filename, notes, problemsid, languagesid, usersid) FROM stdin;
\.


--
-- TOC entry 1926 (class 0 OID 25182)
-- Dependencies: 1556
-- Data for Name: tests; Type: TABLE DATA; Schema: public; Owner: -
--

COPY tests (id, input, output, timelimit, maxpoints, visibility, testorder, problemsid) FROM stdin;
\.


--
-- TOC entry 1918 (class 0 OID 25092)
-- Dependencies: 1540
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: -
--

COPY users (id, firstname, lastname, email, birthdate, login, pass, address, school, tutor, emailnotification) FROM stdin;
1	Marek	Nowicki	faramir@mat.uni.torun.pl	\N	admin	A7b245f5960e94c8b61be972b766c3d7523d92d9				\N
\.


--
-- TOC entry 1930 (class 0 OID 25307)
-- Dependencies: 1564
-- Data for Name: users_roles; Type: TABLE DATA; Schema: public; Owner: -
--

COPY users_roles (id, usersid, rolesid) FROM stdin;
1	1	1
\.


--
-- TOC entry 1889 (class 2606 OID 25213)
-- Dependencies: 1560 1560
-- Name: classes_filename_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY classes
    ADD CONSTRAINT classes_filename_key UNIQUE (filename);


--
-- TOC entry 1891 (class 2606 OID 25211)
-- Dependencies: 1560 1560
-- Name: classes_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY classes
    ADD CONSTRAINT classes_pkey PRIMARY KEY (id);


--
-- TOC entry 1871 (class 2606 OID 25135)
-- Dependencies: 1546 1546
-- Name: contests_name_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY contests
    ADD CONSTRAINT contests_name_key UNIQUE (name);


--
-- TOC entry 1873 (class 2606 OID 25133)
-- Dependencies: 1546 1546
-- Name: contests_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY contests
    ADD CONSTRAINT contests_pkey PRIMARY KEY (id);


--
-- TOC entry 1885 (class 2606 OID 25200)
-- Dependencies: 1558 1558
-- Name: languages_name_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY languages
    ADD CONSTRAINT languages_name_key UNIQUE (name);


--
-- TOC entry 1887 (class 2606 OID 25198)
-- Dependencies: 1558 1558
-- Name: languages_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY languages
    ADD CONSTRAINT languages_pkey PRIMARY KEY (id);


--
-- TOC entry 1897 (class 2606 OID 25330)
-- Dependencies: 1566 1566
-- Name: languages_problems_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY languages_problems
    ADD CONSTRAINT languages_problems_pkey PRIMARY KEY (id);


--
-- TOC entry 1893 (class 2606 OID 25224)
-- Dependencies: 1562 1562
-- Name: pdf_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY pdf
    ADD CONSTRAINT pdf_pkey PRIMARY KEY (id);


--
-- TOC entry 1877 (class 2606 OID 25157)
-- Dependencies: 1550 1550
-- Name: problems_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY problems
    ADD CONSTRAINT problems_pkey PRIMARY KEY (id);


--
-- TOC entry 1879 (class 2606 OID 25168)
-- Dependencies: 1552 1552
-- Name: questions_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY questions
    ADD CONSTRAINT questions_pkey PRIMARY KEY (id);


--
-- TOC entry 1881 (class 2606 OID 25179)
-- Dependencies: 1554 1554
-- Name: results_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY results
    ADD CONSTRAINT results_pkey PRIMARY KEY (id);


--
-- TOC entry 1865 (class 2606 OID 25114)
-- Dependencies: 1542 1542
-- Name: roles_name_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_name_key UNIQUE (name);


--
-- TOC entry 1867 (class 2606 OID 25112)
-- Dependencies: 1542 1542
-- Name: roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- TOC entry 1869 (class 2606 OID 25122)
-- Dependencies: 1544 1544
-- Name: series_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY series
    ADD CONSTRAINT series_pkey PRIMARY KEY (id);


--
-- TOC entry 1875 (class 2606 OID 25146)
-- Dependencies: 1548 1548
-- Name: submits_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY submits
    ADD CONSTRAINT submits_pkey PRIMARY KEY (id);


--
-- TOC entry 1883 (class 2606 OID 25190)
-- Dependencies: 1556 1556
-- Name: tests_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY tests
    ADD CONSTRAINT tests_pkey PRIMARY KEY (id);


--
-- TOC entry 1859 (class 2606 OID 25102)
-- Dependencies: 1540 1540
-- Name: users_email_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 1861 (class 2606 OID 25104)
-- Dependencies: 1540 1540
-- Name: users_login_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_login_key UNIQUE (login);


--
-- TOC entry 1863 (class 2606 OID 25100)
-- Dependencies: 1540 1540
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 1895 (class 2606 OID 25312)
-- Dependencies: 1564 1564
-- Name: users_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY users_roles
    ADD CONSTRAINT users_roles_pkey PRIMARY KEY (id);


--
-- TOC entry 1913 (class 2606 OID 25270)
-- Dependencies: 1890 1560 1558
-- Name: languages_classesid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY languages
    ADD CONSTRAINT languages_classesid_fkey FOREIGN KEY (classesid) REFERENCES classes(id);


--
-- TOC entry 1917 (class 2606 OID 25336)
-- Dependencies: 1566 1558 1886
-- Name: languages_problems_languagesid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY languages_problems
    ADD CONSTRAINT languages_problems_languagesid_fkey FOREIGN KEY (languagesid) REFERENCES languages(id);


--
-- TOC entry 1916 (class 2606 OID 25331)
-- Dependencies: 1876 1566 1550
-- Name: languages_problems_problemsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY languages_problems
    ADD CONSTRAINT languages_problems_problemsid_fkey FOREIGN KEY (problemsid) REFERENCES problems(id);


--
-- TOC entry 1906 (class 2606 OID 25275)
-- Dependencies: 1550 1560 1890
-- Name: problems_classesid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY problems
    ADD CONSTRAINT problems_classesid_fkey FOREIGN KEY (classesid) REFERENCES classes(id);


--
-- TOC entry 1905 (class 2606 OID 25245)
-- Dependencies: 1892 1550 1562
-- Name: problems_pdfid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY problems
    ADD CONSTRAINT problems_pdfid_fkey FOREIGN KEY (pdfid) REFERENCES pdf(id);


--
-- TOC entry 1904 (class 2606 OID 25240)
-- Dependencies: 1544 1550 1868
-- Name: problems_seriesid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY problems
    ADD CONSTRAINT problems_seriesid_fkey FOREIGN KEY (seriesid) REFERENCES series(id);


--
-- TOC entry 1908 (class 2606 OID 25235)
-- Dependencies: 1872 1546 1552
-- Name: questions_contestsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY questions
    ADD CONSTRAINT questions_contestsid_fkey FOREIGN KEY (contestsid) REFERENCES contests(id);


--
-- TOC entry 1909 (class 2606 OID 25260)
-- Dependencies: 1550 1552 1876
-- Name: questions_tasksid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY questions
    ADD CONSTRAINT questions_tasksid_fkey FOREIGN KEY (tasksid) REFERENCES problems(id);


--
-- TOC entry 1907 (class 2606 OID 25230)
-- Dependencies: 1862 1552 1540
-- Name: questions_usersid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY questions
    ADD CONSTRAINT questions_usersid_fkey FOREIGN KEY (usersid) REFERENCES users(id);


--
-- TOC entry 1910 (class 2606 OID 25250)
-- Dependencies: 1874 1554 1548
-- Name: results_submitsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY results
    ADD CONSTRAINT results_submitsid_fkey FOREIGN KEY (submitsid) REFERENCES submits(id);


--
-- TOC entry 1911 (class 2606 OID 25265)
-- Dependencies: 1556 1554 1882
-- Name: results_testsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY results
    ADD CONSTRAINT results_testsid_fkey FOREIGN KEY (testsid) REFERENCES tests(id);


--
-- TOC entry 1898 (class 2606 OID 25295)
-- Dependencies: 1546 1542 1872
-- Name: roles_contestsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_contestsid_fkey FOREIGN KEY (contestsid) REFERENCES contests(id);


--
-- TOC entry 1899 (class 2606 OID 25300)
-- Dependencies: 1544 1542 1868
-- Name: roles_seriesid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_seriesid_fkey FOREIGN KEY (seriesid) REFERENCES series(id);


--
-- TOC entry 1900 (class 2606 OID 25225)
-- Dependencies: 1544 1546 1872
-- Name: series_contestsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY series
    ADD CONSTRAINT series_contestsid_fkey FOREIGN KEY (contestsid) REFERENCES contests(id);


--
-- TOC entry 1902 (class 2606 OID 25280)
-- Dependencies: 1886 1548 1558
-- Name: submits_languagesid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY submits
    ADD CONSTRAINT submits_languagesid_fkey FOREIGN KEY (languagesid) REFERENCES languages(id);


--
-- TOC entry 1901 (class 2606 OID 25255)
-- Dependencies: 1548 1876 1550
-- Name: submits_problemsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY submits
    ADD CONSTRAINT submits_problemsid_fkey FOREIGN KEY (problemsid) REFERENCES problems(id);


--
-- TOC entry 1903 (class 2606 OID 25285)
-- Dependencies: 1540 1548 1862
-- Name: submits_usersid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY submits
    ADD CONSTRAINT submits_usersid_fkey FOREIGN KEY (usersid) REFERENCES users(id);


--
-- TOC entry 1912 (class 2606 OID 25290)
-- Dependencies: 1550 1556 1876
-- Name: tests_problemsid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tests
    ADD CONSTRAINT tests_problemsid_fkey FOREIGN KEY (problemsid) REFERENCES problems(id);


--
-- TOC entry 1915 (class 2606 OID 25318)
-- Dependencies: 1542 1866 1564
-- Name: users_roles_rolesid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY users_roles
    ADD CONSTRAINT users_roles_rolesid_fkey FOREIGN KEY (rolesid) REFERENCES roles(id);


--
-- TOC entry 1914 (class 2606 OID 25313)
-- Dependencies: 1862 1540 1564
-- Name: users_roles_usersid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY users_roles
    ADD CONSTRAINT users_roles_usersid_fkey FOREIGN KEY (usersid) REFERENCES users(id);


--
-- TOC entry 1936 (class 0 OID 0)
-- Dependencies: 3
-- Name: public; Type: ACL; Schema: -; Owner: -
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM konkinf;
GRANT ALL ON SCHEMA public TO konkinf;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2009-10-02 21:59:56 CEST

--
-- PostgreSQL database dump complete
--

