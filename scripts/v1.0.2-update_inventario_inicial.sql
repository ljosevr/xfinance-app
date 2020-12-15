
ALTER TABLE public.inventario_inicial
    ADD COLUMN definitivo boolean NOT NULL DEFAULT False;

ALTER TABLE public.inventario_inicial
    RENAME infinite TO manage_stock;

ALTER TABLE public.inventario_actual_costo
    RENAME infinite TO manage_stock;

ALTER TABLE public.inventario_actual
    RENAME infinite TO manage_stock;