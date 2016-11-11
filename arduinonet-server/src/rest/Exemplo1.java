package rest;
 
import java.io.IOException;

import com.google.gson.*;
import com.google.gson.GsonBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/echo")
public class Exemplo1 {
	
	private class Measures{
		private String voltage = "";
		private String temperature = "";
		private String luminosity = "";
	}
	
//Código de auxilio para fazer leitura de várias grandezas

//	public String echo(@QueryParam("word")String palavra, @QueryParam("word2")String palavra2){
//		System.out.println("Log: " + palavra2);
//		//log de acesso! 
//		return "Echo: " + palavra;
//	}

	AcessoPortaCOM acesso = AcessoPortaCOM.getInstance();
	Measures measures = new Measures();
	
	//JSONObject
	GsonBuilder builder = new GsonBuilder();
	Gson gson = builder.setPrettyPrinting().create();
	
	
	//Método de teste para construção do JSON
//	@Path("/printJson")
//	@GET
//	@Produces("text/plain")
//	public String printJson(){	
//		try{
//			System.out.println(gson);
//		}catch(JsonIOException e){
//			e.printStackTrace();
//		}
//		return gson.toString();
//	}
	
	
	

	
//	@Path("iniciar")
//	@GET
//	@Produces("text/plain")
//	public void readSerial(){
//		//System.out.println("O objeto �: " + acesso);		//Código para debug
//		acesso.leitura();
//	}
	
	@Path("/readData")
	@GET
	@Produces("text/plain")
	//Os paramentros de entrada só podem ser t, e ou l
	public String readData(){	
			try {
				acesso.sendDataBySerialPort("v");
				measures.voltage = acesso.readDataBySerialPort();
				acesso.sendDataBySerialPort("t");
				measures.temperature = acesso.readDataBySerialPort();
				acesso.sendDataBySerialPort("l");
				measures.luminosity = acesso.readDataBySerialPort();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String json = gson.toJson(measures);
			System.out.println(json);
			return json.toString();
	}
	
	@Path("/c")
	@GET
	@Produces("text/plain")
	//Os paramentros de entrada só podem ser t, e ou l
	public String readSingleData(@QueryParam("sensor")String message){	
			try {
				acesso.sendDataBySerialPort(message);
				System.out.println("Mensagem enviada: " + message);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return acesso.readDataBySerialPort();
	}
	
	@Path("/closePort")
	@GET
	@Produces("text/plain")
	public String closePort(){
		//acesso = AcessoPortaCOM.getInstance();
		System.out.println("Porta fechada");
		System.out.println("O objeto 3 " + acesso);
		acesso.closePort();
		return "Porta de comunicação serial fechada";
	}

	
}