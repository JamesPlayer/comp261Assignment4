
public abstract class ConditionNode extends Node {
	
	ExpressionNode e1 = null;
	ExpressionNode e2 = null;
	
	public ConditionNode(ExpressionNode e1, ExpressionNode e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	
	public void execute(Robot robot) {
		// Does nothing
	}
	
	public abstract boolean evaluate(Robot robot);

}
