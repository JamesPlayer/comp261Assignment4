import java.util.ArrayList;

public class TakeFuelNode extends Node {

	public TakeFuelNode() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Robot robot) {
		robot.takeFuel();
	}

	@Override
	public String toString() {
		return "take fuel;\n";
	}

}
