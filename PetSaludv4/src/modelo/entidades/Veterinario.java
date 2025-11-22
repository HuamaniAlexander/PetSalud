package modelo.entidades;

import java.sql.Timestamp;

/**
 * Entidad Veterinario - Profesional medico veterinario
 */
public class Veterinario {
    private int idVeterinario;
    private String nombres;
    private String apellidos;
    private String especialidad;
    private String telefono;
    private String email;
    private String colegiatura;
    private Integer idUsuario;
    private Timestamp fechaRegistro;
    
    // Referencia al usuario
    private Usuario usuario;
    
    // Constructores
    public Veterinario() {}
    
    public Veterinario(String nombres, String apellidos, String telefono, String colegiatura) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.colegiatura = colegiatura;
    }
    
    // Getters y Setters
    public int getIdVeterinario() {
        return idVeterinario;
    }
    
    public void setIdVeterinario(int idVeterinario) {
        this.idVeterinario = idVeterinario;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getColegiatura() {
        return colegiatura;
    }
    
    public void setColegiatura(String colegiatura) {
        this.colegiatura = colegiatura;
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
        return "Veterinario{" +
                "idVeterinario=" + idVeterinario +
                ", nombreCompleto='" + getNombreCompleto() + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", colegiatura='" + colegiatura + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Veterinario that = (Veterinario) o;
        return idVeterinario == that.idVeterinario;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idVeterinario);
    }
}