package gof.comportamiento.command;

// Interfaz Command
public interface ICommand {
    void ejecutar();
    void deshacer();
    String getDescripcion();
}
