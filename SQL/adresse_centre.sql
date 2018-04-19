CREATE TABLE HOTLINE.T_ADRESSECENTREGESTION_ACG(
	ACG_ID		 	NUMBER(4),
	ACG_SCE_ID		NUMBER(6),
	ACG_LIBELLE		VARCHAR2(64),
	ACG_ADRESSE 	VARCHAR2(256),
	ACG_TEL 		VARCHAR2(20),
	ACG_FAX 		VARCHAR2(20),
	ACG_MAIL		VARCHAR2(96)
)
TABLESPACE H_TBS_HCONTACTS_DATA;

ALTER TABLE HOTLINE.T_ADRESSECENTREGESTION_ACG ADD 
CONSTRAINT PK_ACG
 PRIMARY KEY (ACG_ID)
 ENABLE
 VALIDATE;

ALTER TABLE HOTLINE.T_ADRESSECENTREGESTION_ACG ADD 
CONSTRAINT FK_ACG_SCE
 FOREIGN KEY (ACG_SCE_ID)
 REFERENCES HOTLINE.SCENARIO(ID)
 ENABLE
 VALIDATE;
 
 GRANT INSERT,SELECT,UPDATE,DELETE ON HOTLINE.T_ADRESSECENTREGESTION_ACG TO U_HCONTACTS;

CREATE SEQUENCE HOTLINE.SEQ_ID_ACG START WITH 1 MAXVALUE 999 MINVALUE 1 NOCYCLE NOCACHE NOORDER;
GRANT SELECT ON HOTLINE.SEQ_ID_ACG TO U_HCONTACTS;