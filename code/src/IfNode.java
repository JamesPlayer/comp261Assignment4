
public class IfNode extends Node implements RobotProgramNode {
	
	public ConditionNode condition = null;
	public BlockNode block = null;
	public BlockNode elseBlock = null;
	
	public IfNode(ConditionNode condition, BlockNode block) {
		this.condition = condition;
		this.block = block;
	}

	public IfNode(ConditionNode condition, BlockNode block, BlockNode elseBlock) {
		this(condition, block);
		this.elseBlock = elseBlock;
	}

	@Override
	public String toString() {
		
		String elseString = "";
		
		if (elseBlock != null) {
			elseString = " else " + elseBlock;
		}
		
		return "if (" + condition + ") " + block + elseString;
	}

	@Override
	public void execute(Robot robot) {
		if (condition.evaluate(robot)) {
			block.execute(robot);
		} else if (elseBlock != null) {
			elseBlock.execute(robot);
		}
	}
	

}