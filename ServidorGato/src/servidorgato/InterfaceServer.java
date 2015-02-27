/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorgato;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import javax.swing.JOptionPane;


public class InterfaceServer extends javax.swing.JFrame implements AvisaServidor{

    private ServerSocket chatServer;
    
    Vector<ClienteThread> clientes;

    private OutputStream outputStream;
    private InputStream inputStream;
    
    private DataOutputStream salidaDatos;
    private DataInputStream entradaDatos;
    
    private int contadorCliente;
    
    private boolean conexionActiva;
    
    public InterfaceServer() {
        initComponents();
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
        txtMensajesRecibidos = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtMensajesRecibidos.setEditable(false);
        txtMensajesRecibidos.setColumns(20);
        txtMensajesRecibidos.setRows(5);
        jScrollPane1.setViewportView(txtMensajesRecibidos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public void connection(int port){
        try{
            clientes = new Vector<ClienteThread>();

            this.chatServer = new ServerSocket(port);
          
             this.txtMensajesRecibidos.append("Java Chat Server..... esperando conexiones....\n");
             
             while(true){
                Socket socket = this.chatServer.accept();  
                
                this.conexionActiva = true;
                
                ClienteThread cliente = new ClienteThread(socket);
                System.out.println("usuario conectado");
                cliente.addListener(this);
                clientes.add(cliente);                
                this.contadorCliente++;   
                
                System.out.println(this.contadorCliente);
             }
            
        }catch(IOException e){
            messagesInformation(e.getMessage(),JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void messagesInformation(String message,int typeMessage){
        JOptionPane.showMessageDialog(null, message,"Java Chat (Server)",typeMessage);
    }
    
    private void enviarJugada(String msg){
        String datos [] = msg.split(":");
        
        for (int i=0;i<this.clientes.size();i++){
            ClienteThread cliente = this.clientes.get(i);
            
            if (!cliente.usuario.equals(datos[0])){
                cliente.enviarDatos(msg);
            }
        }
    }
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
            java.util.logging.Logger.getLogger(InterfaceServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfaceServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfaceServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfaceServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfaceServer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtMensajesRecibidos;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onClientReceiveName(String name) {
        this.txtMensajesRecibidos.append("\nCliente conectado: "+this.contadorCliente+" ) "+name);
    }

    @Override
    public void onClientReceiveMessage(String msg) {
        this.txtMensajesRecibidos.append("\n"+msg);
        
        enviarJugada(msg);
    }

    @Override
    public void onClientReceivePrivateMessage(String user, String msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void closeServer(String user, String msg,ClienteThread cliente) {
        this.txtMensajesRecibidos.append("\ncerro conexion ---> "+user);
        this.clientes.remove(cliente);
    }

    @Override
    public void closeServer(String user, String msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
}
