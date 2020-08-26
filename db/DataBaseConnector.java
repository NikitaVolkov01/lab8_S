package db;

import ticket.*;

import javax.xml.bind.ValidationException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.concurrent.ForkJoinPool;

public class DataBaseConnector {
    private static String tablename;
    private static Connection connection;
    private static String login;
    private static Integer addId;
    private static Integer addEventId;

    public static void setLogin(String login) {
        DataBaseConnector.login = login;
    }

    public static String getLogin() {
        return login;
    }

    public static Integer getAddId() {
        return addId;
    }

    public static Integer getAddEventId() {
        return addEventId;
    }

    public static void setDBEnter(String user, String pass){
        try {

            Class.forName("org.postgresql.Driver");
            tablename = "users";
            //connection = DriverManager.getConnection("jdbc:postgresql://pg/studs", user, pass);

            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/wolf", "postgres", pass);
        } catch (SQLException | ClassNotFoundException e) {}
    }


    public static boolean register(String username, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select * from " + tablename + " where users.name = ?");
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        if (rs.next()){ return false; }

        PreparedStatement ps = connection.prepareStatement("INSERT INTO " + tablename + " VALUES(?, ?, ?) ");
        String salt = getSalt();
        ps.setString(1, username);
        ps.setString(2, hash(password, salt));
        ps.setString(3, salt);
        ps.executeUpdate();
        return true;
    }

    public static boolean login(String username, String password) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("select * from " + tablename + " where users.name = ?");
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        return rs.next() && hash(password, rs.getString("salt")).equals(rs.getString("pass"));
    }

    public static void deleteAll() {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM tickets");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean writeTicket(Ticket ticket, String login) throws ValidationException {

        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO tickets" + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");

            ps.setString(1, ticket.getName());
            ps.setFloat(2, ticket.getCoordinates().getX());
            ps.setLong(3, ticket.getCoordinates().getY());
            java.util.Date date = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            ps.setString(4, sqlDate.toString());
            ps.setLong(5, ticket.getcost());
            try{
                ps.setInt(6, ticket.getType().ordinal());
            }catch (Exception e){
                ps.setNull(6, Types.INTEGER);
            }
            try {
                int id = (int)(Math.random() *1000);
                ps.setInt(7, id);
                ps.setString(8, ticket.getEvent().getName());
                ps.setFloat(9, ticket.getEvent().getNumber());
            }catch (Exception e){
                ps.setNull(7, Types.INTEGER);
                ps.setString(8, null);
                ps.setNull(9, Types.FLOAT);
            }
            ps.setString(10, DataBaseConnector.login);

            ps.executeUpdate();

            PreparedStatement pps = connection.prepareStatement("SELECT * from tickets where name = ?" );
            pps.setString(1, ticket.getName().trim());
            ResultSet r = pps.executeQuery();
            if (r.next()){
                Integer id = r.getInt("id");
                Integer eventId = r.getInt("event_id");
                ticket.setId(id);
                ticket.getEvent().setId(eventId);
                ticket.setAuthor(DataBaseConnector.login);
                TicketStorage.putTicket(ticket);
                addId = id;
                addEventId = eventId;
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Ticket getTicket(Ticket tick){
        try {
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement("SELECT * FROM tickets WHERE name = ? AND x = ? AND y = ? AND price = ?");
                ps.setString(1, tick.getName());
                ps.setFloat(2, tick.getCoordinates().getX());
                ps.setLong(3, tick.getCoordinates().getY());
                ps.setLong(4, tick.getcost());
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
            ResultSet rs = ps.executeQuery();
            try {
                int id = rs.getInt("id"); tick.setId(id);
                tick.setCreationDate(LocalDate.now());
                tick.setAuthor(rs.getString("author"));

            }catch (ValidationException e){
                e.printStackTrace();
            }


            return tick;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Ticket> readTicket() {
        try {
            ArrayList<Ticket> tickets = new ArrayList<Ticket>();
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement("SELECT * from tickets");
            }catch (NullPointerException e){
                System.out.println("Логин & пароль для доступа к БД неверны");
                System.exit(1);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String name = rs.getString(1);
                Coordinates coordinates = new Coordinates(rs.getLong(2), rs.getFloat(3));
                Long cost = rs.getLong(5);
                TicketType ticketType;
                ticketType = TicketType.values()[rs.getInt(6)];
                if (rs.wasNull()){
                    ticketType = null;
                }
                Event event;
                try{
                    event = new Event(rs.getString(8), rs.getInt(7), rs.getInt(9));
                }catch (ValidationException e){
                    event =  null;
                }
                int id = rs.getInt("id");
                Ticket ticket = new Ticket(name, coordinates, cost, ticketType, event);
                ticket.setAuthor(rs.getString("author"));
                ticket.setCreationDate(LocalDate.now());

                try {
                    ticket.setId(rs.getInt("id"));
                }catch (ValidationException e){
                    ticket.setId(1);
                }

                tickets.add(ticket);
                TicketStorage.putTicket(ticket);
            }
            return tickets;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean removeTicket(Ticket ticket, String currentUser){
        try {
            if (currentUser.equals(ticket.getAuthor())){
                PreparedStatement ps = connection.prepareStatement("DELETE FROM tickets where id = ?");
                ps.setInt(1, ticket.getId());
                ps.executeUpdate();
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void updateTicket(Ticket ticket){
        try {
            //System.out.println(ticket.getcost());
            //Thread.sleep(4000);
            PreparedStatement ps = connection.prepareStatement("UPDATE tickets SET price = ? where (price = ?)");
            ps.setLong(1, ticket.getcost());
            ps.setLong(2, ticket.getcost()-10);
            ps.executeUpdate();

            /*ps = connection.prepareStatement("DELETE from tickets WHERE id = ?");
            ps.setInt(1, ticket.getId() + 1);
            ps.executeQuery();*/
        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    private static String getSalt() {
        byte[] salt = new byte[16];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(salt);
        return new String(salt, StandardCharsets.UTF_8);
    }

    private static String hash(String password, String salt) {
        try {
            String pepper = "22&3CdsFgh2cL97#3";
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] data = (pepper + password + salt).getBytes(StandardCharsets.UTF_8);
            byte[] hashbytes = md.digest(data);
            String s = Base64.getEncoder().encodeToString(hashbytes);
            return s;
        } catch (NoSuchAlgorithmException e) {
            return password;
        }
    }
}

























/*package tools.DataBase;

import data.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;

public class DataBaseConnector {
    private static String tablename;
    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            tablename = "clients";
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/users", "postgres", "BigDick2002");
        } catch (SQLException | ClassNotFoundException e) {}
    }

    public static boolean register(String username, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select * from " + tablename + " where clientname = ?");
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        if (rs.next()){ return false; }

        PreparedStatement ps = connection.prepareStatement("INSERT INTO " + tablename + " VALUES(?, ?, ?) ");
        String salt = getSalt();
        ps.setString(1, username);
        ps.setString(2, hash(password, salt));
        ps.setString(3, salt);
        ps.executeUpdate();
        return true;
    }

    public static boolean login(String username, String password) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("select * from " + tablename + " where clientname = ?");
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        return rs.next() && hash(password, rs.getString("salt")).equals(rs.getString("pass"));
    }

    public static boolean writeLab(String l, String login){
        String[] labpts = l.split(",");
        Coordinates coordinates = new Coordinates(Integer.parseInt(labpts[1]), Float.parseFloat(labpts[2]));
        Difficulty diff = Difficulty.values()[Integer.parseInt(labpts[5])];
        Discipline diss = new Discipline(labpts[6], Integer.parseInt(labpts[7]), Integer.parseInt(labpts[8]), Long.parseLong(labpts[9]));
        LabWork labWork = new LabWork(labpts[0], coordinates, Float.parseFloat(labpts[3]), Long.parseLong(labpts[4]), diff, diss);

        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO labworks" + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
            ps.setString(1, labWork.getName());
            ps.setInt(2, labWork.getCoordinates().getX());
            ps.setFloat(3, labWork.getCoordinates().getY());
            ps.setFloat(4, labWork.getMin());
            ps.setLong(5, labWork.getMax());
            ps.setInt(6, labWork.getDiff().ordinal());
            ps.setString(7, labWork.getDisc().toString());
            ps.setInt(8, labWork.getDisc().getLecture());
            ps.setInt(9, labWork.getDisc().getPractice());
            ps.setLong(10, labWork.getDisc().getSelfStudy());
            ps.setString(11, login);
            ps.executeUpdate();

            PreparedStatement pps = connection.prepareStatement("SELECT * from labworks where name = ?" );
            pps.setString(1, labWork.getName());
            ResultSet r = pps.executeQuery();
            if (r.next()){
                int id = r.getInt("id");
                LabWork flab = new LabWork(id, labpts[0], coordinates, Float.parseFloat(labpts[3]), Long.parseLong(labpts[4]), diff, diss, login);
                flab.setUser(login);
                LabworksStorage.put(flab);
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static ArrayList<LabWork> readLab(){
        try {
            ArrayList<LabWork> labs = new ArrayList<LabWork>();
            PreparedStatement ps = connection.prepareStatement("SELECT * from labworks");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String name = rs.getString(1);
                Coordinates coordinates = new Coordinates(rs.getInt(2), rs.getFloat(3));
                Float minimalPoint = rs.getFloat(4);
                long maximumPoint = rs.getLong(5);
                Difficulty difficulty = Difficulty.values()[rs.getInt(6)];
                Discipline discipline = new Discipline(rs.getString(7), rs.getInt(8), rs.getInt(9), rs.getLong(10));
                int id = rs.getInt("id");
                LabWork labWork = new LabWork(id, name, coordinates, minimalPoint, maximumPoint, difficulty, discipline, rs.getString("author"));
                labs.add(labWork);
            }
            return labs;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void removeLab(LabWork labWork, String currentUser){
        try {
            if (currentUser.equals(labWork.getUser())){
                PreparedStatement ps = connection.prepareStatement("DELETE FROM labworks where name = ?");
                ps.setString(1, labWork.getName());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateLab(LabWork labWork){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE labworks SET name = ? where name = ?");
            ps.setString(1, labWork.getName());
            ps.setString(2, labWork.getName() + "1");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getSalt() {
        byte[] salt = new byte[16];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(salt);
        return new String(salt, StandardCharsets.UTF_8);
    }

    private static String hash(String password, String salt) {
        try {
            String pepper = "22&3CdsFgh2cL97#3";
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] data = (pepper + password + salt).getBytes(StandardCharsets.UTF_8);
            byte[] hashbytes = md.digest(data);
            String s = Base64.getEncoder().encodeToString(hashbytes);
            System.out.println(s);
            return s;
        } catch (NoSuchAlgorithmException e) {
            return password;
        }
    }
}*/
