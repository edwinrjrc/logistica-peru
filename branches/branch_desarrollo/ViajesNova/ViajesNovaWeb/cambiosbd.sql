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