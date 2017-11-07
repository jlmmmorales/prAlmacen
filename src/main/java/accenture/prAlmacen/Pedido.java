package accenture.prAlmacen;

public class Pedido {

    public enum ESTADO {
        ESPERA, DISTRIBUCION
    }

    private ESTADO estado;
    private int identificador;

    public Pedido(int idPedido) {
        identificador = idPedido;
        estado = ESTADO.ESPERA;
    }

    public void setEstadoDistribucion() {
        estado = ESTADO.DISTRIBUCION;
    }

    public ESTADO getEstado() {
        return estado;
    }

    public int getId() {
        return identificador;
    }
}
