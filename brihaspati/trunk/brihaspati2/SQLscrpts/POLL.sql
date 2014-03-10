use brihaspati;

drop table if exists POLL;

CREATE TABLE POLL
(
	QUESTION_ID INTEGER NOT NULL AUTO_INCREMENT,
        QUESTION_TYPE INTEGER (10),
        QUESTION VARCHAR (255),
        RESULT_YES INTEGER (10),
        RESULT_NO INTEGER (10) ,
        RESULT_CAN INTEGER (10),
 	RESULT_PERCENTAGE_YES INTEGER (10) ,
	RESULT_PERCENTAGE_NO INTEGER (10) ,
	RESULT_PERCENTAGE_CAN INTEGER (10),
	INSTITUTE_ID INTEGER (10),
	USER_ID VARCHAR (15),
	INSERT_DATE DATETIME,
	VALID_TILL DATETIME,
	PRIMARY KEY(QUESTION_ID)
);

insert into ID_TABLE (id_table_id, table_name, next_id, quantity) VALUES (166, 'POLL', 1,1);
