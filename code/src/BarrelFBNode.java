
public class BarrelFBNode extends SensorNode implements RobotProgramNode {
	
	ExpressionNode exp = null;

	public BarrelFBNode(ExpressionNode exp) {
		this.exp = exp;
	}

	@Override
	public int getValue(Robot robot) {
		
		if (exp != null && exp.getValue(robot) > 0) {
			return robot.getBarrelFB(exp.getValue(robot)-1);
		}
		
		return robot.getBarrelFB(0);
	}

	@Override
	public String toString() {
		return "get barrel front/back";
	}

}
