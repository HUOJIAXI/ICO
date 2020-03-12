package recuitsimule;

public class Main {
	
	   public static void main(String[] args) {
		   Recuitsimule RS=new Recuitsimule();
		   RS.initTour();
		   Route MeuRoute = RS.recuit();
		   System.out.println("Meuilleure solution: " + MeuRoute.getTotalDistance());
		   System.out.println("Route: " + MeuRoute);
		   
	}

}
