/**
Script para actualizar tabla de ventas y registrar la fecha de aplicar la venta.
*/
ALTER TABLE public.ventas
    ADD COLUMN fecha_venta_efectiva timestamp without time zone;

update ventas set  fecha_venta_efectiva = fecha_creacion where fecha_venta_efectiva IS NULL;
commit;