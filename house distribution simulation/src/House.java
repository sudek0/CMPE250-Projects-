public class House implements Comparable<House>{
	int ID;
	int duration;
	double rating;
	
	public House(int iD, int duration, double rating) {
		ID = iD;
		this.duration = duration;
		this.rating = rating;
	}

	@Override
	public int compareTo(House o) {
		return this.ID-o.ID;
	}
}
