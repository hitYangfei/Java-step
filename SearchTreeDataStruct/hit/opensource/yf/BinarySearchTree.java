package hit.opensource.yf;

public class BinarySearchTree<T> {
	private Entry<T> root;
	BinarySearchTree() {
		root = null ;
	}
	private Entry<T> minElement(Entry<T> element) {
		Entry<T> p = element;
		if (p == null)
			return null;
		while (p.left != null) {
			p = p.left;
		}
		return (p.left != null) ? p.left : p;
	}
	private Entry<T> maxElement(Entry<T> element) {
		Entry<T> p = element;
		if (p == null)
			return null;
		while (p.right != null) {
			p = p.right;
		}
		return (p.right != null) ? p.right : p;
	}
	private Entry<T> successor(Entry<T> element) {
		Entry<T> p = element;
		if (p == null)
			return null;
		if (p.right != null)
			return maxElement(p.right);
		Entry<T> q = p.parent;
		while (q !=null && p == q.right) {
			p = q;
			q = p.parent;
		}
		return q; 
	}
	public void test(){
		System.out.println("12345");
	}
	public boolean search(T element) {
		Entry<T> p = root;
		if (p == null)
			return false;
		@SuppressWarnings("unchecked")
		Comparable<? super T> k = (Comparable<? super T>) element;
		while (p != null) {
			int rtn = k.compareTo(p.element);
			if (rtn == 0)
				return true;
			else if(rtn > 0)
				p = p.right;
			else 
				p = p.left;
		}
		return false;
	}
	private Entry<T> searchForDelete(T element) {
		Entry<T> p = root;
		if (p == null)
			return null;
		@SuppressWarnings("unchecked")
		Comparable<? super T> k = (Comparable<? super T>) element;
		while (p != null) {
			int rtn = k.compareTo(p.element);
			if (rtn == 0)
				return p;
			else if(rtn > 0)
				p = p.right;
			else 
				p = p.left;
		}
		return null;
	}
	public void delete(T element) {
		Entry<T> p = searchForDelete(element);
		System.out.println(p.parent == root);
		if (p == null)
			return;
		Entry<T> q = p.parent;
		if (p.left == null && p.right == null) {
			if (p.parent == null)
				root = null;
			else if (p == p.parent.left) 
				p.parent.left = null;
			else
				p.parent.right = null;
			p = null;
		}
		else if (p.left == null || p.right == null) {
			if (p.parent == null) {
				root = (p.left == null ? p.right : p.left);
				root.parent = null;
			} else {
				Entry<T> tmp = (p.left == null ? p.right : p.left);
				tmp.parent = q;
				if (p == p.parent.left) 
					p.parent.left = tmp;
				else
					p.parent.right = tmp;
			}
		}
		else {
			Entry<T> successorNode = successor(p);
			Entry<T> leaf = (successorNode.left == null ? successorNode.right : successorNode.left);
			if (leaf != null) {
				leaf.parent = successorNode.parent;
			}
			if (successorNode == successorNode.parent.left) {
				successorNode.parent.left = leaf;
			} else {
				successorNode.parent.right = leaf;
			}
			p.element = successorNode.element;
      
		}
	}
	public void insert(T element) {
		Entry<T> p = root ;
		if (p == null) {
			root = new Entry<T>(element);
			return ;
		}
		@SuppressWarnings("unchecked")
		Comparable<? super T> k = (Comparable<? super T>) element;
		Entry<T> q = p;
		int rtn = 0;
		while (p != null) {
			q = p;
			rtn = k.compareTo(p.element);
			if (rtn > 0) {
				p = p.right;
			}
			else if(rtn < 0) {
				p = p.left;
			}
			else {
				System.out.println("can not insert the same element");
				return;
			}
		}
		Entry<T> newEntry = new Entry<T>(element, q);
		if (rtn > 0) {
			q.right = newEntry;
		} else {
			q.left = newEntry;
		}
	}
}
class Entry<T> {
	Entry<T> left;
	Entry<T> right;
	Entry<T> parent;
	T element;
	Entry(T element) {
		this.element = element;
		left = null;
		right = null;
		parent = null;
	}
	Entry(T element, Entry<T> p) {
		this.element = element;
		left = null;
		right = null;
		parent = p;
	}
	@Override
	public String toString() {
		return "[" + element + ",left = " + (left == null ? "null" : left.element) 
				+ ",right = " + (right == null ? "null" : right.element) + ",parent = "
				+ (parent == null ? "null" : parent.element) + "]";
	}
}