-- Table: negocio."Consolidador"

-- DROP TABLE negocio."Consolidador";

CREATE TABLE negocio."ArchivoCargado"
(
  id integer NOT NULL,
  nombre character varying(100) NOT NULL,
  numeroFilas integer not null,
  numeroColumnas integer not null,
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_archivocargado PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);


CREATE TABLE negocio."DetalleArchivoCargado"
(
  id integer NOT NULL,
  idarchivo integer not null,
  campo1 character varying(100) NOT NULL,
  campo2 character varying(100) NOT NULL,
  campo3 character varying(100) NOT NULL,
  campo4 character varying(100) NOT NULL,
  campo5 character varying(100) NOT NULL,
  campo6 character varying(100) NOT NULL,
  campo7 character varying(100) NOT NULL,
  campo8 character varying(100) NOT NULL,
  campo9 character varying(100) NOT NULL,
  campo10 character varying(100) NOT NULL,
  campo11 character varying(100) NOT NULL,
  campo12 character varying(100) NOT NULL,
  campo13 character varying(100) NOT NULL,
  campo14 character varying(100) NOT NULL,
  campo15 character varying(100) NOT NULL,
  campo16 character varying(100) NOT NULL,
  campo17 character varying(100) NOT NULL,
  campo18 character varying(100) NOT NULL,
  campo19 character varying(100) NOT NULL,
  campo20 character varying(100) NOT NULL,
  seleccionado boolean not null default false,
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_detallearchivocargado PRIMARY KEY (id),
  CONSTRAINT fk_archivodetallearchivo FOREIGN KEY (idarchivo)
      REFERENCES negocio."ArchivoCargado" (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE negocio.seq_archivocargado
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE SEQUENCE negocio.seq_detallearchivocargado
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
-- Function: negocio.fn_ingresardireccion(integer, character varying, character varying, character varying, character varying, character varying, character varying, character, character varying, character varying, character varying, character varying)

CREATE OR REPLACE FUNCTION negocio.fn_ingresararchivocargado(p_nombrearchivo character varying, p_numerofilas integer, p_numerocolumnas character varying, 
p_usuariocreacion character varying, p_ipcreacion character varying)
  RETURNS integer AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;

begin

maxid = nextval('negocio.seq_archivocargado');

select current_timestamp into fechahoy;

INSERT INTO negocio."ArchivoCargado"(
            id, nombre, numerofilas, numerocolumnas, usuariocreacion, fechacreacion, 
            ipcreacion, usuariomodificacion, fechamodificacion, ipmodificacion)
    VALUES (maxid, p_nombrearchivo, p_numerofilas, p_numerocolumnas, p_usuariocreacion, fechahoy, 
            p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion);

return maxid;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;



CREATE OR REPLACE FUNCTION negocio.fn_ingresardetallearchivocargado(p_idarchivo character varying, 
p_campo1 character varying, p_campo2 character varying, p_campo3 character varying, p_campo4 character varying, p_campo5 character varying, 
p_campo6 character varying, p_campo7 character varying, p_campo8 character varying, p_campo9 character varying, p_campo10 character varying, 
p_campo11 character varying, p_campo12 character varying, p_campo13 character varying, p_campo14 character varying, p_campo15 character varying, 
p_campo16 character varying, p_campo17 character varying, p_campo18 character varying, p_campo19 character varying, p_campo20 character varying, 
p_seleccionado boolean, p_usuariocreacion character varying, p_ipcreacion character varying)
  RETURNS integer AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;

begin

maxid = nextval('negocio.seq_detallearchivocargado');

select current_timestamp into fechahoy;

INSERT INTO negocio."DetalleArchivoCargado"(
            id, idarchivo, campo1, campo2, campo3, campo4, campo5, campo6, 
            campo7, campo8, campo9, campo10, campo11, campo12, campo13, campo14, 
            campo15, campo16, campo17, campo18, campo19, campo20, seleccionado, 
            usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, 
            fechamodificacion, ipmodificacion)
    VALUES (maxid, p_idarchivo, p_campo1, p_campo2, p_campo3, p_campo4, p_campo5, p_campo6, 
            p_campo7, p_campo8, p_campo9, p_campo10, p_campo11, p_campo12, p_campo13, p_campo14, 
            p_campo15, p_campo16, p_campo17, p_campo18, p_campo19, p_campo20, p_seleccionado, p_usuariocreacion, 
            fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, 
            p_ipcreacion);

return maxid;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
