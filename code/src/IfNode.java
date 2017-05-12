import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class IfNode extends Node implements RobotProgramNode {
	
	public ConditionNode condition = null;
	public BlockNode block = null;
	public BlockNode elseBlock = null;
	public Map<ConditionNode, BlockNode> elifs = new HashMap<ConditionNode, BlockNode>();
	
	public IfNode(ConditionNode condition, BlockNode block, Map<ConditionNode, BlockNode> elifs, BlockNode elseBlock) {
		this.condition = condition;
		this.block = block;
		this.elifs = elifs;
		this.elseBlock = elseBlock;
	}

	@Override
	public String toString() {
		
		String elifsString = "";
		
		for (Entry<ConditionNode, BlockNode> elif : elifs.entrySet()) {
			elifsString += "elif (" + elif.getKey() + ")" + elif.getValue();
		}
		
		
		String elseString = "";
		
		if (elseBlock != null) {
			elseString = " else " + elseBlock;
		}
		
		return "if (" + condition + ") " + block + elifsString + elseString;
	}

	@Override
	public void execute(Robot robot) {
		if (condition.evaluate(robot)) {
			block.execute(robot);
		} else if (!elifs.isEmpty()) {
			
			// Loop through all elifs, if we find a condition that is true then execute
			// and return (so we don't execute anything else)
			for (Entry<ConditionNode, BlockNode> elif : elifs.entrySet()) {
				if (elif.getKey().evaluate(robot)) {
					elif.getValue().execute(robot);
					return;
				}
			}			
		} 
		
		if (elseBlock != null) {
			elseBlock.execute(robot);
		}
	}
	

}