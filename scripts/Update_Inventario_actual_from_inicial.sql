update inventario_actual_costo as costo
set cantidad = i.cantidad, manage_stock = i.manage_stock,
precio_costo = i.precio_costo
from inventario_inicial i
where costo.producto_id = i.producto_id AND i.producto_id IN
(select id from productos);

update inventario_actual as costo
set cantidad = i.cantidad, manage_stock = i.manage_stock
from inventario_inicial i
where costo.producto_id = i.producto_id AND i.producto_id IN
(select id from productos);