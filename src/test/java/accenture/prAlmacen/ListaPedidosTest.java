package accenture.prAlmacen;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import accenture.prAlmacen.Pedido.ESTADO;

public class ListaPedidosTest {
	private static final int ENTRADAS_LIBRES_10 = 10;
	private static final int IDENTIFICADOR_PEDIDO = 1000;
	private static final int ENTRADAS_LIBRES_0 = 0;
	
	private ListaPedidos listaPedidos;
	private ListaPedidos listaPedidosCompleta;

	@Before
	public void setUp() throws Exception {
		listaPedidos = new ListaPedidos(ENTRADAS_LIBRES_10);
		listaPedidosCompleta = new ListaPedidos(ENTRADAS_LIBRES_0);	
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
	
	@Test
	public void siSeGeneraUnPedidoEnEsperaYLaListaEstaLlenaSeElevaUnaExcepcion() {
		try {
			Pedido pedidoAux = listaPedidosCompleta.generarPedidoEnEspera();
			fail("Se esperaba excepcion ListaPedidosExcepcion");
		} catch (ListaPedidosExcepcion e) {
			assertThat(listaPedidosCompleta.entradasLibres(), is(0));
			assertThat(e.getMessage(), containsString("Lista llena"));
		}
	}
	
	@Test
	public void siSePasaUnPedidoADistribucionYHayAgentesEntoncesElEstadoDelProcesoEsDistribucion() {
		Pedido pedido = listaPedidos.generarPedidoEnEspera();
		// crear objeto mock
		IAgenteDistribuidor agente = mock(IAgenteDistribuidor.class);
		when(agente.hayAgenteDisponible()).thenReturn(true); 
		
		listaPedidos.pasarPedidoADistribucion(pedido.getId(), agente);
		verify(agente,times(1)).hayAgenteDisponible();
		verify(agente,times(1)).solicitarAgente();

		assertEquals(pedido.getEstado(), ESTADO.DISTRIBUCION);
	}
	
	@Test
	public void siSePasaUnPedidoADistribucionYNoHayAgentesEntoncesElEstadoDelProcesoEsEnEspera() {
		Pedido pedido = listaPedidos.generarPedidoEnEspera();
		// crear objeto mock
		IAgenteDistribuidor agente = mock(IAgenteDistribuidor.class);
		when(agente.hayAgenteDisponible()).thenReturn(false); 
		
		listaPedidos.pasarPedidoADistribucion(pedido.getId(), agente);
		verify(agente,times(1)).hayAgenteDisponible();
		verify(agente,times(0)).solicitarAgente();

		assertEquals(pedido.getEstado(), ESTADO.ESPERA);
	}
	
	@Test
	public void siSePasaUnPedidoADistribucionYSeEliminaEntoncesSeHaLiberadoAlAgenteDistribuidor() {
		Pedido pedido = listaPedidos.generarPedidoEnEspera();
		int entradasLibres = listaPedidos.entradasLibres();
		// crear objeto mock
		IAgenteDistribuidor agente = mock(IAgenteDistribuidor.class);
		when(agente.hayAgenteDisponible()).thenReturn(true); 
		
		listaPedidos.pasarPedidoADistribucion(pedido.getId(), agente);
		verify(agente,times(1)).hayAgenteDisponible();
		verify(agente,times(1)).solicitarAgente();
		
		//Elimino el pedido
		listaPedidos.eliminaPedido(pedido.getId(), agente);
		verify(agente,times(1)).liberarAgente();
		
		assertEquals(entradasLibres+1,listaPedidos.entradasLibres());

	}
	
	@Test
	public void siSeEliminaUnPedidoEnEsperaNoSeLiberaNingunAgenteDistribuidor() {
		Pedido pedido = listaPedidos.generarPedidoEnEspera();
		int entradasLibres = listaPedidos.entradasLibres();
		// crear objeto mock
		IAgenteDistribuidor agente = mock(IAgenteDistribuidor.class);
		when(agente.hayAgenteDisponible()).thenReturn(true); 
		
		//Elimino el pedido
		listaPedidos.eliminaPedido(pedido.getId(), agente);
		verify(agente,times(0)).liberarAgente();
		
		assertEquals(entradasLibres+1,listaPedidos.entradasLibres());
	}
	
	@Test //(expected = ListaPedidosExcepcion.class)
	public void siSeBuscaUnPedidoQueSeHaEliminadoSeElevaUnaExcepcion() {
		Pedido pedido = listaPedidos.generarPedidoEnEspera();
		int idPedido = pedido.getId();
		//int entradasLibres = listaPedidos.entradasLibres();
		// crear objeto mock
		IAgenteDistribuidor agente = mock(IAgenteDistribuidor.class);
		//when(agente.hayAgenteDisponible()).thenReturn(true); 
		
		//Elimino el pedido
		listaPedidos.eliminaPedido(pedido.getId(), agente);
		//verify(agente,times(0)).liberarAgente();
			
		//assertEquals(entradasLibres+1,listaPedidos.entradasLibres());
		try {
			//Buscar el pedido
			listaPedidos.buscaPedido(idPedido);
			fail("Se esperaba la excepcion: ListaPedidosExcepcion");
		}catch (ListaPedidosExcepcion e) {
			assertThat(e, instanceOf(ListaPedidosExcepcion.class));
			assertThat(e.getMessage(), containsString("El pedido " + idPedido + " no existe"));
		}
	}
		
		@Test //(expected = ListaPedidosExcepcion.class)
		public void siSeEliminaPedidoYNoExisteSeElevaUnaExcepcion() {
			int idPedido = 1;
			try {
				//Buscar el pedido
				listaPedidos.buscaPedido(idPedido);
				fail("Se esperaba la excepcion: ListaPedidosExcepcion");
			}catch (ListaPedidosExcepcion e) {
				assertThat(e, instanceOf(ListaPedidosExcepcion.class));
				assertThat(e.getMessage(), containsString("El pedido " + idPedido + " no existe"));
			}
	}

}
