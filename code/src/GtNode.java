
public class GtNode extends ConditionNode implements RobotProgramNode {

	public GtNode(SensorNode sensorVal, NumberNode number) {
		super(sensorVal, number);
	}

	@Override
	public String toString() {
		return "gt(" + sensorNode + ", " + numberNode + ")";
	}

	@Override
	public boolean evaluate(Robot robot) {
		return (sensorNode.getValue(robot) > numberNode.getValue(robot));
	}

}