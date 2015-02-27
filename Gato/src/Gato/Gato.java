/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Gato;
import java.awt.Color;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.*;
/**
 *
 * @author Alejandra
 */
public class Gato extends javax.swing.JFrame implements ActionListener, Runnable{
    Casilla[][]  juego;
    int contador, Suma;
    private boolean recibo = false;
    
    private Socket chatClient;
    
    private OutputStream outputStream;
    private InputStream inputStream;
    
    private DataOutputStream salidaDatos;
    private DataInputStream entradaDatos;
    
    private Thread recibir;
    
    private String usuarioDos;

    public Gato() {
        initComponents();
        
        contador =0;
        juego = new Casilla [3][3];
        jlbuno.setText(JOptionPane.showInputDialog("Nombre Jugador Uno (O)"));
        //jlbdos.setText(JOptionPane.showInputDialog("Nombre Jugador Dos (X)"));
        
        this.recibir = new Thread(this);
        nuevo();    
        conexion(8080,"localhost");
    }
    
    //establecer la conexion
    public void conexion(int puerto, String ip){
        try{
            this.chatClient = new Socket(ip,puerto); 
            enviarDatos();
            this.recibir.start();            
        }catch(IOException e){
            messagesInformation(e.toString(),JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void enviarDatos(String mensaje){
        //this.txtMensajesRecibidos.append(this.MSG_SEND+mensaje+"\n");
        try{
            this.outputStream = this.chatClient.getOutputStream();
            this.salidaDatos = new DataOutputStream(this.outputStream);
            
            this.salidaDatos.writeUTF("MSG:"+mensaje);
            this.salidaDatos.flush();
        }catch(IOException e){
            messagesInformation(e.toString(),JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void enviarDatos(){        
        try{
            this.outputStream = this.chatClient.getOutputStream();
            this.salidaDatos = new DataOutputStream(this.outputStream);
            
            this.salidaDatos.writeUTF("USER:"+this.jlbuno.getText());
            this.salidaDatos.flush();
        }catch(IOException e){
            messagesInformation(e.toString(),JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void recibirDatos(){
        try{
            this.inputStream = this.chatClient.getInputStream();
            this.entradaDatos = new DataInputStream(this.inputStream);
            
            String dato = this.entradaDatos.readUTF();
            System.out.println(dato);
            determinarJugada(dato);
        }catch(IOException e){
            messagesInformation(e.toString(),JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void messagesInformation(String message,int typeMessage){
        JOptionPane.showMessageDialog(null, message,"Java Chat",typeMessage);
    }
    
    private void determinarJugada(String jugada){
        String datos[] = jugada.split(":");
        
        this.usuarioDos = datos[0];
        
        if(datos[0].equals(this.jlbuno.getText())){
        
        }else{
            if (datos[1].equals("1")){
                this.recibo = true;
                this.juego[0][0].A.doClick();

            }else if (datos[1].equals("4")){
                this.recibo = true;
                this.juego[0][1].A.doClick();            

            }else if (datos[1].equals("7")){
                this.recibo = true;
                this.juego[0][2].A.doClick();            
            }else if (datos[1].equals("2")){
                this.recibo = true;
                this.juego[1][0].A.doClick();            
            }else if (datos[1].equals("5")){
                this.recibo = true;
                this.juego[1][1].A.doClick();            
            }else if (datos[1].equals("8")){
                this.recibo = true;
                this.juego[1][2].A.doClick();            
            }else if (datos[1].equals("3")){
                this.recibo = true;
                this.juego[2][0].A.doClick();            
            }else if (datos[1].equals("6")){
                this.recibo = true;
                this.juego[2][1].A.doClick();            
            }else if (datos[1].equals("9")){
                this.recibo = true;
                this.juego[2][2].A.doClick();            
            }
        }
    }

    @Override
   public void actionPerformed(ActionEvent e) {
        
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                if (e.getSource()==juego[i][j].A) 
                {
                    Tiro(juego[i][j],i,j);
                    if(Revisar())
                    {
                        JOptionPane.showMessageDialog(null, "Ganaste");
                        System.out.println(Suma);
                        limpiar();
                        
                        nuevo();
                        
                    }
                    contador++;
                }
            }
        }
       
   }
    
    void Tiro(Casilla X,int i,int j)
    {
        ImageIcon ICONO=null;
        if(this.recibo==false)
        {
            ICONO = new ImageIcon(this.getClass().getResource("O.png"));
            X.B=1;
            
            String jugada = "";
        
        if (i==0 & j==0){jugada= "1";}              
        if (i==0 & j==1){jugada= "4";}              
        if (i==0 & j==2){jugada= "7";}              
        if (i==1 & j==0){jugada= "2";}              
        if (i==1 & j==1){jugada= "5";}              
        if (i==1 & j==2){jugada= "8";}  
        if (i==2 & j==0){jugada= "3";}  
        if (i==2 & j==1){jugada= "6";}  
        if (i==2 & j==2){jugada= "9";}  
        
        enviarDatos(jugada);
            
        }
        else if (this.recibo==true)
        {
            ICONO = new ImageIcon(this.getClass().getResource("X.png"));
            X.B=4;
            this.recibo = false;
        }
        
        ICONO = new ImageIcon(ICONO.getImage().getScaledInstance(90, -1, java.awt.Image.SCALE_DEFAULT));
        X.A.setIcon(ICONO);
        X.A.removeActionListener(this);
        
        
    }
    boolean Revisar()
    {
        boolean Gano= false;
        Suma=0;
        
        for(int i=0;i<3;i++)
        {
            Suma=juego[i][0].B+juego[i][1].B+juego[i][2].B;
            if(Suma==3 || Suma==12)
            {
                if (Suma==3){
                    System.out.println("Gano O");
                    JOptionPane.showMessageDialog(null, "Gano Jugador Uno."
                            + "\n" +" Felicidades: " + jlbuno.getText() + "...!!");
                }
                else if (Suma==12){
                    System.out.println("Gano X");
                    JOptionPane.showMessageDialog(null, "Gano Jugador Dos."
                            + "\n" +" Felicidades: " +  jlbdos.getText() + "...!!");
                }
                Gano=true;
                break;
            }
        }
        for(int i=0;i<3;i++)
        {
            Suma=juego[0][i].B+juego[1][i].B+juego[2][i].B;
            if(Suma==3 ||Suma==12)
            {
                if (Suma==3){
                    System.out.println("Gano O");
                    JOptionPane.showMessageDialog(null, "Gano Jugador Uno."
                            + "\n" +" Felicidades: " + jlbuno.getText() + "...!!");
                }
                else if (Suma==12){
                    System.out.println("Gano X");
                    JOptionPane.showMessageDialog(null, "Gano Jugador Dos."
                            + "\n" +"Felicidades: " +  jlbdos.getText() + "...!!");
                }
                Gano=true;
                break;
            } 
        }
        Suma=juego[0][2].B+juego[1][1].B+juego[2][0].B;
        if(Suma==3 ||Suma==12){
            if (Suma==3){
                    System.out.println("Gano O");
                    JOptionPane.showMessageDialog(null, "Gano Jugador Uno."
                            + "\n" +" Felicidades: " + jlbuno.getText() + "...!!");
                }
                else if (Suma==12){
                    System.out.println("Gano X");
                    JOptionPane.showMessageDialog(null, "Gano Jugador Dos."
                            + "\n" +" Felicidades: " +  jlbdos.getText() + "...!!");
                }
                Gano=true;
         }
        Suma=0;
        for(int i=0;i<3;i++)
            Suma+=juego[i][i].B;
        
        if(Suma==3 ||Suma==12){
            if (Suma==3){
                    System.out.println("Gano O");
                    JOptionPane.showMessageDialog(null, "Gano Jugador Uno."
                            + "\n" +"Felicidades: " +  jlbuno.getText() + "...!!");
                }
                else if (Suma==12){
                    System.out.println("Gano X");
                    JOptionPane.showMessageDialog(null, "Gano Jugador Dos. "
                            + "\n" +"Felicidades: " +  jlbdos.getText() + "...!!");
                }
            Gano=true;
        }
                            
        
        
        return Gano;
    }
    
    void limpiar(){
        
        for (int ii=0; ii<3;ii++){
            
            for (int jj=0; jj<3;jj++){
               this.setBackground(Color.gray);
               this.remove(juego[ii][jj].A);
               
                
            }
            
        }
    }
    
    public void  nuevo(){
        
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                juego[i][j] = new Casilla();
                juego[i][j].A.setBounds((i*100)+10, (j*100)+10, 100, 100);
                juego[i][j].A.addActionListener(this);
                this.add(juego[i][j].A);
                this.setBackground(Color.gray);
                juego[i][j].A.setBackground(Color.gray);
            }
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jlbX = new javax.swing.JLabel();
        jlbO = new javax.swing.JLabel();
        jlbuno = new javax.swing.JLabel();
        jlbdos = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jlbX.setText("Turno X:");

        jlbO.setText("Turno O:");

        jlbuno.setText("jLabel3");

        jlbdos.setText("jLabel4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(325, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jlbX)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlbdos, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jlbO)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlbuno, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbuno)
                    .addComponent(jlbO))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbdos)
                    .addComponent(jlbX))
                .addContainerGap(266, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Gato.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Gato.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Gato.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Gato.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Gato().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jlbO;
    private javax.swing.JLabel jlbX;
    private javax.swing.JLabel jlbdos;
    private javax.swing.JLabel jlbuno;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
       while(true){
            recibirDatos();
           /*try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                messagesInformation(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }*/            
        }
    }
}
