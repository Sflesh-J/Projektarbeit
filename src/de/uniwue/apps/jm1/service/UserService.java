package de.uniwue.apps.jm1.service;

import java.util.List;
import java.util.OptionalInt;

import de.uniwue.apps.jm1.Buchung;
import de.uniwue.apps.jm1.User;
import de.uniwue.apps.jm1.Buchung.Buchungstyp;
import de.uniwue.apps.jm1.dao.UserDao;

public class UserService {

    private final UserDao userDao;

    public UserService() {
        this(new UserDao());
    }

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserByUid(String uid) {
        return userDao.findByUid(uid).orElse(null);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void changeAktiv(User user, String aktiv) {
        userDao.updateAktiv(user.getUid(), aktiv);
    }

    public void changeStatus(User user, String status) {
        userDao.updateStatus(user.getUid(), status);
    }

    public void changeVorname(User user, String vorname) {
        userDao.updateVorname(user.getUid(), vorname);
    }

    public void changeNachname(User user, String nachname) {
        userDao.updateNachname(user.getUid(), nachname);
    }

    public void setPassword(String pwd, int userUid) {
        userDao.updatePassword(userUid, pwd);
    }

    public void createUser(String vorname, String nachname, String status, String admin, String guthaben, String pwd) {
        boolean isAdmin = "ja".equalsIgnoreCase(admin);
        int guthabenNEW = Integer.parseInt(guthaben) * 100;
        OptionalInt userId = userDao.insertUser(vorname, nachname, status, isAdmin, pwd);
        if (userId.isPresent()) {
            User createdUser = getUserByUid(Integer.toString(userId.getAsInt()));
            if (createdUser != null) {
                Buchung.erstelleBuchung(createdUser, guthabenNEW, "Neuer Benutzer Startguthaben", Buchungstyp.AUFBUCHUNG);
            }
        }
    }

    public void refreshBalance(User user) {
        userDao.recalculateBalance(user.getUid());
    }
}
