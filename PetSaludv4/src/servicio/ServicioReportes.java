package servicio;

import dao.ReportesDAO;
import gof.estructural.bridge.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ServicioReportes {

    private ReportesDAO reportesDAO;
    private SimpleDateFormat sdf;

    public ServicioReportes() {
        this.reportesDAO = new ReportesDAO();
        this.sdf = new SimpleDateFormat("dd/MM/yyyy");
    }

    // ============================================
    // MÉTODOS PRINCIPALES - GENERAN REPORTES DIRECTAMENTE
    // ============================================
public String generarReporteOrdenes(Date fechaInicio, Date fechaFin, String estado, String formato) {
    try {
        List<Map<String, Object>> datos = reportesDAO.reporteOrdenes(fechaInicio, fechaFin, estado);

        if (datos.isEmpty()) {
            return "NO_DATA";
        }

        // ✅ CONSTRUIR CONTENIDO DIRECTAMENTE
        String contenido = construirContenidoOrdenes(datos, fechaInicio, fechaFin, estado);
        
        // ✅ DEBUG: VERIFICAR QUE SE GENERÓ
        System.out.println("✅ CONTENIDO CONSTRUIDO: " + contenido.length() + " caracteres");
        System.out.println("📄 PREVIEW: " + contenido.substring(0, Math.min(200, contenido.length())));
        
        // ✅ PARA PDF: RETORNAR EL CONTENIDO DIRECTAMENTE
        if ("PDF".equalsIgnoreCase(formato)) {
            return contenido;  // ← ESTO ES LO IMPORTANTE
        }
        
        // Para otros formatos, usar el generador Bridge
        IFormatoReporte formatoReporte = obtenerFormato(formato);
        ReporteOrdenes generador = new ReporteOrdenes(formatoReporte);
        
        generador.setContenido(contenido);
        generador.setPeriodo(sdf.format(fechaInicio) + " - " + sdf.format(fechaFin));
        
        // Estadísticas
        int totalOrdenes = datos.size();
        int pendientes = 0, completadas = 0;
        
        for (Map<String, Object> orden : datos) {
            String estadoOrden = getValueSafe(orden, "estado").toString();
            if (estadoOrden.contains("PENDIENTE")) pendientes++;
            if (estadoOrden.contains("COMPLETADA") || estadoOrden.contains("VALIDADA") || estadoOrden.contains("ENTREGADA")) {
                completadas++;
            }
        }
        
        generador.setTotalOrdenes(totalOrdenes);
        generador.setPendientes(pendientes);
        generador.setCompletadas(completadas);
        
        return generador.generar();
        
    } catch (SQLException e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
        return "ERROR: " + e.getMessage();
    }
}

    public String generarReporteFinanciero(Date fechaInicio, Date fechaFin, String formato) {
        try {
            List<Map<String, Object>> datos = reportesDAO.reporteFinanciero(fechaInicio, fechaFin);
            Map<String, Object> resumen = reportesDAO.resumenFinanciero(fechaInicio, fechaFin);

            if (datos.isEmpty()) {
                return "NO_DATA";
            }

            IFormatoReporte formatoReporte = obtenerFormato(formato);
            ReporteFinanciero generador = new ReporteFinanciero(formatoReporte);

            String contenido = construirContenidoFinanciero(datos, resumen, fechaInicio, fechaFin);
            generador.setContenido(contenido);
            generador.setPeriodo(sdf.format(fechaInicio) + " - " + sdf.format(fechaFin));

            double ingresos = getDouble(resumen, "total_pagado");
            double egresos = 0.0;

            generador.setIngresos(ingresos);
            generador.setEgresos(egresos);

            return generador.generar();

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }

    public String generarReporteLaboratorio(Date fechaInicio, Date fechaFin, String formato) {
            try {
            List<Map<String, Object>> datos = reportesDAO.reporteLaboratorio(fechaInicio, fechaFin);

            if (datos == null || datos.isEmpty()) {
                return "NO_DATA";
            }

            System.out.println("✅ Reporte Laboratorio - Datos recibidos: " + datos.size() + " registros");

            String contenido = construirContenidoLaboratorio(datos, fechaInicio, fechaFin);

            System.out.println("✅ CONTENIDO LABORATORIO: " + contenido.length() + " caracteres");
            System.out.println("📄 PREVIEW LAB: " + contenido.substring(0, Math.min(200, contenido.length())));

            if ("PDF".equalsIgnoreCase(formato)) {
                return contenido;  // ← IMPORTANTE: igual que en reporteOrdenes
            }

            // Para otros formatos, usar el generador Bridge
            IFormatoReporte formatoReporte = obtenerFormato(formato);
            ReporteLaboratorio generador = new ReporteLaboratorio(formatoReporte);

            generador.setContenido(contenido);
            generador.setPeriodo(sdf.format(fechaInicio) + " - " + sdf.format(fechaFin));

            // Estadísticas adicionales
            List<String> analisisList = new ArrayList<>();
            for (Map<String, Object> tipo : datos) {
                analisisList.add(getValueSafe(tipo, "tipo_examen") + ": "
                        + getValueSafe(tipo, "cantidad_analisis") + " análisis");
            }
            generador.setAnalisis(analisisList);

            return generador.generar();

        } catch (SQLException e) {
            System.err.println("❌ Error en reporteLaboratorio: " + e.getMessage());
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }

    public String generarReporteVeterinarios(Date fechaInicio, Date fechaFin, String formato) {
        try {
            List<Map<String, Object>> datos = reportesDAO.reporteVeterinarios(fechaInicio, fechaFin);

            if (datos.isEmpty()) {
                return "NO_DATA";
            }

            IFormatoReporte formatoReporte = obtenerFormato(formato);
            ReporteOrdenes generador = new ReporteOrdenes(formatoReporte);

            String contenido = construirContenidoVeterinarios(datos, fechaInicio, fechaFin);
            generador.setContenido(contenido);
            generador.setPeriodo(sdf.format(fechaInicio) + " - " + sdf.format(fechaFin));

            return generador.generar();

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }

    // ============================================
    // MÉTODOS PARA CONSTRUIR CONTENIDO
    // ============================================
private String construirContenidoOrdenes(List<Map<String, Object>> datos, Date fechaInicio, Date fechaFin, String estado) {
    StringBuilder contenido = new StringBuilder();

    contenido.append("REPORTE DE ORDENES VETERINARIAS\n");
    contenido.append("Periodo: ").append(sdf.format(fechaInicio)).append(" - ").append(sdf.format(fechaFin)).append("\n");
    if (estado != null && !estado.isEmpty()) {
        contenido.append("Estado filtrado: ").append(estado).append("\n");
    }
    contenido.append("Total de ordenes: ").append(datos.size()).append("\n\n");
    contenido.append("DETALLE DE ORDENES:\n");
    contenido.append("═══════════════════════════════════════════════════════════════════\n\n");

    for (Map<String, Object> orden : datos) {
        // ✅ USAR NOMBRES EXACTOS DEL RESULTSET
        contenido.append("ID Orden: ").append(getValueSafe(orden, "id_orden")).append("\n");
        contenido.append("Fecha: ").append(getValueSafe(orden, "fecha_orden")).append("\n");
        contenido.append("Tipo Examen: ").append(getValueSafe(orden, "tipo_examen")).append("\n");
        contenido.append("Estado: ").append(getValueSafe(orden, "estado")).append("\n");
        
        // Mascota: columna es "nombre"
        contenido.append("Mascota: ").append(getValueSafe(orden, "nombre"));
        contenido.append(" (").append(getValueSafe(orden, "especie")).append(")\n");
        
        contenido.append("Dueno: ").append(getValueSafe(orden, "dueno")).append("\n");
        
        // Teléfono: columna es "telefono"
        contenido.append("Telefono: ").append(getValueSafe(orden, "telefono")).append("\n");
        
        // Veterinario y especialidad
        contenido.append("Veterinario: ").append(getValueSafe(orden, "veterinario"));
        contenido.append(" - ").append(getValueSafe(orden, "especialidad")).append("\n");
        
        contenido.append("Tiene Resultado: ").append(getValueSafe(orden, "tiene_resultado")).append("\n");
        contenido.append("Estado Resultado: ").append(getValueSafe(orden, "estado_resultado")).append("\n");
        
        // Observaciones si existen
        Object obs = orden.get("observaciones");
        if (obs != null && !obs.toString().trim().isEmpty()) {
            contenido.append("Observaciones: ").append(obs).append("\n");
        }
        
        contenido.append("\n───────────────────────────────────────────────────────────────────\n\n");
    }

    return contenido.toString();
}
// Método auxiliar para obtener valores con fallback
    private String getStringValue(Map<String, Object> map, String... keys) {
        for (String key : keys) {
            Object value = map.get(key);
            if (value != null && !value.toString().trim().isEmpty()) {
                return value.toString();
            }
        }
        return "N/A";
    }

    private String construirContenidoFinanciero(List<Map<String, Object>> datos, Map<String, Object> resumen, Date fechaInicio, Date fechaFin) {
        StringBuilder contenido = new StringBuilder();

        contenido.append("REPORTE FINANCIERO\n");
        contenido.append("Período: ").append(sdf.format(fechaInicio)).append(" - ").append(sdf.format(fechaFin)).append("\n\n");

        contenido.append("RESUMEN GENERAL:\n");
        contenido.append("═══════════════════════════════════════════════════════════════════\n");
        contenido.append(String.format("Total Facturas: %s\n", getValueSafe(resumen, "total_facturas")));
        contenido.append(String.format("Total Facturado: S/ %.2f\n", getDouble(resumen, "total_facturado")));
        contenido.append(String.format("Total Pagado: S/ %.2f\n", getDouble(resumen, "total_pagado")));
        contenido.append(String.format("Total Pendiente: S/ %.2f\n", getDouble(resumen, "total_pendiente")));
        contenido.append(String.format("Promedio por Factura: S/ %.2f\n\n", getDouble(resumen, "promedio_factura")));

        contenido.append("DISTRIBUCIÓN POR MÉTODO DE PAGO:\n");
        contenido.append(String.format("Efectivo: S/ %.2f\n", getDouble(resumen, "total_efectivo")));
        contenido.append(String.format("Tarjeta: S/ %.2f\n", getDouble(resumen, "total_tarjeta")));
        contenido.append(String.format("Transferencia: S/ %.2f\n", getDouble(resumen, "total_transferencia")));
        contenido.append("═══════════════════════════════════════════════════════════════════\n\n");

        contenido.append("DETALLE DE FACTURAS:\n");
        contenido.append("───────────────────────────────────────────────────────────────────\n\n");

        for (Map<String, Object> factura : datos) {
            contenido.append("Factura #").append(getValueSafe(factura, "id_factura")).append("\n");
            contenido.append("Fecha: ").append(getValueSafe(factura, "fecha")).append("\n");
            contenido.append("Cliente: ").append(getValueSafe(factura, "cliente")).append("\n");
            contenido.append("Teléfono: ").append(getValueSafe(factura, "telefono")).append("\n");
            contenido.append("Método de Pago: ").append(getValueSafe(factura, "metodo_pago")).append("\n");
            contenido.append(String.format("Monto: S/ %.2f\n", getDouble(factura, "monto_total")));
            contenido.append("Estado: ").append(getValueSafe(factura, "estado_pago")).append("\n");

            Object servicios = factura.get("servicios");
            if (servicios != null && !servicios.toString().isEmpty()) {
                contenido.append("Servicios: ").append(servicios).append("\n");
            }
            contenido.append("\n───────────────────────────────────────────────────────────────────\n\n");
        }

        return contenido.toString();
    }

    private String construirContenidoLaboratorio(List<Map<String, Object>> datos, Date fechaInicio, Date fechaFin) {
    StringBuilder contenido = new StringBuilder();

    contenido.append("REPORTE DE LABORATORIO\n");
    contenido.append("Periodo: ").append(sdf.format(fechaInicio)).append(" - ").append(sdf.format(fechaFin)).append("\n");
    contenido.append("Total de tipos de examen: ").append(datos.size()).append("\n\n");
    contenido.append("ANALISIS POR TIPO DE EXAMEN:\n");
    contenido.append("═══════════════════════════════════════════════════════════════════\n\n");

    int totalAnalisis = 0;
    int totalCompletados = 0;
    int totalValidados = 0;
    int totalPendientes = 0;
    int totalEnProceso = 0;

    for (Map<String, Object> tipo : datos) {
        int cantidad = getInt(tipo, "cantidad_analisis");
        int completados = getInt(tipo, "completados");
        int validados = getInt(tipo, "validados");
        int pendientes = getInt(tipo, "pendientes");
        int enProceso = getInt(tipo, "en_proceso");
        
        totalAnalisis += cantidad;
        totalCompletados += completados;
        totalValidados += validados;
        totalPendientes += pendientes;
        totalEnProceso += enProceso;

        contenido.append("Tipo de Examen: ").append(getValueSafe(tipo, "tipo_examen")).append("\n");
        contenido.append(String.format("  Cantidad: %d\n", cantidad));
        contenido.append(String.format("  Completados: %d\n", completados));
        contenido.append(String.format("  Validados: %d\n", validados));
        contenido.append(String.format("  Pendientes: %d\n", pendientes));
        contenido.append(String.format("  En Proceso: %d\n", enProceso));

        double tiempoPromedio = getDouble(tipo, "horas_promedio");
        if (tiempoPromedio > 0) {
            contenido.append(String.format("  Tiempo Promedio: %.2f horas\n", tiempoPromedio));
        } else {
            contenido.append("  Tiempo Promedio: N/A\n");
        }

        // ✅ USAR EL MÉTODO MEJORADO
        String primeraFecha = getValueSafe(tipo, "primera_fecha");
        String ultimaFecha = getValueSafe(tipo, "ultima_fecha");
        
        if (!"N/A".equals(primeraFecha) && !"N/A".equals(ultimaFecha) && 
            !primeraFecha.trim().isEmpty() && !ultimaFecha.trim().isEmpty()) {
            contenido.append(String.format("  Rango: %s - %s\n", primeraFecha, ultimaFecha));
        } else {
            contenido.append("  Rango: N/A\n");
        }
        
        contenido.append("\n───────────────────────────────────────────────────────────────────\n\n");
    }

    // Resumen general (igual que antes)
    contenido.append("═══════════════════════════════════════════════════════════════════\n");
    contenido.append("RESUMEN GENERAL:\n");
    contenido.append(String.format("Total Analisis Registrados: %d\n", totalAnalisis));
    contenido.append(String.format("Total Completados: %d\n", totalCompletados));
    contenido.append(String.format("Total Validados: %d\n", totalValidados));
    contenido.append(String.format("Total Pendientes: %d\n", totalPendientes));
    contenido.append(String.format("Total En Proceso: %d\n", totalEnProceso));
    
    if (totalAnalisis > 0) {
        double porcentajeCompletados = (totalCompletados * 100.0) / totalAnalisis;
        double porcentajeValidados = (totalValidados * 100.0) / totalAnalisis;
        contenido.append(String.format("\nTasa de Completitud: %.1f%%\n", porcentajeCompletados));
        contenido.append(String.format("Tasa de Validacion: %.1f%%\n", porcentajeValidados));
    }
    
    contenido.append("═══════════════════════════════════════════════════════════════════\n");

    return contenido.toString();
}

    private String construirContenidoVeterinarios(List<Map<String, Object>> datos, Date fechaInicio, Date fechaFin) {
        StringBuilder contenido = new StringBuilder();

        contenido.append("REPORTE DE ACTIVIDAD DE VETERINARIOS\n");
        contenido.append("Período: ").append(sdf.format(fechaInicio)).append(" - ").append(sdf.format(fechaFin)).append("\n\n");

        contenido.append("DETALLE POR VETERINARIO:\n");
        contenido.append("═══════════════════════════════════════════════════════════════════\n\n");

        for (Map<String, Object> vet : datos) {
            contenido.append("Veterinario: ").append(getValueSafe(vet, "veterinario")).append("\n");
            contenido.append("Especialidad: ").append(getValueSafe(vet, "especialidad")).append("\n");
            contenido.append(String.format("Total Órdenes: %d\n", getInt(vet, "total_ordenes")));
            contenido.append(String.format("  - Pendientes: %d\n", getInt(vet, "pendientes")));
            contenido.append(String.format("  - En Proceso: %d\n", getInt(vet, "en_proceso")));
            contenido.append(String.format("  - Completadas: %d\n", getInt(vet, "completadas")));
            contenido.append(String.format("  - Validadas: %d\n", getInt(vet, "validadas")));
            contenido.append(String.format("Mascotas Atendidas: %d\n", getInt(vet, "mascotas_atendidas")));
            contenido.append(String.format("Clientes Atendidos: %d\n", getInt(vet, "clientes_atendidos")));
            contenido.append("\n───────────────────────────────────────────────────────────────────\n\n");
        }

        return contenido.toString();
    }

    // ============================================
    // MÉTODOS AUXILIARES
    // ============================================
    private IFormatoReporte obtenerFormato(String formato) {
        if (formato == null) {
            formato = "PDF";
        }

        switch (formato.toUpperCase()) {
            case "EXCEL":
            case "CSV":
                return new FormatoExcel();
            case "HTML":
                return new FormatoHTML();
            case "PDF":
            default:
                return new FormatoPDF();
        }
    }

    private String getValueSafe(Map<String, Object> map, String key) {
    if (map == null) return "N/A";
    
    // Buscar exactamente
    if (map.containsKey(key)) {
        Object value = map.get(key);
        return (value != null) ? value.toString() : "N/A";
    }
    
    // Buscar case insensitive
    for (String actualKey : map.keySet()) {
        if (actualKey.equalsIgnoreCase(key)) {
            Object value = map.get(actualKey);
            return (value != null) ? value.toString() : "N/A";
        }
    }
    
    System.err.println("⚠ Key no encontrada: '" + key + "'. Keys disponibles: " + map.keySet());
    return "N/A";
}

// Método específico para fechas
private String getDateSafe(Map<String, Object> map, String key) {
    Object value = getValueSafe(map, key);
    if (value == null || "N/A".equals(value)) {
        return "N/A";
    }
    
    try {
        // Si es una fecha, formatearla
        if (value instanceof Date) {
            return sdf.format((Date) value);
        }
        // Si ya es string, retornarlo tal cual
        return value.toString();
    } catch (Exception e) {
        return "N/A";
    }
}

    private double getDouble(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return 0.0;
        }
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
        if (value == null) {
            return 0;
        }
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