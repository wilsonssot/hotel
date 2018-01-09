/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Formularios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author USUARIO
 */
public class ConexionHotel {

    Connection con;

    public Connection conectar() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hotel", "HOTEL");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Driver no encontrado " + ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Fallo al recibir base de datos");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No hay resultado");
        } finally {
            return con;
        }
    }
}
