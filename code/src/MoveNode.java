import java.util.ArrayList;

public class MoveNode extends Node {
	
	ExpressionNode expressionNode = null;

	public MoveNode() {}

	public MoveNode(ExpressionNode expressionNode) {
		this.expressionNode = expressionNode;
	}

	@Override
	public void execute(Robot robot) {
		
		int numMoves = 1;
		
		if (expressionNode != null) {
			numMoves = expressionNode.getValue(robot);
		}
		
		for (int i=0; i<numMoves; i++) {			
			robot.move();
		}
	}

	@Override
	public String toString() {
		String suffix = (expressionNode != null) ? "(" + expressionNode + ")" : "";
		return "move" + suffix + ";";
	}

}
