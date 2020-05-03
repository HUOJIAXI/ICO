package apprentissageRenforcement;

import java.util.ArrayList;

import city.City;
import mesMetaheuristiques.ModeleGA;
import tour.Tour;
/**
 *@program: PVC
 *@description: Classe de l'algotithme génétique avec l'apprentissage de renforcement
 *@author: Zhenyu Brie ZHAO
 *@created: 02/05/2020
 */

public class LearningModeleGA {
    private static final int CONDITION_LIMIT = 20;

    private GAs GAsML;
    private int QuantityPopu;
    private double RATE_CROSS;
    private double RATE_MUTATION;
    private long Max_Iter;
    private ArrayList<City> cities = new ArrayList<City>();
    private City depart;
    ApprentissageGA apprentissage;
    private Tour lastSolution;
    private Tour NewSolution;
    private int lastFitness;
    private int NewBestFitness;
    private int iteration;
    private int count;
    private int NUM_Iter;

   /**
    * @Description: Méthode pour obtenir la probabolité de croisement
    * @Paramas:
    * @return: La probabolité de croisement
    */
    private double getRATE_CROSS(){ return this.RATE_CROSS; }

    /**
     * @Description: Méthode pour obtenir la probabilité de mutation
     * @Paramas:
     * @return: La probabilité de mutation
     */
    private double getRATE_MUTATION(){ return this.RATE_MUTATION; }

    /**
     * @Description: Méthode pour obtenir la condition pour justifier que la meuilleure solution est obtenue.
     * @Paramas:
     * @return: La condition de limite
     */
    private int getConditionLimit(){ return this.CONDITION_LIMIT; }

    /**
     * @Description: Méthode pour obtenir la meuilleure valeur de fitness
     * @Paramas:
     * @return: La meuilleure valeur de fitness
     */
    private int getNewBestFitness(){ return this.NewBestFitness; }

    /**
     * @Description: Méthode pour obtenir le nombre de l'itération quand nous arrivons la meuilleure solution
     * @Paramas:
     * @return: Le nombre de l'itération
     */
    private int getNUM_Iter(){ return this.NUM_Iter; }

    /**
     * @Description: Méthode pour obtenir la solution
     * @Paramas:
     * @return: La solution en cour
     */
    private Tour getNewSolution(){ return this.NewSolution; }

    /**
     * @Description: Constructeur de l'algorithme en appliquant l'apprentissage renforcement
     * @Paramas: la taille de la population
     *           la probabolité de croisement
     *           la probabilité de mutation
     *           l'itération maximale
     *           la ville de départ
     *           la liste de villes
     * @return:
     *
     */
    public LearningModeleGA(int Population, double InitpCross, double InitpMutation, long max_iter, City depart, ArrayList<City> cities){
        this.Max_Iter = max_iter;
        this.QuantityPopu = Population;
        this.RATE_CROSS = InitpCross;
        this.RATE_MUTATION = InitpMutation;
        this.cities.addAll(cities);
        this.depart = depart;
        this.iteration = 0;
        this.count = 0;
        this.NUM_Iter = 0;
        this.GAsML = new GAs(this.QuantityPopu, this.RATE_CROSS, this.RATE_MUTATION, this.cities, this.depart);
        GAsML.EvolutionInOneGeneration();
        this.lastSolution = new Tour(this.GAsML.getShortestTour());
        this.NewSolution = new Tour(this.lastSolution);
        this.NewBestFitness = this.GAsML.getFitnessForLearning();
        this.lastFitness = this.NewBestFitness;
        apprentissage = new ApprentissageGA(this.GAsML.getFitnessForLearning(), this.GAsML.getFitnessForLearning(), 10, 0.001, 0.99, this.RATE_CROSS, this.RATE_MUTATION);
    }

    /**
     * @Description: Opération de l'evoluation
     * @Paramas:
     * @return:
     */
    public void Train(){
        while(this.iteration < this.Max_Iter){
            this.iteration++;
            apprentissage.majEpsilon(this.iteration);
            for(int i=0; i<apprentissage.nbStep;i++) {
                int actionChoisie = apprentissage.choseAction();
                apprentissage.action(actionChoisie); //choix d'une action et exécution de cette action
                this.RATE_CROSS = apprentissage.parametre1;
                this.RATE_MUTATION = apprentissage.parametre2;
                this.GAsML.renewParamtres(this.RATE_CROSS, this.RATE_MUTATION);
                this.GAsML.EvolutionInOneGeneration();
                this.NewSolution = this.GAsML.getShortestTour();
                apprentissage.fitnessSolutionProposee = this.GAsML.getFitnessForLearning();
                this.NewBestFitness = this.GAsML.getFitnessForLearning();
                double[] parametresUtilises = {apprentissage.parametre1,apprentissage.parametre2};
                apprentissage.historiqueEtats.add(parametresUtilises);
            }
            if(this.NewBestFitness == this.lastFitness){
                this.count++;
            }
            else{
                this.count = 0;
            }
            if(!(this.count>this.CONDITION_LIMIT)){
                this.NUM_Iter++;
            }
            this.lastSolution = this.NewSolution;
            this.lastFitness = this.NewBestFitness;
        }
    }

    /**
     * @Description: Méthode pour afficher les résultats de l'algorithme génétique sans l'apprentissage
     * @Paramas:
     * @return:
     */
    public void getFinalSolution(){
        System.out.println("--------------------------------------------------------------------------");
        System.out.print("The population quantity : ");
        System.out.println(this.GAsML.getSamples_Size());
        System.out.print("Rate of Cross : ");
        System.out.print(this.getRATE_CROSS());
        System.out.print("  Rate of Mutation : ");
        System.out.println(this.getRATE_MUTATION());
        System.out.println(this.getNewSolution().toString()+" ");
        System.out.print("The shortest distance : ");
        System.out.println(this.NewBestFitness);
        System.out.print("The number of iteration : ");
        System.out.println(this.getNUM_Iter() - this.getConditionLimit());
        System.out.println("--------------------------------------------------------------------------");
    }


}
