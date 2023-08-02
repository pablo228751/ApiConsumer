package ar.com.unont.dato5.utils;

import java.sql.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcUtils {

    private Connection connection;

    public boolean openConnection(String ip, int puerto, String usr, String pass, String db) {
        String url = "jdbc:mysql://" + ip + ":" + puerto + "/" + db;
        try {
            connection = DriverManager.getConnection(url, usr, pass);
            return true;
        } catch (SQLException e) {
            log.error("Error al conectar a la base de datos: " + ip);
            return false;
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            log.error("Error al cerrar la conexión");
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            log.error("Error al ejecutar la consulta");
            return null; 
        }
    }

    public int executeUpdate(String query) {
        try {
            if (connection != null) {
                Statement statement = connection.createStatement();
                return statement.executeUpdate(query);
            } else {
                return -1; 
            }
        } catch (SQLException e) {
            log.error("Error al actualizar");
            return -1;
        }
    }
}
