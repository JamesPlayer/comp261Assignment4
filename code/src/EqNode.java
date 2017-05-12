
public class EqNode extends ConditionNode implements RobotProgramNode {

	public EqNode(ExpressionNode e1, ExpressionNode e2) {
		super(e1, e2);
	}

	@Override
	public String toString() {
		return "eq(" + e1 + ", " + e2 + ")";
	}

	@Override
	public boolean evaluate(Robot robot) {
		return (e1.getValue(robot) == e2.getValue(robot));
	}

}