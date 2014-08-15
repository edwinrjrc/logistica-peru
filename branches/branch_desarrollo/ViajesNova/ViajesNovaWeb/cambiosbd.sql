-- Table: negocio."MaestroServicios"

CREATE TABLE negocio."MaestroServicios"
(
  id integer NOT NULL,
  nombre character varying(50) NOT NULL,
  descripcion character varying(300),
  requierefee boolean NOT NULL DEFAULT false,
  pagaimpto boolean NOT NULL DEFAULT false,
  cargacomision boolean NOT NULL DEFAULT false,
  comisionporcen boolean NOT NULL DEFAULT false,
  valorporcomision numeric,
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_maestroservicios PRIMARY KEY (id)
);

CREATE SEQUENCE negocio.seq_maestroservicio
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  

-- Function: negocio.fn_ingresarpais(integer, integer, character varying, character varying, character varying, character varying, character varying, date)

CREATE OR REPLACE FUNCTION negocio.fn_ingresarservicio(p_nombreservicio character varying, 
p_descripcion character varying, 
p_requierefee boolean, p_pagaimpto boolean, p_cargacomision boolean, p_usuariocreacion character varying, 
p_ipcreacion character varying)
  RETURNS integer AS
$BODY$

declare maxid integer;
declare fechahoy timestamp with time zone;

begin

maxid = nextval('negocio.seq_maestroservicio');

select current_timestamp into fechahoy;

INSERT INTO negocio."MaestroServicios"(
            id, nombre, descripcion, requierefee, pagaimpto, cargacomision, 
            usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, 
            fechamodificacion, ipmodificacion)
    VALUES (maxid, p_nombreservicio, p_descripcion, p_requierefee, p_pagaimpto, p_cargacomision, 
            p_usuariocreacion, fechahoy, p_ipcreacion, p_usuariocreacion, fechahoy, p_ipcreacion);

return maxid;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- Function: negocio.fn_ingresarpais(integer, integer, character varying, character varying, character varying, character varying, character varying, date)

CREATE OR REPLACE FUNCTION negocio.fn_actualizarservicio(p_id integer, p_nombreservicio character varying, 
p_descripcion character varying, 
p_requierefee boolean, p_pagaimpto boolean, p_cargacomision boolean, p_usuariomodificacion character varying, p_ipmodificacion character varying)
  RETURNS boolean AS
$BODY$

declare fechahoy timestamp with time zone;

begin

select current_timestamp into fechahoy;

UPDATE negocio."MaestroServicios"
   SET nombre              = p_nombreservicio, 
       descripcion         = p_descripcion, 
       requierefee         = p_requierefee, 
       pagaimpto           = p_pagaimpto, 
       cargacomision       = p_cargacomision, 
       usuariomodificacion = p_usuariomodificacion, 
       fechamodificacion   = fechahoy, 
       ipmodificacion      = p_ipmodificacion
 WHERE id                  = p_id;

return true;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- Function: negocio.fn_ingresarpais(integer, integer, character varying, character varying, character varying, character varying, character varying, date)

CREATE OR REPLACE FUNCTION negocio.fn_consultarservicio(p_idservicio integer)
  RETURNS refcursor AS
$BODY$

declare micursor refcursor;

begin

open micursor for
SELECT id, nombre, descripcion, requierefee, pagaimpto, cargacomision, 
       usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, 
       fechamodificacion, ipmodificacion
  FROM negocio."MaestroServicios"
 WHERE id = p_idservicio;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION negocio.fn_listarmaestroservicios()
  RETURNS refcursor AS
$BODY$

declare micursor refcursor;

begin

open micursor for
SELECT id, nombre, descripcion, requierefee, pagaimpto, cargacomision, 
       usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, 
       fechamodificacion, ipmodificacion
  FROM negocio."MaestroServicios"
 WHERE idestadoregistro = 1;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;