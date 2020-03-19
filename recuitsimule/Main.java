package recuitsimule;

//import java.util.Arrays;
//import java.util.Collections;


public class Main {
		
	   public static void main(String[] args) {

		   Recuitsimule RS=new Recuitsimule();
		   RS.initTour();
		   Route MeuRoute = RS.recuit();
		   System.out.println("Meuilleure solution: " + MeuRoute.getTotalDistance());
		   
		   System.out.println("Route: " + MeuRoute.getCities().toString());
		   
//		   for(int i =5;i>=0;i--)
//		   {
//			   System.out.print("  " + MeuRoute.getCity(i).toString()+ "  -");
//		   }
		 //  System.out.print("Bordeaux");
		//   System.out.println(MeuRoute.toString().getClass());
		   
	}
	   

}
