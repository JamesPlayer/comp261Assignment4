
public class EqNode extends ConditionNode implements RobotProgramNode {
	
	public EqNode(SensorNode sensorVal, NumberNode number) {
		super(sensorVal, number);
	}

	@Override
	public String toString() {
		return "eq(" + sensorNode + ", " + numberNode + ")";
	}

	@Override
	public boolean evaluate(Robot robot) {
		return (sensorNode.getValue(robot) == numberNode.getValue());
	}

}