import java.util.ArrayList;

public class WaitNode extends Node {
	
	ExpressionNode expressionNode = null;

	public WaitNode() {}

	public WaitNode(ExpressionNode expressionNode) {
		this.expressionNode = expressionNode;
	}

	@Override
	public void execute(Robot robot) {
		
		int numWaits = 1;
		
		if (expressionNode != null) {
			numWaits = expressionNode.getValue(robot);
		}
		
		for (int i=0; i<numWaits; i++) {			
			robot.idleWait();
		}
	}

	@Override
	public String toString() {
		String suffix = (expressionNode != null) ? "(" + expressionNode + ")" : "";
		return "Wait" + suffix + ";";
	}

}
