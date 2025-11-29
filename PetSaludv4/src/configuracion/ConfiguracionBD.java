package configuracion;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuracion centralizada de conexiones a base de datos
 * Soporta MySQL, PostgreSQL y SQL Server
 */
public class ConfiguracionBD {
    private static ConfiguracionBD instance;
    private Properties properties;
    
    // Tipo de base de datos actual
    private String dbType;
    
    // Propiedades de conexion
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
    
    private ConfiguracionBD() {
        properties = new Properties();
        cargarConfiguracion();
    }
    
    public static ConfiguracionBD getInstance() {
        if (instance == null) {
            instance = new ConfiguracionBD();
        }
        return instance;
    }
    
    private void cargarConfiguracion() {
        try {
            // Intentar cargar desde archivo
            properties.load(new FileInputStream("recursos/configuracion/database.properties"));
            
            dbType = properties.getProperty("db.type", "mysql");
            host = properties.getProperty("db.host", "db-us.supercores.host");
            //port = properties.getProperty("db.port", obtenerPuertoPorDefecto());
            port = properties.getProperty("db.port", "3306");
            database = properties.getProperty("db.database", "s2941_veterinaria_petsalud");
            username = properties.getProperty("db.username", "u2941_cTygsyUmv6");
                password = properties.getProperty("db.password", "L9sVwCgwmn@LEHlegc=FogTw");
            
        } catch (IOException e) {
            // Configuracion por defecto si no existe archivo
            System.out.println("Usando configuracion por defecto");
            configuracionPorDefecto();
        }
    }
    
    private void configuracionPorDefecto() {
        dbType = "mysql";
        host = "db-us.supercores.host";
        port = "3306";
        database = "s2941_veterinaria_petsalud";
        username = "u2941_cTygsyUmv6";
        password = "L9sVwCgwmn@LEHlegc=FogTw";
    }
    
    private String obtenerPuertoPorDefecto() {
        switch(dbType.toLowerCase()) {
            case "mysql": return "3306";
            case "postgresql": return "5432";
            case "sqlserver": return "1433";
            default: return "3306";
        }
    }
    
    public String getJdbcUrl() {
        switch(dbType.toLowerCase()) {
            case "mysql":
                return String.format("jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=America/Lima&allowPublicKeyRetrieval=true",
                        host, port, database);
            case "postgresql":
                return String.format("jdbc:postgresql://%s:%s/%s", host, port, database);
            case "sqlserver":
                return String.format("jdbc:sqlserver://%s:%s;databaseName=%s", host, port, database);
            default:
                throw new IllegalArgumentException("Tipo de BD no soportado: " + dbType);
        }
    }
    
    public String getDriverClass() {
        switch(dbType.toLowerCase()) {
            case "mysql": return "com.mysql.cj.jdbc.Driver";
            case "postgresql": return "org.postgresql.Driver";
            case "sqlserver": return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            default: throw new IllegalArgumentException("Tipo de BD no soportado: " + dbType);
        }
    }
    
    // Getters
    public String getDbType() { return dbType; }
    public String getHost() { return host; }
    public String getPort() { return port; }
    public String getDatabase() { return database; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    
    // Setters para cambiar configuracion en tiempo de ejecucion
    public void setDbType(String dbType) {
        this.dbType = dbType;
        this.port = obtenerPuertoPorDefecto();
    }
    
    public void setHost(String host) { this.host = host; }
    public void setPort(String port) { this.port = port; }
    public void setDatabase(String database) { this.database = database; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}