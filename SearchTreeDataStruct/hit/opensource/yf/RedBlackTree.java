package hit.opensource.yf;

public class RedBlackTree {
  private static final boolean RED   = false;
  private static final boolean BLACK = true;
  TreeNode root;

  static final class TreeNode {
    int value;
    TreeNode left;
    TreeNode right;
    TreeNode parent;
    boolean color;

    TreeNode(int value, TreeNode p) {
      this.value = value;
      this.parent = p;
      this.color = BLACK;
      this.left = null;
      this.right = null;
    }
  }
  static TreeNode successor(TreeNode t) {
    if (t == null)
      return null;
    else if (t.right != null) {
      TreeNode p = t.right;
      while (p.left != null)
        p = p.left;
      return p;
    } else {
      TreeNode p = t.parent;
      TreeNode q = t;
      while (p != null && q == p.right) {
        q = p;
        p = p.parent;
      }
      return p;
    }
  }
  private static boolean colorOf(TreeNode p) {
    return (p == null ? BLACK : p.color);
  }

  private static TreeNode parentOf(TreeNode p) {
    return (p == null ? null: p.parent);
  }

  private static void setColor(TreeNode p, boolean c) {
    if (p != null)
      p.color = c;
  }

  private static TreeNode leftOf(TreeNode p) {
    return (p == null) ? null: p.left;
  }

  private static TreeNode rightOf(TreeNode p) {
    return (p == null) ? null: p.right;
  }
  private void rotateLeft(TreeNode p) {
    if (p == null)
      return ;
    TreeNode r = p.right;
    p.right = r.left;
    if (r.left != null)
      r.left.parent = p;
    r.parent = p.parent;
    if (p.parent == null) {
      root = r;
    } else if (p == p.parent.left) {
      p.parent.left = r;
    } else {
      p.parent.right = r;
    }
    r.left = p;
    p.parent = r;
  }
  private void rotateRight(TreeNode p) {
    if (p == null)
      return;
    TreeNode l = p.left;
    p.left = l.right;
    if (l.right != null)
      l.right.parent = p;
    l.parent = p.parent;
    if (p.parent == null) {
      root = l;
    } else if (p == p.parent.left) {
      p.parent.left = l;
    } else {
      p.parent.right = l;
    }
    l.right = p;
    p.parent = l;
  }
  public void insert(int value) {
    if (root == null) {
      root = new TreeNode(value, null);
      return;
    }
    TreeNode t = root;
    TreeNode parent = t;
    do {
      parent = t;
      if (value > t.value)
        t = t.right;
      else
        t = t.left;
    } while (t != null);
    TreeNode node = new TreeNode(value, parent);
    if (value > parent.value) {
      parent.right = node;
    } else {
      parent.left = node;
    }
    fixAfterInsertion(node);
  }
  private void fixAfterInsertion(TreeNode x) {
    x.color = RED;
    while( x != null && x != root && colorOf(parentOf(x)) == RED) {
      if (x == leftOf(parentOf(x))) {
        TreeNode y = rightOf(parentOf(x));
        if (colorOf(y) == RED) {
          setColor(y, BLACK);
          setColor(parentOf(x), BLACK);
          setColor(parentOf(parentOf(x)), RED);
          x = parentOf(parentOf(x));
        } else {
          if (parentOf(x) == rightOf(parentOf(parentOf(x)))) {
            x = parentOf(x);
            rotateLeft(x);
          }
          setColor(parentOf(x), BLACK);
          setColor(parentOf(parentOf(x)), RED);
          rotateRight(parentOf(parentOf(x)));

        }

      } else {
        TreeNode y = leftOf(parentOf(x));
        if (colorOf(y) == RED) {
          setColor(y, BLACK);
          setColor(parentOf(x), BLACK);
          setColor(parentOf(parentOf(x)), RED);
          x = parentOf(parentOf(x));
        } else {
          if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
            x = parentOf(x);
            rotateRight(x);
          }
          setColor(parentOf(x), BLACK);
          setColor(parentOf(parentOf(x)), RED);
          rotateLeft(parentOf(parentOf(x)));
        }
      }
    }
    root.color = BLACK;
  }
  private TreeNode search(int value) {
    if (root == null)
      return null;
    TreeNode p = root;
    do {
      if (p.value == value)
        return p;
      else if(value > p.value)
        p = p.right;
      else
        p = p.left;
    } while (p !=null );
    return null;
  }
  public void delete(int value) {
    if (root == null) {
      return;
    }
    TreeNode p = search(value);
    if (p.left != null && p.right != null) {
      TreeNode s = successor(p);
      p.value = s.value;
      p = s;
    }

    TreeNode replacement = (p.left != null ? p.left : p.right);
    if (replacement != null) {
      replacement.parent = p.parent;
      if (replacement = p.parent.left)
        p.parent.left = replacement;
      else
        p.parent.right = replacement;
      p.left = p.right = p.parent = null;
      if (p.color == BLACK)
        fixAfterDeletion(replacement);
    } else if (p.parent == null) {
      root = null;
    } else {
      if (p.color == BLACK)
        fixAfterDeletion(p);
      if (p.parent != null) {
        if (p == p.parent.left)
          p.parent.left = null;
        else if (p == p.parent.right)
          p.parent.right = null;
        p.parent = null;
      }
    }
  }
  public void fixAfterDeletion(TreeNode x) {
    while (x != root && colorOf(x) == BLACK) {
      if (x == leftOf(parentOf(x))) {
        TreeNode w = rightOf(parentOf(x));
        if (colorOf(w) == RED) {
          setColor(w, BLACK);
          setColor(parentOf(x), RED);
          rotateLeft(parentOf(x));
          w = rightOf(parentOf(x));
        }

        if (colorOf(leftOf(w)) == BLACK && colorOf(rightOf(w)) == BLACK) {
          setColor(w, RED);
          x = parentOf(x);
        } else {
          if (colorOf(rightOf(w)) == BLACK) {
            setColor(w, RED);
            setColor(leftOf(w), BLACK);
            rotateRighr(w);
            w = rightOf(parentOf(x));
          }
          setColor(w, colorOf(parentOf(x)));
          setColor(parentOf(x), BLACK);
          setColor(rightOf(w), BLACK);
          rotateLeft(parentOf(x));
          x = root;
        }
      } else {
        Entry<K,V> w = leftOf(parentOf(x));

        if (colorOf(w) == RED) {
          setColor(w, BLACK);
          setColor(parentOf(x), RED);
          rotateRight(parentOf(x));
          w = leftOf(parentOf(x));
        }

        if (colorOf(rightOf(w)) == BLACK &&
            colorOf(leftOf(w)) == BLACK) {
          setColor(w, RED);
          x = parentOf(x);
        } else {
          if (colorOf(leftOf(w)) == BLACK) {
            setColor(rightOf(w), BLACK);
            setColor(w, RED);
            rotateLeft(w);
            w = leftOf(parentOf(x));
          }
          setColor(w, colorOf(parentOf(x)));
          setColor(parentOf(x), BLACK);
          setColor(leftOf(w), BLACK);
          rotateRight(parentOf(x));
          x = root;
        }

      }
    }
  }
}
