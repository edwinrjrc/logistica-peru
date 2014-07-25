-- Function: negocio.fn_proveedorxservicio(integer)

-- DROP FUNCTION negocio.fn_proveedorxservicio(integer);

CREATE OR REPLACE FUNCTION negocio.fn_proveedorxservicio(p_idservicio integer)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin

open micursor for
SELECT per.id, per.nombres, proser.porcencomision, proser.porcenfee
  FROM negocio."Persona" per,
       negocio."ProveedorTipoServicio" proser
 WHERE per.idestadoregistro    = 1 
   AND proser.idestadoregistro = 1 
   AND per.idtipopersona       = 2
   AND per.id                  = proser.idproveedor
   AND proser.idservicio       = p_idservicio;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
