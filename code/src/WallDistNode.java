
public class WallDistNode extends SensorNode implements RobotProgramNode {

	@Override
	public int getValue(Robot robot) {
		return robot.getDistanceToWall();
	}

	@Override
	public String toString() {
		return "get distance to wall";
	}

}
