-- Function: reportes.fn_re_generalventas(date, date)

-- DROP FUNCTION reportes.fn_re_generalventas(date, date);

CREATE OR REPLACE FUNCTION reportes.fn_totalfee(p_desde date, p_hasta date, p_idtiposervicio integer)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin

open micursor for
SELECT SUM(sd.montototal) as montototal
  FROM negocio."ServicioCabecera" sc
 INNER JOIN negocio."ServicioDetalle" sd  ON sd.idservicio     = sc.id AND sd.idestadoregistro = 1
 INNER JOIN negocio."MaestroServicios" ms ON sd.idtiposervicio = ms.id AND sd.idestadoregistro = 1 AND ms.esfee = TRUE
 WHERE sc.idestadoservicio = 2
   AND sc.idestadoregistro = 1
   AND sd.idtiposervicio   = p_idtiposervicio
   AND sc.fechacompra BETWEEN p_desde AND p_hasta;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- Function: reportes.fn_re_generalventas(date, date)

-- DROP FUNCTION reportes.fn_re_generalventas(date, date);

CREATE OR REPLACE FUNCTION reportes.fn_re_generalventas(p_desde date, p_hasta date)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin

open micursor for
SELECT sd.idtiposervicio, ms.nombre, COUNT(sd.id) AS cantidad, SUM(sd.montototal) as montototal, SUM(montocomision) as montocomision
  FROM negocio."ServicioCabecera" sc
 INNER JOIN negocio."ServicioDetalle" sd  ON sd.idservicio     = sc.id AND sd.idestadoregistro = 1
 INNER JOIN negocio."MaestroServicios" ms ON sd.idtiposervicio = ms.id AND sd.idestadoregistro = 1 AND ms.visible = TRUE
 WHERE sc.idestadoservicio = 2
   AND sc.idestadoregistro = 1
   AND sc.fechacompra BETWEEN p_desde AND p_hasta
 GROUP BY sd.idtiposervicio, ms.nombre;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION reportes.fn_re_generalventas(date, date)
  OWNER TO postgres;
