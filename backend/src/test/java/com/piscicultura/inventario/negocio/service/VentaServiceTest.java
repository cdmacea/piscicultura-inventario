package com.piscicultura.inventario.negocio.service;

import com.piscicultura.inventario.common.ReglaNegocioException;
import com.piscicultura.inventario.negocio.dto.DetalleVentaDTO;
import com.piscicultura.inventario.negocio.dto.VentaRequest;
import com.piscicultura.inventario.negocio.dto.VentaResponse;
import com.piscicultura.inventario.negocio.entity.Cliente;
import com.piscicultura.inventario.negocio.entity.Venta;
import com.piscicultura.inventario.negocio.repository.ClienteRepository;
import com.piscicultura.inventario.negocio.repository.VentaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepo;
    @Mock
    private ClienteRepository clienteRepo;

    @InjectMocks
    private VentaService service;

    @Test
    void crearCalculaElTotalComoSumaDeLosSubtotales() {
        Cliente cliente = Cliente.builder().id(1L).nombre("Juan Pérez").build();
        when(ventaRepo.existsByCodigo("VTA-0099")).thenReturn(false);
        when(clienteRepo.findById(1L)).thenReturn(Optional.of(cliente));
        when(ventaRepo.save(any(Venta.class))).thenAnswer(inv -> {
            Venta v = inv.getArgument(0);
            v.setId(10L);
            return v;
        });

        VentaRequest req = new VentaRequest("VTA-0099", 1L, null, "EFECTIVO", List.of(
                new DetalleVentaDTO(null, "Tilapia", new BigDecimal("10"), new BigDecimal("9000")),
                new DetalleVentaDTO(null, "Cachama", new BigDecimal("5"), new BigDecimal("6000"))
        ));

        VentaResponse resp = service.crear(req);

        assertThat(resp.total()).isEqualByComparingTo("120000");
    }

    @Test
    void codigoDeVentaDuplicadoLanzaReglaNegocio() {
        when(ventaRepo.existsByCodigo("VTA-0001")).thenReturn(true);

        VentaRequest req = new VentaRequest("VTA-0001", 1L, null, "EFECTIVO", List.of(
                new DetalleVentaDTO(null, "Tilapia", BigDecimal.TEN, BigDecimal.TEN)
        ));

        assertThrows(ReglaNegocioException.class, () -> service.crear(req));
    }
}
