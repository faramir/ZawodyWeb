/* tabele */

CREATE TABLE USERS (    id                  int         not null    primary key,
                        firstName           varchar(40),
                        lastName            varchar(40),
                        eMail               varchar(40),
                        birthDate           date,
                        login               varchar(20),
                        pass                varchar(10),
                        address             varchar(40),
                        school              varchar(40),
                        tutor               varchar(40),
                        gaduGadu            int,
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
			penaltyTime	int
			);

CREATE TABLE CONTESTS (	id		int	not null	primary key,
			name		varchar(40),
			type		int,
			startDate	date,
			about		varchar(8000),
			rules		varchar(8000),
			tech		varchar(8000),
			visibility	int
			);

CREATE TABLE SUBMITS (	id		int	not null	primary key,
			sDate		date,
			result		int,
			code		varchar(8000),
			filename	varchar(70)
			);

CREATE TABLE TASKS (	id	int	not null	primary key,
			name	varchar(40),
			text	varchar(8000),
			pdf	varchar(70),
			abbrev	varchar(10),
			memlimit int
			);

CREATE TABLE QUESTIONS (	id		int	not null	primary key,
				question	varchar(255),
				answer		varchar(255),
				visibility	int
				);

CREATE TABLE RESULTS (	id	int	not null	primary key,
			points	int,
			runTime	int,
			memory	int,
			notes	varchar(255)
			);

CREATE TABLE TESTS (	id		int	not null	primary key,
			input		varchar(8000),
			output		varchar(8000),
			timeLimit	int,
			maxPoints	int,
			visibility	int
			);

CREATE TABLE LANGUAGES (	id		int	not null	primary key,
				name		varchar(40),
				extension	varchar(5)
				);

CREATE TABLE CLASSES (		id		int	not null	primary key,
				filename	varchar(70),
				version		int,
				description	varchar(80)
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
				