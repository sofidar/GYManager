package interfaces;

import java.util.List;

public interface IDAO<T> {
    List<T> listar();
    boolean crear(T entidad);
    boolean eliminar(int id);
}
