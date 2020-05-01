package mesMetaheuristiques;

import java.util.*;
import java.util.ArrayList;
import java.util.Collections;

import city.City;
import tour.Tour;

import static java.lang.Math.*;

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

    public double getShortestTotalDistance() { return ShortestTotalDistance; }

    public double getRateCross(){
        return RATE_CROSS;
    }

    public long getNum_Iter() {
        return num_Iter;
    }

    public ArrayList<City> getShortestTour(){
        return this.Solutions.get(0).getCityList();
    }

    public double getConvergeLimit(){
        return CONVERGE_LIMIT;
    }

    public double getRate_Mutation(){
        return RATE_MUTATION;
    }

    public int getSamples_Size(){
        return SAMPLES_SIZE;
    }

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

    private double[] fitness(){
        double[] fits = new double[SAMPLES_SIZE];
        for(int i = 0; i < this.SAMPLES_SIZE; i++){
            double fit = 1/(this.Solutions.get(i).getDistance());
            fits[i] = 1000*exp(exp(fit));
        }
        return fits;
    }

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

}
