
public class AssignmentNode extends Node {
	
	public VariableNode varNode = null;
	public ExpressionNode exp = null;

	public AssignmentNode(VariableNode varNode, ExpressionNode exp) {
		this.varNode = varNode;
		this.exp = exp;
	}

	@Override
	public void execute(Robot robot) {
		VariableNode.vars.put(varNode.toString(), exp.getValue(robot));
	}

	@Override
	public String toString() {
		return varNode + " = " + exp + ";";
	}

}
