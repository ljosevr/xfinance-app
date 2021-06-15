select * from empresas;
select * from productos where empresa_id = '81a4454c-ab08-41a1-ae38-f96fd8a66453';

select p.id, p.nombre_producto, a.cantidad, c.cantidad from inventario_actual a, inventario_actual_costo c, productos p
where a.empresa_id = '81a4454c-ab08-41a1-ae38-f96fd8a66453'
AND c.empresa_id = '81a4454c-ab08-41a1-ae38-f96fd8a66453'
AND p.empresa_id = '81a4454c-ab08-41a1-ae38-f96fd8a66453'
AND p.id = a.producto_id
AND c.producto_id = a.producto_id
AND c.cantidad <> a.cantidad
AND c.activo;

select * from inventario_actual where producto_id = '81a4454c-ab08-41a1-ae38-f96fd8a66453';
select * from inventario_actual_costo where producto_id = '81a4454c-ab08-41a1-ae38-f96fd8a66453';