ALTER TABLE HOTLINE.APPEL
ADD (RESOLU NUMBER(1));

-- Mise à jour des statuts
-- 4 : OUVERTE	
-- 5 : CLOTURE	
-- 6 : TRANSFERE_A	
-- 7 : ATRAITER	
-- 8 : HORSCIBLE	
-- 9 : APPELSORTANT	
-- 221 : ACQUITTEMENT		
-- 261 : TRANSFERE_EX	

update hotline.appel a
set A.RESOLU = 1
where A.CLOTURE_CODE in(4,6,8,9,261);

update hotline.appel a
set A.RESOLU = 0
where A.CLOTURE_CODE in(7,221);

update hotline.appel a
set A.RESOLU = 0
where A.CLOTURE_CODE = 5
    and exists(select e.appel_id 
                from evenement.evenement e
                where E.APPEL_ID = a.id )
                
update hotline.appel a
set A.RESOLU = 1
where A.CLOTURE_CODE = 5
    and not exists(select e.appel_id 
                from evenement.evenement e
                where E.APPEL_ID = a.id )                
