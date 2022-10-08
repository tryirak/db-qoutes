package com.dbquotes.models;

import com.dbquotes.utility.QueryStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public record Quote(long id, String quote, String teacher, String subject,
                    Date date, String owner, Permissions permissions) {

    public QueryStatus deleteQuote(ApplicationUser applicationUser) {
        String queryCheckPermissions = "SELECT (SELECT id_creator=? FROM quotes WHERE id=?) OR " +
                "(SELECT op_delete FROM access WHERE id_quote=? AND id_user=?);";
        String queryDelete = "DELETE FROM quotes WHERE quotes.id=?;";

        try {
            Connection c = HandlerDB.getConnection();
            PreparedStatement pPermission = c.prepareStatement(queryCheckPermissions);
            PreparedStatement pDelete = c.prepareStatement(queryDelete);
            pPermission.setLong(1, applicationUser.getId());
            pPermission.setLong(2, id);
            pPermission.setLong(3, id);
            pPermission.setLong(4, applicationUser.getId());
            pDelete.setLong(1, id);
            ResultSet result = pPermission.executeQuery();
            result.next();
            if (result.getString(1).equals("NULL"))
                return QueryStatus.NO_ENTRY;
            if (result.getBoolean(1))
                pDelete.executeUpdate();
            else
                return QueryStatus.NO_PERMISSIONS;

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

    public QueryStatus editQuote(String quote, String teacher, String subject, Date date, ApplicationUser applicationUser,
                                 HashMap<User, Permissions> usersPermissionsHashMap) {
        String queryCheckPermissions = "SELECT (SELECT id_creator=? FROM quotes WHERE id=?) OR " +
                "(SELECT op_write FROM access WHERE id_quote=? AND id_user=?);";

        String queryUpdateQuotes = "UPDATE quotes SET quote=?, teacher=?, subject=?, date=? WHERE id=?;";

        StringBuilder queryUpdateAccessBuilder = new StringBuilder("INSERT INTO access VALUES ");
        queryUpdateAccessBuilder.append("(?, ?, ?, ?, ?),".repeat(usersPermissionsHashMap.size()));
        queryUpdateAccessBuilder.deleteCharAt(queryUpdateAccessBuilder.length()-1);
        queryUpdateAccessBuilder.append(" ON DUPLICATE KEY UPDATE " +
                "op_read = VALUES(op_read), op_write = VALUES(op_write), op_delete=VALUES(op_delete);");
        String queryUpdateAccess = queryUpdateAccessBuilder.toString();

        try {
            Connection c = HandlerDB.getConnection();
            PreparedStatement pPermission = c.prepareStatement(queryCheckPermissions);
            PreparedStatement pQuotes = c.prepareStatement(queryUpdateQuotes);
            PreparedStatement pAccess = c.prepareStatement(queryUpdateAccess);

            pPermission.setLong(1, applicationUser.getId());
            pPermission.setLong(2, id);
            pPermission.setLong(3, id);
            pPermission.setLong(4, applicationUser.getId());
            ResultSet result = pPermission.executeQuery();
            result.next();
            boolean flag = result.getBoolean(1);

            if (!flag)
                return QueryStatus.NO_PERMISSIONS;

            pQuotes.setString(1, quote);
            pQuotes.setString(2, teacher);
            pQuotes.setString(3, subject);
            pQuotes.setDate(4, new java.sql.Date(date.getTime()));
            pQuotes.setLong(5, id);
            pQuotes.executeUpdate();

            if (!usersPermissionsHashMap.isEmpty()) {
                int i = 1;
                for (Map.Entry<User, Permissions> set: usersPermissionsHashMap.entrySet()) {
                    User user = set.getKey();
                    Permissions perm = set.getValue();
                    pAccess.setLong(i++, id);
                    pAccess.setLong(i++, user.id());
                    pAccess.setBoolean(i++, perm.r());
                    pAccess.setBoolean(i++, perm.w());
                    pAccess.setBoolean(i++, perm.d());
                }
                pAccess.executeUpdate();
            }

            return QueryStatus.DONE;
        } catch (SQLException e) {
            e.printStackTrace();
            return switch (e.getSQLState()) {
                case "08S01" -> QueryStatus.NO_CONNECTION;
                case "42000" -> QueryStatus.CUSTOM;
                default -> QueryStatus.UNKNOWN;
            };
        } finally {
            HandlerDB.closeConnection();
        }
    }

    public static QueryStatus createQuote(String quote, String teacher, String subject, Date date, ApplicationUser applicationUser,
                                          HashMap<User, Permissions> usersPermissionsHashMap) {
        if (applicationUser.getRole() == UserRole.GUEST)
            return QueryStatus.NO_PERMISSIONS;

        ArrayList<Long> moderatorsId = applicationUser.getModerators();

        // Insert quote to Quotes
        String queryInsertQuote = "INSERT INTO quotes(quote, teacher, subject, date, id_creator) VALUES (?, ?, ?, ?, ?);";

        // Get id of inserted quote
        String queryGetID = "SELECT id FROM quotes WHERE quote=? AND teacher=? AND subject=? AND date=? AND id_creator=?;";

        // Template for insert users permissions
        StringBuilder queryInsertUsersBuilder = new StringBuilder(
                "INSERT INTO access (id_quote, id_user, op_read, op_write, op_delete) VALUES");
        queryInsertUsersBuilder.append("(?, ?, ?, ?, ?),".repeat(usersPermissionsHashMap.size()));
        queryInsertUsersBuilder.deleteCharAt(queryInsertUsersBuilder.length()-1);
        queryInsertUsersBuilder.append(" ON DUPLICATE KEY UPDATE " +
                "op_read=VALUES(op_read), op_write=VALUES(op_write), op_delete=VALUES(op_delete);");
        String queryInsertUsers = queryInsertUsersBuilder.toString();

        try {
            Connection c = HandlerDB.getConnection();
            PreparedStatement pQuote = c.prepareStatement(queryInsertQuote);
            PreparedStatement pGetID = c.prepareStatement(queryGetID);
            PreparedStatement pAccess = c.prepareStatement(queryInsertUsers);

            pQuote.setString(1, quote);
            pQuote.setString(2, teacher);
            pQuote.setString(3, subject);
            pQuote.setDate(4, new java.sql.Date(date.getTime()));
            pQuote.setLong(5, applicationUser.getId());
            pQuote.executeUpdate();

            if (!usersPermissionsHashMap.isEmpty()) {

                pGetID.setString(1, quote);
                pGetID.setString(2, teacher);
                pGetID.setString(3, subject);
                pGetID.setDate(4, new java.sql.Date(date.getTime()));
                pGetID.setLong(5, applicationUser.getId());
                ResultSet result = pGetID.executeQuery();
                result.next();
                long id = result.getLong("id");

                System.out.println(queryInsertUsers);

                int i = 1;
                for (Map.Entry<User, Permissions> set: usersPermissionsHashMap.entrySet()) {
                    User user = set.getKey();
                    Permissions permission = set.getValue();
                    if (user.role() != UserRole.SUPERUSER && !moderatorsId.contains(user.id())) {
                        pAccess.setLong(i++, id);
                        pAccess.setLong(i++, user.id());
                        pAccess.setBoolean(i++, permission.r());
                        pAccess.setBoolean(i++, permission.w());
                        pAccess.setBoolean(i++, permission.d());
                    }
                }
                pAccess.executeUpdate();
            }
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
}
