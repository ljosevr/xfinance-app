ALTER TABLE public.roles
    ADD COLUMN activo boolean NOT NULL DEFAULT False;

COMMENT ON COLUMN public.roles.activo
    IS 'Campo para validar si esta activo o no el Rol';

update public.roles SET activo = true;