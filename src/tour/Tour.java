package tour;

import java.util.ArrayList;
import java.util.Collections;

import city.City;
import jade.util.leap.Serializable;

public class Tour implements Serializable {
	private ArrayList<City> cityList = new ArrayList<City>();
    private double distance = 0;
    private City depart;
    private int num_cities = 0;

    public Tour() {
        this.cityList=new ArrayList<City>();
    }

    public Tour(Tour tour){
        this.cityList.addAll(tour.cityList);
        this.depart = tour.depart;
        this.distance = tour.distance;
        this.num_cities = tour.num_cities;
    }

    public Tour(ArrayList<City> cities){
        this.cityList.addAll(cities);
        if(!cities.isEmpty()) {
        	this.depart = cities.get(0);
        	this.num_cities = cities.size();
        }
    }

    public Tour(City depart, ArrayList<City> citylist) {
        this.depart=depart;
        this.num_cities = citylist.size();
        for(City city:citylist) {
            this.cityList.add(city);
        }
    }

	public Tour generateIndividuel() {
		Collections.shuffle(this.cityList);
		for (int i = 0; i < this.cityList.size(); i++) {
			if (this.cityList.get(i).getName() == this.depart.getName()) { //严格来说这里要用equal（）函数
				Collections.swap(this.cityList, 0, i);
//				System.out.println("Ville Départ: "+this.cityList.get(i));
//				City city0=this.cityList.get(0);
//				this.cityList.set(0, this.depart);
//				this.cityList.set(i, city0);
				break;
			}
		}
		Tour tr=new Tour(this.depart,this.cityList);
		return tr;
	}

	public Tour createNewNeighbourTour() {
		Tour newSolution = new Tour(this.depart, this.cityList);
		int position1 = 0;
		int position2 = 0;
		while(position1==position2||position1==0||position2==0)
		{
			position1 = (int) (newSolution.getCityList().size() * Math.random());
			position2 = (int) (newSolution.getCityList().size() * Math.random());
		}
//		int position1 = (int) ((this.cityList.size() - 1) * Math.random()) + 1;
//		int position2 = (int) ((this.cityList.size() - 1) * Math.random()) + 1;

		City city1 = this.cityList.get(position1);
		City city2 = this.cityList.get(position2);

		newSolution.cityList.set(position1, city2);
		newSolution.cityList.set(position2, city1);

		return newSolution;
	}

	public City getCity(int index) {
		return this.cityList.get(index);
	}

	public void setCity(int index, City city) {
		this.cityList.set(index, city);
		this.distance = this.getDistance();
	}

	public void setDepart(City depart) {
		this.depart = depart;
	}
	
	public Tour addCity(City city) {
		this.cityList.add(city);
		return this;
	}

	public ArrayList<City> getCityList() {
		return this.cityList;
	}

	public City getDepart() {
		return this.depart;
	}
	
    public int getNum_cities(){
        return this.num_cities;
    }

    public double getDistance() {
        int cities_nums = this.cityList.size();
        double TotalDistance = 0;
        ArrayList<City> x = getCityList();
        for(int i = 0;i < cities_nums - 1;i++){
            TotalDistance += x.get(i).measureDistance(x.get(i+1));
        }
        return TotalDistance;
    }
    
	public String toString() {
		String geneString = "";
		int i = 0;
		for (i = 0; i < this.cityList.size() - 1; i++) {
			geneString += getCity(i) + " -> ";
		}
		geneString += getCity(i);
		return geneString;
	}

	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		final Tour other = (Tour) obj;
		if (this.cityList.isEmpty() || other.cityList.isEmpty()) {
			return false;
		}
		for (int i = 0; i < this.cityList.size(); i++) {
			if (!other.getCityList().get(i).equals(this.cityList.get(i)))
				;
			return false;
		}
		return true;
	}


}
