/* tabele */

CREATE TABLE USERS (    id                  int         not null    primary key,
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

CREATE TABLE ROLES (    id                  int         not null    primary key,
                        name                varchar(40),
                        addContest          int,
                        editContest         int,
                        delContest          int,
                        addSeries           int,
                        editSeries          int,
                        delSeries           int,
                        contestant          int
                    );

CREATE TABLE SERIES (	id		int	not null	primary key,
			name		varchar(40),
			startDate	date,
			endDate		date,
			freezeDate	date,
			unfreezeDate	date,
			penaltyTime	int
			);

CREATE TABLE CONTESTS (	id		int	not null	primary key,
			name		varchar(120),
			type		int,
			startDate	date,
			about		text,
			rules		text,
			tech		text,
			visibility	int
			);

CREATE TABLE SUBMITS (	id		int	not null	primary key,
			sDate		date,
			result		int,
			code		bytea,
			filename	varchar(255),
            notes       text
			);

CREATE TABLE TASKS (	id	int	not null	primary key,
			name	varchar(80),
			text	text,
			pdf	bytea,
			abbrev	varchar(5),
			memlimit int
			);

CREATE TABLE QUESTIONS (	id		int	not null	primary key,
				question	text,
				answer		text,
				visibility	int
				);

CREATE TABLE RESULTS (	id	int	not null	primary key,
			points	int,
			runTime	int,
			memory	int,
			notes	text
			);

CREATE TABLE TESTS (	id		int	not null	primary key,
			input		text,
			output		text,
			timeLimit	int,
			maxPoints	int,
			visibility	int
			);

CREATE TABLE LANGUAGES (	id		int	not null	primary key,
				name		varchar(40),
				extension	varchar(8)
				);

CREATE TABLE CLASSES (		id		int	not null	primary key,
				filename	varchar(255),
				version		int,
				description	varchar(255)
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
CREATE TABLE USERS_ROLES ( 	id		int not null primary key,
				userId 		int REFERENCES USERS(id),
			   	roleId		int REFERENCES ROLES(id)
				);

CREATE TABLE SERIES_ROLES ( 	id		int not null primary key,
				seriesId	int REFERENCES SERIES(id),
			   	roleId		int REFERENCES ROLES(id)
				);

CREATE TABLE CONTESTS_ROLES ( 	id		int not null primary key,
				contestsId 	int REFERENCES CONTESTS(id),
			   	roleId		int REFERENCES ROLES(id)
				);

CREATE TABLE LANGUAGES_TASKS ( 	id		int not null primary key,
				tasksId 	int REFERENCES TASKS(id),
			   	languagesId	int REFERENCES LANGUAGES(id)
				);

CREATE TABLE SUBMITS_RESULTS_TESTS ( 	id		int not null primary key,
				submitsId 	int REFERENCES SUBMITS(id),
			   	resultsId	int REFERENCES RESULTS(id),
			   	testsId		int REFERENCES TESTS(id)
				);

