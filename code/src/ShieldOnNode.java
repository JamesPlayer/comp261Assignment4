import java.util.ArrayList;

public class ShieldOnNode extends Node {

	public ShieldOnNode() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Robot robot) {
		robot.setShield(true);
	}

	@Override
	public String toString() {
		return "shield on;";
	}

}
