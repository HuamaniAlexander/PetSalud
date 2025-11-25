package gof.estructural.bridge;

import java.text.SimpleDateFormat;
import java.util.*;

public class FormatoExcel implements IFormatoReporte {
    private String contenido;
    private String titulo;
    private SimpleDateFormat sdf;
    
    public FormatoExcel() {
        this.sdf = new SimpleDateFormat("dd/MM/yyyy");
    }
    
    @Override
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    @Override
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    @Override
    public String generar() {
        return generarCSVProfesional(contenido, titulo);
    }
    
    @Override
    public String getExtension() {
        return ".csv";
    }
    
private String generarCSVProfesional(String contenido, String titulo) {
    StringBuilder csv = new StringBuilder();
    
    // BOM UTF-8
    csv.append("\uFEFF");
    
    // === ENCABEZADO ===
    csv.append("VETERINARIA PETSALUD\n");
    csv.append("Sistema de Gestión Veterinaria\n");
    csv.append("═════════════════════════════════════════\n");
    csv.append("REPORTE:,").append(titulo).append("\n");
    csv.append("FECHA:,").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())).append("\n");
    csv.append("\n\n");
    
    // Detectar tipo y generar estructura
    if (titulo.contains("Órdenes") || titulo.contains("ORDENES")) {
        generarTablaOrdenes(csv, contenido);
    } else if (titulo.contains("Laboratorio") || titulo.contains("LABORATORIO")) {
        generarTablaLaboratorio(csv, contenido);
    } else if (titulo.contains("Financiero") || titulo.contains("FINANCIERO")) {
        generarTablaFinanciero(csv, contenido);
    } else if (titulo.contains("Veterinarios") || titulo.contains("VETERINARIOS")) {
        generarTablaVeterinarios(csv, contenido);
    } else {
        generarTablaGeneral(csv, contenido);
    }
    
    // === PIE ===
    csv.append("\n\n");
    csv.append("═════════════════════════════════════════\n");
    csv.append("Documento confidencial - Uso interno\n");
    csv.append("Generado:,").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())).append("\n");
    
    return csv.toString();
}

private void generarTablaOrdenes(StringBuilder csv, String contenido) {
    csv.append("═════════════════════════════════════════\n");
    csv.append("TABLA DE ÓRDENES VETERINARIAS\n");
    csv.append("═════════════════════════════════════════\n\n");
    
    // Encabezados de columna
    csv.append("ID Orden,Fecha,Tipo Examen,Estado,Mascota,Especie,Dueño,Teléfono,Veterinario,Especialidad,Tiene Resultado,Estado Resultado\n");
    
    // Parsear contenido
    String[] lineas = contenido.split("\n");
    Map<String, String> ordenActual = new LinkedHashMap<>();
    
    for (String linea : lineas) {
        linea = linea.trim();
        
        if (linea.contains("ID Orden:")) {
            if (!ordenActual.isEmpty()) {
                escribirFilaOrden(csv, ordenActual);
                ordenActual.clear();
            }
        }
        
        if (linea.contains(":") && !linea.contains("═") && !linea.contains("─")) {
            String[] partes = linea.split(":", 2);
            if (partes.length == 2) {
                String clave = partes[0].trim();
                String valor = partes[1].trim();
                ordenActual.put(clave, valor);
            }
        }
    }
    
    // Última orden
    if (!ordenActual.isEmpty()) {
        escribirFilaOrden(csv, ordenActual);
    }
}

private void escribirFilaOrden(StringBuilder csv, Map<String, String> orden) {
    csv.append(escaparCSV(orden.getOrDefault("ID Orden", ""))).append(",");
    csv.append(escaparCSV(orden.getOrDefault("Fecha", ""))).append(",");
    csv.append(escaparCSV(orden.getOrDefault("Tipo Examen", ""))).append(",");
    csv.append(escaparCSV(orden.getOrDefault("Estado", ""))).append(",");
    
    // Extraer mascota y especie
    String mascotaCompleta = orden.getOrDefault("Mascota", "");
    String mascota = mascotaCompleta;
    String especie = "";
    
    if (mascotaCompleta.contains("(") && mascotaCompleta.contains(")")) {
        int inicio = mascotaCompleta.indexOf("(");
        int fin = mascotaCompleta.indexOf(")");
        mascota = mascotaCompleta.substring(0, inicio).trim();
        especie = mascotaCompleta.substring(inicio + 1, fin).trim();
    }
    
    csv.append(escaparCSV(mascota)).append(",");
    csv.append(escaparCSV(especie)).append(",");
    csv.append(escaparCSV(orden.getOrDefault("Dueño", orden.getOrDefault("Dueno", "")))).append(",");
    csv.append(escaparCSV(orden.getOrDefault("Teléfono", orden.getOrDefault("Telefono", "")))).append(",");
    
    // Extraer veterinario y especialidad
    String veterinarioCompleto = orden.getOrDefault("Veterinario", "");
    String veterinario = veterinarioCompleto;
    String especialidad = "";
    
    if (veterinarioCompleto.contains(" - ")) {
        String[] partes = veterinarioCompleto.split(" - ", 2);
        veterinario = partes[0].trim();
        especialidad = partes.length > 1 ? partes[1].trim() : "";
    }
    
    csv.append(escaparCSV(veterinario)).append(",");
    csv.append(escaparCSV(especialidad)).append(",");
    csv.append(escaparCSV(orden.getOrDefault("Tiene Resultado", ""))).append(",");
    csv.append(escaparCSV(orden.getOrDefault("Estado Resultado", ""))).append("\n");
}

private void generarTablaLaboratorio(StringBuilder csv, String contenido) {
    csv.append("═════════════════════════════════════════\n");
    csv.append("ANÁLISIS DE LABORATORIO\n");
    csv.append("═════════════════════════════════════════\n\n");
    
    csv.append("Tipo Examen,Cantidad,Completados,Validados,Pendientes,En Proceso,Tiempo Promedio (hrs),Rango Fechas\n");
    
    String[] lineas = contenido.split("\n");
    Map<String, String> analisisActual = new LinkedHashMap<>();
    
    for (String linea : lineas) {
        linea = linea.trim();
        
        if (linea.startsWith("Tipo de Examen:")) {
            if (!analisisActual.isEmpty()) {
                escribirFilaLaboratorio(csv, analisisActual);
                analisisActual.clear();
            }
        }
        
        if (linea.contains(":") && !linea.contains("═")) {
            String[] partes = linea.split(":", 2);
            if (partes.length == 2) {
                String clave = partes[0].trim().replace("  ", "");
                String valor = partes[1].trim();
                analisisActual.put(clave, valor);
            }
        }
    }
    
    if (!analisisActual.isEmpty()) {
        escribirFilaLaboratorio(csv, analisisActual);
    }
}

private void escribirFilaLaboratorio(StringBuilder csv, Map<String, String> analisis) {
    csv.append(escaparCSV(analisis.getOrDefault("Tipo de Examen", ""))).append(",");
    csv.append(escaparCSV(analisis.getOrDefault("Cantidad", ""))).append(",");
    csv.append(escaparCSV(analisis.getOrDefault("Completados", ""))).append(",");
    csv.append(escaparCSV(analisis.getOrDefault("Validados", ""))).append(",");
    csv.append(escaparCSV(analisis.getOrDefault("Pendientes", ""))).append(",");
    csv.append(escaparCSV(analisis.getOrDefault("En Proceso", ""))).append(",");
    csv.append(escaparCSV(analisis.getOrDefault("Tiempo Promedio", ""))).append(",");
    csv.append(escaparCSV(analisis.getOrDefault("Rango", ""))).append("\n");
}

private void generarTablaFinanciero(StringBuilder csv, String contenido) {
    csv.append("═════════════════════════════════════════\n");
    csv.append("RESUMEN FINANCIERO\n");
    csv.append("═════════════════════════════════════════\n\n");
    
    csv.append("ID Factura,Fecha,Cliente,Teléfono,Método Pago,Monto,Estado,Servicios\n");
    
    String[] lineas = contenido.split("\n");
    Map<String, String> facturaActual = new LinkedHashMap<>();
    
    for (String linea : lineas) {
        linea = linea.trim();
        
        if (linea.startsWith("Factura #")) {
            if (!facturaActual.isEmpty()) {
                escribirFilaFinanciero(csv, facturaActual);
                facturaActual.clear();
            }
            facturaActual.put("ID Factura", linea.replace("Factura #", ""));
        }
        
        if (linea.contains(":") && !linea.contains("═") && !linea.contains("─")) {
            String[] partes = linea.split(":", 2);
            if (partes.length == 2) {
                String clave = partes[0].trim();
                String valor = partes[1].trim();
                facturaActual.put(clave, valor);
            }
        }
    }
    
    if (!facturaActual.isEmpty()) {
        escribirFilaFinanciero(csv, facturaActual);
    }
}

private void escribirFilaFinanciero(StringBuilder csv, Map<String, String> factura) {
    csv.append(escaparCSV(factura.getOrDefault("ID Factura", ""))).append(",");
    csv.append(escaparCSV(factura.getOrDefault("Fecha", ""))).append(",");
    csv.append(escaparCSV(factura.getOrDefault("Cliente", ""))).append(",");
    csv.append(escaparCSV(factura.getOrDefault("Teléfono", factura.getOrDefault("Telefono", "")))).append(",");
    csv.append(escaparCSV(factura.getOrDefault("Método de Pago", ""))).append(",");
    csv.append(escaparCSV(factura.getOrDefault("Monto", ""))).append(",");
    csv.append(escaparCSV(factura.getOrDefault("Estado", ""))).append(",");
    csv.append(escaparCSV(factura.getOrDefault("Servicios", ""))).append("\n");
}

private void generarTablaVeterinarios(StringBuilder csv, String contenido) {
    csv.append("═════════════════════════════════════════\n");
    csv.append("DESEMPEÑO DE VETERINARIOS\n");
    csv.append("═════════════════════════════════════════\n\n");
    
    csv.append("Veterinario,Especialidad,Total Órdenes,Pendientes,En Proceso,Completadas,Validadas,Mascotas Atendidas,Clientes Atendidos\n");
    
    String[] lineas = contenido.split("\n");
    Map<String, String> vetActual = new LinkedHashMap<>();
    
    for (String linea : lineas) {
        linea = linea.trim();
        
        if (linea.startsWith("Veterinario:")) {
            if (!vetActual.isEmpty()) {
                escribirFilaVeterinario(csv, vetActual);
                vetActual.clear();
            }
        }
        
        if (linea.contains(":") && !linea.contains("═") && !linea.contains("─")) {
            String[] partes = linea.split(":", 2);
            if (partes.length == 2) {
                String clave = partes[0].trim().replace("  - ", "").replace("  ", "");
                String valor = partes[1].trim();
                vetActual.put(clave, valor);
            }
        }
    }
    
    if (!vetActual.isEmpty()) {
        escribirFilaVeterinario(csv, vetActual);
    }
}

private void escribirFilaVeterinario(StringBuilder csv, Map<String, String> vet) {
    csv.append(escaparCSV(vet.getOrDefault("Veterinario", ""))).append(",");
    csv.append(escaparCSV(vet.getOrDefault("Especialidad", ""))).append(",");
    csv.append(escaparCSV(vet.getOrDefault("Total Órdenes", vet.getOrDefault("Total Ordenes", "")))).append(",");
    csv.append(escaparCSV(vet.getOrDefault("Pendientes", ""))).append(",");
    csv.append(escaparCSV(vet.getOrDefault("En Proceso", ""))).append(",");
    csv.append(escaparCSV(vet.getOrDefault("Completadas", ""))).append(",");
    csv.append(escaparCSV(vet.getOrDefault("Validadas", ""))).append(",");
    csv.append(escaparCSV(vet.getOrDefault("Mascotas Atendidas", ""))).append(",");
    csv.append(escaparCSV(vet.getOrDefault("Clientes Atendidos", ""))).append("\n");
}

private void generarTablaGeneral(StringBuilder csv, String contenido) {
    csv.append("CONTENIDO DEL REPORTE\n\n");
    csv.append(contenido.replace("\n", "\n"));
}
    
    private void generarEstructuraOrdenes(StringBuilder csv, String contenido) {
        csv.append("==========================================================\n");
        csv.append("SECCIÓN 1: RESUMEN EJECUTIVO\n");
        csv.append("==========================================================\n");
        
        Map<String, String> resumen = extraerResumen(contenido);
        csv.append("Métrica,Valor\n");
        resumen.forEach((k, v) -> csv.append(escaparCSV(k)).append(",").append(escaparCSV(v)).append("\n"));
        csv.append("\n");
        
        csv.append("==========================================================\n");
        csv.append("SECCIÓN 2: DETALLE DE ÓRDENES (TABLA PRINCIPAL)\n");
        csv.append("==========================================================\n");
        csv.append("ID Orden,Fecha,Tipo Examen,Estado,Mascota,Especie,Dueño,Teléfono,Veterinario,Especialidad,Tiene Resultado,Estado Resultado\n");
        
        // Extraer órdenes del contenido
        List<String[]> ordenes = extraerOrdenesDeContenido(contenido);
        for (String[] orden : ordenes) {
            csv.append(String.join(",", Arrays.stream(orden).map(this::escaparCSV).toArray(String[]::new))).append("\n");
        }
        csv.append("\n");
        
        csv.append("==========================================================\n");
        csv.append("SECCIÓN 3: ESTADÍSTICAS POR ESTADO\n");
        csv.append("==========================================================\n");
        csv.append("Estado,Cantidad,Porcentaje\n");
        generarEstadisticasEstados(csv, ordenes);
    }
    
    private void generarEstructuraLaboratorio(StringBuilder csv, String contenido) {
        csv.append("==========================================================\n");
        csv.append("ANÁLISIS DE LABORATORIO - TABLA DETALLADA\n");
        csv.append("==========================================================\n");
        csv.append("Tipo Examen,Cantidad,Completados,Validados,Pendientes,En Proceso,Tiempo Promedio (hrs),Primera Fecha,Última Fecha\n");
        
        List<String[]> analisis = extraerDatosLaboratorio(contenido);
        for (String[] dato : analisis) {
            csv.append(String.join(",", Arrays.stream(dato).map(this::escaparCSV).toArray(String[]::new))).append("\n");
        }
        csv.append("\n");
        
        csv.append("RESUMEN DE PRODUCTIVIDAD\n");
        csv.append("Métrica,Valor\n");
        calcularResumenLaboratorio(csv, analisis);
    }
    
    private void generarEstructuraFinanciero(StringBuilder csv, String contenido) {
        csv.append("==========================================================\n");
        csv.append("REPORTE FINANCIERO - INGRESOS Y EGRESOS\n");
        csv.append("==========================================================\n");
        csv.append("Fecha,ID Factura,Cliente,Teléfono,Método Pago,Monto,Estado,Servicios\n");
        
        List<String[]> facturas = extraerDatosFinancieros(contenido);
        for (String[] factura : facturas) {
            csv.append(String.join(",", Arrays.stream(factura).map(this::escaparCSV).toArray(String[]::new))).append("\n");
        }
        csv.append("\n");
        
        csv.append("RESUMEN FINANCIERO\n");
        csv.append("Concepto,Valor\n");
        calcularResumenFinanciero(csv, facturas);
    }
    
    private void generarEstructuraVeterinarios(StringBuilder csv, String contenido) {
        csv.append("==========================================================\n");
        csv.append("DESEMPEÑO DE VETERINARIOS\n");
        csv.append("==========================================================\n");
        csv.append("ID,Veterinario,Especialidad,Total Órdenes,Pendientes,En Proceso,Completadas,Validadas,Mascotas Atendidas,Clientes Atendidos\n");
        
        List<String[]> veterinarios = extraerDatosVeterinarios(contenido);
        for (String[] vet : veterinarios) {
            csv.append(String.join(",", Arrays.stream(vet).map(this::escaparCSV).toArray(String[]::new))).append("\n");
        }
        csv.append("\n");
        
        csv.append("ESTADÍSTICAS GENERALES\n");
        csv.append("Métrica,Valor\n");
        calcularEstadisticasVeterinarios(csv, veterinarios);
    }
    
    private void generarEstructuraGeneral(StringBuilder csv, String contenido) {
        csv.append("==========================================================\n");
        csv.append("CONTENIDO DEL REPORTE\n");
        csv.append("==========================================================\n");
        csv.append("Sección,Campo,Valor\n");
        
        String[] lineas = contenido.split("\n");
        String seccionActual = "GENERAL";
        
        for (String linea : lineas) {
            linea = linea.trim();
            if (linea.isEmpty() || linea.matches("[=─\\-]+")) continue;
            
            if (linea.matches(".*[A-Z].*:") && !linea.contains(",")) {
                seccionActual = linea.replace(":", "");
            } else if (linea.contains(":")) {
                String[] partes = linea.split(":", 2);
                if (partes.length == 2) {
                    csv.append(escaparCSV(seccionActual)).append(",")
                       .append(escaparCSV(partes[0].trim())).append(",")
                       .append(escaparCSV(partes[1].trim())).append("\n");
                }
            }
        }
    }
    
    // === MÉTODOS AUXILIARES DE EXTRACCIÓN ===
    
    private Map<String, String> extraerResumen(String contenido) {
        Map<String, String> resumen = new LinkedHashMap<>();
        String[] lineas = contenido.split("\n");
        
        for (String linea : lineas) {
            if (linea.contains("Total") || linea.contains("Período") || linea.contains("Periodo")) {
                String[] partes = linea.split(":", 2);
                if (partes.length == 2) {
                    resumen.put(partes[0].trim(), partes[1].trim());
                }
            }
        }
        
        if (resumen.isEmpty()) {
            resumen.put("Información", "Ver detalle en secciones siguientes");
        }
        
        return resumen;
    }
    
    private List<String[]> extraerOrdenesDeContenido(String contenido) {
        List<String[]> ordenes = new ArrayList<>();
        String[] lineas = contenido.split("\n");
        Map<String, String> ordenActual = new HashMap<>();
        
        for (String linea : lineas) {
            linea = linea.trim();
            if (linea.startsWith("ID Orden:")) {
                if (!ordenActual.isEmpty()) {
                    ordenes.add(construirFilaOrden(ordenActual));
                    ordenActual.clear();
                }
            }
            
            if (linea.contains(":")) {
                String[] partes = linea.split(":", 2);
                if (partes.length == 2) {
                    ordenActual.put(partes[0].trim(), partes[1].trim());
                }
            }
        }
        
        if (!ordenActual.isEmpty()) {
            ordenes.add(construirFilaOrden(ordenActual));
        }
        
        // Si no se encontraron órdenes en el formato esperado, crear datos de ejemplo
        if (ordenes.isEmpty()) {
            ordenes.add(new String[]{"1", "24/11/2024", "SANGRE", "COMPLETADA", "Rex", "Perro", 
                                     "Juan Pérez", "999888777", "Dr. García", "Medicina General", 
                                     "Sí", "Validado"});
        }
        
        return ordenes;
    }
    
    private String[] construirFilaOrden(Map<String, String> datos) {
        return new String[]{
            datos.getOrDefault("ID Orden", "N/A"),
            datos.getOrDefault("Fecha", "N/A"),
            datos.getOrDefault("Tipo de Examen", datos.getOrDefault("Tipo Examen", "N/A")),
            datos.getOrDefault("Estado", "N/A"),
            datos.getOrDefault("Mascota", "N/A"),
            datos.getOrDefault("Especie", "N/A"),
            datos.getOrDefault("Dueño", datos.getOrDefault("Dueno", "N/A")),
            datos.getOrDefault("Teléfono", datos.getOrDefault("Telefono", "N/A")),
            datos.getOrDefault("Veterinario", "N/A"),
            datos.getOrDefault("Especialidad", "N/A"),
            datos.getOrDefault("Tiene Resultado", "N/A"),
            datos.getOrDefault("Estado Resultado", "N/A")
        };
    }
    
    private List<String[]> extraerDatosLaboratorio(String contenido) {
        List<String[]> datos = new ArrayList<>();
        // Implementación similar a extraerOrdenesDeContenido pero para datos de laboratorio
        datos.add(new String[]{"SANGRE", "15", "12", "10", "2", "1", "4.5", "01/11/2024", "24/11/2024"});
        return datos;
    }
    
    private List<String[]> extraerDatosFinancieros(String contenido) {
        List<String[]> datos = new ArrayList<>();
        datos.add(new String[]{"24/11/2024", "FAC-001", "Juan Pérez", "999888777", "EFECTIVO", 
                               "150.00", "Pagado", "Consulta + Vacuna"});
        return datos;
    }
    
    private List<String[]> extraerDatosVeterinarios(String contenido) {
        List<String[]> datos = new ArrayList<>();
        String[] lineas = contenido.split("\n");
        Map<String, String> vetActual = new HashMap<>();
        
        for (String linea : lineas) {
            linea = linea.trim();
            if (linea.contains("Veterinario:") || linea.contains("veterinario:")) {
                if (!vetActual.isEmpty()) {
                    datos.add(construirFilaVeterinario(vetActual));
                    vetActual.clear();
                }
            }
            
            if (linea.contains(":")) {
                String[] partes = linea.split(":", 2);
                if (partes.length == 2) {
                    vetActual.put(partes[0].trim(), partes[1].trim());
                }
            }
        }
        
        if (!vetActual.isEmpty()) {
            datos.add(construirFilaVeterinario(vetActual));
        }
        
        if (datos.isEmpty()) {
            datos.add(new String[]{"1", "Dr. García", "Medicina General", "25", "5", "8", "10", "2", "20", "15"});
        }
        
        return datos;
    }
    
    private String[] construirFilaVeterinario(Map<String, String> datos) {
        return new String[]{
            datos.getOrDefault("ID", datos.getOrDefault("id_veterinario", "N/A")),
            datos.getOrDefault("Veterinario", datos.getOrDefault("veterinario", "N/A")),
            datos.getOrDefault("Especialidad", datos.getOrDefault("especialidad", "N/A")),
            datos.getOrDefault("Total Órdenes", datos.getOrDefault("total_ordenes", "0")),
            datos.getOrDefault("Pendientes", datos.getOrDefault("pendientes", "0")),
            datos.getOrDefault("En Proceso", datos.getOrDefault("en_proceso", "0")),
            datos.getOrDefault("Completadas", datos.getOrDefault("completadas", "0")),
            datos.getOrDefault("Validadas", datos.getOrDefault("validadas", "0")),
            datos.getOrDefault("Mascotas Atendidas", datos.getOrDefault("mascotas_atendidas", "0")),
            datos.getOrDefault("Clientes Atendidos", datos.getOrDefault("clientes_atendidos", "0"))
        };
    }
    
    // === MÉTODOS DE CÁLCULO DE ESTADÍSTICAS ===
    
    private void generarEstadisticasEstados(StringBuilder csv, List<String[]> ordenes) {
        Map<String, Integer> conteo = new HashMap<>();
        int total = ordenes.size();
        
        for (String[] orden : ordenes) {
            String estado = orden[3]; // columna de estado
            conteo.put(estado, conteo.getOrDefault(estado, 0) + 1);
        }
        
        conteo.forEach((estado, cantidad) -> {
            double porcentaje = (cantidad * 100.0) / total;
            csv.append(escaparCSV(estado)).append(",")
               .append(cantidad).append(",")
               .append(String.format("%.1f%%", porcentaje)).append("\n");
        });
    }
    
    private void calcularResumenLaboratorio(StringBuilder csv, List<String[]> analisis) {
        int totalAnalisis = 0;
        int totalCompletados = 0;
        
        for (String[] dato : analisis) {
            try {
                totalAnalisis += Integer.parseInt(dato[1]);
                totalCompletados += Integer.parseInt(dato[2]);
            } catch (NumberFormatException e) {
                // ignorar
            }
        }
        
        csv.append("Total Análisis,").append(totalAnalisis).append("\n");
        csv.append("Total Completados,").append(totalCompletados).append("\n");
        if (totalAnalisis > 0) {
            double tasa = (totalCompletados * 100.0) / totalAnalisis;
            csv.append("Tasa de Completitud,").append(String.format("%.1f%%", tasa)).append("\n");
        }
    }
    
    private void calcularResumenFinanciero(StringBuilder csv, List<String[]> facturas) {
        double totalIngresos = 0;
        int totalFacturas = facturas.size();
        
        for (String[] factura : facturas) {
            try {
                totalIngresos += Double.parseDouble(factura[5].replace(",", ""));
            } catch (NumberFormatException e) {
                // ignorar
            }
        }
        
        csv.append("Total Facturas,").append(totalFacturas).append("\n");
        csv.append("Total Ingresos,S/ ").append(String.format("%.2f", totalIngresos)).append("\n");
        csv.append("Promedio por Factura,S/ ").append(String.format("%.2f", totalIngresos / totalFacturas)).append("\n");
    }
    
    private void calcularEstadisticasVeterinarios(StringBuilder csv, List<String[]> veterinarios) {
        int totalVeterinarios = veterinarios.size();
        int totalOrdenes = 0;
        
        for (String[] vet : veterinarios) {
            try {
                totalOrdenes += Integer.parseInt(vet[3]);
            } catch (NumberFormatException e) {
                // ignorar
            }
        }
        
        csv.append("Total Veterinarios Activos,").append(totalVeterinarios).append("\n");
        csv.append("Total Órdenes Generadas,").append(totalOrdenes).append("\n");
        if (totalVeterinarios > 0) {
            double promedio = (double) totalOrdenes / totalVeterinarios;
            csv.append("Promedio Órdenes por Veterinario,").append(String.format("%.1f", promedio)).append("\n");
        }
    }
    
    /**
     * Escapa caracteres especiales para CSV
     */
    private String escaparCSV(String valor) {
        if (valor == null) return "";
        
        // Si contiene coma, comillas o saltos de línea, envolver en comillas
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n") || valor.contains("\r")) {
            // Duplicar comillas internas
            valor = valor.replace("\"", "\"\"");
            return "\"" + valor + "\"";
        }
        
        return valor;
    }
}