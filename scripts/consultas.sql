--select * from empresas;
--select * from productos where empresa_id = 'a5a53323-a969-4d0c-ade4-1ff0764c42d7';

select p.id, p.nombre_producto, a.cantidad, c.cantidad from inventario_actual a, inventario_actual_costo c, productos p
where a.empresa_id = 'a5a53323-a969-4d0c-ade4-1ff0764c42d7'
AND c.empresa_id = 'a5a53323-a969-4d0c-ade4-1ff0764c42d7'
AND c.producto_id = a.producto_id
AND c.cantidad <> a.cantidad
AND c.activo;

select * from inventario_actual where producto_id = '6295e79e-23fe-4e8a-bb67-0c6a31157930';
select * from inventario_actual_costo where producto_id = '6295e79e-23fe-4e8a-bb67-0c6a31157930';