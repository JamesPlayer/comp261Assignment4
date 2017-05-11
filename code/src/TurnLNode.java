import java.util.ArrayList;

public class TurnLNode extends Node {

	public TurnLNode() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Robot robot) {
		robot.turnLeft();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "turn left;\n";
	}

}
