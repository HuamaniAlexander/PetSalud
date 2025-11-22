package modelo.entidades;

import modelo.Enumeraciones.SexoMascota;
import java.sql.Timestamp;

/**
 * Entidad Mascota - Animal bajo cuidado veterinario
 */
public class Mascota {
    private int idMascota;
    private String nombre;
    private String especie;
    private String raza;
    private int edad;
    private SexoMascota sexo;
    private double peso;
    private int idDueno;
    private Timestamp fechaRegistro;
    
    // Referencia al dueno (opcional para relaciones)
    private Dueno dueno;
    
    // Constructores
    public Mascota() {}
    
    public Mascota(String nombre, String especie, SexoMascota sexo, int idDueno) {
        this.nombre = nombre;
        this.especie = especie;
        this.sexo = sexo;
        this.idDueno = idDueno;
    }
    
    // Getters y Setters
    public int getIdMascota() {
        return idMascota;
    }
    
    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getEspecie() {
        return especie;
    }
    
    public void setEspecie(String especie) {
        this.especie = especie;
    }
    
    public String getRaza() {
        return raza;
    }
    
    public void setRaza(String raza) {
        this.raza = raza;
    }
    
    public int getEdad() {
        return edad;
    }
    
    public void setEdad(int edad) {
        this.edad = edad;
    }
    
    public SexoMascota getSexo() {
        return sexo;
    }
    
    public void setSexo(SexoMascota sexo) {
        this.sexo = sexo;
    }
    
    public double getPeso() {
        return peso;
    }
    
    public void setPeso(double peso) {
        this.peso = peso;
    }
    
    public int getIdDueno() {
        return idDueno;
    }
    
    public void setIdDueno(int idDueno) {
        this.idDueno = idDueno;
    }
    
    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public Dueno getDueno() {
        return dueno;
    }
    
    public void setDueno(Dueno dueno) {
        this.dueno = dueno;
        if (dueno != null) {
            this.idDueno = dueno.getIdDueno();
        }
    }
    
    @Override
    public String toString() {
        return "Mascota{" +
                "idMascota=" + idMascota +
                ", nombre='" + nombre + '\'' +
                ", especie='" + especie + '\'' +
                ", raza='" + raza + '\'' +
                ", edad=" + edad +
                ", sexo=" + sexo +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mascota mascota = (Mascota) o;
        return idMascota == mascota.idMascota;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idMascota);
    }
}