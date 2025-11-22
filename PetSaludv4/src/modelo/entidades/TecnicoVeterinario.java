package modelo.entidades;

import java.sql.Timestamp;

/**
 * Entidad TecnicoVeterinario - Responsable de toma y procesamiento de muestras
 */
public class TecnicoVeterinario {
    private int idTecnico;
    private String nombres;
    private String apellidos;
    private String especialidad;
    private String telefono;
    private Integer idUsuario;
    private Timestamp fechaRegistro;
    
    // Referencia al usuario
    private Usuario usuario;
    
    // Constructores
    public TecnicoVeterinario() {}
    
    public TecnicoVeterinario(String nombres, String apellidos, String telefono) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
    }
    
    // Getters y Setters
    public int getIdTecnico() {
        return idTecnico;
    }
    
    public void setIdTecnico(int idTecnico) {
        this.idTecnico = idTecnico;
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
    
    public String getEspecialidad() {
        return especialidad;
    }
    
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public Integer getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            this.idUsuario = usuario.getIdUsuario();
        }
    }
    
    // Metodo utilitario
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
    
    @Override
    public String toString() {
        return "TecnicoVeterinario{" +
                "idTecnico=" + idTecnico +
                ", nombreCompleto='" + getNombreCompleto() + '\'' +
                ", especialidad='" + especialidad + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TecnicoVeterinario that = (TecnicoVeterinario) o;
        return idTecnico == that.idTecnico;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idTecnico);
    }
}