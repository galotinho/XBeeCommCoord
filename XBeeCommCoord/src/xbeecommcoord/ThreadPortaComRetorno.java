/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xbeecommcoord;

import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.XBeeNetwork;
import com.digi.xbee.api.ZigBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;
import java.util.concurrent.Callable;

/**
 *
 * @author Rafael
 */
public class ThreadPortaComRetorno implements Callable<String>{
    
    String destinatario; 
    String mensagem;
    String port;
    int baud_rate;

    public ThreadPortaComRetorno(String destinatario, String mensagem, String port, int baud_rate) {
        this.destinatario = destinatario;
        this.mensagem = mensagem;
        this.port = port;
        this.baud_rate = baud_rate;
    }
    
        
    public String inicializarConexaoRedeZigbee(){
        try{
            ZigBeeDevice myDevice = new ZigBeeDevice(port, baud_rate); 
            myDevice.open();
            return inicializarRedeZigbee(myDevice);
        }catch(Exception e){
           return "false";
        }
    }
    
    public String inicializarRedeZigbee(ZigBeeDevice myDevice){
        try{
            XBeeNetwork network = myDevice.getNetwork();
            network.discoverDevice(destinatario);
            return enviarRequisicao(myDevice, network);
        }catch(Exception e){
            return "false";
        }
    }
    
    public String enviarRequisicao(ZigBeeDevice myDevice, XBeeNetwork network){
               
        try{
            RemoteXBeeDevice remote = myDevice.getNetwork().getDevice(destinatario);
            if(remote != null){
                myDevice.sendData(remote, mensagem.getBytes());
                return myDevice.readData().getDataString();
            }else{
                System.err.println("Could not find the module " + destinatario + " in the network.");
                return "false";
            }
        }catch(XBeeException e) {
           return "false";
        }
    }
    
    @Override
    public String call(){
        String resultado = null;
        try {
            do{
                resultado = inicializarConexaoRedeZigbee();
                if(resultado.equals("false")){
                    Thread.sleep(500);
                }
            }while(resultado.equals("false"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
}
