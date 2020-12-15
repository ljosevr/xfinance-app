CREATE TABLE public.gastos
(
    id character varying(255) NOT NULL,
    descripcion character varying(1000) NOT NULL,
    fecha timestamp without time zone NOT NULL,
    valor numeric(19, 2) NOT NULL,
    empresa_id character varying(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_gastos_empresa FOREIGN KEY (empresa_id)
        REFERENCES empresas (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
);

