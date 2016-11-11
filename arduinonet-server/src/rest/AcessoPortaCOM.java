package rest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import com.fazecast.jSerialComm.SerialPort;

public class AcessoPortaCOM {
	
	private static Scanner scannerIn; 
	private static OutputStream scannerOut;	
	private static SerialPort chosenPort;	

	
	public String line;;
	//Singleton ----------------------------------------------
	public static AcessoPortaCOM acesso;
	   public static AcessoPortaCOM getInstance() {
	      if(acesso == null) {
	         acesso = new AcessoPortaCOM();
	         System.out.println("O objeto2 �: " + acesso);
	         chosenPort = SerialPort.getCommPort("ttyACM0");
	         chosenPort.setBaudRate(115200);
	 		 chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
	 		 chosenPort.openPort(); 
	 		 
	 		 //Objetos para enviar e receber dados da porta serial
	 		 scannerIn = new Scanner(chosenPort.getInputStream()); 			//Recebe o fluxo de informa��es que vem da porta serial
	 		 scannerOut = chosenPort.getOutputStream();		//Envia o fluxo de dados pela porta serial
	      
	      }
	      return acesso;
	   }
	//----------------------------------------------
	   
	public void leitura(){ 			
		
		while(scannerIn.hasNext()){ 
			setLine(scannerIn.nextLine());
		}		
	}
	
	
	
	public void closePort() {
		chosenPort.closePort();
	}

	public String readDataBySerialPort() {
		try{
			return scannerIn.nextLine();
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		return "Houston, we have a problem!!";		
	}

	public void setLine(String line) {
		this.line = line;
	}
	
	public void sendDataBySerialPort(String message) throws IOException {
		try{
			scannerOut.write(message.getBytes());			
		}catch(IOException e){
			System.err.println(e);
		}
			
	}
}