use brihaspati;
ALTER TABLE ASSIGNMENT DROP PRIMARY KEY;
ALTER TABLE ASSIGNMENT modify ASSIGN_ID VARCHAR(255) UNIQUE NOT NULL;
ALTER TABLE ASSIGNMENT ADD PRIMARY KEY (ID);
