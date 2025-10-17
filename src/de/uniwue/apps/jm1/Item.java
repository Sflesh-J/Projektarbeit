package de.uniwue.apps.jm1;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import de.uniwue.apps.jm1.db.DB;

public class Item {
	


		private final int uid;
		private final String name;
		private final String iconUrl;
		private final int preis;
		private final int itempreisID;
		private boolean isAktiv;
		public Item(int uid,String name, String iconUrl, int preis,int  itempreisID ,boolean isAktiv) {
			this.uid = uid;
			this.name = name;
			this.preis = preis;
			this.iconUrl = iconUrl;
			this.itempreisID = itempreisID;
			this.isAktiv = isAktiv;
		}
		public int getUid() { return uid; }
		public String getName() {return name;}
		public int getPreis() { return preis; }
		public String getIconUrl() { return iconUrl;}
		public int getItempreisID(){return itempreisID;}
		public boolean isAktiv() {return isAktiv;}
		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (obj == null || !(obj instanceof Item))
				return false;
			return ((Item) obj).uid == this.uid;
		}
		
		@Override
		public int hashCode() {
			return ((Integer) uid).hashCode();
		}
		
	
		public static Item getItem(int uid) {
			Item res = null;

			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			
			try {
				con = DB.cp.getConnection();
				
				stmt = con.prepareStatement(
						"SELECT item.UID, item.Name, item.IconUrl, item.Aktiv, itempreis.Preis, itempreis.ID " +
			                     "FROM item " +
			                     "INNER JOIN itempreis ON item.UID = itempreis.Itemid " +
			                     "    AND itempreis.Datum = (" +
			                     "        SELECT MAX(Datum) " +             
			                     "        FROM itempreis ip2 " +
			                     "        WHERE ip2.Itemid = item.UID" +
			                     "    ) " +
			                     "WHERE item.UID = ?"
						);

				
				((PreparedStatement) stmt).setInt(1, uid);
				rs = ((PreparedStatement) stmt).executeQuery();
				
				if (rs.next())
					res = itemFromRS(rs);

			} catch (final Exception mye) {
				mye.printStackTrace();
			} finally {
				DB.close(con, rs, stmt);
			}
			
			return res;
		}
		
		public static Item getItem(int uid, int itempreisID) {
			Item res = null;

			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			
			try {
				con = DB.cp.getConnection();
				
				stmt = con.prepareStatement(
						"SELECT item.UID, item.Name, item.IconUrl, item.Aktiv, itempreis.Preis, itempreis.ID " +
			                     "FROM item " +
			                     "INNER JOIN itempreis ON ID = ? " +
			                     "WHERE item.UID = ?"
						);

				
				((PreparedStatement) stmt).setInt(1, itempreisID);
				((PreparedStatement) stmt).setInt(2, uid);
				rs = ((PreparedStatement) stmt).executeQuery();
				
				while (rs.next()) {
					res = itemFromRS(rs);
				}
			} catch (final Exception mye) {
				mye.printStackTrace();
			} finally {
				DB.close(con, rs, stmt);
			}
			
			return res;
		}
		
		
		
		public static List<Item> getItLi() {
			List<Item> res = new LinkedList<Item>();
			
			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				con = DB.cp.getConnection();
	 
				stmt = con.createStatement();
				final String sql = 
					    "SELECT item.UID, item.Name, item.IconUrl, item.Aktiv, itempreis.Preis, itempreis.ID " +
					    "FROM item " +
					    "INNER JOIN itempreis ON item.UID = itempreis.Itemid "
					    + "    AND itempreis.Datum = ("
					    + "        SELECT MAX(Datum) "
					    + "        FROM itempreis ip2 "
					    + "        WHERE ip2.Itemid = item.UID"
					    + "    )";


				rs = stmt.executeQuery(sql);
				while (rs.next())
					res.add(itemFromRS(rs));
	 
			} catch (final Exception mye) {
				mye.printStackTrace();
			} finally {
				DB.close(con, rs, stmt);
			}
			
			return res;
		}
		
		
		public static List<Item> getStriche(int buchungsid) {
			List<Item> res = new LinkedList<Item>();
			List<Integer> ID = new LinkedList<>();
			//zwei zahlen 1. Item-ID 2. Itempreis-ID wird mit rs hinzugefügt
			
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				con = DB.cp.getConnection();
				final String sql = 
					    "SELECT * FROM strich " +
					    "WHERE `Buchung-ID` = ? ";

				pstmt = con.prepareStatement(sql); 
			    pstmt.setInt(1, buchungsid);
				rs = pstmt.executeQuery();
				while (rs.next()) {
						ID.add(rs.getInt("Item-ID"));
						ID.add(rs.getInt("Itempreis-ID"));
				}
			} catch (final Exception mye) {
				mye.printStackTrace();
			} finally {
				DB.close(con, rs, pstmt);
			}
			
			for(int i = 0; i < ID.size(); i = i + 2) {
				res.add(Item.getItem(ID.get(i), ID.get(i + 1)));
			}
			
			return res;
		}
		
		private static Item itemFromRS(ResultSet rs) throws SQLException {
			return new Item(
					rs.getInt("uid"),
					rs.getString("Name"),
					rs.getString("IconUrl"),
					rs.getInt("Preis"),
					rs.getInt("id"),
					"TRUE".equals(rs.getString("Aktiv"))
					);
		}
		
		public static void bucheItem(Item item, int buchungID, int itempreisID) {
			Connection con = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			try {
				con = DB.cp.getConnection();
				
				stmt = con.prepareStatement("INSERT INTO `strich`"
						+ " (`Item-ID`, `Buchung-ID`, `Itempreis-ID`)"
						+ " VALUES (?, ?, ?);");
				stmt.setInt(1, item.getUid());
				stmt.setInt(2, buchungID);
				stmt.setInt(3, item.getItempreisID());
				stmt.executeUpdate();
	 
			} catch (final Exception mye) {
				mye.printStackTrace();
			} finally {
				DB.close(con, rs, stmt);
			}
			
		}
		
		public static void changeAktiv(Item item, String aktiv) {
			Connection con = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			try {
				con = DB.cp.getConnection();
				
				
				stmt = con.prepareStatement("UPDATE `item` "
						+ "SET `Aktiv` = ?  "
						+ "WHERE `Uid` = ?;");
				
				stmt.setString(1, aktiv);
				stmt.setInt(2, item.getUid());
				stmt.executeUpdate();
				
			} catch (final Exception mye) {
				mye.printStackTrace();
			} finally {
				DB.close(con, rs, stmt);
			}
		}
		
		public static void changeName(Item item, String name) {
			Connection con = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			try {
				con = DB.cp.getConnection();
				
				
				stmt = con.prepareStatement("UPDATE `item` "
						+ "SET `Name` = ?  "
						+ "WHERE `Uid` = ?;");
				
				stmt.setString(1, name);
				stmt.setInt(2, item.getUid());
				stmt.executeUpdate();
				
			} catch (final Exception mye) {
				mye.printStackTrace();
			} finally {
				DB.close(con, rs, stmt);
			}
		}
		
		public static void changePreis(Item item, int preis, String datum) {
			Connection con = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			try {
				con = DB.cp.getConnection();
				
				
				stmt = con.prepareStatement("INSERT INTO itempreis (ItemID, Preis, Datum)"
						+ " VALUES (?, ?, ?)");
				
				stmt.setInt(1, item.getUid());
				stmt.setInt(2, preis);
				stmt.setDate(3, Date.valueOf(datum));
				stmt.executeUpdate();
				
			} catch (final Exception mye) {
				mye.printStackTrace();
			} finally {
				DB.close(con, rs, stmt);
			}
		}
		
		public static void newItem(String name, int preis, String datum) {
			Connection con = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			try {
				con = DB.cp.getConnection();
				
				
				stmt = con.prepareStatement("INSERT INTO item(IconUrl, Name, Aktiv) "
						+ "VALUES(?, ?, TRUE)", Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, "/");
				stmt.setString(2, name);
				
				int rows = stmt.executeUpdate();
				
				int itemId = -1;
				if (rows > 0) {
				    ResultSet generatedKeys = stmt.getGeneratedKeys();
				    if (generatedKeys.next()) {
				        itemId = generatedKeys.getInt(1); // Die neue Item-ID
				    }
				}
				stmt.close();
				
				if (itemId != -1) {
				    PreparedStatement preisStmt = con.prepareStatement(
				        "INSERT INTO itempreis (ItemID, Preis, Datum) VALUES (?, ?, ?)"
				    );
				    preisStmt.setInt(1, itemId);
				    preisStmt.setInt(2, preis);
				    preisStmt.setDate(3, Date.valueOf(datum));

				    preisStmt.executeUpdate();
				    preisStmt.close();
				}

				
			} catch (final Exception mye) {
				mye.printStackTrace();
			} finally {
				DB.close(con, rs, stmt);
			}
		}
		
		
		
		
		@Override
		public String toString() {
			return "Item [name=" + name + ", preis=" + preis + "]";
		}
	
		
		


}
