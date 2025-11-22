package utilidades;

import java.util.regex.Pattern;

/**
 * Utilidades de validacion
 */
public class Validador {
    
    private static final Pattern PATTERN_EMAIL = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern PATTERN_TELEFONO = Pattern.compile(
        "^[0-9]{9,15}$"
    );
    
    // Validar DNI (8 digitos para Peru)
    public static boolean validarDNI(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            return false;
        }
        return dni.matches("^[0-9]{8}$");
    }
    
    // Validar email
    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return PATTERN_EMAIL.matcher(email).matches();
    }
    
    // Validar telefono
    public static boolean validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return false;
        }
        return PATTERN_TELEFONO.matcher(telefono).matches();
    }
    
    // Validar campo no vacio
    public static boolean validarNoVacio(String campo) {
        return campo != null && !campo.trim().isEmpty();
    }
    
    // Validar numero positivo
    public static boolean validarNumeroPositivo(double numero) {
        return numero > 0;
    }
    
    // Validar rango de edad
    public static boolean validarEdad(int edad, int min, int max) {
        return edad >= min && edad <= max;
    }
}