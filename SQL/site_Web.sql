CREATE TABLE T_SITEWEBENTITE_SWB(
    ENTITE_ID NUMBER(4),
    SITE      VARCHAR2(12),
    URL		  VARCHAR2(64)	
)
TABLESPACE H_TBS_HCONTACTS_DATA;

GRANT INSERT,SELECT,UPDATE,DELETE ON T_SITEWEBENTITE_SWB TO U_HCONTACTS;     