package dao;

import basedatos.GestorConexion;
import java.sql.*;
import java.util.List;

/**
 * Clase abstracta GenericDAO - Implementa operaciones CRUD comunes
 * Patron Template Method para reutilizar logica de base de datos
 * Aplica principios SOLID: DRY y Single Responsibility
 */
public abstract class GenericDAO<T> {
    
    protected Connection getConnection() throws SQLException {
        return GestorConexion.getInstance().getConnection();
    }
    
    // Metodos abstractos que cada DAO debe implementar
    public abstract T crear(T entity) throws SQLException;
    public abstract T obtenerPorId(int id) throws SQLException;
    public abstract List<T> listarTodos() throws SQLException;
    public abstract T actualizar(T entity) throws SQLException;
    public abstract boolean eliminar(int id) throws SQLException;
    
    // Metodo utilitario para cerrar recursos
    protected void cerrarRecursos(ResultSet rs, PreparedStatement ps) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
    
    protected void cerrarRecursos(PreparedStatement ps) {
        cerrarRecursos(null, ps);
    }
    
    // Metodo utilitario para obtener el ultimo ID insertado
    protected int obtenerUltimoId(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException("No se pudo obtener el ID generado");
    }
}