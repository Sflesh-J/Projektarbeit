package de.uniwue.apps.jm1;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import de.uniwue.apps.jm1.db.DB;

public class User implements Serializable  {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3898743429030183341L;
	
	private final int uid;
	private final String vorname;
	private final String nachname;
	private final int guthaben;
	private final String passwort;
	private final String status;
	private final boolean admin;
	private final boolean aktiv;
	public User(int uid,String vorname, String nachname, int guthaben, String passwort,String status, boolean admin, boolean aktiv) {
		this.uid = uid;
		this.vorname = vorname;
		this.guthaben = guthaben;
		this.nachname = nachname;
		this.passwort = passwort;
		this.status = status;
		this.admin = admin;
		this.aktiv = aktiv;
	}
	public int getUid() { return uid; }
	public String getVorname() {return vorname;}
	public int getGuthaben() { return guthaben; }
	public String getNachname() {return nachname;}
	public String getPasswort() {return passwort;}
	public String getStatus() {return status;}
	public boolean isAdmin() {return admin;}
	public boolean isAktiv() {return aktiv;}

	public static User getUserbyUID(String uid) {
		User res = null;

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			con = DB.cp.getConnection();
			
			stmt = con.prepareStatement("SELECT *" +
							" FROM `user`" +
							" WHERE `UID` = ?" +
							";");
			
			((PreparedStatement) stmt).setString(1, uid);
			rs = ((PreparedStatement) stmt).executeQuery();
			
			if (rs.next())
				res = userFromRS(rs);

		} catch (final Exception mye) {
			mye.printStackTrace();
		} finally {
			DB.close(con, rs, stmt);
		}
		
		return res;
	}
	
	
	public static  List<User>getUser() {
		List<User> res = new LinkedList<User>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		 
		try {
			con = DB.cp.getConnection();
			
			stmt = con.createStatement();
			final String query =
					"SELECT *" +
							" FROM `user`" +
							";";
			rs = stmt.executeQuery(query);
			while (rs.next())
				res.add(userFromRS(rs));
 
		} catch (final Exception mye) {
			mye.printStackTrace();
		} finally {
			DB.close(con, rs, stmt);
		}
		return res;
	}
	private static User userFromRS(ResultSet rs) throws SQLException {
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
	
	
	public static void changeAktiv(User user, String aktiv) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			con = DB.cp.getConnection();
			
			
			stmt = con.prepareStatement("UPDATE `user` "
					+ "SET `Aktiv` = ?  "
					+ "WHERE `Uid` = ?;");
			
			stmt.setString(1, aktiv);
			stmt.setInt(2, user.getUid());
			stmt.executeUpdate();
			
		} catch (final Exception mye) {
			mye.printStackTrace();
		} finally {
			DB.close(con, rs, stmt);
		}
	}
	
	public static void changeStatus(User user, String status) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			con = DB.cp.getConnection();
			
			
			stmt = con.prepareStatement("UPDATE `user` "
					+ "SET `Status` = ?  "
					+ "WHERE `Uid` = ?;");
			
			stmt.setString(1, status);
			stmt.setInt(2, user.getUid());
			stmt.executeUpdate();
			
		} catch (final Exception mye) {
			mye.printStackTrace();
		} finally {
			DB.close(con, rs, stmt);
		}
	}
	
	public static void changeVorname(User user, String vorname) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			con = DB.cp.getConnection();
			
			
			stmt = con.prepareStatement("UPDATE `user` "
					+ "SET `Vorname` = ?  "
					+ "WHERE `Uid` = ?;");
			
			stmt.setString(1, vorname);
			stmt.setInt(2, user.getUid());
			stmt.executeUpdate();
			
		} catch (final Exception mye) {
			mye.printStackTrace();
		} finally {
			DB.close(con, rs, stmt);
		}
	}
	
	public static void changeNachname(User user, String nachname) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			con = DB.cp.getConnection();
			
			
			stmt = con.prepareStatement("UPDATE `user` "
					+ "SET `Nachname` = ?  "
					+ "WHERE `Uid` = ?;");
			
			stmt.setString(1, nachname);
			stmt.setInt(2, user.getUid());
			stmt.executeUpdate();
			
		} catch (final Exception mye) {
			mye.printStackTrace();
		} finally {
			DB.close(con, rs, stmt);
		}
	}
	

	
	public static String _getPassword(final String pwd) {
		String res = null;

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = DB.cp.getConnection();

			stmt = con.prepareStatement("SELECT SHA2(?, 256);");
			stmt.setString(1, pwd);
			rs = stmt.executeQuery();
			if (rs.next())
				res = rs.getString(1);

		} catch (final Exception mye) {
			mye.printStackTrace();
		} finally {
			DB.close(con, rs, stmt);
		}

		return res;
	}
	
        public static void setPassword(final String pwd, int userUid) {

                Connection con = null;
                PreparedStatement stmt = null;
                try {
                        con = DB.cp.getConnection();

                        stmt = con.prepareStatement("UPDATE `user` "
                                        + "SET `Passwort` = SHA2(?, 256)  "
                                        + "WHERE `Uid` = ?;");
                        if (pwd == null || pwd.isEmpty()) {
                                stmt.setNull(1, Types.VARCHAR);
                        } else {
                                stmt.setString(1, pwd);
                        }
                        stmt.setInt(2, userUid);

                        stmt.executeUpdate();

                } catch (final Exception mye) {
                        mye.printStackTrace();
                } finally {
                        DB.close(con, null, stmt);
                }
        }
	
	public static void newUser(String vorname, String nachname, String status, String admin, String guthaben, String pwd ) {
		
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		int guthabenNEW = Integer.parseInt(guthaben) * 100;
		
		String user = null;
		try {
			con = DB.cp.getConnection();

			stmt = con.prepareStatement("INSERT INTO user(Vorname, Nachname, Guthaben, Passwort, Admin, Status) "
					+ "VALUES(?, ?, ?, ?, ?, ? ) ", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, vorname);
			stmt.setString(2, nachname);
			
			stmt.setInt(3, 0);
			stmt.setString(4, User._getPassword(pwd));
			if(admin.equals("ja")) {
				stmt.setString(5, "TRUE");
			}else {
				stmt.setString(5, "FALSE");
			}
			stmt.setString(6, status);
			stmt.executeUpdate();
			
			ResultSet rs1 = stmt.getGeneratedKeys();
			if (rs1.next()) {
	            user = rs1.getString(1); 
	        }
			if(user != null) {
				Buchung.erstelleBuchung(User.getUserbyUID(user), guthabenNEW, "Neuer Benutzer Startguthaben", Buchung.Buchungstyp.AUFBUCHUNG);
			}

		} catch (final Exception mye) {
			mye.printStackTrace();
		} finally {
			DB.close(con, rs, stmt);
		}
	}
	
	public static void aktualisiereGuthaben(User user) {
	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	    	con = DB.cp.getConnection();
	        stmt = con.prepareStatement( 
	        		 "UPDATE `user` SET `Guthaben` = (" +
	        		            "COALESCE((SELECT SUM(`Summe`) FROM `buchung` WHERE `UserID` = ? AND `Typ` = 'AUFBUCHUNG'), 0) - " +
	        		            "COALESCE((SELECT SUM(`Summe`) FROM `buchung` WHERE `UserID` = ? AND `Typ` IN ('STRICHE', 'ABBUCHUNG')), 0)" +
	        		            ") " +
	        		            "WHERE `UID` = ?");
	        stmt.setInt(1, user.getUid());
	        stmt.setInt(2, user.getUid());
	        stmt.setInt(3, user.getUid());
	        stmt.executeUpdate();

	    } catch (final Exception mye) {
	        mye.printStackTrace();
	    } finally {
	    	DB.close(con, rs, stmt);
	    }
	}
	
	public static boolean userHatPin(User user) {
		if(user.getPasswort() == null) {
			return false;
		}else {
			return true;
		}
		
	}
		
	
	
}
