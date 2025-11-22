package gof.estructural.decorator;

import modelo.entidades.ResultadoVeterinario;

/**
 * Patron Decorator - Permite agregar funcionalidades a informes dinamicamente
 */

// Componente concreto
public class InformeBasico implements IInforme {
    private ResultadoVeterinario resultado;
    
    public InformeBasico(ResultadoVeterinario resultado) {
        this.resultado = resultado;
    }
    
    @Override
    public String generar() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== INFORME VETERINARIO ===\n\n");
        sb.append("ID Resultado: ").append(resultado.getIdResultado()).append("\n");
        sb.append("Fecha: ").append(resultado.getFechaResultado()).append("\n\n");
        sb.append("DESCRIPCION:\n").append(resultado.getDescripcion()).append("\n\n");
        sb.append("VALORES:\n").append(resultado.getValores()).append("\n\n");
        sb.append("CONCLUSIONES:\n").append(resultado.getConclusiones()).append("\n");
        sb.append("Validado: ").append(resultado.isValidado() ? "SI" : "NO").append("\n");
        return sb.toString();
    }
    
    @Override
    public String getTitulo() {
        return "Informe Basico";
    }
    
    public ResultadoVeterinario getResultado() {
        return resultado;
    }
}
