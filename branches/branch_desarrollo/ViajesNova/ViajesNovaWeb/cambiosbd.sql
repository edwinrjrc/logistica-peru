drop table negocio."ServicioNovios";

CREATE TABLE negocio."ProgramaNovios"
(
  id integer NOT NULL,
  codigonovios character varying(20) NOT NULL,
  idnovia integer NOT NULL,
  idnovio integer NOT NULL,
  iddestino integer NOT NULL,
  fechaboda date NOT NULL,
  fechaviaje date NOT NULL,
  idmoneda integer NOT NULL,
  cuotainicial double precision NOT NULL,
  dias integer NOT NULL,
  noches integer NOT NULL,
  fechashower date,
  observaciones text,
  montosubtotal double precision NOT NULL,
  montoigv double precision NOT NULL,
  porcentajeigv double precision NOT NULL,
  montototal double precision NOT NULL,
  usuariocreacion character varying(15),
  fechacreacion timestamp with time zone,
  ipcreacion character varying(15),
  usuariomodificacion character varying(15),
  fechamodificacion timestamp with time zone,
  ipmodificacion character varying(15),
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_servicionovios PRIMARY KEY (id)
);

-- Function: negocio.fn_ingresarprogramanovios(integer, integer, integer, date, date, integer, double precision, integer, integer, date, text, double precision, double precision, double precision, double precision, character varying, character varying)

-- DROP FUNCTION negocio.fn_ingresarprogramanovios(integer, integer, integer, date, date, integer, double precision, integer, integer, date, text, double precision, double precision, double precision, double precision, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_ingresarprogramanovios(p_idnovia integer, p_idnovio integer, p_iddestino integer, p_fechaboda date, p_fechaviaje date, p_idmoneda integer, p_cuotainicial double precision, p_dias integer, p_noches integer, p_fechashower date, p_observaciones text, p_montosubtotal double precision, p_montoigv double precision, p_porcentajeigv double precision, p_montototal double precision, p_usuariocreacion character varying, p_ipcreacion character varying)
  RETURNS integer AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;
declare cod_novio character varying(20);

Begin

maxid = nextval('negocio.seq_novios');
select current_timestamp into fechahoy;
cod_novio = negocio.fn_generarcodigonovio(maxid,p_usuariocreacion);

INSERT INTO negocio."ProgramaNovios"(
            id, codigonovios, idnovia, idnovio, iddestino, fechaboda, fechaviaje, 
            idmoneda, cuotainicial, dias, noches, fechashower, observaciones, 
            montosubtotal, montoigv, porcentajeigv, montototal, usuariocreacion, 
            fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
            ipmodificacion)
    VALUES (maxid, cod_novio, p_idnovia, p_idnovio, p_iddestino, p_fechaboda, p_fechaviaje, p_idmoneda, p_cuotainicial, p_dias, p_noches, p_fechashower, 
	    p_observaciones, p_montosubtotal, p_montoigv, p_porcentajeigv, p_montototal, p_usuariocreacion, 
	    fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion);

return maxid;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
  
CREATE SEQUENCE negocio.seq_serviciosnovios
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

  
-- Function: negocio.fn_ingresarserviciosnovios(integer, integer, character varying, integer, double precision, double precision, character varying, character varying)

-- DROP FUNCTION negocio.fn_ingresarserviciosnovios(integer, integer, character varying, integer, double precision, double precision, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_ingresarserviciosnovios(p_idnovios integer, p_idtiposervicio integer, p_descripcion character varying, p_cantidad integer, p_preciounitario double precision, p_totalservicio double precision, p_usuariocreacion character varying, p_ipcreacion character varying)
  RETURNS boolean AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;

Begin

maxid = nextval('negocio.seq_serviciosnovios');
select current_timestamp into fechahoy;

INSERT INTO negocio."ServiciosProgramaNovios"(
            id, idnovios, idtiposervicio, descripcion, cantidad, preciounitario, 
            totalservicio, usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, 
            fechamodificacion, ipmodificacion)
    VALUES (maxid, p_idnovios, p_idtiposervicio, p_descripcion, p_cantidad, p_preciounitario, 
            p_totalservicio, p_usuariocreacion, fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion);

return true;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
 -- Table: negocio."Personapotencial"

-- DROP TABLE negocio."Personapotencial";

CREATE TABLE negocio."Personapotencial"
(
  id integer NOT NULL,
  nombres character varying(50) NOT NULL,
  apellidopaterno character varying(20) NOT NULL,
  apellidomaterno character varying(20),
  telefono character varying(12),
  correoelectronico character varying(100),
  idnovios integer NOT NULL,
  fecnacimiento date,
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1
)
WITH (
  OIDS=FALSE
);
ALTER TABLE negocio."Personapotencial"
  OWNER TO postgres;
