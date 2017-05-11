import java.util.ArrayList;

public class WaitNode extends Node {

	public WaitNode() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Robot robot) {
		robot.idleWait();
	}

	@Override
	public String toString() {
		return "wait;\n";
	}

}
