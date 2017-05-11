import java.util.ArrayList;

public class ProgramNode extends Node {
	
	public ArrayList<RobotProgramNode> children = new ArrayList<RobotProgramNode>();

	public ProgramNode() {
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
			childString += "\n" + node.toString();
		}
		
		return childString;
	}

}
