package de.uniwue.apps.jm1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniwue.apps.jm1.db.DB;

public class Buchung {
	
	public static enum Buchungstyp { STRICHE, AUFBUCHUNG, ABBUCHUNG }
	
	private final HashMap<Item, Integer> itemlist;
	private final int uid;
	private final int summe;
	private final String kommentar;
	private final int userID;
	private final Date datum;
	private final Buchungstyp bt;
	
	public Buchung(HashMap<Item, Integer> itemlist, int uid, int summe, String kommentar, int userID, Date datum, Buchungstyp bt) {
		super();
		this.itemlist = itemlist;
		this.uid = uid;
		this.summe = summe;
		this.kommentar = kommentar;
		this.userID = userID;
		this.datum = datum;
		this.bt = bt;
		
	}
	
	public HashMap<Item, Integer> getItemlist() {return itemlist;}
	public int getUid() {return uid;}
	public int getSumme() {return summe;}
	public String getKommentar() {return kommentar;}
	public int getUserID() {return userID;}
	
	public Date getDatum() {return datum;}
	
	public Buchungstyp getBt() { return bt;}

	
	public static List<Buchung> getAllBuchungen() {
		
		List<Buchung> res = new LinkedList<Buchung>();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			con = DB.cp.getConnection();
			 
			stmt = con.prepareStatement("SELECT * "
					+ "FROM buchung");
			rs = stmt.executeQuery();
			

			
			while (rs.next())
				res.add(
						
						 new Buchung(
								new HashMap<>(),
								rs.getInt("uid"),
								rs.getInt("Summe"),
								rs.getString("Kommentar"),
								rs.getInt("UserID"),
								rs.getDate("Datum"),
								Buchungstyp.valueOf(rs.getString("Typ"))
								));
			
		} catch (final Exception mye) {
			mye.printStackTrace();
		} finally {
			DB.close(con, rs, stmt);
		}
		
		for(Buchung b : res) {
			Buchung.getItemlistFromStrich(b);
		}
		
		return res;
	}
	
	
	
	public static void erstelleBuchung(User user,int summe,String kommentar,Buchungstyp typ) {
		erstelleBuchung(null, user, summe, kommentar, typ);
	}
	
	public static int erstelleBuchung(HashMap<Item, Integer> itemlist, User user,int summe,String kommentar,Buchungstyp typ) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int buchungID = -1;
		
		try {
			con = DB.cp.getConnection();
			
			stmt = con.prepareStatement(
					"INSERT INTO `buchung`"
					+ " (`UserID`, `Kommentar`,`Summe`, `Datum`,`Typ`)"
					+ " VALUES (?, ?,?, NOW(), ?)"
					+ ";", Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, user.getUid());
			stmt.setString(2, kommentar);
			stmt.setInt(3, summe);
			stmt.setString(4, typ.toString());
			stmt.executeUpdate();
			
			if (itemlist != null) {
			
			ResultSet rs1 = stmt.getGeneratedKeys();
			if (rs1.next()) {
	            buchungID = rs1.getInt(1); 
	        }
			
			
			for(HashMap.Entry<Item, Integer> i : itemlist.entrySet()) {
				Item item = i.getKey();
				for(int j = 0; j < i.getValue(); j++) {
					Item.bucheItem(item, buchungID, item.getItempreisID());
				}
			}
			}
			if(typ.equals(Buchungstyp.AUFBUCHUNG)) {
				User.aktualisiereGuthaben(user);
			}else {
				User.aktualisiereGuthaben(user);
			}
			
		} catch (final Exception mye) {
			mye.printStackTrace();
		} finally {
			DB.close(con, rs, stmt);
		}
		
		return buchungID;
	}
	
	
	// Ändern zu itempreis id
	public static void _aktualiesiereSumme(int buchungID) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			con = DB.cp.getConnection();
			stmt = con.prepareStatement("UPDATE `buchung` SET `Summe` = ((SELECT SUM(`itempreis`.`Preis`) "
					+ "FROM `strich` "
					+ "INNER JOIN `itempreis` ON `strich`.`Item-ID` = `itempreis`.`Itemid` "  //alte preise beachten
					+ "WHERE `strich`.`Buchung-ID` = ?)"
					+ " WHERE `Buchung-ID` = ?;) / 100");
		
			stmt.setInt(1, buchungID);
			stmt.setInt(2, buchungID);
			stmt.executeUpdate();
			
		} catch (final Exception mye) {
			mye.printStackTrace();
		} finally {
			DB.close(con, rs, stmt);
		}
	}
	
	
	public static List<Buchung> getBuLi(User user){
		List<Buchung> res = new LinkedList<Buchung>();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			con = DB.cp.getConnection();
			 
			stmt = con.prepareStatement("SELECT * "
					+ "FROM buchung "
					+ "WHERE `UserID` = ? ");
			stmt.setInt(1, user.getUid());
			rs = stmt.executeQuery();
			

			
			while (rs.next())
				res.add(
						
						 new Buchung(
								new HashMap<>(),
								rs.getInt("uid"),
								rs.getInt("Summe"),
								rs.getString("Kommentar"),
								rs.getInt("UserID"),
								rs.getDate("Datum"),
								Buchungstyp.valueOf(rs.getString("Typ"))
								));
			
		} catch (final Exception mye) {
			mye.printStackTrace();
		} finally {
			DB.close(con, rs, stmt);
		}
		
		for(Buchung b : res) {
			Buchung.getItemlistFromStrich(b);
		}
		
		return res;
	}
	
	
	public static Buchung getBuchung(int buchung){
		Buchung res = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			con = DB.cp.getConnection();
			 
			stmt = con.prepareStatement("SELECT * "
					+ "FROM buchung "
					+ "WHERE `UID` = ? ");
			stmt.setInt(1, buchung);
			rs = stmt.executeQuery();
			

			
			while (rs.next())
				res = new Buchung(
								new HashMap<>(),
								rs.getInt("uid"),
								rs.getInt("Summe"),
								rs.getString("Kommentar"),
								rs.getInt("UserID"),
								rs.getDate("Datum"),
								Buchungstyp.valueOf(rs.getString("Typ"))
								);
			
		} catch (final Exception mye) {
			mye.printStackTrace();
		} finally {
			DB.close(con, rs, stmt);
		}
		
		return res;
	}
	
	
	public static void getItemlistFromStrich(Buchung b){
		
		Map<Integer, Integer> itemuid2amnt = new HashMap<>();
		{
			Connection con = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			try {
				con = DB.cp.getConnection();
				 
				stmt = con.prepareStatement("SELECT * "
						+ "FROM strich "
						+ "WHERE `Buchung-ID` = ? ");
				stmt.setInt(1, b.getUid());
				rs = stmt.executeQuery();
			
				while (rs.next()) {
					int uid = rs.getInt("Item-ID");
					itemuid2amnt.put(uid, (itemuid2amnt.containsKey(uid) ? itemuid2amnt.get(uid) : 0) + 1);
				}
			} catch (final Exception mye) {
				mye.printStackTrace();
			} finally {
				DB.close(con, rs, stmt);
			}
		}
		
		for(Map.Entry<Integer, Integer> i : itemuid2amnt.entrySet()) {
			b.itemlist.put(Item.getItem(i.getKey()), i.getValue());
		}
		
	}
	public static String itemlistToString(Buchung buchung) {
		
		HashMap<Item, Integer> itemlist =  buchung.getItemlist();

		List<String> items = new LinkedList<>();
		for (Item i : itemlist.keySet())
			items.add(i.getName() + " " + itemlist.get(i) + "x");
		
		return String.join(" || ", items);

	}
	
	public static boolean loescheBuchung(int buchungID, User user) {
	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    boolean erfolg = false;
	    
	    try {
	        con = DB.cp.getConnection();

	        stmt = con.prepareStatement("SELECT * FROM buchung WHERE uid = ? AND UserID = ?");
	        stmt.setInt(1, buchungID);
	        stmt.setInt(2, user.getUid());
	        rs = stmt.executeQuery();
	        
	        if (!rs.next()) {
	            return false;
	        }
	        rs.close();
	        stmt.close();

	        stmt = con.prepareStatement("DELETE FROM strich WHERE `Buchung-ID` = ?");
	        stmt.setInt(1, buchungID);
	        stmt.executeUpdate();
	        stmt.close();

	        stmt = con.prepareStatement("DELETE FROM buchung WHERE uid = ?");
	        stmt.setInt(1, buchungID);
	        int deletedRows = stmt.executeUpdate();
	        
	        if (deletedRows > 0) {
	                try{
	                	User.aktualisiereGuthaben(user);
	                	erfolg = true;
	                }catch(Exception e) {
	                	return false;
	                }
	        }
	        
	        
	    } catch (final Exception mye) {
	    	
	    } finally {
	        DB.close(con, rs, stmt);
	    }
	    
	    return erfolg;
	}
	
	
        public static void bearbeiteBuchung(List<Integer> itempreisID,HashMap<Item, Integer> itemlist, User user,int summe,String kommentar,Buchungstyp typ, int buchungID) {


                Connection con = null;
                PreparedStatement stmt = null;

                if(typ == Buchungstyp.STRICHE || typ == Buchungstyp.ABBUCHUNG) {
                        summe = summe * -1;
                }
		
		
		try {
			con = DB.cp.getConnection();
			
			stmt = con.prepareStatement("UPDATE `buchung` SET `Summe` = ?, `Kommentar` = ?, `Typ` = ? WHERE `uid` = ?;");
			stmt.setInt(1, summe);
			stmt.setString(2, kommentar);
			stmt.setString(3, typ.toString());
			stmt.setInt(4, buchungID);
			stmt.executeUpdate();
			
			stmt = con.prepareStatement("DELETE FROM strich WHERE `Buchung-ID` = ?");
			stmt.setInt(1, buchungID);
			stmt.executeUpdate();
			int zaehler = 0;
			for(HashMap.Entry<Item, Integer> i : itemlist.entrySet()) {
				Item item = i.getKey();
				for(int j = 0; j < i.getValue(); j++) {
					Item.bucheItem(item, buchungID, itempreisID.get(zaehler));
				}
				zaehler++;
			}
			
			User.aktualisiereGuthaben(user);
			
			
		} catch (final Exception mye) {
			mye.printStackTrace();
		} finally {
			DB.close(con, null, stmt);
		}
		
		
	}
	
	// In deiner Buchung-Klasse oder einer Utility-Klasse
	public boolean istLoeschbar() {
	    long differenzInMillis = new Date().getTime() - this.datum.getTime();
	    long differenzInTagen = differenzInMillis / (1000 * 60 * 60 * 24);
	    return differenzInTagen <= 2;
	}
	
}
