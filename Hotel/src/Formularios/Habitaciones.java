/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Formularios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author USUARIO
 */
public class Habitaciones extends javax.swing.JDialog {

    DefaultTableModel modeloTabla;
    TableColumnModel modeloColumna;

    /**
     * Creates new form Habitaciones
     */
    public Habitaciones(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        llenarTabla();
        this.setLocationRelativeTo(null);
    }

    public void establecerTamañoColumnas() {
        modeloColumna = jTable_Habitaciones.getColumnModel();
        modeloColumna.getColumn(0).setPreferredWidth(60);
        modeloColumna.getColumn(1).setPreferredWidth(100);
        modeloColumna.getColumn(2).setPreferredWidth(100);
        modeloColumna.getColumn(3).setPreferredWidth(100);
        modeloColumna.getColumn(4).setPreferredWidth(60);
        modeloColumna.getColumn(5).setPreferredWidth(130);

    }

    public void llenarTabla() {
        String[] titulos = {"CÓDIGO", "PISO", "ESTADO", "TIPO", "COSTO", "DESCRIPCIÓN"};
        String[] registros = new String[6];
        jTable_Habitaciones.getTableHeader().setReorderingAllowed(false);
        jTable_Habitaciones.getTableHeader().setResizingAllowed(false);
        modeloTabla = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ConexionHotel cc = new ConexionHotel();
        Connection cn = cc.conectar();
        String sql = "select h.cod_hab, p.des_pis,h.est_hab,t.nom_tip,t.cos_hab,h.obs_hab "
                + "from habitaciones h, pisos p, tipos_habitacion t "
                + "where h.cod_pis_p=p.cod_pis "
                + "and h.tip_hab_per=t.cod_tip "
                + "order by h.cod_hab";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                registros[0] = rs.getString(1);
                registros[1] = rs.getString(2);
                registros[2] = rs.getString(3);
                registros[3] = rs.getString(4);
                registros[4] = String.valueOf(rs.getDouble(5));
                registros[5] = rs.getString(6);
                modeloTabla.addRow(registros);
            }
            jTable_Habitaciones.setModel(modeloTabla);
            establecerTamañoColumnas();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al recuperar datos de la base \n" + ex);
        }
    }

    public void llenarTablaLibres() {
        String[] titulos = {"CÓDIGO", "PISO", "ESTADO", "TIPO", "COSTO", "DESCRIPCIÓN"};
        String[] registros = new String[6];
        jTable_Habitaciones.getTableHeader().setReorderingAllowed(false);
        jTable_Habitaciones.getTableHeader().setResizingAllowed(false);
        modeloTabla = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ConexionHotel cc = new ConexionHotel();
        Connection cn = cc.conectar();
        String sql = "select h.cod_hab, p.des_pis,h.est_hab,t.nom_tip,t.cos_hab,h.obs_hab "
                + "from habitaciones h, pisos p, tipos_habitacion t "
                + "where h.cod_pis_p=p.cod_pis "
                + "and h.tip_hab_per=t.cod_tip "
                + "and h.est_hab='LIBRE' "
                + "order by h.cod_hab";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                registros[0] = rs.getString(1);
                registros[1] = rs.getString(2);
                registros[2] = rs.getString(3);
                registros[3] = rs.getString(4);
                registros[4] = String.valueOf(rs.getDouble(5));
                registros[5] = rs.getString(6);
                modeloTabla.addRow(registros);
            }
            jTable_Habitaciones.setModel(modeloTabla);
            establecerTamañoColumnas();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al recuperar datos de la base \n" + ex);
        }
    }

    public void llenarTablaReservadas() {
        String[] titulos = {"CÓDIGO", "PISO", "ESTADO", "TIPO", "COSTO", "DESCRIPCIÓN"};
        String[] registros = new String[6];
        jTable_Habitaciones.getTableHeader().setReorderingAllowed(false);
        jTable_Habitaciones.getTableHeader().setResizingAllowed(false);
        modeloTabla = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ConexionHotel cc = new ConexionHotel();
        Connection cn = cc.conectar();
        String sql = "select h.cod_hab, p.des_pis,h.est_hab,t.nom_tip,t.cos_hab,h.obs_hab "
                + "from habitaciones h, pisos p, tipos_habitacion t "
                + "where h.cod_pis_p=p.cod_pis "
                + "and h.tip_hab_per=t.cod_tip "
                + "and h.est_hab='RESERVADA' "
                + "order by h.cod_hab";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                registros[0] = rs.getString(1);
                registros[1] = rs.getString(2);
                registros[2] = rs.getString(3);
                registros[3] = rs.getString(4);
                registros[4] = String.valueOf(rs.getDouble(5));
                registros[5] = rs.getString(6);
                modeloTabla.addRow(registros);
            }
            jTable_Habitaciones.setModel(modeloTabla);
            establecerTamañoColumnas();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al recuperar datos de la base \n" + ex);
        }
    }

    public void llenarTablaOcupadas() {
        String[] titulos = {"CÓDIGO", "PISO", "ESTADO", "TIPO", "COSTO", "DESCRIPCIÓN"};
        String[] registros = new String[6];
        jTable_Habitaciones.getTableHeader().setReorderingAllowed(false);
        jTable_Habitaciones.getTableHeader().setResizingAllowed(false);
        modeloTabla = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ConexionHotel cc = new ConexionHotel();
        Connection cn = cc.conectar();
        String sql = "select h.cod_hab, p.des_pis,h.est_hab,t.nom_tip,t.cos_hab,h.obs_hab "
                + "from habitaciones h, pisos p, tipos_habitacion t "
                + "where h.cod_pis_p=p.cod_pis "
                + "and h.tip_hab_per=t.cod_tip "
                + "and h.est_hab='OCUPADA' "
                + "order by h.cod_hab";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                registros[0] = rs.getString(1);
                registros[1] = rs.getString(2);
                registros[2] = rs.getString(3);
                registros[3] = rs.getString(4);
                registros[4] = String.valueOf(rs.getDouble(5));
                registros[5] = rs.getString(6);
                modeloTabla.addRow(registros);
            }
            jTable_Habitaciones.setModel(modeloTabla);
            establecerTamañoColumnas();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al recuperar datos de la base \n" + ex);
        }
    }

    public void verFactura() {
        if(jTable_Habitaciones.getSelectedRow()>-1){
            int fila = jTable_Habitaciones.getSelectedRow();
            String cod = jTable_Habitaciones.getValueAt(fila, 0).toString();
            String sql="select * from cabecera_factura "
                    + "where cod_hab_fac='"+cod+"' "
                    + "and TO_CHAR(fec_fac,'DD/MM/YYYY')=TO_CHAR(SYSDATE,'DD/MM/YYYY')";
            ConexionHotel cc = new ConexionHotel();
            Connection cn = cc.conectar();
            try {
                int numFac=0;
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while(rs.next()){
                    numFac = rs.getInt("num_fac");
                }
                //System.out.println(numFac+" "+cod);
                if(numFac>0){
                    new Venta1(null, true, numFac).setVisible(true);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Fallo recibiendo datos de la base1\n"+ex);
            }
        }else{
            JOptionPane.showMessageDialog(null, "Escoja una habitación");
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_Habitaciones = new javax.swing.JTable();
        jToggleButton_Libres = new javax.swing.JToggleButton();
        jToggleButton_Reservadas = new javax.swing.JToggleButton();
        jToggleButton_Ocupadas = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTable_Habitaciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable_Habitaciones.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane2.setViewportView(jTable_Habitaciones);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
        );

        jToggleButton_Libres.setText("Libres");
        jToggleButton_Libres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_LibresActionPerformed(evt);
            }
        });

        jToggleButton_Reservadas.setText("Reservadas");
        jToggleButton_Reservadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_ReservadasActionPerformed(evt);
            }
        });

        jToggleButton_Ocupadas.setText("Ocupadas");
        jToggleButton_Ocupadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_OcupadasActionPerformed(evt);
            }
        });

        jButton1.setText("Ver Factura");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jToggleButton_Ocupadas, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jToggleButton_Reservadas)
                        .addGap(18, 18, 18)
                        .addComponent(jToggleButton_Libres, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton_Libres)
                    .addComponent(jToggleButton_Reservadas)
                    .addComponent(jToggleButton_Ocupadas)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton_LibresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_LibresActionPerformed
        // TODO add your handling code here:
        if (jToggleButton_Libres.isSelected()) {
            llenarTablaLibres();
            jToggleButton_Reservadas.setEnabled(false);
            jToggleButton_Ocupadas.setEnabled(false);
        } else {
            llenarTabla();
            jToggleButton_Reservadas.setEnabled(true);
            jToggleButton_Ocupadas.setEnabled(true);
        }
    }//GEN-LAST:event_jToggleButton_LibresActionPerformed

    private void jToggleButton_ReservadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_ReservadasActionPerformed
        // TODO add your handling code here:
        if (jToggleButton_Reservadas.isSelected()) {
            llenarTablaReservadas();
            jToggleButton_Libres.setEnabled(false);
            jToggleButton_Ocupadas.setEnabled(false);
        } else {
            llenarTabla();
            jToggleButton_Libres.setEnabled(true);
            jToggleButton_Ocupadas.setEnabled(true);
        }
    }//GEN-LAST:event_jToggleButton_ReservadasActionPerformed

    private void jToggleButton_OcupadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_OcupadasActionPerformed
        // TODO add your handling code here:
        if (jToggleButton_Ocupadas.isSelected()) {
            llenarTablaOcupadas();
            jToggleButton_Libres.setEnabled(false);
            jToggleButton_Reservadas.setEnabled(false);
        } else {
            llenarTabla();
            jToggleButton_Libres.setEnabled(true);
            jToggleButton_Reservadas.setEnabled(true);
        }
    }//GEN-LAST:event_jToggleButton_OcupadasActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        verFactura();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Habitaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Habitaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Habitaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Habitaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Habitaciones dialog = new Habitaciones(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable_Habitaciones;
    private javax.swing.JToggleButton jToggleButton_Libres;
    private javax.swing.JToggleButton jToggleButton_Ocupadas;
    private javax.swing.JToggleButton jToggleButton_Reservadas;
    // End of variables declaration//GEN-END:variables
}
