package com.dbquotes.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public record Permissions(boolean r, boolean w, boolean d) {
    public static HashMap<User, Permissions> getPermissionsByRecordID(long id) {
        String query = "SELECT users.id as id, users.login as name, users.role as role, " +
                "access.read_access as r, access.write_access as w, access.delete_access as d FROM access " +
                "INNER JOIN users ON access.id_user=users.id WHERE id_quote=?;";
        HashMap<User, Permissions> userPermissionsHashMap = new HashMap<>();

        try {
            Connection c = HandlerDB.getConnection();
            PreparedStatement p = c.prepareStatement(query);
            p.setLong(1, id);
            ResultSet result = p.executeQuery();
            while (result.next()) {
                User user = new User(result.getLong("id"),
                        result.getString("name"),
                        UserRole.getRole(result.getString("role")));
                Permissions userPermissions = new Permissions(
                        result.getBoolean("r"),
                        result.getBoolean("w"),
                        result.getBoolean("d")
                );
                userPermissionsHashMap.put(user, userPermissions);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userPermissionsHashMap;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (r) builder.append('r');
        else builder.append('-');
        if (w) builder.append('w');
        else builder.append('-');
        if (d) builder.append('d');
        else builder.append('-');
        return builder.toString();
    }
}
