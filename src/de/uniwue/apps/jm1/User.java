package de.uniwue.apps.jm1;

import java.io.Serializable;
import java.util.List;

import de.uniwue.apps.jm1.service.UserService;

public class User implements Serializable  {


        /**
         *
         */
        private static final long serialVersionUID = -3898743429030183341L;

        private static final UserService USER_SERVICE = new UserService();

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
                return USER_SERVICE.getUserByUid(uid);
        }


        public static  List<User>getUser() {
                return USER_SERVICE.getAllUsers();
        }


        public static void changeAktiv(User user, String aktiv) {
                USER_SERVICE.changeAktiv(user, aktiv);
        }

        public static void changeStatus(User user, String status) {
                USER_SERVICE.changeStatus(user, status);
        }

        public static void changeVorname(User user, String vorname) {
                USER_SERVICE.changeVorname(user, vorname);
        }

        public static void changeNachname(User user, String nachname) {
                USER_SERVICE.changeNachname(user, nachname);
        }

        public static void setPassword(final String pwd, int userUid) {
                USER_SERVICE.setPassword(pwd, userUid);
        }

        public static void newUser(String vorname, String nachname, String status, String admin, String guthaben, String pwd ) {
                USER_SERVICE.createUser(vorname, nachname, status, admin, guthaben, pwd);
        }

        public static void aktualisiereGuthaben(User user) {
            USER_SERVICE.refreshBalance(user);
        }

        public static boolean userHatPin(User user) {
                if(user.getPasswort() == null) {
                        return false;
                }else {
                        return true;
                }

        }



}
