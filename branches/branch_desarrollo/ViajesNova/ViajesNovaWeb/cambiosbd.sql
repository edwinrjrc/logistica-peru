-- Function: negocio.fn_consultarservicioventadetalle(integer)

-- DROP FUNCTION negocio.fn_consultarservicioventadetalle(integer);

CREATE OR REPLACE FUNCTION negocio.fn_consultarservicioventadetalle(p_idservicio integer)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin
open micursor for
SELECT serdet.id as idSerdetalle, idtiposervicio, tipser.nombre as nomtipservicio, descripcionservicio, iddestino, 
       descripciondestino, dias, noches, fechaida, fecharegreso, cantidad, 
       preciobase, porcencomision, montocomision, montototal
  FROM negocio."ServicioDetalle" serdet,
       soporte."Tablamaestra" tipser
 WHERE serdet.idestadoregistro = 1
   AND tipser.idmaestro        = 12
   AND tipser.estado           = 'A'
   AND tipser.id               = serdet.idtiposervicio
   AND serdet.idservicio       = p_idservicio;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
