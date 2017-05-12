
public class LtNode extends ConditionNode implements RobotProgramNode {
	
	public LtNode(ExpressionNode e1, ExpressionNode e2) {
		super(e1, e2);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "lt(" + e1 + ", " + e2 + ")";
	}

	@Override
	public boolean evaluate(Robot robot) {
		return (e1.getValue(robot) < e2.getValue(robot));
	}

}