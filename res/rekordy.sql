-- roles

INSERT INTO roles (id, name, edituser, addcontest, editcontest, delcontest, addseries, editseries, delseries, addproblem, editproblem, delproblem, canrate, contestant) VALUES (default, 'superadmin', true, true, true, true, true, true, true, true, true, true, true, true);

INSERT INTO roles (id, name, edituser, addcontest, editcontest, delcontest, addseries, editseries, delseries, addproblem, editproblem, delproblem, canrate, contestant) VALUES (default, 'admin', false, true, true, true, true, true, true, true, true, true, true, true);

INSERT INTO roles (id, name, edituser, addcontest, editcontest, delcontest, addseries, editseries, delseries, addproblem, editproblem, delproblem, canrate, contestant) VALUES (default, 'autor', false, false, false, false, true, true, true, true, true, true, true, true);

-- users

INSERT INTO users (id, firstname, lastname, email, birthdate, login, pass, address, school, tutor, emailnotification) VALUES (default, 'Administrator', 'Administracyjny', 'konkinf@mat.umk.pl', '1986-01-05 00:00:00', 'admin', 'OLAT', '', '', '', NULL);

-- user <--> roles

INSERT INTO users_roles (id, usersid, rolesid, contestsid, seriesid) VALUES (default, 1, 1, null, null);

