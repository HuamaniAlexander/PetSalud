package gof.estructural.proxy;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import modelo.entidades.ResultadoVeterinario;
import modelo.entidades.Usuario;


// Proxy con cache
public class ProxyCacheResultado implements IAccesoResultado {
    private AccesoResultadoReal accesoReal;
    private Map<Integer, ResultadoVeterinario> cache;
    private Map<Integer, Long> tiempoCache;
    private static final long TIEMPO_EXPIRACION = 5 * 60 * 1000; // 5 minutos
    
    public ProxyCacheResultado() {
        this.accesoReal = new AccesoResultadoReal();
        this.cache = new HashMap<>();
        this.tiempoCache = new HashMap<>();
    }
    
    @Override
    public ResultadoVeterinario obtenerResultado(int idResultado, Usuario usuario) throws SQLException {
        // Verificar si esta en cache y no ha expirado
        if (cache.containsKey(idResultado)) {
            long tiempoTranscurrido = System.currentTimeMillis() - tiempoCache.get(idResultado);
            
            if (tiempoTranscurrido < TIEMPO_EXPIRACION) {
                System.out.println("Resultado #" + idResultado + " obtenido desde cache");
                return cache.get(idResultado);
            } else {
                // Cache expirado, remover
                cache.remove(idResultado);
                tiempoCache.remove(idResultado);
            }
        }
        
        // Obtener de BD y guardar en cache
        System.out.println("Resultado #" + idResultado + " obtenido desde BD y guardado en cache");
        ResultadoVeterinario resultado = accesoReal.obtenerResultado(idResultado, usuario);
        
        if (resultado != null) {
            cache.put(idResultado, resultado);
            tiempoCache.put(idResultado, System.currentTimeMillis());
        }
        
        return resultado;
    }
    
    @Override
    public boolean validarResultado(int idResultado, Usuario usuario) throws SQLException {
        boolean validado = accesoReal.validarResultado(idResultado, usuario);
        
        if (validado) {
            // Invalidar cache al validar
            cache.remove(idResultado);
            tiempoCache.remove(idResultado);
            System.out.println("Cache invalidado para resultado #" + idResultado);
        }
        
        return validado;
    }
    
    @Override
    public String generarInforme(int idResultado, Usuario usuario) {
        return accesoReal.generarInforme(idResultado, usuario);
    }
    
    public void limpiarCache() {
        cache.clear();
        tiempoCache.clear();
        System.out.println("Cache limpiado completamente");
    }
    
    public int getTamanoCache() {
        return cache.size();
    }
}
