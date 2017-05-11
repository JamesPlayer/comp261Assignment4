import java.util.ArrayList;

public class MoveNode extends Node {

	public MoveNode() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Robot robot) {
		robot.move();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "move;";
	}

}
