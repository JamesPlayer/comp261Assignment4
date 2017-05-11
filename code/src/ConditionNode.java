
public abstract class ConditionNode extends Node {
	
	SensorNode sensorNode = null;
	NumberNode numberNode = null;
	
	public ConditionNode(SensorNode sensorNode, NumberNode numberNode) {
		this.sensorNode = sensorNode;
		this.numberNode = numberNode;
	}

	public ConditionNode() {
		// TODO Auto-generated constructor stub
	}
	
	public void execute(Robot robot) {
		// Does nothing
	}
	
	public abstract boolean evaluate(Robot robot);

}
