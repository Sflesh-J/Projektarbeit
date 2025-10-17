package de.uniwue.apps.jm1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import de.uniwue.apps.jm1.User;
import de.uniwue.apps.jm1.db.DB;

public class UserDao {

    private static final String USER_SELECT = "SELECT * FROM `user`";

    public Optional<User> findByUid(String uid) {
        final String query = USER_SELECT + " WHERE `UID` = ?;";
        try (Connection con = DB.cp.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, uid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        try (Connection con = DB.cp.getConnection();
             PreparedStatement stmt = con.prepareStatement(USER_SELECT + ";")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public void updateAktiv(int userUid, String aktiv) {
        final String sql = "UPDATE `user` SET `Aktiv` = ? WHERE `Uid` = ?;";
        executeSimpleUpdate(userUid, aktiv, sql);
    }

    public void updateStatus(int userUid, String status) {
        final String sql = "UPDATE `user` SET `Status` = ? WHERE `Uid` = ?;";
        executeSimpleUpdate(userUid, status, sql);
    }

    public void updateVorname(int userUid, String vorname) {
        final String sql = "UPDATE `user` SET `Vorname` = ? WHERE `Uid` = ?;";
        executeSimpleUpdate(userUid, vorname, sql);
    }

    public void updateNachname(int userUid, String nachname) {
        final String sql = "UPDATE `user` SET `Nachname` = ? WHERE `Uid` = ?;";
        executeSimpleUpdate(userUid, nachname, sql);
    }

    public void updatePassword(int userUid, String pwd) {
        final String sql = "UPDATE `user` SET `Passwort` = SHA2(?, 256) WHERE `Uid` = ?;";
        try (Connection con = DB.cp.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            if (pwd == null || pwd.isEmpty()) {
                stmt.setNull(1, Types.VARCHAR);
            } else {
                stmt.setString(1, pwd);
            }
            stmt.setInt(2, userUid);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public OptionalInt insertUser(String vorname, String nachname, String status, boolean admin, String pwd) {
        final String sql = "INSERT INTO user(Vorname, Nachname, Guthaben, Passwort, Admin, Status) "
                + "VALUES(?, ?, ?, SHA2(?, 256), ?, ? );";
        try (Connection con = DB.cp.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, vorname);
            stmt.setString(2, nachname);
            stmt.setInt(3, 0);
            if (pwd == null || pwd.isEmpty()) {
                stmt.setNull(4, Types.VARCHAR);
            } else {
                stmt.setString(4, pwd);
            }
            stmt.setString(5, admin ? "TRUE" : "FALSE");
            stmt.setString(6, status);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return OptionalInt.of(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return OptionalInt.empty();
    }

    public void recalculateBalance(int userUid) {
        final String sql =
                "UPDATE `user` SET `Guthaben` = (" +
                        "COALESCE((SELECT SUM(`Summe`) FROM `buchung` WHERE `UserID` = ? AND `Typ` = 'AUFBUCHUNG'), 0) - " +
                        "COALESCE((SELECT SUM(`Summe`) FROM `buchung` WHERE `UserID` = ? AND `Typ` IN ('STRICHE', 'ABBUCHUNG')), 0)" +
                        ") WHERE `UID` = ?;";
        try (Connection con = DB.cp.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, userUid);
            stmt.setInt(2, userUid);
            stmt.setInt(3, userUid);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void executeSimpleUpdate(int userUid, String value, String sql) {
        try (Connection con = DB.cp.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, value);
            stmt.setInt(2, userUid);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("Uid"),
                rs.getString("Vorname"),
                rs.getString("Nachname"),
                rs.getInt("Guthaben"),
                rs.getString("Passwort") == null ? null : rs.getString("Passwort"),
                rs.getString("Status"),
                "TRUE".equals(rs.getString("Admin")),
                "TRUE".equals(rs.getString("Aktiv"))
        );
    }
}
