package recuitsimule;

public class Recuitsimule {
	private double Tempinit = 5000;
	private double Tempfini = 0.0001;
	
	private double LoopInternal=1000;
	private double coolingRate = 0.001;
	
	private Route Solution ;
	
	   public void initTour() {
	        Route route = new Route();
	        route.addCity(new City("Bordeaux", 44.8333, -0.5667))
	            .addCity(new City("Lyon", 45.75,4.85))
	            .addCity(new City("Nantes",47.2173, -1.5534))
	            .addCity(new City("Paris", 48.8534,2.3488))
	            .addCity(new City("Marseille",43.3, 5.4))
	            .addCity(new City("Dijon",47.3167,  5.0167));
	      // System.out.println(route);
	        Solution = route.GenerateTour();
	        System.out.println("Initial solution distance: " + Solution.getTotalDistance());
	        System.out.println("Route: " + Solution);
	    }
	   
	   public Route recuit()
	   {
		   Route MeuSolution = new Route(Solution.getCities());
		   Route NouSolution = null;
		   while(Tempinit > Tempfini ) {
			   for(int i = 0;i<LoopInternal;i++)
			   {
				   NouSolution=Solution.GenerateTourVoisinage();
				   
				   double Psolution=Solution.getTotalDistance();
				   
				   double Pvsolution=NouSolution.getTotalDistance();
				   
				   if (Probabilite(Psolution, Pvsolution, Tempinit)>Math.random())
				   {
					   Solution = new Route(NouSolution.getCities());
				   }
				   
				   if (Solution.getTotalDistance() < MeuSolution.getTotalDistance())
				   {
					   MeuSolution = new Route(Solution.getCities());
				   }			   
			   }
			   Tempinit *= 1-coolingRate;
		   }
		   
		   return MeuSolution;
	   }
	   
	   public double Probabilite(double P,double NP,double temperature)
	   {
		   if (NP<P)
		   {
			   return 1.0;
		   }
		   
		   return Math.exp((P-NP)/temperature);
	   }

}
