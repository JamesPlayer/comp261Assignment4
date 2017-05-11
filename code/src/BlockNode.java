import java.util.ArrayList;

public class BlockNode extends Node {
	
	public ArrayList<RobotProgramNode> children = new ArrayList<RobotProgramNode>();

	public BlockNode() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		String childString = "";
		
		for (RobotProgramNode node : children) {
			childString += "\n\t" + node.toString();
		}
		
		return "{" + childString + "\n}";
	}

}
