
public class LtNode extends ConditionNode implements RobotProgramNode {
	
	public LtNode(SensorNode sensorVal, NumberNode number) {
		super(sensorVal, number);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "lt(" + sensorNode + ", " + numberNode + ")";
	}

	@Override
	public boolean evaluate(Robot robot) {
		return (sensorNode.getValue(robot) < numberNode.getValue());
	}

}