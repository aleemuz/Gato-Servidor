/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorgato;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Gateway
 */
public class ClienteThread {
    private Socket socket;
   
    private OutputStream outputStream;
    private InputStream inputStream;
    
    private DataOutputStream salidaDatos;
    private DataInputStream entradaDatos;
    
    private boolean conexionActiva;
    
    public AvisaServidor avisaServidor;
    
    public String usuario;
    
    public ClienteThread(Socket socket){
        this.socket=socket;  
        
        conexionActiva=true;
        System.out.println("inicia el hilo");
        Thread hiloServer = new Thread(new Runnable() {      
            @Override
            public void run() {
                while(conexionActiva){
                    recibirDatos();
                }
                cerrarConexion();
            }
        });
        hiloServer.start();  
        
    }//Fin del constructor
    
    public void recibirDatos(){
        
        try{
            inputStream = socket.getInputStream();
            entradaDatos = new DataInputStream(inputStream);                       
            
            String datos = entradaDatos.readUTF();
            determinarDatoRecibido(datos);
            System.out.println(datos);
        }catch(Exception ex){
            System.out.println("recibir datos --->"+ex);
            conexionActiva=false;
        }
    }    
    
    public void enviarDatos(String datos){
        try{
            outputStream = socket.getOutputStream();
            salidaDatos = new DataOutputStream(outputStream);
            salidaDatos.writeUTF(datos);
            salidaDatos.flush();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void cerrarConexion(){
        try{
            salidaDatos.close();
            entradaDatos.close();
            socket.close();
             if(this.avisaServidor != null){
                this.avisaServidor.closeServer(this.usuario,"close",this);  
        
            } 
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void addListener(AvisaServidor avisaServidor){
        this.avisaServidor = avisaServidor;      
    }
    
    public void determinarDatoRecibido(String msg){
        String verificar [] = msg.split(":");
        
        if (verificar[0].equals("USER")){
            if(this.avisaServidor != null){
                this.usuario = verificar[1];
                this.avisaServidor.onClientReceiveName(verificar[1]);  
                System.out.println(verificar[1]);                
            } 
        }else if (verificar[0].equals("MSG")){
            if(this.avisaServidor != null){
                this.avisaServidor.onClientReceiveMessage(this.usuario+":"+verificar[1]);  
                System.out.println(this.usuario+":"+verificar[1]);
            } 
        }else if(verificar[0].equals("CLOSE")){
            cerrarConexion();
        }
    }
}
