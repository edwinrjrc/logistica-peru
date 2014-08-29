CREATE OR REPLACE FUNCTION negocio.fn_listarcorreoscontactos()
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin

open micursor for
select cor.correo, con.*
  from negocio."CorreoElectronico" cor,
       negocio."Persona" con
 where cor.idestadoregistro = 1
   and con.idestadoregistro = 1
   and con.idtipopersona    = 3
   and cor.idpersona        = con.id;


return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
select percon.idcontacto, cli.*
  from negocio.vw_clientesnova cli,
       negocio."PersonaContactoProveedor" percon,
       negocio."CorreoElectronico" cor
 where percon.idestadoregistro = 1
   and percon.idproveedor = cli.id
   and cor.idestadoregistro = 1
   and cor.idpersona = percon.idcontacto
   and (select count(1) from negocio.vw_consultacontacto where id = percon.idcontacto) >0;
   
CREATE OR REPLACE FUNCTION negocio.fn_tienecorreo(p_idpersona integer)
  RETURNS boolean AS
$BODY$

declare cantidad integer;

begin

select count(1)
  into cantidad
  from negocio."PersonaContactoProveedor" percon,
       negocio."CorreoElectronico" cor
 where percon.idestadoregistro = 1 
   and percon.idcontacto       = cor.idpersona
   and percon.idproveedor      = p_idpersona;

if cantidad > 0 then
return true;
else
return false;
end if;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  

CREATE OR REPLACE FUNCTION negocio.fn_listarclientescorreo()
  RETURNS refcursor AS
$BODY$

declare micursor refcursor;

begin

open micursor for
select cli.*
  from negocio.vw_clientesnova cli
 where negocio.fn_tienecorreo(cast(cli.id as integer)) = true;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;