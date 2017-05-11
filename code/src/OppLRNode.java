
public class OppLRNode extends SensorNode implements RobotProgramNode {

	@Override
	public int getValue(Robot robot) {
		return robot.getOpponentLR();
	}

	@Override
	public String toString() {
		return "get opponent left/right";
	}

}
