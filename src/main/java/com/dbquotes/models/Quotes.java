package com.dbquotes.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Quotes extends ArrayList<Quote> {

    public void parse(ApplicationUser applicationUser) {
        String query = "SELECT DISTINCT quotes.id as id, quote, teacher, subject, date, login as owner, " +
                "IF(id_creator=?,true, op_read) as r, IF(id_creator=?, true, op_write) as w, IF(id_creator=?,true, op_delete) as d " +
                "FROM quotes INNER JOIN access INNER JOIN users ON quotes.id = access.id_quote AND users.id=quotes.id_creator " +
                "WHERE id_creator=? OR id_user=?;";

        try {
            Connection c = HandlerDB.getConnection();
            PreparedStatement p = c.prepareStatement(query);
            int count = (int) query.chars().filter(ch -> ch == '?').count();
            for (int i = 1; i <= count; i++)
                p.setLong(i, applicationUser.getId());
            ResultSet result = p.executeQuery();

            while (result.next()) {
                long id = result.getLong("id");
                String quote = result.getString("quote");
                String teacher = result.getString("teacher");
                String subject = result.getString("subject");
                Date date = result.getDate("date");
                String owner = result.getString("owner");
                boolean r = result.getBoolean("r");
                boolean w = result.getBoolean("w");
                boolean d = result.getBoolean("d");
                Quote q = new Quote(id, quote, teacher, subject, date, owner, new Permissions(r, w, d));
                this.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            HandlerDB.closeConnection();
        }
    }
}
