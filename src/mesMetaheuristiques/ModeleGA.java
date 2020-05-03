package mesMetaheuristiques;

import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import city.City;
import data.RequeteVilleNombre;
import tour.Tour;
import static java.lang.Math.*;

/**
 *@description: Classe de l'algotithme génétique
 *@author: Zhenyu Brie ZHAO
 *@created: 02/05/2020
 */
public class ModeleGA {
    private static final long MAX_ITER = 10000;
    private static final double CONVERGE_LIMIT = 0.00001;
    private static final double RATE_CROSS = 0.95;

    private double RATE_MUTATION;
    private int SAMPLES_SIZE;
    private int ROUTE_SIZE;
    private long num_Iter;
    private double ShortestTotalDistance;
    private ArrayList<City> cities = new ArrayList<City>();
    private ArrayList<Tour> Solutions = new ArrayList<Tour>();
    private ArrayList<Tour> Generation_Parents = new ArrayList<Tour>();
    private City depart;
    public long getMaxIter(){
        return MAX_ITER;
    }

    /**
     * @Description: Méthode pour obtenir la distance de la meuilleure solution du PVC
     * @Paramas: 
     * @return: La distance de la meuilleure solution du PVC
     */
    public double getShortestTotalDistance() { return ShortestTotalDistance; }

    /**
     * @Description: Méthode pour obtenir la probabolité de croisement
     * @Paramas: 
     * @return: La probabolité de croisement
     */
    public double getRateCross(){
        return RATE_CROSS;
    }
    
    /**
     * @Description: Méthode pour obtenir le nombre de l'itération quand nous arrivons la meuilleure solution
     * @Paramas: 
     * @return: le nombre de l'itération
     */
    public long getNum_Iter() {
        return num_Iter;
    }

    /**
     * @Description: Méthode pour obtenir la meuilleure solution
     * @Paramas: 
     * @return: la meuilleure solution
     */
    public ArrayList<City> getShortestTour(){
        return this.Solutions.get(0).getCityList();
    }

    /**
     * @Description: Méthode pour obtenir la condition pour justifier que la meuilleure solution est obtenue.
     * @Paramas: 
     * @return: la condition de limite
     */
    public double getConvergeLimit(){
        return CONVERGE_LIMIT;
    }

    /**
     * @Description: Méthode pour obtenir la probabilité de mutation
     * @Paramas: 
     * @return: la probabilité de mutation
     */
    public double getRate_Mutation(){
        return RATE_MUTATION;
    }

    /**
     * @Description: Méthode pour obtenir la taille de la population
     * @Paramas:
     * @return: la taille de la population
     */
    public int getSamples_Size(){
        return SAMPLES_SIZE;
    }
    
    /**
     * @Description: Constructeur des la classe l'algorithme en limitant la ville de départ
     * @Paramas: la taille de la population
     *           la probabilité de mutation
     *           la liste de villes
     *           la ville de départ
     * @return: null
     */
    public ModeleGA(int num_Sample, double Rate_Mutation, ArrayList<City> cities, City city){
        this.num_Iter = 0;
        this.depart = city;
        this.ShortestTotalDistance = 0;
        this.SAMPLES_SIZE = num_Sample;
        this.RATE_MUTATION = Rate_Mutation;
        this.cities.addAll(cities);
        this.ROUTE_SIZE = cities.size();
        for(int i = 0; i < this.SAMPLES_SIZE; i++){
            Tour r = new Tour(city, cities);
            r = r.generateIndividuel();
            this.Solutions.add(r);
        }
    }
    
    /**
     * @Description: Constructeur des la classe l'algorithme avec une ville de départ aléatoire
     * @Paramas: la taille de la population
     *           la probabolité de croisement
     *           la probabilité de mutation
     *           la liste de villes
     * @return: null
     */
    public ModeleGA(int num_Sample, double Rate_Mutation, Tour solution){
        this.num_Iter = 0;
        this.depart = solution.getDepart();
        this.ShortestTotalDistance = 0;
        this.SAMPLES_SIZE = num_Sample;
        this.RATE_MUTATION = Rate_Mutation;
        this.cities.addAll(solution.getCityList());
        this.ROUTE_SIZE = this.cities.size();
        this.Solutions.add(solution);
        for(int i = 1; i < this.SAMPLES_SIZE; i++){
            Tour r = new Tour(this.depart, this.cities);
            r = r.generateIndividuel();
            this.Solutions.add(r);
        }
    }

    /**
     * @Description: Méthode pour calculer les fonctions de "fitness" pour la population des solutions
     * @Paramas:
     * @return: Une liste de fonctions de "fitness"
     */
    private double[] fitness(){
        double[] fits = new double[SAMPLES_SIZE];
        for(int i = 0; i < this.SAMPLES_SIZE; i++){
            double fit = 1/(this.Solutions.get(i).getDistance());
            fits[i] = 1000*exp(exp(fit));
        }
        return fits;
    }

    /**
     * @Description: Méthode pour obtenir l'indice de la valeur maximale dans une liste
     * @Paramas: Une liste des valeurs
     * @return: l'indice de la valeur maximale
     */
    private int getMax_index(double[] a){
        double t = a[0];
        int target = 0;
        for(int i = 1; i<a.length; i++){
            if(a[i] > t){
                t = a[i];
                target = i;
            }
        }
        return target;
    }

    /**
     * @Description: Méthode pour calculer les probabilités de choix avec les valeurs d'une liste.
     *               Plus grande la valeur, plus probablement la choisir
     * @Paramas: Une liste des valeurs
     * @return: Une liste de intervalles divisées par chaque valeur
     */
    private double[] getP_interval(double[] a){
        double sum = 0;
        for(double x : a){
            sum += x;
        }
        double[] P_interval = new double[a.length];
        for(int j = 0; j < a.length; j++){
            P_interval[j] = a[j]/sum;
        }
        return P_interval;
    }
    
    /**
     * @Description: Méthode de la sélection d'une génération des parents
     * @Paramas:
     * @return:
     */
    public void getGenerationI(){
        double[] fits = fitness();
        int k = getMax_index(fits);
        this.Generation_Parents.add(this.Solutions.get(k));
        double[] p_interval = getP_interval(fits);
        for(int i = 1; i < this.SAMPLES_SIZE; i++) {
            double p = random();
            int j = 0;
            while (p_interval[j] < p && p_interval[j + 1] <= p) {
                j++;
                if(j >= this.SAMPLES_SIZE - 1) break;
            }
            Tour r_parent = new Tour(Solutions.get(j));
            this.Generation_Parents.add(r_parent);
        }
    }

    /**
     * @Description: Méthode pour comparer une liste de villes et la liste de villes complète et compenser les villes manquantes
     * @Paramas: Une liste de villes incomplète
     * @return: Une liste de villes complète
     */
    public ArrayList<City> getdifferentlist(ArrayList<City> r){
        Map<String, Integer> map=new HashMap<String, Integer>();
        ArrayList<City> longList= this.cities;
        ArrayList<City> shortList= r;
        if(r.size()>this.cities.size()){
            longList=r;
            shortList=this.cities;
        }
        for(City city: shortList){
            map.put(city.getName(),0);
        }
        Integer in;
        for(City city:longList){
            in = map.get(city.getName());
            if(null==in){
                shortList.add(city);
            }
        }
        return shortList;
    }

    public ArrayList<City> getdifferentlist2(ArrayList<City> r){
        ArrayList<City> shortList = new ArrayList<City>();
        Set set = new HashSet();
        for(City city: r){
            set.add(city.getName());
        }
        for(City city: this.cities){
            if(!set.add(city.getName())){
                shortList.add(city);
            }
        }
        return shortList;
    }

    /**
     * @Description: Méthode pour enlever les villes répétitives dans une liste
     * @Paramas: Une liste de villes
     * @return:
     */
    public void removeDuplicate(ArrayList<City> r){
        Set set = new HashSet();
        ArrayList<City> newline = new ArrayList<City>();
        for(City city: r){
            if(set.add(city.getName())){
                newline.add(city);
            }
        }
        r.clear();
        r.addAll(newline);
    }
    
    /**
     * @Description: Opération de mutation
     * @Paramas: Une liste de villes
     * @return: Une liste de villes apèrs la mutation
     */
    private ArrayList<City> getMutation(ArrayList<City> r){
        double p = random();
        ArrayList<City> rm = r;
        if( p < getRate_Mutation()){
            int i = (int)(1+random()*(this.ROUTE_SIZE-2));
            int j = (int)(1+random()*(this.ROUTE_SIZE-2));
            City t = rm.get(i);
            rm.set(i, r.get(j));
            rm.set(j, t);
        }
        return rm;
    }

    /**
     * @Description: Opération de croisement
     * @Paramas: Deux indices de liste des parents
     * @return:
     */
    public void Xcross(int i, int j){
        Tour r_parent1 = new Tour(this.Generation_Parents.get(i));
        Tour r_parent2 = new Tour(this.Generation_Parents.get(j));
        double p = random();
        if(p <= getRateCross()){
            ArrayList<City> r1 = new ArrayList<City>();
            ArrayList<City> r2 = new ArrayList<City>();
            int k = (int)(random()*(this.ROUTE_SIZE-1));
            int l = 1+(int)(random()*(this.ROUTE_SIZE - 1-k));
            int t = 0;
            r1.add(this.depart);
            r2.add(this.depart);
            /*while(t < this.ROUTE_SIZE){
                if((t>=k)&&(t < (k+l))){
                    r2.add(r_parent1.getCityList().get(t));
                }
            }*/
            for(City city:r_parent1.getCityList()){
                if((t>=k)&&(t < (k+l))){
                    r2.add(city);
                }
                else if(t>0){
                    r1.add(city);
                }
                t++;
            }
            t = 0;
            for(City city:r_parent2.getCityList()){
                if((t>=k)&&(t<=(k+l))){
                    r1.add(city);
                }
                else if(t>0){
                    r2.add(city);
                }
                t++;
            }
            removeDuplicate(r1);
            removeDuplicate(r2);
            r1 = getdifferentlist(r1);
            r1 = getMutation(r1);
            r2 = getdifferentlist(r2);
            r2 = getMutation(r2);
            Tour R1 = new Tour(r1);
            Tour R2 = new Tour(r2);
            this.Solutions.set(i,R1);
            this.Solutions.set(j,R2);
        }
        else{
            this.Solutions.set(i,r_parent1);
            this.Solutions.set(j,r_parent2);
        }
    }

    public boolean isGetSolution(){
        return true;
    }

    /**
     * @Description: Evaluation jusqu'à la itération maximale
     * @Paramas:
     * @return:
     */
    public void Evolution(){
        int i = 0;
        ArrayList<Integer> indexs = new ArrayList<Integer>();
        for(int j = 1; j < this.SAMPLES_SIZE; j ++){
            indexs.add(j);
        }
        while(i < getMaxIter()){
            getGenerationI();
            this.Solutions.set(0, this.Generation_Parents.get(0));
            ArrayList<Integer> newList = new ArrayList<Integer>();
            newList.addAll(indexs);
            Collections.shuffle(newList);
            while(!newList.isEmpty()){
                if(newList.size() == 1){
                    this.Solutions.set(newList.get(0),this.Generation_Parents.get(newList.get(0)));
                    newList.remove(0);
                    break;
                }
                int rp1 = newList.get(0);
                newList.remove(0);
                int rp2 = newList.get(0);
                newList.remove(0);
                Xcross(rp1, rp2);
            }
            this.Generation_Parents.clear();
            i++;
            if(this.Solutions.get(0).getDistance()>1331.91){
                this.num_Iter++;
            }

            this.ShortestTotalDistance = this.Solutions.get(0).getDistance();
        }
    }

    /**
     * @Description: Méthode pour afficher les résultats de l'algorithme génétique sans l'apprentissage
     * @Paramas:
     * @return:
     */
    public void getFinalSolution(){
        for(int i = 0; i < this.SAMPLES_SIZE; i++) {
            for (City city : this.Solutions.get(i).getCityList()) {
                System.out.print(city);
                System.out.print(" ");
            }
            System.out.print(this.Solutions.get(i).getDistance());
            System.out.println(" ");
        }
    }


    
    public static void main(String[] args){
    	RequeteVilleNombre requeteVilleNobre = new RequeteVilleNombre(30);
		ArrayList<String> carte = requeteVilleNobre.BDDconnexion();
		ArrayList<String> tableauVilles = requeteVilleNobre.getName(carte);
		ArrayList<Double> listeLatitudes = requeteVilleNobre.getLatitude(carte);
		ArrayList<Double> listeLongitudes = requeteVilleNobre.getLongitude(carte);
		Tour tourAInit=new Tour();
		tourAInit.setDepart(new City(tableauVilles.get(0), listeLatitudes.get(0),listeLongitudes.get(0)));
		for(int i=0;i<tableauVilles.size();i++) {
			City city=new City(tableauVilles.get(i),listeLatitudes.get(i),listeLongitudes.get(i));
			tourAInit.addCity(city);
		}

		for(int i=0;i<5;i++) {
			    long initTime = System.currentTimeMillis();
			    ModeleGA gas = new ModeleGA(50,0.15, tourAInit.getCityList(),tourAInit.getDepart());
			    gas.Evolution();
		        Tour bestT = new Tour(gas.getShortestTour());
		        double bestDis = bestT.getDistance();
				long overTime = System.currentTimeMillis();
				long excutionTime=overTime-initTime;		// variable qui enregistre le temps utilisé par mon modèle heuristique chaquefois
				System.out.println("GA    |Best Tour: " + bestT);
				System.out.println("GA    |Final solution distance: " + bestDis);
				System.out.println("GA    |Le temps d'éxecution est :"+ excutionTime+"ms");
			}
    }
}
