ALTER TABLE classes DROP CONSTRAINT classes_filename_key;

ALTER TABLE pdf RENAME TO files;
ALTER TABLE files RENAME COLUMN pdf TO bytes;
ALTER TABLE files ADD COLUMN filename VARCHAR(255);
ALTER TABLE files ADD COLUMN extension VARCHAR(8);

ALTER TABLE problems RENAME pdfId TO filesId;

UPDATE files SET filename=abbrev,extension='pdf' FROM problems WHERE filesid=files.id;

