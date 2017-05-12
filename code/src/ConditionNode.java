
public abstract class ConditionNode extends Node {
	
	public void execute(Robot robot) {
		// Does nothing
	}
	
	public abstract boolean evaluate(Robot robot);

}
