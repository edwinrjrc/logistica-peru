-- Function: seguridad.fn_cambiarclaveusuario(character varying, character varying, character varying)

-- DROP FUNCTION seguridad.fn_cambiarclaveusuario(character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION seguridad.fn_cambiarclaveusuario(p_usuario character varying, p_credencialactual character varying, p_credencialnueva character varying)
  RETURNS boolean AS
$BODY$

declare idusuario integer;

begin

select COALESCE(max(id),0) 
  into idusuario
  from seguridad.usuario
 where usuario    = p_usuario
   and credencial = p_credencialactual;

if idusuario = 0 then
 raise exception 'Informacion de usuario incorrecta';
else
update seguridad.usuario
   set credencial = p_credencialactual
 where id 	  = idusuario;
end if;

return true;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION seguridad.fn_cambiarclaveusuario(character varying, character varying, character varying) OWNER TO postgres;


-- Function: seguridad.fn_iniciosesion(character varying, character varying)

-- DROP FUNCTION seguridad.fn_iniciosesion(character varying, character varying);

CREATE OR REPLACE FUNCTION seguridad.fn_iniciosesion(p_usuario character varying, p_credencial character varying)
  RETURNS boolean AS
$BODY$

declare cantidad integer;

begin

cantidad = 0;

select count(1)
  into cantidad
  from seguridad.usuario usr
 where upper(usr.usuario) = p_usuario
   and usr.credencial     = p_credencial;

if cantidad = 1 then
   return true;
else
   return false;
end if;

exception
when others then
  return false;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

  
  ALTER TABLE seguridad.usuario ALTER credencial TYPE character varying(50);
  
  DROP VIEW seguridad.vw_listarusuarios;
  
  CREATE OR REPLACE VIEW seguridad.vw_listarusuarios AS 
 SELECT u.id, u.usuario, u.credencial, u.id_rol, r.nombre, u.nombres, u.apepaterno, u.apematerno
   FROM seguridad.usuario u, seguridad.rol r
  WHERE u.id_rol = r.id;

ALTER TABLE seguridad.vw_listarusuarios OWNER TO postgres;

ALTER TABLE seguridad.usuario ADD CONSTRAINT uq_usuario UNIQUE (usuario);