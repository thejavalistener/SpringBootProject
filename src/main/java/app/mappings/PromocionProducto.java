package app.mappings;

import jakarta.persistence.*;

@Entity
@Table(name = "promocion_producto")
public class PromocionProducto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id_promocion_producto") private Integer idPromocionProducto;
    @ManyToOne @JoinColumn(name = "id_producto") private Producto producto;
    @ManyToOne @JoinColumn(name = "id_promocion_vigencia") private PromocionVigencia promocionVigencia;
    private Double descuento;
    public Integer getIdPromocionProducto() { return idPromocionProducto; } public void setIdPromocionProducto(Integer v) { idPromocionProducto = v; }
    public Producto getProducto() { return producto; } public void setProducto(Producto v) { producto = v; }
    public PromocionVigencia getPromocionVigencia() { return promocionVigencia; } public void setPromocionVigencia(PromocionVigencia v) { promocionVigencia = v; }
    public Double getDescuento() { return descuento; } public void setDescuento(Double v) { descuento = v; }
}
