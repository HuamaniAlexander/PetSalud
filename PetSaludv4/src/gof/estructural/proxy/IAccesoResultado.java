/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gof.estructural.proxy;

// Interfaz del sujeto

import java.sql.SQLException;
import modelo.entidades.ResultadoVeterinario;
import modelo.entidades.Usuario;

public interface IAccesoResultado {
    ResultadoVeterinario obtenerResultado(int idResultado, Usuario usuario) throws SQLException;
    boolean validarResultado(int idResultado, Usuario usuario) throws SQLException;
    String generarInforme(int idResultado, Usuario usuario);
}