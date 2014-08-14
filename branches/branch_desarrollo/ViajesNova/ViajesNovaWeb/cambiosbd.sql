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
