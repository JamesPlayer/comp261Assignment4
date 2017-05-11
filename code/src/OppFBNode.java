
public class OppFBNode extends SensorNode implements RobotProgramNode {

	@Override
	public int getValue(Robot robot) {
		return robot.getOpponentFB();
	}

	@Override
	public String toString() {
		return "get opponent front/back";
	}

}
