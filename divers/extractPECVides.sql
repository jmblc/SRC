select e.ID idEvenement, E.DATE_CREATION dateEvenement,se.libelle statutEvenement,
       E.APPEL_ID idFiche,cc.libelle statutFiche,
       m.libelle,pp.nom,pp.prenom,
       e.COMMENTAIRE, 
       e.COL01 nomEtab, e.COL02 finessEtab, e.COL03 telEtab, e.COL04 faxEtab, e.COL05 adr1Etab, e.COL06 adr2Etab, e.COL07 adr3Etab, e.COL08 cpEtab, e.COL09 villeEtab, 
       e.COL10 numEntree,e.COL11 codeDMT, e.COL13 dateEntree,
       DECODE(e.COL18,1,'chirurgie',2,'medecine',3,'maternite',4,'psychiatrie',5,'maisonreposconvalescence',e.COL18) typeHospi, 
       DECODE(e.COL23,1,'hospitalisationComplete',2,'hospitalisationDeJour',3,'hospitalisationAmbulatoire',4,'hospitalisationDomicile',e.COL23) modeHospi, 
       e.COL15 fraisSejour, e.COL16 fraisForfait18, e.COL17 fraisForfaitJour, 
       e.COL24 fraisChambPart, e.COL32 fraisLitAcc, e.COL33 fraisHonoraire,
       e.EMAIL
from evenement.evenement e,
     application.personne pp,
     application.mutuelle m,
     hotline.appel        a,
     hotline.codes        cc,
     evenement.statut     se            
where E.JDOCLASS='demandePEC'
    and E.COURRIER_ID is null
    and pp.id = E.PERSONNE_ID
    and m.id = E.MUTUELLE_ID
    and a.id = E.APPEL_ID
    and A.CLOTURE_CODE = cc.code
    and E.STATUT_ID = se.id
    and se.libelle='Envoi en attente'
    and CC.LIBELLE='Clôturé'
order by pp.nom,E.DATE_CREATION    
    
    
    