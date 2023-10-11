package server;

import java.security.*;
import java.sql.*;


public class AuthrizationManager {
    public static boolean authorized = false;
    public static boolean loginning = false;
    public static int logstate = 0;
    public static boolean registering = false;
    public static int regstate = 0;
    public static String username;
    public static String password;
    public static String hashedPassword;

    public String authorize(String input) throws Exception {


        if (loginning) {
            if (!input.isEmpty()) {
                if (logstate == 1) {
                    username = input;
                    logstate = 2;
                    return "Enter password";
                } else if (logstate == 2) {
                    logstate = 3;
                    password = input;
                }
            }
            if (logstate == 3) {
                System.out.println(username + " " + password);
                try {
                    Class.forName("org.postgresql.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "qwerty");
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("select * from users");
                    while (rs.next()) {
                        if (rs.getString("username").equalsIgnoreCase(username) & rs.getString("password").equalsIgnoreCase(password)) {
                            logstate = 0;
                            loginning = false;
                            authorized = true;
                            return "logged in succesfully";
                        }

                    }
                    loginning = false;
                    logstate = 0;
                    return "Bad login/password";

                } catch (Exception e) {
                    return "Bad login/password";
                }
            }


        } else if (registering) {
            if (!input.isEmpty()) {
                if (regstate == 1) {
                    username = input;
                    regstate = 2;
                    return "Enter password";
                } else if (regstate == 2) {
                    regstate = 3;
                    password = input;
                }
            }
            if (regstate == 3) {
                try {
                    Class.forName("org.postgresql.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "qwerty");
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("select * from users");
                    while (rs.next()) {
                        String s = (rs.getString("username"));
                        if (s.equalsIgnoreCase(username)) {
                            regstate = 0;
                            registering = false;
                            return "login already exists";
                        }
                    }

                    registering = false;
                    regstate = 0;
                    String sql = String.format("insert into users(username,password) values ('%s','%s')",username,password);

                    Statement rsr = conn.createStatement();
                    rsr.executeUpdate(sql);

                    sql = String.format("create role %s login password '%s'",username,password);
                    rsr = conn.createStatement();
                    rsr.executeUpdate(sql);

                    sql = String.format("grant select, insert, update, delete on workers to %s",username);
                    rsr = conn.createStatement();
                    rsr.executeUpdate(sql);
                    authorized=true;
                    return "account created succesfully";

                } catch (Exception e) {
                    return "Bad login/password";
                }
            }

        }
        if (input.equalsIgnoreCase("log in")) {
            loginning = true;
            logstate = 1;
            return "Enter login";
        } else if (input.equalsIgnoreCase("register")) {
            registering = true;
            regstate = 1;
            return "Enter login";
        }
        else{return "Please, enter log in or register";}
    }
    public boolean isAuthorized(){return authorized;}
    public void notAuthorized(){authorized=false;}
    public String getUsername(){return username;}
    public String getPassword(){return password;}
}
