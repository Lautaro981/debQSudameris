# --- !Ups
UPDATE attribute
SET attribute_value = 'http://10.100.14.2:9280/bantotal/servlet/com.dlya.bantotal.odwsbt_Authenticate'
WHERE service_id = (SELECT TOP 1 id FROM service where name = 'authentication') and attribute = 'url';

UPDATE attribute
SET attribute_value = 'http://10.100.14.2:9280/bantotal/servlet/com.dlya.bantotal.odwsbt_ConsultaReportesSQL'
WHERE service_id = (SELECT TOP 1 id FROM service where name = 'segmentoPersona') and attribute = 'url';

UPDATE attribute
SET attribute_value = 'http://10.100.14.2:9280/bantotal/servlet/com.dlya.bantotal.odwsbt_ConsultaReportesSQL'
WHERE service_id = (SELECT TOP 1 id FROM service where name = 'chequeraPendienteRetiro') and attribute = 'url';

UPDATE attribute
SET attribute_value = 'http://10.100.14.2:9280/bantotal/servlet/com.dlya.bantotal.odwsbt_ConsultaReportesSQL'
WHERE service_id = (SELECT TOP 1 id FROM service where name = 'tarjetaPendienteEntrega') and attribute = 'url';

# --- !Downs
