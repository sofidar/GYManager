package modelo;

import java.time.LocalDate;

public class Membresia {
    private int idMembresia;
    private String tipo;
    private int duracionMeses;
    private double precio;


    public Membresia(int idMembresia, String tipo, int duracionMeses, double precio) {
        this.idMembresia = idMembresia;
        this.tipo = tipo;
        this.duracionMeses = duracionMeses;
        this.precio = precio;
    }
    // Nuevo constructor para usar en mostrarSocios()
    public Membresia(int idMembresia, String tipo, int duracionMeses) {
        this.idMembresia = idMembresia;
        this.tipo = tipo;
        this.duracionMeses = duracionMeses;
    }

    public Membresia(int idMembresia) {
    }

    // Getters y setters
    public int getIdMembresia() { return idMembresia; }
    public String getTipo() { return tipo; }
    public int getDuracionMeses() { return duracionMeses; }
    public double getPrecio() { return precio; }



    @Override
    public String toString() {return tipo ;}
}
