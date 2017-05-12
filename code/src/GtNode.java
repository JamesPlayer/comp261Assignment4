
public class GtNode extends ConditionNode implements RobotProgramNode {

	public GtNode(ExpressionNode e1, ExpressionNode e2) {
		super(e1, e2);
	}

	@Override
	public String toString() {
		return "gt(" + e1 + ", " + e2 + ")";
	}

	@Override
	public boolean evaluate(Robot robot) {
		return (e1.getValue(robot) > e2.getValue(robot));
	}

}