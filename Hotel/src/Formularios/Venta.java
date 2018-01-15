/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Formularios;

import java.awt.HeadlessException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Administrador
 */
public class Venta extends javax.swing.JDialog {

    /**
     * Creates new form Venta
     */
    Connection cn = null;
    ConexionHotel cc = new ConexionHotel();
    int codFac;
    DefaultTableModel modeloTabla;
    TableColumnModel modeloColumna;
    float total;
    String codHab;

    public Venta(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        establecerFecha();
        establecerUsuario();
        habilitarEdicionCamposCliente(true);
        modeloTablaCarrito();
        establecerTamañoColumnas();
        jButton_Cancelar.setEnabled(true);
    }

    public Venta(java.awt.Frame parent, boolean modal, float t,String codigo) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        establecerFecha();
        establecerUsuario();
        habilitarEdicionCamposCliente(true);
        modeloTablaCarrito();
        establecerTamañoColumnas();
        jButton_Cancelar.setEnabled(true);
        codHab = codigo;
        total = t;
    }

    public void establecerFecha() {
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        Calendar fecha = Calendar.getInstance();
        jLabel_Fecha.setText(formateador.format(fecha.getTime()));
    }

    public void establecerUsuario() {
        String sql = "select * from recepcionistas where ced_rec='" + Principal.cedRec + "'";
//        String sql = "select * from usuarios where cod_usu='1101715876'";
        cn = cc.conectar();
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                jTextField_CedEmp.setText(rs.getString("CED_REC"));
                jTextField_NomEmp.setText(rs.getString("NOM_REC") + " " + rs.getString("APE_REC"));
            }
            cn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al obtener datos de la base:\n" + ex);
            try {
                cn.close();
            } catch (SQLException ex1) {
                JOptionPane.showMessageDialog(null, "Error de conexión");
            }
        } catch (Exception ex) {

        }
    }

    public void habilitarEdicionCamposCliente(boolean habilitado) {
        jTextField_NomCli.setEditable(habilitado);
        jTextField_CedCli.setEditable(habilitado);
        jTextField_CedCli.requestFocus();
        jTextField_ApeCli.setEditable(habilitado);
        jTextField_TelCli.setEditable(habilitado);
        jTextField_DirCli.setEditable(habilitado);
    }

    public void txtLimpiar() {
        jTextField_CedCli.setText("");
        jTextField_NomCli.setText("");
        jTextField_ApeCli.setText("");
        jTextField_DirCli.setText("");
        jTextField_TelCli.setText("");
        habilitarEdicionCamposCliente(true);
        jButton_Cargar.setEnabled(true);
        jButton_Seleccionar.setEnabled(false);
    }

    public void cargarCliente() {
        String sql = "select * from clientes where ced_cli='" + jTextField_CedCli.getText() + "'";
        boolean clienteExiste = false;
//        String sql = "select * from clientes where ced_cli='172438393'";
        cn = cc.conectar();
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                jTextField_NomCli.setText(rs.getString("NOM_CLI"));
                jTextField_ApeCli.setText(rs.getString("APE_CLI"));
                jTextField_TelCli.setText(rs.getString("TEL_CLI"));
                jTextField_DirCli.setText(rs.getString("DIR_CLI"));
                jTextField_CedCli.setEditable(false);
                jButton_Cargar.setEnabled(false);
                clienteExiste = true;
                habilitarEdicionCamposCliente(false);
                jButton_Seleccionar.setEnabled(true);
            }
            cn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
            try {
                cn.close();
            } catch (SQLException ex1) {
                JOptionPane.showMessageDialog(null, "Error de conexión\n" + ex1);
            }
        } catch (Exception ex) {

        }
        if (!clienteExiste) {
            int opcion = JOptionPane.showConfirmDialog(null, "¿Desea ingresar cliente?", "Cliente no existe", JOptionPane.YES_OPTION);
            if (opcion == 0) {
                habilitarEdicionCamposCliente(true);
                jTextField_NomCli.requestFocus();
                jButton_Guardar.setEnabled(true);
            }
        } else {
            crearCabeceraFactura();
        }

    }

    public void modeloTablaCarrito() {
        String[] titulos = {"CÓDIGO", "NOMBRE", "CANTIDAD", "V/U", "V/T"};
        jTable_CarritoCompra.getTableHeader().setReorderingAllowed(false);
        jTable_CarritoCompra.getTableHeader().setResizingAllowed(false);
        modeloTabla = new DefaultTableModel(null, titulos) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable_CarritoCompra.setModel(modeloTabla);

    }

    public void establecerTamañoColumnas() {
        modeloColumna = jTable_CarritoCompra.getColumnModel();
        modeloColumna.getColumn(0).setPreferredWidth(60);
        modeloColumna.getColumn(1).setPreferredWidth(190);
        modeloColumna.getColumn(2).setPreferredWidth(80);
        modeloColumna.getColumn(3).setPreferredWidth(90);
        modeloColumna.getColumn(4).setPreferredWidth(60);
        jTable_CarritoCompra.setColumnModel(modeloColumna);

    }

    public void crearCabeceraFactura() {
        String CED_CLI_F, CED_REC_FAC, COD_HAB_FAC;
        float TOT_VEN = total;
        CED_CLI_F = jTextField_CedCli.getText();
        CED_REC_FAC = jTextField_CedEmp.getText();
        COD_HAB_FAC = codHab;
        String sql = "insert into cabecera_factura(CED_REC_F,CED_CLI_F,COD_HAB_FAC,TOTAL) values(?,?,?,?)";
        cn = cc.conectar();
        try {
            PreparedStatement psd = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psd.setString(1, CED_REC_FAC);
            psd.setString(2, CED_CLI_F);
            psd.setString(3, COD_HAB_FAC);
            psd.setFloat(4, TOT_VEN);

            int affectedRows = psd.executeUpdate();

            try (ResultSet generatedKeys = psd.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    affectedRows = generatedKeys.getInt(1);
                    codFac = affectedRows;
                    System.out.println(codFac);
                } else {
                    JOptionPane.showMessageDialog(null, "No se ha podido obtener id");
                }
            }
            cn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al insertar datos en la base:\n" + ex);
            try {
                cn.close();
            } catch (SQLException ex1) {
                JOptionPane.showMessageDialog(null, "Error de conexión");
            }
        }
    }

    public void guardarCliente() {
        cn = cc.conectar();
        String CED_CLI, APE_CLI, NOM_CLI, DIR_CLI, TEL_CLI, EMAIL_CLI;
        CED_CLI = jTextField_CedCli.getText().trim();
        APE_CLI = jTextField_ApeCli.getText().trim().toUpperCase();
        NOM_CLI = jTextField_NomCli.getText().trim().toUpperCase();
        DIR_CLI = jTextField_DirCli.getText().trim().toUpperCase();
        TEL_CLI = jTextField_TelCli.getText().trim();
        EMAIL_CLI = "ingreseemail@ejemplo.com";
        String sql = "";
        sql = "insert into clientes(CED_CLI, APE_CLI, NOM_CLI, DIR_CLI, TEL_CLI, EMAIL_CLI)"
                + "values(?,?,?,?,?,?)";
        try {
            PreparedStatement psd = cn.prepareStatement(sql);
            psd.setString(1, CED_CLI); //(Numero de campo/ nombre)
            psd.setString(2, APE_CLI);
            psd.setString(3, NOM_CLI);
            psd.setString(4, DIR_CLI);
            psd.setString(5, TEL_CLI);
            psd.setString(6, EMAIL_CLI);
            int n = psd.executeUpdate();

            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Se insertó la información correctamente");
                cargarCliente();
                jButton_Cargar.setEnabled(false);
                jButton_Guardar.setEnabled(false);
                habilitarEdicionCamposCliente(false);
                cn.close();
            } else {
                cn.close();
            }

        } catch (SQLException ex) { //permite manejar la excepcion de la base de datos
            JOptionPane.showMessageDialog(null, ex);
            try {
                cn.close();
            } catch (SQLException ex1) {
                JOptionPane.showMessageDialog(null, "Error de conexión\n" + ex1);
            }
        } catch (Exception ex) {
        }
    }

    public void eliminarProducto() {
        
    }

    public void agregarArticulo() {
        String codigo;
        int cantidad = 0;
        InventarioVendedor sel = new InventarioVendedor(null, true);
        sel.setVisible(true);
        if (!sel.isShowing()) {
            codigo = sel.enviarCodigo();
            cantidad = sel.obtenerCantidad();
            String sql = "insert into detalle_factura (cod_ser_uso,cant,num_fac_per) values (?,?,?)";
            cn = cc.conectar();
            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.setString(1, codigo);
                psd.setInt(2, cantidad);
                psd.setInt(3, codFac);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
            if (jTable_CarritoCompra.getRowCount() > 0) {
                jButton_Cobrar.setEnabled(true);
            }

        }
    }

  

    public void facturar() {
        crearCabeceraFactura();
        cn = cc.conectar();
        int NUM_VEN_P, CANTIDAD;
        float PRE_TOT_P;
        String ID_PRO_V;
        NUM_VEN_P = codFac;
        int n = 0;
        int i;
        for (i = 0; i < jTable_CarritoCompra.getRowCount(); i++) {
            ID_PRO_V = jTable_CarritoCompra.getValueAt(i, 0).toString();
            CANTIDAD = Integer.valueOf(jTable_CarritoCompra.getValueAt(i, 3).toString());
            PRE_TOT_P = Float.valueOf(jTable_CarritoCompra.getValueAt(i, 5).toString());
            String sql = "insert into detalle_ventas (NUM_VEN_P, ID_PRO_V, CANTIDAD, PRE_TOT_P) values(?,?,?,?)";
            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.setInt(1, NUM_VEN_P);
                psd.setString(2, ID_PRO_V);
                psd.setInt(3, CANTIDAD);
                psd.setFloat(4, PRE_TOT_P);

                n += psd.executeUpdate();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error en el envío de datos a la base\n" + ex);
                try {
                    cn.close();
                } catch (SQLException ex1) {
                    JOptionPane.showMessageDialog(null, "Error de conexión");
                }
            }
        }
        if (n == i) {
            JOptionPane.showMessageDialog(null, "Venta exitosa!");
            vaciarCarrito();
            valoresIniciales();
            //Aqui sacar la factura a imprimir OJOOO
        }
        try {
            cn.close();
        } catch (SQLException ex1) {
            JOptionPane.showMessageDialog(null, "Error de conexión");
        }

    }

    public void vaciarCarrito() {
        for (int i = 0; i < jTable_CarritoCompra.getRowCount(); i++) {
            modeloTabla.removeRow(i);
            i -= 1;
        }
    }

    public void valoresIniciales() {
        jButton_Cobrar.setEnabled(false);
        jTextField_Total.setText("");
    }
    
    public void setearCodigo(){
        Habitaciones11 h = new Habitaciones11(null, true);
        h.setVisible(true);
        if(!h.isShowing()){
            codHab=h.cod;
            jButton_SelHab.setEnabled(false);
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel_Fecha = new javax.swing.JLabel();
        jPanel_Fondo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField_CedCli = new javax.swing.JTextField();
        jTextField_NomCli = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField_ApeCli = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField_TelCli = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField_DirCli = new javax.swing.JTextField();
        jButton_Cargar = new javax.swing.JButton();
        jButton_Guardar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jButton_Seleccionar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_CarritoCompra = new javax.swing.JTable();
        jButton_Cobrar = new javax.swing.JButton();
        jButton_Cancelar = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jTextField_Total = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jTextField_CedEmp = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField_NomEmp = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jButton_SelHab = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextField_CodHab = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel5.setAutoscrolls(true);

        jLabel_Fecha.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel_Fecha.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jPanel_Fondo.setBorder(javax.swing.BorderFactory.createTitledBorder("FACTURA HABITACIÓN"));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("DATOS DEL CLIENTE"));

        jLabel1.setText("Cédula:");

        jTextField_CedCli.setNextFocusableComponent(jButton_Cargar);
        jTextField_CedCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField_CedCliKeyTyped(evt);
            }
        });

        jTextField_NomCli.setEditable(false);
        jTextField_NomCli.setNextFocusableComponent(jTextField_ApeCli);
        jTextField_NomCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField_NomCliKeyTyped(evt);
            }
        });

        jLabel2.setText("Nombre:");

        jTextField_ApeCli.setEditable(false);
        jTextField_ApeCli.setNextFocusableComponent(jTextField_TelCli);
        jTextField_ApeCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField_ApeCliKeyTyped(evt);
            }
        });

        jLabel3.setText("Apellido:");

        jLabel4.setText("Teléfono:");

        jTextField_TelCli.setEditable(false);
        jTextField_TelCli.setNextFocusableComponent(jTextField_DirCli);
        jTextField_TelCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField_TelCliKeyTyped(evt);
            }
        });

        jLabel5.setText("Dirección:");

        jTextField_DirCli.setEditable(false);
        jTextField_DirCli.setNextFocusableComponent(jButton_Guardar);
        jTextField_DirCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField_DirCliKeyTyped(evt);
            }
        });

        jButton_Cargar.setText("Cargar");
        jButton_Cargar.setNextFocusableComponent(jTextField_NomCli);
        jButton_Cargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CargarActionPerformed(evt);
            }
        });

        jButton_Guardar.setText("Guardar");
        jButton_Guardar.setEnabled(false);
        jButton_Guardar.setNextFocusableComponent(jTextField_CedCli);
        jButton_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_GuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField_CedCli, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_Cargar, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField_TelCli, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField_ApeCli)
                            .addComponent(jTextField_NomCli, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_Guardar, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                                .addGap(115, 115, 115))
                            .addComponent(jTextField_DirCli))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField_CedCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField_TelCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Cargar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField_NomCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField_DirCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField_ApeCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Guardar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("SERVICIOS"));

        jLabel6.setText("Seleccione:");

        jButton_Seleccionar.setText("...");
        jButton_Seleccionar.setEnabled(false);
        jButton_Seleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SeleccionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jButton_Seleccionar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jButton_Seleccionar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("CARRITO DE COMPRA"));

        jTable_CarritoCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Descripción", "Cantidad", "V/U", "V/T"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable_CarritoCompra);

        jButton_Cobrar.setText("Facturar");
        jButton_Cobrar.setEnabled(false);
        jButton_Cobrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CobrarActionPerformed(evt);
            }
        });

        jButton_Cancelar.setText("Cancelar");
        jButton_Cancelar.setEnabled(false);
        jButton_Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CancelarActionPerformed(evt);
            }
        });

        jLabel14.setText("Total:");

        jTextField_Total.setEditable(false);
        jTextField_Total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton_Cobrar)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_Cancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField_Total, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 63, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_Cobrar)
                            .addComponent(jButton_Cancelar)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jTextField_Total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("DATOS VENDEDOR"));

        jLabel8.setText("Cedula:");

        jTextField_CedEmp.setEditable(false);

        jLabel11.setText("Nombres:");

        jTextField_NomEmp.setEditable(false);
        jTextField_NomEmp.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jTextField_CedEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField_NomEmp)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel11)
                    .addComponent(jTextField_CedEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField_NomEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("ESCOJA HABITACIÓN"));

        jLabel16.setText("Seleccione:");

        jButton_SelHab.setText("...");
        jButton_SelHab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SelHabActionPerformed(evt);
            }
        });

        jLabel7.setText("Habitación:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addGap(18, 18, 18)
                .addComponent(jButton_SelHab)
                .addGap(234, 234, 234)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jTextField_CodHab, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jButton_SelHab)
                    .addComponent(jLabel7)
                    .addComponent(jTextField_CodHab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel_FondoLayout = new javax.swing.GroupLayout(jPanel_Fondo);
        jPanel_Fondo.setLayout(jPanel_FondoLayout);
        jPanel_FondoLayout.setHorizontalGroup(
            jPanel_FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_FondoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel_FondoLayout.setVerticalGroup(
            jPanel_FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_FondoLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel13.setText("Fecha:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel_Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel_Fondo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel_Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel_Fondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane2.setViewportView(jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField_CedCliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_CedCliKeyTyped
        // TODO add your handling code here:
//        deshabilitarCancelar();

    }//GEN-LAST:event_jTextField_CedCliKeyTyped

    private void jTextField_NomCliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_NomCliKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField_NomCliKeyTyped

    private void jTextField_ApeCliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_ApeCliKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField_ApeCliKeyTyped

    private void jTextField_TelCliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_TelCliKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField_TelCliKeyTyped

    private void jTextField_DirCliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_DirCliKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField_DirCliKeyTyped

    private void jButton_CargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CargarActionPerformed
        cargarCliente();
    }//GEN-LAST:event_jButton_CargarActionPerformed

    private void jButton_CobrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CobrarActionPerformed
        int preg = JOptionPane.showConfirmDialog(this, "Realizar Venta?...", "Facturando...", JOptionPane.YES_OPTION);
        if (preg == 0) {
            facturar();
        }

    }//GEN-LAST:event_jButton_CobrarActionPerformed

    private void jButton_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CancelarActionPerformed
//        confirmarCancelarVenta();
        int confirm = JOptionPane.showConfirmDialog(null, "¿Desea cancelar?", "Cancelar venta", JOptionPane.YES_OPTION);
        if (confirm == 0) {
            this.dispose();
        }
    }//GEN-LAST:event_jButton_CancelarActionPerformed

    private void jButton_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_GuardarActionPerformed
        // TODO add your handling code here:
        guardarCliente();
    }//GEN-LAST:event_jButton_GuardarActionPerformed


    private void jButton_SeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SeleccionarActionPerformed
        // TODO add your handling code here:
        agregarArticulo();
    }//GEN-LAST:event_jButton_SeleccionarActionPerformed

    private void jButton_SelHabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SelHabActionPerformed
        // TODO add your handling code here:
        setearCodigo();
        
    }//GEN-LAST:event_jButton_SelHabActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Venta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Venta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Venta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Venta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                Venta dialog = new Venta(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton_Cancelar;
    private javax.swing.JButton jButton_Cargar;
    private javax.swing.JButton jButton_Cobrar;
    private javax.swing.JButton jButton_Guardar;
    private javax.swing.JButton jButton_SelHab;
    private javax.swing.JButton jButton_Seleccionar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel_Fecha;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel_Fondo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable_CarritoCompra;
    private javax.swing.JTextField jTextField_ApeCli;
    private javax.swing.JTextField jTextField_CedCli;
    private javax.swing.JTextField jTextField_CedEmp;
    private javax.swing.JTextField jTextField_CodHab;
    private javax.swing.JTextField jTextField_DirCli;
    private javax.swing.JTextField jTextField_NomCli;
    private javax.swing.JTextField jTextField_NomEmp;
    private javax.swing.JTextField jTextField_TelCli;
    private javax.swing.JTextField jTextField_Total;
    // End of variables declaration//GEN-END:variables
}
