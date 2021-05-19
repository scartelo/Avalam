package Structures;

public class Couple<Premier, Second> {
	Premier premier;
	Second second;

	public Couple(Premier p, Second s) {
		premier = p;
		second = s;
	}

	@Override
	public String toString() {
		return "(" + premier + ", " + second + ")";
	}


}
