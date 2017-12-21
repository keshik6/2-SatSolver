


public class Node implements Comparable<Node>{
	private String varName;
	private int val;
	private boolean bool;
	
	public Node(int x, boolean t) {
		this.varName = "x" + x;
		this.val = x;
		this.bool = t;
	}

	
	
	public String toString() {
		String out = varName + "set to :" + bool;
		return out;
	}


	@Override
	public int compareTo(Node o) {
		if (this.val > o.val) {
			return 1;
		}
		else if (this.val<o.val) {
			return -1;
		}
		return 0;
	}



	@Override
	public boolean equals(Object obj) {
		Node o = (Node)obj;
		if (o.val == this.val ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
}
