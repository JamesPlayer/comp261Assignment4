
public abstract class NumericConditionNode extends ConditionNode {
	
	ExpressionNode e1 = null;
	ExpressionNode e2 = null;
	
	public NumericConditionNode(ExpressionNode e1, ExpressionNode e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

}
