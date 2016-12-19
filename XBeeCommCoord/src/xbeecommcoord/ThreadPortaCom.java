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

/**
 *
 * @author Rafael
 */
public class ThreadPortaCom implements Runnable{
    
    String destinatario; 
    String mensagem;
    String port;
    int baud_rate;

    public ThreadPortaCom(String destinatario, String mensagem, String port, int baud_rate) {
        this.destinatario = destinatario;
        this.mensagem = mensagem;
        this.port = port;
        this.baud_rate = baud_rate;
    }
    
        
    public boolean inicializarConexaoRedeZigbee(){
        
        boolean retorno;     
        
        try{
            ZigBeeDevice myDevice = new ZigBeeDevice(port, baud_rate); 
            myDevice.open();
            retorno = inicializarRedeZigbee(myDevice);
        }catch(Exception e){
           return false;
        }
        
        return retorno;    
        
    }
    
    public boolean inicializarRedeZigbee(ZigBeeDevice myDevice){
        
        boolean retorno;
      
        try{
            XBeeNetwork network = myDevice.getNetwork();
            RemoteXBeeDevice remote = network.discoverDevice(destinatario);
            System.out.println("Destinatário encontrado: - " + remote.getNodeID());
            retorno = enviarRequisicao(myDevice, network);
        }catch(Exception e){
            return false;
        }
        return retorno;
    }
    
    public boolean enviarRequisicao(ZigBeeDevice myDevice, XBeeNetwork network){
               
        try{
            RemoteXBeeDevice remote = myDevice.getNetwork().getDevice(destinatario);
            if(remote != null){
                myDevice.sendData(remote, mensagem.getBytes());
                System.out.println(myDevice.readData().getDataString());
            }else{
                System.err.println("Could not find the module " + destinatario + " in the network.");
            }
        }catch(XBeeException e) {
           return false;
        }
        return true;
    }
    
    @Override
    public void run(){
        try {
            while(!inicializarConexaoRedeZigbee()){
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
