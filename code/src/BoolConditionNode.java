
public abstract class BoolConditionNode extends ConditionNode {

	public ConditionNode c1 = null;
	public ConditionNode c2 = null;
	
	public BoolConditionNode(ConditionNode c1, ConditionNode c2) {
		this.c1 = c1;
		this.c2 = c2;
	}

}
