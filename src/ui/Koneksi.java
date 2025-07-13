/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author ASUS
 */
public class Koneksi {
    private static Connection mysqlconfig;
    public static Connection configDB()throws SQLException{
        try{
            String url= "jdbc:mysql://localhost:3306/pemkom";
            String ussername = "root";
            String password= "";
            
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            mysqlconfig = DriverManager.getConnection(url,ussername,password);
        } catch (Exception e) {
            System.err.println("koneksi gagal"+ e.getMessage());
        }
        return mysqlconfig;
    }
}
