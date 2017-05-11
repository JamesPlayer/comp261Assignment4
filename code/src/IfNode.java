
public class IfNode extends Node implements RobotProgramNode {
	
	public ConditionNode condition = null;
	public BlockNode block = null;
	
	public IfNode(ConditionNode condition, BlockNode block) {
		this.condition = condition;
		this.block = block;
	}

	@Override
	public String toString() {
		return "if (" + condition + ") " + block;
	}

	@Override
	public void execute(Robot robot) {
		if (condition.evaluate(robot)) {
			block.execute(robot);
		}
	}
	

}