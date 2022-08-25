public class Student implements Comparable<Student> {
	private int ID;
	String name;
	int duration;
	double rating;
	int house; //id of the house the student is currently living
	
	public Student(int iD, String name, int duration, double rating) {
		ID = iD;
		this.name = name;
		this.duration = duration;
		this.rating = rating;
		house = -1;
	}

	@Override
	public int compareTo(Student o) {
		return this.ID - o.ID;
	}

}
