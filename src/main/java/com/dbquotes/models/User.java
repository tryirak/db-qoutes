package com.dbquotes.models;

import com.dbquotes.utils.PasswordEncryptor;
import com.dbquotes.utils.QueryStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Singleton pattern
public class User {
    private long id;
    private String login;
    private UserRole role;
    private static User instance;
    private int count = 0;

    public static User getInstance() {
        if (instance == null)
            instance = new User();
        return instance;
    }

    public void reset() {
        id = -1;
        login = "";
        role = UserRole.GUEST;
        count = 0;
    }

    public QueryStatus auth(String login, String password) {
        String query = "SELECT id, role FROM users WHERE login=? AND password=?;";

        try {
            Connection c = HandlerDB.getConnection();
            PreparedStatement p = c.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            p.setString(1, login);
            p.setString(2, PasswordEncryptor.encrypt(password));
            ResultSet result = p.executeQuery();
            if (result.first()) {
                this.id = result.getLong("id");
                this.login = login;
                this.role = UserRole.getRole(result.getString("role"));
            } else
                return QueryStatus.NO_ENTRY;

            result.close();
            return QueryStatus.DONE;
        } catch (SQLException e) {
            e.printStackTrace();
            return switch (e.getSQLState()) {
                case "08S01" -> QueryStatus.NO_CONNECTION;
                default -> QueryStatus.UNKNOWN;
            };
        } finally {
            HandlerDB.closeConnection();
        }
    }

    public QueryStatus authAsGuest() {
        String query = "SELECT id FROM users WHERE role='GUEST';";

        try {
            Connection c = HandlerDB.getConnection();
            PreparedStatement p = c.prepareStatement(query);
            ResultSet result = p.executeQuery();
            result.next();
            this.id = result.getLong("id");
            this.role = UserRole.GUEST;
            result.close();
            return QueryStatus.DONE;
        } catch (SQLException e) {
            e.printStackTrace();
            return switch (e.getSQLState()) {
                case "08S01" -> QueryStatus.NO_CONNECTION;
                default -> QueryStatus.UNKNOWN;
            };
        } finally {
            HandlerDB.closeConnection();
        }
    }

    public QueryStatus edit(String newLogin, String newPassword) {
        String query = "UPDATE users SET login=?, password=? WHERE id=?;";

        try {
            Connection c = HandlerDB.getConnection();
            PreparedStatement p = c.prepareStatement(query);
            p.setString(1, newLogin);
            p.setString(2, com.dbquotes.utils.PasswordEncryptor.encrypt(newPassword));
            p.setLong(3, id);
            p.executeUpdate();
            return QueryStatus.DONE;
        } catch (SQLException e) {
            e.printStackTrace();
            return switch (e.getSQLState()) {
                case "08S01" -> QueryStatus.NO_CONNECTION;
                case "23000" -> QueryStatus.DUPLICATE;
                default -> QueryStatus.UNKNOWN;
            };
        } finally {
            HandlerDB.closeConnection();
        }
    }

    public static QueryStatus createNewUser(String login, String password) {
        String query = "INSERT INTO users (login, password) VALUES (?, ?)";

        try {
            Connection c = HandlerDB.getConnection();
            PreparedStatement p = c.prepareStatement(query);
            p.setString(1, login);
            p.setString(2, com.dbquotes.utils.PasswordEncryptor.encrypt(password));
            p.executeUpdate();
            return QueryStatus.DONE;
        } catch (SQLException e) {
            e.printStackTrace();
            return switch (e.getSQLState()) {
                case "08S01" -> QueryStatus.NO_CONNECTION;
                case "23000" -> QueryStatus.DUPLICATE;
                default -> QueryStatus.UNKNOWN;
            };
        } finally {
            HandlerDB.closeConnection();
        }
    }

    public void parseCount() {
        String query = "SELECT COUNT(*) FROM quotes WHERE id_creator=?;";

        try {
            Connection c = HandlerDB.getConnection();
            PreparedStatement p = c.prepareStatement(query);
            p.setLong(1, id);
            ResultSet result = p.executeQuery();
            result.next();
            count = result.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            HandlerDB.closeConnection();
        }
    }

    public void countUpdate(boolean add) {
        if (add) count++;
        else count--;
    }

    public String getLogin() {
        return login;
    }

    public long getId() {
        return id;
    }

    public int getCount() {
        return count;
    }
}