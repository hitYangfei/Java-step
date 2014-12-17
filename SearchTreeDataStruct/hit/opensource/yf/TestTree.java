package hit.opensource.yf;

public class TestTree {
	public static void main(String[] argvs) {
		BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
		tree.insert(5);
		tree.insert(6);
		tree.insert(3);
		tree.insert(9);
		tree.insert(7);
		tree.insert(8);
		tree.insert(2);
		tree.insert(1);
		tree.insert(4);
		
		tree.delete(6);
		
		
	}
}
