SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET SESSION AUTHORIZATION 'postgres';

CREATE SCHEMA auditoria;

CREATE SCHEMA negocio;

CREATE SCHEMA seguridad;

CREATE SCHEMA soporte;

------------------------------------------------------------------------------------------------------------
-- ESQUEMA SOPORTE =========================================================================================
--**********************************************************************************************************

SET search_path = soporte, pg_catalog;

CREATE TABLE ubigeo (
    id character varying(6) NOT NULL,
    iddepartamento character varying(2),
    idprovincia character varying(2),
    iddistrito character varying(2),
    descripcion character varying(50)
);

CREATE TABLE "Tablamaestra" (
    id integer NOT NULL,
    idmaestro integer DEFAULT 0 NOT NULL,
    nombre character varying(50),
    descripcion character varying(100),
    orden integer,
    estado character(1),
    abreviatura character varying(5)
);

CREATE TABLE "Parametro" (
    id integer NOT NULL,
    nombre character varying(50) NOT NULL,
    descripcion character varying(200),
    valor character varying(50) NOT NULL,
    estado character(1) NOT NULL,
    editable boolean NOT NULL
);

CREATE TABLE destino (
    id integer NOT NULL,
    idcontinente integer NOT NULL,
    idpais integer NOT NULL,
    codigoiata character varying(3) NOT NULL,
    idtipodestino integer NOT NULL,
    descripcion character varying(100) NOT NULL,
    usuariocreacion character varying(15) NOT NULL,
    fechacreacion timestamp with time zone NOT NULL,
    ipcreacion character varying(15) NOT NULL,
    usuariomodificacion character varying(15) NOT NULL,
    fechamodificacion timestamp with time zone NOT NULL,
    ipmodificacion character varying(15) NOT NULL,
    idestadoregistro integer DEFAULT 1 NOT NULL
);

CREATE TABLE pais (
    id integer NOT NULL,
    descripcion character varying(100),
    idcontinente integer NOT NULL,
    usuariocreacion character varying(15) NOT NULL,
    fechacreacion timestamp with time zone NOT NULL,
    ipcreacion character varying(15) NOT NULL,
    usuariomodificacion character varying(15) NOT NULL,
    fechamodificacion timestamp with time zone NOT NULL,
    ipmodificacion character varying(15) NOT NULL,
    idestadoregistro integer DEFAULT 1 NOT NULL
);

ALTER TABLE ONLY destino
    ADD CONSTRAINT pk_destino PRIMARY KEY (id);

ALTER TABLE ONLY pais
    ADD CONSTRAINT pk_pais PRIMARY KEY (id);

ALTER TABLE ONLY "Parametro"
    ADD CONSTRAINT pk_parametro PRIMARY KEY (id);

ALTER TABLE ONLY "Tablamaestra"
    ADD CONSTRAINT pk_tablamaestra PRIMARY KEY (id, idmaestro);

ALTER TABLE ONLY ubigeo
    ADD CONSTRAINT pk_ubigeo PRIMARY KEY (id);

CREATE SEQUENCE seq_destino
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_pais
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



CREATE VIEW vw_catalogodepartamento AS
    SELECT ubigeo.id, ubigeo.iddepartamento, ubigeo.descripcion FROM ubigeo WHERE ((((ubigeo.idprovincia)::text = '00'::text) AND ((ubigeo.iddistrito)::text = '00'::text)) AND ((ubigeo.iddepartamento)::text <> '00'::text));

CREATE VIEW vw_catalogodistrito AS
    SELECT ubigeo.id, ubigeo.iddepartamento, ubigeo.idprovincia, ubigeo.iddistrito, ubigeo.descripcion FROM ubigeo WHERE (((ubigeo.iddepartamento)::text <> '00'::text) AND ((ubigeo.idprovincia)::text <> '00'::text));

CREATE VIEW vw_catalogomaestro AS
    SELECT "Tablamaestra".id, "Tablamaestra".idmaestro, "Tablamaestra".nombre, "Tablamaestra".descripcion FROM "Tablamaestra" WHERE (("Tablamaestra".idmaestro <> 0) AND ("Tablamaestra".estado = 'A'::bpchar));

CREATE VIEW vw_catalogoprovincia AS
    SELECT ubigeo.id, ubigeo.iddepartamento, ubigeo.idprovincia, ubigeo.descripcion FROM ubigeo WHERE ((((ubigeo.iddistrito)::text = '00'::text) AND ((ubigeo.idprovincia)::text <> '00'::text)) AND ((ubigeo.iddepartamento)::text <> '00'::text));

CREATE VIEW vw_listahijosmaestro AS
    SELECT "Tablamaestra".id, "Tablamaestra".idmaestro, "Tablamaestra".nombre, "Tablamaestra".descripcion, "Tablamaestra".orden, "Tablamaestra".estado, CASE WHEN ("Tablamaestra".estado = 'A'::bpchar) THEN 'Activo'::text ELSE 'Inactivo'::text END AS descestado, "Tablamaestra".abreviatura FROM "Tablamaestra" WHERE ("Tablamaestra".idmaestro <> 0);

CREATE VIEW vw_listamaestros AS
    SELECT "Tablamaestra".id, "Tablamaestra".idmaestro, "Tablamaestra".nombre, "Tablamaestra".descripcion, "Tablamaestra".orden, "Tablamaestra".estado, CASE WHEN ("Tablamaestra".estado = 'A'::bpchar) THEN 'ACTIVO'::text ELSE 'INACTIVO'::text END AS descestado FROM "Tablamaestra" WHERE ("Tablamaestra".idmaestro = 0);

CREATE VIEW vw_listaparametros AS
    SELECT "Parametro".id, "Parametro".nombre, "Parametro".descripcion, "Parametro".valor, "Parametro".estado, "Parametro".editable FROM "Parametro";

CREATE VIEW vw_ubigeo AS
    SELECT ubigeo.id, ubigeo.iddepartamento, ubigeo.idprovincia, ubigeo.iddistrito, ubigeo.descripcion FROM ubigeo;

CREATE FUNCTION fn_actualizardestino(p_id integer, p_idcontinente integer, p_idpais integer, p_idtipodestino integer, p_codigoiata character varying, p_descripcion character varying, p_usuariomodificacion character varying, p_ipmodificacion character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

declare fechahoy timestamp with time zone;

begin

select current_timestamp into fechahoy;

UPDATE soporte.destino
   SET idcontinente        = p_idcontinente, 
       idpais              = p_idpais, 
       codigoiata          = p_codigoiata, 
       idtipodestino       = p_idtipodestino, 
       descripcion         = p_descripcion, 
       usuariomodificacion = p_usuariomodificacion, 
       fechamodificacion   = fechahoy, 
       ipmodificacion      = p_ipmodificacion
 WHERE id                  = p_id;
 
return true;

end;
$$;

CREATE FUNCTION fn_actualizarmaestro(p_id integer, p_idtipo integer, p_nombre character varying, p_descripcion character varying, p_estado character varying, p_orden integer, p_abreviatura character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

begin

UPDATE soporte."Tablamaestra"
   SET nombre      = p_nombre,
       descripcion = p_descripcion,
       estado      = p_estado,
       orden       = p_orden,
       abreviatura = p_abreviatura
 WHERE id          = p_id
   AND idmaestro   = p_idtipo;

return true;

end;
$$;

CREATE FUNCTION fn_actualizarparametro(p_id integer, p_nombre character varying, p_descripcion character varying, p_valor character varying, p_estado character varying, p_editable boolean) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

begin

update soporte."Parametro"
   set nombre      = p_nombre,
       descripcion = p_descripcion,
       valor       = p_valor,
       estado      = p_estado,
       editable    = p_editable
 where id          = p_id;


return true;

exception
when others then
  return false;

end;
$$;


CREATE FUNCTION fn_ingresardestino(p_idcontinente integer, p_idpais integer, p_idtipodestino integer, p_codigoiata character varying, p_descripcion character varying, p_usuariocreacion character varying, p_ipcreacion character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

declare maxid integer;
declare fechahoy timestamp with time zone;

begin

maxid = nextval('soporte.seq_destino');

select current_timestamp into fechahoy;

INSERT INTO soporte.destino(
            id, idcontinente, idpais, codigoiata, idtipodestino, descripcion, 
            usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, 
            fechamodificacion, ipmodificacion)
    VALUES (maxid, p_idcontinente, p_idpais, p_codigoiata, p_idtipodestino, p_descripcion, 
            p_usuariocreacion, fechahoy, p_ipcreacion, p_usuariocreacion, 
            fechahoy, p_ipcreacion);

return true;

end;
$$;


CREATE FUNCTION fn_ingresarhijomaestro(p_idmaestro integer, p_nombre character varying, p_descripcion character varying, p_abreviatura character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

declare maxid integer;

begin

select max(id)
  into maxid
  from soporte."Tablamaestra"
 where idmaestro = p_idmaestro;

if (maxid is null) then
maxid = 0;
end if;

maxid = maxid + 1;

INSERT INTO soporte."Tablamaestra"(id, idmaestro, nombre, descripcion, abreviatura, orden, estado)
values (maxid,p_idmaestro,p_nombre,p_descripcion,p_abreviatura,maxid,'A');

return true;

end;
$$;

CREATE FUNCTION fn_ingresarmaestro(p_nombre character varying, p_descripcion character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

declare maxid integer;

begin


select max(id)
  into maxid
  from soporte."Tablamaestra"
 where idmaestro = 0;

if (maxid is null) then
maxid = 0;
end if;

maxid = maxid + 1;

INSERT INTO soporte."Tablamaestra"(id, nombre, descripcion, orden, estado)
values (maxid,p_nombre,p_descripcion,maxid,'A');


return true;


end;
$$;


CREATE FUNCTION fn_ingresarpais(p_descripcion character varying, p_idcontinente integer, p_usuariocreacion character varying, p_ipcreacion character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

declare maxpais integer;
declare fechahoy timestamp with time zone;

begin

maxpais = nextval('soporte.seq_pais');

select current_timestamp into fechahoy;

INSERT INTO soporte.pais(
            id, descripcion, idcontinente, usuariocreacion, fechacreacion, 
            ipcreacion, usuariomodificacion, fechamodificacion, ipmodificacion, 
            idestadoregistro)
    VALUES (maxpais, p_descripcion, p_idcontinente, p_usuariocreacion, fechahoy, 
            p_ipcreacion, p_usuariocreacion, fechahoy, 
            p_ipcreacion, 1);

return true;

end;
$$;


CREATE FUNCTION fn_ingresarparametro(p_nombre character varying, p_descripcion character varying, p_valor character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

declare maxparametro integer;

begin


select max(id)
  into maxparametro
  from soporte."Parametro";

if (maxparametro is null) then
maxparametro = 0;
end if;

maxparametro = maxparametro + 1;

INSERT INTO soporte."Parametro"(id, nombre, descripcion, valor, estado, editable)
values (maxparametro,p_nombre,p_descripcion,p_valor,'A',true);


return true;


end;
$$;


CREATE FUNCTION fn_listardestinos() RETURNS refcursor
    LANGUAGE plpgsql
    AS $$
declare micursor refcursor;

begin

open micursor for
SELECT des.id, des.idcontinente, cont.nombre as nombrecontinente, idpais, pai.descripcion as nombrepais, codigoiata, idtipodestino, tipdes.nombre as nombretipdestino, des.descripcion, 
       des.usuariocreacion, des.fechacreacion, des.ipcreacion, des.usuariomodificacion, 
       des.fechamodificacion, des.ipmodificacion, des.idestadoregistro
  FROM soporte.destino des,
       soporte."Tablamaestra" cont,
       soporte."Tablamaestra" tipdes,
       soporte.pais pai       
 WHERE des.idestadoregistro = 1
   AND cont.idmaestro       = 10
   AND cont.estado          = 'A'
   AND cont.id              = des.idcontinente
   AND pai.idestadoregistro = 1
   AND pai.id               = des.idpais
   AND tipdes.idmaestro     = 11
   AND tipdes.estado        = 'A'
   AND tipdes.id            = des.idtipodestino;

return micursor;

end;
$$;

CREATE FUNCTION fn_listarpaises(p_idcontinente integer) RETURNS refcursor
    LANGUAGE plpgsql
    AS $$
declare micursor refcursor;

begin

open micursor for
SELECT id, descripcion, idcontinente, usuariocreacion, fechacreacion, 
       ipcreacion, usuariomodificacion, fechamodificacion, ipmodificacion, 
       idestadoregistro
  FROM soporte.pais
 WHERE idcontinente = p_idcontinente;


return micursor;

end;
$$;


INSERT INTO "Parametro" VALUES (1, 'tiempo de espera', 'tiempo de espera', '1222', 'A', true);
INSERT INTO "Parametro" VALUES (2, 'sdfsdf', 'dsfdsf', '232', 'A', true);
INSERT INTO "Parametro" VALUES (3, 'sdsd', 'sdsd', 'sdsd', 'A', true);
INSERT INTO "Parametro" VALUES (4, 'dsd', 'sdsad', 'sdasd', 'A', true);
INSERT INTO "Parametro" VALUES (5, 'dasd', 'sdsad', 'sad', 'A', true);
INSERT INTO "Parametro" VALUES (6, 'sdasd', 'sad', 'saddsad', 'A', true);
INSERT INTO "Parametro" VALUES (7, 'ssd', 'asdadad', 'adda', 'A', true);
INSERT INTO "Parametro" VALUES (8, 'aa', 'aaaaa', 'aaaa', 'A', true);
INSERT INTO "Parametro" VALUES (9, 'bbbb', 'bbbb', 'bbbbb', 'A', true);
INSERT INTO "Parametro" VALUES (10, 'ccc', 'cccc', 'ccc', 'A', true);
INSERT INTO "Parametro" VALUES (11, 'ddd', 'ddd', 'ddd', 'A', true);
INSERT INTO "Parametro" VALUES (12, 'eeee', 'eeee', 'eeeee', 'A', true);
INSERT INTO "Parametro" VALUES (13, 'eeee', 'eeee', 'eeeee', 'A', true);
INSERT INTO "Parametro" VALUES (14, 'eeee', 'eeee', 'eeeee', 'A', true);
INSERT INTO "Parametro" VALUES (15, 'qqqq', 'qqqq', 'qqq', 'A', true);
INSERT INTO "Parametro" VALUES (16, 'SDSAD', 'SADSAD', 'ASDSAD', 'A', true);

INSERT INTO "Tablamaestra" VALUES (1, 0, 'MAESTRO DE TIPO DE DOCUMENTO', 'MAESTRO DE TIPO DE DOCUMENTO', 1, 'A', NULL);
INSERT INTO "Tablamaestra" VALUES (2, 0, 'MAESTRO DE VIAS', 'MAESTRO DE VIAS', 2, 'A', NULL);
INSERT INTO "Tablamaestra" VALUES (3, 0, 'MAESTRO DE RUBRO', 'MAESTRO DE RUBRO', 3, 'A', NULL);
INSERT INTO "Tablamaestra" VALUES (1, 1, 'DNI', 'DOCUMENO NACIONAL DE IDENTIDAD', 1, 'A', 'DNI');
INSERT INTO "Tablamaestra" VALUES (2, 1, 'CARNÉ DE EXTRANJERIA', 'CARNÉ DE EXTRANJERIA', 2, 'A', 'CE');
INSERT INTO "Tablamaestra" VALUES (3, 1, 'RUC', 'REGISTRO UNICO DEL CONTRIBUYENTE', 3, 'A', 'RUC');
INSERT INTO "Tablamaestra" VALUES (1, 2, 'AVENIDA', 'AVENIDA', 1, 'A', 'AV');
INSERT INTO "Tablamaestra" VALUES (3, 2, 'JIRON', 'JIRON', 3, 'A', 'JR');
INSERT INTO "Tablamaestra" VALUES (4, 2, 'PASAJE', 'PASAJE', 4, 'A', 'PSJ');
INSERT INTO "Tablamaestra" VALUES (1, 3, 'EDUCACION', 'EDUCACION', 1, 'A', 'EDU');
INSERT INTO "Tablamaestra" VALUES (2, 3, 'SALUD', 'SALUD', 2, 'A', 'SALUD');
INSERT INTO "Tablamaestra" VALUES (3, 3, 'TELEFONIA', 'TELEFONIA', 3, 'A', 'TELEF');
INSERT INTO "Tablamaestra" VALUES (4, 3, 'MECANICA', 'MECANICA', 4, 'A', 'MEC');
INSERT INTO "Tablamaestra" VALUES (1, 4, 'produccion', 'produccion', 1, 'A', 'prod');
INSERT INTO "Tablamaestra" VALUES (2, 4, 'ventas', 'ventas', 2, 'A', 'ven');
INSERT INTO "Tablamaestra" VALUES (3, 4, 'finanzas', 'finanzas', 3, 'A', 'fina');
INSERT INTO "Tablamaestra" VALUES (4, 4, 'compras', 'compras', 4, 'A', 'com');
INSERT INTO "Tablamaestra" VALUES (5, 0, 'MAESTRO DE EMPRESAS OPERADORAS', 'MAESTRO DE EMPRESAS OPERADORAS', 5, 'A', NULL);
INSERT INTO "Tablamaestra" VALUES (1, 5, 'CLARO', 'CLARO', 1, 'A', 'CLA');
INSERT INTO "Tablamaestra" VALUES (2, 5, 'MOVISTAR', 'MOVISTAR', 2, 'A', 'MOV');
INSERT INTO "Tablamaestra" VALUES (3, 5, 'NEXTEL', 'NEXTEL', 3, 'A', 'NEX');
INSERT INTO "Tablamaestra" VALUES (6, 0, 'MAESTRO DE PERSONAS', 'MAESTRO DE PERSONAS', 6, 'A', NULL);
INSERT INTO "Tablamaestra" VALUES (1, 6, 'CLIENTE', 'CLIENTE', 1, 'A', 'CLI');
INSERT INTO "Tablamaestra" VALUES (2, 6, 'PROVEEDOR', 'PROVEEDOR', 2, 'A', 'PRO');
INSERT INTO "Tablamaestra" VALUES (3, 6, 'CONTACTO', 'CONTACTO DE PROVEEDOR', 3, 'A', 'CPRO');
INSERT INTO "Tablamaestra" VALUES (5, 3, 'FERRETERIA', 'FERRETERIA', 5, 'A', 'FER');
INSERT INTO "Tablamaestra" VALUES (6, 3, 'LIBRERIA', 'LIBRERIA', 6, 'A', 'LIB');
INSERT INTO "Tablamaestra" VALUES (7, 3, 'MAYORISTAS', 'AGENCIAS MAYORITAS', 7, 'A', 'AGMAY');
INSERT INTO "Tablamaestra" VALUES (8, 3, 'ASEGURADORAS', 'ASEGURADORAS', 8, 'A', 'ASEG');
INSERT INTO "Tablamaestra" VALUES (9, 3, 'AGENCIA DE VIAJES', 'AGENCIA DE VIAJES', 9, 'A', 'AGE');
INSERT INTO "Tablamaestra" VALUES (2, 2, 'CALLE', 'CALLE', 0, 'A', 'CA');
INSERT INTO "Tablamaestra" VALUES (4, 0, 'MAESTRO DE AREAS', 'MAESTRO DE AREAS LO', 0, 'A', NULL);
INSERT INTO "Tablamaestra" VALUES (5, 4, 'ALMACEN', 'ALMACEN', 5, 'A', 'ALM');
INSERT INTO "Tablamaestra" VALUES (6, 4, 'CONTABILIDAD', 'CONTABILIDAD', 6, 'A', 'CON');
INSERT INTO "Tablamaestra" VALUES (7, 0, 'MAESTRO DE MENSAJES', 'MAESTRO DE MENSAJES', 7, 'A', NULL);
INSERT INTO "Tablamaestra" VALUES (8, 0, 'maestro de bancos', 'maestro de bancos', 8, 'A', NULL);
INSERT INTO "Tablamaestra" VALUES (1, 8, 'banco financiero', 'banco financiero del peru', 1, 'A', 'bfp');
INSERT INTO "Tablamaestra" VALUES (2, 8, 'banco interbank', 'banco internacional del peru', 2, 'A', 'ibk');
INSERT INTO "Tablamaestra" VALUES (3, 8, 'banco de credito', 'banco de credito del peru', 3, 'A', 'bcp');
INSERT INTO "Tablamaestra" VALUES (4, 8, 'banco scotiabank', 'banco scotiabank', 4, 'A', 'sco');
INSERT INTO "Tablamaestra" VALUES (5, 8, 'banco bbva continental', 'banco bbva continental', 5, 'A', 'bbva');
INSERT INTO "Tablamaestra" VALUES (6, 8, 'banco citybank', 'banco citybank', 6, 'A', 'citi');
INSERT INTO "Tablamaestra" VALUES (9, 0, 'maestro de estado civil', 'maestro de estado civil', 9, 'A', NULL);
INSERT INTO "Tablamaestra" VALUES (1, 9, 'SOLTERO', 'SOLTERO', 1, 'A', 'SOL');
INSERT INTO "Tablamaestra" VALUES (2, 9, 'CASADO', 'CASADO', 2, 'A', 'CAS');
INSERT INTO "Tablamaestra" VALUES (3, 9, 'DIVORCIADO', 'DIVORCIADO', 3, 'A', 'DIV');
INSERT INTO "Tablamaestra" VALUES (4, 9, 'VIUDO', 'VIUDO', 4, 'A', 'VIU');
INSERT INTO "Tablamaestra" VALUES (10, 3, 'BELLEZA', 'PELUQUERIA Y SPA', 10, 'A', 'BEL');
INSERT INTO "Tablamaestra" VALUES (10, 0, 'MAESTRO DE CONTINENTES', 'MAestro de continentes', 10, 'A', NULL);
INSERT INTO "Tablamaestra" VALUES (1, 10, 'AMERICA DEL SUR', 'AMERCIA DEL SUR', 1, 'A', 'AMSUR');
INSERT INTO "Tablamaestra" VALUES (2, 10, 'AMERICA DEL CENTRO', 'AMERICA DEL CENTRO', 2, 'A', 'AMCEN');
INSERT INTO "Tablamaestra" VALUES (3, 10, 'AMERICA DEL NORTE', 'AMERICA DEL NORTE', 3, 'A', 'AMNOR');
INSERT INTO "Tablamaestra" VALUES (4, 10, 'EUROPA', 'EUROPA', 4, 'A', 'EUR');
INSERT INTO "Tablamaestra" VALUES (5, 10, 'ASIA', 'ASIA', 5, 'A', 'ASI');
INSERT INTO "Tablamaestra" VALUES (6, 10, 'AFRICA', 'AFRICA', 6, 'A', 'AFR');
INSERT INTO "Tablamaestra" VALUES (7, 10, 'OCEANIA', 'OCEANIA', 7, 'A', 'OCE');
INSERT INTO "Tablamaestra" VALUES (11, 0, 'maestro tipo de destino', 'maestro tipo de destino', 11, 'A', NULL);
INSERT INTO "Tablamaestra" VALUES (1, 11, 'PLAYA', 'PLAYA', 1, 'A', 'PLA');
INSERT INTO "Tablamaestra" VALUES (2, 11, 'CIUDAD', 'CIUDAD', 2, 'A', 'CIU');
INSERT INTO "Tablamaestra" VALUES (3, 11, 'NIEVE', 'NIEVE', 3, 'A', 'NIEVE');
INSERT INTO "Tablamaestra" VALUES (4, 11, 'AVENTURA', 'AVENTURA', 4, 'A', 'AVEN');


INSERT INTO pais VALUES (9, 'PERU', 1, 'admin', '2014-06-18 07:39:44.057+02', '127.0.0.1', 'admin', '2014-06-18 07:39:44.057+02', '127.0.0.1', 1);
INSERT INTO pais VALUES (10, 'COLOMBIA', 1, 'admin', '2014-06-18 07:53:23.71+02', '127.0.0.1', 'admin', '2014-06-18 07:53:23.71+02', '127.0.0.1', 1);
INSERT INTO pais VALUES (11, 'VENEZUELA', 1, 'admin', '2014-06-18 07:53:35.21+02', '127.0.0.1', 'admin', '2014-06-18 07:53:35.21+02', '127.0.0.1', 1);
INSERT INTO pais VALUES (12, 'BOLIVIA', 1, 'admin', '2014-06-18 07:53:47.705+02', '127.0.0.1', 'admin', '2014-06-18 07:53:47.705+02', '127.0.0.1', 1);
INSERT INTO pais VALUES (13, 'CHILE', 1, 'admin', '2014-06-18 07:53:55.331+02', '127.0.0.1', 'admin', '2014-06-18 07:53:55.331+02', '127.0.0.1', 1);
INSERT INTO pais VALUES (14, 'ARGENTINA', 1, 'admin', '2014-06-18 07:54:07.654+02', '127.0.0.1', 'admin', '2014-06-18 07:54:07.654+02', '127.0.0.1', 1);

INSERT INTO destino VALUES (2, 1, 9, 'lim', 2, 'lima', 'admin', '2014-06-18 08:02:16.267+02', '127.0.0.1', 'admin', '2014-06-18 08:02:16.267+02', '127.0.0.1', 1);
INSERT INTO destino VALUES (4, 1, 9, 'AQP', 2, 'AREQUIPA', 'ADMIN', '2014-06-19 04:15:24.994+02', '127.0.0.1', 'ADMIN', '2014-06-19 04:15:24.994+02', '127.0.0.1', 1);
INSERT INTO destino VALUES (5, 1, 9, 'TRU', 1, 'TRUJILLO', 'ADMIN', '2014-06-19 04:17:25.086+02', '127.0.0.1', 'ADMIN', '2014-06-19 04:17:25.086+02', '127.0.0.1', 1);

SELECT pg_catalog.setval('seq_destino', 5, true);

SELECT pg_catalog.setval('seq_pais', 14, true);


INSERT INTO ubigeo VALUES ('010000', '01', '00', '00', 'AMAZONAS');
INSERT INTO ubigeo VALUES ('010100', '01', '01', '00', 'CHACHAPOYAS');
INSERT INTO ubigeo VALUES ('010101', '01', '01', '01', 'CHACHAPOYAS');
INSERT INTO ubigeo VALUES ('010102', '01', '01', '02', 'ASUNCION');
INSERT INTO ubigeo VALUES ('010103', '01', '01', '03', 'BALSAS');
INSERT INTO ubigeo VALUES ('010104', '01', '01', '04', 'CHETO');
INSERT INTO ubigeo VALUES ('010105', '01', '01', '05', 'CHILIQUIN');
INSERT INTO ubigeo VALUES ('010106', '01', '01', '06', 'CHUQUIBAMBA');
INSERT INTO ubigeo VALUES ('010107', '01', '01', '07', 'GRANADA');
INSERT INTO ubigeo VALUES ('010108', '01', '01', '08', 'HUANCAS');
INSERT INTO ubigeo VALUES ('010109', '01', '01', '09', 'LA JALCA');
INSERT INTO ubigeo VALUES ('010110', '01', '01', '10', 'LEIMEBAMBA');
INSERT INTO ubigeo VALUES ('010111', '01', '01', '11', 'LEVANTO');
INSERT INTO ubigeo VALUES ('010112', '01', '01', '12', 'MAGDALENA');
INSERT INTO ubigeo VALUES ('010113', '01', '01', '13', 'MARISCAL CASTILLA');
INSERT INTO ubigeo VALUES ('010114', '01', '01', '14', 'MOLINOPAMPA');
INSERT INTO ubigeo VALUES ('010115', '01', '01', '15', 'MONTEVIDEO');
INSERT INTO ubigeo VALUES ('010116', '01', '01', '16', 'OLLEROS');
INSERT INTO ubigeo VALUES ('010117', '01', '01', '17', 'QUINJALCA');
INSERT INTO ubigeo VALUES ('010118', '01', '01', '18', 'SAN FRANCISCO DE DAGUAS');
INSERT INTO ubigeo VALUES ('010119', '01', '01', '19', 'SAN ISIDRO DE MAINO');
INSERT INTO ubigeo VALUES ('010120', '01', '01', '20', 'SOLOCO');
INSERT INTO ubigeo VALUES ('010121', '01', '01', '21', 'SONCHE');
INSERT INTO ubigeo VALUES ('010200', '01', '02', '00', 'BAGUA');
INSERT INTO ubigeo VALUES ('010201', '01', '02', '01', 'BAGUA');
INSERT INTO ubigeo VALUES ('010202', '01', '02', '02', 'ARAMANGO');
INSERT INTO ubigeo VALUES ('010203', '01', '02', '03', 'COPALLIN');
INSERT INTO ubigeo VALUES ('010204', '01', '02', '04', 'EL PARCO');
INSERT INTO ubigeo VALUES ('010205', '01', '02', '05', 'IMAZA');
INSERT INTO ubigeo VALUES ('010206', '01', '02', '06', 'LA PECA');
INSERT INTO ubigeo VALUES ('010300', '01', '03', '00', 'BONGARA');
INSERT INTO ubigeo VALUES ('010301', '01', '03', '01', 'JUMBILLA');
INSERT INTO ubigeo VALUES ('010302', '01', '03', '02', 'CHISQUILLA');
INSERT INTO ubigeo VALUES ('010303', '01', '03', '03', 'CHURUJA');
INSERT INTO ubigeo VALUES ('010304', '01', '03', '04', 'COROSHA');
INSERT INTO ubigeo VALUES ('010305', '01', '03', '05', 'CUISPES');
INSERT INTO ubigeo VALUES ('010306', '01', '03', '06', 'FLORIDA');
INSERT INTO ubigeo VALUES ('010307', '01', '03', '07', 'JAZAN');
INSERT INTO ubigeo VALUES ('010308', '01', '03', '08', 'RECTA');
INSERT INTO ubigeo VALUES ('010309', '01', '03', '09', 'SAN CARLOS');
INSERT INTO ubigeo VALUES ('010310', '01', '03', '10', 'SHIPASBAMBA');
INSERT INTO ubigeo VALUES ('010311', '01', '03', '11', 'VALERA');
INSERT INTO ubigeo VALUES ('010312', '01', '03', '12', 'YAMBRASBAMBA');
INSERT INTO ubigeo VALUES ('010400', '01', '04', '00', 'CONDORCANQUI');
INSERT INTO ubigeo VALUES ('010401', '01', '04', '01', 'NIEVA');
INSERT INTO ubigeo VALUES ('010402', '01', '04', '02', 'EL CENEPA');
INSERT INTO ubigeo VALUES ('010403', '01', '04', '03', 'RIO SANTIAGO');
INSERT INTO ubigeo VALUES ('010500', '01', '05', '00', 'LUYA');
INSERT INTO ubigeo VALUES ('010501', '01', '05', '01', 'LAMUD');
INSERT INTO ubigeo VALUES ('010502', '01', '05', '02', 'CAMPORREDONDO');
INSERT INTO ubigeo VALUES ('010503', '01', '05', '03', 'COCABAMBA');
INSERT INTO ubigeo VALUES ('010504', '01', '05', '04', 'COLCAMAR');
INSERT INTO ubigeo VALUES ('010505', '01', '05', '05', 'CONILA');
INSERT INTO ubigeo VALUES ('010506', '01', '05', '06', 'INGUILPATA');
INSERT INTO ubigeo VALUES ('010507', '01', '05', '07', 'LONGUITA');
INSERT INTO ubigeo VALUES ('010508', '01', '05', '08', 'LONYA CHICO');
INSERT INTO ubigeo VALUES ('010509', '01', '05', '09', 'LUYA');
INSERT INTO ubigeo VALUES ('010510', '01', '05', '10', 'LUYA VIEJO');
INSERT INTO ubigeo VALUES ('010511', '01', '05', '11', 'MARIA');
INSERT INTO ubigeo VALUES ('010512', '01', '05', '12', 'OCALLI');
INSERT INTO ubigeo VALUES ('010513', '01', '05', '13', 'OCUMAL');
INSERT INTO ubigeo VALUES ('010514', '01', '05', '14', 'PISUQUIA');
INSERT INTO ubigeo VALUES ('010515', '01', '05', '15', 'PROVIDENCIA');
INSERT INTO ubigeo VALUES ('010516', '01', '05', '16', 'SAN CRISTOBAL');
INSERT INTO ubigeo VALUES ('010517', '01', '05', '17', 'SAN FRANCISCO DEL YESO');
INSERT INTO ubigeo VALUES ('010518', '01', '05', '18', 'SAN JERONIMO');
INSERT INTO ubigeo VALUES ('010519', '01', '05', '19', 'SAN JUAN DE LOPECANCHA');
INSERT INTO ubigeo VALUES ('010520', '01', '05', '20', 'SANTA CATALINA');
INSERT INTO ubigeo VALUES ('010521', '01', '05', '21', 'SANTO TOMAS');
INSERT INTO ubigeo VALUES ('010522', '01', '05', '22', 'TINGO');
INSERT INTO ubigeo VALUES ('010523', '01', '05', '23', 'TRITA');
INSERT INTO ubigeo VALUES ('010600', '01', '06', '00', 'RODRIGUEZ DE MENDOZA');
INSERT INTO ubigeo VALUES ('010601', '01', '06', '01', 'SAN NICOLAS');
INSERT INTO ubigeo VALUES ('010602', '01', '06', '02', 'CHIRIMOTO');
INSERT INTO ubigeo VALUES ('010603', '01', '06', '03', 'COCHAMAL');
INSERT INTO ubigeo VALUES ('010604', '01', '06', '04', 'HUAMBO');
INSERT INTO ubigeo VALUES ('010605', '01', '06', '05', 'LIMABAMBA');
INSERT INTO ubigeo VALUES ('010606', '01', '06', '06', 'LONGAR');
INSERT INTO ubigeo VALUES ('010607', '01', '06', '07', 'MARISCAL BENAVIDES');
INSERT INTO ubigeo VALUES ('010608', '01', '06', '08', 'MILPUC');
INSERT INTO ubigeo VALUES ('010609', '01', '06', '09', 'OMIA');
INSERT INTO ubigeo VALUES ('010610', '01', '06', '10', 'SANTA ROSA');
INSERT INTO ubigeo VALUES ('010611', '01', '06', '11', 'TOTORA');
INSERT INTO ubigeo VALUES ('010612', '01', '06', '12', 'VISTA ALEGRE');
INSERT INTO ubigeo VALUES ('010700', '01', '07', '00', 'UTCUBAMBA');
INSERT INTO ubigeo VALUES ('010701', '01', '07', '01', 'BAGUA GRANDE');
INSERT INTO ubigeo VALUES ('010702', '01', '07', '02', 'CAJARURO');
INSERT INTO ubigeo VALUES ('010703', '01', '07', '03', 'CUMBA');
INSERT INTO ubigeo VALUES ('010704', '01', '07', '04', 'EL MILAGRO');
INSERT INTO ubigeo VALUES ('010705', '01', '07', '05', 'JAMALCA');
INSERT INTO ubigeo VALUES ('010706', '01', '07', '06', 'LONYA GRANDE');
INSERT INTO ubigeo VALUES ('010707', '01', '07', '07', 'YAMON');
INSERT INTO ubigeo VALUES ('020000', '02', '00', '00', 'ANCASH');
INSERT INTO ubigeo VALUES ('020100', '02', '01', '00', 'HUARAZ');
INSERT INTO ubigeo VALUES ('020101', '02', '01', '01', 'HUARAZ');
INSERT INTO ubigeo VALUES ('020102', '02', '01', '02', 'COCHABAMBA');
INSERT INTO ubigeo VALUES ('020103', '02', '01', '03', 'COLCABAMBA');
INSERT INTO ubigeo VALUES ('020104', '02', '01', '04', 'HUANCHAY');
INSERT INTO ubigeo VALUES ('020105', '02', '01', '05', 'INDEPENDENCIA');
INSERT INTO ubigeo VALUES ('020106', '02', '01', '06', 'JANGAS');
INSERT INTO ubigeo VALUES ('020107', '02', '01', '07', 'LA LIBERTAD');
INSERT INTO ubigeo VALUES ('020108', '02', '01', '08', 'OLLEROS');
INSERT INTO ubigeo VALUES ('020109', '02', '01', '09', 'PAMPAS');
INSERT INTO ubigeo VALUES ('020110', '02', '01', '10', 'PARIACOTO');
INSERT INTO ubigeo VALUES ('020111', '02', '01', '11', 'PIRA');
INSERT INTO ubigeo VALUES ('020112', '02', '01', '12', 'TARICA');
INSERT INTO ubigeo VALUES ('020200', '02', '02', '00', 'AIJA');
INSERT INTO ubigeo VALUES ('020201', '02', '02', '01', 'AIJA');
INSERT INTO ubigeo VALUES ('020202', '02', '02', '02', 'CORIS');
INSERT INTO ubigeo VALUES ('020203', '02', '02', '03', 'HUACLLAN');
INSERT INTO ubigeo VALUES ('020204', '02', '02', '04', 'LA MERCED');
INSERT INTO ubigeo VALUES ('020205', '02', '02', '05', 'SUCCHA');
INSERT INTO ubigeo VALUES ('020300', '02', '03', '00', 'ANTONIO RAYMONDI');
INSERT INTO ubigeo VALUES ('020301', '02', '03', '01', 'LLAMELLIN');
INSERT INTO ubigeo VALUES ('020302', '02', '03', '02', 'ACZO');
INSERT INTO ubigeo VALUES ('020303', '02', '03', '03', 'CHACCHO');
INSERT INTO ubigeo VALUES ('020304', '02', '03', '04', 'CHINGAS');
INSERT INTO ubigeo VALUES ('020305', '02', '03', '05', 'MIRGAS');
INSERT INTO ubigeo VALUES ('020306', '02', '03', '06', 'SAN JUAN DE RONTOY');
INSERT INTO ubigeo VALUES ('020400', '02', '04', '00', 'ASUNCION');
INSERT INTO ubigeo VALUES ('020401', '02', '04', '01', 'CHACAS');
INSERT INTO ubigeo VALUES ('020402', '02', '04', '02', 'ACOCHACA');
INSERT INTO ubigeo VALUES ('020500', '02', '05', '00', 'BOLOGNESI');
INSERT INTO ubigeo VALUES ('020501', '02', '05', '01', 'CHIQUIAN');
INSERT INTO ubigeo VALUES ('020502', '02', '05', '02', 'ABELARDO PARDO LEZAMETA');
INSERT INTO ubigeo VALUES ('020503', '02', '05', '03', 'ANTONIO RAYMONDI');
INSERT INTO ubigeo VALUES ('020504', '02', '05', '04', 'AQUIA');
INSERT INTO ubigeo VALUES ('020505', '02', '05', '05', 'CAJACAY');
INSERT INTO ubigeo VALUES ('020506', '02', '05', '06', 'CANIS');
INSERT INTO ubigeo VALUES ('020507', '02', '05', '07', 'COLQUIOC');
INSERT INTO ubigeo VALUES ('020508', '02', '05', '08', 'HUALLANCA');
INSERT INTO ubigeo VALUES ('020509', '02', '05', '09', 'HUASTA');
INSERT INTO ubigeo VALUES ('020510', '02', '05', '10', 'HUAYLLACAYAN');
INSERT INTO ubigeo VALUES ('020511', '02', '05', '11', 'LA PRIMAVERA');
INSERT INTO ubigeo VALUES ('020512', '02', '05', '12', 'MANGAS');
INSERT INTO ubigeo VALUES ('020513', '02', '05', '13', 'PACLLON');
INSERT INTO ubigeo VALUES ('020514', '02', '05', '14', 'SAN MIGUEL DE CORPANQUI');
INSERT INTO ubigeo VALUES ('020515', '02', '05', '15', 'TICLLOS');
INSERT INTO ubigeo VALUES ('020600', '02', '06', '00', 'CARHUAZ');
INSERT INTO ubigeo VALUES ('020601', '02', '06', '01', 'CARHUAZ');
INSERT INTO ubigeo VALUES ('020602', '02', '06', '02', 'ACOPAMPA');
INSERT INTO ubigeo VALUES ('020603', '02', '06', '03', 'AMASHCA');
INSERT INTO ubigeo VALUES ('020604', '02', '06', '04', 'ANTA');
INSERT INTO ubigeo VALUES ('020605', '02', '06', '05', 'ATAQUERO');
INSERT INTO ubigeo VALUES ('020606', '02', '06', '06', 'MARCARA');
INSERT INTO ubigeo VALUES ('020607', '02', '06', '07', 'PARIAHUANCA');
INSERT INTO ubigeo VALUES ('020608', '02', '06', '08', 'SAN MIGUEL DE ACO');
INSERT INTO ubigeo VALUES ('020609', '02', '06', '09', 'SHILLA');
INSERT INTO ubigeo VALUES ('020610', '02', '06', '10', 'TINCO');
INSERT INTO ubigeo VALUES ('020611', '02', '06', '11', 'YUNGAR');
INSERT INTO ubigeo VALUES ('020700', '02', '07', '00', 'CARLOS FERMIN FITZCARRALD');
INSERT INTO ubigeo VALUES ('020701', '02', '07', '01', 'SAN LUIS');
INSERT INTO ubigeo VALUES ('020702', '02', '07', '02', 'SAN NICOLAS');
INSERT INTO ubigeo VALUES ('020703', '02', '07', '03', 'YAUYA');
INSERT INTO ubigeo VALUES ('020800', '02', '08', '00', 'CASMA');
INSERT INTO ubigeo VALUES ('020801', '02', '08', '01', 'CASMA');
INSERT INTO ubigeo VALUES ('020802', '02', '08', '02', 'BUENA VISTA ALTA');
INSERT INTO ubigeo VALUES ('020803', '02', '08', '03', 'COMANDANTE NOEL');
INSERT INTO ubigeo VALUES ('020804', '02', '08', '04', 'YAUTAN');
INSERT INTO ubigeo VALUES ('020900', '02', '09', '00', 'CORONGO');
INSERT INTO ubigeo VALUES ('020901', '02', '09', '01', 'CORONGO');
INSERT INTO ubigeo VALUES ('020902', '02', '09', '02', 'ACO');
INSERT INTO ubigeo VALUES ('020903', '02', '09', '03', 'BAMBAS');
INSERT INTO ubigeo VALUES ('020904', '02', '09', '04', 'CUSCA');
INSERT INTO ubigeo VALUES ('020905', '02', '09', '05', 'LA PAMPA');
INSERT INTO ubigeo VALUES ('020906', '02', '09', '06', 'YANAC');
INSERT INTO ubigeo VALUES ('020907', '02', '09', '07', 'YUPAN');
INSERT INTO ubigeo VALUES ('021000', '02', '10', '00', 'HUARI');
INSERT INTO ubigeo VALUES ('021001', '02', '10', '01', 'HUARI');
INSERT INTO ubigeo VALUES ('021002', '02', '10', '02', 'ANRA');
INSERT INTO ubigeo VALUES ('021003', '02', '10', '03', 'CAJAY');
INSERT INTO ubigeo VALUES ('021004', '02', '10', '04', 'CHAVIN DE HUANTAR');
INSERT INTO ubigeo VALUES ('021005', '02', '10', '05', 'HUACACHI');
INSERT INTO ubigeo VALUES ('021006', '02', '10', '06', 'HUACCHIS');
INSERT INTO ubigeo VALUES ('021007', '02', '10', '07', 'HUACHIS');
INSERT INTO ubigeo VALUES ('021008', '02', '10', '08', 'HUANTAR');
INSERT INTO ubigeo VALUES ('021009', '02', '10', '09', 'MASIN');
INSERT INTO ubigeo VALUES ('021010', '02', '10', '10', 'PAUCAS');
INSERT INTO ubigeo VALUES ('021011', '02', '10', '11', 'PONTO');
INSERT INTO ubigeo VALUES ('021012', '02', '10', '12', 'RAHUAPAMPA');
INSERT INTO ubigeo VALUES ('021013', '02', '10', '13', 'RAPAYAN');
INSERT INTO ubigeo VALUES ('021014', '02', '10', '14', 'SAN MARCOS');
INSERT INTO ubigeo VALUES ('021015', '02', '10', '15', 'SAN PEDRO DE CHANA');
INSERT INTO ubigeo VALUES ('021016', '02', '10', '16', 'UCO');
INSERT INTO ubigeo VALUES ('021100', '02', '11', '00', 'HUARMEY');
INSERT INTO ubigeo VALUES ('021101', '02', '11', '01', 'HUARMEY');
INSERT INTO ubigeo VALUES ('021102', '02', '11', '02', 'COCHAPETI');
INSERT INTO ubigeo VALUES ('021103', '02', '11', '03', 'CULEBRAS');
INSERT INTO ubigeo VALUES ('021104', '02', '11', '04', 'HUAYAN');
INSERT INTO ubigeo VALUES ('021105', '02', '11', '05', 'MALVAS');
INSERT INTO ubigeo VALUES ('021200', '02', '12', '00', 'HUAYLAS');
INSERT INTO ubigeo VALUES ('021201', '02', '12', '01', 'CARAZ');
INSERT INTO ubigeo VALUES ('021202', '02', '12', '02', 'HUALLANCA');
INSERT INTO ubigeo VALUES ('021203', '02', '12', '03', 'HUATA');
INSERT INTO ubigeo VALUES ('021204', '02', '12', '04', 'HUAYLAS');
INSERT INTO ubigeo VALUES ('021205', '02', '12', '05', 'MATO');
INSERT INTO ubigeo VALUES ('021206', '02', '12', '06', 'PAMPAROMAS');
INSERT INTO ubigeo VALUES ('021207', '02', '12', '07', 'PUEBLO LIBRE');
INSERT INTO ubigeo VALUES ('021208', '02', '12', '08', 'SANTA CRUZ');
INSERT INTO ubigeo VALUES ('021209', '02', '12', '09', 'SANTO TORIBIO');
INSERT INTO ubigeo VALUES ('021210', '02', '12', '10', 'YURACMARCA');
INSERT INTO ubigeo VALUES ('021300', '02', '13', '00', 'MARISCAL LUZURIAGA');
INSERT INTO ubigeo VALUES ('021301', '02', '13', '01', 'PISCOBAMBA');
INSERT INTO ubigeo VALUES ('021302', '02', '13', '02', 'CASCA');
INSERT INTO ubigeo VALUES ('021303', '02', '13', '03', 'ELEAZAR GUZMAN BARRON');
INSERT INTO ubigeo VALUES ('021304', '02', '13', '04', 'FIDEL OLIVAS ESCUDERO');
INSERT INTO ubigeo VALUES ('021305', '02', '13', '05', 'LLAMA');
INSERT INTO ubigeo VALUES ('021306', '02', '13', '06', 'LLUMPA');
INSERT INTO ubigeo VALUES ('021307', '02', '13', '07', 'LUCMA');
INSERT INTO ubigeo VALUES ('021308', '02', '13', '08', 'MUSGA');
INSERT INTO ubigeo VALUES ('021400', '02', '14', '00', 'OCROS');
INSERT INTO ubigeo VALUES ('021401', '02', '14', '01', 'OCROS');
INSERT INTO ubigeo VALUES ('021402', '02', '14', '02', 'ACAS');
INSERT INTO ubigeo VALUES ('021403', '02', '14', '03', 'CAJAMARQUILLA');
INSERT INTO ubigeo VALUES ('021404', '02', '14', '04', 'CARHUAPAMPA');
INSERT INTO ubigeo VALUES ('021405', '02', '14', '05', 'COCHAS');
INSERT INTO ubigeo VALUES ('021406', '02', '14', '06', 'CONGAS');
INSERT INTO ubigeo VALUES ('021407', '02', '14', '07', 'LLIPA');
INSERT INTO ubigeo VALUES ('021408', '02', '14', '08', 'SAN CRISTOBAL DE RAJAN');
INSERT INTO ubigeo VALUES ('021409', '02', '14', '09', 'SAN PEDRO');
INSERT INTO ubigeo VALUES ('021410', '02', '14', '10', 'SANTIAGO DE CHILCAS');
INSERT INTO ubigeo VALUES ('021500', '02', '15', '00', 'PALLASCA');
INSERT INTO ubigeo VALUES ('021501', '02', '15', '01', 'CABANA');
INSERT INTO ubigeo VALUES ('021502', '02', '15', '02', 'BOLOGNESI');
INSERT INTO ubigeo VALUES ('021503', '02', '15', '03', 'CONCHUCOS');
INSERT INTO ubigeo VALUES ('021504', '02', '15', '04', 'HUACASCHUQUE');
INSERT INTO ubigeo VALUES ('021505', '02', '15', '05', 'HUANDOVAL');
INSERT INTO ubigeo VALUES ('021506', '02', '15', '06', 'LACABAMBA');
INSERT INTO ubigeo VALUES ('021507', '02', '15', '07', 'LLAPO');
INSERT INTO ubigeo VALUES ('021508', '02', '15', '08', 'PALLASCA');
INSERT INTO ubigeo VALUES ('021509', '02', '15', '09', 'PAMPAS');
INSERT INTO ubigeo VALUES ('021510', '02', '15', '10', 'SANTA ROSA');
INSERT INTO ubigeo VALUES ('021511', '02', '15', '11', 'TAUCA');
INSERT INTO ubigeo VALUES ('021600', '02', '16', '00', 'POMABAMBA');
INSERT INTO ubigeo VALUES ('021601', '02', '16', '01', 'POMABAMBA');
INSERT INTO ubigeo VALUES ('021602', '02', '16', '02', 'HUAYLLAN');
INSERT INTO ubigeo VALUES ('021603', '02', '16', '03', 'PAROBAMBA');
INSERT INTO ubigeo VALUES ('021604', '02', '16', '04', 'QUINUABAMBA');
INSERT INTO ubigeo VALUES ('021700', '02', '17', '00', 'RECUAY');
INSERT INTO ubigeo VALUES ('021701', '02', '17', '01', 'RECUAY');
INSERT INTO ubigeo VALUES ('021702', '02', '17', '02', 'CATAC');
INSERT INTO ubigeo VALUES ('021703', '02', '17', '03', 'COTAPARACO');
INSERT INTO ubigeo VALUES ('021704', '02', '17', '04', 'HUAYLLAPAMPA');
INSERT INTO ubigeo VALUES ('021705', '02', '17', '05', 'LLACLLIN');
INSERT INTO ubigeo VALUES ('021706', '02', '17', '06', 'MARCA');
INSERT INTO ubigeo VALUES ('021707', '02', '17', '07', 'PAMPAS CHICO');
INSERT INTO ubigeo VALUES ('021708', '02', '17', '08', 'PARARIN');
INSERT INTO ubigeo VALUES ('021709', '02', '17', '09', 'TAPACOCHA');
INSERT INTO ubigeo VALUES ('021710', '02', '17', '10', 'TICAPAMPA');
INSERT INTO ubigeo VALUES ('021800', '02', '18', '00', 'SANTA');
INSERT INTO ubigeo VALUES ('021801', '02', '18', '01', 'CHIMBOTE');
INSERT INTO ubigeo VALUES ('021802', '02', '18', '02', 'CACERES DEL PERU');
INSERT INTO ubigeo VALUES ('021803', '02', '18', '03', 'COISHCO');
INSERT INTO ubigeo VALUES ('021804', '02', '18', '04', 'MACATE');
INSERT INTO ubigeo VALUES ('021805', '02', '18', '05', 'MORO');
INSERT INTO ubigeo VALUES ('021806', '02', '18', '06', 'NEPEÑA');
INSERT INTO ubigeo VALUES ('021807', '02', '18', '07', 'SAMANCO');
INSERT INTO ubigeo VALUES ('021808', '02', '18', '08', 'SANTA');
INSERT INTO ubigeo VALUES ('021809', '02', '18', '09', 'NUEVO CHIMBOTE');
INSERT INTO ubigeo VALUES ('021900', '02', '19', '00', 'SIHUAS');
INSERT INTO ubigeo VALUES ('021901', '02', '19', '01', 'SIHUAS');
INSERT INTO ubigeo VALUES ('021902', '02', '19', '02', 'ACOBAMBA');
INSERT INTO ubigeo VALUES ('021903', '02', '19', '03', 'ALFONSO UGARTE');
INSERT INTO ubigeo VALUES ('021904', '02', '19', '04', 'CASHAPAMPA');
INSERT INTO ubigeo VALUES ('021905', '02', '19', '05', 'CHINGALPO');
INSERT INTO ubigeo VALUES ('021906', '02', '19', '06', 'HUAYLLABAMBA');
INSERT INTO ubigeo VALUES ('021907', '02', '19', '07', 'QUICHES');
INSERT INTO ubigeo VALUES ('021908', '02', '19', '08', 'RAGASH');
INSERT INTO ubigeo VALUES ('021909', '02', '19', '09', 'SAN JUAN');
INSERT INTO ubigeo VALUES ('021910', '02', '19', '10', 'SICSIBAMBA');
INSERT INTO ubigeo VALUES ('022000', '02', '20', '00', 'YUNGAY');
INSERT INTO ubigeo VALUES ('022001', '02', '20', '01', 'YUNGAY');
INSERT INTO ubigeo VALUES ('022002', '02', '20', '02', 'CASCAPARA');
INSERT INTO ubigeo VALUES ('022003', '02', '20', '03', 'MANCOS');
INSERT INTO ubigeo VALUES ('022004', '02', '20', '04', 'MATACOTO');
INSERT INTO ubigeo VALUES ('022005', '02', '20', '05', 'QUILLO');
INSERT INTO ubigeo VALUES ('022006', '02', '20', '06', 'RANRAHIRCA');
INSERT INTO ubigeo VALUES ('022007', '02', '20', '07', 'SHUPLUY');
INSERT INTO ubigeo VALUES ('022008', '02', '20', '08', 'YANAMA');
INSERT INTO ubigeo VALUES ('030000', '03', '00', '00', 'APURIMAC');
INSERT INTO ubigeo VALUES ('030100', '03', '01', '00', 'ABANCAY');
INSERT INTO ubigeo VALUES ('030101', '03', '01', '01', 'ABANCAY');
INSERT INTO ubigeo VALUES ('030102', '03', '01', '02', 'CHACOCHE');
INSERT INTO ubigeo VALUES ('030103', '03', '01', '03', 'CIRCA');
INSERT INTO ubigeo VALUES ('030104', '03', '01', '04', 'CURAHUASI');
INSERT INTO ubigeo VALUES ('030105', '03', '01', '05', 'HUANIPACA');
INSERT INTO ubigeo VALUES ('030106', '03', '01', '06', 'LAMBRAMA');
INSERT INTO ubigeo VALUES ('030107', '03', '01', '07', 'PICHIRHUA');
INSERT INTO ubigeo VALUES ('030108', '03', '01', '08', 'SAN PEDRO DE CACHORA');
INSERT INTO ubigeo VALUES ('030109', '03', '01', '09', 'TAMBURCO');
INSERT INTO ubigeo VALUES ('030200', '03', '02', '00', 'ANDAHUAYLAS');
INSERT INTO ubigeo VALUES ('030201', '03', '02', '01', 'ANDAHUAYLAS');
INSERT INTO ubigeo VALUES ('030202', '03', '02', '02', 'ANDARAPA');
INSERT INTO ubigeo VALUES ('030203', '03', '02', '03', 'CHIARA');
INSERT INTO ubigeo VALUES ('030204', '03', '02', '04', 'HUANCARAMA');
INSERT INTO ubigeo VALUES ('030205', '03', '02', '05', 'HUANCARAY');
INSERT INTO ubigeo VALUES ('030206', '03', '02', '06', 'HUAYANA');
INSERT INTO ubigeo VALUES ('030207', '03', '02', '07', 'KISHUARA');
INSERT INTO ubigeo VALUES ('030208', '03', '02', '08', 'PACOBAMBA');
INSERT INTO ubigeo VALUES ('030209', '03', '02', '09', 'PACUCHA');
INSERT INTO ubigeo VALUES ('030210', '03', '02', '10', 'PAMPACHIRI');
INSERT INTO ubigeo VALUES ('030211', '03', '02', '11', 'POMACOCHA');
INSERT INTO ubigeo VALUES ('030212', '03', '02', '12', 'SAN ANTONIO DE CACHI');
INSERT INTO ubigeo VALUES ('030213', '03', '02', '13', 'SAN JERONIMO');
INSERT INTO ubigeo VALUES ('030214', '03', '02', '14', 'SAN MIGUEL DE CHACCRAMPA');
INSERT INTO ubigeo VALUES ('030215', '03', '02', '15', 'SANTA MARIA DE CHICMO');
INSERT INTO ubigeo VALUES ('030216', '03', '02', '16', 'TALAVERA');
INSERT INTO ubigeo VALUES ('030217', '03', '02', '17', 'TUMAY HUARACA');
INSERT INTO ubigeo VALUES ('030218', '03', '02', '18', 'TURPO');
INSERT INTO ubigeo VALUES ('030219', '03', '02', '19', 'KAQUIABAMBA');
INSERT INTO ubigeo VALUES ('030300', '03', '03', '00', 'ANTABAMBA');
INSERT INTO ubigeo VALUES ('030301', '03', '03', '01', 'ANTABAMBA');
INSERT INTO ubigeo VALUES ('030302', '03', '03', '02', 'EL ORO');
INSERT INTO ubigeo VALUES ('030303', '03', '03', '03', 'HUAQUIRCA');
INSERT INTO ubigeo VALUES ('030304', '03', '03', '04', 'JUAN ESPINOZA MEDRANO');
INSERT INTO ubigeo VALUES ('030305', '03', '03', '05', 'OROPESA');
INSERT INTO ubigeo VALUES ('030306', '03', '03', '06', 'PACHACONAS');
INSERT INTO ubigeo VALUES ('030307', '03', '03', '07', 'SABAINO');
INSERT INTO ubigeo VALUES ('030400', '03', '04', '00', 'AYMARAES');
INSERT INTO ubigeo VALUES ('030401', '03', '04', '01', 'CHALHUANCA');
INSERT INTO ubigeo VALUES ('030402', '03', '04', '02', 'CAPAYA');
INSERT INTO ubigeo VALUES ('030403', '03', '04', '03', 'CARAYBAMBA');
INSERT INTO ubigeo VALUES ('030404', '03', '04', '04', 'CHAPIMARCA');
INSERT INTO ubigeo VALUES ('030405', '03', '04', '05', 'COLCABAMBA');
INSERT INTO ubigeo VALUES ('030406', '03', '04', '06', 'COTARUSE');
INSERT INTO ubigeo VALUES ('030407', '03', '04', '07', 'HUAYLLO');
INSERT INTO ubigeo VALUES ('030408', '03', '04', '08', 'JUSTO APU SAHUARAURA');
INSERT INTO ubigeo VALUES ('030409', '03', '04', '09', 'LUCRE');
INSERT INTO ubigeo VALUES ('030410', '03', '04', '10', 'POCOHUANCA');
INSERT INTO ubigeo VALUES ('030411', '03', '04', '11', 'SAN JUAN DE CHACÑA');
INSERT INTO ubigeo VALUES ('030412', '03', '04', '12', 'SAÑAYCA');
INSERT INTO ubigeo VALUES ('030413', '03', '04', '13', 'SORAYA');
INSERT INTO ubigeo VALUES ('030414', '03', '04', '14', 'TAPAIRIHUA');
INSERT INTO ubigeo VALUES ('030415', '03', '04', '15', 'TINTAY');
INSERT INTO ubigeo VALUES ('030416', '03', '04', '16', 'TORAYA');
INSERT INTO ubigeo VALUES ('030417', '03', '04', '17', 'YANACA');
INSERT INTO ubigeo VALUES ('030500', '03', '05', '00', 'COTABAMBAS');
INSERT INTO ubigeo VALUES ('030501', '03', '05', '01', 'TAMBOBAMBA');
INSERT INTO ubigeo VALUES ('030502', '03', '05', '02', 'COTABAMBAS');
INSERT INTO ubigeo VALUES ('030503', '03', '05', '03', 'COYLLURQUI');
INSERT INTO ubigeo VALUES ('030504', '03', '05', '04', 'HAQUIRA');
INSERT INTO ubigeo VALUES ('030505', '03', '05', '05', 'MARA');
INSERT INTO ubigeo VALUES ('030506', '03', '05', '06', 'CHALLHUAHUACHO');
INSERT INTO ubigeo VALUES ('030600', '03', '06', '00', 'CHINCHEROS');
INSERT INTO ubigeo VALUES ('030601', '03', '06', '01', 'CHINCHEROS');
INSERT INTO ubigeo VALUES ('030602', '03', '06', '02', 'ANCO_HUALLO');
INSERT INTO ubigeo VALUES ('030603', '03', '06', '03', 'COCHARCAS');
INSERT INTO ubigeo VALUES ('030604', '03', '06', '04', 'HUACCANA');
INSERT INTO ubigeo VALUES ('030605', '03', '06', '05', 'OCOBAMBA');
INSERT INTO ubigeo VALUES ('030606', '03', '06', '06', 'ONGOY');
INSERT INTO ubigeo VALUES ('030607', '03', '06', '07', 'URANMARCA');
INSERT INTO ubigeo VALUES ('030608', '03', '06', '08', 'RANRACANCHA');
INSERT INTO ubigeo VALUES ('030700', '03', '07', '00', 'GRAU');
INSERT INTO ubigeo VALUES ('030701', '03', '07', '01', 'CHUQUIBAMBILLA');
INSERT INTO ubigeo VALUES ('030702', '03', '07', '02', 'CURPAHUASI');
INSERT INTO ubigeo VALUES ('030703', '03', '07', '03', 'GAMARRA');
INSERT INTO ubigeo VALUES ('030704', '03', '07', '04', 'HUAYLLATI');
INSERT INTO ubigeo VALUES ('030705', '03', '07', '05', 'MAMARA');
INSERT INTO ubigeo VALUES ('030706', '03', '07', '06', 'MICAELA BASTIDAS');
INSERT INTO ubigeo VALUES ('030707', '03', '07', '07', 'PATAYPAMPA');
INSERT INTO ubigeo VALUES ('030708', '03', '07', '08', 'PROGRESO');
INSERT INTO ubigeo VALUES ('030709', '03', '07', '09', 'SAN ANTONIO');
INSERT INTO ubigeo VALUES ('030710', '03', '07', '10', 'SANTA ROSA');
INSERT INTO ubigeo VALUES ('030711', '03', '07', '11', 'TURPAY');
INSERT INTO ubigeo VALUES ('030712', '03', '07', '12', 'VILCABAMBA');
INSERT INTO ubigeo VALUES ('030713', '03', '07', '13', 'VIRUNDO');
INSERT INTO ubigeo VALUES ('030714', '03', '07', '14', 'CURASCO');
INSERT INTO ubigeo VALUES ('040000', '04', '00', '00', 'AREQUIPA');
INSERT INTO ubigeo VALUES ('040100', '04', '01', '00', 'AREQUIPA');
INSERT INTO ubigeo VALUES ('040101', '04', '01', '01', 'AREQUIPA');
INSERT INTO ubigeo VALUES ('040102', '04', '01', '02', 'ALTO SELVA ALEGRE');
INSERT INTO ubigeo VALUES ('040103', '04', '01', '03', 'CAYMA');
INSERT INTO ubigeo VALUES ('040104', '04', '01', '04', 'CERRO COLORADO');
INSERT INTO ubigeo VALUES ('040105', '04', '01', '05', 'CHARACATO');
INSERT INTO ubigeo VALUES ('040106', '04', '01', '06', 'CHIGUATA');
INSERT INTO ubigeo VALUES ('040107', '04', '01', '07', 'JACOBO HUNTER');
INSERT INTO ubigeo VALUES ('040108', '04', '01', '08', 'LA JOYA');
INSERT INTO ubigeo VALUES ('040109', '04', '01', '09', 'MARIANO MELGAR');
INSERT INTO ubigeo VALUES ('040110', '04', '01', '10', 'MIRAFLORES');
INSERT INTO ubigeo VALUES ('040111', '04', '01', '11', 'MOLLEBAYA');
INSERT INTO ubigeo VALUES ('040112', '04', '01', '12', 'PAUCARPATA');
INSERT INTO ubigeo VALUES ('040113', '04', '01', '13', 'POCSI');
INSERT INTO ubigeo VALUES ('040114', '04', '01', '14', 'POLOBAYA');
INSERT INTO ubigeo VALUES ('040115', '04', '01', '15', 'QUEQUEÑA');
INSERT INTO ubigeo VALUES ('040116', '04', '01', '16', 'SABANDIA');
INSERT INTO ubigeo VALUES ('040117', '04', '01', '17', 'SACHACA');
INSERT INTO ubigeo VALUES ('040118', '04', '01', '18', 'SAN JUAN DE SIGUAS');
INSERT INTO ubigeo VALUES ('040119', '04', '01', '19', 'SAN JUAN DE TARUCANI');
INSERT INTO ubigeo VALUES ('040120', '04', '01', '20', 'SANTA ISABEL DE SIGUAS');
INSERT INTO ubigeo VALUES ('040121', '04', '01', '21', 'SANTA RITA DE SIGUAS');
INSERT INTO ubigeo VALUES ('040122', '04', '01', '22', 'SOCABAYA');
INSERT INTO ubigeo VALUES ('040123', '04', '01', '23', 'TIABAYA');
INSERT INTO ubigeo VALUES ('040124', '04', '01', '24', 'UCHUMAYO');
INSERT INTO ubigeo VALUES ('040125', '04', '01', '25', 'VITOR');
INSERT INTO ubigeo VALUES ('040126', '04', '01', '26', 'YANAHUARA');
INSERT INTO ubigeo VALUES ('040127', '04', '01', '27', 'YARABAMBA');
INSERT INTO ubigeo VALUES ('040128', '04', '01', '28', 'YURA');
INSERT INTO ubigeo VALUES ('040129', '04', '01', '29', 'JOSE LUIS BUSTAMANTE Y RIVERO');
INSERT INTO ubigeo VALUES ('040200', '04', '02', '00', 'CAMANA');
INSERT INTO ubigeo VALUES ('040201', '04', '02', '01', 'CAMANA');
INSERT INTO ubigeo VALUES ('040202', '04', '02', '02', 'JOSE MARIA QUIMPER');
INSERT INTO ubigeo VALUES ('040203', '04', '02', '03', 'MARIANO NICOLAS VALCARCEL');
INSERT INTO ubigeo VALUES ('040204', '04', '02', '04', 'MARISCAL CACERES');
INSERT INTO ubigeo VALUES ('040205', '04', '02', '05', 'NICOLAS DE PIEROLA');
INSERT INTO ubigeo VALUES ('040206', '04', '02', '06', 'OCOÑA');
INSERT INTO ubigeo VALUES ('040207', '04', '02', '07', 'QUILCA');
INSERT INTO ubigeo VALUES ('040208', '04', '02', '08', 'SAMUEL PASTOR');
INSERT INTO ubigeo VALUES ('040300', '04', '03', '00', 'CARAVELI');
INSERT INTO ubigeo VALUES ('040301', '04', '03', '01', 'CARAVELI');
INSERT INTO ubigeo VALUES ('040302', '04', '03', '02', 'ACARI');
INSERT INTO ubigeo VALUES ('040303', '04', '03', '03', 'ATICO');
INSERT INTO ubigeo VALUES ('040304', '04', '03', '04', 'ATIQUIPA');
INSERT INTO ubigeo VALUES ('040305', '04', '03', '05', 'BELLA UNION');
INSERT INTO ubigeo VALUES ('040306', '04', '03', '06', 'CAHUACHO');
INSERT INTO ubigeo VALUES ('040307', '04', '03', '07', 'CHALA');
INSERT INTO ubigeo VALUES ('040308', '04', '03', '08', 'CHAPARRA');
INSERT INTO ubigeo VALUES ('040309', '04', '03', '09', 'HUANUHUANU');
INSERT INTO ubigeo VALUES ('040310', '04', '03', '10', 'JAQUI');
INSERT INTO ubigeo VALUES ('040311', '04', '03', '11', 'LOMAS');
INSERT INTO ubigeo VALUES ('040312', '04', '03', '12', 'QUICACHA');
INSERT INTO ubigeo VALUES ('040313', '04', '03', '13', 'YAUCA');
INSERT INTO ubigeo VALUES ('040400', '04', '04', '00', 'CASTILLA');
INSERT INTO ubigeo VALUES ('040401', '04', '04', '01', 'APLAO');
INSERT INTO ubigeo VALUES ('040402', '04', '04', '02', 'ANDAGUA');
INSERT INTO ubigeo VALUES ('040403', '04', '04', '03', 'AYO');
INSERT INTO ubigeo VALUES ('040404', '04', '04', '04', 'CHACHAS');
INSERT INTO ubigeo VALUES ('040405', '04', '04', '05', 'CHILCAYMARCA');
INSERT INTO ubigeo VALUES ('040406', '04', '04', '06', 'CHOCO');
INSERT INTO ubigeo VALUES ('040407', '04', '04', '07', 'HUANCARQUI');
INSERT INTO ubigeo VALUES ('040408', '04', '04', '08', 'MACHAGUAY');
INSERT INTO ubigeo VALUES ('040409', '04', '04', '09', 'ORCOPAMPA');
INSERT INTO ubigeo VALUES ('040410', '04', '04', '10', 'PAMPACOLCA');
INSERT INTO ubigeo VALUES ('040411', '04', '04', '11', 'TIPAN');
INSERT INTO ubigeo VALUES ('040412', '04', '04', '12', 'UÑON');
INSERT INTO ubigeo VALUES ('040413', '04', '04', '13', 'URACA');
INSERT INTO ubigeo VALUES ('040414', '04', '04', '14', 'VIRACO');
INSERT INTO ubigeo VALUES ('040500', '04', '05', '00', 'CAYLLOMA');
INSERT INTO ubigeo VALUES ('040501', '04', '05', '01', 'CHIVAY');
INSERT INTO ubigeo VALUES ('040502', '04', '05', '02', 'ACHOMA');
INSERT INTO ubigeo VALUES ('040503', '04', '05', '03', 'CABANACONDE');
INSERT INTO ubigeo VALUES ('040504', '04', '05', '04', 'CALLALLI');
INSERT INTO ubigeo VALUES ('040505', '04', '05', '05', 'CAYLLOMA');
INSERT INTO ubigeo VALUES ('040506', '04', '05', '06', 'COPORAQUE');
INSERT INTO ubigeo VALUES ('040507', '04', '05', '07', 'HUAMBO');
INSERT INTO ubigeo VALUES ('040508', '04', '05', '08', 'HUANCA');
INSERT INTO ubigeo VALUES ('040509', '04', '05', '09', 'ICHUPAMPA');
INSERT INTO ubigeo VALUES ('040510', '04', '05', '10', 'LARI');
INSERT INTO ubigeo VALUES ('040511', '04', '05', '11', 'LLUTA');
INSERT INTO ubigeo VALUES ('040512', '04', '05', '12', 'MACA');
INSERT INTO ubigeo VALUES ('040513', '04', '05', '13', 'MADRIGAL');
INSERT INTO ubigeo VALUES ('040514', '04', '05', '14', 'SAN ANTONIO DE CHUCA');
INSERT INTO ubigeo VALUES ('040515', '04', '05', '15', 'SIBAYO');
INSERT INTO ubigeo VALUES ('040516', '04', '05', '16', 'TAPAY');
INSERT INTO ubigeo VALUES ('040517', '04', '05', '17', 'TISCO');
INSERT INTO ubigeo VALUES ('040518', '04', '05', '18', 'TUTI');
INSERT INTO ubigeo VALUES ('040519', '04', '05', '19', 'YANQUE');
INSERT INTO ubigeo VALUES ('040520', '04', '05', '20', 'MAJES');
INSERT INTO ubigeo VALUES ('040600', '04', '06', '00', 'CONDESUYOS');
INSERT INTO ubigeo VALUES ('040601', '04', '06', '01', 'CHUQUIBAMBA');
INSERT INTO ubigeo VALUES ('040602', '04', '06', '02', 'ANDARAY');
INSERT INTO ubigeo VALUES ('040603', '04', '06', '03', 'CAYARANI');
INSERT INTO ubigeo VALUES ('040604', '04', '06', '04', 'CHICHAS');
INSERT INTO ubigeo VALUES ('040605', '04', '06', '05', 'IRAY');
INSERT INTO ubigeo VALUES ('040606', '04', '06', '06', 'RIO GRANDE');
INSERT INTO ubigeo VALUES ('040607', '04', '06', '07', 'SALAMANCA');
INSERT INTO ubigeo VALUES ('040608', '04', '06', '08', 'YANAQUIHUA');
INSERT INTO ubigeo VALUES ('040700', '04', '07', '00', 'ISLAY');
INSERT INTO ubigeo VALUES ('040701', '04', '07', '01', 'MOLLENDO');
INSERT INTO ubigeo VALUES ('040702', '04', '07', '02', 'COCACHACRA');
INSERT INTO ubigeo VALUES ('040703', '04', '07', '03', 'DEAN VALDIVIA');
INSERT INTO ubigeo VALUES ('040704', '04', '07', '04', 'ISLAY');
INSERT INTO ubigeo VALUES ('040705', '04', '07', '05', 'MEJIA');
INSERT INTO ubigeo VALUES ('040706', '04', '07', '06', 'PUNTA DE BOMBON');
INSERT INTO ubigeo VALUES ('040800', '04', '08', '00', 'LA UNION');
INSERT INTO ubigeo VALUES ('040801', '04', '08', '01', 'COTAHUASI');
INSERT INTO ubigeo VALUES ('040802', '04', '08', '02', 'ALCA');
INSERT INTO ubigeo VALUES ('040803', '04', '08', '03', 'CHARCANA');
INSERT INTO ubigeo VALUES ('040804', '04', '08', '04', 'HUAYNACOTAS');
INSERT INTO ubigeo VALUES ('040805', '04', '08', '05', 'PAMPAMARCA');
INSERT INTO ubigeo VALUES ('040806', '04', '08', '06', 'PUYCA');
INSERT INTO ubigeo VALUES ('040807', '04', '08', '07', 'QUECHUALLA');
INSERT INTO ubigeo VALUES ('040808', '04', '08', '08', 'SAYLA');
INSERT INTO ubigeo VALUES ('040809', '04', '08', '09', 'TAURIA');
INSERT INTO ubigeo VALUES ('040810', '04', '08', '10', 'TOMEPAMPA');
INSERT INTO ubigeo VALUES ('040811', '04', '08', '11', 'TORO');
INSERT INTO ubigeo VALUES ('050000', '05', '00', '00', 'AYACUCHO');
INSERT INTO ubigeo VALUES ('050100', '05', '01', '00', 'HUAMANGA');
INSERT INTO ubigeo VALUES ('050101', '05', '01', '01', 'AYACUCHO');
INSERT INTO ubigeo VALUES ('050102', '05', '01', '02', 'ACOCRO');
INSERT INTO ubigeo VALUES ('050103', '05', '01', '03', 'ACOS VINCHOS');
INSERT INTO ubigeo VALUES ('050104', '05', '01', '04', 'CARMEN ALTO');
INSERT INTO ubigeo VALUES ('050105', '05', '01', '05', 'CHIARA');
INSERT INTO ubigeo VALUES ('050106', '05', '01', '06', 'OCROS');
INSERT INTO ubigeo VALUES ('050107', '05', '01', '07', 'PACAYCASA');
INSERT INTO ubigeo VALUES ('050108', '05', '01', '08', 'QUINUA');
INSERT INTO ubigeo VALUES ('050109', '05', '01', '09', 'SAN JOSE DE TICLLAS');
INSERT INTO ubigeo VALUES ('050110', '05', '01', '10', 'SAN JUAN BAUTISTA');
INSERT INTO ubigeo VALUES ('050111', '05', '01', '11', 'SANTIAGO DE PISCHA');
INSERT INTO ubigeo VALUES ('050112', '05', '01', '12', 'SOCOS');
INSERT INTO ubigeo VALUES ('050113', '05', '01', '13', 'TAMBILLO');
INSERT INTO ubigeo VALUES ('050114', '05', '01', '14', 'VINCHOS');
INSERT INTO ubigeo VALUES ('050115', '05', '01', '15', 'JESUS NAZARENO');
INSERT INTO ubigeo VALUES ('050200', '05', '02', '00', 'CANGALLO');
INSERT INTO ubigeo VALUES ('050201', '05', '02', '01', 'CANGALLO');
INSERT INTO ubigeo VALUES ('050202', '05', '02', '02', 'CHUSCHI');
INSERT INTO ubigeo VALUES ('050203', '05', '02', '03', 'LOS MOROCHUCOS');
INSERT INTO ubigeo VALUES ('050204', '05', '02', '04', 'MARIA PARADO DE BELLIDO');
INSERT INTO ubigeo VALUES ('050205', '05', '02', '05', 'PARAS');
INSERT INTO ubigeo VALUES ('050206', '05', '02', '06', 'TOTOS');
INSERT INTO ubigeo VALUES ('050300', '05', '03', '00', 'HUANCA SANCOS');
INSERT INTO ubigeo VALUES ('050301', '05', '03', '01', 'SANCOS');
INSERT INTO ubigeo VALUES ('050302', '05', '03', '02', 'CARAPO');
INSERT INTO ubigeo VALUES ('050303', '05', '03', '03', 'SACSAMARCA');
INSERT INTO ubigeo VALUES ('050304', '05', '03', '04', 'SANTIAGO DE LUCANAMARCA');
INSERT INTO ubigeo VALUES ('050400', '05', '04', '00', 'HUANTA');
INSERT INTO ubigeo VALUES ('050401', '05', '04', '01', 'HUANTA');
INSERT INTO ubigeo VALUES ('050402', '05', '04', '02', 'AYAHUANCO');
INSERT INTO ubigeo VALUES ('050403', '05', '04', '03', 'HUAMANGUILLA');
INSERT INTO ubigeo VALUES ('050404', '05', '04', '04', 'IGUAIN');
INSERT INTO ubigeo VALUES ('050405', '05', '04', '05', 'LURICOCHA');
INSERT INTO ubigeo VALUES ('050406', '05', '04', '06', 'SANTILLANA');
INSERT INTO ubigeo VALUES ('050407', '05', '04', '07', 'SIVIA');
INSERT INTO ubigeo VALUES ('050408', '05', '04', '08', 'LLOCHEGUA');
INSERT INTO ubigeo VALUES ('050500', '05', '05', '00', 'LA MAR');
INSERT INTO ubigeo VALUES ('050501', '05', '05', '01', 'SAN MIGUEL');
INSERT INTO ubigeo VALUES ('050502', '05', '05', '02', 'ANCO');
INSERT INTO ubigeo VALUES ('050503', '05', '05', '03', 'AYNA');
INSERT INTO ubigeo VALUES ('050504', '05', '05', '04', 'CHILCAS');
INSERT INTO ubigeo VALUES ('050505', '05', '05', '05', 'CHUNGUI');
INSERT INTO ubigeo VALUES ('050506', '05', '05', '06', 'LUIS CARRANZA');
INSERT INTO ubigeo VALUES ('050507', '05', '05', '07', 'SANTA ROSA');
INSERT INTO ubigeo VALUES ('050508', '05', '05', '08', 'TAMBO');
INSERT INTO ubigeo VALUES ('050509', '05', '05', '09', 'SAMUGARI');
INSERT INTO ubigeo VALUES ('050600', '05', '06', '00', 'LUCANAS');
INSERT INTO ubigeo VALUES ('050601', '05', '06', '01', 'PUQUIO');
INSERT INTO ubigeo VALUES ('050602', '05', '06', '02', 'AUCARA');
INSERT INTO ubigeo VALUES ('050603', '05', '06', '03', 'CABANA');
INSERT INTO ubigeo VALUES ('050604', '05', '06', '04', 'CARMEN SALCEDO');
INSERT INTO ubigeo VALUES ('050605', '05', '06', '05', 'CHAVIÑA');
INSERT INTO ubigeo VALUES ('050606', '05', '06', '06', 'CHIPAO');
INSERT INTO ubigeo VALUES ('050607', '05', '06', '07', 'HUAC-HUAS');
INSERT INTO ubigeo VALUES ('050608', '05', '06', '08', 'LARAMATE');
INSERT INTO ubigeo VALUES ('050609', '05', '06', '09', 'LEONCIO PRADO');
INSERT INTO ubigeo VALUES ('050610', '05', '06', '10', 'LLAUTA');
INSERT INTO ubigeo VALUES ('050611', '05', '06', '11', 'LUCANAS');
INSERT INTO ubigeo VALUES ('050612', '05', '06', '12', 'OCAÑA');
INSERT INTO ubigeo VALUES ('050613', '05', '06', '13', 'OTOCA');
INSERT INTO ubigeo VALUES ('050614', '05', '06', '14', 'SAISA');
INSERT INTO ubigeo VALUES ('050615', '05', '06', '15', 'SAN CRISTOBAL');
INSERT INTO ubigeo VALUES ('050616', '05', '06', '16', 'SAN JUAN');
INSERT INTO ubigeo VALUES ('050617', '05', '06', '17', 'SAN PEDRO');
INSERT INTO ubigeo VALUES ('050618', '05', '06', '18', 'SAN PEDRO DE PALCO');
INSERT INTO ubigeo VALUES ('050619', '05', '06', '19', 'SANCOS');
INSERT INTO ubigeo VALUES ('050620', '05', '06', '20', 'SANTA ANA DE HUAYCAHUACHO');
INSERT INTO ubigeo VALUES ('050621', '05', '06', '21', 'SANTA LUCIA');
INSERT INTO ubigeo VALUES ('050700', '05', '07', '00', 'PARINACOCHAS');
INSERT INTO ubigeo VALUES ('050701', '05', '07', '01', 'CORACORA');
INSERT INTO ubigeo VALUES ('050702', '05', '07', '02', 'CHUMPI');
INSERT INTO ubigeo VALUES ('050703', '05', '07', '03', 'CORONEL CASTAÑEDA');
INSERT INTO ubigeo VALUES ('050704', '05', '07', '04', 'PACAPAUSA');
INSERT INTO ubigeo VALUES ('050705', '05', '07', '05', 'PULLO');
INSERT INTO ubigeo VALUES ('050706', '05', '07', '06', 'PUYUSCA');
INSERT INTO ubigeo VALUES ('050707', '05', '07', '07', 'SAN FRANCISCO DE RAVACAYCO');
INSERT INTO ubigeo VALUES ('050708', '05', '07', '08', 'UPAHUACHO');
INSERT INTO ubigeo VALUES ('050800', '05', '08', '00', 'PAUCAR DEL SARA SARA');
INSERT INTO ubigeo VALUES ('050801', '05', '08', '01', 'PAUSA');
INSERT INTO ubigeo VALUES ('050802', '05', '08', '02', 'COLTA');
INSERT INTO ubigeo VALUES ('050803', '05', '08', '03', 'CORCULLA');
INSERT INTO ubigeo VALUES ('050804', '05', '08', '04', 'LAMPA');
INSERT INTO ubigeo VALUES ('050805', '05', '08', '05', 'MARCABAMBA');
INSERT INTO ubigeo VALUES ('050806', '05', '08', '06', 'OYOLO');
INSERT INTO ubigeo VALUES ('050807', '05', '08', '07', 'PARARCA');
INSERT INTO ubigeo VALUES ('050808', '05', '08', '08', 'SAN JAVIER DE ALPABAMBA');
INSERT INTO ubigeo VALUES ('050809', '05', '08', '09', 'SAN JOSE DE USHUA');
INSERT INTO ubigeo VALUES ('050810', '05', '08', '10', 'SARA SARA');
INSERT INTO ubigeo VALUES ('050900', '05', '09', '00', 'SUCRE');
INSERT INTO ubigeo VALUES ('050901', '05', '09', '01', 'QUEROBAMBA');
INSERT INTO ubigeo VALUES ('050902', '05', '09', '02', 'BELEN');
INSERT INTO ubigeo VALUES ('050903', '05', '09', '03', 'CHALCOS');
INSERT INTO ubigeo VALUES ('050904', '05', '09', '04', 'CHILCAYOC');
INSERT INTO ubigeo VALUES ('050905', '05', '09', '05', 'HUACAÑA');
INSERT INTO ubigeo VALUES ('050906', '05', '09', '06', 'MORCOLLA');
INSERT INTO ubigeo VALUES ('050907', '05', '09', '07', 'PAICO');
INSERT INTO ubigeo VALUES ('050908', '05', '09', '08', 'SAN PEDRO DE LARCAY');
INSERT INTO ubigeo VALUES ('050909', '05', '09', '09', 'SAN SALVADOR DE QUIJE');
INSERT INTO ubigeo VALUES ('050910', '05', '09', '10', 'SANTIAGO DE PAUCARAY');
INSERT INTO ubigeo VALUES ('050911', '05', '09', '11', 'SORAS');
INSERT INTO ubigeo VALUES ('051000', '05', '10', '00', 'VICTOR FAJARDO');
INSERT INTO ubigeo VALUES ('051001', '05', '10', '01', 'HUANCAPI');
INSERT INTO ubigeo VALUES ('051002', '05', '10', '02', 'ALCAMENCA');
INSERT INTO ubigeo VALUES ('051003', '05', '10', '03', 'APONGO');
INSERT INTO ubigeo VALUES ('051004', '05', '10', '04', 'ASQUIPATA');
INSERT INTO ubigeo VALUES ('051005', '05', '10', '05', 'CANARIA');
INSERT INTO ubigeo VALUES ('051006', '05', '10', '06', 'CAYARA');
INSERT INTO ubigeo VALUES ('051007', '05', '10', '07', 'COLCA');
INSERT INTO ubigeo VALUES ('051008', '05', '10', '08', 'HUAMANQUIQUIA');
INSERT INTO ubigeo VALUES ('051009', '05', '10', '09', 'HUANCARAYLLA');
INSERT INTO ubigeo VALUES ('051010', '05', '10', '10', 'HUAYA');
INSERT INTO ubigeo VALUES ('051011', '05', '10', '11', 'SARHUA');
INSERT INTO ubigeo VALUES ('051012', '05', '10', '12', 'VILCANCHOS');
INSERT INTO ubigeo VALUES ('051100', '05', '11', '00', 'VILCAS HUAMAN');
INSERT INTO ubigeo VALUES ('051101', '05', '11', '01', 'VILCAS HUAMAN');
INSERT INTO ubigeo VALUES ('051102', '05', '11', '02', 'ACCOMARCA');
INSERT INTO ubigeo VALUES ('051103', '05', '11', '03', 'CARHUANCA');
INSERT INTO ubigeo VALUES ('051104', '05', '11', '04', 'CONCEPCION');
INSERT INTO ubigeo VALUES ('051105', '05', '11', '05', 'HUAMBALPA');
INSERT INTO ubigeo VALUES ('051106', '05', '11', '06', 'INDEPENDENCIA');
INSERT INTO ubigeo VALUES ('051107', '05', '11', '07', 'SAURAMA');
INSERT INTO ubigeo VALUES ('051108', '05', '11', '08', 'VISCHONGO');
INSERT INTO ubigeo VALUES ('060000', '06', '00', '00', 'CAJAMARCA');
INSERT INTO ubigeo VALUES ('060100', '06', '01', '00', 'CAJAMARCA');
INSERT INTO ubigeo VALUES ('060101', '06', '01', '01', 'CAJAMARCA');
INSERT INTO ubigeo VALUES ('060102', '06', '01', '02', 'ASUNCION');
INSERT INTO ubigeo VALUES ('060103', '06', '01', '03', 'CHETILLA');
INSERT INTO ubigeo VALUES ('060104', '06', '01', '04', 'COSPAN');
INSERT INTO ubigeo VALUES ('060105', '06', '01', '05', 'ENCAÑADA');
INSERT INTO ubigeo VALUES ('060106', '06', '01', '06', 'JESUS');
INSERT INTO ubigeo VALUES ('060107', '06', '01', '07', 'LLACANORA');
INSERT INTO ubigeo VALUES ('060108', '06', '01', '08', 'LOS BAÑOS DEL INCA');
INSERT INTO ubigeo VALUES ('060109', '06', '01', '09', 'MAGDALENA');
INSERT INTO ubigeo VALUES ('060110', '06', '01', '10', 'MATARA');
INSERT INTO ubigeo VALUES ('060111', '06', '01', '11', 'NAMORA');
INSERT INTO ubigeo VALUES ('060112', '06', '01', '12', 'SAN JUAN');
INSERT INTO ubigeo VALUES ('060200', '06', '02', '00', 'CAJABAMBA');
INSERT INTO ubigeo VALUES ('060201', '06', '02', '01', 'CAJABAMBA');
INSERT INTO ubigeo VALUES ('060202', '06', '02', '02', 'CACHACHI');
INSERT INTO ubigeo VALUES ('060203', '06', '02', '03', 'CONDEBAMBA');
INSERT INTO ubigeo VALUES ('060204', '06', '02', '04', 'SITACOCHA');
INSERT INTO ubigeo VALUES ('060300', '06', '03', '00', 'CELENDIN');
INSERT INTO ubigeo VALUES ('060301', '06', '03', '01', 'CELENDIN');
INSERT INTO ubigeo VALUES ('060302', '06', '03', '02', 'CHUMUCH');
INSERT INTO ubigeo VALUES ('060303', '06', '03', '03', 'CORTEGANA');
INSERT INTO ubigeo VALUES ('060304', '06', '03', '04', 'HUASMIN');
INSERT INTO ubigeo VALUES ('060305', '06', '03', '05', 'JORGE CHAVEZ');
INSERT INTO ubigeo VALUES ('060306', '06', '03', '06', 'JOSE GALVEZ');
INSERT INTO ubigeo VALUES ('060307', '06', '03', '07', 'MIGUEL IGLESIAS');
INSERT INTO ubigeo VALUES ('060308', '06', '03', '08', 'OXAMARCA');
INSERT INTO ubigeo VALUES ('060309', '06', '03', '09', 'SOROCHUCO');
INSERT INTO ubigeo VALUES ('060310', '06', '03', '10', 'SUCRE');
INSERT INTO ubigeo VALUES ('060311', '06', '03', '11', 'UTCO');
INSERT INTO ubigeo VALUES ('060312', '06', '03', '12', 'LA LIBERTAD DE PALLAN');
INSERT INTO ubigeo VALUES ('060400', '06', '04', '00', 'CHOTA');
INSERT INTO ubigeo VALUES ('060401', '06', '04', '01', 'CHOTA');
INSERT INTO ubigeo VALUES ('060402', '06', '04', '02', 'ANGUIA');
INSERT INTO ubigeo VALUES ('060403', '06', '04', '03', 'CHADIN');
INSERT INTO ubigeo VALUES ('060404', '06', '04', '04', 'CHIGUIRIP');
INSERT INTO ubigeo VALUES ('060405', '06', '04', '05', 'CHIMBAN');
INSERT INTO ubigeo VALUES ('060406', '06', '04', '06', 'CHOROPAMPA');
INSERT INTO ubigeo VALUES ('060407', '06', '04', '07', 'COCHABAMBA');
INSERT INTO ubigeo VALUES ('060408', '06', '04', '08', 'CONCHAN');
INSERT INTO ubigeo VALUES ('060409', '06', '04', '09', 'HUAMBOS');
INSERT INTO ubigeo VALUES ('060410', '06', '04', '10', 'LAJAS');
INSERT INTO ubigeo VALUES ('060411', '06', '04', '11', 'LLAMA');
INSERT INTO ubigeo VALUES ('060412', '06', '04', '12', 'MIRACOSTA');
INSERT INTO ubigeo VALUES ('060413', '06', '04', '13', 'PACCHA');
INSERT INTO ubigeo VALUES ('060414', '06', '04', '14', 'PION');
INSERT INTO ubigeo VALUES ('060415', '06', '04', '15', 'QUEROCOTO');
INSERT INTO ubigeo VALUES ('060416', '06', '04', '16', 'SAN JUAN DE LICUPIS');
INSERT INTO ubigeo VALUES ('060417', '06', '04', '17', 'TACABAMBA');
INSERT INTO ubigeo VALUES ('060418', '06', '04', '18', 'TOCMOCHE');
INSERT INTO ubigeo VALUES ('060419', '06', '04', '19', 'CHALAMARCA');
INSERT INTO ubigeo VALUES ('060500', '06', '05', '00', 'CONTUMAZA');
INSERT INTO ubigeo VALUES ('060501', '06', '05', '01', 'CONTUMAZA');
INSERT INTO ubigeo VALUES ('060502', '06', '05', '02', 'CHILETE');
INSERT INTO ubigeo VALUES ('060503', '06', '05', '03', 'CUPISNIQUE');
INSERT INTO ubigeo VALUES ('060504', '06', '05', '04', 'GUZMANGO');
INSERT INTO ubigeo VALUES ('060505', '06', '05', '05', 'SAN BENITO');
INSERT INTO ubigeo VALUES ('060506', '06', '05', '06', 'SANTA CRUZ DE TOLED');
INSERT INTO ubigeo VALUES ('060507', '06', '05', '07', 'TANTARICA');
INSERT INTO ubigeo VALUES ('060508', '06', '05', '08', 'YONAN');
INSERT INTO ubigeo VALUES ('060600', '06', '06', '00', 'CUTERVO');
INSERT INTO ubigeo VALUES ('060601', '06', '06', '01', 'CUTERVO');
INSERT INTO ubigeo VALUES ('060602', '06', '06', '02', 'CALLAYUC');
INSERT INTO ubigeo VALUES ('060603', '06', '06', '03', 'CHOROS');
INSERT INTO ubigeo VALUES ('060604', '06', '06', '04', 'CUJILLO');
INSERT INTO ubigeo VALUES ('060605', '06', '06', '05', 'LA RAMADA');
INSERT INTO ubigeo VALUES ('060606', '06', '06', '06', 'PIMPINGOS');
INSERT INTO ubigeo VALUES ('060607', '06', '06', '07', 'QUEROCOTILLO');
INSERT INTO ubigeo VALUES ('060608', '06', '06', '08', 'SAN ANDRES DE CUTERVO');
INSERT INTO ubigeo VALUES ('060609', '06', '06', '09', 'SAN JUAN DE CUTERVO');
INSERT INTO ubigeo VALUES ('060610', '06', '06', '10', 'SAN LUIS DE LUCMA');
INSERT INTO ubigeo VALUES ('060611', '06', '06', '11', 'SANTA CRUZ');
INSERT INTO ubigeo VALUES ('060612', '06', '06', '12', 'SANTO DOMINGO DE LA CAPILLA');
INSERT INTO ubigeo VALUES ('060613', '06', '06', '13', 'SANTO TOMAS');
INSERT INTO ubigeo VALUES ('060614', '06', '06', '14', 'SOCOTA');
INSERT INTO ubigeo VALUES ('060615', '06', '06', '15', 'TORIBIO CASANOVA');
INSERT INTO ubigeo VALUES ('060700', '06', '07', '00', 'HUALGAYOC');
INSERT INTO ubigeo VALUES ('060701', '06', '07', '01', 'BAMBAMARCA');
INSERT INTO ubigeo VALUES ('060702', '06', '07', '02', 'CHUGUR');
INSERT INTO ubigeo VALUES ('060703', '06', '07', '03', 'HUALGAYOC');
INSERT INTO ubigeo VALUES ('060800', '06', '08', '00', 'JAEN');
INSERT INTO ubigeo VALUES ('060801', '06', '08', '01', 'JAEN');
INSERT INTO ubigeo VALUES ('060802', '06', '08', '02', 'BELLAVISTA');
INSERT INTO ubigeo VALUES ('060803', '06', '08', '03', 'CHONTALI');
INSERT INTO ubigeo VALUES ('060804', '06', '08', '04', 'COLASAY');
INSERT INTO ubigeo VALUES ('060805', '06', '08', '05', 'HUABAL');
INSERT INTO ubigeo VALUES ('060806', '06', '08', '06', 'LAS PIRIAS');
INSERT INTO ubigeo VALUES ('060807', '06', '08', '07', 'POMAHUACA');
INSERT INTO ubigeo VALUES ('060808', '06', '08', '08', 'PUCARA');
INSERT INTO ubigeo VALUES ('060809', '06', '08', '09', 'SALLIQUE');
INSERT INTO ubigeo VALUES ('060810', '06', '08', '10', 'SAN FELIPE');
INSERT INTO ubigeo VALUES ('060811', '06', '08', '11', 'SAN JOSE DEL ALTO');
INSERT INTO ubigeo VALUES ('060812', '06', '08', '12', 'SANTA ROSA');
INSERT INTO ubigeo VALUES ('060900', '06', '09', '00', 'SAN IGNACIO');
INSERT INTO ubigeo VALUES ('060901', '06', '09', '01', 'SAN IGNACIO');
INSERT INTO ubigeo VALUES ('060902', '06', '09', '02', 'CHIRINOS');
INSERT INTO ubigeo VALUES ('060903', '06', '09', '03', 'HUARANGO');
INSERT INTO ubigeo VALUES ('060904', '06', '09', '04', 'LA COIPA');
INSERT INTO ubigeo VALUES ('060905', '06', '09', '05', 'NAMBALLE');
INSERT INTO ubigeo VALUES ('060906', '06', '09', '06', 'SAN JOSE DE LOURDES');
INSERT INTO ubigeo VALUES ('060907', '06', '09', '07', 'TABACONAS');
INSERT INTO ubigeo VALUES ('061000', '06', '10', '00', 'SAN MARCOS');
INSERT INTO ubigeo VALUES ('061001', '06', '10', '01', 'PEDRO GALVEZ');
INSERT INTO ubigeo VALUES ('061002', '06', '10', '02', 'CHANCAY');
INSERT INTO ubigeo VALUES ('061003', '06', '10', '03', 'EDUARDO VILLANUEVA');
INSERT INTO ubigeo VALUES ('061004', '06', '10', '04', 'GREGORIO PITA');
INSERT INTO ubigeo VALUES ('061005', '06', '10', '05', 'ICHOCAN');
INSERT INTO ubigeo VALUES ('061006', '06', '10', '06', 'JOSE MANUEL QUIROZ');
INSERT INTO ubigeo VALUES ('061007', '06', '10', '07', 'JOSE SABOGAL');
INSERT INTO ubigeo VALUES ('061100', '06', '11', '00', 'SAN MIGUEL');
INSERT INTO ubigeo VALUES ('061101', '06', '11', '01', 'SAN MIGUEL');
INSERT INTO ubigeo VALUES ('061102', '06', '11', '02', 'BOLIVAR');
INSERT INTO ubigeo VALUES ('061103', '06', '11', '03', 'CALQUIS');
INSERT INTO ubigeo VALUES ('061104', '06', '11', '04', 'CATILLUC');
INSERT INTO ubigeo VALUES ('061105', '06', '11', '05', 'EL PRADO');
INSERT INTO ubigeo VALUES ('061106', '06', '11', '06', 'LA FLORIDA');
INSERT INTO ubigeo VALUES ('061107', '06', '11', '07', 'LLAPA');
INSERT INTO ubigeo VALUES ('061108', '06', '11', '08', 'NANCHOC');
INSERT INTO ubigeo VALUES ('061109', '06', '11', '09', 'NIEPOS');
INSERT INTO ubigeo VALUES ('061110', '06', '11', '10', 'SAN GREGORIO');
INSERT INTO ubigeo VALUES ('061111', '06', '11', '11', 'SAN SILVESTRE DE COCHAN');
INSERT INTO ubigeo VALUES ('061112', '06', '11', '12', 'TONGOD');
INSERT INTO ubigeo VALUES ('061113', '06', '11', '13', 'UNION AGUA BLANCA');
INSERT INTO ubigeo VALUES ('061200', '06', '12', '00', 'SAN PABLO');
INSERT INTO ubigeo VALUES ('061201', '06', '12', '01', 'SAN PABLO');
INSERT INTO ubigeo VALUES ('061202', '06', '12', '02', 'SAN BERNARDINO');
INSERT INTO ubigeo VALUES ('061203', '06', '12', '03', 'SAN LUIS');
INSERT INTO ubigeo VALUES ('061204', '06', '12', '04', 'TUMBADEN');
INSERT INTO ubigeo VALUES ('061300', '06', '13', '00', 'SANTA CRUZ');
INSERT INTO ubigeo VALUES ('061301', '06', '13', '01', 'SANTA CRUZ');
INSERT INTO ubigeo VALUES ('061302', '06', '13', '02', 'ANDABAMBA');
INSERT INTO ubigeo VALUES ('061303', '06', '13', '03', 'CATACHE');
INSERT INTO ubigeo VALUES ('061304', '06', '13', '04', 'CHANCAYBAÑOS');
INSERT INTO ubigeo VALUES ('061305', '06', '13', '05', 'LA ESPERANZA');
INSERT INTO ubigeo VALUES ('061306', '06', '13', '06', 'NINABAMBA');
INSERT INTO ubigeo VALUES ('061307', '06', '13', '07', 'PULAN');
INSERT INTO ubigeo VALUES ('061308', '06', '13', '08', 'SAUCEPAMPA');
INSERT INTO ubigeo VALUES ('061309', '06', '13', '09', 'SEXI');
INSERT INTO ubigeo VALUES ('061310', '06', '13', '10', 'UTICYACU');
INSERT INTO ubigeo VALUES ('061311', '06', '13', '11', 'YAUYUCAN');
INSERT INTO ubigeo VALUES ('070000', '07', '00', '00', 'CALLAO');
INSERT INTO ubigeo VALUES ('070100', '07', '01', '00', 'CALLAO');
INSERT INTO ubigeo VALUES ('070101', '07', '01', '01', 'CALLAO');
INSERT INTO ubigeo VALUES ('070102', '07', '01', '02', 'BELLAVISTA');
INSERT INTO ubigeo VALUES ('070103', '07', '01', '03', 'CARMEN DE LA LEGUA REYNOSO');
INSERT INTO ubigeo VALUES ('070104', '07', '01', '04', 'LA PERLA');
INSERT INTO ubigeo VALUES ('070105', '07', '01', '05', 'LA PUNTA');
INSERT INTO ubigeo VALUES ('070106', '07', '01', '06', 'VENTANILLA');
INSERT INTO ubigeo VALUES ('080000', '08', '00', '00', 'CUSCO');
INSERT INTO ubigeo VALUES ('080100', '08', '01', '00', 'CUSCO');
INSERT INTO ubigeo VALUES ('080101', '08', '01', '01', 'CUSCO');
INSERT INTO ubigeo VALUES ('080102', '08', '01', '02', 'CCORCA');
INSERT INTO ubigeo VALUES ('080103', '08', '01', '03', 'POROY');
INSERT INTO ubigeo VALUES ('080104', '08', '01', '04', 'SAN JERONIMO');
INSERT INTO ubigeo VALUES ('080105', '08', '01', '05', 'SAN SEBASTIAN');
INSERT INTO ubigeo VALUES ('080106', '08', '01', '06', 'SANTIAGO');
INSERT INTO ubigeo VALUES ('080107', '08', '01', '07', 'SAYLLA');
INSERT INTO ubigeo VALUES ('080108', '08', '01', '08', 'WANCHAQ');
INSERT INTO ubigeo VALUES ('080200', '08', '02', '00', 'ACOMAYO');
INSERT INTO ubigeo VALUES ('080201', '08', '02', '01', 'ACOMAYO');
INSERT INTO ubigeo VALUES ('080202', '08', '02', '02', 'ACOPIA');
INSERT INTO ubigeo VALUES ('080203', '08', '02', '03', 'ACOS');
INSERT INTO ubigeo VALUES ('080204', '08', '02', '04', 'MOSOC LLACTA');
INSERT INTO ubigeo VALUES ('080205', '08', '02', '05', 'POMACANCHI');
INSERT INTO ubigeo VALUES ('080206', '08', '02', '06', 'RONDOCAN');
INSERT INTO ubigeo VALUES ('080207', '08', '02', '07', 'SANGARARA');
INSERT INTO ubigeo VALUES ('080300', '08', '03', '00', 'ANTA');
INSERT INTO ubigeo VALUES ('080301', '08', '03', '01', 'ANTA');
INSERT INTO ubigeo VALUES ('080302', '08', '03', '02', 'ANCAHUASI');
INSERT INTO ubigeo VALUES ('080303', '08', '03', '03', 'CACHIMAYO');
INSERT INTO ubigeo VALUES ('080304', '08', '03', '04', 'CHINCHAYPUJIO');
INSERT INTO ubigeo VALUES ('080305', '08', '03', '05', 'HUAROCONDO');
INSERT INTO ubigeo VALUES ('080306', '08', '03', '06', 'LIMATAMBO');
INSERT INTO ubigeo VALUES ('080307', '08', '03', '07', 'MOLLEPATA');
INSERT INTO ubigeo VALUES ('080308', '08', '03', '08', 'PUCYURA');
INSERT INTO ubigeo VALUES ('080309', '08', '03', '09', 'ZURITE');
INSERT INTO ubigeo VALUES ('080400', '08', '04', '00', 'CALCA');
INSERT INTO ubigeo VALUES ('080401', '08', '04', '01', 'CALCA');
INSERT INTO ubigeo VALUES ('080402', '08', '04', '02', 'COYA');
INSERT INTO ubigeo VALUES ('080403', '08', '04', '03', 'LAMAY');
INSERT INTO ubigeo VALUES ('080404', '08', '04', '04', 'LARES');
INSERT INTO ubigeo VALUES ('080405', '08', '04', '05', 'PISAC');
INSERT INTO ubigeo VALUES ('080406', '08', '04', '06', 'SAN SALVADOR');
INSERT INTO ubigeo VALUES ('080407', '08', '04', '07', 'TARAY');
INSERT INTO ubigeo VALUES ('080408', '08', '04', '08', 'YANATILE');
INSERT INTO ubigeo VALUES ('080500', '08', '05', '00', 'CANAS');
INSERT INTO ubigeo VALUES ('080501', '08', '05', '01', 'YANAOCA');
INSERT INTO ubigeo VALUES ('080502', '08', '05', '02', 'CHECCA');
INSERT INTO ubigeo VALUES ('080503', '08', '05', '03', 'KUNTURKANKI');
INSERT INTO ubigeo VALUES ('080504', '08', '05', '04', 'LANGUI');
INSERT INTO ubigeo VALUES ('080505', '08', '05', '05', 'LAYO');
INSERT INTO ubigeo VALUES ('080506', '08', '05', '06', 'PAMPAMARCA');
INSERT INTO ubigeo VALUES ('080507', '08', '05', '07', 'QUEHUE');
INSERT INTO ubigeo VALUES ('080508', '08', '05', '08', 'TUPAC AMARU');
INSERT INTO ubigeo VALUES ('080600', '08', '06', '00', 'CANCHIS');
INSERT INTO ubigeo VALUES ('080601', '08', '06', '01', 'SICUANI');
INSERT INTO ubigeo VALUES ('080602', '08', '06', '02', 'CHECACUPE');
INSERT INTO ubigeo VALUES ('080603', '08', '06', '03', 'COMBAPATA');
INSERT INTO ubigeo VALUES ('080604', '08', '06', '04', 'MARANGANI');
INSERT INTO ubigeo VALUES ('080605', '08', '06', '05', 'PITUMARCA');
INSERT INTO ubigeo VALUES ('080606', '08', '06', '06', 'SAN PABLO');
INSERT INTO ubigeo VALUES ('080607', '08', '06', '07', 'SAN PEDRO');
INSERT INTO ubigeo VALUES ('080608', '08', '06', '08', 'TINTA');
INSERT INTO ubigeo VALUES ('080700', '08', '07', '00', 'CHUMBIVILCAS');
INSERT INTO ubigeo VALUES ('080701', '08', '07', '01', 'SANTO TOMAS');
INSERT INTO ubigeo VALUES ('080702', '08', '07', '02', 'CAPACMARCA');
INSERT INTO ubigeo VALUES ('080703', '08', '07', '03', 'CHAMACA');
INSERT INTO ubigeo VALUES ('080704', '08', '07', '04', 'COLQUEMARCA');
INSERT INTO ubigeo VALUES ('080705', '08', '07', '05', 'LIVITACA');
INSERT INTO ubigeo VALUES ('080706', '08', '07', '06', 'LLUSCO');
INSERT INTO ubigeo VALUES ('080707', '08', '07', '07', 'QUIÑOTA');
INSERT INTO ubigeo VALUES ('080708', '08', '07', '08', 'VELILLE');
INSERT INTO ubigeo VALUES ('080800', '08', '08', '00', 'ESPINAR');
INSERT INTO ubigeo VALUES ('080801', '08', '08', '01', 'ESPINAR');
INSERT INTO ubigeo VALUES ('080802', '08', '08', '02', 'CONDOROMA');
INSERT INTO ubigeo VALUES ('080803', '08', '08', '03', 'COPORAQUE');
INSERT INTO ubigeo VALUES ('080804', '08', '08', '04', 'OCORURO');
INSERT INTO ubigeo VALUES ('080805', '08', '08', '05', 'PALLPATA');
INSERT INTO ubigeo VALUES ('080806', '08', '08', '06', 'PICHIGUA');
INSERT INTO ubigeo VALUES ('080807', '08', '08', '07', 'SUYCKUTAMBO');
INSERT INTO ubigeo VALUES ('080808', '08', '08', '08', 'ALTO PICHIGUA');
INSERT INTO ubigeo VALUES ('080900', '08', '09', '00', 'LA CONVENCION');
INSERT INTO ubigeo VALUES ('080901', '08', '09', '01', 'SANTA ANA');
INSERT INTO ubigeo VALUES ('080902', '08', '09', '02', 'ECHARATE');
INSERT INTO ubigeo VALUES ('080903', '08', '09', '03', 'HUAYOPATA');
INSERT INTO ubigeo VALUES ('080904', '08', '09', '04', 'MARANURA');
INSERT INTO ubigeo VALUES ('080905', '08', '09', '05', 'OCOBAMBA');
INSERT INTO ubigeo VALUES ('080906', '08', '09', '06', 'QUELLOUNO');
INSERT INTO ubigeo VALUES ('080907', '08', '09', '07', 'KIMBIRI');
INSERT INTO ubigeo VALUES ('080908', '08', '09', '08', 'SANTA TERESA');
INSERT INTO ubigeo VALUES ('080909', '08', '09', '09', 'VILCABAMBA');
INSERT INTO ubigeo VALUES ('080910', '08', '09', '10', 'PICHARI');
INSERT INTO ubigeo VALUES ('081000', '08', '10', '00', 'PARURO');
INSERT INTO ubigeo VALUES ('081001', '08', '10', '01', 'PARURO');
INSERT INTO ubigeo VALUES ('081002', '08', '10', '02', 'ACCHA');
INSERT INTO ubigeo VALUES ('081003', '08', '10', '03', 'CCAPI');
INSERT INTO ubigeo VALUES ('081004', '08', '10', '04', 'COLCHA');
INSERT INTO ubigeo VALUES ('081005', '08', '10', '05', 'HUANOQUITE');
INSERT INTO ubigeo VALUES ('081006', '08', '10', '06', 'OMACHA');
INSERT INTO ubigeo VALUES ('081007', '08', '10', '07', 'PACCARITAMBO');
INSERT INTO ubigeo VALUES ('081008', '08', '10', '08', 'PILLPINTO');
INSERT INTO ubigeo VALUES ('081009', '08', '10', '09', 'YAURISQUE');
INSERT INTO ubigeo VALUES ('081100', '08', '11', '00', 'PAUCARTAMBO');
INSERT INTO ubigeo VALUES ('081101', '08', '11', '01', 'PAUCARTAMBO');
INSERT INTO ubigeo VALUES ('081102', '08', '11', '02', 'CAICAY');
INSERT INTO ubigeo VALUES ('081103', '08', '11', '03', 'CHALLABAMBA');
INSERT INTO ubigeo VALUES ('081104', '08', '11', '04', 'COLQUEPATA');
INSERT INTO ubigeo VALUES ('081105', '08', '11', '05', 'HUANCARANI');
INSERT INTO ubigeo VALUES ('081106', '08', '11', '06', 'KOSÑIPATA');
INSERT INTO ubigeo VALUES ('081200', '08', '12', '00', 'QUISPICANCHI');
INSERT INTO ubigeo VALUES ('081201', '08', '12', '01', 'URCOS');
INSERT INTO ubigeo VALUES ('081202', '08', '12', '02', 'ANDAHUAYLILLAS');
INSERT INTO ubigeo VALUES ('081203', '08', '12', '03', 'CAMANTI');
INSERT INTO ubigeo VALUES ('081204', '08', '12', '04', 'CCARHUAYO');
INSERT INTO ubigeo VALUES ('081205', '08', '12', '05', 'CCATCA');
INSERT INTO ubigeo VALUES ('081206', '08', '12', '06', 'CUSIPATA');
INSERT INTO ubigeo VALUES ('081207', '08', '12', '07', 'HUARO');
INSERT INTO ubigeo VALUES ('081208', '08', '12', '08', 'LUCRE');
INSERT INTO ubigeo VALUES ('081209', '08', '12', '09', 'MARCAPATA');
INSERT INTO ubigeo VALUES ('081210', '08', '12', '10', 'OCONGATE');
INSERT INTO ubigeo VALUES ('081211', '08', '12', '11', 'OROPESA');
INSERT INTO ubigeo VALUES ('081212', '08', '12', '12', 'QUIQUIJANA');
INSERT INTO ubigeo VALUES ('081300', '08', '13', '00', 'URUBAMBA');
INSERT INTO ubigeo VALUES ('081301', '08', '13', '01', 'URUBAMBA');
INSERT INTO ubigeo VALUES ('081302', '08', '13', '02', 'CHINCHERO');
INSERT INTO ubigeo VALUES ('081303', '08', '13', '03', 'HUAYLLABAMBA');
INSERT INTO ubigeo VALUES ('081304', '08', '13', '04', 'MACHUPICCHU');
INSERT INTO ubigeo VALUES ('081305', '08', '13', '05', 'MARAS');
INSERT INTO ubigeo VALUES ('081306', '08', '13', '06', 'OLLANTAYTAMBO');
INSERT INTO ubigeo VALUES ('081307', '08', '13', '07', 'YUCAY');
INSERT INTO ubigeo VALUES ('090000', '09', '00', '00', 'HUANCAVELICA');
INSERT INTO ubigeo VALUES ('090100', '09', '01', '00', 'HUANCAVELICA');
INSERT INTO ubigeo VALUES ('090101', '09', '01', '01', 'HUANCAVELICA');
INSERT INTO ubigeo VALUES ('090102', '09', '01', '02', 'ACOBAMBILLA');
INSERT INTO ubigeo VALUES ('090103', '09', '01', '03', 'ACORIA');
INSERT INTO ubigeo VALUES ('090104', '09', '01', '04', 'CONAYCA');
INSERT INTO ubigeo VALUES ('090105', '09', '01', '05', 'CUENCA');
INSERT INTO ubigeo VALUES ('090106', '09', '01', '06', 'HUACHOCOLPA');
INSERT INTO ubigeo VALUES ('090107', '09', '01', '07', 'HUAYLLAHUARA');
INSERT INTO ubigeo VALUES ('090108', '09', '01', '08', 'IZCUCHACA');
INSERT INTO ubigeo VALUES ('090109', '09', '01', '09', 'LARIA');
INSERT INTO ubigeo VALUES ('090110', '09', '01', '10', 'MANTA');
INSERT INTO ubigeo VALUES ('090111', '09', '01', '11', 'MARISCAL CACERES');
INSERT INTO ubigeo VALUES ('090112', '09', '01', '12', 'MOYA');
INSERT INTO ubigeo VALUES ('090113', '09', '01', '13', 'NUEVO OCCORO');
INSERT INTO ubigeo VALUES ('090114', '09', '01', '14', 'PALCA');
INSERT INTO ubigeo VALUES ('090115', '09', '01', '15', 'PILCHACA');
INSERT INTO ubigeo VALUES ('090116', '09', '01', '16', 'VILCA');
INSERT INTO ubigeo VALUES ('090117', '09', '01', '17', 'YAULI');
INSERT INTO ubigeo VALUES ('090118', '09', '01', '18', 'ASCENSION');
INSERT INTO ubigeo VALUES ('090119', '09', '01', '19', 'HUANDO');
INSERT INTO ubigeo VALUES ('090200', '09', '02', '00', 'ACOBAMBA');
INSERT INTO ubigeo VALUES ('090201', '09', '02', '01', 'ACOBAMBA');
INSERT INTO ubigeo VALUES ('090202', '09', '02', '02', 'ANDABAMBA');
INSERT INTO ubigeo VALUES ('090203', '09', '02', '03', 'ANTA');
INSERT INTO ubigeo VALUES ('090204', '09', '02', '04', 'CAJA');
INSERT INTO ubigeo VALUES ('090205', '09', '02', '05', 'MARCAS');
INSERT INTO ubigeo VALUES ('090206', '09', '02', '06', 'PAUCARA');
INSERT INTO ubigeo VALUES ('090207', '09', '02', '07', 'POMACOCHA');
INSERT INTO ubigeo VALUES ('090208', '09', '02', '08', 'ROSARIO');
INSERT INTO ubigeo VALUES ('090300', '09', '03', '00', 'ANGARAES');
INSERT INTO ubigeo VALUES ('090301', '09', '03', '01', 'LIRCAY');
INSERT INTO ubigeo VALUES ('090302', '09', '03', '02', 'ANCHONGA');
INSERT INTO ubigeo VALUES ('090303', '09', '03', '03', 'CALLANMARCA');
INSERT INTO ubigeo VALUES ('090304', '09', '03', '04', 'CCOCHACCASA');
INSERT INTO ubigeo VALUES ('090305', '09', '03', '05', 'CHINCHO');
INSERT INTO ubigeo VALUES ('090306', '09', '03', '06', 'CONGALLA');
INSERT INTO ubigeo VALUES ('090307', '09', '03', '07', 'HUANCA-HUANCA');
INSERT INTO ubigeo VALUES ('090308', '09', '03', '08', 'HUAYLLAY GRANDE');
INSERT INTO ubigeo VALUES ('090309', '09', '03', '09', 'JULCAMARCA');
INSERT INTO ubigeo VALUES ('090310', '09', '03', '10', 'SAN ANTONIO DE ANTAPARCO');
INSERT INTO ubigeo VALUES ('090311', '09', '03', '11', 'SANTO TOMAS DE PATA');
INSERT INTO ubigeo VALUES ('090312', '09', '03', '12', 'SECCLLA');
INSERT INTO ubigeo VALUES ('090400', '09', '04', '00', 'CASTROVIRREYNA');
INSERT INTO ubigeo VALUES ('090401', '09', '04', '01', 'CASTROVIRREYNA');
INSERT INTO ubigeo VALUES ('090402', '09', '04', '02', 'ARMA');
INSERT INTO ubigeo VALUES ('090403', '09', '04', '03', 'AURAHUA');
INSERT INTO ubigeo VALUES ('090404', '09', '04', '04', 'CAPILLAS');
INSERT INTO ubigeo VALUES ('090405', '09', '04', '05', 'CHUPAMARCA');
INSERT INTO ubigeo VALUES ('090406', '09', '04', '06', 'COCAS');
INSERT INTO ubigeo VALUES ('090407', '09', '04', '07', 'HUACHOS');
INSERT INTO ubigeo VALUES ('090408', '09', '04', '08', 'HUAMATAMBO');
INSERT INTO ubigeo VALUES ('090409', '09', '04', '09', 'MOLLEPAMPA');
INSERT INTO ubigeo VALUES ('090410', '09', '04', '10', 'SAN JUAN');
INSERT INTO ubigeo VALUES ('090411', '09', '04', '11', 'SANTA ANA');
INSERT INTO ubigeo VALUES ('090412', '09', '04', '12', 'TANTARA');
INSERT INTO ubigeo VALUES ('090413', '09', '04', '13', 'TICRAPO');
INSERT INTO ubigeo VALUES ('090500', '09', '05', '00', 'CHURCAMPA');
INSERT INTO ubigeo VALUES ('090501', '09', '05', '01', 'CHURCAMPA');
INSERT INTO ubigeo VALUES ('090502', '09', '05', '02', 'ANCO');
INSERT INTO ubigeo VALUES ('090503', '09', '05', '03', 'CHINCHIHUASI');
INSERT INTO ubigeo VALUES ('090504', '09', '05', '04', 'EL CARMEN');
INSERT INTO ubigeo VALUES ('090505', '09', '05', '05', 'LA MERCED');
INSERT INTO ubigeo VALUES ('090506', '09', '05', '06', 'LOCROJA');
INSERT INTO ubigeo VALUES ('090507', '09', '05', '07', 'PAUCARBAMBA');
INSERT INTO ubigeo VALUES ('090508', '09', '05', '08', 'SAN MIGUEL DE MAYOCC');
INSERT INTO ubigeo VALUES ('090509', '09', '05', '09', 'SAN PEDRO DE CORIS');
INSERT INTO ubigeo VALUES ('090510', '09', '05', '10', 'PACHAMARCA');
INSERT INTO ubigeo VALUES ('090511', '09', '05', '11', 'COSME');
INSERT INTO ubigeo VALUES ('090600', '09', '06', '00', 'HUAYTARA');
INSERT INTO ubigeo VALUES ('090601', '09', '06', '01', 'HUAYTARA');
INSERT INTO ubigeo VALUES ('090602', '09', '06', '02', 'AYAVI');
INSERT INTO ubigeo VALUES ('090603', '09', '06', '03', 'CORDOVA');
INSERT INTO ubigeo VALUES ('090604', '09', '06', '04', 'HUAYACUNDO ARMA');
INSERT INTO ubigeo VALUES ('090605', '09', '06', '05', 'LARAMARCA');
INSERT INTO ubigeo VALUES ('090606', '09', '06', '06', 'OCOYO');
INSERT INTO ubigeo VALUES ('090607', '09', '06', '07', 'PILPICHACA');
INSERT INTO ubigeo VALUES ('090608', '09', '06', '08', 'QUERCO');
INSERT INTO ubigeo VALUES ('090609', '09', '06', '09', 'QUITO-ARMA');
INSERT INTO ubigeo VALUES ('090610', '09', '06', '10', 'SAN ANTONIO DE CUSICANCHA');
INSERT INTO ubigeo VALUES ('090611', '09', '06', '11', 'SAN FRANCISCO DE SANGAYAICO');
INSERT INTO ubigeo VALUES ('090612', '09', '06', '12', 'SAN ISIDRO');
INSERT INTO ubigeo VALUES ('090613', '09', '06', '13', 'SANTIAGO DE CHOCORVOS');
INSERT INTO ubigeo VALUES ('090614', '09', '06', '14', 'SANTIAGO DE QUIRAHUARA');
INSERT INTO ubigeo VALUES ('090615', '09', '06', '15', 'SANTO DOMINGO DE CAPILLAS');
INSERT INTO ubigeo VALUES ('090616', '09', '06', '16', 'TAMBO');
INSERT INTO ubigeo VALUES ('090700', '09', '07', '00', 'TAYACAJA');
INSERT INTO ubigeo VALUES ('090701', '09', '07', '01', 'PAMPAS');
INSERT INTO ubigeo VALUES ('090702', '09', '07', '02', 'ACOSTAMBO');
INSERT INTO ubigeo VALUES ('090703', '09', '07', '03', 'ACRAQUIA');
INSERT INTO ubigeo VALUES ('090704', '09', '07', '04', 'AHUAYCHA');
INSERT INTO ubigeo VALUES ('090705', '09', '07', '05', 'COLCABAMBA');
INSERT INTO ubigeo VALUES ('090706', '09', '07', '06', 'DANIEL HERNANDEZ');
INSERT INTO ubigeo VALUES ('090707', '09', '07', '07', 'HUACHOCOLPA');
INSERT INTO ubigeo VALUES ('090709', '09', '07', '09', 'HUARIBAMBA');
INSERT INTO ubigeo VALUES ('090710', '09', '07', '10', 'ÑAHUIMPUQUIO');
INSERT INTO ubigeo VALUES ('090711', '09', '07', '11', 'PAZOS');
INSERT INTO ubigeo VALUES ('090713', '09', '07', '13', 'QUISHUAR');
INSERT INTO ubigeo VALUES ('090714', '09', '07', '14', 'SALCABAMBA');
INSERT INTO ubigeo VALUES ('090715', '09', '07', '15', 'SALCAHUASI');
INSERT INTO ubigeo VALUES ('090716', '09', '07', '16', 'SAN MARCOS DE ROCCHAC');
INSERT INTO ubigeo VALUES ('090717', '09', '07', '17', 'SURCUBAMBA');
INSERT INTO ubigeo VALUES ('090718', '09', '07', '18', 'TINTAY PUNCU');
INSERT INTO ubigeo VALUES ('100000', '10', '00', '00', 'HUANUCO');
INSERT INTO ubigeo VALUES ('100100', '10', '01', '00', 'HUANUCO');
INSERT INTO ubigeo VALUES ('100101', '10', '01', '01', 'HUANUCO');
INSERT INTO ubigeo VALUES ('100102', '10', '01', '02', 'AMARILIS');
INSERT INTO ubigeo VALUES ('100103', '10', '01', '03', 'CHINCHAO');
INSERT INTO ubigeo VALUES ('100104', '10', '01', '04', 'CHURUBAMBA');
INSERT INTO ubigeo VALUES ('100105', '10', '01', '05', 'MARGOS');
INSERT INTO ubigeo VALUES ('100106', '10', '01', '06', 'QUISQUI (KICHKI)');
INSERT INTO ubigeo VALUES ('100107', '10', '01', '07', 'SAN FRANCISCO DE CAYRAN');
INSERT INTO ubigeo VALUES ('100108', '10', '01', '08', 'SAN PEDRO DE CHAULAN');
INSERT INTO ubigeo VALUES ('100109', '10', '01', '09', 'SANTA MARIA DEL VALLE');
INSERT INTO ubigeo VALUES ('100110', '10', '01', '10', 'YARUMAYO');
INSERT INTO ubigeo VALUES ('100111', '10', '01', '11', 'PILLCO MARCA');
INSERT INTO ubigeo VALUES ('100112', '10', '01', '12', 'YACUS');
INSERT INTO ubigeo VALUES ('100200', '10', '02', '00', 'AMBO');
INSERT INTO ubigeo VALUES ('100201', '10', '02', '01', 'AMBO');
INSERT INTO ubigeo VALUES ('100202', '10', '02', '02', 'CAYNA');
INSERT INTO ubigeo VALUES ('100203', '10', '02', '03', 'COLPAS');
INSERT INTO ubigeo VALUES ('100204', '10', '02', '04', 'CONCHAMARCA');
INSERT INTO ubigeo VALUES ('100205', '10', '02', '05', 'HUACAR');
INSERT INTO ubigeo VALUES ('100206', '10', '02', '06', 'SAN FRANCISCO');
INSERT INTO ubigeo VALUES ('100207', '10', '02', '07', 'SAN RAFAEL');
INSERT INTO ubigeo VALUES ('100208', '10', '02', '08', 'TOMAY KICHWA');
INSERT INTO ubigeo VALUES ('100300', '10', '03', '00', 'DOS DE MAYO');
INSERT INTO ubigeo VALUES ('100301', '10', '03', '01', 'LA UNION');
INSERT INTO ubigeo VALUES ('100307', '10', '03', '07', 'CHUQUIS');
INSERT INTO ubigeo VALUES ('100311', '10', '03', '11', 'MARIAS');
INSERT INTO ubigeo VALUES ('100313', '10', '03', '13', 'PACHAS');
INSERT INTO ubigeo VALUES ('100316', '10', '03', '16', 'QUIVILLA');
INSERT INTO ubigeo VALUES ('100317', '10', '03', '17', 'RIPAN');
INSERT INTO ubigeo VALUES ('100321', '10', '03', '21', 'SHUNQUI');
INSERT INTO ubigeo VALUES ('100322', '10', '03', '22', 'SILLAPATA');
INSERT INTO ubigeo VALUES ('100323', '10', '03', '23', 'YANAS');
INSERT INTO ubigeo VALUES ('100400', '10', '04', '00', 'HUACAYBAMBA');
INSERT INTO ubigeo VALUES ('100401', '10', '04', '01', 'HUACAYBAMBA');
INSERT INTO ubigeo VALUES ('100402', '10', '04', '02', 'CANCHABAMBA');
INSERT INTO ubigeo VALUES ('100403', '10', '04', '03', 'COCHABAMBA');
INSERT INTO ubigeo VALUES ('100404', '10', '04', '04', 'PINRA');
INSERT INTO ubigeo VALUES ('100500', '10', '05', '00', 'HUAMALIES');
INSERT INTO ubigeo VALUES ('100501', '10', '05', '01', 'LLATA');
INSERT INTO ubigeo VALUES ('100502', '10', '05', '02', 'ARANCAY');
INSERT INTO ubigeo VALUES ('100503', '10', '05', '03', 'CHAVIN DE PARIARCA');
INSERT INTO ubigeo VALUES ('100504', '10', '05', '04', 'JACAS GRANDE');
INSERT INTO ubigeo VALUES ('100505', '10', '05', '05', 'JIRCAN');
INSERT INTO ubigeo VALUES ('100506', '10', '05', '06', 'MIRAFLORES');
INSERT INTO ubigeo VALUES ('100507', '10', '05', '07', 'MONZON');
INSERT INTO ubigeo VALUES ('100508', '10', '05', '08', 'PUNCHAO');
INSERT INTO ubigeo VALUES ('100509', '10', '05', '09', 'PUÑOS');
INSERT INTO ubigeo VALUES ('100510', '10', '05', '10', 'SINGA');
INSERT INTO ubigeo VALUES ('100511', '10', '05', '11', 'TANTAMAYO');
INSERT INTO ubigeo VALUES ('100600', '10', '06', '00', 'LEONCIO PRADO');
INSERT INTO ubigeo VALUES ('100601', '10', '06', '01', 'RUPA-RUPA');
INSERT INTO ubigeo VALUES ('100602', '10', '06', '02', 'DANIEL ALOMIA ROBLES');
INSERT INTO ubigeo VALUES ('100603', '10', '06', '03', 'HERMILIO VALDIZAN');
INSERT INTO ubigeo VALUES ('100604', '10', '06', '04', 'JOSE CRESPO Y CASTILLO');
INSERT INTO ubigeo VALUES ('100605', '10', '06', '05', 'LUYANDO');
INSERT INTO ubigeo VALUES ('100606', '10', '06', '06', 'MARIANO DAMASO BERAUN');
INSERT INTO ubigeo VALUES ('100700', '10', '07', '00', 'MARAÑON');
INSERT INTO ubigeo VALUES ('100701', '10', '07', '01', 'HUACRACHUCO');
INSERT INTO ubigeo VALUES ('100702', '10', '07', '02', 'CHOLON');
INSERT INTO ubigeo VALUES ('100703', '10', '07', '03', 'SAN BUENAVENTURA');
INSERT INTO ubigeo VALUES ('100800', '10', '08', '00', 'PACHITEA');
INSERT INTO ubigeo VALUES ('100801', '10', '08', '01', 'PANAO');
INSERT INTO ubigeo VALUES ('100802', '10', '08', '02', 'CHAGLLA');
INSERT INTO ubigeo VALUES ('100803', '10', '08', '03', 'MOLINO');
INSERT INTO ubigeo VALUES ('100804', '10', '08', '04', 'UMARI');
INSERT INTO ubigeo VALUES ('100900', '10', '09', '00', 'PUERTO INCA');
INSERT INTO ubigeo VALUES ('100901', '10', '09', '01', 'PUERTO INCA');
INSERT INTO ubigeo VALUES ('100902', '10', '09', '02', 'CODO DEL POZUZO');
INSERT INTO ubigeo VALUES ('100903', '10', '09', '03', 'HONORIA');
INSERT INTO ubigeo VALUES ('100904', '10', '09', '04', 'TOURNAVISTA');
INSERT INTO ubigeo VALUES ('100905', '10', '09', '05', 'YUYAPICHIS');
INSERT INTO ubigeo VALUES ('101000', '10', '10', '00', 'LAURICOCHA');
INSERT INTO ubigeo VALUES ('101001', '10', '10', '01', 'JESUS');
INSERT INTO ubigeo VALUES ('101002', '10', '10', '02', 'BAÑOS');
INSERT INTO ubigeo VALUES ('101003', '10', '10', '03', 'JIVIA');
INSERT INTO ubigeo VALUES ('101004', '10', '10', '04', 'QUEROPALCA');
INSERT INTO ubigeo VALUES ('101005', '10', '10', '05', 'RONDOS');
INSERT INTO ubigeo VALUES ('101006', '10', '10', '06', 'SAN FRANCISCO DE ASIS');
INSERT INTO ubigeo VALUES ('101007', '10', '10', '07', 'SAN MIGUEL DE CAURI');
INSERT INTO ubigeo VALUES ('101100', '10', '11', '00', 'YAROWILCA');
INSERT INTO ubigeo VALUES ('101101', '10', '11', '01', 'CHAVINILLO');
INSERT INTO ubigeo VALUES ('101102', '10', '11', '02', 'CAHUAC');
INSERT INTO ubigeo VALUES ('101103', '10', '11', '03', 'CHACABAMBA');
INSERT INTO ubigeo VALUES ('101104', '10', '11', '04', 'APARICIO POMARES');
INSERT INTO ubigeo VALUES ('101105', '10', '11', '05', 'JACAS CHICO');
INSERT INTO ubigeo VALUES ('101106', '10', '11', '06', 'OBAS');
INSERT INTO ubigeo VALUES ('101107', '10', '11', '07', 'PAMPAMARCA');
INSERT INTO ubigeo VALUES ('101108', '10', '11', '08', 'CHORAS');
INSERT INTO ubigeo VALUES ('110000', '11', '00', '00', 'ICA');
INSERT INTO ubigeo VALUES ('110100', '11', '01', '00', 'ICA');
INSERT INTO ubigeo VALUES ('110101', '11', '01', '01', 'ICA');
INSERT INTO ubigeo VALUES ('110102', '11', '01', '02', 'LA TINGUIÑA');
INSERT INTO ubigeo VALUES ('110103', '11', '01', '03', 'LOS AQUIJES');
INSERT INTO ubigeo VALUES ('110104', '11', '01', '04', 'OCUCAJE');
INSERT INTO ubigeo VALUES ('110105', '11', '01', '05', 'PACHACUTEC');
INSERT INTO ubigeo VALUES ('110106', '11', '01', '06', 'PARCONA');
INSERT INTO ubigeo VALUES ('110107', '11', '01', '07', 'PUEBLO NUEVO');
INSERT INTO ubigeo VALUES ('110108', '11', '01', '08', 'SALAS');
INSERT INTO ubigeo VALUES ('110109', '11', '01', '09', 'SAN JOSE DE LOS MOLINOS');
INSERT INTO ubigeo VALUES ('110110', '11', '01', '10', 'SAN JUAN BAUTISTA');
INSERT INTO ubigeo VALUES ('110111', '11', '01', '11', 'SANTIAGO');
INSERT INTO ubigeo VALUES ('110112', '11', '01', '12', 'SUBTANJALLA');
INSERT INTO ubigeo VALUES ('110113', '11', '01', '13', 'TATE');
INSERT INTO ubigeo VALUES ('110114', '11', '01', '14', 'YAUCA DEL ROSARIO');
INSERT INTO ubigeo VALUES ('110200', '11', '02', '00', 'CHINCHA');
INSERT INTO ubigeo VALUES ('110201', '11', '02', '01', 'CHINCHA ALTA');
INSERT INTO ubigeo VALUES ('110202', '11', '02', '02', 'ALTO LARAN');
INSERT INTO ubigeo VALUES ('110203', '11', '02', '03', 'CHAVIN');
INSERT INTO ubigeo VALUES ('110204', '11', '02', '04', 'CHINCHA BAJA');
INSERT INTO ubigeo VALUES ('110205', '11', '02', '05', 'EL CARMEN');
INSERT INTO ubigeo VALUES ('110206', '11', '02', '06', 'GROCIO PRADO');
INSERT INTO ubigeo VALUES ('110207', '11', '02', '07', 'PUEBLO NUEVO');
INSERT INTO ubigeo VALUES ('110208', '11', '02', '08', 'SAN JUAN DE YANAC');
INSERT INTO ubigeo VALUES ('110209', '11', '02', '09', 'SAN PEDRO DE HUACARPANA');
INSERT INTO ubigeo VALUES ('110210', '11', '02', '10', 'SUNAMPE');
INSERT INTO ubigeo VALUES ('110211', '11', '02', '11', 'TAMBO DE MORA');
INSERT INTO ubigeo VALUES ('110300', '11', '03', '00', 'NAZCA');
INSERT INTO ubigeo VALUES ('110301', '11', '03', '01', 'NAZCA');
INSERT INTO ubigeo VALUES ('110302', '11', '03', '02', 'CHANGUILLO');
INSERT INTO ubigeo VALUES ('110303', '11', '03', '03', 'EL INGENIO');
INSERT INTO ubigeo VALUES ('110304', '11', '03', '04', 'MARCONA');
INSERT INTO ubigeo VALUES ('110305', '11', '03', '05', 'VISTA ALEGRE');
INSERT INTO ubigeo VALUES ('110400', '11', '04', '00', 'PALPA');
INSERT INTO ubigeo VALUES ('110401', '11', '04', '01', 'PALPA');
INSERT INTO ubigeo VALUES ('110402', '11', '04', '02', 'LLIPATA');
INSERT INTO ubigeo VALUES ('110403', '11', '04', '03', 'RIO GRANDE');
INSERT INTO ubigeo VALUES ('110404', '11', '04', '04', 'SANTA CRUZ');
INSERT INTO ubigeo VALUES ('110405', '11', '04', '05', 'TIBILLO');
INSERT INTO ubigeo VALUES ('110500', '11', '05', '00', 'PISCO');
INSERT INTO ubigeo VALUES ('110501', '11', '05', '01', 'PISCO');
INSERT INTO ubigeo VALUES ('110502', '11', '05', '02', 'HUANCANO');
INSERT INTO ubigeo VALUES ('110503', '11', '05', '03', 'HUMAY');
INSERT INTO ubigeo VALUES ('110504', '11', '05', '04', 'INDEPENDENCIA');
INSERT INTO ubigeo VALUES ('110505', '11', '05', '05', 'PARACAS');
INSERT INTO ubigeo VALUES ('110506', '11', '05', '06', 'SAN ANDRES');
INSERT INTO ubigeo VALUES ('110507', '11', '05', '07', 'SAN CLEMENTE');
INSERT INTO ubigeo VALUES ('110508', '11', '05', '08', 'TUPAC AMARU INCA');
INSERT INTO ubigeo VALUES ('120000', '12', '00', '00', 'JUNIN');
INSERT INTO ubigeo VALUES ('120100', '12', '01', '00', 'HUANCAYO');
INSERT INTO ubigeo VALUES ('120101', '12', '01', '01', 'HUANCAYO');
INSERT INTO ubigeo VALUES ('120104', '12', '01', '04', 'CARHUACALLANGA');
INSERT INTO ubigeo VALUES ('120105', '12', '01', '05', 'CHACAPAMPA');
INSERT INTO ubigeo VALUES ('120106', '12', '01', '06', 'CHICCHE');
INSERT INTO ubigeo VALUES ('120107', '12', '01', '07', 'CHILCA');
INSERT INTO ubigeo VALUES ('120108', '12', '01', '08', 'CHONGOS ALTO');
INSERT INTO ubigeo VALUES ('120111', '12', '01', '11', 'CHUPURO');
INSERT INTO ubigeo VALUES ('120112', '12', '01', '12', 'COLCA');
INSERT INTO ubigeo VALUES ('120113', '12', '01', '13', 'CULLHUAS');
INSERT INTO ubigeo VALUES ('120114', '12', '01', '14', 'EL TAMBO');
INSERT INTO ubigeo VALUES ('120116', '12', '01', '16', 'HUACRAPUQUIO');
INSERT INTO ubigeo VALUES ('120117', '12', '01', '17', 'HUALHUAS');
INSERT INTO ubigeo VALUES ('120119', '12', '01', '19', 'HUANCAN');
INSERT INTO ubigeo VALUES ('120120', '12', '01', '20', 'HUASICANCHA');
INSERT INTO ubigeo VALUES ('120121', '12', '01', '21', 'HUAYUCACHI');
INSERT INTO ubigeo VALUES ('120122', '12', '01', '22', 'INGENIO');
INSERT INTO ubigeo VALUES ('120124', '12', '01', '24', 'PARIAHUANCA');
INSERT INTO ubigeo VALUES ('120125', '12', '01', '25', 'PILCOMAYO');
INSERT INTO ubigeo VALUES ('120126', '12', '01', '26', 'PUCARA');
INSERT INTO ubigeo VALUES ('120127', '12', '01', '27', 'QUICHUAY');
INSERT INTO ubigeo VALUES ('120128', '12', '01', '28', 'QUILCAS');
INSERT INTO ubigeo VALUES ('120129', '12', '01', '29', 'SAN AGUSTIN');
INSERT INTO ubigeo VALUES ('120130', '12', '01', '30', 'SAN JERONIMO DE TUNAN');
INSERT INTO ubigeo VALUES ('120132', '12', '01', '32', 'SAÑO');
INSERT INTO ubigeo VALUES ('120133', '12', '01', '33', 'SAPALLANGA');
INSERT INTO ubigeo VALUES ('120134', '12', '01', '34', 'SICAYA');
INSERT INTO ubigeo VALUES ('120135', '12', '01', '35', 'SANTO DOMINGO DE ACOBAMBA');
INSERT INTO ubigeo VALUES ('120136', '12', '01', '36', 'VIQUES');
INSERT INTO ubigeo VALUES ('120200', '12', '02', '00', 'CONCEPCION');
INSERT INTO ubigeo VALUES ('120201', '12', '02', '01', 'CONCEPCION');
INSERT INTO ubigeo VALUES ('120202', '12', '02', '02', 'ACO');
INSERT INTO ubigeo VALUES ('120203', '12', '02', '03', 'ANDAMARCA');
INSERT INTO ubigeo VALUES ('120204', '12', '02', '04', 'CHAMBARA');
INSERT INTO ubigeo VALUES ('120205', '12', '02', '05', 'COCHAS');
INSERT INTO ubigeo VALUES ('120206', '12', '02', '06', 'COMAS');
INSERT INTO ubigeo VALUES ('120207', '12', '02', '07', 'HEROINAS TOLEDO');
INSERT INTO ubigeo VALUES ('120208', '12', '02', '08', 'MANZANARES');
INSERT INTO ubigeo VALUES ('120209', '12', '02', '09', 'MARISCAL CASTILLA');
INSERT INTO ubigeo VALUES ('120210', '12', '02', '10', 'MATAHUASI');
INSERT INTO ubigeo VALUES ('120211', '12', '02', '11', 'MITO');
INSERT INTO ubigeo VALUES ('120212', '12', '02', '12', 'NUEVE DE JULIO');
INSERT INTO ubigeo VALUES ('120213', '12', '02', '13', 'ORCOTUNA');
INSERT INTO ubigeo VALUES ('120214', '12', '02', '14', 'SAN JOSE DE QUERO');
INSERT INTO ubigeo VALUES ('120215', '12', '02', '15', 'SANTA ROSA DE OCOPA');
INSERT INTO ubigeo VALUES ('120300', '12', '03', '00', 'CHANCHAMAYO');
INSERT INTO ubigeo VALUES ('120301', '12', '03', '01', 'CHANCHAMAYO');
INSERT INTO ubigeo VALUES ('120302', '12', '03', '02', 'PERENE');
INSERT INTO ubigeo VALUES ('120303', '12', '03', '03', 'PICHANAQUI');
INSERT INTO ubigeo VALUES ('120304', '12', '03', '04', 'SAN LUIS DE SHUARO');
INSERT INTO ubigeo VALUES ('120305', '12', '03', '05', 'SAN RAMON');
INSERT INTO ubigeo VALUES ('120306', '12', '03', '06', 'VITOC');
INSERT INTO ubigeo VALUES ('120400', '12', '04', '00', 'JAUJA');
INSERT INTO ubigeo VALUES ('120401', '12', '04', '01', 'JAUJA');
INSERT INTO ubigeo VALUES ('120402', '12', '04', '02', 'ACOLLA');
INSERT INTO ubigeo VALUES ('120403', '12', '04', '03', 'APATA');
INSERT INTO ubigeo VALUES ('120404', '12', '04', '04', 'ATAURA');
INSERT INTO ubigeo VALUES ('120405', '12', '04', '05', 'CANCHAYLLO');
INSERT INTO ubigeo VALUES ('120406', '12', '04', '06', 'CURICACA');
INSERT INTO ubigeo VALUES ('120407', '12', '04', '07', 'EL MANTARO');
INSERT INTO ubigeo VALUES ('120408', '12', '04', '08', 'HUAMALI');
INSERT INTO ubigeo VALUES ('120409', '12', '04', '09', 'HUARIPAMPA');
INSERT INTO ubigeo VALUES ('120410', '12', '04', '10', 'HUERTAS');
INSERT INTO ubigeo VALUES ('120411', '12', '04', '11', 'JANJAILLO');
INSERT INTO ubigeo VALUES ('120412', '12', '04', '12', 'JULCAN');
INSERT INTO ubigeo VALUES ('120413', '12', '04', '13', 'LEONOR ORDOÑEZ');
INSERT INTO ubigeo VALUES ('120414', '12', '04', '14', 'LLOCLLAPAMPA');
INSERT INTO ubigeo VALUES ('120415', '12', '04', '15', 'MARCO');
INSERT INTO ubigeo VALUES ('120416', '12', '04', '16', 'MASMA');
INSERT INTO ubigeo VALUES ('120417', '12', '04', '17', 'MASMA CHICCHE');
INSERT INTO ubigeo VALUES ('120418', '12', '04', '18', 'MOLINOS');
INSERT INTO ubigeo VALUES ('120419', '12', '04', '19', 'MONOBAMBA');
INSERT INTO ubigeo VALUES ('120420', '12', '04', '20', 'MUQUI');
INSERT INTO ubigeo VALUES ('120421', '12', '04', '21', 'MUQUIYAUYO');
INSERT INTO ubigeo VALUES ('120422', '12', '04', '22', 'PACA');
INSERT INTO ubigeo VALUES ('120423', '12', '04', '23', 'PACCHA');
INSERT INTO ubigeo VALUES ('120424', '12', '04', '24', 'PANCAN');
INSERT INTO ubigeo VALUES ('120425', '12', '04', '25', 'PARCO');
INSERT INTO ubigeo VALUES ('120426', '12', '04', '26', 'POMACANCHA');
INSERT INTO ubigeo VALUES ('120427', '12', '04', '27', 'RICRAN');
INSERT INTO ubigeo VALUES ('120428', '12', '04', '28', 'SAN LORENZO');
INSERT INTO ubigeo VALUES ('120429', '12', '04', '29', 'SAN PEDRO DE CHUNAN');
INSERT INTO ubigeo VALUES ('120430', '12', '04', '30', 'SAUSA');
INSERT INTO ubigeo VALUES ('120431', '12', '04', '31', 'SINCOS');
INSERT INTO ubigeo VALUES ('120432', '12', '04', '32', 'TUNAN MARCA');
INSERT INTO ubigeo VALUES ('120433', '12', '04', '33', 'YAULI');
INSERT INTO ubigeo VALUES ('120434', '12', '04', '34', 'YAUYOS');
INSERT INTO ubigeo VALUES ('120500', '12', '05', '00', 'JUNIN');
INSERT INTO ubigeo VALUES ('120501', '12', '05', '01', 'JUNIN');
INSERT INTO ubigeo VALUES ('120502', '12', '05', '02', 'CARHUAMAYO');
INSERT INTO ubigeo VALUES ('120503', '12', '05', '03', 'ONDORES');
INSERT INTO ubigeo VALUES ('120504', '12', '05', '04', 'ULCUMAYO');
INSERT INTO ubigeo VALUES ('120600', '12', '06', '00', 'SATIPO');
INSERT INTO ubigeo VALUES ('120601', '12', '06', '01', 'SATIPO');
INSERT INTO ubigeo VALUES ('120602', '12', '06', '02', 'COVIRIALI');
INSERT INTO ubigeo VALUES ('120603', '12', '06', '03', 'LLAYLLA');
INSERT INTO ubigeo VALUES ('120605', '12', '06', '05', 'PAMPA HERMOSA');
INSERT INTO ubigeo VALUES ('120607', '12', '06', '07', 'RIO NEGRO');
INSERT INTO ubigeo VALUES ('120608', '12', '06', '08', 'RIO TAMBO');
INSERT INTO ubigeo VALUES ('120699', '12', '06', '99', 'MAZAMARI - PANGOA');
INSERT INTO ubigeo VALUES ('120700', '12', '07', '00', 'TARMA');
INSERT INTO ubigeo VALUES ('120701', '12', '07', '01', 'TARMA');
INSERT INTO ubigeo VALUES ('120702', '12', '07', '02', 'ACOBAMBA');
INSERT INTO ubigeo VALUES ('120703', '12', '07', '03', 'HUARICOLCA');
INSERT INTO ubigeo VALUES ('120704', '12', '07', '04', 'HUASAHUASI');
INSERT INTO ubigeo VALUES ('120705', '12', '07', '05', 'LA UNION');
INSERT INTO ubigeo VALUES ('120706', '12', '07', '06', 'PALCA');
INSERT INTO ubigeo VALUES ('120707', '12', '07', '07', 'PALCAMAYO');
INSERT INTO ubigeo VALUES ('120708', '12', '07', '08', 'SAN PEDRO DE CAJAS');
INSERT INTO ubigeo VALUES ('120709', '12', '07', '09', 'TAPO');
INSERT INTO ubigeo VALUES ('120800', '12', '08', '00', 'YAULI');
INSERT INTO ubigeo VALUES ('120801', '12', '08', '01', 'LA OROYA');
INSERT INTO ubigeo VALUES ('120802', '12', '08', '02', 'CHACAPALPA');
INSERT INTO ubigeo VALUES ('120803', '12', '08', '03', 'HUAY-HUAY');
INSERT INTO ubigeo VALUES ('120804', '12', '08', '04', 'MARCAPOMACOCHA');
INSERT INTO ubigeo VALUES ('120805', '12', '08', '05', 'MOROCOCHA');
INSERT INTO ubigeo VALUES ('120806', '12', '08', '06', 'PACCHA');
INSERT INTO ubigeo VALUES ('120807', '12', '08', '07', 'SANTA BARBARA DE CARHUACAYAN');
INSERT INTO ubigeo VALUES ('120808', '12', '08', '08', 'SANTA ROSA DE SACCO');
INSERT INTO ubigeo VALUES ('120809', '12', '08', '09', 'SUITUCANCHA');
INSERT INTO ubigeo VALUES ('120810', '12', '08', '10', 'YAULI');
INSERT INTO ubigeo VALUES ('120900', '12', '09', '00', 'CHUPACA');
INSERT INTO ubigeo VALUES ('120901', '12', '09', '01', 'CHUPACA');
INSERT INTO ubigeo VALUES ('120902', '12', '09', '02', 'AHUAC');
INSERT INTO ubigeo VALUES ('120903', '12', '09', '03', 'CHONGOS BAJO');
INSERT INTO ubigeo VALUES ('120904', '12', '09', '04', 'HUACHAC');
INSERT INTO ubigeo VALUES ('120905', '12', '09', '05', 'HUAMANCACA CHICO');
INSERT INTO ubigeo VALUES ('120906', '12', '09', '06', 'SAN JUAN DE ISCOS');
INSERT INTO ubigeo VALUES ('120907', '12', '09', '07', 'SAN JUAN DE JARPA');
INSERT INTO ubigeo VALUES ('120908', '12', '09', '08', 'TRES DE DICIEMBRE');
INSERT INTO ubigeo VALUES ('120909', '12', '09', '09', 'YANACANCHA');
INSERT INTO ubigeo VALUES ('130000', '13', '00', '00', 'LA LIBERTAD');
INSERT INTO ubigeo VALUES ('130100', '13', '01', '00', 'TRUJILLO');
INSERT INTO ubigeo VALUES ('130101', '13', '01', '01', 'TRUJILLO');
INSERT INTO ubigeo VALUES ('130102', '13', '01', '02', 'EL PORVENIR');
INSERT INTO ubigeo VALUES ('130103', '13', '01', '03', 'FLORENCIA DE MORA');
INSERT INTO ubigeo VALUES ('130104', '13', '01', '04', 'HUANCHACO');
INSERT INTO ubigeo VALUES ('130105', '13', '01', '05', 'LA ESPERANZA');
INSERT INTO ubigeo VALUES ('130106', '13', '01', '06', 'LAREDO');
INSERT INTO ubigeo VALUES ('130107', '13', '01', '07', 'MOCHE');
INSERT INTO ubigeo VALUES ('130108', '13', '01', '08', 'POROTO');
INSERT INTO ubigeo VALUES ('130109', '13', '01', '09', 'SALAVERRY');
INSERT INTO ubigeo VALUES ('130110', '13', '01', '10', 'SIMBAL');
INSERT INTO ubigeo VALUES ('130111', '13', '01', '11', 'VICTOR LARCO HERRERA');
INSERT INTO ubigeo VALUES ('130200', '13', '02', '00', 'ASCOPE');
INSERT INTO ubigeo VALUES ('130201', '13', '02', '01', 'ASCOPE');
INSERT INTO ubigeo VALUES ('130202', '13', '02', '02', 'CHICAMA');
INSERT INTO ubigeo VALUES ('130203', '13', '02', '03', 'CHOCOPE');
INSERT INTO ubigeo VALUES ('130204', '13', '02', '04', 'MAGDALENA DE CAO');
INSERT INTO ubigeo VALUES ('130205', '13', '02', '05', 'PAIJAN');
INSERT INTO ubigeo VALUES ('130206', '13', '02', '06', 'RAZURI');
INSERT INTO ubigeo VALUES ('130207', '13', '02', '07', 'SANTIAGO DE CAO');
INSERT INTO ubigeo VALUES ('130208', '13', '02', '08', 'CASA GRANDE');
INSERT INTO ubigeo VALUES ('130300', '13', '03', '00', 'BOLIVAR');
INSERT INTO ubigeo VALUES ('130301', '13', '03', '01', 'BOLIVAR');
INSERT INTO ubigeo VALUES ('130302', '13', '03', '02', 'BAMBAMARCA');
INSERT INTO ubigeo VALUES ('130303', '13', '03', '03', 'CONDORMARCA');
INSERT INTO ubigeo VALUES ('130304', '13', '03', '04', 'LONGOTEA');
INSERT INTO ubigeo VALUES ('130305', '13', '03', '05', 'UCHUMARCA');
INSERT INTO ubigeo VALUES ('130306', '13', '03', '06', 'UCUNCHA');
INSERT INTO ubigeo VALUES ('130400', '13', '04', '00', 'CHEPEN');
INSERT INTO ubigeo VALUES ('130401', '13', '04', '01', 'CHEPEN');
INSERT INTO ubigeo VALUES ('130402', '13', '04', '02', 'PACANGA');
INSERT INTO ubigeo VALUES ('130403', '13', '04', '03', 'PUEBLO NUEVO');
INSERT INTO ubigeo VALUES ('130500', '13', '05', '00', 'JULCAN');
INSERT INTO ubigeo VALUES ('130501', '13', '05', '01', 'JULCAN');
INSERT INTO ubigeo VALUES ('130502', '13', '05', '02', 'CALAMARCA');
INSERT INTO ubigeo VALUES ('130503', '13', '05', '03', 'CARABAMBA');
INSERT INTO ubigeo VALUES ('130504', '13', '05', '04', 'HUASO');
INSERT INTO ubigeo VALUES ('130600', '13', '06', '00', 'OTUZCO');
INSERT INTO ubigeo VALUES ('130601', '13', '06', '01', 'OTUZCO');
INSERT INTO ubigeo VALUES ('130602', '13', '06', '02', 'AGALLPAMPA');
INSERT INTO ubigeo VALUES ('130604', '13', '06', '04', 'CHARAT');
INSERT INTO ubigeo VALUES ('130605', '13', '06', '05', 'HUARANCHAL');
INSERT INTO ubigeo VALUES ('130606', '13', '06', '06', 'LA CUESTA');
INSERT INTO ubigeo VALUES ('130608', '13', '06', '08', 'MACHE');
INSERT INTO ubigeo VALUES ('130610', '13', '06', '10', 'PARANDAY');
INSERT INTO ubigeo VALUES ('130611', '13', '06', '11', 'SALPO');
INSERT INTO ubigeo VALUES ('130613', '13', '06', '13', 'SINSICAP');
INSERT INTO ubigeo VALUES ('130614', '13', '06', '14', 'USQUIL');
INSERT INTO ubigeo VALUES ('130700', '13', '07', '00', 'PACASMAYO');
INSERT INTO ubigeo VALUES ('130701', '13', '07', '01', 'SAN PEDRO DE LLOC');
INSERT INTO ubigeo VALUES ('130702', '13', '07', '02', 'GUADALUPE');
INSERT INTO ubigeo VALUES ('130703', '13', '07', '03', 'JEQUETEPEQUE');
INSERT INTO ubigeo VALUES ('130704', '13', '07', '04', 'PACASMAYO');
INSERT INTO ubigeo VALUES ('130705', '13', '07', '05', 'SAN JOSE');
INSERT INTO ubigeo VALUES ('130800', '13', '08', '00', 'PATAZ');
INSERT INTO ubigeo VALUES ('130801', '13', '08', '01', 'TAYABAMBA');
INSERT INTO ubigeo VALUES ('130802', '13', '08', '02', 'BULDIBUYO');
INSERT INTO ubigeo VALUES ('130803', '13', '08', '03', 'CHILLIA');
INSERT INTO ubigeo VALUES ('130804', '13', '08', '04', 'HUANCASPATA');
INSERT INTO ubigeo VALUES ('130805', '13', '08', '05', 'HUAYLILLAS');
INSERT INTO ubigeo VALUES ('130806', '13', '08', '06', 'HUAYO');
INSERT INTO ubigeo VALUES ('130807', '13', '08', '07', 'ONGON');
INSERT INTO ubigeo VALUES ('130808', '13', '08', '08', 'PARCOY');
INSERT INTO ubigeo VALUES ('130809', '13', '08', '09', 'PATAZ');
INSERT INTO ubigeo VALUES ('130810', '13', '08', '10', 'PIAS');
INSERT INTO ubigeo VALUES ('130811', '13', '08', '11', 'SANTIAGO DE CHALLAS');
INSERT INTO ubigeo VALUES ('130812', '13', '08', '12', 'TAURIJA');
INSERT INTO ubigeo VALUES ('130813', '13', '08', '13', 'URPAY');
INSERT INTO ubigeo VALUES ('130900', '13', '09', '00', 'SANCHEZ CARRION');
INSERT INTO ubigeo VALUES ('130901', '13', '09', '01', 'HUAMACHUCO');
INSERT INTO ubigeo VALUES ('130902', '13', '09', '02', 'CHUGAY');
INSERT INTO ubigeo VALUES ('130903', '13', '09', '03', 'COCHORCO');
INSERT INTO ubigeo VALUES ('130904', '13', '09', '04', 'CURGOS');
INSERT INTO ubigeo VALUES ('130905', '13', '09', '05', 'MARCABAL');
INSERT INTO ubigeo VALUES ('130906', '13', '09', '06', 'SANAGORAN');
INSERT INTO ubigeo VALUES ('130907', '13', '09', '07', 'SARIN');
INSERT INTO ubigeo VALUES ('130908', '13', '09', '08', 'SARTIMBAMBA');
INSERT INTO ubigeo VALUES ('131000', '13', '10', '00', 'SANTIAGO DE CHUCO');
INSERT INTO ubigeo VALUES ('131001', '13', '10', '01', 'SANTIAGO DE CHUCO');
INSERT INTO ubigeo VALUES ('131002', '13', '10', '02', 'ANGASMARCA');
INSERT INTO ubigeo VALUES ('131003', '13', '10', '03', 'CACHICADAN');
INSERT INTO ubigeo VALUES ('131004', '13', '10', '04', 'MOLLEBAMBA');
INSERT INTO ubigeo VALUES ('131005', '13', '10', '05', 'MOLLEPATA');
INSERT INTO ubigeo VALUES ('131006', '13', '10', '06', 'QUIRUVILCA');
INSERT INTO ubigeo VALUES ('131007', '13', '10', '07', 'SANTA CRUZ DE CHUCA');
INSERT INTO ubigeo VALUES ('131008', '13', '10', '08', 'SITABAMBA');
INSERT INTO ubigeo VALUES ('131100', '13', '11', '00', 'GRAN CHIMU');
INSERT INTO ubigeo VALUES ('131101', '13', '11', '01', 'CASCAS');
INSERT INTO ubigeo VALUES ('131102', '13', '11', '02', 'LUCMA');
INSERT INTO ubigeo VALUES ('131103', '13', '11', '03', 'MARMOT');
INSERT INTO ubigeo VALUES ('131104', '13', '11', '04', 'SAYAPULLO');
INSERT INTO ubigeo VALUES ('131200', '13', '12', '00', 'VIRU');
INSERT INTO ubigeo VALUES ('131201', '13', '12', '01', 'VIRU');
INSERT INTO ubigeo VALUES ('131202', '13', '12', '02', 'CHAO');
INSERT INTO ubigeo VALUES ('131203', '13', '12', '03', 'GUADALUPITO');
INSERT INTO ubigeo VALUES ('140000', '14', '00', '00', 'LAMBAYEQUE');
INSERT INTO ubigeo VALUES ('140100', '14', '01', '00', 'CHICLAYO');
INSERT INTO ubigeo VALUES ('140101', '14', '01', '01', 'CHICLAYO');
INSERT INTO ubigeo VALUES ('140102', '14', '01', '02', 'CHONGOYAPE');
INSERT INTO ubigeo VALUES ('140103', '14', '01', '03', 'ETEN');
INSERT INTO ubigeo VALUES ('140104', '14', '01', '04', 'ETEN PUERTO');
INSERT INTO ubigeo VALUES ('140105', '14', '01', '05', 'JOSE LEONARDO ORTIZ');
INSERT INTO ubigeo VALUES ('140106', '14', '01', '06', 'LA VICTORIA');
INSERT INTO ubigeo VALUES ('140107', '14', '01', '07', 'LAGUNAS');
INSERT INTO ubigeo VALUES ('140108', '14', '01', '08', 'MONSEFU');
INSERT INTO ubigeo VALUES ('140109', '14', '01', '09', 'NUEVA ARICA');
INSERT INTO ubigeo VALUES ('140110', '14', '01', '10', 'OYOTUN');
INSERT INTO ubigeo VALUES ('140111', '14', '01', '11', 'PICSI');
INSERT INTO ubigeo VALUES ('140112', '14', '01', '12', 'PIMENTEL');
INSERT INTO ubigeo VALUES ('140113', '14', '01', '13', 'REQUE');
INSERT INTO ubigeo VALUES ('140114', '14', '01', '14', 'SANTA ROSA');
INSERT INTO ubigeo VALUES ('140115', '14', '01', '15', 'SAÑA');
INSERT INTO ubigeo VALUES ('140116', '14', '01', '16', 'CAYALTI');
INSERT INTO ubigeo VALUES ('140117', '14', '01', '17', 'PATAPO');
INSERT INTO ubigeo VALUES ('140118', '14', '01', '18', 'POMALCA');
INSERT INTO ubigeo VALUES ('140119', '14', '01', '19', 'PUCALA');
INSERT INTO ubigeo VALUES ('140120', '14', '01', '20', 'TUMAN');
INSERT INTO ubigeo VALUES ('140200', '14', '02', '00', 'FERREÑAFE');
INSERT INTO ubigeo VALUES ('140201', '14', '02', '01', 'FERREÑAFE');
INSERT INTO ubigeo VALUES ('140202', '14', '02', '02', 'CAÑARIS');
INSERT INTO ubigeo VALUES ('140203', '14', '02', '03', 'INCAHUASI');
INSERT INTO ubigeo VALUES ('140204', '14', '02', '04', 'MANUEL ANTONIO MESONES MURO');
INSERT INTO ubigeo VALUES ('140205', '14', '02', '05', 'PITIPO');
INSERT INTO ubigeo VALUES ('140206', '14', '02', '06', 'PUEBLO NUEVO');
INSERT INTO ubigeo VALUES ('140300', '14', '03', '00', 'LAMBAYEQUE');
INSERT INTO ubigeo VALUES ('140301', '14', '03', '01', 'LAMBAYEQUE');
INSERT INTO ubigeo VALUES ('140302', '14', '03', '02', 'CHOCHOPE');
INSERT INTO ubigeo VALUES ('140303', '14', '03', '03', 'ILLIMO');
INSERT INTO ubigeo VALUES ('140304', '14', '03', '04', 'JAYANCA');
INSERT INTO ubigeo VALUES ('140305', '14', '03', '05', 'MOCHUMI');
INSERT INTO ubigeo VALUES ('140306', '14', '03', '06', 'MORROPE');
INSERT INTO ubigeo VALUES ('140307', '14', '03', '07', 'MOTUPE');
INSERT INTO ubigeo VALUES ('140308', '14', '03', '08', 'OLMOS');
INSERT INTO ubigeo VALUES ('140309', '14', '03', '09', 'PACORA');
INSERT INTO ubigeo VALUES ('140310', '14', '03', '10', 'SALAS');
INSERT INTO ubigeo VALUES ('140311', '14', '03', '11', 'SAN JOSE');
INSERT INTO ubigeo VALUES ('140312', '14', '03', '12', 'TUCUME');
INSERT INTO ubigeo VALUES ('150000', '15', '00', '00', 'LIMA');
INSERT INTO ubigeo VALUES ('150100', '15', '01', '00', 'LIMA');
INSERT INTO ubigeo VALUES ('150101', '15', '01', '01', 'LIMA');
INSERT INTO ubigeo VALUES ('150102', '15', '01', '02', 'ANCON');
INSERT INTO ubigeo VALUES ('150103', '15', '01', '03', 'ATE');
INSERT INTO ubigeo VALUES ('150104', '15', '01', '04', 'BARRANCO');
INSERT INTO ubigeo VALUES ('150105', '15', '01', '05', 'BREÑA');
INSERT INTO ubigeo VALUES ('150106', '15', '01', '06', 'CARABAYLLO');
INSERT INTO ubigeo VALUES ('150107', '15', '01', '07', 'CHACLACAYO');
INSERT INTO ubigeo VALUES ('150108', '15', '01', '08', 'CHORRILLOS');
INSERT INTO ubigeo VALUES ('150109', '15', '01', '09', 'CIENEGUILLA');
INSERT INTO ubigeo VALUES ('150110', '15', '01', '10', 'COMAS');
INSERT INTO ubigeo VALUES ('150111', '15', '01', '11', 'EL AGUSTINO');
INSERT INTO ubigeo VALUES ('150112', '15', '01', '12', 'INDEPENDENCIA');
INSERT INTO ubigeo VALUES ('150113', '15', '01', '13', 'JESUS MARIA');
INSERT INTO ubigeo VALUES ('150114', '15', '01', '14', 'LA MOLINA');
INSERT INTO ubigeo VALUES ('150115', '15', '01', '15', 'LA VICTORIA');
INSERT INTO ubigeo VALUES ('150116', '15', '01', '16', 'LINCE');
INSERT INTO ubigeo VALUES ('150117', '15', '01', '17', 'LOS OLIVOS');
INSERT INTO ubigeo VALUES ('150118', '15', '01', '18', 'LURIGANCHO');
INSERT INTO ubigeo VALUES ('150119', '15', '01', '19', 'LURIN');
INSERT INTO ubigeo VALUES ('150120', '15', '01', '20', 'MAGDALENA DEL MAR');
INSERT INTO ubigeo VALUES ('150121', '15', '01', '21', 'PUEBLO LIBRE');
INSERT INTO ubigeo VALUES ('150122', '15', '01', '22', 'MIRAFLORES');
INSERT INTO ubigeo VALUES ('150123', '15', '01', '23', 'PACHACAMAC');
INSERT INTO ubigeo VALUES ('150124', '15', '01', '24', 'PUCUSANA');
INSERT INTO ubigeo VALUES ('150125', '15', '01', '25', 'PUENTE PIEDRA');
INSERT INTO ubigeo VALUES ('150126', '15', '01', '26', 'PUNTA HERMOSA');
INSERT INTO ubigeo VALUES ('150127', '15', '01', '27', 'PUNTA NEGRA');
INSERT INTO ubigeo VALUES ('150128', '15', '01', '28', 'RIMAC');
INSERT INTO ubigeo VALUES ('150129', '15', '01', '29', 'SAN BARTOLO');
INSERT INTO ubigeo VALUES ('150130', '15', '01', '30', 'SAN BORJA');
INSERT INTO ubigeo VALUES ('150131', '15', '01', '31', 'SAN ISIDRO');
INSERT INTO ubigeo VALUES ('150132', '15', '01', '32', 'SAN JUAN DE LURIGANCHO');
INSERT INTO ubigeo VALUES ('150133', '15', '01', '33', 'SAN JUAN DE MIRAFLORES');
INSERT INTO ubigeo VALUES ('150134', '15', '01', '34', 'SAN LUIS');
INSERT INTO ubigeo VALUES ('150135', '15', '01', '35', 'SAN MARTIN DE PORRES');
INSERT INTO ubigeo VALUES ('150136', '15', '01', '36', 'SAN MIGUEL');
INSERT INTO ubigeo VALUES ('150137', '15', '01', '37', 'SANTA ANITA');
INSERT INTO ubigeo VALUES ('150138', '15', '01', '38', 'SANTA MARIA DEL MAR');
INSERT INTO ubigeo VALUES ('150139', '15', '01', '39', 'SANTA ROSA');
INSERT INTO ubigeo VALUES ('150140', '15', '01', '40', 'SANTIAGO DE SURCO');
INSERT INTO ubigeo VALUES ('150141', '15', '01', '41', 'SURQUILLO');
INSERT INTO ubigeo VALUES ('150142', '15', '01', '42', 'VILLA EL SALVADOR');
INSERT INTO ubigeo VALUES ('150143', '15', '01', '43', 'VILLA MARIA DEL TRIUNFO');
INSERT INTO ubigeo VALUES ('150200', '15', '02', '00', 'BARRANCA');
INSERT INTO ubigeo VALUES ('150201', '15', '02', '01', 'BARRANCA');
INSERT INTO ubigeo VALUES ('150202', '15', '02', '02', 'PARAMONGA');
INSERT INTO ubigeo VALUES ('150203', '15', '02', '03', 'PATIVILCA');
INSERT INTO ubigeo VALUES ('150204', '15', '02', '04', 'SUPE');
INSERT INTO ubigeo VALUES ('150205', '15', '02', '05', 'SUPE PUERTO');
INSERT INTO ubigeo VALUES ('150300', '15', '03', '00', 'CAJATAMBO');
INSERT INTO ubigeo VALUES ('150301', '15', '03', '01', 'CAJATAMBO');
INSERT INTO ubigeo VALUES ('150302', '15', '03', '02', 'COPA');
INSERT INTO ubigeo VALUES ('150303', '15', '03', '03', 'GORGOR');
INSERT INTO ubigeo VALUES ('150304', '15', '03', '04', 'HUANCAPON');
INSERT INTO ubigeo VALUES ('150305', '15', '03', '05', 'MANAS');
INSERT INTO ubigeo VALUES ('150400', '15', '04', '00', 'CANTA');
INSERT INTO ubigeo VALUES ('150401', '15', '04', '01', 'CANTA');
INSERT INTO ubigeo VALUES ('150402', '15', '04', '02', 'ARAHUAY');
INSERT INTO ubigeo VALUES ('150403', '15', '04', '03', 'HUAMANTANGA');
INSERT INTO ubigeo VALUES ('150404', '15', '04', '04', 'HUAROS');
INSERT INTO ubigeo VALUES ('150405', '15', '04', '05', 'LACHAQUI');
INSERT INTO ubigeo VALUES ('150406', '15', '04', '06', 'SAN BUENAVENTURA');
INSERT INTO ubigeo VALUES ('150407', '15', '04', '07', 'SANTA ROSA DE QUIVES');
INSERT INTO ubigeo VALUES ('150500', '15', '05', '00', 'CAÑETE');
INSERT INTO ubigeo VALUES ('150501', '15', '05', '01', 'SAN VICENTE DE CAÑETE');
INSERT INTO ubigeo VALUES ('150502', '15', '05', '02', 'ASIA');
INSERT INTO ubigeo VALUES ('150503', '15', '05', '03', 'CALANGO');
INSERT INTO ubigeo VALUES ('150504', '15', '05', '04', 'CERRO AZUL');
INSERT INTO ubigeo VALUES ('150505', '15', '05', '05', 'CHILCA');
INSERT INTO ubigeo VALUES ('150506', '15', '05', '06', 'COAYLLO');
INSERT INTO ubigeo VALUES ('150507', '15', '05', '07', 'IMPERIAL');
INSERT INTO ubigeo VALUES ('150508', '15', '05', '08', 'LUNAHUANA');
INSERT INTO ubigeo VALUES ('150509', '15', '05', '09', 'MALA');
INSERT INTO ubigeo VALUES ('150510', '15', '05', '10', 'NUEVO IMPERIAL');
INSERT INTO ubigeo VALUES ('150511', '15', '05', '11', 'PACARAN');
INSERT INTO ubigeo VALUES ('150512', '15', '05', '12', 'QUILMANA');
INSERT INTO ubigeo VALUES ('150513', '15', '05', '13', 'SAN ANTONIO');
INSERT INTO ubigeo VALUES ('150514', '15', '05', '14', 'SAN LUIS');
INSERT INTO ubigeo VALUES ('150515', '15', '05', '15', 'SANTA CRUZ DE FLORES');
INSERT INTO ubigeo VALUES ('150516', '15', '05', '16', 'ZUÑIGA');
INSERT INTO ubigeo VALUES ('150600', '15', '06', '00', 'HUARAL');
INSERT INTO ubigeo VALUES ('150601', '15', '06', '01', 'HUARAL');
INSERT INTO ubigeo VALUES ('150602', '15', '06', '02', 'ATAVILLOS ALTO');
INSERT INTO ubigeo VALUES ('150603', '15', '06', '03', 'ATAVILLOS BAJO');
INSERT INTO ubigeo VALUES ('150604', '15', '06', '04', 'AUCALLAMA');
INSERT INTO ubigeo VALUES ('150605', '15', '06', '05', 'CHANCAY');
INSERT INTO ubigeo VALUES ('150606', '15', '06', '06', 'IHUARI');
INSERT INTO ubigeo VALUES ('150607', '15', '06', '07', 'LAMPIAN');
INSERT INTO ubigeo VALUES ('150608', '15', '06', '08', 'PACARAOS');
INSERT INTO ubigeo VALUES ('150609', '15', '06', '09', 'SAN MIGUEL DE ACOS');
INSERT INTO ubigeo VALUES ('150610', '15', '06', '10', 'SANTA CRUZ DE ANDAMARCA');
INSERT INTO ubigeo VALUES ('150611', '15', '06', '11', 'SUMBILCA');
INSERT INTO ubigeo VALUES ('150612', '15', '06', '12', 'VEINTISIETE DE NOVIEMBRE');
INSERT INTO ubigeo VALUES ('150700', '15', '07', '00', 'HUAROCHIRI');
INSERT INTO ubigeo VALUES ('150701', '15', '07', '01', 'MATUCANA');
INSERT INTO ubigeo VALUES ('150702', '15', '07', '02', 'ANTIOQUIA');
INSERT INTO ubigeo VALUES ('150703', '15', '07', '03', 'CALLAHUANCA');
INSERT INTO ubigeo VALUES ('150704', '15', '07', '04', 'CARAMPOMA');
INSERT INTO ubigeo VALUES ('150705', '15', '07', '05', 'CHICLA');
INSERT INTO ubigeo VALUES ('150706', '15', '07', '06', 'CUENCA');
INSERT INTO ubigeo VALUES ('150707', '15', '07', '07', 'HUACHUPAMPA');
INSERT INTO ubigeo VALUES ('150708', '15', '07', '08', 'HUANZA');
INSERT INTO ubigeo VALUES ('150709', '15', '07', '09', 'HUAROCHIRI');
INSERT INTO ubigeo VALUES ('150710', '15', '07', '10', 'LAHUAYTAMBO');
INSERT INTO ubigeo VALUES ('150711', '15', '07', '11', 'LANGA');
INSERT INTO ubigeo VALUES ('150712', '15', '07', '12', 'LARAOS');
INSERT INTO ubigeo VALUES ('150713', '15', '07', '13', 'MARIATANA');
INSERT INTO ubigeo VALUES ('150714', '15', '07', '14', 'RICARDO PALMA');
INSERT INTO ubigeo VALUES ('150715', '15', '07', '15', 'SAN ANDRES DE TUPICOCHA');
INSERT INTO ubigeo VALUES ('150716', '15', '07', '16', 'SAN ANTONIO');
INSERT INTO ubigeo VALUES ('150717', '15', '07', '17', 'SAN BARTOLOME');
INSERT INTO ubigeo VALUES ('150718', '15', '07', '18', 'SAN DAMIAN');
INSERT INTO ubigeo VALUES ('150719', '15', '07', '19', 'SAN JUAN DE IRIS');
INSERT INTO ubigeo VALUES ('150720', '15', '07', '20', 'SAN JUAN DE TANTARANCHE');
INSERT INTO ubigeo VALUES ('150721', '15', '07', '21', 'SAN LORENZO DE QUINTI');
INSERT INTO ubigeo VALUES ('150722', '15', '07', '22', 'SAN MATEO');
INSERT INTO ubigeo VALUES ('150723', '15', '07', '23', 'SAN MATEO DE OTAO');
INSERT INTO ubigeo VALUES ('150724', '15', '07', '24', 'SAN PEDRO DE CASTA');
INSERT INTO ubigeo VALUES ('150725', '15', '07', '25', 'SAN PEDRO DE HUANCAYRE');
INSERT INTO ubigeo VALUES ('150726', '15', '07', '26', 'SANGALLAYA');
INSERT INTO ubigeo VALUES ('150727', '15', '07', '27', 'SANTA CRUZ DE COCACHACRA');
INSERT INTO ubigeo VALUES ('150728', '15', '07', '28', 'SANTA EULALIA');
INSERT INTO ubigeo VALUES ('150729', '15', '07', '29', 'SANTIAGO DE ANCHUCAYA');
INSERT INTO ubigeo VALUES ('150730', '15', '07', '30', 'SANTIAGO DE TUNA');
INSERT INTO ubigeo VALUES ('150731', '15', '07', '31', 'SANTO DOMINGO DE LOS OLLEROS');
INSERT INTO ubigeo VALUES ('150732', '15', '07', '32', 'SURCO');
INSERT INTO ubigeo VALUES ('150800', '15', '08', '00', 'HUAURA');
INSERT INTO ubigeo VALUES ('150801', '15', '08', '01', 'HUACHO');
INSERT INTO ubigeo VALUES ('150802', '15', '08', '02', 'AMBAR');
INSERT INTO ubigeo VALUES ('150803', '15', '08', '03', 'CALETA DE CARQUIN');
INSERT INTO ubigeo VALUES ('150804', '15', '08', '04', 'CHECRAS');
INSERT INTO ubigeo VALUES ('150805', '15', '08', '05', 'HUALMAY');
INSERT INTO ubigeo VALUES ('150806', '15', '08', '06', 'HUAURA');
INSERT INTO ubigeo VALUES ('150807', '15', '08', '07', 'LEONCIO PRADO');
INSERT INTO ubigeo VALUES ('150808', '15', '08', '08', 'PACCHO');
INSERT INTO ubigeo VALUES ('150809', '15', '08', '09', 'SANTA LEONOR');
INSERT INTO ubigeo VALUES ('150810', '15', '08', '10', 'SANTA MARIA');
INSERT INTO ubigeo VALUES ('150811', '15', '08', '11', 'SAYAN');
INSERT INTO ubigeo VALUES ('150812', '15', '08', '12', 'VEGUETA');
INSERT INTO ubigeo VALUES ('150900', '15', '09', '00', 'OYON');
INSERT INTO ubigeo VALUES ('150901', '15', '09', '01', 'OYON');
INSERT INTO ubigeo VALUES ('150902', '15', '09', '02', 'ANDAJES');
INSERT INTO ubigeo VALUES ('150903', '15', '09', '03', 'CAUJUL');
INSERT INTO ubigeo VALUES ('150904', '15', '09', '04', 'COCHAMARCA');
INSERT INTO ubigeo VALUES ('150905', '15', '09', '05', 'NAVAN');
INSERT INTO ubigeo VALUES ('150906', '15', '09', '06', 'PACHANGARA');
INSERT INTO ubigeo VALUES ('151000', '15', '10', '00', 'YAUYOS');
INSERT INTO ubigeo VALUES ('151001', '15', '10', '01', 'YAUYOS');
INSERT INTO ubigeo VALUES ('151002', '15', '10', '02', 'ALIS');
INSERT INTO ubigeo VALUES ('151003', '15', '10', '03', 'ALLAUCA');
INSERT INTO ubigeo VALUES ('151004', '15', '10', '04', 'AYAVIRI');
INSERT INTO ubigeo VALUES ('151005', '15', '10', '05', 'AZANGARO');
INSERT INTO ubigeo VALUES ('151006', '15', '10', '06', 'CACRA');
INSERT INTO ubigeo VALUES ('151007', '15', '10', '07', 'CARANIA');
INSERT INTO ubigeo VALUES ('151008', '15', '10', '08', 'CATAHUASI');
INSERT INTO ubigeo VALUES ('151009', '15', '10', '09', 'CHOCOS');
INSERT INTO ubigeo VALUES ('151010', '15', '10', '10', 'COCHAS');
INSERT INTO ubigeo VALUES ('151011', '15', '10', '11', 'COLONIA');
INSERT INTO ubigeo VALUES ('151012', '15', '10', '12', 'HONGOS');
INSERT INTO ubigeo VALUES ('151013', '15', '10', '13', 'HUAMPARA');
INSERT INTO ubigeo VALUES ('151014', '15', '10', '14', 'HUANCAYA');
INSERT INTO ubigeo VALUES ('151015', '15', '10', '15', 'HUANGASCAR');
INSERT INTO ubigeo VALUES ('151016', '15', '10', '16', 'HUANTAN');
INSERT INTO ubigeo VALUES ('151017', '15', '10', '17', 'HUAÑEC');
INSERT INTO ubigeo VALUES ('151018', '15', '10', '18', 'LARAOS');
INSERT INTO ubigeo VALUES ('151019', '15', '10', '19', 'LINCHA');
INSERT INTO ubigeo VALUES ('151020', '15', '10', '20', 'MADEAN');
INSERT INTO ubigeo VALUES ('151021', '15', '10', '21', 'MIRAFLORES');
INSERT INTO ubigeo VALUES ('151022', '15', '10', '22', 'OMAS');
INSERT INTO ubigeo VALUES ('151023', '15', '10', '23', 'PUTINZA');
INSERT INTO ubigeo VALUES ('151024', '15', '10', '24', 'QUINCHES');
INSERT INTO ubigeo VALUES ('151025', '15', '10', '25', 'QUINOCAY');
INSERT INTO ubigeo VALUES ('151026', '15', '10', '26', 'SAN JOAQUIN');
INSERT INTO ubigeo VALUES ('151027', '15', '10', '27', 'SAN PEDRO DE PILAS');
INSERT INTO ubigeo VALUES ('151028', '15', '10', '28', 'TANTA');
INSERT INTO ubigeo VALUES ('151029', '15', '10', '29', 'TAURIPAMPA');
INSERT INTO ubigeo VALUES ('151030', '15', '10', '30', 'TOMAS');
INSERT INTO ubigeo VALUES ('151031', '15', '10', '31', 'TUPE');
INSERT INTO ubigeo VALUES ('151032', '15', '10', '32', 'VIÑAC');
INSERT INTO ubigeo VALUES ('151033', '15', '10', '33', 'VITIS');
INSERT INTO ubigeo VALUES ('160000', '16', '00', '00', 'LORETO');
INSERT INTO ubigeo VALUES ('160100', '16', '01', '00', 'MAYNAS');
INSERT INTO ubigeo VALUES ('160101', '16', '01', '01', 'IQUITOS');
INSERT INTO ubigeo VALUES ('160102', '16', '01', '02', 'ALTO NANAY');
INSERT INTO ubigeo VALUES ('160103', '16', '01', '03', 'FERNANDO LORES');
INSERT INTO ubigeo VALUES ('160104', '16', '01', '04', 'INDIANA');
INSERT INTO ubigeo VALUES ('160105', '16', '01', '05', 'LAS AMAZONAS');
INSERT INTO ubigeo VALUES ('160106', '16', '01', '06', 'MAZAN');
INSERT INTO ubigeo VALUES ('160107', '16', '01', '07', 'NAPO');
INSERT INTO ubigeo VALUES ('160108', '16', '01', '08', 'PUNCHANA');
INSERT INTO ubigeo VALUES ('160109', '16', '01', '09', 'PUTUMAYO');
INSERT INTO ubigeo VALUES ('160110', '16', '01', '10', 'TORRES CAUSANA');
INSERT INTO ubigeo VALUES ('160112', '16', '01', '12', 'BELEN');
INSERT INTO ubigeo VALUES ('160113', '16', '01', '13', 'SAN JUAN BAUTISTA');
INSERT INTO ubigeo VALUES ('160114', '16', '01', '14', 'TENIENTE MANUEL CLAVERO');
INSERT INTO ubigeo VALUES ('160200', '16', '02', '00', 'ALTO AMAZONAS');
INSERT INTO ubigeo VALUES ('160201', '16', '02', '01', 'YURIMAGUAS');
INSERT INTO ubigeo VALUES ('160202', '16', '02', '02', 'BALSAPUERTO');
INSERT INTO ubigeo VALUES ('160205', '16', '02', '05', 'JEBEROS');
INSERT INTO ubigeo VALUES ('160206', '16', '02', '06', 'LAGUNAS');
INSERT INTO ubigeo VALUES ('160210', '16', '02', '10', 'SANTA CRUZ');
INSERT INTO ubigeo VALUES ('160211', '16', '02', '11', 'TENIENTE CESAR LOPEZ ROJAS');
INSERT INTO ubigeo VALUES ('160300', '16', '03', '00', 'LORETO');
INSERT INTO ubigeo VALUES ('160301', '16', '03', '01', 'NAUTA');
INSERT INTO ubigeo VALUES ('160302', '16', '03', '02', 'PARINARI');
INSERT INTO ubigeo VALUES ('160303', '16', '03', '03', 'TIGRE');
INSERT INTO ubigeo VALUES ('160304', '16', '03', '04', 'TROMPETEROS');
INSERT INTO ubigeo VALUES ('160305', '16', '03', '05', 'URARINAS');
INSERT INTO ubigeo VALUES ('160400', '16', '04', '00', 'MARISCAL RAMON CASTILLA');
INSERT INTO ubigeo VALUES ('160401', '16', '04', '01', 'RAMON CASTILLA');
INSERT INTO ubigeo VALUES ('160402', '16', '04', '02', 'PEBAS');
INSERT INTO ubigeo VALUES ('160403', '16', '04', '03', 'YAVARI');
INSERT INTO ubigeo VALUES ('160404', '16', '04', '04', 'SAN PABLO');
INSERT INTO ubigeo VALUES ('160500', '16', '05', '00', 'REQUENA');
INSERT INTO ubigeo VALUES ('160501', '16', '05', '01', 'REQUENA');
INSERT INTO ubigeo VALUES ('160502', '16', '05', '02', 'ALTO TAPICHE');
INSERT INTO ubigeo VALUES ('160503', '16', '05', '03', 'CAPELO');
INSERT INTO ubigeo VALUES ('160504', '16', '05', '04', 'EMILIO SAN MARTIN');
INSERT INTO ubigeo VALUES ('160505', '16', '05', '05', 'MAQUIA');
INSERT INTO ubigeo VALUES ('160506', '16', '05', '06', 'PUINAHUA');
INSERT INTO ubigeo VALUES ('160507', '16', '05', '07', 'SAQUENA');
INSERT INTO ubigeo VALUES ('160508', '16', '05', '08', 'SOPLIN');
INSERT INTO ubigeo VALUES ('160509', '16', '05', '09', 'TAPICHE');
INSERT INTO ubigeo VALUES ('160510', '16', '05', '10', 'JENARO HERRERA');
INSERT INTO ubigeo VALUES ('160511', '16', '05', '11', 'YAQUERANA');
INSERT INTO ubigeo VALUES ('160600', '16', '06', '00', 'UCAYALI');
INSERT INTO ubigeo VALUES ('160601', '16', '06', '01', 'CONTAMANA');
INSERT INTO ubigeo VALUES ('160602', '16', '06', '02', 'INAHUAYA');
INSERT INTO ubigeo VALUES ('160603', '16', '06', '03', 'PADRE MARQUEZ');
INSERT INTO ubigeo VALUES ('160604', '16', '06', '04', 'PAMPA HERMOSA');
INSERT INTO ubigeo VALUES ('160605', '16', '06', '05', 'SARAYACU');
INSERT INTO ubigeo VALUES ('160606', '16', '06', '06', 'VARGAS GUERRA');
INSERT INTO ubigeo VALUES ('160700', '16', '07', '00', 'DATEM DEL MARAÑON');
INSERT INTO ubigeo VALUES ('160701', '16', '07', '01', 'BARRANCA');
INSERT INTO ubigeo VALUES ('160702', '16', '07', '02', 'CAHUAPANAS');
INSERT INTO ubigeo VALUES ('160703', '16', '07', '03', 'MANSERICHE');
INSERT INTO ubigeo VALUES ('160704', '16', '07', '04', 'MORONA');
INSERT INTO ubigeo VALUES ('160705', '16', '07', '05', 'PASTAZA');
INSERT INTO ubigeo VALUES ('160706', '16', '07', '06', 'ANDOAS');
INSERT INTO ubigeo VALUES ('170000', '17', '00', '00', 'MADRE DE DIOS');
INSERT INTO ubigeo VALUES ('170100', '17', '01', '00', 'TAMBOPATA');
INSERT INTO ubigeo VALUES ('170101', '17', '01', '01', 'TAMBOPATA');
INSERT INTO ubigeo VALUES ('170102', '17', '01', '02', 'INAMBARI');
INSERT INTO ubigeo VALUES ('170103', '17', '01', '03', 'LAS PIEDRAS');
INSERT INTO ubigeo VALUES ('170104', '17', '01', '04', 'LABERINTO');
INSERT INTO ubigeo VALUES ('170200', '17', '02', '00', 'MANU');
INSERT INTO ubigeo VALUES ('170201', '17', '02', '01', 'MANU');
INSERT INTO ubigeo VALUES ('170202', '17', '02', '02', 'FITZCARRALD');
INSERT INTO ubigeo VALUES ('170203', '17', '02', '03', 'MADRE DE DIOS');
INSERT INTO ubigeo VALUES ('170204', '17', '02', '04', 'HUEPETUHE');
INSERT INTO ubigeo VALUES ('170300', '17', '03', '00', 'TAHUAMANU');
INSERT INTO ubigeo VALUES ('170301', '17', '03', '01', 'IÑAPARI');
INSERT INTO ubigeo VALUES ('170302', '17', '03', '02', 'IBERIA');
INSERT INTO ubigeo VALUES ('170303', '17', '03', '03', 'TAHUAMANU');
INSERT INTO ubigeo VALUES ('180000', '18', '00', '00', 'MOQUEGUA');
INSERT INTO ubigeo VALUES ('180100', '18', '01', '00', 'MARISCAL NIETO');
INSERT INTO ubigeo VALUES ('180101', '18', '01', '01', 'MOQUEGUA');
INSERT INTO ubigeo VALUES ('180102', '18', '01', '02', 'CARUMAS');
INSERT INTO ubigeo VALUES ('180103', '18', '01', '03', 'CUCHUMBAYA');
INSERT INTO ubigeo VALUES ('180104', '18', '01', '04', 'SAMEGUA');
INSERT INTO ubigeo VALUES ('180105', '18', '01', '05', 'SAN CRISTOBAL');
INSERT INTO ubigeo VALUES ('180106', '18', '01', '06', 'TORATA');
INSERT INTO ubigeo VALUES ('180200', '18', '02', '00', 'GENERAL SANCHEZ CERRO');
INSERT INTO ubigeo VALUES ('180201', '18', '02', '01', 'OMATE');
INSERT INTO ubigeo VALUES ('180202', '18', '02', '02', 'CHOJATA');
INSERT INTO ubigeo VALUES ('180203', '18', '02', '03', 'COALAQUE');
INSERT INTO ubigeo VALUES ('180204', '18', '02', '04', 'ICHUÑA');
INSERT INTO ubigeo VALUES ('180205', '18', '02', '05', 'LA CAPILLA');
INSERT INTO ubigeo VALUES ('180206', '18', '02', '06', 'LLOQUE');
INSERT INTO ubigeo VALUES ('180207', '18', '02', '07', 'MATALAQUE');
INSERT INTO ubigeo VALUES ('180208', '18', '02', '08', 'PUQUINA');
INSERT INTO ubigeo VALUES ('180209', '18', '02', '09', 'QUINISTAQUILLAS');
INSERT INTO ubigeo VALUES ('180210', '18', '02', '10', 'UBINAS');
INSERT INTO ubigeo VALUES ('180211', '18', '02', '11', 'YUNGA');
INSERT INTO ubigeo VALUES ('180300', '18', '03', '00', 'ILO');
INSERT INTO ubigeo VALUES ('180301', '18', '03', '01', 'ILO');
INSERT INTO ubigeo VALUES ('180302', '18', '03', '02', 'EL ALGARROBAL');
INSERT INTO ubigeo VALUES ('180303', '18', '03', '03', 'PACOCHA');
INSERT INTO ubigeo VALUES ('190000', '19', '00', '00', 'PASCO');
INSERT INTO ubigeo VALUES ('190100', '19', '01', '00', 'PASCO');
INSERT INTO ubigeo VALUES ('190101', '19', '01', '01', 'CHAUPIMARCA');
INSERT INTO ubigeo VALUES ('190102', '19', '01', '02', 'HUACHON');
INSERT INTO ubigeo VALUES ('190103', '19', '01', '03', 'HUARIACA');
INSERT INTO ubigeo VALUES ('190104', '19', '01', '04', 'HUAYLLAY');
INSERT INTO ubigeo VALUES ('190105', '19', '01', '05', 'NINACACA');
INSERT INTO ubigeo VALUES ('190106', '19', '01', '06', 'PALLANCHACRA');
INSERT INTO ubigeo VALUES ('190107', '19', '01', '07', 'PAUCARTAMBO');
INSERT INTO ubigeo VALUES ('190108', '19', '01', '08', 'SAN FRANCISCO DE ASIS DE YARUSYACAN');
INSERT INTO ubigeo VALUES ('190109', '19', '01', '09', 'SIMON BOLIVAR');
INSERT INTO ubigeo VALUES ('190110', '19', '01', '10', 'TICLACAYAN');
INSERT INTO ubigeo VALUES ('190111', '19', '01', '11', 'TINYAHUARCO');
INSERT INTO ubigeo VALUES ('190112', '19', '01', '12', 'VICCO');
INSERT INTO ubigeo VALUES ('190113', '19', '01', '13', 'YANACANCHA');
INSERT INTO ubigeo VALUES ('190200', '19', '02', '00', 'DANIEL ALCIDES CARRION');
INSERT INTO ubigeo VALUES ('190201', '19', '02', '01', 'YANAHUANCA');
INSERT INTO ubigeo VALUES ('190202', '19', '02', '02', 'CHACAYAN');
INSERT INTO ubigeo VALUES ('190203', '19', '02', '03', 'GOYLLARISQUIZGA');
INSERT INTO ubigeo VALUES ('190204', '19', '02', '04', 'PAUCAR');
INSERT INTO ubigeo VALUES ('190205', '19', '02', '05', 'SAN PEDRO DE PILLAO');
INSERT INTO ubigeo VALUES ('190206', '19', '02', '06', 'SANTA ANA DE TUSI');
INSERT INTO ubigeo VALUES ('190207', '19', '02', '07', 'TAPUC');
INSERT INTO ubigeo VALUES ('190208', '19', '02', '08', 'VILCABAMBA');
INSERT INTO ubigeo VALUES ('190300', '19', '03', '00', 'OXAPAMPA');
INSERT INTO ubigeo VALUES ('190301', '19', '03', '01', 'OXAPAMPA');
INSERT INTO ubigeo VALUES ('190302', '19', '03', '02', 'CHONTABAMBA');
INSERT INTO ubigeo VALUES ('190303', '19', '03', '03', 'HUANCABAMBA');
INSERT INTO ubigeo VALUES ('190304', '19', '03', '04', 'PALCAZU');
INSERT INTO ubigeo VALUES ('190305', '19', '03', '05', 'POZUZO');
INSERT INTO ubigeo VALUES ('190306', '19', '03', '06', 'PUERTO BERMUDEZ');
INSERT INTO ubigeo VALUES ('190307', '19', '03', '07', 'VILLA RICA');
INSERT INTO ubigeo VALUES ('190308', '19', '03', '08', 'CONSTITUCION');
INSERT INTO ubigeo VALUES ('200000', '20', '00', '00', 'PIURA');
INSERT INTO ubigeo VALUES ('200100', '20', '01', '00', 'PIURA');
INSERT INTO ubigeo VALUES ('200101', '20', '01', '01', 'PIURA');
INSERT INTO ubigeo VALUES ('200104', '20', '01', '04', 'CASTILLA');
INSERT INTO ubigeo VALUES ('200105', '20', '01', '05', 'CATACAOS');
INSERT INTO ubigeo VALUES ('200107', '20', '01', '07', 'CURA MORI');
INSERT INTO ubigeo VALUES ('200108', '20', '01', '08', 'EL TALLAN');
INSERT INTO ubigeo VALUES ('200109', '20', '01', '09', 'LA ARENA');
INSERT INTO ubigeo VALUES ('200110', '20', '01', '10', 'LA UNION');
INSERT INTO ubigeo VALUES ('200111', '20', '01', '11', 'LAS LOMAS');
INSERT INTO ubigeo VALUES ('200114', '20', '01', '14', 'TAMBO GRANDE');
INSERT INTO ubigeo VALUES ('200200', '20', '02', '00', 'AYABACA');
INSERT INTO ubigeo VALUES ('200201', '20', '02', '01', 'AYABACA');
INSERT INTO ubigeo VALUES ('200202', '20', '02', '02', 'FRIAS');
INSERT INTO ubigeo VALUES ('200203', '20', '02', '03', 'JILILI');
INSERT INTO ubigeo VALUES ('200204', '20', '02', '04', 'LAGUNAS');
INSERT INTO ubigeo VALUES ('200205', '20', '02', '05', 'MONTERO');
INSERT INTO ubigeo VALUES ('200206', '20', '02', '06', 'PACAIPAMPA');
INSERT INTO ubigeo VALUES ('200207', '20', '02', '07', 'PAIMAS');
INSERT INTO ubigeo VALUES ('200208', '20', '02', '08', 'SAPILLICA');
INSERT INTO ubigeo VALUES ('200209', '20', '02', '09', 'SICCHEZ');
INSERT INTO ubigeo VALUES ('200210', '20', '02', '10', 'SUYO');
INSERT INTO ubigeo VALUES ('200300', '20', '03', '00', 'HUANCABAMBA');
INSERT INTO ubigeo VALUES ('200301', '20', '03', '01', 'HUANCABAMBA');
INSERT INTO ubigeo VALUES ('200302', '20', '03', '02', 'CANCHAQUE');
INSERT INTO ubigeo VALUES ('200303', '20', '03', '03', 'EL CARMEN DE LA FRONTERA');
INSERT INTO ubigeo VALUES ('200304', '20', '03', '04', 'HUARMACA');
INSERT INTO ubigeo VALUES ('200305', '20', '03', '05', 'LALAQUIZ');
INSERT INTO ubigeo VALUES ('200306', '20', '03', '06', 'SAN MIGUEL DE EL FAIQUE');
INSERT INTO ubigeo VALUES ('200307', '20', '03', '07', 'SONDOR');
INSERT INTO ubigeo VALUES ('200308', '20', '03', '08', 'SONDORILLO');
INSERT INTO ubigeo VALUES ('200400', '20', '04', '00', 'MORROPON');
INSERT INTO ubigeo VALUES ('200401', '20', '04', '01', 'CHULUCANAS');
INSERT INTO ubigeo VALUES ('200402', '20', '04', '02', 'BUENOS AIRES');
INSERT INTO ubigeo VALUES ('200403', '20', '04', '03', 'CHALACO');
INSERT INTO ubigeo VALUES ('200404', '20', '04', '04', 'LA MATANZA');
INSERT INTO ubigeo VALUES ('200405', '20', '04', '05', 'MORROPON');
INSERT INTO ubigeo VALUES ('200406', '20', '04', '06', 'SALITRAL');
INSERT INTO ubigeo VALUES ('200407', '20', '04', '07', 'SAN JUAN DE BIGOTE');
INSERT INTO ubigeo VALUES ('200408', '20', '04', '08', 'SANTA CATALINA DE MOSSA');
INSERT INTO ubigeo VALUES ('200409', '20', '04', '09', 'SANTO DOMINGO');
INSERT INTO ubigeo VALUES ('200410', '20', '04', '10', 'YAMANGO');
INSERT INTO ubigeo VALUES ('200500', '20', '05', '00', 'PAITA');
INSERT INTO ubigeo VALUES ('200501', '20', '05', '01', 'PAITA');
INSERT INTO ubigeo VALUES ('200502', '20', '05', '02', 'AMOTAPE');
INSERT INTO ubigeo VALUES ('200503', '20', '05', '03', 'ARENAL');
INSERT INTO ubigeo VALUES ('200504', '20', '05', '04', 'COLAN');
INSERT INTO ubigeo VALUES ('200505', '20', '05', '05', 'LA HUACA');
INSERT INTO ubigeo VALUES ('200506', '20', '05', '06', 'TAMARINDO');
INSERT INTO ubigeo VALUES ('200507', '20', '05', '07', 'VICHAYAL');
INSERT INTO ubigeo VALUES ('200600', '20', '06', '00', 'SULLANA');
INSERT INTO ubigeo VALUES ('200601', '20', '06', '01', 'SULLANA');
INSERT INTO ubigeo VALUES ('200602', '20', '06', '02', 'BELLAVISTA');
INSERT INTO ubigeo VALUES ('200603', '20', '06', '03', 'IGNACIO ESCUDERO');
INSERT INTO ubigeo VALUES ('200604', '20', '06', '04', 'LANCONES');
INSERT INTO ubigeo VALUES ('200605', '20', '06', '05', 'MARCAVELICA');
INSERT INTO ubigeo VALUES ('200606', '20', '06', '06', 'MIGUEL CHECA');
INSERT INTO ubigeo VALUES ('200607', '20', '06', '07', 'QUERECOTILLO');
INSERT INTO ubigeo VALUES ('200608', '20', '06', '08', 'SALITRAL');
INSERT INTO ubigeo VALUES ('200700', '20', '07', '00', 'TALARA');
INSERT INTO ubigeo VALUES ('200701', '20', '07', '01', 'PARIÑAS');
INSERT INTO ubigeo VALUES ('200702', '20', '07', '02', 'EL ALTO');
INSERT INTO ubigeo VALUES ('200703', '20', '07', '03', 'LA BREA');
INSERT INTO ubigeo VALUES ('200704', '20', '07', '04', 'LOBITOS');
INSERT INTO ubigeo VALUES ('200705', '20', '07', '05', 'LOS ORGANOS');
INSERT INTO ubigeo VALUES ('200706', '20', '07', '06', 'MANCORA');
INSERT INTO ubigeo VALUES ('200800', '20', '08', '00', 'SECHURA');
INSERT INTO ubigeo VALUES ('200801', '20', '08', '01', 'SECHURA');
INSERT INTO ubigeo VALUES ('200802', '20', '08', '02', 'BELLAVISTA DE LA UNION');
INSERT INTO ubigeo VALUES ('200803', '20', '08', '03', 'BERNAL');
INSERT INTO ubigeo VALUES ('200804', '20', '08', '04', 'CRISTO NOS VALGA');
INSERT INTO ubigeo VALUES ('200805', '20', '08', '05', 'VICE');
INSERT INTO ubigeo VALUES ('200806', '20', '08', '06', 'RINCONADA LLICUAR');
INSERT INTO ubigeo VALUES ('210000', '21', '00', '00', 'PUNO');
INSERT INTO ubigeo VALUES ('210100', '21', '01', '00', 'PUNO');
INSERT INTO ubigeo VALUES ('210101', '21', '01', '01', 'PUNO');
INSERT INTO ubigeo VALUES ('210102', '21', '01', '02', 'ACORA');
INSERT INTO ubigeo VALUES ('210103', '21', '01', '03', 'AMANTANI');
INSERT INTO ubigeo VALUES ('210104', '21', '01', '04', 'ATUNCOLLA');
INSERT INTO ubigeo VALUES ('210105', '21', '01', '05', 'CAPACHICA');
INSERT INTO ubigeo VALUES ('210106', '21', '01', '06', 'CHUCUITO');
INSERT INTO ubigeo VALUES ('210107', '21', '01', '07', 'COATA');
INSERT INTO ubigeo VALUES ('210108', '21', '01', '08', 'HUATA');
INSERT INTO ubigeo VALUES ('210109', '21', '01', '09', 'MAÑAZO');
INSERT INTO ubigeo VALUES ('210110', '21', '01', '10', 'PAUCARCOLLA');
INSERT INTO ubigeo VALUES ('210111', '21', '01', '11', 'PICHACANI');
INSERT INTO ubigeo VALUES ('210112', '21', '01', '12', 'PLATERIA');
INSERT INTO ubigeo VALUES ('210113', '21', '01', '13', 'SAN ANTONIO');
INSERT INTO ubigeo VALUES ('210114', '21', '01', '14', 'TIQUILLACA');
INSERT INTO ubigeo VALUES ('210115', '21', '01', '15', 'VILQUE');
INSERT INTO ubigeo VALUES ('210200', '21', '02', '00', 'AZANGARO');
INSERT INTO ubigeo VALUES ('210201', '21', '02', '01', 'AZANGARO');
INSERT INTO ubigeo VALUES ('210202', '21', '02', '02', 'ACHAYA');
INSERT INTO ubigeo VALUES ('210203', '21', '02', '03', 'ARAPA');
INSERT INTO ubigeo VALUES ('210204', '21', '02', '04', 'ASILLO');
INSERT INTO ubigeo VALUES ('210205', '21', '02', '05', 'CAMINACA');
INSERT INTO ubigeo VALUES ('210206', '21', '02', '06', 'CHUPA');
INSERT INTO ubigeo VALUES ('210207', '21', '02', '07', 'JOSE DOMINGO CHOQUEHUANCA');
INSERT INTO ubigeo VALUES ('210208', '21', '02', '08', 'MUÑANI');
INSERT INTO ubigeo VALUES ('210209', '21', '02', '09', 'POTONI');
INSERT INTO ubigeo VALUES ('210210', '21', '02', '10', 'SAMAN');
INSERT INTO ubigeo VALUES ('210211', '21', '02', '11', 'SAN ANTON');
INSERT INTO ubigeo VALUES ('210212', '21', '02', '12', 'SAN JOSE');
INSERT INTO ubigeo VALUES ('210213', '21', '02', '13', 'SAN JUAN DE SALINAS');
INSERT INTO ubigeo VALUES ('210214', '21', '02', '14', 'SANTIAGO DE PUPUJA');
INSERT INTO ubigeo VALUES ('210215', '21', '02', '15', 'TIRAPATA');
INSERT INTO ubigeo VALUES ('210300', '21', '03', '00', 'CARABAYA');
INSERT INTO ubigeo VALUES ('210301', '21', '03', '01', 'MACUSANI');
INSERT INTO ubigeo VALUES ('210302', '21', '03', '02', 'AJOYANI');
INSERT INTO ubigeo VALUES ('210303', '21', '03', '03', 'AYAPATA');
INSERT INTO ubigeo VALUES ('210304', '21', '03', '04', 'COASA');
INSERT INTO ubigeo VALUES ('210305', '21', '03', '05', 'CORANI');
INSERT INTO ubigeo VALUES ('210306', '21', '03', '06', 'CRUCERO');
INSERT INTO ubigeo VALUES ('210307', '21', '03', '07', 'ITUATA');
INSERT INTO ubigeo VALUES ('210308', '21', '03', '08', 'OLLACHEA');
INSERT INTO ubigeo VALUES ('210309', '21', '03', '09', 'SAN GABAN');
INSERT INTO ubigeo VALUES ('210310', '21', '03', '10', 'USICAYOS');
INSERT INTO ubigeo VALUES ('210400', '21', '04', '00', 'CHUCUITO');
INSERT INTO ubigeo VALUES ('210401', '21', '04', '01', 'JULI');
INSERT INTO ubigeo VALUES ('210402', '21', '04', '02', 'DESAGUADERO');
INSERT INTO ubigeo VALUES ('210403', '21', '04', '03', 'HUACULLANI');
INSERT INTO ubigeo VALUES ('210404', '21', '04', '04', 'KELLUYO');
INSERT INTO ubigeo VALUES ('210405', '21', '04', '05', 'PISACOMA');
INSERT INTO ubigeo VALUES ('210406', '21', '04', '06', 'POMATA');
INSERT INTO ubigeo VALUES ('210407', '21', '04', '07', 'ZEPITA');
INSERT INTO ubigeo VALUES ('210500', '21', '05', '00', 'EL COLLAO');
INSERT INTO ubigeo VALUES ('210501', '21', '05', '01', 'ILAVE');
INSERT INTO ubigeo VALUES ('210502', '21', '05', '02', 'CAPAZO');
INSERT INTO ubigeo VALUES ('210503', '21', '05', '03', 'PILCUYO');
INSERT INTO ubigeo VALUES ('210504', '21', '05', '04', 'SANTA ROSA');
INSERT INTO ubigeo VALUES ('210505', '21', '05', '05', 'CONDURIRI');
INSERT INTO ubigeo VALUES ('210600', '21', '06', '00', 'HUANCANE');
INSERT INTO ubigeo VALUES ('210601', '21', '06', '01', 'HUANCANE');
INSERT INTO ubigeo VALUES ('210602', '21', '06', '02', 'COJATA');
INSERT INTO ubigeo VALUES ('210603', '21', '06', '03', 'HUATASANI');
INSERT INTO ubigeo VALUES ('210604', '21', '06', '04', 'INCHUPALLA');
INSERT INTO ubigeo VALUES ('210605', '21', '06', '05', 'PUSI');
INSERT INTO ubigeo VALUES ('210606', '21', '06', '06', 'ROSASPATA');
INSERT INTO ubigeo VALUES ('210607', '21', '06', '07', 'TARACO');
INSERT INTO ubigeo VALUES ('210608', '21', '06', '08', 'VILQUE CHICO');
INSERT INTO ubigeo VALUES ('210700', '21', '07', '00', 'LAMPA');
INSERT INTO ubigeo VALUES ('210701', '21', '07', '01', 'LAMPA');
INSERT INTO ubigeo VALUES ('210702', '21', '07', '02', 'CABANILLA');
INSERT INTO ubigeo VALUES ('210703', '21', '07', '03', 'CALAPUJA');
INSERT INTO ubigeo VALUES ('210704', '21', '07', '04', 'NICASIO');
INSERT INTO ubigeo VALUES ('210705', '21', '07', '05', 'OCUVIRI');
INSERT INTO ubigeo VALUES ('210706', '21', '07', '06', 'PALCA');
INSERT INTO ubigeo VALUES ('210707', '21', '07', '07', 'PARATIA');
INSERT INTO ubigeo VALUES ('210708', '21', '07', '08', 'PUCARA');
INSERT INTO ubigeo VALUES ('210709', '21', '07', '09', 'SANTA LUCIA');
INSERT INTO ubigeo VALUES ('210710', '21', '07', '10', 'VILAVILA');
INSERT INTO ubigeo VALUES ('210800', '21', '08', '00', 'MELGAR');
INSERT INTO ubigeo VALUES ('210801', '21', '08', '01', 'AYAVIRI');
INSERT INTO ubigeo VALUES ('210802', '21', '08', '02', 'ANTAUTA');
INSERT INTO ubigeo VALUES ('210803', '21', '08', '03', 'CUPI');
INSERT INTO ubigeo VALUES ('210804', '21', '08', '04', 'LLALLI');
INSERT INTO ubigeo VALUES ('210805', '21', '08', '05', 'MACARI');
INSERT INTO ubigeo VALUES ('210806', '21', '08', '06', 'NUÑOA');
INSERT INTO ubigeo VALUES ('210807', '21', '08', '07', 'ORURILLO');
INSERT INTO ubigeo VALUES ('210808', '21', '08', '08', 'SANTA ROSA');
INSERT INTO ubigeo VALUES ('210809', '21', '08', '09', 'UMACHIRI');
INSERT INTO ubigeo VALUES ('210900', '21', '09', '00', 'MOHO');
INSERT INTO ubigeo VALUES ('210901', '21', '09', '01', 'MOHO');
INSERT INTO ubigeo VALUES ('210902', '21', '09', '02', 'CONIMA');
INSERT INTO ubigeo VALUES ('210903', '21', '09', '03', 'HUAYRAPATA');
INSERT INTO ubigeo VALUES ('210904', '21', '09', '04', 'TILALI');
INSERT INTO ubigeo VALUES ('211000', '21', '10', '00', 'SAN ANTONIO DE PUTINA');
INSERT INTO ubigeo VALUES ('211001', '21', '10', '01', 'PUTINA');
INSERT INTO ubigeo VALUES ('211002', '21', '10', '02', 'ANANEA');
INSERT INTO ubigeo VALUES ('211003', '21', '10', '03', 'PEDRO VILCA APAZA');
INSERT INTO ubigeo VALUES ('211004', '21', '10', '04', 'QUILCAPUNCU');
INSERT INTO ubigeo VALUES ('211005', '21', '10', '05', 'SINA');
INSERT INTO ubigeo VALUES ('211100', '21', '11', '00', 'SAN ROMAN');
INSERT INTO ubigeo VALUES ('211101', '21', '11', '01', 'JULIACA');
INSERT INTO ubigeo VALUES ('211102', '21', '11', '02', 'CABANA');
INSERT INTO ubigeo VALUES ('211103', '21', '11', '03', 'CABANILLAS');
INSERT INTO ubigeo VALUES ('211104', '21', '11', '04', 'CARACOTO');
INSERT INTO ubigeo VALUES ('211200', '21', '12', '00', 'SANDIA');
INSERT INTO ubigeo VALUES ('211201', '21', '12', '01', 'SANDIA');
INSERT INTO ubigeo VALUES ('211202', '21', '12', '02', 'CUYOCUYO');
INSERT INTO ubigeo VALUES ('211203', '21', '12', '03', 'LIMBANI');
INSERT INTO ubigeo VALUES ('211204', '21', '12', '04', 'PATAMBUCO');
INSERT INTO ubigeo VALUES ('211205', '21', '12', '05', 'PHARA');
INSERT INTO ubigeo VALUES ('211206', '21', '12', '06', 'QUIACA');
INSERT INTO ubigeo VALUES ('211207', '21', '12', '07', 'SAN JUAN DEL ORO');
INSERT INTO ubigeo VALUES ('211208', '21', '12', '08', 'YANAHUAYA');
INSERT INTO ubigeo VALUES ('211209', '21', '12', '09', 'ALTO INAMBARI');
INSERT INTO ubigeo VALUES ('211210', '21', '12', '10', 'SAN PEDRO DE PUTINA PUNCO');
INSERT INTO ubigeo VALUES ('211300', '21', '13', '00', 'YUNGUYO');
INSERT INTO ubigeo VALUES ('211301', '21', '13', '01', 'YUNGUYO');
INSERT INTO ubigeo VALUES ('211302', '21', '13', '02', 'ANAPIA');
INSERT INTO ubigeo VALUES ('211303', '21', '13', '03', 'COPANI');
INSERT INTO ubigeo VALUES ('211304', '21', '13', '04', 'CUTURAPI');
INSERT INTO ubigeo VALUES ('211305', '21', '13', '05', 'OLLARAYA');
INSERT INTO ubigeo VALUES ('211306', '21', '13', '06', 'TINICACHI');
INSERT INTO ubigeo VALUES ('211307', '21', '13', '07', 'UNICACHI');
INSERT INTO ubigeo VALUES ('220000', '22', '00', '00', 'SAN MARTIN');
INSERT INTO ubigeo VALUES ('220100', '22', '01', '00', 'MOYOBAMBA');
INSERT INTO ubigeo VALUES ('220101', '22', '01', '01', 'MOYOBAMBA');
INSERT INTO ubigeo VALUES ('220102', '22', '01', '02', 'CALZADA');
INSERT INTO ubigeo VALUES ('220103', '22', '01', '03', 'HABANA');
INSERT INTO ubigeo VALUES ('220104', '22', '01', '04', 'JEPELACIO');
INSERT INTO ubigeo VALUES ('220105', '22', '01', '05', 'SORITOR');
INSERT INTO ubigeo VALUES ('220106', '22', '01', '06', 'YANTALO');
INSERT INTO ubigeo VALUES ('220200', '22', '02', '00', 'BELLAVISTA');
INSERT INTO ubigeo VALUES ('220201', '22', '02', '01', 'BELLAVISTA');
INSERT INTO ubigeo VALUES ('220202', '22', '02', '02', 'ALTO BIAVO');
INSERT INTO ubigeo VALUES ('220203', '22', '02', '03', 'BAJO BIAVO');
INSERT INTO ubigeo VALUES ('220204', '22', '02', '04', 'HUALLAGA');
INSERT INTO ubigeo VALUES ('220205', '22', '02', '05', 'SAN PABLO');
INSERT INTO ubigeo VALUES ('220206', '22', '02', '06', 'SAN RAFAEL');
INSERT INTO ubigeo VALUES ('220300', '22', '03', '00', 'EL DORADO');
INSERT INTO ubigeo VALUES ('220301', '22', '03', '01', 'SAN JOSE DE SISA');
INSERT INTO ubigeo VALUES ('220302', '22', '03', '02', 'AGUA BLANCA');
INSERT INTO ubigeo VALUES ('220303', '22', '03', '03', 'SAN MARTIN');
INSERT INTO ubigeo VALUES ('220304', '22', '03', '04', 'SANTA ROSA');
INSERT INTO ubigeo VALUES ('220305', '22', '03', '05', 'SHATOJA');
INSERT INTO ubigeo VALUES ('220400', '22', '04', '00', 'HUALLAGA');
INSERT INTO ubigeo VALUES ('220401', '22', '04', '01', 'SAPOSOA');
INSERT INTO ubigeo VALUES ('220402', '22', '04', '02', 'ALTO SAPOSOA');
INSERT INTO ubigeo VALUES ('220403', '22', '04', '03', 'EL ESLABON');
INSERT INTO ubigeo VALUES ('220404', '22', '04', '04', 'PISCOYACU');
INSERT INTO ubigeo VALUES ('220405', '22', '04', '05', 'SACANCHE');
INSERT INTO ubigeo VALUES ('220406', '22', '04', '06', 'TINGO DE SAPOSOA');
INSERT INTO ubigeo VALUES ('220500', '22', '05', '00', 'LAMAS');
INSERT INTO ubigeo VALUES ('220501', '22', '05', '01', 'LAMAS');
INSERT INTO ubigeo VALUES ('220502', '22', '05', '02', 'ALONSO DE ALVARADO');
INSERT INTO ubigeo VALUES ('220503', '22', '05', '03', 'BARRANQUITA');
INSERT INTO ubigeo VALUES ('220504', '22', '05', '04', 'CAYNARACHI');
INSERT INTO ubigeo VALUES ('220505', '22', '05', '05', 'CUÑUMBUQUI');
INSERT INTO ubigeo VALUES ('220506', '22', '05', '06', 'PINTO RECODO');
INSERT INTO ubigeo VALUES ('220507', '22', '05', '07', 'RUMISAPA');
INSERT INTO ubigeo VALUES ('220508', '22', '05', '08', 'SAN ROQUE DE CUMBAZA');
INSERT INTO ubigeo VALUES ('220509', '22', '05', '09', 'SHANAO');
INSERT INTO ubigeo VALUES ('220510', '22', '05', '10', 'TABALOSOS');
INSERT INTO ubigeo VALUES ('220511', '22', '05', '11', 'ZAPATERO');
INSERT INTO ubigeo VALUES ('220600', '22', '06', '00', 'MARISCAL CACERES');
INSERT INTO ubigeo VALUES ('220601', '22', '06', '01', 'JUANJUI');
INSERT INTO ubigeo VALUES ('220602', '22', '06', '02', 'CAMPANILLA');
INSERT INTO ubigeo VALUES ('220603', '22', '06', '03', 'HUICUNGO');
INSERT INTO ubigeo VALUES ('220604', '22', '06', '04', 'PACHIZA');
INSERT INTO ubigeo VALUES ('220605', '22', '06', '05', 'PAJARILLO');
INSERT INTO ubigeo VALUES ('220700', '22', '07', '00', 'PICOTA');
INSERT INTO ubigeo VALUES ('220701', '22', '07', '01', 'PICOTA');
INSERT INTO ubigeo VALUES ('220702', '22', '07', '02', 'BUENOS AIRES');
INSERT INTO ubigeo VALUES ('220703', '22', '07', '03', 'CASPISAPA');
INSERT INTO ubigeo VALUES ('220704', '22', '07', '04', 'PILLUANA');
INSERT INTO ubigeo VALUES ('220705', '22', '07', '05', 'PUCACACA');
INSERT INTO ubigeo VALUES ('220706', '22', '07', '06', 'SAN CRISTOBAL');
INSERT INTO ubigeo VALUES ('220707', '22', '07', '07', 'SAN HILARION');
INSERT INTO ubigeo VALUES ('220708', '22', '07', '08', 'SHAMBOYACU');
INSERT INTO ubigeo VALUES ('220709', '22', '07', '09', 'TINGO DE PONASA');
INSERT INTO ubigeo VALUES ('220710', '22', '07', '10', 'TRES UNIDOS');
INSERT INTO ubigeo VALUES ('220800', '22', '08', '00', 'RIOJA');
INSERT INTO ubigeo VALUES ('220801', '22', '08', '01', 'RIOJA');
INSERT INTO ubigeo VALUES ('220802', '22', '08', '02', 'AWAJUN');
INSERT INTO ubigeo VALUES ('220803', '22', '08', '03', 'ELIAS SOPLIN VARGAS');
INSERT INTO ubigeo VALUES ('220804', '22', '08', '04', 'NUEVA CAJAMARCA');
INSERT INTO ubigeo VALUES ('220805', '22', '08', '05', 'PARDO MIGUEL');
INSERT INTO ubigeo VALUES ('220806', '22', '08', '06', 'POSIC');
INSERT INTO ubigeo VALUES ('220807', '22', '08', '07', 'SAN FERNANDO');
INSERT INTO ubigeo VALUES ('220808', '22', '08', '08', 'YORONGOS');
INSERT INTO ubigeo VALUES ('220809', '22', '08', '09', 'YURACYACU');
INSERT INTO ubigeo VALUES ('220900', '22', '09', '00', 'SAN MARTIN');
INSERT INTO ubigeo VALUES ('220901', '22', '09', '01', 'TARAPOTO');
INSERT INTO ubigeo VALUES ('220902', '22', '09', '02', 'ALBERTO LEVEAU');
INSERT INTO ubigeo VALUES ('220903', '22', '09', '03', 'CACATACHI');
INSERT INTO ubigeo VALUES ('220904', '22', '09', '04', 'CHAZUTA');
INSERT INTO ubigeo VALUES ('220905', '22', '09', '05', 'CHIPURANA');
INSERT INTO ubigeo VALUES ('220906', '22', '09', '06', 'EL PORVENIR');
INSERT INTO ubigeo VALUES ('220907', '22', '09', '07', 'HUIMBAYOC');
INSERT INTO ubigeo VALUES ('220908', '22', '09', '08', 'JUAN GUERRA');
INSERT INTO ubigeo VALUES ('220909', '22', '09', '09', 'LA BANDA DE SHILCAYO');
INSERT INTO ubigeo VALUES ('220910', '22', '09', '10', 'MORALES');
INSERT INTO ubigeo VALUES ('220911', '22', '09', '11', 'PAPAPLAYA');
INSERT INTO ubigeo VALUES ('220912', '22', '09', '12', 'SAN ANTONIO');
INSERT INTO ubigeo VALUES ('220913', '22', '09', '13', 'SAUCE');
INSERT INTO ubigeo VALUES ('220914', '22', '09', '14', 'SHAPAJA');
INSERT INTO ubigeo VALUES ('221000', '22', '10', '00', 'TOCACHE');
INSERT INTO ubigeo VALUES ('221001', '22', '10', '01', 'TOCACHE');
INSERT INTO ubigeo VALUES ('221002', '22', '10', '02', 'NUEVO PROGRESO');
INSERT INTO ubigeo VALUES ('221003', '22', '10', '03', 'POLVORA');
INSERT INTO ubigeo VALUES ('221004', '22', '10', '04', 'SHUNTE');
INSERT INTO ubigeo VALUES ('221005', '22', '10', '05', 'UCHIZA');
INSERT INTO ubigeo VALUES ('230000', '23', '00', '00', 'TACNA');
INSERT INTO ubigeo VALUES ('230100', '23', '01', '00', 'TACNA');
INSERT INTO ubigeo VALUES ('230101', '23', '01', '01', 'TACNA');
INSERT INTO ubigeo VALUES ('230102', '23', '01', '02', 'ALTO DE LA ALIANZA');
INSERT INTO ubigeo VALUES ('230103', '23', '01', '03', 'CALANA');
INSERT INTO ubigeo VALUES ('230104', '23', '01', '04', 'CIUDAD NUEVA');
INSERT INTO ubigeo VALUES ('230105', '23', '01', '05', 'INCLAN');
INSERT INTO ubigeo VALUES ('230106', '23', '01', '06', 'PACHIA');
INSERT INTO ubigeo VALUES ('230107', '23', '01', '07', 'PALCA');
INSERT INTO ubigeo VALUES ('230108', '23', '01', '08', 'POCOLLAY');
INSERT INTO ubigeo VALUES ('230109', '23', '01', '09', 'SAMA');
INSERT INTO ubigeo VALUES ('230110', '23', '01', '10', 'CORONEL GREGORIO ALBARRACIN LANCHIPA');
INSERT INTO ubigeo VALUES ('230200', '23', '02', '00', 'CANDARAVE');
INSERT INTO ubigeo VALUES ('230201', '23', '02', '01', 'CANDARAVE');
INSERT INTO ubigeo VALUES ('230202', '23', '02', '02', 'CAIRANI');
INSERT INTO ubigeo VALUES ('230203', '23', '02', '03', 'CAMILACA');
INSERT INTO ubigeo VALUES ('230204', '23', '02', '04', 'CURIBAYA');
INSERT INTO ubigeo VALUES ('230205', '23', '02', '05', 'HUANUARA');
INSERT INTO ubigeo VALUES ('230206', '23', '02', '06', 'QUILAHUANI');
INSERT INTO ubigeo VALUES ('230300', '23', '03', '00', 'JORGE BASADRE');
INSERT INTO ubigeo VALUES ('230301', '23', '03', '01', 'LOCUMBA');
INSERT INTO ubigeo VALUES ('230302', '23', '03', '02', 'ILABAYA');
INSERT INTO ubigeo VALUES ('230303', '23', '03', '03', 'ITE');
INSERT INTO ubigeo VALUES ('230400', '23', '04', '00', 'TARATA');
INSERT INTO ubigeo VALUES ('230401', '23', '04', '01', 'TARATA');
INSERT INTO ubigeo VALUES ('230402', '23', '04', '02', 'HEROES ALBARRACIN');
INSERT INTO ubigeo VALUES ('230403', '23', '04', '03', 'ESTIQUE');
INSERT INTO ubigeo VALUES ('230404', '23', '04', '04', 'ESTIQUE-PAMPA');
INSERT INTO ubigeo VALUES ('230405', '23', '04', '05', 'SITAJARA');
INSERT INTO ubigeo VALUES ('230406', '23', '04', '06', 'SUSAPAYA');
INSERT INTO ubigeo VALUES ('230407', '23', '04', '07', 'TARUCACHI');
INSERT INTO ubigeo VALUES ('230408', '23', '04', '08', 'TICACO');
INSERT INTO ubigeo VALUES ('240000', '24', '00', '00', 'TUMBES');
INSERT INTO ubigeo VALUES ('240100', '24', '01', '00', 'TUMBES');
INSERT INTO ubigeo VALUES ('240101', '24', '01', '01', 'TUMBES');
INSERT INTO ubigeo VALUES ('240102', '24', '01', '02', 'CORRALES');
INSERT INTO ubigeo VALUES ('240103', '24', '01', '03', 'LA CRUZ');
INSERT INTO ubigeo VALUES ('240104', '24', '01', '04', 'PAMPAS DE HOSPITAL');
INSERT INTO ubigeo VALUES ('240105', '24', '01', '05', 'SAN JACINTO');
INSERT INTO ubigeo VALUES ('240106', '24', '01', '06', 'SAN JUAN DE LA VIRGEN');
INSERT INTO ubigeo VALUES ('240200', '24', '02', '00', 'CONTRALMIRANTE VILLAR');
INSERT INTO ubigeo VALUES ('240201', '24', '02', '01', 'ZORRITOS');
INSERT INTO ubigeo VALUES ('240202', '24', '02', '02', 'CASITAS');
INSERT INTO ubigeo VALUES ('240203', '24', '02', '03', 'CANOAS DE PUNTA SAL');
INSERT INTO ubigeo VALUES ('240300', '24', '03', '00', 'ZARUMILLA');
INSERT INTO ubigeo VALUES ('240301', '24', '03', '01', 'ZARUMILLA');
INSERT INTO ubigeo VALUES ('240302', '24', '03', '02', 'AGUAS VERDES');
INSERT INTO ubigeo VALUES ('240303', '24', '03', '03', 'MATAPALO');
INSERT INTO ubigeo VALUES ('240304', '24', '03', '04', 'PAPAYAL');
INSERT INTO ubigeo VALUES ('250000', '25', '00', '00', 'UCAYALI');
INSERT INTO ubigeo VALUES ('250100', '25', '01', '00', 'CORONEL PORTILLO');
INSERT INTO ubigeo VALUES ('250101', '25', '01', '01', 'CALLERIA');
INSERT INTO ubigeo VALUES ('250102', '25', '01', '02', 'CAMPOVERDE');
INSERT INTO ubigeo VALUES ('250103', '25', '01', '03', 'IPARIA');
INSERT INTO ubigeo VALUES ('250104', '25', '01', '04', 'MASISEA');
INSERT INTO ubigeo VALUES ('250105', '25', '01', '05', 'YARINACOCHA');
INSERT INTO ubigeo VALUES ('250106', '25', '01', '06', 'NUEVA REQUENA');
INSERT INTO ubigeo VALUES ('250107', '25', '01', '07', 'MANANTAY');
INSERT INTO ubigeo VALUES ('250200', '25', '02', '00', 'ATALAYA');
INSERT INTO ubigeo VALUES ('250201', '25', '02', '01', 'RAYMONDI');
INSERT INTO ubigeo VALUES ('250202', '25', '02', '02', 'SEPAHUA');
INSERT INTO ubigeo VALUES ('250203', '25', '02', '03', 'TAHUANIA');
INSERT INTO ubigeo VALUES ('250204', '25', '02', '04', 'YURUA');
INSERT INTO ubigeo VALUES ('250300', '25', '03', '00', 'PADRE ABAD');
INSERT INTO ubigeo VALUES ('250301', '25', '03', '01', 'PADRE ABAD');
INSERT INTO ubigeo VALUES ('250302', '25', '03', '02', 'IRAZOLA');
INSERT INTO ubigeo VALUES ('250303', '25', '03', '03', 'CURIMANA');
INSERT INTO ubigeo VALUES ('250400', '25', '04', '00', 'PURUS');
INSERT INTO ubigeo VALUES ('250401', '25', '04', '01', 'PURUS');


------------------------------------------------------------------------------------------------------------
-- ESQUEMA SEGURIDAD========================================================================================
--**********************************************************************************************************

SET search_path = seguridad, pg_catalog;

CREATE TABLE rol (
    id integer NOT NULL,
    nombre character varying(30)
);

CREATE TABLE usuario (
    id integer DEFAULT 0 NOT NULL,
    usuario character varying(15) NOT NULL,
    credencial character varying(15) NOT NULL,
    id_rol integer,
    nombres character varying(50),
    apepaterno character varying(20),
    apematerno character varying(20)
);

COMMENT ON COLUMN usuario.id IS 'identificador de usuario';
COMMENT ON COLUMN usuario.usuario IS 'usuario de inicio de sesion';


CREATE VIEW vw_listarusuarios AS
    SELECT u.id, u.usuario, u.credencial, u.id_rol, r.nombre, u.nombres, u.apepaterno, u.apematerno FROM usuario u, rol r WHERE (u.id_rol = r.id);

ALTER TABLE ONLY rol
    ADD CONSTRAINT pk_rol PRIMARY KEY (id);

ALTER TABLE ONLY usuario
    ADD CONSTRAINT pk_usuario PRIMARY KEY (id);

ALTER TABLE ONLY usuario
    ADD CONSTRAINT fk_usuario_rol FOREIGN KEY (id_rol) REFERENCES rol(id);


CREATE FUNCTION fn_actualizarusuario(p_id integer, p_rol integer, p_nombres character varying, p_apepaterno character varying, p_apematerno character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

begin

update seguridad.usuario
   set id_rol	  = p_rol,
       nombres    = p_nombres,
       apepaterno = p_apepaterno,
       apematerno = p_apematerno
 where id 	  = p_id;

return true;

end;
$$;


CREATE FUNCTION fn_ingresarusuario(p_usuario character varying, p_credencial character varying, p_rol integer, p_nombres character varying, p_apepaterno character varying, p_apematerno character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

declare maxusuario integer;

begin


select max(id)
  into maxusuario
  from seguridad.usuario;

maxusuario = maxusuario + 1;

insert into seguridad.usuario(id,usuario,credencial,id_rol,nombres,apepaterno,apematerno)
values (maxusuario,p_usuario,p_credencial,p_rol,p_nombres,p_apepaterno,p_apematerno);


return true;

exception
when others then
  return false;

end;
$$;

CREATE FUNCTION fn_iniciosesion(p_usuario character varying, p_credencial character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

declare cantidad integer;

begin

cantidad = 0;

select count(1)
  into cantidad
  from seguridad.usuario usr
 where usr.usuario    = p_usuario
   and usr.credencial = p_credencial;

if cantidad = 1 then
   return true;
else
   return false;
end if;

exception
when others then
  return false;

end;
$$;


CREATE FUNCTION fn_listarusuarios() RETURNS void
    LANGUAGE plpgsql
    AS $$

begin

select * from seguridad.usuario;

end;$$;



INSERT INTO rol VALUES (1, 'admin');


--
-- TOC entry 2212 (class 0 OID 16393)
-- Dependencies: 172
-- Data for Name: usuario; Type: TABLE DATA; Schema: seguridad; Owner: postgres
--

INSERT INTO usuario VALUES (1, 'admin', 'clave1', 1, 'admin', 'admin', 'admin');
INSERT INTO usuario VALUES (2, 'paola', 'paolah', 1, 'PAOLA FIORELLA', 'HUARACHI', 'PFLUCKER');
INSERT INTO usuario VALUES (3, 'EDWIN', '12345', 1, 'EDWIN', 'REBAZA', 'CERPA');
INSERT INTO usuario VALUES (4, 'CELIA', '12345', 1, 'CELIA', 'CERPA', 'BARRIGA');

------------------------------------------------------------------------------------------------------------
-- ESQUEMA NEGOCIO =========================================================================================
--**********************************************************************************************************	
	
	
SET search_path = negocio, pg_catalog;

CREATE TABLE "CorreoElectronico" (
    id integer NOT NULL,
    correo character varying(40) NOT NULL,
    idpersona integer NOT NULL,
    usuariocreacion character varying(15),
    fechacreacion timestamp with time zone,
    ipcreacion character varying(15),
    usuariomodificacion character varying(15),
    fechamodificacion timestamp with time zone,
    ipmodificacion character varying(15),
    idestadoregistro integer DEFAULT 1 NOT NULL
);

CREATE TABLE "Direccion" (
    id integer NOT NULL,
    idvia integer NOT NULL,
    nombrevia character varying(50),
    numero character varying(10),
    interior character varying(10),
    manzana character varying(10),
    lote character varying(10),
    principal character varying(1),
    idubigeo character(6),
    usuariocreacion character varying(50),
    fechacreacion timestamp with time zone,
    ipcreacion character varying(50),
    usuariomodificacion character varying(50),
    fechamodificacion timestamp with time zone,
    ipmodificacion character varying(50),
    idestadoregistro integer DEFAULT 1 NOT NULL,
    observacion character varying(300),
    referencia character varying(300)
);

CREATE TABLE "Persona" (
    id bigint NOT NULL,
    idtipopersona integer NOT NULL,
    nombres character varying(100),
    apellidopaterno character varying(50),
    apellidomaterno character varying(50),
    idgenero character varying(1),
    idestadocivil integer,
    idtipodocumento integer,
    numerodocumento character varying(15),
    usuariocreacion character varying(20) NOT NULL,
    fechacreacion timestamp with time zone NOT NULL,
    ipcreacion character(15) NOT NULL,
    usuariomodificacion character varying(15) NOT NULL,
    fechamodificacion timestamp with time zone NOT NULL,
    ipmodificacion character(15) NOT NULL,
    idestadoregistro integer DEFAULT 1 NOT NULL,
    fecnacimiento date
);


CREATE TABLE "PersonaAdicional" (
    idpersona integer NOT NULL,
    idrubro integer,
    idestadoregistro integer DEFAULT 1 NOT NULL
);


CREATE TABLE "PersonaContactoProveedor" (
    idproveedor integer NOT NULL,
    idcontacto integer NOT NULL,
    idarea integer,
    anexo character varying(5),
    idestadoregistro integer DEFAULT 1 NOT NULL
);


CREATE TABLE "PersonaDireccion" (
    idpersona bigint NOT NULL,
    iddireccion integer NOT NULL,
    idtipopersona integer NOT NULL,
    idestadoregistro integer DEFAULT 1 NOT NULL
);


CREATE TABLE "ServicioNovios" (
    id integer NOT NULL,
    codigonovios character varying(5) NOT NULL,
    idnovia integer NOT NULL,
    idnovio integer NOT NULL,
    iddestino integer NOT NULL,
    fechaboda date NOT NULL,
    idmoneda integer NOT NULL,
    cuotainicial double precision NOT NULL,
    dias integer NOT NULL,
    noches integer NOT NULL,
    fechashower date,
    observaciones text,
    usuariocreacion character varying(15),
    fechacreacion timestamp with time zone,
    ipcreacion character varying(15),
    usuariomodificacion character varying(15),
    fechamodificacion timestamp with time zone,
    ipmodificacion character varying(15),
    idestadoregistro integer DEFAULT 1 NOT NULL
);

CREATE TABLE "Telefono" (
    id integer NOT NULL,
    numero character varying(9) NOT NULL,
    idempresaproveedor integer NOT NULL,
    usuariocreacion character varying(15),
    fechacreacion timestamp with time zone,
    ipcreacion character varying(15),
    usuariomodificacion character varying(15),
    fechamodificacion timestamp with time zone,
    ipmodificacion character varying(15),
    idestadoregistro integer DEFAULT 1 NOT NULL
);


CREATE TABLE "TelefonoDireccion" (
    idtelefono integer NOT NULL,
    iddireccion integer NOT NULL,
    idestadoregistro integer DEFAULT 1 NOT NULL
);


CREATE TABLE "TelefonoPersona" (
    idtelefono integer NOT NULL,
    idpersona integer NOT NULL,
    idestadoregistro integer DEFAULT 1 NOT NULL
);


CREATE SEQUENCE seq_correoelectronico
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_direccion
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_novios
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_persona
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_telefono
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE VIEW vw_consultacontacto AS
    SELECT pro.id, pro.nombres, pro.apellidopaterno, pro.apellidomaterno, pro.idgenero, pro.idestadocivil, pro.idtipodocumento, pro.numerodocumento, pro.usuariocreacion, pro.fechacreacion, pro.ipcreacion FROM "Persona" pro WHERE ((pro.idtipopersona = 3) AND (pro.idestadoregistro = 1));

CREATE VIEW vw_consultacorreocontacto AS
    SELECT cor.id, cor.correo, cor.idpersona, cor.usuariocreacion, cor.fechacreacion, cor.ipcreacion, cor.usuariomodificacion, cor.fechamodificacion, cor.ipmodificacion FROM "CorreoElectronico" cor WHERE (cor.idestadoregistro = 1);
	
CREATE VIEW vw_consultadireccionproveedor AS
    SELECT dir.id, dir.idvia, dir.nombrevia, dir.numero, dir.interior, dir.manzana, dir.lote, dir.principal, dir.idubigeo, dir.usuariocreacion, dir.fechacreacion, dir.ipcreacion, dep.iddepartamento, dep.descripcion AS departamento, pro.idprovincia, pro.descripcion AS provincia, dis.iddistrito, dis.descripcion AS distrito, pdir.idpersona, dir.observacion, dir.referencia FROM "Direccion" dir, "PersonaDireccion" pdir, soporte.ubigeo dep, soporte.ubigeo pro, soporte.ubigeo dis WHERE ((((((((((((dir.idestadoregistro = 1) AND (pdir.idestadoregistro = 1)) AND (dir.id = pdir.iddireccion)) AND (("substring"((dir.idubigeo)::text, 1, 2) || '0000'::text) = (dep.id)::text)) AND ((dep.iddepartamento)::text <> '00'::text)) AND ((dep.idprovincia)::text = '00'::text)) AND ((dep.iddistrito)::text = '00'::text)) AND (("substring"((dir.idubigeo)::text, 1, 4) || '00'::text) = (pro.id)::text)) AND ((pro.iddepartamento)::text <> '00'::text)) AND ((pro.idprovincia)::text <> '00'::text)) AND ((pro.iddistrito)::text = '00'::text)) AND ((dis.id)::bpchar = dir.idubigeo));

CREATE VIEW vw_consultaproveedor AS
    SELECT pro.id, pro.nombres, pro.apellidopaterno, pro.apellidomaterno, pro.idgenero, pro.idestadocivil, pro.idtipodocumento, pro.numerodocumento, pro.usuariocreacion, pro.fechacreacion, pro.ipcreacion, ppro.idrubro FROM "Persona" pro, "PersonaAdicional" ppro WHERE ((((pro.idestadoregistro = 1) AND (ppro.idestadoregistro = 1)) AND (pro.idtipopersona = 2)) AND (pro.id = ppro.idpersona));
	
CREATE VIEW vw_contactoproveedor AS
    SELECT con.id, con.nombres, con.apellidopaterno, con.apellidomaterno, con.idgenero, con.idestadocivil, con.idtipodocumento, con.numerodocumento, con.usuariocreacion, con.fechacreacion, con.ipcreacion, pcpro.idproveedor, pcpro.idarea, area.nombre, pcpro.anexo FROM "Persona" con, ("PersonaContactoProveedor" pcpro LEFT JOIN soporte."Tablamaestra" area ON ((((pcpro.idarea = area.id) AND (area.estado = 'A'::bpchar)) AND (area.idmaestro = 4)))) WHERE ((((con.idestadoregistro = 1) AND (pcpro.idestadoregistro = 1)) AND (con.idtipopersona = 3)) AND (con.id = pcpro.idcontacto));

CREATE VIEW vw_proveedor AS
    SELECT pro.id AS idproveedor, tdoc.id AS idtipodocumento, tdoc.nombre AS nombretipodocumento, pro.numerodocumento, pro.nombres, pro.apellidopaterno, pro.apellidomaterno, ppro.idrubro, trub.nombre AS nombrerubro, dir.idvia, tvia.nombre AS nombretipovia, dir.nombrevia, dir.numero, dir.interior, dir.manzana, dir.lote, (SELECT tel.numero FROM "TelefonoDireccion" tedir, "Telefono" tel WHERE ((((tedir.idestadoregistro = 1) AND (tel.idestadoregistro = 1)) AND (tedir.iddireccion = dir.id)) AND (tedir.idtelefono = tel.id)) LIMIT 1) AS teledireccion FROM "Persona" pro, soporte."Tablamaestra" tdoc, "PersonaAdicional" ppro, soporte."Tablamaestra" trub, (("PersonaDireccion" pdir LEFT JOIN "Direccion" dir ON (((pdir.iddireccion = dir.id) AND ((dir.principal)::text = 'S'::text)))) LEFT JOIN soporte."Tablamaestra" tvia ON (((tvia.idmaestro = 2) AND (dir.idvia = tvia.id)))) WHERE (((((((((((pro.idestadoregistro = 1) AND (pro.idtipopersona = 2)) AND (tdoc.idmaestro = 1)) AND (pro.idtipodocumento = tdoc.id)) AND (pro.id = ppro.idpersona)) AND (trub.idmaestro = 3)) AND (ppro.idestadoregistro = 1)) AND (ppro.idrubro = trub.id)) AND (dir.idestadoregistro = 1)) AND (pdir.idestadoregistro = 1)) AND (pro.id = pdir.idpersona));

CREATE VIEW vw_telefonocontacto AS
    SELECT tel.id, tel.numero, tel.idempresaproveedor, tper.idpersona FROM "Telefono" tel, "TelefonoPersona" tper, "Persona" per WHERE ((((((tel.idestadoregistro = 1) AND (tper.idestadoregistro = 1)) AND (tel.id = tper.idtelefono)) AND (per.idestadoregistro = 1)) AND (tper.idpersona = per.id)) AND (per.idtipopersona = 3));

CREATE VIEW vw_telefonodireccion AS
    SELECT tel.id, tel.numero, tel.idempresaproveedor, teldir.iddireccion FROM "Telefono" tel, "TelefonoDireccion" teldir WHERE (((tel.idestadoregistro = 1) AND (teldir.idestadoregistro = 1)) AND (tel.id = teldir.idtelefono));

ALTER TABLE ONLY "Persona"
    ADD CONSTRAINT cons_uniq_idpersona UNIQUE (id);


ALTER TABLE ONLY "CorreoElectronico"
    ADD CONSTRAINT pk_correoelectronico PRIMARY KEY (id);

ALTER TABLE ONLY "Direccion"
    ADD CONSTRAINT pk_direccion PRIMARY KEY (id);

ALTER TABLE ONLY "Persona"
    ADD CONSTRAINT pk_persona PRIMARY KEY (id, idtipopersona);

ALTER TABLE ONLY "PersonaContactoProveedor"
    ADD CONSTRAINT pk_personacontactoproveedor PRIMARY KEY (idproveedor, idcontacto);

ALTER TABLE ONLY "PersonaDireccion"
    ADD CONSTRAINT pk_personadireccion PRIMARY KEY (idpersona, iddireccion, idtipopersona);

ALTER TABLE ONLY "PersonaAdicional"
    ADD CONSTRAINT pk_personaproveedor PRIMARY KEY (idpersona);

ALTER TABLE ONLY "ServicioNovios"
    ADD CONSTRAINT pk_servicionovios PRIMARY KEY (id);

ALTER TABLE ONLY "Telefono"
    ADD CONSTRAINT pk_telefono PRIMARY KEY (id);

ALTER TABLE ONLY "TelefonoDireccion"
    ADD CONSTRAINT pk_telefonodireccion PRIMARY KEY (idtelefono, iddireccion);

ALTER TABLE ONLY "TelefonoPersona"
    ADD CONSTRAINT pk_telefonopersona PRIMARY KEY (idtelefono, idpersona);


ALTER TABLE ONLY "PersonaContactoProveedor"
    ADD CONSTRAINT fk_contacto FOREIGN KEY (idcontacto) REFERENCES "Persona"(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "CorreoElectronico"
    ADD CONSTRAINT fk_correopersona FOREIGN KEY (idpersona) REFERENCES "Persona"(id);

ALTER TABLE ONLY "PersonaDireccion"
    ADD CONSTRAINT fk_direccion FOREIGN KEY (iddireccion) REFERENCES "Direccion"(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "PersonaDireccion"
    ADD CONSTRAINT fk_persona FOREIGN KEY (idpersona) REFERENCES "Persona"(id);

ALTER TABLE ONLY "PersonaAdicional"
    ADD CONSTRAINT fk_personaproveedorpersona FOREIGN KEY (idpersona) REFERENCES "Persona"(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY "PersonaContactoProveedor"
    ADD CONSTRAINT fk_proveedor FOREIGN KEY (idproveedor) REFERENCES "Persona"(id) ON UPDATE CASCADE ON DELETE CASCADE;


CREATE FUNCTION fn_actualizarcontactoproveedor(p_idproveedor integer, p_idcontacto integer, p_idarea integer, p_anexo character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
BEGIN

UPDATE 
  negocio."PersonaContactoProveedor" 
SET 
  idarea               = p_idarea,
  anexo                = p_anexo
WHERE idestadoregistro = 1
  AND idproveedor      = p_idproveedor
  AND idcontacto       = p_idcontacto;

return true;
END;
$$;

CREATE FUNCTION fn_actualizarcorreoelectronico(p_id integer, p_correo character varying, p_usuariomodificacion character varying, p_ipmodificacion character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$

declare fechahoy timestamp with time zone;

begin

select current_timestamp into fechahoy;

UPDATE negocio."CorreoElectronico"
   SET correo              = p_correo, 
       usuariomodificacion = p_usuariomodificacion, 
       fechamodificacion   = fechahoy, 
       ipmodificacion      = p_ipmodificacion
 WHERE idestadoregistro    = 1
   AND id                  = p_id;


end;
$$;


CREATE FUNCTION fn_actualizardireccion(p_id integer, p_idvia integer, p_nombrevia character varying, p_numero character varying, p_interior character varying, p_manzana character varying, p_lote character varying, p_principal character varying, p_idubigeo character, p_usuariomodificacion character varying, p_ipmodificacion character varying, p_observacion character varying, p_referencia character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$
declare 

fechahoy    timestamp with time zone;
iddireccion integer = 0;
cantidad    integer    = 0;

begin

select current_timestamp into fechahoy;

select count(1)
  into cantidad
  from negocio."Direccion"
 where id               = p_id
   and idestadoregistro = 1;

if cantidad = 1 then
iddireccion           = p_id;
UPDATE 
  negocio."Direccion" 
SET 
  idvia                = p_idvia,
  nombrevia            = p_nombrevia,
  numero               = p_numero,
  interior             = p_interior,
  manzana              = p_manzana,
  lote                 = p_lote,
  principal            = p_principal,
  idubigeo             = p_idubigeo,
  observacion          = p_observacion,
  referencia           = p_referencia,
  usuariomodificacion  = p_usuariomodificacion,
  fechamodificacion    = fechahoy,
  ipmodificacion       = p_ipmodificacion
WHERE idestadoregistro = 1
  AND id               = iddireccion;

elsif cantidad = 0 then
select 
negocio.fn_ingresardireccion(p_idvia, p_nombrevia, p_numero, p_interior, p_manzana, p_lote, p_principal, p_idubigeo, p_usuariomodificacion, p_ipmodificacion, 
p_observacion, p_referencia)
into iddireccion;

end if;

return iddireccion;

end;
$$;


CREATE FUNCTION fn_actualizarpersona(p_id integer, p_idtipopersona integer, p_nombres character varying, p_apepaterno character varying, p_apematerno character varying, p_idgenero character varying, p_idestadocivil integer, p_idtipodocumento integer, p_numerodocumento character varying, p_usuariomodificacion character varying, p_ipmodificacion character varying, p_fecnacimiento date) RETURNS integer
    LANGUAGE plpgsql
    AS $$

declare 

fechahoy timestamp with time zone;
cantidad integer;
idpersona integer;

begin

select current_timestamp into fechahoy;

select count(1)
  into cantidad
  from negocio."Persona"
 where id               = p_id
   AND idtipopersona    = p_idtipopersona;

if cantidad = 1 then
idpersona                  = p_id;
UPDATE negocio."Persona"
   SET nombres             = p_nombres, 
       apellidopaterno     = p_apepaterno, 
       apellidomaterno     = p_apematerno, 
       idgenero            = p_idgenero, 
       idestadocivil       = p_idestadocivil, 
       idtipodocumento     = p_idtipodocumento, 
       numerodocumento     = p_numerodocumento, 
       usuariomodificacion = p_usuariomodificacion, 
       fechamodificacion   = fechahoy, 
       ipmodificacion      = p_ipmodificacion,
       fecnacimiento       = p_fecnacimiento,
       idestadoregistro    = 1
 WHERE id                  = idpersona
   AND idtipopersona       = p_idtipopersona;

elsif cantidad = 0 then

select negocio.fn_ingresarpersona(p_idtipopersona, p_nombres, p_apepaterno, p_apematerno, p_idgenero, p_idestadocivil, p_idtipodocumento, 
p_numerodocumento, p_usuariomodificacion, p_ipmodificacion) into idpersona;

end if;

return idpersona;

 end;
$$;


CREATE FUNCTION fn_actualizarpersonaproveedor(p_idpersona integer, p_idrubro integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
BEGIN
  UPDATE 
  negocio."PersonaAdicional" 
SET 
  idrubro   = p_idrubro
WHERE idestadoregistro = 1
  AND idpersona = p_idpersona;
  
  return true;
END;
$$;


CREATE FUNCTION fn_consultarclientesnovios(p_genero character varying) RETURNS refcursor
    LANGUAGE plpgsql
    AS $$
declare micursor refcursor;

begin

open micursor for
SELECT pro.id AS idpersona, tdoc.id AS idtipodocumento, 
       tdoc.nombre AS nombretipodocumento, pro.numerodocumento, pro.nombres, 
       pro.apellidopaterno, pro.apellidomaterno
   FROM negocio."Persona" pro, 
        soporte."Tablamaestra" tdoc
  WHERE pro.idestadoregistro  = 1 
    AND pro.idtipopersona     = 1
    AND tdoc.idmaestro        = 1 
    AND pro.idtipodocumento   = tdoc.id
    AND pro.idgenero          = p_genero;


return micursor;

end;
$$;


CREATE FUNCTION fn_consultarpersona(p_id integer, p_idtipopersona integer) RETURNS refcursor
    LANGUAGE plpgsql
    AS $$
declare micursor refcursor;

begin

open micursor for
SELECT pro.id, pro.nombres, pro.apellidopaterno, pro.apellidomaterno, 
    pro.idgenero, pro.idestadocivil, pro.idtipodocumento, pro.numerodocumento, 
    pro.usuariocreacion, pro.fechacreacion, pro.ipcreacion, ppro.idrubro, pro.fecnacimiento
   FROM negocio."Persona" pro
   left join negocio."PersonaAdicional" ppro on ppro.idpersona = pro.id AND ppro.idestadoregistro = 1
  WHERE pro.idestadoregistro = 1 
    AND pro.idtipopersona = p_idtipopersona
    AND pro.id = p_id;

return micursor;

end;
$$;

CREATE FUNCTION fn_consultarpersonas(p_idtipopersona integer, p_idtipodocumento integer, p_numerodocumento character varying, p_nombres character varying) RETURNS refcursor
    LANGUAGE plpgsql
    AS $$
declare micursor refcursor;

begin

open micursor for
SELECT pro.id AS idproveedor, tdoc.id AS idtipodocumento, 
       tdoc.nombre AS nombretipodocumento, pro.numerodocumento, pro.nombres, 
       pro.apellidopaterno, pro.apellidomaterno, 
       dir.idvia, tvia.nombre AS nombretipovia, 
       dir.nombrevia, dir.numero, dir.interior, dir.manzana, dir.lote, 
	    ( SELECT tel.numero
		FROM negocio."TelefonoDireccion" tedir, 
		     negocio."Telefono" tel
	       WHERE tedir.idestadoregistro = 1 
		 AND tel.idestadoregistro   = 1 
		 AND tedir.iddireccion      = dir.id 
		 AND tedir.idtelefono       = tel.id LIMIT 1) AS teledireccion
   FROM negocio."Persona" pro, 
        soporte."Tablamaestra" tdoc,
        negocio."PersonaDireccion" pdir
   LEFT JOIN negocio."Direccion" dir     ON pdir.iddireccion = dir.id AND dir.principal = 'S'
   LEFT JOIN soporte."Tablamaestra" tvia ON tvia.idmaestro  = 2       AND dir.idvia     = tvia.id
  WHERE pro.idestadoregistro  = 1 
    AND pro.idtipopersona     = 1
    AND tdoc.idmaestro        = 1 
    AND pro.idtipodocumento   = tdoc.id
    AND dir.idestadoregistro  = 1 
    AND pdir.idestadoregistro = 1 
    AND pro.id                = pdir.idpersona
    AND tdoc.id               = COALESCE(p_idtipodocumento,tdoc.id)
    AND pro.numerodocumento   = COALESCE(p_numerodocumento,pro.numerodocumento)
    AND CONCAT(replace(pro.nombres,' ',''),trim(pro.apellidopaterno),trim(pro.apellidomaterno)) like '%'||COALESCE(p_nombres,CONCAT(replace(pro.nombres,' ',''),trim(pro.apellidopaterno),trim(pro.apellidomaterno)))||'%';

return micursor;

end;
$$;


CREATE FUNCTION fn_eliminarcontactoproveedor(p_idpersona integer, p_usuariomodificacion character varying, p_ipmodificacion character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
declare fechahoy timestamp with time zone;

begin

select current_timestamp into fechahoy;

UPDATE negocio."Persona"
   SET idestadoregistro    = 0,
       usuariomodificacion = p_usuariomodificacion, 
       fechamodificacion   = fechahoy, 
       ipmodificacion      = p_ipmodificacion
 WHERE idestadoregistro    = 1
   AND id                  in (select idcontacto
                                 from negocio."PersonaContactoProveedor"
                                where idestadoregistro = 1
                                  and idproveedor      = p_idpersona);

return true;

end;
$$;


CREATE FUNCTION fn_eliminarcorreoscontacto(p_idpersona integer, p_usuariomodificacion character varying, p_ipmodificacion character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$

declare fechahoy timestamp with time zone;

begin

select current_timestamp into fechahoy;

UPDATE negocio."CorreoElectronico"
   SET idestadoregistro    = 0, 
       usuariomodificacion = p_usuariomodificacion, 
       fechamodificacion   = fechahoy, 
       ipmodificacion      = p_ipmodificacion
 WHERE idestadoregistro    = 1
   AND idpersona           = p_idpersona;

return p_idpersona;

end;
$$;

CREATE FUNCTION fn_eliminardirecciones(p_idpersona integer, p_usuariomodificacion character varying, p_ipmodificacion character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
declare fechahoy timestamp with time zone;

begin

select current_timestamp into fechahoy;

UPDATE 
  negocio."Direccion" 
SET 
  idestadoregistro     = 0,
  usuariomodificacion  = p_usuariomodificacion,
  fechamodificacion    = fechahoy,
  ipmodificacion       = p_ipmodificacion
WHERE idestadoregistro = 1
  AND id               IN (SELECT iddireccion 
                             FROM negocio."PersonaDireccion"
                            WHERE idpersona        = p_idpersona
                              AND idestadoregistro = 1);

return true;

end;
$$;

CREATE FUNCTION fn_eliminarpersona(p_idpersona integer, p_idtipopersona integer, p_usuariomodificacion character varying, p_ipmodificacion character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
declare fechahoy timestamp with time zone;

begin

select current_timestamp into fechahoy;

UPDATE negocio."Persona"
   SET idestadoregistro    = 0,
       usuariomodificacion = p_usuariomodificacion, 
       fechamodificacion   = fechahoy, 
       ipmodificacion      = p_ipmodificacion
 WHERE idestadoregistro    = 1
   AND id                  = p_idpersona
   AND idtipopersona       = p_idtipopersona;

return true;

end;
$$;

CREATE FUNCTION fn_eliminartelefonoscontacto(p_idcontacto integer, p_usuariomodificacion character varying, p_ipmodificacion character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
declare fechahoy timestamp with time zone;

begin

select current_timestamp into fechahoy;

UPDATE 
  negocio."Telefono" 
SET
  idestadoregistro     = 0,
  usuariomodificacion  = p_usuariomodificacion,
  fechamodificacion    = fechahoy,
  ipmodificacion       = p_ipmodificacion
WHERE idestadoregistro = 1
  AND id               IN (SELECT idtelefono
                             FROM negocio."TelefonoPersona"
                            WHERE idpersona        = p_idcontacto
                              AND idestadoregistro = 1);

return true;

end;
$$;

CREATE FUNCTION fn_eliminartelefonosdireccion(p_iddireccion integer, p_usuariomodificacion character varying, p_ipmodificacion character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
declare fechahoy timestamp with time zone;

begin

select current_timestamp into fechahoy;

UPDATE 
  negocio."Telefono" 
SET 
  idestadoregistro     = 0,
  usuariomodificacion  = p_usuariomodificacion,
  fechamodificacion    = fechahoy,
  ipmodificacion       = p_ipmodificacion
WHERE idestadoregistro = 1
  AND id               IN (SELECT idtelefono
                             FROM negocio."TelefonoDireccion"
                            WHERE iddireccion      = p_iddireccion
                              AND idestadoregistro = 1);

return true;

end;
$$;

CREATE FUNCTION fn_generarcodigonovio(p_codigosnovios integer) RETURNS character varying
    LANGUAGE plpgsql
    AS $$

declare cod_novio character varying(5);
declare ceros character varying(5);
declare cantidad integer;

Begin

ceros = '00000';
cod_novio = cast(p_codigosnovios as character varying);
cantidad = length(cast(p_codigosnovios as character varying));
cod_novio = left(ceros, (5-cantidad)) || cod_novio;

return cod_novio;

end;
$$;

CREATE FUNCTION fn_ingresarcontactoproveedor(p_idproveedor integer, p_idcontacto integer, p_idarea integer, p_anexo character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

Begin

INSERT INTO negocio."PersonaContactoProveedor"(
            idproveedor, idcontacto, idarea, anexo)
    VALUES (p_idproveedor, p_idcontacto, p_idarea, p_anexo);

return true;

end;
$$;

CREATE FUNCTION fn_ingresarcorreoelectronico(p_correo character varying, p_idpersona integer, p_usuariocreacion character varying, p_ipcreacion character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$

declare maxcorreo integer = 0;
declare fechahoy timestamp with time zone;

begin

maxcorreo = nextval('negocio.seq_correoelectronico');

select current_timestamp into fechahoy;

INSERT INTO negocio."CorreoElectronico"(
            id, correo, idpersona, usuariocreacion, fechacreacion, ipcreacion, 
            usuariomodificacion, fechamodificacion, ipmodificacion)
    VALUES (maxcorreo, p_correo, p_idpersona, p_usuariocreacion, fechahoy, p_ipcreacion, 
            p_usuariocreacion, fechahoy, p_ipcreacion);

return maxcorreo;

end;
$$;

CREATE FUNCTION fn_ingresardireccion(p_idvia integer, p_nombrevia character varying, p_numero character varying, p_interior character varying, p_manzana character varying, p_lote character varying, p_principal character varying, p_idubigeo character, p_usuariocreacion character varying, p_ipcreacion character varying, p_observacion character varying, p_referencia character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$

declare maxdireccion integer;
declare fechahoy timestamp with time zone;

begin

select coalesce(max(id),0)
  into maxdireccion
  from negocio."Direccion";

maxdireccion = nextval('negocio.seq_direccion');

select current_timestamp into fechahoy;

insert into negocio."Direccion"(id, idvia, nombrevia, numero, interior, manzana, lote, principal, idubigeo, 
            usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, 
            fechamodificacion, ipmodificacion, observacion, referencia)
values (maxdireccion,p_idvia,p_nombrevia,p_numero,p_interior,p_manzana,p_lote,p_principal,p_idubigeo,p_usuariocreacion,fechahoy,
	p_ipcreacion,p_usuariocreacion,fechahoy,p_ipcreacion, p_observacion, p_referencia);

return maxdireccion;

end;
$$;

CREATE FUNCTION fn_ingresarpais(p_descripcion character varying, p_idcontinente integer, p_usuariocreacion character varying, p_ipcreacion character varying, p_fecnacimiento date) RETURNS integer
    LANGUAGE plpgsql
    AS $$

declare maxpais integer;
declare fechahoy timestamp with time zone;

begin

maxpais = nextval('negocio.seq_pais');

select current_timestamp into fechahoy;

INSERT INTO soporte.pais(
            id, descripcion, idcontinente, usuariocreacion, fechacreacion, 
            ipcreacion, usuariomodificacion, fechamodificacion, ipmodificacion, 
            idestadoregistro)
    VALUES (maxpais, p_descripcion, p_idcontinente, p_usuariocreacion, fechahoy, 
            p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion, 1);

return maxpais;
end;
$$;

CREATE FUNCTION fn_ingresarpais(p_idcontinente integer, p_idpais integer, p_codigoiata character varying, p_idtipodestino character varying, p_descripcion character varying, p_usuariocreacion character varying, p_ipcreacion character varying, p_fecnacimiento date) RETURNS integer
    LANGUAGE plpgsql
    AS $$

declare maxdestino integer;
declare fechahoy timestamp with time zone;

begin

maxdestino = nextval('negocio.seq_desrino');

select current_timestamp into fechahoy;

INSERT INTO soporte.destino(
            id, idcontinente, idpais, codigoiata, idtipodestino, descripcion, 
            usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, 
            fechamodificacion, ipmodificacion, idestadoregistro)
    VALUES (maxdestino, p_idcontinente, p_codigoiata, p_idpais, p_idtipodestino, p_descripcion, p_usuariocreacion, fechahoy, 
            p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion, 1);

return maxdestino;
end;
$$;

CREATE FUNCTION fn_ingresarpersona(p_idtipopersona integer, p_nombres character varying, p_apepaterno character varying, p_apematerno character varying, p_idgenero character varying, p_idestadocivil integer, p_idtipodocumento integer, p_numerodocumento character varying, p_usuariocreacion character varying, p_ipcreacion character varying, p_fecnacimiento date) RETURNS integer
    LANGUAGE plpgsql
    AS $$

declare maxpersona integer;
declare fechahoy timestamp with time zone;

begin

maxpersona = nextval('negocio.seq_persona');

select current_timestamp into fechahoy;

insert into negocio."Persona"(id, idtipopersona, nombres, apellidopaterno, apellidomaterno, 
            idgenero, idestadocivil, idtipodocumento, numerodocumento, usuariocreacion, 
            fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
            ipmodificacion,fecnacimiento)
values (maxpersona,p_idtipopersona,p_nombres,p_apepaterno,p_apematerno,p_idgenero,p_idestadocivil,p_idtipodocumento,p_numerodocumento,p_usuariocreacion,fechahoy,
	p_ipcreacion,p_usuariocreacion,fechahoy,p_ipcreacion,p_fecnacimiento);

return maxpersona;
end;
$$;

CREATE FUNCTION fn_ingresarpersonadireccion(p_idpersona integer, p_idtipopersona integer, p_iddireccion integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

Begin

INSERT INTO negocio."PersonaDireccion"(
            idpersona, iddireccion, idtipopersona)
    VALUES (p_idpersona, p_iddireccion, p_idtipopersona);

return true;

end;
$$;

CREATE FUNCTION fn_ingresarpersonaproveedor(p_idpersona integer, p_idrubro integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

Begin

INSERT INTO negocio."PersonaAdicional"(idpersona, idrubro)
    VALUES (p_idpersona, p_idrubro);

return true;

end;
$$;

CREATE FUNCTION fn_ingresarservicionovios(p_codigonovios character varying, p_idnovia integer, p_idnovio integer, p_iddestino integer, p_fechaboda date, p_idmoneda integer, p_cuotainicial double precision, p_dias integer, p_noches integer, p_fechashower date, p_observaciones text, p_usuariocreacion character varying, p_ipcreacion character varying) RETURNS character varying
    LANGUAGE plpgsql
    AS $$

declare maxid integer;
declare fechahoy timestamp with time zone;
declare cod_novio character varying(5);

Begin

maxid = nextval('negocio.seq_novios');
select current_timestamp into fechahoy;
cod_novio = negocio.fn_generarcodigonovio(p_codigonovios);

INSERT INTO negocio."ServicioNovios"(
            id, codigonovios, idnovia, idnovio, iddestino, fechaboda, idmoneda, 
            cuotainicial, dias, noches, fechashower, observaciones, usuariocreacion, 
            fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
            ipmodificacion, idestadoregistro)
    VALUES (maxid, cod_novio, p_idnovia, p_idnovio, p_iddestino, p_fechaboda, p_idmoneda, p_cuotainicial, p_dias, p_noches, p_fechashower, p_observacionestext, p_usuariocreacion, 
	    p_fechacreacion, p_ipcreacion, p_usuariocreacion, p_fechacreacion, p_ipcreacion);

return cod_novio;

end;
$$;

CREATE FUNCTION fn_ingresartelefono(p_numero character varying, p_idempresaproveedor integer, p_usuariocreacion character varying, p_ipcreacion character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$

declare maxtelefono integer;
declare fechahoy timestamp with time zone;

Begin

select coalesce(max(id),0)
  into maxtelefono
  from negocio."Telefono";

maxtelefono = nextval('negocio.seq_telefono');
select current_timestamp into fechahoy;

INSERT INTO negocio."Telefono"(
            id, numero, idempresaproveedor, usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, 
            fechamodificacion, ipmodificacion)
    VALUES (maxtelefono, p_numero, p_idempresaproveedor, p_usuariocreacion,fechahoy,
	p_ipcreacion,p_usuariocreacion,fechahoy,p_ipcreacion);

return maxtelefono;

end;
$$;

CREATE FUNCTION fn_ingresartelefonodireccion(p_idtelefono integer, p_iddireccion integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

Begin

INSERT INTO negocio."TelefonoDireccion"(
            idtelefono, iddireccion)
    VALUES (p_idtelefono, p_iddireccion);

return true;

end;
$$;

CREATE FUNCTION fn_ingresartelefonopersona(p_idtelefono integer, p_idpersona integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$

Begin

INSERT INTO negocio."TelefonoPersona"(
            idtelefono, idpersona)
    VALUES (p_idtelefono, p_idpersona);

return true;

end;
$$;


INSERT INTO "Persona" VALUES (34, 3, 'JORGE ANIBAL', 'ESPINOZA', 'SANCHEZ', NULL, NULL, 1, '12345678', 'ADMIN', '2014-05-18 02:30:47.824+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 05:34:30.32+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (21, 3, 'kjklj kjlkj', 'sdfsdfsdf', 'kjkljklj', NULL, NULL, 1, '12345678', 'admin', '2014-05-05 01:27:34.348+02', '127.0.0.1      ', 'admin', '2014-05-05 01:29:12.662+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (39, 3, 'JORGE ANIBAL', 'ESPINOZA', 'SANCHEZ', NULL, NULL, 1, '12345678', 'ADMIN', '2014-05-18 05:34:30.32+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 05:34:30.32+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (22, 3, 'kjklj kjlkj', 'sdfsdfsdf', 'kjkljklj', NULL, NULL, 1, '12345678', 'admin', '2014-05-05 01:29:12.662+02', '127.0.0.1      ', 'admin', '2014-05-05 06:39:30.68+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (35, 3, 'VICTOR', 'LOPEZ', 'SANTIAGO', NULL, NULL, 3, '23423232132', 'ADMIN', '2014-05-18 02:31:03.257+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 05:34:36.071+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (27, 2, 'TIGO', NULL, NULL, NULL, NULL, 3, '20154546541', 'admin', '2014-05-09 07:12:35.458+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 05:34:48+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (36, 3, 'DSFDSFDSFDSF', 'SDFDSF', 'SDFDSFDSF', NULL, NULL, 2, '232132132', 'ADMIN', '2014-05-18 02:31:18.191+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 05:34:48+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (11, 3, 'carlos', 'caballeo', 'sdsd', NULL, NULL, 1, '21457878', 'admin', '2014-05-04 06:53:25.427+02', '127.0.0.1      ', 'admin', '2014-05-04 08:56:24.999+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (17, 3, 'carlos alberto', 'caballeo', 'castro', NULL, NULL, 1, '21457878', 'admin', '2014-05-04 08:56:24.999+02', '127.0.0.1      ', 'admin', '2014-05-05 07:00:22.184+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (29, 1, 'fsdfsd sdsdfds', NULL, NULL, NULL, NULL, 3, '20248498798', 'admin', '2014-05-17 04:36:10.523+02', '127.0.0.1      ', 'admin', '2014-05-17 04:36:10.523+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (30, 3, 'asdasd sdasdsad', 'afas', 'sdasd', NULL, NULL, 1, '34343433', 'admin', '2014-05-17 04:36:10.523+02', '127.0.0.1      ', 'admin', '2014-05-17 04:36:10.523+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (16, 3, 'kjklj kjlkj', 'sdfsdfsdf', 'kjkljklj', NULL, NULL, 1, '12345678', 'admin', '2014-05-04 08:10:04.292+02', '127.0.0.1      ', 'admin', '2014-05-05 01:26:56.602+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (41, 3, 'DSFDSFDSFDSF', 'SDFDSF', 'SDFDSFDSF', NULL, NULL, 2, '232132132', 'ADMIN', '2014-05-18 05:34:48+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 05:34:48+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (20, 3, 'kjklj kjlkj', 'sdfsdfsdf', 'kjkljklj', NULL, NULL, 1, '12345678', 'admin', '2014-05-05 01:26:56.602+02', '127.0.0.1      ', 'admin', '2014-05-05 01:27:34.348+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (25, 2, 'LUIS', 'LOPEZ', 'CARMEN', NULL, NULL, 1, '23243232', 'admin', '2014-05-08 06:29:39.062+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 05:34:51.979+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (32, 3, 'victor', 'lopez', 'santiago', NULL, NULL, 3, '23423232132', 'admin', '2014-05-17 23:24:44.088+02', '127.0.0.1      ', 'admin', '2014-05-17 23:54:37.44+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (23, 3, 'jorge anibal', 'espinoza', 'sanchez', NULL, NULL, 1, '12345678', 'admin', '2014-05-05 06:39:30.68+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 02:30:47.824+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (31, 2, 'CARLOS ALBERTO', 'CHAVEZ', 'LOPEZZ', NULL, NULL, 1, '32015787', 'admin', '2014-05-17 23:24:44.088+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 19:52:41.042+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (33, 3, 'victor', 'lopez', 'santiago', NULL, NULL, 3, '23423232132', 'admin', '2014-05-17 23:54:37.44+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 02:31:03.257+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (40, 3, 'VICTOR', 'LOPEZ', 'SANTIAGO', NULL, NULL, 3, '23423232132', 'ADMIN', '2014-05-18 05:34:36.071+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 19:52:41.042+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (28, 3, 'dsfdsfdsfdsf', 'sdfdsf', 'sdfdsfdsf', NULL, NULL, 2, '232132132', 'admin', '2014-05-09 07:12:35.458+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 02:31:18.191+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (52, 3, 'VICTOR', 'LOPEZ', 'SANTIAGO', NULL, NULL, 3, '23423232132', 'ADMIN', '2014-05-18 19:52:41.042+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 19:52:41.042+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (26, 3, 'sfsd dfsdsdfs', 'assdsad', 'dsfdf', NULL, NULL, 1, '23213213', 'admin', '2014-05-08 06:29:39.062+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 02:31:23.221+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (24, 3, 'carlos alberto', 'caballeo', 'castro', NULL, NULL, 1, '21457878', 'admin', '2014-05-05 07:00:22.184+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 02:31:27.531+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (15, 2, 'CAMELLOS CAMEL', NULL, NULL, NULL, NULL, 3, '20157844456', 'admin', '2014-05-04 08:10:04.292+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 05:34:30.32+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (37, 3, 'SFSD DFSDSDFS', 'ASSDSAD', 'DSFDF', NULL, NULL, 1, '23213213', 'ADMIN', '2014-05-18 02:31:23.221+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 05:34:51.979+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (42, 3, 'SFSD DFSDSDFS', 'ASSDSAD', 'DSFDF', NULL, NULL, 1, '23213213', 'ADMIN', '2014-05-18 05:34:51.979+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 05:34:51.979+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (10, 2, 'LOS ANGELES NEGROS', NULL, NULL, NULL, NULL, 3, '20123456789', 'admin', '2014-05-04 06:53:25.427+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 05:34:56.946+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (38, 3, 'CARLOS ALBERTO', 'CABALLEO', 'CASTRO', NULL, NULL, 1, '21457878', 'ADMIN', '2014-05-18 02:31:27.531+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 05:34:56.946+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (43, 3, 'CARLOS ALBERTO', 'CABALLEO', 'CASTRO', NULL, NULL, 1, '21457878', 'ADMIN', '2014-05-18 05:34:56.946+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 05:34:56.946+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (46, 1, 'CXVXCBXCV', 'CVXCVCV', 'XCVXV', NULL, NULL, 1, '21323213', 'ADMIN', '2014-05-18 07:39:09.344+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 07:39:09.344+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (47, 3, 'DADAS SADDSAD', 'ZXCXZC', 'QWEWQE', NULL, NULL, 1, '21213123', 'ADMIN', '2014-05-18 07:39:09.344+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 07:39:09.344+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (48, 1, 'SDFDSF DSFSDF', NULL, NULL, NULL, NULL, 3, '24435123213', 'ADMIN', '2014-05-18 07:44:23.685+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 07:44:23.685+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (49, 3, 'SDFDS DSFDSF', 'SDFDSF', 'SDFSDF', NULL, NULL, 3, '25654353453', 'ADMIN', '2014-05-18 07:44:23.685+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 07:44:23.685+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (51, 3, 'ASDASD ASDAD', 'ASDAD', 'ASDASD', NULL, NULL, 3, '21312312312', 'ADMIN', '2014-05-18 07:55:27.999+02', '127.0.0.1      ', 'ADMIN', '2014-05-21 04:28:25.32+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (55, 1, 'PAOLA FIORELLA', 'HUARACHI', 'PFLUCKER', 'F', 1, 1, '42094852', 'PAOLA', '2014-06-04 05:11:02.109+02', '192.168.1.13   ', 'PAOLA', '2014-06-04 05:15:24.068+02', '192.168.1.13   ', 1, '1983-09-02');
INSERT INTO "Persona" VALUES (56, 3, 'PAOLA FIORELLA', 'HUARACHI', 'PFLUCKER', NULL, NULL, 1, '42094852', 'PAOLA', '2014-06-04 05:11:02.109+02', '192.168.1.13   ', 'PAOLA', '2014-06-04 05:12:19.966+02', '192.168.1.13   ', 0, NULL);
INSERT INTO "Persona" VALUES (50, 1, 'JORGE', 'SANCHEZ', 'SACOS', 'M', 1, 2, '32432434', 'ADMIN', '2014-05-18 07:55:27.999+02', '127.0.0.1      ', 'ADMIN', '2014-05-21 05:02:18.967+02', '127.0.0.1      ', 1, '1962-04-11');
INSERT INTO "Persona" VALUES (53, 3, 'ASDASD ASDAD', 'ASDAD', 'ASDASD', NULL, NULL, 3, '21312312312', 'ADMIN', '2014-05-21 04:28:25.32+02', '127.0.0.1      ', 'ADMIN', '2014-05-21 05:02:18.967+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (54, 3, 'ASDASD ASDAD', 'ASDAD', 'ASDASD', NULL, NULL, 3, '21312312312', 'ADMIN', '2014-05-21 05:02:18.967+02', '127.0.0.1      ', 'ADMIN', '2014-05-21 05:02:18.967+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (60, 1, 'LIZ', 'BOLO', 'CACERES', 'F', 1, 1, '40887612', 'PAOLA', '2014-06-04 05:23:48.989+02', '192.168.1.13   ', 'PAOLA', '2014-06-04 05:23:48.989+02', '192.168.1.13   ', 1, '1982-06-04');
INSERT INTO "Persona" VALUES (57, 3, 'PAOLA FIORELLA', 'HUARACHI', 'PFLUCKER', NULL, NULL, 1, '42094852', 'PAOLA', '2014-06-04 05:12:19.966+02', '192.168.1.13   ', 'ADMIN', '2014-06-04 05:13:24.112+02', '0:0:0:0:0:0:0:1', 0, NULL);
INSERT INTO "Persona" VALUES (58, 3, 'PAOLA FIORELLA', 'HUARACHI', 'PFLUCKER', NULL, NULL, 1, '42094852', 'PAOLA', '2014-06-04 05:13:24.112+02', '192.168.1.13   ', 'PAOLA', '2014-06-04 05:15:24.068+02', '192.168.1.13   ', 0, NULL);
INSERT INTO "Persona" VALUES (59, 3, 'PAOLA FIORELLA', 'HUARACHI', 'PFLUCKER', NULL, NULL, 1, '42094852', 'PAOLA', '2014-06-04 05:15:24.068+02', '192.168.1.13   ', 'PAOLA', '2014-06-04 05:15:24.068+02', '192.168.1.13   ', 1, NULL);
INSERT INTO "Persona" VALUES (61, 3, 'LIZ', 'BOLO', 'CACERES', NULL, NULL, 1, '40605677', 'PAOLA', '2014-06-04 05:23:48.989+02', '192.168.1.13   ', 'PAOLA', '2014-06-04 05:23:48.989+02', '192.168.1.13   ', 1, NULL);
INSERT INTO "Persona" VALUES (62, 3, 'PEPITO', 'PEREZ', 'QUISPE', NULL, NULL, 1, '34567789', 'PAOLA', '2014-06-04 05:23:48.989+02', '192.168.1.13   ', 'PAOLA', '2014-06-04 05:23:48.989+02', '192.168.1.13   ', 1, NULL);
INSERT INTO "Persona" VALUES (45, 3, 'DSFSDF', 'FSDFD', 'SDFSDF', NULL, NULL, 1, '32213343', 'ADMIN', '2014-05-18 07:11:20.371+02', '127.0.0.1      ', 'ADMIN', '2014-06-21 18:38:26.081+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (44, 1, 'ESTEBAN', 'ALVAREZ', 'CAMONO', 'M', 1, 1, '23423434', 'ADMIN', '2014-05-18 07:11:20.371+02', '127.0.0.1      ', 'ADMIN', '2014-06-22 06:28:05.685+02', '127.0.0.1      ', 1, '1992-09-09');
INSERT INTO "Persona" VALUES (63, 3, 'DSFSDF', 'FSDFD', 'SDFSDF', NULL, NULL, 1, '32213343', 'ADMIN', '2014-06-21 18:38:26.081+02', '127.0.0.1      ', 'ADMIN', '2014-06-22 06:28:05.685+02', '127.0.0.1      ', 0, NULL);
INSERT INTO "Persona" VALUES (64, 3, 'DSFSDF', 'FSDFD', 'SDFSDF', NULL, NULL, 1, '32213343', 'ADMIN', '2014-06-22 06:28:05.685+02', '127.0.0.1      ', 'ADMIN', '2014-06-22 06:28:05.685+02', '127.0.0.1      ', 1, NULL);
INSERT INTO "Persona" VALUES (65, 1, 'CLAUDIA', 'VILCAPUMA', 'SANCHEZ', 'F', 1, 1, '45454545', 'ADMIN', '2014-06-22 06:32:18.505+02', '127.0.0.1      ', 'ADMIN', '2014-06-22 06:32:18.505+02', '127.0.0.1      ', 1, '1994-05-21');
INSERT INTO "Persona" VALUES (66, 3, 'CLAUDIA', 'VILCAPUMA', 'SANCHEZ', NULL, NULL, 1, '45454545', 'ADMIN', '2014-06-22 06:32:18.505+02', '127.0.0.1      ', 'ADMIN', '2014-06-22 06:32:18.505+02', '127.0.0.1      ', 1, NULL);

INSERT INTO "Direccion" VALUES (43, 4, 'SDFSDFSDF', '2132', NULL, NULL, NULL, 'S', '060504', NULL, '2014-05-21 04:28:25.32+02', NULL, 'ADMIN', '2014-05-21 05:02:18.967+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (2, 3, 'los callos', '485', NULL, NULL, NULL, 'S', '070103', NULL, '2014-05-04 06:53:25.427+02', NULL, 'admin', '2014-05-04 08:56:24.999+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (45, 4, 'SDFSDFSDF', '2132', NULL, NULL, NULL, 'S', '060504', NULL, '2014-05-21 05:02:18.967+02', NULL, NULL, '2014-05-21 05:02:18.967+02', NULL, 1, NULL, NULL);
INSERT INTO "Direccion" VALUES (46, 2, 'LA PLANICIE', NULL, NULL, 'E', '17', 'S', '150135', NULL, '2014-06-04 05:11:02.109+02', NULL, 'PAOLA', '2014-06-04 05:12:19.966+02', '192.168.1.13', 0, NULL, 'ENTRE AV. CANTA CALLAO Y AV. SOL DE NARANJAL');
INSERT INTO "Direccion" VALUES (5, 3, 'los catas', '232', NULL, NULL, NULL, 'S', '080502', NULL, '2014-05-04 08:10:04.292+02', NULL, 'admin', '2014-05-05 01:26:56.602+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (10, 3, 'los catas', '232', NULL, NULL, NULL, 'S', '080502', NULL, '2014-05-05 01:26:56.602+02', NULL, 'admin', '2014-05-05 01:27:34.348+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (11, 3, 'los catas', '232', NULL, NULL, NULL, 'S', '080502', NULL, '2014-05-05 01:27:34.348+02', NULL, 'admin', '2014-05-05 01:29:12.662+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (12, 3, 'los catas', '232', NULL, NULL, NULL, 'S', '080502', NULL, '2014-05-05 01:29:12.662+02', NULL, 'admin', '2014-05-05 06:39:30.68+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (6, 3, 'los callos', '485', NULL, NULL, NULL, 'S', '070103', NULL, '2014-05-04 08:56:24.999+02', NULL, 'admin', '2014-05-05 06:42:21.139+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (14, 3, 'los callos', '485', NULL, NULL, NULL, 'S', '070103', NULL, '2014-05-05 06:42:21.139+02', NULL, 'admin', '2014-05-05 06:47:38.932+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (16, 4, 'las aves', NULL, NULL, NULL, NULL, 'N', '110302', NULL, '2014-05-05 06:47:38.932+02', NULL, NULL, '2014-05-05 06:47:38.932+02', NULL, 1, NULL, NULL);
INSERT INTO "Direccion" VALUES (15, 3, 'los callos', '485', NULL, NULL, NULL, 'S', '070103', NULL, '2014-05-05 06:47:38.932+02', NULL, 'admin', '2014-05-05 06:57:06.379+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (18, 2, 'las almas', '2323', NULL, NULL, NULL, 'N', '040402', NULL, '2014-05-05 06:57:06.379+02', NULL, NULL, '2014-05-05 06:57:06.379+02', NULL, 1, NULL, NULL);
INSERT INTO "Direccion" VALUES (17, 3, 'los callos', '485', NULL, NULL, NULL, 'S', '070103', NULL, '2014-05-05 06:57:06.379+02', NULL, 'admin', '2014-05-05 07:00:22.184+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (23, 3, 'dfsdfd', '3434', NULL, NULL, NULL, 'S', '060505', NULL, '2014-05-17 04:36:10.523+02', NULL, NULL, '2014-05-17 04:36:10.523+02', NULL, 1, NULL, NULL);
INSERT INTO "Direccion" VALUES (24, 3, 'los andes', '2321', NULL, NULL, NULL, 'S', '050704', NULL, '2014-05-17 23:24:44.088+02', NULL, 'admin', '2014-05-17 23:54:37.44+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (13, 3, 'los catas', '232', NULL, NULL, NULL, 'S', '080502', NULL, '2014-05-05 06:39:30.68+02', NULL, 'ADMIN', '2014-05-18 02:30:47.824+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (25, 3, 'los andes', '2321', NULL, NULL, NULL, 'S', '050704', NULL, '2014-05-17 23:54:37.44+02', NULL, 'ADMIN', '2014-05-18 02:31:03.257+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (22, 4, 'dsf sdfds', '21312', NULL, NULL, NULL, 'S', '050304', NULL, '2014-05-09 07:12:35.458+02', NULL, 'ADMIN', '2014-05-18 02:31:18.191+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (21, 4, 'las viñas', '2121', '303', NULL, NULL, 'S', '090504', NULL, '2014-05-08 06:29:39.062+02', NULL, 'ADMIN', '2014-05-18 02:31:23.221+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (19, 3, 'los callos', '485', NULL, NULL, NULL, 'S', '070103', NULL, '2014-05-05 07:00:22.184+02', NULL, 'ADMIN', '2014-05-18 02:31:27.531+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (20, 4, 'los condores', '2966', '656', NULL, NULL, 'N', '050303', NULL, '2014-05-05 07:00:22.184+02', NULL, 'ADMIN', '2014-05-18 02:31:27.531+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (26, 3, 'los catas', '232', NULL, NULL, NULL, 'S', '080502', NULL, '2014-05-18 02:30:47.824+02', NULL, 'ADMIN', '2014-05-18 05:34:30.32+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (32, 3, 'LOS CATAS', '232', NULL, NULL, NULL, 'S', '080502', NULL, '2014-05-18 05:34:30.32+02', NULL, NULL, '2014-05-18 05:34:30.32+02', NULL, 1, NULL, NULL);
INSERT INTO "Direccion" VALUES (27, 3, 'los andes', '2321', NULL, NULL, NULL, 'S', '050704', NULL, '2014-05-18 02:31:03.257+02', NULL, 'ADMIN', '2014-05-18 05:34:36.071+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (28, 4, 'dsf sdfds', '21312', NULL, NULL, NULL, 'S', '050304', NULL, '2014-05-18 02:31:18.191+02', NULL, 'ADMIN', '2014-05-18 05:34:48+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (34, 4, 'DSF SDFDS', '21312', NULL, NULL, NULL, 'S', '050304', NULL, '2014-05-18 05:34:48+02', NULL, NULL, '2014-05-18 05:34:48+02', NULL, 1, NULL, NULL);
INSERT INTO "Direccion" VALUES (29, 4, 'las viñas', '2121', '303', NULL, NULL, 'S', '090504', NULL, '2014-05-18 02:31:23.221+02', NULL, 'ADMIN', '2014-05-18 05:34:51.979+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (35, 4, 'LAS VIÑAS', '2121', '303', NULL, NULL, 'S', '090504', NULL, '2014-05-18 05:34:51.979+02', NULL, NULL, '2014-05-18 05:34:51.979+02', NULL, 1, NULL, NULL);
INSERT INTO "Direccion" VALUES (30, 4, 'los condores', '2966', '656', NULL, NULL, 'N', '050303', NULL, '2014-05-18 02:31:27.531+02', NULL, 'ADMIN', '2014-05-18 05:34:56.946+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (31, 3, 'los callos', '485', NULL, NULL, NULL, 'S', '070103', NULL, '2014-05-18 02:31:27.531+02', NULL, 'ADMIN', '2014-05-18 05:34:56.946+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (36, 4, 'LOS CONDORES', '2966', '656', NULL, NULL, 'N', '050303', NULL, '2014-05-18 05:34:56.946+02', NULL, NULL, '2014-05-18 05:34:56.946+02', NULL, 1, NULL, NULL);
INSERT INTO "Direccion" VALUES (37, 3, 'LOS CALLOS', '485', NULL, NULL, NULL, 'S', '070103', NULL, '2014-05-18 05:34:56.946+02', NULL, NULL, '2014-05-18 05:34:56.946+02', NULL, 1, NULL, NULL);
INSERT INTO "Direccion" VALUES (39, 3, 'DFSDF', '2132', NULL, NULL, NULL, 'S', '050605', NULL, '2014-05-18 07:39:09.344+02', NULL, NULL, '2014-05-18 07:39:09.344+02', NULL, 1, NULL, NULL);
INSERT INTO "Direccion" VALUES (40, 2, 'DSASD', '2132', NULL, NULL, NULL, 'S', '050304', NULL, '2014-05-18 07:44:23.685+02', NULL, NULL, '2014-05-18 07:44:23.685+02', NULL, 1, NULL, NULL);
INSERT INTO "Direccion" VALUES (33, 3, 'LOS ANDES', '2321', NULL, NULL, NULL, 'S', '050704', NULL, '2014-05-18 05:34:36.071+02', NULL, 'ADMIN', '2014-05-18 19:52:41.042+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (42, 3, 'LOS ANDES', '2321', NULL, NULL, NULL, 'S', '050704', NULL, '2014-05-18 19:52:41.042+02', NULL, NULL, '2014-05-18 19:52:41.042+02', NULL, 1, 'SDFDFSDFSADFDSF', 'DFASDFASDF');
INSERT INTO "Direccion" VALUES (41, 4, 'SDFSDFSDF', '2132', NULL, NULL, NULL, 'S', '060504', NULL, '2014-05-18 07:55:27.999+02', NULL, 'ADMIN', '2014-05-21 04:28:25.32+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (47, 2, 'LA PLANICIE', NULL, NULL, 'E', '17', 'S', '150135', NULL, '2014-06-04 05:12:19.966+02', NULL, 'ADMIN', '2014-06-04 05:13:24.112+02', '0:0:0:0:0:0:0:1', 0, NULL, 'ENTRE AV. CANTA CALLAO Y AV. SOL DE NARANJAL');
INSERT INTO "Direccion" VALUES (48, 2, 'LA PLANICIE', NULL, NULL, 'E', '17', 'S', '150135', NULL, '2014-06-04 05:13:24.112+02', NULL, 'PAOLA', '2014-06-04 05:15:24.068+02', '192.168.1.13', 0, NULL, 'ENTRE AV. CANTA CALLAO Y AV. SOL DE NARANJAL');
INSERT INTO "Direccion" VALUES (49, 2, 'LA PLANICIE', NULL, NULL, 'E', '17', 'S', '150135', NULL, '2014-06-04 05:15:24.068+02', NULL, NULL, '2014-06-04 05:15:24.068+02', NULL, 1, NULL, 'ENTRE AV. CANTA CALLAO Y AV. SOL DE NARANJAL');
INSERT INTO "Direccion" VALUES (50, 3, 'CARACAS', '133', NULL, NULL, NULL, 'S', '150113', NULL, '2014-06-04 05:23:48.989+02', NULL, NULL, '2014-06-04 05:23:48.989+02', NULL, 1, NULL, NULL);
INSERT INTO "Direccion" VALUES (38, 3, 'DADSAD', '231', NULL, NULL, NULL, 'S', '070104', NULL, '2014-05-18 07:11:20.371+02', NULL, 'ADMIN', '2014-06-21 18:38:26.081+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (51, 3, 'DADSAD', '231', NULL, NULL, NULL, 'S', '070104', NULL, '2014-06-21 18:38:26.081+02', NULL, 'ADMIN', '2014-06-22 06:28:05.685+02', '127.0.0.1', 0, NULL, NULL);
INSERT INTO "Direccion" VALUES (52, 3, 'DADSAD', '231', NULL, NULL, NULL, 'S', '070104', NULL, '2014-06-22 06:28:05.685+02', NULL, NULL, '2014-06-22 06:28:05.685+02', NULL, 1, NULL, NULL);
INSERT INTO "Direccion" VALUES (53, 1, 'COMANDANTE ESPINAR', '145', '304', NULL, NULL, 'S', '150122', NULL, '2014-06-22 06:32:18.505+02', NULL, NULL, '2014-06-22 06:32:18.505+02', NULL, 1, NULL, NULL);


INSERT INTO "PersonaAdicional" VALUES (15, 4, 1);
INSERT INTO "PersonaAdicional" VALUES (27, 3, 1);
INSERT INTO "PersonaAdicional" VALUES (25, 4, 1);
INSERT INTO "PersonaAdicional" VALUES (10, 4, 1);
INSERT INTO "PersonaAdicional" VALUES (46, 4, 1);
INSERT INTO "PersonaAdicional" VALUES (48, 5, 1);
INSERT INTO "PersonaAdicional" VALUES (31, 8, 1);
INSERT INTO "PersonaAdicional" VALUES (50, 4, 1);
INSERT INTO "PersonaAdicional" VALUES (55, 9, 1);
INSERT INTO "PersonaAdicional" VALUES (60, 2, 1);
INSERT INTO "PersonaAdicional" VALUES (44, 5, 1);
INSERT INTO "PersonaAdicional" VALUES (65, 9, 1);


INSERT INTO "PersonaContactoProveedor" VALUES (10, 11, 5, '144', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (15, 16, 2, NULL, 1);
INSERT INTO "PersonaContactoProveedor" VALUES (10, 17, 5, '144', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (15, 20, 2, NULL, 1);
INSERT INTO "PersonaContactoProveedor" VALUES (15, 21, 2, NULL, 1);
INSERT INTO "PersonaContactoProveedor" VALUES (15, 22, 2, NULL, 1);
INSERT INTO "PersonaContactoProveedor" VALUES (15, 23, 2, '1475', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (10, 24, 5, '144', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (25, 26, 3, '3232', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (27, 28, 3, '323', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (29, 30, 0, NULL, 1);
INSERT INTO "PersonaContactoProveedor" VALUES (31, 32, 0, NULL, 1);
INSERT INTO "PersonaContactoProveedor" VALUES (31, 33, 0, NULL, 1);
INSERT INTO "PersonaContactoProveedor" VALUES (15, 34, 2, '1475', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (31, 35, 0, NULL, 1);
INSERT INTO "PersonaContactoProveedor" VALUES (27, 36, 3, '323', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (25, 37, 3, '3232', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (10, 38, 5, '144', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (15, 39, 2, '1475', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (31, 40, 0, NULL, 1);
INSERT INTO "PersonaContactoProveedor" VALUES (27, 41, 3, '323', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (25, 42, 3, '3232', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (10, 43, 5, '144', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (44, 45, 0, NULL, 1);
INSERT INTO "PersonaContactoProveedor" VALUES (46, 47, 4, '1232', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (48, 49, 4, '2131', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (50, 51, 4, '2132', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (31, 52, 0, NULL, 1);
INSERT INTO "PersonaContactoProveedor" VALUES (50, 53, 4, '2132', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (50, 54, 4, '2132', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (55, 56, 2, '22', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (55, 57, 2, '22', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (55, 58, 2, '22', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (55, 59, 2, '22', 1);
INSERT INTO "PersonaContactoProveedor" VALUES (60, 61, 2, NULL, 1);
INSERT INTO "PersonaContactoProveedor" VALUES (60, 62, 1, NULL, 1);
INSERT INTO "PersonaContactoProveedor" VALUES (44, 63, 0, NULL, 1);
INSERT INTO "PersonaContactoProveedor" VALUES (44, 64, 0, NULL, 1);
INSERT INTO "PersonaContactoProveedor" VALUES (65, 66, 2, NULL, 1);

INSERT INTO "PersonaDireccion" VALUES (15, 10, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (15, 11, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (15, 12, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (15, 5, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (10, 6, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (15, 13, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (10, 14, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (10, 15, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (10, 17, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (10, 19, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (10, 20, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (25, 21, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (27, 22, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (29, 23, 1, 1);
INSERT INTO "PersonaDireccion" VALUES (31, 24, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (31, 25, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (15, 26, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (31, 27, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (27, 28, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (25, 29, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (10, 30, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (10, 31, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (15, 32, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (31, 33, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (27, 34, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (25, 35, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (10, 36, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (10, 37, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (44, 38, 1, 1);
INSERT INTO "PersonaDireccion" VALUES (46, 39, 1, 1);
INSERT INTO "PersonaDireccion" VALUES (48, 40, 1, 1);
INSERT INTO "PersonaDireccion" VALUES (50, 41, 1, 1);
INSERT INTO "PersonaDireccion" VALUES (31, 42, 2, 1);
INSERT INTO "PersonaDireccion" VALUES (50, 43, 1, 1);
INSERT INTO "PersonaDireccion" VALUES (50, 45, 1, 1);
INSERT INTO "PersonaDireccion" VALUES (55, 46, 1, 1);
INSERT INTO "PersonaDireccion" VALUES (55, 47, 1, 1);
INSERT INTO "PersonaDireccion" VALUES (55, 48, 1, 1);
INSERT INTO "PersonaDireccion" VALUES (55, 49, 1, 1);
INSERT INTO "PersonaDireccion" VALUES (60, 50, 1, 1);
INSERT INTO "PersonaDireccion" VALUES (44, 51, 1, 1);
INSERT INTO "PersonaDireccion" VALUES (44, 52, 1, 1);
INSERT INTO "PersonaDireccion" VALUES (65, 53, 1, 1);


INSERT INTO "Telefono" VALUES (12, '3216547', 0, NULL, '2014-05-04 06:53:25.427+02', NULL, NULL, '2014-05-04 06:53:25.427+02', NULL, 1);
INSERT INTO "Telefono" VALUES (13, '1234577', 0, NULL, '2014-05-04 06:53:25.427+02', NULL, NULL, '2014-05-04 06:53:25.427+02', NULL, 1);
INSERT INTO "Telefono" VALUES (14, '987541236', 1, NULL, '2014-05-04 06:53:25.427+02', NULL, NULL, '2014-05-04 06:53:25.427+02', NULL, 1);
INSERT INTO "Telefono" VALUES (15, '987452146', 2, NULL, '2014-05-04 06:53:25.427+02', NULL, NULL, '2014-05-04 06:53:25.427+02', NULL, 1);
INSERT INTO "Telefono" VALUES (82, '2344342', 1, NULL, '2014-06-21 18:38:26.081+02', NULL, NULL, '2014-06-21 18:38:26.081+02', NULL, 1);
INSERT INTO "Telefono" VALUES (81, '3123231', 0, NULL, '2014-06-21 18:38:26.081+02', NULL, '', '2014-06-22 06:28:05.685+02', NULL, 0);
INSERT INTO "Telefono" VALUES (24, '254664656', 0, NULL, '2014-05-04 08:10:04.292+02', NULL, NULL, '2014-05-04 08:10:04.292+02', NULL, 1);
INSERT INTO "Telefono" VALUES (25, '2545454', 0, NULL, '2014-05-04 08:10:04.292+02', NULL, NULL, '2014-05-04 08:10:04.292+02', NULL, 1);
INSERT INTO "Telefono" VALUES (26, '987541236', 1, NULL, '2014-05-04 08:56:24.999+02', NULL, NULL, '2014-05-04 08:56:24.999+02', NULL, 1);
INSERT INTO "Telefono" VALUES (27, '987452146', 2, NULL, '2014-05-04 08:56:24.999+02', NULL, NULL, '2014-05-04 08:56:24.999+02', NULL, 1);
INSERT INTO "Telefono" VALUES (30, '945654654', 2, NULL, '2014-05-05 01:26:56.602+02', NULL, NULL, '2014-05-05 01:26:56.602+02', NULL, 1);
INSERT INTO "Telefono" VALUES (31, '945654654', 2, NULL, '2014-05-05 01:27:34.348+02', NULL, NULL, '2014-05-05 01:27:34.348+02', NULL, 1);
INSERT INTO "Telefono" VALUES (32, '945654654', 2, NULL, '2014-05-05 01:29:12.662+02', NULL, NULL, '2014-05-05 01:29:12.662+02', NULL, 1);
INSERT INTO "Telefono" VALUES (33, '945654654', 2, NULL, '2014-05-05 06:39:30.68+02', NULL, NULL, '2014-05-05 06:39:30.68+02', NULL, 1);
INSERT INTO "Telefono" VALUES (36, '987541236', 1, NULL, '2014-05-05 07:00:22.184+02', NULL, NULL, '2014-05-05 07:00:22.184+02', NULL, 1);
INSERT INTO "Telefono" VALUES (37, '987452146', 2, NULL, '2014-05-05 07:00:22.184+02', NULL, NULL, '2014-05-05 07:00:22.184+02', NULL, 1);
INSERT INTO "Telefono" VALUES (39, '976542323', 2, NULL, '2014-05-08 06:29:39.062+02', NULL, NULL, '2014-05-08 06:29:39.062+02', NULL, 1);
INSERT INTO "Telefono" VALUES (40, '1312323', 3, NULL, '2014-05-09 07:12:35.458+02', NULL, NULL, '2014-05-09 07:12:35.458+02', NULL, 1);
INSERT INTO "Telefono" VALUES (41, '4342343', 0, NULL, '2014-05-17 04:36:10.523+02', NULL, NULL, '2014-05-17 04:36:10.523+02', NULL, 1);
INSERT INTO "Telefono" VALUES (42, '978456465', 3, NULL, '2014-05-17 04:36:10.523+02', NULL, NULL, '2014-05-17 04:36:10.523+02', NULL, 1);
INSERT INTO "Telefono" VALUES (43, '30244654', 0, NULL, '2014-05-17 23:24:44.088+02', NULL, NULL, '2014-05-17 23:54:37.44+02', NULL, 0);
INSERT INTO "Telefono" VALUES (45, '945654654', 2, NULL, '2014-05-18 02:30:47.824+02', NULL, NULL, '2014-05-18 02:30:47.824+02', NULL, 1);
INSERT INTO "Telefono" VALUES (44, '30244654', 0, NULL, '2014-05-17 23:54:37.44+02', NULL, '', '2014-05-18 02:31:03.257+02', NULL, 0);
INSERT INTO "Telefono" VALUES (47, '1312323', 3, NULL, '2014-05-18 02:31:18.191+02', NULL, NULL, '2014-05-18 02:31:18.191+02', NULL, 1);
INSERT INTO "Telefono" VALUES (38, '43243243', 0, NULL, '2014-05-08 06:29:39.062+02', NULL, '', '2014-05-18 02:31:23.221+02', NULL, 0);
INSERT INTO "Telefono" VALUES (49, '976542323', 2, NULL, '2014-05-18 02:31:23.221+02', NULL, NULL, '2014-05-18 02:31:23.221+02', NULL, 1);
INSERT INTO "Telefono" VALUES (34, '2487878', 0, NULL, '2014-05-05 07:00:22.184+02', NULL, '', '2014-05-18 02:31:27.531+02', NULL, 0);
INSERT INTO "Telefono" VALUES (35, '3579685', 0, NULL, '2014-05-05 07:00:22.184+02', NULL, '', '2014-05-18 02:31:27.531+02', NULL, 0);
INSERT INTO "Telefono" VALUES (52, '987541236', 1, NULL, '2014-05-18 02:31:27.531+02', NULL, NULL, '2014-05-18 02:31:27.531+02', NULL, 1);
INSERT INTO "Telefono" VALUES (53, '987452146', 2, NULL, '2014-05-18 02:31:27.531+02', NULL, NULL, '2014-05-18 02:31:27.531+02', NULL, 1);
INSERT INTO "Telefono" VALUES (54, '945654654', 2, NULL, '2014-05-18 05:34:30.32+02', NULL, NULL, '2014-05-18 05:34:30.32+02', NULL, 1);
INSERT INTO "Telefono" VALUES (46, '30244654', 0, NULL, '2014-05-18 02:31:03.257+02', NULL, '', '2014-05-18 05:34:36.071+02', NULL, 0);
INSERT INTO "Telefono" VALUES (56, '1312323', 3, NULL, '2014-05-18 05:34:48+02', NULL, NULL, '2014-05-18 05:34:48+02', NULL, 1);
INSERT INTO "Telefono" VALUES (48, '43243243', 0, NULL, '2014-05-18 02:31:23.221+02', NULL, '', '2014-05-18 05:34:51.979+02', NULL, 0);
INSERT INTO "Telefono" VALUES (57, '43243243', 0, NULL, '2014-05-18 05:34:51.979+02', NULL, NULL, '2014-05-18 05:34:51.979+02', NULL, 1);
INSERT INTO "Telefono" VALUES (58, '976542323', 2, NULL, '2014-05-18 05:34:51.979+02', NULL, NULL, '2014-05-18 05:34:51.979+02', NULL, 1);
INSERT INTO "Telefono" VALUES (50, '2487878', 0, NULL, '2014-05-18 02:31:27.531+02', NULL, '', '2014-05-18 05:34:56.946+02', NULL, 0);
INSERT INTO "Telefono" VALUES (51, '3579685', 0, NULL, '2014-05-18 02:31:27.531+02', NULL, '', '2014-05-18 05:34:56.946+02', NULL, 0);
INSERT INTO "Telefono" VALUES (59, '2487878', 0, NULL, '2014-05-18 05:34:56.946+02', NULL, NULL, '2014-05-18 05:34:56.946+02', NULL, 1);
INSERT INTO "Telefono" VALUES (60, '3579685', 0, NULL, '2014-05-18 05:34:56.946+02', NULL, NULL, '2014-05-18 05:34:56.946+02', NULL, 1);
INSERT INTO "Telefono" VALUES (61, '987541236', 1, NULL, '2014-05-18 05:34:56.946+02', NULL, NULL, '2014-05-18 05:34:56.946+02', NULL, 1);
INSERT INTO "Telefono" VALUES (62, '987452146', 2, NULL, '2014-05-18 05:34:56.946+02', NULL, NULL, '2014-05-18 05:34:56.946+02', NULL, 1);
INSERT INTO "Telefono" VALUES (64, '2344342', 1, NULL, '2014-05-18 07:11:20.371+02', NULL, NULL, '2014-05-18 07:11:20.371+02', NULL, 1);
INSERT INTO "Telefono" VALUES (65, '2131232', 0, NULL, '2014-05-18 07:39:09.344+02', NULL, NULL, '2014-05-18 07:39:09.344+02', NULL, 1);
INSERT INTO "Telefono" VALUES (66, '1232323', 0, NULL, '2014-05-18 07:44:23.685+02', NULL, NULL, '2014-05-18 07:44:23.685+02', NULL, 1);
INSERT INTO "Telefono" VALUES (55, '30244654', 0, NULL, '2014-05-18 05:34:36.071+02', NULL, '', '2014-05-18 19:52:41.042+02', NULL, 0);
INSERT INTO "Telefono" VALUES (68, '30244654', 0, NULL, '2014-05-18 19:52:41.042+02', NULL, NULL, '2014-05-18 19:52:41.042+02', NULL, 1);
INSERT INTO "Telefono" VALUES (67, '2312323', 0, NULL, '2014-05-18 07:55:27.999+02', NULL, '', '2014-05-21 04:28:25.32+02', NULL, 0);
INSERT INTO "Telefono" VALUES (83, '3123231', 0, NULL, '2014-06-22 06:28:05.685+02', NULL, NULL, '2014-06-22 06:28:05.685+02', NULL, 1);
INSERT INTO "Telefono" VALUES (69, '2312323', 0, NULL, '2014-05-21 04:28:25.32+02', NULL, '', '2014-05-21 05:02:18.967+02', NULL, 0);
INSERT INTO "Telefono" VALUES (71, '2312323', 0, NULL, '2014-05-21 05:02:18.967+02', NULL, NULL, '2014-05-21 05:02:18.967+02', NULL, 1);
INSERT INTO "Telefono" VALUES (73, '966720314', 1, NULL, '2014-06-04 05:11:02.109+02', NULL, NULL, '2014-06-04 05:11:02.109+02', NULL, 1);
INSERT INTO "Telefono" VALUES (72, '966720314', 0, NULL, '2014-06-04 05:11:02.109+02', NULL, '', '2014-06-04 05:12:19.966+02', NULL, 0);
INSERT INTO "Telefono" VALUES (75, '966720314', 1, NULL, '2014-06-04 05:12:19.966+02', NULL, NULL, '2014-06-04 05:12:19.966+02', NULL, 1);
INSERT INTO "Telefono" VALUES (74, '966720314', 0, NULL, '2014-06-04 05:12:19.966+02', NULL, '', '2014-06-04 05:13:24.112+02', NULL, 0);
INSERT INTO "Telefono" VALUES (77, '966720314', 1, NULL, '2014-06-04 05:13:24.112+02', NULL, NULL, '2014-06-04 05:13:24.112+02', NULL, 1);
INSERT INTO "Telefono" VALUES (76, '966720314', 0, NULL, '2014-06-04 05:13:24.112+02', NULL, '', '2014-06-04 05:15:24.068+02', NULL, 0);
INSERT INTO "Telefono" VALUES (78, '966720314', 0, NULL, '2014-06-04 05:15:24.068+02', NULL, NULL, '2014-06-04 05:15:24.068+02', NULL, 1);
INSERT INTO "Telefono" VALUES (79, '966720314', 1, NULL, '2014-06-04 05:15:24.068+02', NULL, NULL, '2014-06-04 05:15:24.068+02', NULL, 1);
INSERT INTO "Telefono" VALUES (80, '3456789', 0, NULL, '2014-06-04 05:23:48.989+02', NULL, NULL, '2014-06-04 05:23:48.989+02', NULL, 1);
INSERT INTO "Telefono" VALUES (63, '3123231', 0, NULL, '2014-05-18 07:11:20.371+02', NULL, '', '2014-06-21 18:38:26.081+02', NULL, 0);
INSERT INTO "Telefono" VALUES (84, '2344342', 1, NULL, '2014-06-22 06:28:05.685+02', NULL, NULL, '2014-06-22 06:28:05.685+02', NULL, 1);
INSERT INTO "Telefono" VALUES (85, '1457892', 0, NULL, '2014-06-22 06:32:18.505+02', NULL, NULL, '2014-06-22 06:32:18.505+02', NULL, 1);
INSERT INTO "Telefono" VALUES (86, '487656556', 2, NULL, '2014-06-22 06:32:18.505+02', NULL, NULL, '2014-06-22 06:32:18.505+02', NULL, 1);

INSERT INTO "TelefonoDireccion" VALUES (12, 2, 1);
INSERT INTO "TelefonoDireccion" VALUES (13, 2, 1);
INSERT INTO "TelefonoDireccion" VALUES (24, 5, 1);
INSERT INTO "TelefonoDireccion" VALUES (25, 5, 1);
INSERT INTO "TelefonoDireccion" VALUES (34, 20, 1);
INSERT INTO "TelefonoDireccion" VALUES (35, 20, 1);
INSERT INTO "TelefonoDireccion" VALUES (38, 21, 1);
INSERT INTO "TelefonoDireccion" VALUES (41, 23, 1);
INSERT INTO "TelefonoDireccion" VALUES (43, 24, 1);
INSERT INTO "TelefonoDireccion" VALUES (44, 25, 1);
INSERT INTO "TelefonoDireccion" VALUES (46, 27, 1);
INSERT INTO "TelefonoDireccion" VALUES (48, 29, 1);
INSERT INTO "TelefonoDireccion" VALUES (50, 30, 1);
INSERT INTO "TelefonoDireccion" VALUES (51, 30, 1);
INSERT INTO "TelefonoDireccion" VALUES (55, 33, 1);
INSERT INTO "TelefonoDireccion" VALUES (57, 35, 1);
INSERT INTO "TelefonoDireccion" VALUES (59, 36, 1);
INSERT INTO "TelefonoDireccion" VALUES (60, 36, 1);
INSERT INTO "TelefonoDireccion" VALUES (63, 38, 1);
INSERT INTO "TelefonoDireccion" VALUES (65, 39, 1);
INSERT INTO "TelefonoDireccion" VALUES (66, 40, 1);
INSERT INTO "TelefonoDireccion" VALUES (67, 41, 1);
INSERT INTO "TelefonoDireccion" VALUES (68, 42, 1);
INSERT INTO "TelefonoDireccion" VALUES (69, 43, 1);
INSERT INTO "TelefonoDireccion" VALUES (71, 45, 1);
INSERT INTO "TelefonoDireccion" VALUES (72, 46, 1);
INSERT INTO "TelefonoDireccion" VALUES (74, 47, 1);
INSERT INTO "TelefonoDireccion" VALUES (76, 48, 1);
INSERT INTO "TelefonoDireccion" VALUES (78, 49, 1);
INSERT INTO "TelefonoDireccion" VALUES (80, 50, 1);
INSERT INTO "TelefonoDireccion" VALUES (81, 51, 1);
INSERT INTO "TelefonoDireccion" VALUES (83, 52, 1);
INSERT INTO "TelefonoDireccion" VALUES (85, 53, 1);

INSERT INTO "TelefonoPersona" VALUES (14, 11, 1);
INSERT INTO "TelefonoPersona" VALUES (15, 11, 1);
INSERT INTO "TelefonoPersona" VALUES (26, 17, 1);
INSERT INTO "TelefonoPersona" VALUES (27, 17, 1);
INSERT INTO "TelefonoPersona" VALUES (30, 20, 1);
INSERT INTO "TelefonoPersona" VALUES (31, 21, 1);
INSERT INTO "TelefonoPersona" VALUES (32, 22, 1);
INSERT INTO "TelefonoPersona" VALUES (33, 23, 1);
INSERT INTO "TelefonoPersona" VALUES (36, 24, 1);
INSERT INTO "TelefonoPersona" VALUES (37, 24, 1);
INSERT INTO "TelefonoPersona" VALUES (39, 26, 1);
INSERT INTO "TelefonoPersona" VALUES (40, 28, 1);
INSERT INTO "TelefonoPersona" VALUES (42, 30, 1);
INSERT INTO "TelefonoPersona" VALUES (45, 34, 1);
INSERT INTO "TelefonoPersona" VALUES (47, 36, 1);
INSERT INTO "TelefonoPersona" VALUES (49, 37, 1);
INSERT INTO "TelefonoPersona" VALUES (52, 38, 1);
INSERT INTO "TelefonoPersona" VALUES (53, 38, 1);
INSERT INTO "TelefonoPersona" VALUES (54, 39, 1);
INSERT INTO "TelefonoPersona" VALUES (56, 41, 1);
INSERT INTO "TelefonoPersona" VALUES (58, 42, 1);
INSERT INTO "TelefonoPersona" VALUES (61, 43, 1);
INSERT INTO "TelefonoPersona" VALUES (62, 43, 1);
INSERT INTO "TelefonoPersona" VALUES (64, 45, 1);
INSERT INTO "TelefonoPersona" VALUES (73, 56, 1);
INSERT INTO "TelefonoPersona" VALUES (75, 57, 1);
INSERT INTO "TelefonoPersona" VALUES (77, 58, 1);
INSERT INTO "TelefonoPersona" VALUES (79, 59, 1);
INSERT INTO "TelefonoPersona" VALUES (82, 63, 1);
INSERT INTO "TelefonoPersona" VALUES (84, 64, 1);
INSERT INTO "TelefonoPersona" VALUES (86, 66, 1);


INSERT INTO "CorreoElectronico" VALUES (1, 'viajes@terranova.com.pe', 20, 'admin', '2014-05-05 01:26:56.602+02', '127.0.0.1', 'admin', '2014-05-05 01:26:56.602+02', '127.0.0.1', 1);
INSERT INTO "CorreoElectronico" VALUES (2, 'viajes@terranova.com.pe', 21, 'admin', '2014-05-05 01:27:34.348+02', '127.0.0.1      ', 'admin', '2014-05-05 01:27:34.348+02', '127.0.0.1      ', 1);
INSERT INTO "CorreoElectronico" VALUES (3, 'viajes@terranova.com.pe', 22, 'admin', '2014-05-05 01:29:12.662+02', '127.0.0.1      ', 'admin', '2014-05-05 01:29:12.662+02', '127.0.0.1      ', 1);
INSERT INTO "CorreoElectronico" VALUES (4, 'viajes@terranova.com.pe', 23, 'admin', '2014-05-05 06:39:30.68+02', '127.0.0.1', 'admin', '2014-05-05 06:39:30.68+02', '127.0.0.1', 1);
INSERT INTO "CorreoElectronico" VALUES (5, 'dfdfsdfsf@viajes.com.pe', 26, 'admin', '2014-05-08 06:29:39.062+02', '127.0.0.1', 'admin', '2014-05-08 06:29:39.062+02', '127.0.0.1', 1);
INSERT INTO "CorreoElectronico" VALUES (6, 'victor@andes.com.pe', 32, 'admin', '2014-05-17 23:24:44.088+02', '127.0.0.1', 'admin', '2014-05-17 23:24:44.088+02', '127.0.0.1', 1);
INSERT INTO "CorreoElectronico" VALUES (7, 'victor@andes.com.pe', 33, 'admin', '2014-05-17 23:54:37.44+02', '127.0.0.1      ', 'admin', '2014-05-17 23:54:37.44+02', '127.0.0.1      ', 1);
INSERT INTO "CorreoElectronico" VALUES (8, 'VIAJES@TERRANOVA.COM.PE', 34, 'ADMIN', '2014-05-18 02:30:47.824+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 02:30:47.824+02', '127.0.0.1      ', 1);
INSERT INTO "CorreoElectronico" VALUES (9, 'VICTOR@ANDES.COM.PE', 35, 'ADMIN', '2014-05-18 02:31:03.257+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 02:31:03.257+02', '127.0.0.1      ', 1);
INSERT INTO "CorreoElectronico" VALUES (10, 'DFDFSDFSF@VIAJES.COM.PE', 37, 'ADMIN', '2014-05-18 02:31:23.221+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 02:31:23.221+02', '127.0.0.1      ', 1);
INSERT INTO "CorreoElectronico" VALUES (11, 'VIAJES@TERRANOVA.COM.PE', 39, 'ADMIN', '2014-05-18 05:34:30.32+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 05:34:30.32+02', '127.0.0.1      ', 1);
INSERT INTO "CorreoElectronico" VALUES (12, 'VICTOR@ANDES.COM.PE', 40, 'ADMIN', '2014-05-18 05:34:36.071+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 05:34:36.071+02', '127.0.0.1      ', 1);
INSERT INTO "CorreoElectronico" VALUES (13, 'DFDFSDFSF@VIAJES.COM.PE', 42, 'ADMIN', '2014-05-18 05:34:51.979+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 05:34:51.979+02', '127.0.0.1      ', 1);
INSERT INTO "CorreoElectronico" VALUES (14, 'ASDADASD@DSFFD.COM', 47, 'ADMIN', '2014-05-18 07:39:09.344+02', '127.0.0.1', 'ADMIN', '2014-05-18 07:39:09.344+02', '127.0.0.1', 1);
INSERT INTO "CorreoElectronico" VALUES (15, 'VICTOR@ANDES.COM.PE', 52, 'ADMIN', '2014-05-18 19:52:41.042+02', '127.0.0.1      ', 'ADMIN', '2014-05-18 19:52:41.042+02', '127.0.0.1      ', 1);
INSERT INTO "CorreoElectronico" VALUES (16, 'PHUARACHI@VIAJESTERRANOVA.COM', 56, 'PAOLA', '2014-06-04 05:11:02.109+02', '192.168.1.13', 'PAOLA', '2014-06-04 05:11:02.109+02', '192.168.1.13', 1);
INSERT INTO "CorreoElectronico" VALUES (17, 'PHUARACHI@VIAJESTERRANOVA.COM', 57, 'PAOLA', '2014-06-04 05:12:19.966+02', '192.168.1.13   ', 'PAOLA', '2014-06-04 05:12:19.966+02', '192.168.1.13   ', 1);
INSERT INTO "CorreoElectronico" VALUES (18, 'PHUARACHI@VIAJESTERRANOVA.COM', 58, 'PAOLA', '2014-06-04 05:13:24.112+02', '192.168.1.13   ', 'PAOLA', '2014-06-04 05:13:24.112+02', '192.168.1.13   ', 1);
INSERT INTO "CorreoElectronico" VALUES (19, 'PHUARACHI@VIAJESTERRANOVA.COM', 59, 'PAOLA', '2014-06-04 05:15:24.068+02', '192.168.1.13   ', 'PAOLA', '2014-06-04 05:15:24.068+02', '192.168.1.13   ', 1);


SELECT pg_catalog.setval('seq_correoelectronico', 19, true);

SELECT pg_catalog.setval('seq_direccion', 53, true);

SELECT pg_catalog.setval('seq_novios', 1, false);

SELECT pg_catalog.setval('seq_persona', 66, true);

SELECT pg_catalog.setval('seq_telefono', 86, true);