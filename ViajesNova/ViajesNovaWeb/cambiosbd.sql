CREATE OR REPLACE FUNCTION negocio.fn_consultarclientescumple()
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin

open micursor for
select id, idtipopersona, nombres, apellidopaterno, apellidomaterno, 
       idgenero, idestadocivil, idtipodocumento, numerodocumento, usuariocreacion, 
       fechacreacion, ipcreacion, usuariomodificacion, fechamodificacion, 
       ipmodificacion, idestadoregistro, fecnacimiento, nropasaporte, 
       fecvctopasaporte
  from negocio."Persona" p
 where p.idestadoregistro              = 1
   and p.idtipopersona                 = 1
   and to_char(p.fecnacimiento,'ddMM') = to_char(current_date,'ddMM');

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
-- Function: negocio.fn_listarmaestroservicios()

-- DROP FUNCTION negocio.fn_listarmaestroservicios();

CREATE OR REPLACE FUNCTION negocio.fn_listarmaestroservicios()
  RETURNS refcursor AS
$BODY$

declare micursor refcursor;

begin

open micursor for
SELECT id, nombre, desccorta, desclarga, requierefee, pagaimpto, cargacomision
  FROM negocio."MaestroServicios"
 WHERE idestadoregistro = 1;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- Function: negocio.fn_actualizarservicio(integer, character varying, character varying, boolean, boolean, boolean, boolean, boolean, character varying, character varying)


CREATE OR REPLACE FUNCTION negocio.fn_actualizarservicio(p_id integer, p_nombreservicio character varying, 
p_desccorta character varying, p_desclarga character varying, p_requierefee boolean, p_idmaeserfee integer, p_pagaimpto boolean, 
p_idmaeserimpto integer, p_cargacomision boolean, 
p_esimpuesto boolean, p_esfee boolean, p_usuariomodificacion character varying, p_ipmodificacion character varying)
  RETURNS boolean AS
$BODY$

declare fechahoy timestamp with time zone;

begin

select current_timestamp into fechahoy;

UPDATE negocio."MaestroServicios"
   SET nombre              = p_nombreservicio, 
       desccorta           = p_desccorta, 
       desclarga           = p_desclarga, 
       requierefee         = p_requierefee, 
       idmaeserfee         = p_idmaeserfee,
       pagaimpto           = p_pagaimpto, 
       idmaeserimpto       = p_idmaeserimpto,
       cargacomision       = p_cargacomision,
       esimpuesto          = p_esimpuesto,
       esfee	           = p_esfee,
       usuariomodificacion = p_usuariomodificacion, 
       fechamodificacion   = fechahoy, 
       ipmodificacion      = p_ipmodificacion
 WHERE id                  = p_id;

return true;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- Function: negocio.fn_consultarservicio(integer)

-- DROP FUNCTION negocio.fn_consultarservicio(integer);

CREATE OR REPLACE FUNCTION negocio.fn_consultarservicio(p_idservicio integer)
  RETURNS refcursor AS
$BODY$

declare micursor refcursor;

begin

open micursor for
SELECT id, nombre, desccorta, desclarga, requierefee, pagaimpto, cargacomision, esimpuesto, esfee
  FROM negocio."MaestroServicios"
 WHERE id = p_idservicio;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
