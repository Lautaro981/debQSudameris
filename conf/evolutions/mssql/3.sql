# --- !Ups

 --- Script para hacer los INSERT de las configuraciones

  --- general Configurations
INSERT INTO service (name) VALUES
('general');

INSERT INTO attribute (service_id,attribute, attribute_value) VALUES
((SELECT TOP 1 id FROM service where name = 'general'), 'Template Btinreq',
'{
    "Device": "[*.device.*]",
    "Usuario": "[*.usuario.*]",
    "Requerimiento": "1",
    "Canal": "BTINTERNO",
    "Token": "[*.token.*]"
}'),
((SELECT TOP 1 id FROM service where name = 'general'), 'Template Parametros',
'{
    "Parametro": {
        "sBTRepParametros.It": [{
            "Tipo": "Texto",
            "Nombre": "DOC",
            "Codigo": 1,
            "Valor": "[*.valorDoc.*]"
        }]
    }
}'),
((SELECT TOP 1 id FROM service where name = 'general'), 'ip','10.100.9.90');

 --- authentication Configurations
 INSERT INTO service (name) VALUES
('authentication');
INSERT INTO attribute (service_id,attribute, attribute_value) VALUES
((SELECT TOP 1 id FROM service where name = 'authentication'), 'url', 'http://10.100.14.2:9280/bantotal/servlet/com.dlya.bantotal.odwsbt_Authenticate?Execute'),
((SELECT TOP 1 id FROM service where name = 'authentication'), 'userId', 'DBMEDIAUSR'),
((SELECT TOP 1 id FROM service where name = 'authentication'), 'password', ''),
((SELECT TOP 1 id FROM service where name = 'authentication'), 'timeout', '10000');

 --- segmentoPersona Configurations
INSERT INTO service (name) VALUES
('segmentoPersona');
INSERT INTO attribute (service_id,attribute, attribute_value) VALUES
((SELECT TOP 1 id FROM service where name = 'segmentoPersona'), 'url', 'http://10.100.14.2:9280/bantotal/servlet/com.dlya.bantotal.odwsbt_ConsultaReportesSQL?SegmentoPersona'),
((SELECT TOP 1 id FROM service where name = 'segmentoPersona'), 'timeout', '10000');

 --- chequeraPendienteRetiro Configurations
INSERT INTO service (name) VALUES
('chequeraPendienteRetiro');
INSERT INTO attribute (service_id,attribute, attribute_value) VALUES
((SELECT TOP 1 id FROM service where name = 'chequeraPendienteRetiro'), 'url', 'http://10.100.14.2:9280/bantotal/servlet/com.dlya.bantotal.odwsbt_ConsultaReportesSQL?ChequeraPendRetiro'),
((SELECT TOP 1 id FROM service where name = 'chequeraPendienteRetiro'), 'timeout', '10000');

 --- tarjetaPendienteEntrega Configurations
INSERT INTO service (name) VALUES
('tarjetaPendienteEntrega');
INSERT INTO attribute (service_id,attribute, attribute_value) VALUES
((SELECT TOP 1 id FROM service where name = 'tarjetaPendienteEntrega'), 'url', 'http://10.100.14.2:9280/bantotal/servlet/com.dlya.bantotal.odwsbt_ConsultaReportesSQL?TarjPendEntrega'),
((SELECT TOP 1 id FROM service where name = 'tarjetaPendienteEntrega'), 'timeout', '10000');

# --- !Downs

DELETE FROM attribute WHERE service_id = (SELECT TOP 1 id FROM service where name = 'tarjetaPendienteEntrega');
DELETE FROM service where name = 'tarjetaPendienteEntrega';

DELETE FROM attribute WHERE service_id = (SELECT TOP 1 id FROM service where name = 'chequeraPendienteRetiro');
DELETE FROM service where name = 'chequeraPendienteRetiro';

DELETE FROM attribute WHERE service_id = (SELECT TOP 1 id FROM service where name = 'segmentoPersona');
DELETE FROM service where name = 'segmentoPersona';

DELETE FROM attribute WHERE service_id = (SELECT TOP 1 id FROM service where name = 'authentication');
DELETE FROM service where name = 'authentication';

DELETE FROM attribute WHERE service_id = (SELECT TOP 1 id FROM service where name = 'general');
DELETE FROM service where name = 'general';