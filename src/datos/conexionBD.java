package datos;

import modelo.*;

import java.sql.*;


public class conexionBD {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String BBDD = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "gym";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    //Conexion a la base de datos php/////////////////
    public Connection conexionBBDD() {
        Connection conexion = null;
        try {
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(BBDD + DB_NAME, USUARIO, PASSWORD);
            System.out.println("conexion ok a " + DB_NAME);
        } catch (ClassNotFoundException e) {
            System.err.println("Error en DRIVER\n" + e);
        } catch (SQLException e) {
            System.err.println("Error al conectar con la BBDD\n" + e);
        }
        return conexion;
    }

    /// ////////////////////////////////////////////////
    public void cerrarConexion(Connection conexion) {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Se ha producido un error al cerrar la conexión con la base de datos." + e);
        }
    }

    /// ////////////////////////////////////////////////
    public void crearTablayBD() {
        Connection conexion = null;
        Statement declaracion = null;
        try {
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(BBDD, USUARIO, PASSWORD); // conecta al server
            declaracion = conexion.createStatement();

            //Crear la base de datos si no existe y usarla
            declaracion.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            declaracion.executeUpdate("USE " + DB_NAME);

            //Tablas
            String sql;

            sql = "CREATE TABLE IF NOT EXISTS socios (" +
                    "idSocio INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(100) NOT NULL, " +
                    "correo VARCHAR(150) NOT NULL UNIQUE, " +
                    "contrasena VARCHAR(255) NOT NULL, " +
                    "membresia VARCHAR(50) " +
                    ")";
            declaracion.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS empleados (" +
                    "idEmpleado INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(100) NOT NULL, " +
                    "correo VARCHAR(150) NOT NULL UNIQUE, " +
                    "contrasena VARCHAR(255) NOT NULL" +
                    ")";
            declaracion.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS actividades (" +
                    "idActividad INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(150) NOT NULL, " +
                    "horario VARCHAR(100), " +
                    "sector VARCHAR(100), " +
                    "capacidad INT NOT NULL" +
                    ")";
            declaracion.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS reservas (" +
                    "idReserva INT AUTO_INCREMENT PRIMARY KEY, " +
                    "idSocio INT NOT NULL, " +
                    "idActividad INT NOT NULL, " +
                    "FOREIGN KEY (idSocio) REFERENCES socios(idSocio) ON DELETE CASCADE, " +
                    "FOREIGN KEY (idActividad) REFERENCES actividades(idActividad) ON DELETE CASCADE" +
                    ")";
            declaracion.executeUpdate(sql);

            System.out.println("Base de datos y tablas creadas correctamente.");

        } catch (ClassNotFoundException e) {
            System.err.println("Error en Driver.\n" + e);
        } catch (SQLException e) {
            System.err.println("Error en la conexion con Base de datos.\n" + e);
        } finally {
            if (declaracion != null) try { declaracion.close(); } catch (SQLException ignored) {}
            cerrarConexion(conexion);
        }
    }


}


