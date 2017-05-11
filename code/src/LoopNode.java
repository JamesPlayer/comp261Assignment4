import java.util.ArrayList;

public class LoopNode extends Node {
	
	public RobotProgramNode block = null;

	public LoopNode(RobotProgramNode block) {
		this.block = block;
	}

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return "loop " + block;
	}

}
