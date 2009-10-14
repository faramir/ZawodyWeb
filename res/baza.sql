-- > createuser -P zawody
-- > createdb -O zawody -E UTF8 zawodyweb

/* tabele */

CREATE TABLE USERS (
        id                  serial primary key,
        firstName           varchar(40),
        lastName            varchar(40),
        eMail               varchar(40) unique,
        birthDate           timestamp,
        login               varchar(20) unique,
        pass                varchar(40),
        address             varchar(80),
        school              varchar(80),
        tutor               text,
        eMailNotification   int
);

CREATE TABLE ROLES (  
        id                  serial primary key,
        name                varchar(40) unique,
	editUser            boolean,
        addContest          boolean,
        editContest         boolean,
        delContest          boolean,
        addSeries           boolean,
        editSeries          boolean,
        delSeries           boolean,
        addProblem          boolean,
        editProblem         boolean,
        delProblem          boolean,
        canRate             boolean,
        contestant          boolean
);

CREATE TABLE SERIES (  
        id                  serial primary key,
        name                varchar(40),
        startDate           timestamp,
        endDate             timestamp,
        freezeDate          timestamp,
        unfreezeDate        timestamp,
        penaltyTime         int
);

CREATE TABLE CONTESTS (
        id                  serial primary key,
        name                varchar(120) unique,
        type                int,
        pass                varchar(32),
        startDate           timestamp,
        about               text,
        rules               text,
        tech                text,
        rankingRefreshRate  int,
        visibility          int
);

CREATE TABLE SUBMITS ( 
        id                  serial primary key,
        sDate               timestamp,
        result              int,
        code                bytea,
        filename            varchar(255),
        notes               text
);

CREATE TABLE PROBLEMS ( 
        id                  serial primary key,
        name                varchar(80),
        text                text,
        abbrev              varchar(5),
        memlimit            int,
        codesize            int,
        config              text
);

CREATE TABLE QUESTIONS ( 
        id                  serial primary key,
        subject             varchar(50),
        question            text,
        visibility          int,
        qtype               int
);

CREATE TABLE RESULTS (
        id                  serial primary key,
        points              int,
        runTime             int,
        memory              int,
        notes               text,
	submitresult        int
);

CREATE TABLE TESTS (   
        id                  serial primary key,
        input               text,
        output              text,
        timeLimit           int,
        maxPoints           int,
        visibility          int,
	testorder	    int
);

CREATE TABLE LANGUAGES ( 
        id                  serial primary key,
        name                varchar(40) unique,
        extension           varchar(8)
);

CREATE TABLE CLASSES ( 
        id                  serial primary key,
        filename            varchar(255) unique,
        version             int,
        description         varchar(255),
        code                bytea
);

CREATE TABLE PDF (  
        id              serial primary key,
        pdf             bytea
);


/* Powiazadania jeden do wielu */
ALTER TABLE SERIES ADD contestsId int REFERENCES CONTESTS(id);
ALTER TABLE QUESTIONS ADD usersId int REFERENCES USERS(id);
ALTER TABLE QUESTIONS ADD contestsId int REFERENCES CONTESTS(id);
ALTER TABLE PROBLEMS ADD seriesId int REFERENCES SERIES(id);
ALTER TABLE PROBLEMS ADD pdfId int REFERENCES PDF(id);
ALTER TABLE RESULTS ADD submitsId int REFERENCES SUBMITS(id);
ALTER TABLE SUBMITS ADD problemsId int REFERENCES PROBLEMS(id);
ALTER TABLE QUESTIONS ADD tasksId int REFERENCES PROBLEMS(id);
ALTER TABLE RESULTS ADD testsId int REFERENCES TESTS(id);
ALTER TABLE LANGUAGES ADD classesId int REFERENCES CLASSES(id);
ALTER TABLE PROBLEMS ADD classesId int REFERENCES CLASSES(id);
ALTER TABLE SUBMITS ADD languagesId int REFERENCES LANGUAGES(id);
ALTER TABLE SUBMITS ADD usersId int REFERENCES USERS(id);
ALTER TABLE TESTS ADD problemsId int REFERENCES PROBLEMS(id);
ALTER TABLE ROLES ADD contestsId int REFERENCES CONTESTS(id);
ALTER TABLE ROLES ADD seriesId int REFERENCES SERIES(id);


/* Powiazania wiele do wielu */
CREATE TABLE USERS_ROLES (  
        id                  serial primary key,
        usersId             int REFERENCES USERS(id),
        rolesId             int REFERENCES ROLES(id)
);

CREATE TABLE LANGUAGES_PROBLEMS (
        id                  serial primary key,
        problemsId          int REFERENCES PROBLEMS(id),
        languagesId         int REFERENCES LANGUAGES(id)
);

