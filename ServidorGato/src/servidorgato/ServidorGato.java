/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorgato;

/**
 *
 * @author Gateway
 */
public class ServidorGato {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InterfaceServer interfaceServer = new InterfaceServer();
        interfaceServer.setVisible(true);
        interfaceServer.connection(8080);
    }
    
}
