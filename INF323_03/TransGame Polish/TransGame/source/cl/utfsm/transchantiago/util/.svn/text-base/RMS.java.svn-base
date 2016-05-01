/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.utfsm.transchantiago.util;

/**
 *
 * @author eraddatz
 */

import java.io.*;
import javax.microedition.rms.*;

public class RMS{
	static final String BD = "baseDatos";
	private String dato;
	private int x=0;
	

	public boolean hayRegistro(){
		try{
			RecordStore rs = RecordStore.openRecordStore(BD,true);
			this.x = rs.getNumRecords();
			rs.closeRecordStore();
		}catch(RecordStoreException e){
			System.out.println(e);
        }
		if(x == 0){
			return false;
		}else{
			return true;
		}
	}
	
	public String verRegistro(int id){
        try{
			RecordStore rs = RecordStore.openRecordStore(BD,true);
            try{
                ByteArrayInputStream bais = new ByteArrayInputStream(rs.getRecord(id));
                DataInputStream is = new DataInputStream(bais);
                     try{
                          dato = is.readUTF();
                          }catch(EOFException eofe){}
                           catch(IOException ioe){}
                  }catch(RecordStoreException e){}
			rs.closeRecordStore();
		}catch(RecordStoreException e){
			System.out.println(e);
        }
        return dato;
	}
	
	public void eliminaBD(){
		try{
			RecordStore.deleteRecordStore(BD);
		}catch(Exception e){
			System.out.println("ex:" + e);
		}
		
	}
	
	public void guardaRegistro(String dato){ 	
        try{
            RecordStore rs = RecordStore.openRecordStore(BD,true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream os = new DataOutputStream(baos);
            try{
            	os.writeUTF(dato);
            }catch (IOException ioe){}

            byte[] b = baos.toByteArray();

            try{
            	rs.addRecord(b,0,b.length);            	
            }catch(RecordStoreException rse) {}
            rs.closeRecordStore();
        }catch(RecordStoreException e){}
    }
}
