import java.util.ArrayList;

public class TurnRNode extends Node {

	public TurnRNode() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Robot robot) {
		robot.turnRight();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "turn right;\n";
	}

}
