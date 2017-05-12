
public class BarrelLRNode extends SensorNode implements RobotProgramNode {
	
	ExpressionNode exp = null;

	public BarrelLRNode(ExpressionNode exp) {
		this.exp = exp;
	}

	@Override
	public int getValue(Robot robot) {
		
		if (exp != null) {
			return robot.getBarrelLR(exp.getValue(robot));
		}
		
		return robot.getBarrelLR(0);
	}

	@Override
	public String toString() {
		return "get barrel left/right";
	}

}
