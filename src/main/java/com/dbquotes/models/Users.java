package com.dbquotes.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class Users extends HashSet<User> {

    public boolean contain(User user) {
        return this.stream().toList().contains(user);
    }

    public User getByName(String user_name) {
        for (User user : this) {
            if (user.name().equals(user_name))
                return user;
        }
        return null;
    }

    public void parse(ApplicationUser applicationUser) {
        String query = "SELECT id, login as name, role FROM users " +
                "WHERE role!='SUPERUSER' AND role!='GUEST' AND id!=? " +
                "AND id NOT IN (SELECT id_moderator FROM moderator_appointments WHERE id_user=?);";

        try {
            Connection c = HandlerDB.getConnection();
            PreparedStatement p = c.prepareStatement(query);
            p.setLong(1, applicationUser.getId());
            p.setLong(2, applicationUser.getId());
            ResultSet result = p.executeQuery();

            while (result.next())
                this.add(
                        new User(result.getLong("id"),
                                result.getString("name"),
                                UserRole.getRole(result.getString("role"))
                        ));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            HandlerDB.closeConnection();
        }
    }
}
