
import org.jgrapht.graph.DefaultEdge;
public class test {

	
}
class pickedge extends DefaultEdge{
	private String label;
	public pickedge(String label) {
		this.label = label;
	}
	public String getLabel() {
		return this.label;
	}
	public String toString() {
		//return "("+getSource()+":"+getTarget()+")";
		return this.label;
	}
	
}
