package accenture.prAlmacen;

public class ListaPedidos {

    private final int MAX_PEDIDOS;
    private Pedido[] listaPedidos;
    private int entradasLibres;

    /**
     * Crea una lista con capacidad para MAX_PEDIDOS.
     *
     * @param maxPedidos
     *            la capacidad de la lista
     */
    public ListaPedidos(int maxPedidos) {
        MAX_PEDIDOS = maxPedidos;
        listaPedidos = new Pedido[MAX_PEDIDOS];
        entradasLibres = MAX_PEDIDOS;
    }

    /**
     * Devuelve el número máximo de pedidos permitidos en la lista.
     */
    public int maxPedidosTotal() {
        return MAX_PEDIDOS;
    }

    /**
     * Devuelve el número de entradas libres en la lista.
     */
    public int entradasLibres() {
        return entradasLibres;
    }

    /**
     * Devuelve verdadero si el identificador de pedido está libre y falso en
     * caso contrario.
     *
     * @param idPedido
     *            el identificador del pedido a comprobar
     */
    public boolean identificadorEstaLibre(int idPedido) {
        return listaPedidos[idPedido] == null;
    }

    /**
     * Crea un nuevo pedido en espera y lo almacena en la lista. Si la lista
     * está llena eleva una excepción.
     *
     * @return el nuevo pedido en espera creado
     */
    public Pedido generarPedidoEnEspera() {
        int identificador = buscarEntradaLibre();
        entradasLibres--;
        Pedido pedido = new Pedido(identificador);
        listaPedidos[identificador] = pedido;
        return pedido;
    }

    /**
     * Pasa un pedido que estaba en espera a distribución, siempre que haya un
     * agente de distribución disponible. En caso contrario el pedido permanece
     * en espera.
     *
     * @param idPedido
     *            el pedido a distribuir
     * @param agenteDist
     *            el agente de distribución
     */
    public void pasarPedidoADistribucion(int idPedido, IAgenteDistribuidor agenteDist) {
        Pedido pedido = buscaPedido(idPedido);
        if (agenteDist.hayAgenteDisponible()) {
            agenteDist.solicitarAgente();
            pedido.setEstadoDistribucion();
        }
    }

    /**
     * Busca un pedido en la lista a partir de su identificador; si el
     * identificador no es válido se eleva una excepción.
     *
     * @param idPedido
     *            el pedido a buscar
     * @return el pedido encontrado
     */
    public Pedido buscaPedido(int idPedido) {
        if (idPedido < 0 || idPedido >= MAX_PEDIDOS || listaPedidos[idPedido] == null) {
            throw new ListaPedidosExcepcion("El pedido " + idPedido + " no existe");
        }
        return listaPedidos[idPedido];
    }

    /**
     * Borra un pedido de la lista. Si el pedido estaba en distribución, se
     * libera al agente que tenía asignado.
     *
     * @param idPedido
     *            el pedido a eliminar
     * @param agenteDist
     *            el agente asociado al pedido, si existe
     */
    public void eliminaPedido(int idPedido, IAgenteDistribuidor agenteDist) {
        Pedido pedido = buscaPedido(idPedido);
        if (pedido.getEstado() == Pedido.ESTADO.DISTRIBUCION) {
            agenteDist.liberarAgente();
        }
        listaPedidos[idPedido] = null;
        entradasLibres++;
    }

    /**
     * Busca la primera entrada libre en la lista de pedidos; si la lista está
     * llena se eleva una excepción.
     *
     * @return La entrada encontrada
     */
    private int buscarEntradaLibre() {
        if (entradasLibres == 0) {
            throw new ListaPedidosExcepcion("Lista llena");
        }

        int indice = 0;
        while (indice < MAX_PEDIDOS && listaPedidos[indice] != null) {
            indice++;
        }
        return indice;
    }
}
