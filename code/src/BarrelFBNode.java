
public class BarrelFBNode extends SensorNode implements RobotProgramNode {
	
	ExpressionNode exp = null;

	public BarrelFBNode(ExpressionNode exp) {
		this.exp = exp;
	}

	@Override
	public int getValue(Robot robot) {
		
		if (exp != null) {
			return robot.getBarrelFB(exp.getValue(robot));
		}
		
		return robot.getBarrelFB(0);
	}

	@Override
	public String toString() {
		return "get barrel front/back";
	}

}
