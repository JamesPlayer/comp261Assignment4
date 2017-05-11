
public class FuelLeftNode extends SensorNode implements RobotProgramNode {

	@Override
	public int getValue(Robot robot) {
		return robot.getFuel();
	}

	@Override
	public String toString() {
		return "fuel remaining";
	}

}
