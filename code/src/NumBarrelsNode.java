
public class NumBarrelsNode extends SensorNode implements RobotProgramNode {

	@Override
	public int getValue(Robot robot) {
		return robot.numBarrels();
	}

	@Override
	public String toString() {
		return "get num barrels";
	}

}
