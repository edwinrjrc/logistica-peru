-- Function: soporte.fn_listardestinos()

-- DROP FUNCTION soporte.fn_listardestinos();

CREATE OR REPLACE FUNCTION soporte.fn_consultardestino(p_iddestino integer)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin

open micursor for
SELECT id, idcontinente, idpais, codigoiata, idtipodestino, descripcion, 
       usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, 
       fechamodificacion, ipmodificacion, idestadoregistro
  FROM soporte.destino
 WHERE id = p_iddestino;


return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
