
public class AssignmentNode extends Node {
	
	public String varName = null;
	public ExpressionNode exp = null;

	public AssignmentNode(String varName, ExpressionNode exp) {
		this.varName = varName;
		this.exp = exp;
	}

	@Override
	public void execute(Robot robot) {
		VariableNode.vars.put(varName, exp.getValue(robot));
	}

	@Override
	public String toString() {
		return varName + " = " + exp;
	}

}
