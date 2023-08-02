package ar.com.unont.dato5.service;

import org.springframework.stereotype.Service;
import ar.com.unont.dato5.utils.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class DatabaseService {

    private final JdbcUtils jdbcUtils;

    public DatabaseService() {
        this.jdbcUtils = new JdbcUtils();
    }

    public boolean openConnection(String ip, int puerto, String usr, String pass, String db) {
        jdbcUtils.openConnection(ip, puerto, usr, pass, db);
        return true;
    }

    public void closeConnection() {
        jdbcUtils.closeConnection();
    }

    public ResultSet executeQuery(String query) throws SQLException {
        return jdbcUtils.executeQuery(query);
    }

    public int executeUpdate(String query) throws SQLException {
        return jdbcUtils.executeUpdate(query);
    }
}
