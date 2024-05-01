SELECT link.ID,app.name as appName,app.id as appId,iden.name as identityName,link.native_identity as native_iden FROM spt_link link,spt_application app, spt_identity iden WHERE link.identity_id IN (SELECT identity_id FROM spt_link aa  WHERE STR_TO_DATE(ExtractValue(aa.attributes, '/Attributes/Map/entry[@key="last_login"]/@value'), '%d/%m/%Y') <= CURDATE() AND aa.application='7f0001018e7f1afc818e80a4c7710162') AND  link.application=app.id AND link.identity_id=iden.id AND ExtractValue(iden.attributes, '/Attributes/Map/entry[@key="region"]/@value') ='';
