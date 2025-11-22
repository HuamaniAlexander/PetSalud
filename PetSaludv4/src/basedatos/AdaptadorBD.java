package basedatos;

import configuracion.ConfiguracionBD;

/**
 * Patron Adapter - Adapta diferencias de sintaxis SQL entre diferentes motores de BD
 * Soporta MySQL, PostgreSQL y SQL Server
 */
public class AdaptadorBD {
    private static String dbType;
    
    static {
        dbType = ConfiguracionBD.getInstance().getDbType();
    }
    
    // Actualizar tipo de BD
    public static void actualizarTipoBD() {
        dbType = ConfiguracionBD.getInstance().getDbType();
    }
    
    /**
     * Obtener sintaxis para auto-incremento
     */
    public static String getAutoIncrementType() {
        switch(dbType.toLowerCase()) {
            case "mysql":
                return "AUTO_INCREMENT";
            case "postgresql":
                return "SERIAL";
            case "sqlserver":
                return "IDENTITY(1,1)";
            default:
                return "AUTO_INCREMENT";
        }
    }
    
    /**
     * Obtener query para ultimo ID insertado
     */
    public static String getLastInsertIdQuery() {
        switch(dbType.toLowerCase()) {
            case "mysql":
                return "SELECT LAST_INSERT_ID()";
            case "postgresql":
                return "SELECT currval(pg_get_serial_sequence(?, 'id'))";
            case "sqlserver":
                return "SELECT SCOPE_IDENTITY()";
            default:
                return "SELECT LAST_INSERT_ID()";
        }
    }
    
    /**
     * Obtener sintaxis para LIMIT
     */
    public static String getLimitClause(int limit) {
        switch(dbType.toLowerCase()) {
            case "mysql":
            case "postgresql":
                return "LIMIT " + limit;
            case "sqlserver":
                return "TOP " + limit;
            default:
                return "LIMIT " + limit;
        }
    }
    
    /**
     * Obtener sintaxis para LIMIT con OFFSET
     */
    public static String getLimitOffsetClause(int limit, int offset) {
        switch(dbType.toLowerCase()) {
            case "mysql":
            case "postgresql":
                return "LIMIT " + limit + " OFFSET " + offset;
            case "sqlserver":
                return "OFFSET " + offset + " ROWS FETCH NEXT " + limit + " ROWS ONLY";
            default:
                return "LIMIT " + limit + " OFFSET " + offset;
        }
    }
    
    /**
     * Obtener tipo de dato para DATETIME
     */
    public static String getDateTimeType() {
        switch(dbType.toLowerCase()) {
            case "mysql":
                return "DATETIME";
            case "postgresql":
                return "TIMESTAMP";
            case "sqlserver":
                return "DATETIME2";
            default:
                return "DATETIME";
        }
    }
    
    /**
     * Obtener tipo de dato para BOOLEAN
     */
    public static String getBooleanType() {
        switch(dbType.toLowerCase()) {
            case "mysql":
                return "BOOLEAN";
            case "postgresql":
                return "BOOLEAN";
            case "sqlserver":
                return "BIT";
            default:
                return "BOOLEAN";
        }
    }
    
    /**
     * Obtener tipo de dato para TEXT largo
     */
    public static String getTextType() {
        switch(dbType.toLowerCase()) {
            case "mysql":
                return "TEXT";
            case "postgresql":
                return "TEXT";
            case "sqlserver":
                return "NVARCHAR(MAX)";
            default:
                return "TEXT";
        }
    }
    
    /**
     * Obtener sintaxis para concatenar strings
     */
    public static String getConcatFunction(String... campos) {
        switch(dbType.toLowerCase()) {
            case "mysql":
                return "CONCAT(" + String.join(", ", campos) + ")";
            case "postgresql":
                return String.join(" || ", campos);
            case "sqlserver":
                return String.join(" + ", campos);
            default:
                return "CONCAT(" + String.join(", ", campos) + ")";
        }
    }
    
    /**
     * Obtener sintaxis para LIKE case-insensitive
     */
    public static String getILikeOperator() {
        switch(dbType.toLowerCase()) {
            case "mysql":
                return "LIKE";
            case "postgresql":
                return "ILIKE";
            case "sqlserver":
                return "LIKE";
            default:
                return "LIKE";
        }
    }
    
    /**
     * Obtener sintaxis para fecha actual
     */
    public static String getCurrentDateFunction() {
        switch(dbType.toLowerCase()) {
            case "mysql":
                return "NOW()";
            case "postgresql":
                return "NOW()";
            case "sqlserver":
                return "GETDATE()";
            default:
                return "NOW()";
        }
    }
    
    /**
     * Obtener sintaxis para extraer año de fecha
     */
    public static String getYearFunction(String campo) {
        switch(dbType.toLowerCase()) {
            case "mysql":
                return "YEAR(" + campo + ")";
            case "postgresql":
                return "EXTRACT(YEAR FROM " + campo + ")";
            case "sqlserver":
                return "YEAR(" + campo + ")";
            default:
                return "YEAR(" + campo + ")";
        }
    }
}