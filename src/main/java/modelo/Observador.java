package modelo;

/**
 * Interfaz con los metodos de los observers.
 * @author jesus
 */
public interface Observador {

    /*
    Metodo que notifica cuando algo cambia en el modelo, 
    las clases suscritas lo utilizan para redibujar elementos.
    */
    void actualizar();
    
}
