package accenture.prAlmacen;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ListaPedidosTest {
	private static final int ENTRADAS_LIBRES = 10;
	private static final int IDENTIFICADOR_PEDIDO = 1000;
	
	private ListaPedidos listaPedidos;

	@Before
	public void setUp() throws Exception {
		listaPedidos = new ListaPedidos(ENTRADAS_LIBRES);
	}

	@After
	public void tearDown() throws Exception {
		listaPedidos = null;
	}

	@Test
	public void siSeGeneraUnPedidoEnEsperaElEspacioLibreDeLaListaDisminuyeEnUno() {
		int espacioLibre = listaPedidos.entradasLibres();
		Pedido pedidoAux = listaPedidos.generarPedidoEnEspera();
		assertEquals(espacioLibre-1, listaPedidos.entradasLibres());		
	}
	
	@Test
	public void siSeGeneraUnPedidoEnEsperaYLaListaEstaVaciaSuIdentificadorEsCero() {
		
		Pedido pedidoAux = listaPedidos.generarPedidoEnEspera();
		assertEquals(0,pedidoAux.getId());
		
	}

}
