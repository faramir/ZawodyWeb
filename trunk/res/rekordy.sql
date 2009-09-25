-- roles

INSERT INTO roles (id, name, edituser, addcontest, editcontest, delcontest, addseries, editseries, delseries, addproblem, editproblem, delproblem, canrate, contestant, contestsid, seriesid) VALUES (default, 'superadmin', true, true, true, true, true, true, true, true, true, true, true, true, NULL, NULL);

INSERT INTO roles (id, name, edituser, addcontest, editcontest, delcontest, addseries, editseries, delseries, addproblem, editproblem, delproblem, canrate, contestant, contestsid, seriesid) VALUES (default, 'admin', false, true, true, true, true, true, true, true, true, true, true, true, NULL, NULL);

INSERT INTO roles (id, name, edituser, addcontest, editcontest, delcontest, addseries, editseries, delseries, addproblem, editproblem, delproblem, canrate, contestant, contestsid, seriesid) VALUES (default, 'autor', false, false, false, false, true, true, true, true, true, true, true, true, NULL, NULL);

-- users

INSERT INTO users (id, firstname, lastname, email, birthdate, login, pass, address, school, tutor, emailnotification) VALUES (default, 'Administrator', 'Administracyjny', 'konkinf@mat.umk.pl', '1986-01-05 00:00:00', 'admin', '2a203e804c2bf23bf0043b31fa56488743cd780b', '', '', '', NULL); -- C0lours

-- user <--> roles

INSERT INTO users_roles (id, usersid, rolesid) VALUES (default, 1, 1);
