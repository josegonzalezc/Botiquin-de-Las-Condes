package cl.uilabs.botiquindelascondes.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by jose on 17-11-17.
 */

@Entity
public class Medicamento {
    private String laboratorio;
    @PrimaryKey
    private String nombre;
    private String precio_normal;
    private String descuento;
    private String precio_rebajado;

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = "Laboratorio: "+laboratorio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio_normal() {
        return precio_normal;
    }

    public void setPrecio_normal(String precio_normal) {
        this.precio_normal = "Precio normal: "+precio_normal;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = "Descuento: "+descuento;
    }

    public String getPrecio_rebajado() {
        return precio_rebajado;
    }

    public void setPrecio_rebajado(String precio_rebajado) {
        this.precio_rebajado = "Precio descuento: "+precio_rebajado;
    }
}
