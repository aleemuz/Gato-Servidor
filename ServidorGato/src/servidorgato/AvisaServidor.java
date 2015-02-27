/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorgato;

import java.util.EventListener;

/**
 *
 * @author Gateway
 */
interface AvisaServidor extends EventListener{
    
   public void onClientReceiveName(String name);
   public void onClientReceiveMessage(String msg);
   public void onClientReceivePrivateMessage(String user,String msg);
   public void closeServer(String user,String msg);

    public void closeServer(String usuario, String close, ClienteThread aThis);
    
}
