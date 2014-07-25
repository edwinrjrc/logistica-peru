-- Function: seguridad.fn_actualizarclaveusuario(integer, character varying)

-- DROP FUNCTION seguridad.fn_actualizarclaveusuario(integer, character varying);

CREATE OR REPLACE FUNCTION seguridad.fn_actualizarclaveusuario(p_idusuario integer, p_credencialnueva character varying)
  RETURNS boolean AS
$BODY$

declare idusuario integer;

begin


update seguridad.usuario
   set credencial = p_credencialnueva
 where id 	  = p_idusuario;


return true;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION seguridad.fn_actualizarclaveusuario(integer, character varying)
  OWNER TO postgres;
  
  
-- Table: negocio."ProveedorTipoServicio"

-- DROP TABLE negocio."ProveedorTipoServicio";

CREATE TABLE negocio."ProveedorTipoServicio"
(
  idproveedor integer NOT NULL,
  idservicio integer NOT NULL,
  porcencomision numeric NOT NULL,
  porcenfee numeric NOT NULL,
  usuariocreacion character varying(15) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character varying(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character varying(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1
)
WITH (
  OIDS=FALSE
);
ALTER TABLE negocio."ProveedorTipoServicio"
  OWNER TO postgres;

