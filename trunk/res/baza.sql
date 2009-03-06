/* tabele */

CREATE TABLE USERS (
        id                  serial primary key,
        firstName           varchar(40),
        lastName            varchar(40),
        eMail               varchar(40),
        birthDate           date,
        login               varchar(20),
        pass                varchar(32),
        address             varchar(80),
        school              varchar(80),
        tutor               text,
        eMailNotification   int
);

CREATE TABLE ROLES (  
        id                  serial primary key,
        name                varchar(40),
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
        startDate           date,
        endDate             date,
        freezeDate          date,
        unfreezeDate        date,
        penaltyTime         int
);

CREATE TABLE CONTESTS (
        id                  serial primary key,
        name                varchar(120),
        type                int,
        startDate           date,
        about               text,
        rules               text,
        tech                text,
        visibility          int
);

CREATE TABLE SUBMITS ( 
        id                  serial primary key,
        sDate               date,
        result              int,
        code                bytea,
        filename            varchar(255),
        notes               text
);

CREATE TABLE TASKS ( 
        id                  serial primary key,
        name                varchar(80),
        text                text,
        pdf                 bytea,
        abbrev              varchar(5),
        memlimit            int
);

CREATE TABLE QUESTIONS ( 
        id                  serial primary key,
        question            text,
        answer              text,
        visibility          int
);

CREATE TABLE RESULTS (
        id                  serial primary key,
        points              int,
        runTime             int,
        memory              int,
        notes               text
);

CREATE TABLE TESTS (   
        id                  serial primary key,
        input               text,
        output              text,
        timeLimit           int,
        maxPoints           int,
        visibility          int
);

CREATE TABLE LANGUAGES ( 
        id                  serial primary key,
        name                varchar(40),
        extension           varchar(8)
);

CREATE TABLE CLASSES ( 
        id                  serial primary key,
        filename            varchar(255),
        version             int,
        description         varchar(255),
        code                bytea
);

/* Powiazadania jeden do wielu */
ALTER TABLE SERIES ADD contestsId int REFERENCES CONTESTS(id);
ALTER TABLE QUESTIONS ADD userId int REFERENCES USERS(id);
ALTER TABLE QUESTIONS ADD contestsId int REFERENCES CONTESTS(id);
ALTER TABLE TASKS ADD seriesId int REFERENCES SERIES(id);
ALTER TABLE RESULTS ADD submitId int REFERENCES SUBMITS(id);
ALTER TABLE SUBMITS ADD tasksId int REFERENCES TASKS(id);
ALTER TABLE QUESTIONS ADD taksId int REFERENCES TASKS(id);
ALTER TABLE RESULTS ADD testsId int REFERENCES TESTS(id);
ALTER TABLE LANGUAGES ADD classesId int REFERENCES CLASSES(id);
ALTER TABLE TASKS ADD classesId int REFERENCES CLASSES(id);

/* Powiazania wiele do wielu */
CREATE TABLE TASKS_QUESTIONS (
        id                  serial primary key,
        taskId              int REFERENCES TASKS(id),
        questionId          int REFERENCES QUESTIONS(id)
);
CREATE TABLE USERS_ROLES (  
        id                  serial primary key,
        userId              int REFERENCES USERS(id),
        roleId              int REFERENCES ROLES(id)
);

CREATE TABLE SERIES_ROLES ( 
        id                  serial primary key,
        seriesId            int REFERENCES SERIES(id),
        roleId              int REFERENCES ROLES(id)
);

CREATE TABLE CONTESTS_ROLES ( 
        id                  serial primary key,
        contestsId          int REFERENCES CONTESTS(id),
        roleId              int REFERENCES ROLES(id)
);

CREATE TABLE LANGUAGES_TASKS (
        id                  serial primary key,
        tasksId             int REFERENCES TASKS(id),
        languagesId         int REFERENCES LANGUAGES(id)
);
