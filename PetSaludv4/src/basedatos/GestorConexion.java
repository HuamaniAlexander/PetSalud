package basedatos;

import configuracion.ConfiguracionBD;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Patron Singleton + Factory Method
 * Gestiona una unica instancia de conexion a la base de datos
 * Factory Method crea conexion segun el tipo de BD configurada
 */
public class GestorConexion {
    private static GestorConexion instance;
    private Connection connection;
    private ConfiguracionBD config;
    
    // Constructor privado para Singleton
    private GestorConexion() {
        this.config = ConfiguracionBD.getInstance();
        this.connection = crearConexion();
    }
    
    // Metodo Singleton: Obtener instancia unica
    public static synchronized GestorConexion getInstance() {
        if (instance == null) {
            instance = new GestorConexion();
        }
        return instance;
    }
    
    // Factory Method: Crea conexion segun tipo de BD
    private Connection crearConexion() {
        try {
            // Cargar driver JDBC
            Class.forName(config.getDriverClass());
            
            // Crear conexion segun tipo de BD
            String url = config.getJdbcUrl();
            String username = config.getUsername();
            String password = config.getPassword();
            
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Conexion exitosa a " + config.getDbType().toUpperCase());
            
            return conn;
            
        } catch (ClassNotFoundException e) {
            System.err.println("Driver no encontrado: " + e.getMessage());
            throw new RuntimeException("Error al cargar driver JDBC", e);
        } catch (SQLException e) {
            System.err.println("Error de conexion: " + e.getMessage());
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }
    
    // Obtener conexion activa
    public Connection getConnection() {
        try {
            // Verificar si la conexion esta cerrada y recrearla
            if (connection == null || connection.isClosed()) {
                connection = crearConexion();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar conexion: " + e.getMessage());
            connection = crearConexion();
        }
        return connection;
    }
    
    // Cerrar conexion
    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexion cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexion: " + e.getMessage());
        }
    }
    
    // Probar conexion
    public boolean probarConexion() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    // Reconectar (util para cambio de configuracion)
    public void reconectar() {
        cerrarConexion();
        this.config = ConfiguracionBD.getInstance();
        this.connection = crearConexion();
    }
}