
public class WhileNode extends Node implements RobotProgramNode {
	
	public ConditionNode condition = null;
	public BlockNode block = null;
	
	public WhileNode(ConditionNode condition, BlockNode block) {
		this.condition = condition;
		this.block = block;
	}

	@Override
	public String toString() {
		return "while (" + condition + ") " + block;
	}

	@Override
	public void execute(Robot robot) {
		while (condition.evaluate(robot)) {
			block.execute(robot);
		}
	}
	

}