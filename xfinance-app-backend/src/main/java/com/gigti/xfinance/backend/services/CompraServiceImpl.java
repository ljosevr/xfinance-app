package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Compra;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.repositories.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CompraServiceImpl implements CompraService {

    private static final Logger logger = Logger.getLogger(CompraServiceImpl.class.getName());

    @Autowired
    private CompraRepository compraRepository;

    @Override
    public int count(String filterText, Empresa empresa, LocalDate dateStart, LocalDate dateEnd) {
        int count;
        dateStart = dateStart == null ? LocalDate.of(2020,1,1) : dateStart;
        dateEnd = dateEnd == null ? LocalDate.now() : dateEnd;
        if(filterText == null || filterText.isEmpty()) {
            count = compraRepository.countAllByEmpresa(empresa,
                    java.sql.Date.valueOf(dateStart),
                    java.sql.Date.valueOf(dateEnd));
        } else  {
            count = compraRepository.countAllByEmpresaAndNumeroFactura(empresa,
                    filterText,
                    java.sql.Date.valueOf(dateStart),
                    java.sql.Date.valueOf(dateEnd));
        }

        return count;
    }

    @Override
    public List<Compra> findAll(String filterText, Empresa empresa, LocalDate dateStart, LocalDate dateEnd, int page, int size) {
        String methodName = "findAllCompra";
        logger.info("--> "+methodName);
        Pageable pageable = PageRequest.of(page, size);
        dateStart = dateStart == null ? LocalDate.of(2020,1,1) : dateStart;
        dateEnd = dateEnd == null ? LocalDate.now() : dateEnd;

        List<Compra> listResult;
        if(filterText == null || filterText.isEmpty()) {
            listResult = compraRepository.findAllByEmpresa(empresa,
                    java.sql.Date.valueOf(dateStart),
                    java.sql.Date.valueOf(dateEnd),
                    pageable);
        } else  {
            listResult = compraRepository.search(empresa,
                    filterText,
                    java.sql.Date.valueOf(dateStart),
                    java.sql.Date.valueOf(dateEnd),
                    pageable);
        }

        listResult.forEach(c -> c.setTotalFactura(BigDecimal.valueOf(c.getItems().stream().mapToDouble(i -> i.getPrecioTotalVenta().doubleValue()).sum())));

        return listResult;
    }

    @Override
    public Response saveCompra(Compra compra, Empresa empresa, Usuario usuario) {
        return null;
    }
}
