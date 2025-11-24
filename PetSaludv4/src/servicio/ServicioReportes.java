package servicio;

import dao.ReportesDAO;
import gof.estructural.bridge.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Servicio para gestión y generación de reportes
 * Implementa patrón Bridge para diferentes formatos de salida
 */
public class ServicioReportes {
    private ReportesDAO reportesDAO;
    private SimpleDateFormat sdf;
    
    public ServicioReportes() {
        this.reportesDAO = new ReportesDAO();
        this.sdf = new SimpleDateFormat("dd/MM/yyyy");
    }
    
    // ============================================
    // MÉTODOS PARA OBTENER DATOS DE REPORTES
    // ============================================
    
    public List<Map<String, Object>> obtenerReporteOrdenes(Date fechaInicio, Date fechaFin, String estado) {
        try {
            return reportesDAO.reporteOrdenes(fechaInicio, fechaFin, estado);
        } catch (SQLException e) {
            System.err.println("Error al obtener reporte de órdenes: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public List<Map<String, Object>> obtenerReporteFinanciero(Date fechaInicio, Date fechaFin) {
        try {
            return reportesDAO.reporteFinanciero(fechaInicio, fechaFin);
        } catch (SQLException e) {
            System.err.println("Error al obtener reporte financiero: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public Map<String, Object> obtenerResumenFinanciero(Date fechaInicio, Date fechaFin) {
        try {
            return reportesDAO.resumenFinanciero(fechaInicio, fechaFin);
        } catch (SQLException e) {
            System.err.println("Error al obtener resumen financiero: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }
    
    public List<Map<String, Object>> obtenerReporteLaboratorio(Date fechaInicio, Date fechaFin) {
        try {
            return reportesDAO.reporteLaboratorio(fechaInicio, fechaFin);
        } catch (SQLException e) {
            System.err.println("Error al obtener reporte de laboratorio: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public List<Map<String, Object>> obtenerReporteVeterinarios(Date fechaInicio, Date fechaFin) {
        try {
            return reportesDAO.reporteVeterinarios(fechaInicio, fechaFin);
        } catch (SQLException e) {
            System.err.println("Error al obtener reporte de veterinarios: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public List<Map<String, Object>> obtenerTopClientes(Date fechaInicio, Date fechaFin, int limite) {
        try {
            return reportesDAO.reporteTopClientes(fechaInicio, fechaFin, limite);
        } catch (SQLException e) {
            System.err.println("Error al obtener top clientes: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // ============================================
    // MÉTODOS PARA GENERAR REPORTES CON PATRÓN BRIDGE
    // ============================================
    
    public String generarReporteOrdenesFormateado(Date fechaInicio, Date fechaFin, String estado, String formato) {
        List<Map<String, Object>> datos = obtenerReporteOrdenes(fechaInicio, fechaFin, estado);
        
        if (datos.isEmpty()) {
            return "No hay datos para el período seleccionado";
        }
        
        // Construir contenido del reporte
        StringBuilder contenido = new StringBuilder();
        contenido.append("REPORTE DE ÓRDENES VETERINARIAS\n");
        contenido.append("Período: ").append(sdf.format(fechaInicio)).append(" - ").append(sdf.format(fechaFin)).append("\n");
        if (estado != null && !estado.isEmpty()) {
            contenido.append("Estado filtrado: ").append(estado).append("\n");
        }
        contenido.append("Total de órdenes: ").append(datos.size()).append("\n\n");
        
        contenido.append("DETALLE DE ÓRDENES:\n");
        contenido.append("═══════════════════════════════════════════════════════════════════════════════\n\n");
        
        for (Map<String, Object> orden : datos) {
            contenido.append("ID Orden: ").append(orden.get("id_orden")).append("\n");
            contenido.append("Fecha: ").append(orden.get("fecha_orden")).append("\n");
            contenido.append("Tipo de Examen: ").append(orden.get("tipo_examen")).append("\n");
            contenido.append("Estado: ").append(orden.get("estado")).append("\n");
            contenido.append("Mascota: ").append(orden.get("mascota")).append(" (").append(orden.get("especie")).append(")\n");
            contenido.append("Dueño: ").append(orden.get("dueno")).append("\n");
            contenido.append("Teléfono: ").append(orden.get("telefono_dueno")).append("\n");
            contenido.append("Veterinario: ").append(orden.get("veterinario"));
            contenido.append(" - ").append(orden.get("especialidad_veterinario")).append("\n");
            contenido.append("Tiene Resultado: ").append(orden.get("tiene_resultado")).append("\n");
            contenido.append("Estado Resultado: ").append(orden.get("estado_resultado")).append("\n");
            
            Object obs = orden.get("observaciones");
            if (obs != null && !obs.toString().isEmpty()) {
                contenido.append("Observaciones: ").append(obs).append("\n");
            }
            contenido.append("\n───────────────────────────────────────────────────────────────────────────────\n\n");
        }
        
        // Aplicar patrón Bridge para formato
        IFormatoReporte formatoReporte = obtenerFormato(formato);
        ReporteOrdenes generador = new ReporteOrdenes(formatoReporte);
        generador.setContenido(contenido.toString());
        generador.setPeriodo(sdf.format(fechaInicio) + " - " + sdf.format(fechaFin));
        generador.setTotalOrdenes(datos.size());
        
        // Calcular estadísticas
        int pendientes = 0, completadas = 0;
        for (Map<String, Object> orden : datos) {
            String estadoOrden = orden.get("estado").toString();
            if (estadoOrden.contains("PENDIENTE")) pendientes++;
            if (estadoOrden.contains("COMPLETADA") || estadoOrden.contains("VALIDADA") || estadoOrden.contains("ENTREGADA")) completadas++;
        }
        generador.setPendientes(pendientes);
        generador.setCompletadas(completadas);
        
        return generador.generar();
    }
    
    public String generarReporteFinancieroFormateado(Date fechaInicio, Date fechaFin, String formato) {
        List<Map<String, Object>> datos = obtenerReporteFinanciero(fechaInicio, fechaFin);
        Map<String, Object> resumen = obtenerResumenFinanciero(fechaInicio, fechaFin);
        
        if (datos.isEmpty()) {
            return "No hay datos financieros para el período seleccionado";
        }
        
        StringBuilder contenido = new StringBuilder();
        contenido.append("REPORTE FINANCIERO\n");
        contenido.append("Período: ").append(sdf.format(fechaInicio)).append(" - ").append(sdf.format(fechaFin)).append("\n\n");
        
        // Resumen
        contenido.append("RESUMEN GENERAL:\n");
        contenido.append("═══════════════════════════════════════════════════════════════════════════════\n");
        contenido.append(String.format("Total Facturas: %s\n", resumen.getOrDefault("total_facturas", 0)));
        contenido.append(String.format("Total Facturado: S/ %.2f\n", getDouble(resumen, "total_facturado")));
        contenido.append(String.format("Total Pagado: S/ %.2f\n", getDouble(resumen, "total_pagado")));
        contenido.append(String.format("Total Pendiente: S/ %.2f\n", getDouble(resumen, "total_pendiente")));
        contenido.append(String.format("Promedio por Factura: S/ %.2f\n\n", getDouble(resumen, "promedio_factura")));
        
        contenido.append("DISTRIBUCIÓN POR MÉTODO DE PAGO:\n");
        contenido.append(String.format("Efectivo: S/ %.2f\n", getDouble(resumen, "total_efectivo")));
        contenido.append(String.format("Tarjeta: S/ %.2f\n", getDouble(resumen, "total_tarjeta")));
        contenido.append(String.format("Transferencia: S/ %.2f\n", getDouble(resumen, "total_transferencia")));
        contenido.append("═══════════════════════════════════════════════════════════════════════════════\n\n");
        
        // Detalle de facturas
        contenido.append("DETALLE DE FACTURAS:\n");
        contenido.append("───────────────────────────────────────────────────────────────────────────────\n\n");
        
        for (Map<String, Object> factura : datos) {
            contenido.append("Factura #").append(factura.get("id_factura")).append("\n");
            contenido.append("Fecha: ").append(factura.get("fecha")).append("\n");
            contenido.append("Cliente: ").append(factura.get("cliente")).append("\n");
            contenido.append("Teléfono: ").append(factura.get("telefono")).append("\n");
            contenido.append("Método de Pago: ").append(factura.get("metodo_pago")).append("\n");
            contenido.append(String.format("Monto: S/ %.2f\n", getDouble(factura, "monto_total")));
            contenido.append("Estado: ").append(factura.get("estado_pago")).append("\n");
            
            Object servicios = factura.get("servicios");
            if (servicios != null && !servicios.toString().isEmpty()) {
                contenido.append("Servicios: ").append(servicios).append("\n");
            }
            contenido.append("\n───────────────────────────────────────────────────────────────────────────────\n\n");
        }
        
        // Aplicar patrón Bridge
        IFormatoReporte formatoReporte = obtenerFormato(formato);
        ReporteFinanciero generador = new ReporteFinanciero(formatoReporte);
        
        double ingresos = getDouble(resumen, "total_pagado");
        double egresos = 0.0;
        
        generador.setIngresos(ingresos);
        generador.setEgresos(egresos);
        generador.setPeriodo(sdf.format(fechaInicio) + " - " + sdf.format(fechaFin));
        generador.setContenido(contenido.toString());
        
        return generador.generar();
    }
    
    public String generarReporteLaboratorioFormateado(Date fechaInicio, Date fechaFin, String formato) {
        List<Map<String, Object>> datos = obtenerReporteLaboratorio(fechaInicio, fechaFin);
        
        if (datos.isEmpty()) {
            return "No hay datos de laboratorio para el período seleccionado";
        }
        
        StringBuilder contenido = new StringBuilder();
        contenido.append("REPORTE DE LABORATORIO\n");
        contenido.append("Período: ").append(sdf.format(fechaInicio)).append(" - ").append(sdf.format(fechaFin)).append("\n\n");
        
        int totalAnalisis = 0;
        int totalCompletados = 0;
        int totalValidados = 0;
        
        contenido.append("ANÁLISIS POR TIPO DE EXAMEN:\n");
        contenido.append("═══════════════════════════════════════════════════════════════════════════════\n\n");
        
        for (Map<String, Object> tipo : datos) {
            int cantidad = getInt(tipo, "cantidad_analisis");
            totalAnalisis += cantidad;
            totalCompletados += getInt(tipo, "completados");
            totalValidados += getInt(tipo, "validados");
            
            contenido.append("Tipo de Examen: ").append(tipo.get("tipo_examen")).append("\n");
            contenido.append(String.format("  Cantidad: %d\n", cantidad));
            contenido.append(String.format("  Completados: %d\n", getInt(tipo, "completados")));
            contenido.append(String.format("  Validados: %d\n", getInt(tipo, "validados")));
            contenido.append(String.format("  Pendientes: %d\n", getInt(tipo, "pendientes")));
            contenido.append(String.format("  En Proceso: %d\n", getInt(tipo, "en_proceso")));
            
            Object tiempoPromedio = tipo.get("horas_promedio");
            if (tiempoPromedio != null) {
                contenido.append(String.format("  Tiempo Promedio: %.2f horas\n", getDouble(tipo, "horas_promedio")));
            }
            
            contenido.append(String.format("  Rango: %s - %s\n", 
                tipo.getOrDefault("primera_fecha", "N/A"), 
                tipo.getOrDefault("ultima_fecha", "N/A")));
            contenido.append("\n");
        }
        
        contenido.append("═══════════════════════════════════════════════════════════════════════════════\n");
        contenido.append(String.format("TOTAL ANÁLISIS: %d\n", totalAnalisis));
        contenido.append(String.format("TOTAL COMPLETADOS: %d\n", totalCompletados));
        contenido.append(String.format("TOTAL VALIDADOS: %d\n", totalValidados));
        
        // Aplicar patrón Bridge
        IFormatoReporte formatoReporte = obtenerFormato(formato);
        ReporteLaboratorio generador = new ReporteLaboratorio(formatoReporte);
        
        List<String> analisisList = new ArrayList<>();
        for (Map<String, Object> tipo : datos) {
            analisisList.add(tipo.get("tipo_examen") + ": " + tipo.get("cantidad_analisis") + " análisis");
        }
        
        generador.setAnalisis(analisisList);
        generador.setPeriodo(sdf.format(fechaInicio) + " - " + sdf.format(fechaFin));
        generador.setContenido(contenido.toString());
        
        return generador.generar();
    }
    
    public String generarReporteVeterinariosFormateado(Date fechaInicio, Date fechaFin, String formato) {
        List<Map<String, Object>> datos = obtenerReporteVeterinarios(fechaInicio, fechaFin);
        
        if (datos.isEmpty()) {
            return "No hay datos de veterinarios para el período seleccionado";
        }
        
        StringBuilder contenido = new StringBuilder();
        contenido.append("REPORTE DE ACTIVIDAD DE VETERINARIOS\n");
        contenido.append("Período: ").append(sdf.format(fechaInicio)).append(" - ").append(sdf.format(fechaFin)).append("\n\n");
        
        contenido.append("DETALLE POR VETERINARIO:\n");
        contenido.append("═══════════════════════════════════════════════════════════════════════════════\n\n");
        
        for (Map<String, Object> vet : datos) {
            contenido.append("Veterinario: ").append(vet.get("veterinario")).append("\n");
            contenido.append("Especialidad: ").append(vet.getOrDefault("especialidad", "N/A")).append("\n");
            contenido.append(String.format("Total Órdenes: %d\n", getInt(vet, "total_ordenes")));
            contenido.append(String.format("  - Pendientes: %d\n", getInt(vet, "pendientes")));
            contenido.append(String.format("  - En Proceso: %d\n", getInt(vet, "en_proceso")));
            contenido.append(String.format("  - Completadas: %d\n", getInt(vet, "completadas")));
            contenido.append(String.format("  - Validadas: %d\n", getInt(vet, "validadas")));
            contenido.append(String.format("Mascotas Atendidas: %d\n", getInt(vet, "mascotas_atendidas")));
            contenido.append(String.format("Clientes Atendidos: %d\n", getInt(vet, "clientes_atendidos")));
            contenido.append("\n───────────────────────────────────────────────────────────────────────────────\n\n");
        }
        
        // Aplicar formato
        IFormatoReporte formatoReporte = obtenerFormato(formato);
        GeneradorReporte generador = new ReporteOrdenes(formatoReporte);
        generador.setContenido(contenido.toString());
        generador.setPeriodo(sdf.format(fechaInicio) + " - " + sdf.format(fechaFin));
        
        return generador.generar();
    }
    
    // ============================================
    // MÉTODOS AUXILIARES
    // ============================================
    
    private IFormatoReporte obtenerFormato(String formato) {
        if (formato == null) formato = "PDF";
        
        switch (formato.toUpperCase()) {
            case "EXCEL":
                return new FormatoExcel();
            case "HTML":
                return new FormatoHTML();
            case "PDF":
            default:
                return new FormatoPDF();
        }
    }
    
    private double getDouble(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return 0.0;
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    private int getInt(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return 0;
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}