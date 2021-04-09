package com.gigti.xfinance.backend.business;

import com.gigti.xfinance.backend.data.Compra;
import com.gigti.xfinance.backend.data.CompraItem;
import com.gigti.xfinance.backend.repositories.CompraItemRepository;
import com.gigti.xfinance.backend.repositories.CompraRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CompraBusinessServiceImpl implements CompraBusinessService {
    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private CompraItemRepository compraItemRepository;

    @Override
    @Transactional
    public Compra saveCompra(Compra compra) {
        return compraRepository.save(compra);
    }

    @Override
    @Transactional
    public CompraItem saveCompraItem(CompraItem item) {
        return compraItemRepository.save(item);
    }

    
}
