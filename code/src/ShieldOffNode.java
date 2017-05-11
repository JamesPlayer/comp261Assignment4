import java.util.ArrayList;

public class ShieldOffNode extends Node {

	public ShieldOffNode() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Robot robot) {
		robot.setShield(false);
	}

	@Override
	public String toString() {
		return "shield off;\n";
	}

}
