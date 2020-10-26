ALTER TABLE public.vistas
    ADD COLUMN view_root boolean NOT NULL DEFAULT False;

COMMENT ON COLUMN public.vistas.view_root
    IS 'Campo para validar si vista es Root o Publica';

update public.vistas SET view_root = true WHERE nombre_vista = 'Administrar Empresas';

