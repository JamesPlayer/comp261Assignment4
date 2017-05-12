
public class OrNode extends BoolConditionNode {

	public OrNode(ConditionNode c1, ConditionNode c2) {
		super(c1, c2);
	}

	@Override
	public boolean evaluate(Robot robot) {
		return c1.evaluate(robot) || c2.evaluate(robot);
	}

	@Override
	public String toString() {
		return c1 + " OR " + c2;
	}

}
