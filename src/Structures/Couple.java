package Structures;

public class Couple<Premier, Second> {
	Premier premier;
	Second second;

	public Couple(Premier p, Second s) {
		premier = p;
		second = s;
	}
	public Premier get_premier(){
		return premier;
	}
	public Second get_second(){
		return second;
	}
	@Override
	public String toString() {
		return "(" + premier + ", " + second + ")";
	}


}
