
CREATE TABLE negocio."Personapotencial"
(
  id integer NOT NULL,
  nombres character varying(50) NOT NULL,
  apellidopaterno character varying(20) NOT NULL,
  apellidomaterno character varying(20),
  fecnacimiento date,
  telefono character varying(12),
  correoelectronico character varying(100),
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1
);

DROP FUNCTION negocio.fn_ingresarservicionovios(integer, integer, integer, date, integer, double precision, integer, integer, date, text, character varying, character varying);


CREATE OR REPLACE FUNCTION negocio.fn_ingresarservicionovios(p_idnovia integer, p_idnovio integer, p_iddestino integer, p_fechaboda date, p_fechaviaje date, p_idmoneda integer, p_cuotainicial double precision, p_dias integer, p_noches integer, p_fechashower date, p_observaciones text, p_usuariocreacion character varying, p_ipcreacion character varying)
  RETURNS integer AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;
declare cod_novio character varying(5);

Begin

maxid = nextval('negocio.seq_novios');
select current_timestamp into fechahoy;
cod_novio = negocio.fn_generarcodigonovio(maxid,p_usuariocreacion);

INSERT INTO negocio."ServicioNovios"(
            id, codigonovios, idnovia, idnovio, iddestino, fechaboda, fechaviaje, idmoneda, 
            cuotainicial, dias, noches, fechashower, observaciones, usuariocreacion, 
            fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
            ipmodificacion)
    VALUES (maxid, cod_novio, p_idnovia, p_idnovio, p_iddestino, p_fechaboda, p_fechaviaje, p_idmoneda, p_cuotainicial, p_dias, p_noches, p_fechashower, 
    p_observaciones, p_usuariocreacion, 
	    fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion);

return maxid;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
  
-- Function: negocio.fn_ingresainvitado(character varying, character varying, character varying, date, character varying, character varying, integer, character varying, character varying)

-- DROP FUNCTION negocio.fn_ingresainvitado(character varying, character varying, character varying, date, character varying, character varying, integer, character varying, character varying);

CREATE OR REPLACE FUNCTION negocio.fn_ingresainvitado(p_nombres character varying, p_apellidopaterno character varying, p_apellidomaterno character varying, p_fecnacimiento date, p_telefono character varying, p_correoelectronico character varying, p_idnovios integer, p_usuariocreacion character varying, p_ipcreacion character varying)
  RETURNS boolean AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;

begin

maxid = nextval('negocio.seq_personapotencial');

select current_timestamp into fechahoy;

INSERT INTO negocio."Personapotencial"(
            id, nombres, apellidopaterno, apellidomaterno, 
            fecnacimiento, telefono, correoelectronico, idnovios, usuariocreacion, 
            fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
            ipmodificacion)
    VALUES (maxid, p_nombres, p_apellidopaterno, p_apellidomaterno, 
            p_fecnacimiento, p_telefono, p_correoelectronico, p_idnovios, p_usuariocreacion, 
            fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, 
            p_ipcreacion);

return true;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
  
-- Function: negocio.fn_generarcodigonovio(integer)

-- DROP FUNCTION negocio.fn_generarcodigonovio(integer);

CREATE OR REPLACE FUNCTION negocio.fn_generarcodigonovio(p_codigosnovios integer, p_usuario character varying)
  RETURNS character varying AS
$BODY$

declare cod_novio character varying(5);
declare fechaserie character varying(4);

Begin


select to_char(fecnacimiento,'ddMM')
  into fechaserie
  from seguridad.usuario
 where usuario = p_usuario;

cod_novio = fechaserie || p_codigosnovios;

return cod_novio;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  


CREATE OR REPLACE FUNCTION negocio.fn_consultarnovios(p_id integer, p_codnovios character varying, p_idnovia integer, p_idnovio integer)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin

open micursor for
SELECT snov.id, snov.codigonovios, 
       snov.idnovia, novia.nombres as nomnovia, novia.apellidopaterno as apepatnovia, novia.apellidomaterno as apematnovia,
       snov.idnovio, novio.nombres as nomnovio, novio.apellidopaterno as apepatnovio, novio.apellidomaterno as apematnovio,
       snov.iddestino, dest.descripcion as descdestino, dest.codigoiata, pai.descripcion as descpais,
       snov.fechaboda, snov.fechaviaje, 
       snov.idmoneda, snov.cuotainicial, snov.dias, snov.noches, snov.fechashower, snov.observaciones, 
       snov.usuariocreacion, snov.fechacreacion, snov.ipcreacion, snov.usuariomodificacion, 
       snov.fechamodificacion, snov.ipmodificacion,
       (select count(1) from negocio."Personapotencial" where idnovios = snov.id) as cantidadInvitados
  FROM negocio."ServicioNovios" snov,
       negocio."Persona" novia,
       negocio."Persona" novio,
       soporte.destino dest,
       soporte.pais pai
 WHERE snov.idestadoregistro  = 1
   AND snov.id                = COALESCE(p_id,snov.id)
   AND novia.idestadoregistro = 1
   AND novia.idtipopersona    = 1
   AND novia.id               = snov.idnovia
   AND snov.idnovia           = COALESCE(p_idnovia,snov.idnovia)
   AND novio.idestadoregistro = 1
   AND novio.idtipopersona    = 1
   AND novio.id               = snov.idnovio
   AND snov.idnovio           = COALESCE(p_idnovio,snov.idnovio)
   AND dest.idestadoregistro  = 1
   AND dest.id                = snov.iddestino
   AND pai.idestadoregistro   = 1
   AND dest.idpais            = pai.id;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

  -- Table: negocio."ServicioNovios"

-- DROP TABLE negocio."ServicioNovios";

CREATE TABLE negocio."ServicioNovios"
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
  usuariocreacion character varying(15),
  fechacreacion timestamp with time zone,
  ipcreacion character varying(15),
  usuariomodificacion character varying(15),
  fechamodificacion timestamp with time zone,
  ipmodificacion character varying(15),
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_servicionovios PRIMARY KEY (id)
);



CREATE OR REPLACE FUNCTION negocio.fn_generarcodigonovio(p_codigosnovios integer, p_usuario character varying)
  RETURNS character varying AS
$BODY$

declare cod_novio character varying(20);
declare fechaserie character varying(4);

Begin


select to_char(fecnacimiento,'ddMM')
  into fechaserie
  from seguridad.usuario
 where usuario = p_usuario;

cod_novio = fechaserie || p_codigosnovios;

return cod_novio;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION negocio.fn_generarcodigonovio(integer, character varying) OWNER TO postgres;



CREATE OR REPLACE FUNCTION negocio.fn_ingresarservicionovios(p_idnovia integer, p_idnovio integer, p_iddestino integer, p_fechaboda date, p_fechaviaje date, p_idmoneda integer, p_cuotainicial double precision, p_dias integer, p_noches integer, p_fechashower date, p_observaciones text, p_usuariocreacion character varying, p_ipcreacion character varying)
  RETURNS integer AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;
declare cod_novio character varying(20);

Begin

maxid = nextval('negocio.seq_novios');
select current_timestamp into fechahoy;
cod_novio = negocio.fn_generarcodigonovio(maxid,p_usuariocreacion);

INSERT INTO negocio."ServicioNovios"(
            id, codigonovios, idnovia, idnovio, iddestino, fechaboda, fechaviaje, idmoneda, 
            cuotainicial, dias, noches, fechashower, observaciones, usuariocreacion, 
            fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
            ipmodificacion)
    VALUES (maxid, cod_novio, p_idnovia, p_idnovio, p_iddestino, p_fechaboda, p_fechaviaje, p_idmoneda, p_cuotainicial, p_dias, p_noches, p_fechashower, 
    p_observaciones, p_usuariocreacion, 
	    fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion);

return maxid;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION negocio.fn_ingresarservicionovios(integer, integer, integer, date, date, integer, double precision, integer, integer, date, text, character varying, character varying) OWNER TO postgres;
