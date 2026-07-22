package com.piscicultura.inventario.inventario.service;

import com.piscicultura.inventario.common.ReglaNegocioException;
import com.piscicultura.inventario.inventario.dto.MovimientoRequest;
import com.piscicultura.inventario.inventario.dto.MovimientoResponse;
import com.piscicultura.inventario.inventario.entity.Insumo;
import com.piscicultura.inventario.inventario.entity.MovimientoInventario;
import com.piscicultura.inventario.inventario.entity.TipoMovimiento;
import com.piscicultura.inventario.inventario.repository.InsumoRepository;
import com.piscicultura.inventario.inventario.repository.MovimientoInventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Reglas de negocio de MovimientoService: el stock nunca debe quedar negativo
 * y cada movimiento debe reflejar el stock resultante correcto.
 */
@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {

    @Mock
    private MovimientoInventarioRepository movimientoRepo;
    @Mock
    private InsumoRepository insumoRepo;

    @InjectMocks
    private MovimientoService service;

    private Insumo insumo;

    @BeforeEach
    void setUp() {
        insumo = Insumo.builder()
                .id(1L)
                .codigo("ALB-38")
                .nombre("Balanceado 38%")
                .stockActual(new BigDecimal("100.000"))
                .build();
    }

    @Test
    void entradaIncrementaElStock() {
        when(insumoRepo.findById(1L)).thenReturn(Optional.of(insumo));
        when(movimientoRepo.save(any(MovimientoInventario.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        MovimientoRequest req = new MovimientoRequest(1L, TipoMovimiento.ENTRADA,
                new BigDecimal("50.000"), "Compra", "admin");

        MovimientoResponse resp = service.registrar(req);

        assertThat(resp.stockResultante()).isEqualByComparingTo("150.000");
        assertThat(insumo.getStockActual()).isEqualByComparingTo("150.000");
    }

    @Test
    void salidaMayorAlStockDisponibleLanzaReglaNegocio() {
        when(insumoRepo.findById(1L)).thenReturn(Optional.of(insumo));

        MovimientoRequest req = new MovimientoRequest(1L, TipoMovimiento.SALIDA,
                new BigDecimal("500.000"), "Venta", "admin");

        assertThrows(ReglaNegocioException.class, () -> service.registrar(req));
        verify(movimientoRepo, never()).save(any());
        verify(insumoRepo, never()).save(any());
    }

    @Test
    void salidaValidaDejaElStockResultanteCorrecto() {
        when(insumoRepo.findById(1L)).thenReturn(Optional.of(insumo));
        ArgumentCaptor<MovimientoInventario> captor = ArgumentCaptor.forClass(MovimientoInventario.class);
        when(movimientoRepo.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

        MovimientoRequest req = new MovimientoRequest(1L, TipoMovimiento.SALIDA,
                new BigDecimal("30.000"), "Alimentación", "admin");

        service.registrar(req);

        assertThat(captor.getValue().getStockResultante()).isEqualByComparingTo("70.000");
    }
}
