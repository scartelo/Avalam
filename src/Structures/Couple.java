package Structures;

public class Couple<Premier, Second> {
	Premier premier;
	Second second;

	public Couple(Premier p, Second s) {
		premier = p;
		second = s;
	}
	public Premier premier(){
		return premier;
	}
	public Second second(){
		return second;
	}
	@Override
	public String toString() {
		return "(" + premier + ", " + second + ")";
	}

	public void setPremier(Premier p){
		premier = p;
	}

	public void setSecond(Second s){
		second = s;
	}

}

