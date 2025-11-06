package interfaces;

import excepciones.UsuarioNoEncontradoException;

public interface IVerificable {
    boolean verificar()throws UsuarioNoEncontradoException;
}

