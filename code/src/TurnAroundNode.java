import java.util.ArrayList;

public class TurnAroundNode extends Node {

	public TurnAroundNode() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Robot robot) {
		robot.turnAround();
	}

	@Override
	public String toString() {
		return "turn around;";
	}

}
