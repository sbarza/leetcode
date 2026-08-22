package doubly_linked_list;

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 * int val;
 * TreeNode left;
 * TreeNode right;
 * TreeNode() {}
 * TreeNode(int val) { this.val = val; }
 * TreeNode(int val, TreeNode left, TreeNode right) {
 * this.val = val;
 * this.left = left;
 * this.right = right;
 * }
 * }
 */

/*
class Solution {

   public void flatten(TreeNode root) {

       if (root != null)
           visit(root);
   }

   private TreeNode visit(TreeNode node) {

       TreeNode left = node.left;
       TreeNode right = node.right;
       TreeNode tail = null;

       if (left != null) {
           tail = visit(left);
           node.right = left;
           node.left = null;
           tail.right = right;
       }

       if (right != null)
           return visit(right);

       if (left == null)
           return node;

       return tail;
   }
}
*/

public class FlattenBinaryTreeToLinkedList {

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public void flatten(TreeNode root) {

        if (root != null)
            visit(root);

    }

    private TreeNode visit(TreeNode node) {

        TreeNode left = node.left;
        TreeNode right = node.right;

        TreeNode leftTail = null;
        TreeNode rightTail = null;

        if (left != null) {
            leftTail = visit(left);

            node.right = left;
            node.left = null;

            leftTail.right = right;
        }

        if (right != null)
            rightTail = visit(right);

        return rightTail != null ? rightTail
                : leftTail != null ? leftTail
                : node;
    }
}
