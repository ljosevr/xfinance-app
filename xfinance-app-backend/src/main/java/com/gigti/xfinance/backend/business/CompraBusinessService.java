package com.gigti.xfinance.backend.business;

import com.gigti.xfinance.backend.data.Compra;
import com.gigti.xfinance.backend.data.CompraItem;

public interface CompraBusinessService {
    Compra saveCompra(Compra compra);
    CompraItem saveCompraItem(CompraItem item);
}
