package modelo;

/**
 * Consolidacion de todas las enumeraciones del sistema
 */
public class Enumeraciones {
    
    public enum TipoExamen {
        SANGRE("Analisis de Sangre"),
        ORINA("Analisis de Orina"),
        HECES("Analisis de Heces"),
        RAYOS_X("Rayos X"),
        ECOGRAFIA("Ecografia"),
        OTROS("Otros Examenes");
        
        private final String descripcion;
        
        TipoExamen(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    public enum TipoMuestra {
        SANGRE_VENOSA("Sangre Venosa"),
        SANGRE_CAPILAR("Sangre Capilar"),
        ORINA_SIMPLE("Orina Simple"),
        HECES_FRESCAS("Heces Frescas"),
        OTROS("Otros Tipos");
        
        private final String descripcion;
        
        TipoMuestra(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    public enum RolUsuario {
        ADMIN("Administrador"),
        VETERINARIO("Veterinario"),
        TECNICO("Tecnico de Laboratorio"),
        RECEPCIONISTA("Recepcionista");
        
        private final String descripcion;
        
        RolUsuario(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    public enum EstadoOrden {
        PENDIENTE("Pendiente"),
        EN_PROCESO("En Proceso"),
        COMPLETADA("Completada"),
        VALIDADA("Validada"),
        ENTREGADA("Entregada");
        
        private final String descripcion;
        
        EstadoOrden(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
        
        // Validar transiciones de estado permitidas
        public boolean puedeTransicionarA(EstadoOrden nuevoEstado) {
            switch(this) {
                case PENDIENTE:
                    return nuevoEstado == EN_PROCESO;
                case EN_PROCESO:
                    return nuevoEstado == COMPLETADA;
                case COMPLETADA:
                    return nuevoEstado == VALIDADA;
                case VALIDADA:
                    return nuevoEstado == ENTREGADA;
                case ENTREGADA:
                    return false; // Estado final
                default:
                    return false;
            }
        }
    }
    
    public enum MetodoPago {
        EFECTIVO("Efectivo"),
        TARJETA("Tarjeta de Credito/Debito"),
        TRANSFERENCIA("Transferencia Bancaria");
        
        private final String descripcion;
        
        MetodoPago(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    public enum SexoMascota {
        MACHO("Macho"),
        HEMBRA("Hembra");
        
        private final String descripcion;
        
        SexoMascota(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
}