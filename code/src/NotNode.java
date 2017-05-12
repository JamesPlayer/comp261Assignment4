
public class NotNode extends BoolConditionNode {

	public NotNode(ConditionNode c1) {
		super(c1, null);
	}

	@Override
	public boolean evaluate(Robot robot) {
		return !c1.evaluate(robot);
	}

	@Override
	public String toString() {
		return "NOT " + c1;
	}

}
