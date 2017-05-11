
public class BarrelFBNode extends SensorNode implements RobotProgramNode {

	@Override
	public int getValue(Robot robot) {
		return robot.getBarrelLR(0);
	}

	@Override
	public String toString() {
		return "get barrel front/back";
	}

}
