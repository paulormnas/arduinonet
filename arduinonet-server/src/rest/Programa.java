package rest;

public class Programa {	
	
	public static void main(String[] args) {		
	
	AcessoPortaCOM acesso = AcessoPortaCOM.getInstance();
	System.out.println("O objeto �: " + acesso);
	
	
	acesso.leitura();
	
	}
}