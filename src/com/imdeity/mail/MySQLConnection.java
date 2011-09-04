package com.imdeity.mail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import com.avaje.ebeaninternal.server.lib.sql.DataSourceException;

public class MySQLConnection {

    private Connection conn;

    public MySQLConnection() {

        // Load the driver instance
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            throw new DataSourceException(
                    "[Mail] Failed to initialize JDBC driver");
        }

        // make the connection
        try {
            conn = DriverManager.getConnection(getConnectionString());
            System.out.println("[Mail] Connection success!");
        } catch (SQLException ex) {
            dumpSqlException(ex);
            throw new DataSourceException(
                    "[Mail] Failed to create connection to Mysql database");
        }

        createDatabaseTables();
    }

    public void createDatabaseTables() {

        Write("CREATE TABLE IF NOT EXISTS " + tableName("mail") + " ("
                + "`id` INT(16) NOT NULL AUTO_INCREMENT ,"
                + "`sender` VARCHAR(16) NOT NULL ,"
                + "`receiver` VARCHAR(16) NOT NULL ,"
                + "`message` TEXT NOT NULL ,"
                + "`read` INT(1) NOT NULL DEFAULT  '0'," + "PRIMARY KEY (`id`),"
                + "INDEX (`receiver`)"
                + ") ENGINE = MyISAM COMMENT = 'In Game Mail';");

    }

    // check if its closed
    private void reconnect() {
        System.out.println("[Mail] Reconnecting to MySQL...");
        try {
            conn = DriverManager.getConnection(getConnectionString());
            System.out.println("[Mail] Connection success!");
        } catch (SQLException ex) {
            System.out
                    .println("[Mail] Connection to MySQL failed! Check status of MySQL server!");
            dumpSqlException(ex);
        }
    }

    private PreparedStatement prepareSqlStatement(String sql, Object[] params)
            throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql);

        int counter = 1;

        for (Object param : params) {
            if (param instanceof Integer) {
                stmt.setInt(counter++, (Integer) param);
            } else if (param instanceof Long) {
                stmt.setLong(counter++, (Long) param);
            } else if (param instanceof Double) {
                stmt.setDouble(counter++, (Double) param);
            } else if (param instanceof String) {
                stmt.setString(counter++, (String) param);
            } else {
                System.out.printf("Database -> Unsupported data type %s", param
                        .getClass().getSimpleName());
            }
        }
        return stmt;
    }

    // write query
    public boolean Write(String sql, Object... params) {
        try {
            ensureConnection();
            PreparedStatement stmt = prepareSqlStatement(sql, params);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            dumpSqlException(ex);
            return false;
        }
    }

    // write query
    public boolean WriteNoError(String sql) {
        try {
            PreparedStatement stmt = null;
            stmt = this.conn.prepareStatement(sql);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    private String getConnectionString() {
        return "jdbc:mysql://" + Settings.getMySQLServerAddress() + ":"
                + Settings.getMySQLServerPort() + "/"
                + Settings.getMySQLDatabaseName() + "?user="
                + Settings.getMySQLUsername() + "&password="
                + Settings.getMySQLPassword();
    }

    public String tableName(String nameOfTable) {
        return (String.format("`%s`.`%s`", Settings.getMySQLDatabaseName(),
                Settings.getMySQLDatabaseTablePrefix() + nameOfTable));
    }

    private void dumpSqlException(SQLException ex) {
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
        ex.printStackTrace();
    }

    private void ensureConnection() {
        try {
            if (!conn.isValid(5)) {
                reconnect();
            }
        } catch (SQLException ex) {
            dumpSqlException(ex);
        }
    }

    // Get Int
    // only return first row / first field
    public Integer GetInt(String sql) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Integer result = 0;

        /*
         * Double check connection to MySQL
         */
        try {
            if (!conn.isValid(5)) {
                reconnect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stmt = this.conn.prepareStatement(sql);
            if (stmt.executeQuery() != null) {
                stmt.executeQuery();
                rs = stmt.getResultSet();
                if (rs.next()) {
                    result = rs.getInt(1);
                } else {
                    result = 0;
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return result;
    }

    // read query
    public HashMap<Integer, ArrayList<String>> Read(String sql, Object... params) {

        /*
         * Double check connection to MySQL
         */
        try {
            if (!conn.isValid(5)) {
                reconnect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<Integer, ArrayList<String>> Rows = new HashMap<Integer, ArrayList<String>>();

        try {
            stmt = prepareSqlStatement(sql, params);
            if (stmt.executeQuery() != null) {
                stmt.executeQuery();
                rs = stmt.getResultSet();
                while (rs.next()) {
                    ArrayList<String> Col = new ArrayList<String>();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        Col.add(rs.getString(i));
                    }
                    Rows.put(rs.getRow(), Col);
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            // release dataset
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
        }
        return Rows;
    }

}
