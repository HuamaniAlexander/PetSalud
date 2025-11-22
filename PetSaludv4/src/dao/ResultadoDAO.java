package dao;

import modelo.entidades.ResultadoVeterinario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para ResultadoVeterinario - Gestion de resultados de analisis
 */
public class ResultadoDAO extends GenericDAO<ResultadoVeterinario> {
    
    @Override
    public ResultadoVeterinario crear(ResultadoVeterinario resultado) throws SQLException {
        String sql = "INSERT INTO resultado_veterinario (descripcion, valores, conclusiones, validado, id_orden, id_validador, fecha_validacion) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, resultado.getDescripcion());
            ps.setString(2, resultado.getValores());
            ps.setString(3, resultado.getConclusiones());
            ps.setBoolean(4, resultado.isValidado());
            ps.setInt(5, resultado.getIdOrden());
            
            if (resultado.getIdValidador() != null) {
                ps.setInt(6, resultado.getIdValidador());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            
            if (resultado.getFechaValidacion() != null) {
                ps.setTimestamp(7, resultado.getFechaValidacion());
            } else {
                ps.setNull(7, Types.TIMESTAMP);
            }
            
            ps.executeUpdate();
            resultado.setIdResultado(obtenerUltimoId(ps));
            
            return resultado;
        } finally {
            cerrarRecursos(ps);
        }
    }
    
    @Override
    public ResultadoVeterinario obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM resultado_veterinario WHERE id_resultado = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearResultado(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    @Override
    public List<ResultadoVeterinario> listarTodos() throws SQLException {
        String sql = "SELECT * FROM resultado_veterinario ORDER BY fecha_resultado DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ResultadoVeterinario> resultados = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                resultados.add(mapearResultado(rs));
            }
            return resultados;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    @Override
    public ResultadoVeterinario actualizar(ResultadoVeterinario resultado) throws SQLException {
        String sql = "UPDATE resultado_veterinario SET descripcion = ?, valores = ?, conclusiones = ?, validado = ?, id_orden = ?, id_validador = ?, fecha_validacion = ? WHERE id_resultado = ?";
        PreparedStatement ps = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, resultado.getDescripcion());
            ps.setString(2, resultado.getValores());
            ps.setString(3, resultado.getConclusiones());
            ps.setBoolean(4, resultado.isValidado());
            ps.setInt(5, resultado.getIdOrden());
            
            if (resultado.getIdValidador() != null) {
                ps.setInt(6, resultado.getIdValidador());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            
            if (resultado.getFechaValidacion() != null) {
                ps.setTimestamp(7, resultado.getFechaValidacion());
            } else {
                ps.setNull(7, Types.TIMESTAMP);
            }
            
            ps.setInt(8, resultado.getIdResultado());
            
            ps.executeUpdate();
            return resultado;
        } finally {
            cerrarRecursos(ps);
        }
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM resultado_veterinario WHERE id_resultado = ?";
        PreparedStatement ps = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            
            return ps.executeUpdate() > 0;
        } finally {
            cerrarRecursos(ps);
        }
    }
    
    // Metodo especifico: Obtener resultado por orden
    public ResultadoVeterinario obtenerPorOrden(int idOrden) throws SQLException {
        String sql = "SELECT * FROM resultado_veterinario WHERE id_orden = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idOrden);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearResultado(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    // Metodo especifico: Listar resultados pendientes de validacion
    public List<ResultadoVeterinario> listarPendientesValidacion() throws SQLException {
        String sql = "SELECT * FROM resultado_veterinario WHERE validado = FALSE ORDER BY fecha_resultado ASC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ResultadoVeterinario> resultados = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                resultados.add(mapearResultado(rs));
            }
            return resultados;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    // Mapear ResultSet a ResultadoVeterinario
    private ResultadoVeterinario mapearResultado(ResultSet rs) throws SQLException {
        ResultadoVeterinario resultado = new ResultadoVeterinario();
        resultado.setIdResultado(rs.getInt("id_resultado"));
        resultado.setFechaResultado(rs.getTimestamp("fecha_resultado"));
        resultado.setDescripcion(rs.getString("descripcion"));
        resultado.setValores(rs.getString("valores"));
        resultado.setConclusiones(rs.getString("conclusiones"));
        resultado.setValidado(rs.getBoolean("validado"));
        resultado.setIdOrden(rs.getInt("id_orden"));
        
        int idValidador = rs.getInt("id_validador");
        if (!rs.wasNull()) {
            resultado.setIdValidador(idValidador);
        }
        
        Timestamp fechaValidacion = rs.getTimestamp("fecha_validacion");
        if (fechaValidacion != null) {
            resultado.setFechaValidacion(fechaValidacion);
        }
        
        return resultado;
    }
}