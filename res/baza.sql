/* tabele */

CREATE TABLE USERS (    id                  serial         not null    primary key,
                        firstName           text,
                        lastName            text,
                        eMail               text,
                        birthDate           date,
                        login               varchar(20),
                        pass                varchar(10),
                        address             text,
                        school              text,
                        tutor               text,
                        gaduGadu            int,
                        eMailNotification   int
                    );

CREATE TABLE ROLES (    id                  serial         not null    primary key,
                        name                text,
                        addContest          int,
                        editContest         int,
                        delContest          int,
                        addSeries           int,
                        editSeries          int,
                        delSeries           int,
                        contestant          int
                    );

CREATE TABLE SERIES (	id		serial	not null	primary key,
			name		text,
			startDate	date,
			endDate		date,
			freezeDate	date,
			penaltyTime	int
			);

CREATE TABLE CONTESTS (	id		serial	not null	primary key,
			name		text,
			type		int,
			startDate	date,
			about		text,
			rules		text,
			tech		text,
			visibility	int
			);

CREATE TABLE SUBMITS (	id		serial	not null	primary key,
			sDate		date,
			result		int,
			code		text,
			filename	text
			);

CREATE TABLE TASKS (	id	serial	not null	primary key,
			name	text,
			text	text,
			pdf	varchar(70),
			abbrev	varchar(10),
			memlimit int
			);

CREATE TABLE QUESTIONS (	id		serial	not null	primary key,
				question	text,
				answer		text,
				visibility	int
				);

CREATE TABLE RESULTS (	id	serial	not null	primary key,
			points	int,
			runTime	int,
			memory	int,
			notes	text
			);

CREATE TABLE TESTS (	id		serial	not null	primary key,
			input		text,
			output		text,
			timeLimit	int,
			maxPoints	int,
			visibility	int
			);

CREATE TABLE LANGUAGES (	id		serial	not null	primary key,
				name		text,
				extension	varchar(7)
				);

CREATE TABLE CLASSES (		id		serial	not null	primary key,
				filename	text,
				version		int,
				description	text
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
				id		serial not null primary key,
				taskId		int REFERENCES TASKS(id),
				questionId	int REFERENCES QUESTIONS(id)
				);
CREATE TABLE USERS_ROLES ( 	id		serial not null primary key,
				userId 		int REFERENCES USERS(id),
			   	roleId		int REFERENCES ROLES(id)
				);

CREATE TABLE SERIES_ROLES ( 	id		serial not null primary key,
				seriesId	int REFERENCES SERIES(id),
			   	roleId		int REFERENCES ROLES(id)
				);

CREATE TABLE CONTESTS_ROLES ( 	id		serial not null primary key,
				contestsId 	int REFERENCES CONTESTS(id),
			   	roleId		int REFERENCES ROLES(id)
				);

CREATE TABLE LANGUAGES_TASKS ( 	id		serial not null primary key,
				tasksId 	int REFERENCES TASKS(id),
			   	languagesId	int REFERENCES LANGUAGES(id)
				);
				