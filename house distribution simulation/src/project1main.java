import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;

public class project1main {
	public static void main(String[] args) throws FileNotFoundException {
		
		Scanner in = new Scanner(new File(args[0]));
		PrintStream out = new PrintStream(new File(args[1]));
		
//INPUT PART
		ArrayList<House> houseArray = new ArrayList<>();
		ArrayList<Student> studentArray = new ArrayList<>();
		String inLineArray[] = new String[5];
		PriorityQueue<Student> studentPQ = new PriorityQueue<>();
	
		while (in.hasNextLine()) {			
			inLineArray = in.nextLine().split(" ",5);		
			int ID = Integer.parseInt(inLineArray[1]);			
			if(inLineArray[0].equals("h")) {
				int duration = Integer.parseInt(inLineArray[2]);
				double rating = Double.parseDouble(inLineArray[3]);
				House house = new House(ID, duration, rating);
				houseArray.add(house);
			}
			
			if(inLineArray[0].equals("s")) {
				String name = inLineArray[2];
				int duration = Integer.parseInt(inLineArray[3]);
				double rating = Double.parseDouble(inLineArray[4]);
				Student student = new Student(ID, name, duration, rating);
				studentArray.add(student);
			}		
		}
//INPUT PART
		
		Collections.sort(houseArray);
		Collections.sort(studentArray);
		
		//for loop for 8 semesters
		for(int x=0; x<8; x++) { 
			int itr = 0;
			while(itr<studentArray.size() && !studentArray.isEmpty()) {
				Student currentS = studentArray.get(itr);

				for (int h=0; h<houseArray.size(); h++) {
					if(houseArray.get(h).duration == 0 && currentS.duration !=0 &&
						houseArray.get(h).rating >= currentS.rating && currentS.house == -1)   {
								
						houseArray.get(h).duration += currentS.duration;
						currentS.house = houseArray.get(h).ID;
						break;
					}
				}
				//graduating
				if(currentS.duration == 0) {
					studentArray.remove(currentS);
					if(currentS.house==-1) {
						studentPQ.add(currentS);
					}
				}
				else {
					itr += 1;
				}	
			}
					
			for(House h: houseArray) {
				if (h.duration != 0) {
					h.duration -= 1;
				}
			}
			
			for(Student s: studentArray) {
				s.duration -= 1;				
			}			
		} //end of semester loop
		
		for(Student s1: studentArray) {
			if(s1.house == -1) {
				studentPQ.add(s1);
			}
		}		
		
//OUTPUT PART
		int studentNum = studentPQ.size();		
		for(int outInd=0; outInd<studentNum; outInd++) {
			Student printCurrentStudent = studentPQ.poll();
			out.println(printCurrentStudent.name);
		}		
	}
}
