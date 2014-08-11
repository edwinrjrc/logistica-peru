-- Function: negocio.fn_consultarnovios(integer, character varying, integer, integer)

CREATE OR REPLACE FUNCTION negocio.fn_consultarinvitadosnovios(p_idnovios integer)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin

open micursor for
SELECT id, nombres, apellidopaterno, apellidomaterno, telefono, correoelectronico, 
       fecnacimiento
  FROM negocio."Personapotencial"
 WHERE idnovios = p_idnovios;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;


-- Function: negocio.fn_consultarnovios(integer, character varying, integer, integer)

-- DROP FUNCTION negocio.fn_consultarnovios(integer, character varying, integer, integer);

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
       (select count(1) from negocio."Personapotencial" where idnovios = snov.id) as cantidadInvitados, snov.idservicio
  FROM negocio."ProgramaNovios" snov,
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
