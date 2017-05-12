
public abstract class OperatorNode extends ExpressionNode {

	public ExpressionNode e1 = null;
	public ExpressionNode e2 = null;
	
	public OperatorNode(ExpressionNode e1, ExpressionNode e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

}
