
public class SubNode extends OperatorNode {

	public SubNode(ExpressionNode e1, ExpressionNode e2) {
		super(e1, e2);
	}

	@Override
	public int getValue(Robot robot) {
		return e1.getValue(robot) - e2.getValue(robot);
	}

	@Override
	public String toString() {
		return e1 + " - " + e2;
	}

}
