package com.foogaro.cdc.mssql;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MSSQLFillerRunner {

    //private final static String URL = "jdbc:microsoft:sqlserver://localhost:1433"; //;DatabaseName=DATABASE
    private final static String URL = "jdbc:sqlserver://localhost:1433"; //;DatabaseName=DATABASE
    private final static String UID = "sa";
    private final static String PWD = "_123@ABC_";

    public static void main(String[] args) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            Connection conn = DriverManager.getConnection(URL, UID, PWD);
            System.out.println("Connected to MSSQL server");
            String sql = "insert into debezium.dbo.users (name, lastname, username, email) values (?,?,?,?);";
            Faker faker = new Faker();
            String firstName, lastName, username, email;
            while (true) {

                PreparedStatement stmt = conn.prepareStatement(sql);
                int index = 1;
                firstName = faker.name().firstName();
                lastName = faker.name().lastName();
                username = firstName + "." + lastName;
                email = username + "@foogaro.com";
                stmt.setString(index++, firstName);
                stmt.setString(index++, lastName);
                stmt.setString(index++, username);
                stmt.setString(index++, email);
                stmt.executeUpdate();
                stmt.close();
                System.out.printf("Added [%s %s %s %s]%n", firstName, lastName, username, email);
                try {
                    Thread.sleep(faker.number().numberBetween(0l, 250l));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
