package modelo.entidades;

import java.sql.Timestamp;

/**
 * Entidad Dueno - Propietario de mascotas
 */
public class Dueno {
    private int idDueno;
    private String dni;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String email;
    private String direccion;
    private Timestamp fechaRegistro;
    
    // Constructores
    public Dueno() {}
    
    public Dueno(String dni, String nombres, String apellidos, String telefono) {
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
    }
    
    // Getters y Setters
    public int getIdDueno() {
        return idDueno;
    }
    
    public void setIdDueno(int idDueno) {
        this.idDueno = idDueno;
    }
    
    public String getDni() {
        return dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }
    
    public String getNombres() {
        return nombres;
    }
    
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    
    public String getApellidos() {
        return apellidos;
    }
    
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    // Metodo utilitario
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
    
    @Override
    public String toString() {
        return "Dueno{" +
                "idDueno=" + idDueno +
                ", dni='" + dni + '\'' +
                ", nombreCompleto='" + getNombreCompleto() + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dueno dueno = (Dueno) o;
        return idDueno == dueno.idDueno;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idDueno);
    }
}