package tree.binary;

import java.util.*;

/**
 * 二叉树相关题目
 *
 * @author ZhangD
 * @since 2021-03-16
 */
public class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    public TreeNode(int val) {
        this.val = val;
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "val=" + val +
                ", left=" + left +
                ", right=" + right +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeNode treeNode = (TreeNode) o;
        return val == treeNode.val &&
                left.equals(treeNode.left) &&
                right.equals(treeNode.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(val, left, right);
    }
}

/**
 * 反转二叉树
 */
class InvertTree {
    /**
     * 前序遍历
     */
    public static TreeNode invertTree(TreeNode treeNode) {
        if (treeNode == null) {
            return null;
        }
        TreeNode temp = treeNode.left;
        treeNode.left = treeNode.right;
        treeNode.right = temp;
        invertTree(treeNode.left);
        invertTree(treeNode.right);
        return treeNode;
    }
}

/**
 * 判断二叉树是否对称
 */
class Symmetric {
    /**
     * 左前序和右前序结合,并拼接字符串序列化,进行比较.自己想出来的思路.耗时较多.
     * 类似的可以用中序序列化,求序列化之后是否为回文.(也可以判断是对称二叉树)
     */
    public static boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        TreeNode left = root.left;
        TreeNode right = root.right;
        StringBuilder sl = new StringBuilder();
        StringBuilder sr = new StringBuilder();
        sl = ergodicLeft(left, sl);
        sr = ergodicRight(right, sr);
        return Objects.equals(sl.toString(), sr.toString());
    }

    private static StringBuilder ergodicLeft(TreeNode treeNode, StringBuilder s) {
        if (treeNode == null) {
            return s.append(" ");
        }
        s = s.append(treeNode.val);
        StringBuilder s1 = ergodicLeft(treeNode.left, s);
        StringBuilder s2 = ergodicLeft(treeNode.right, s1);
        return s2;
    }

    private static StringBuilder ergodicRight(TreeNode treeNode, StringBuilder s) {
        if (treeNode == null) {
            return s.append(" ");
        }
        s = s.append(treeNode.val);
        StringBuilder s1 = ergodicRight(treeNode.right, s);
        StringBuilder s2 = ergodicRight(treeNode.left, s1);
        return s2;
    }

    public boolean isSymmetric2(TreeNode root) {
        //三目运算的简化版
        return root == null || recur(root.left, root.right);
    }

    //其实也是二叉树的一种遍历.两边往中间夹的感觉.而且是两个根节点的夹逼
    boolean recur(TreeNode L, TreeNode R) {
        if (L == null && R == null) {
            return true;
        }
        //这里还隐藏着一个条件.L==null 时 R!=null R同理
        if (L == null || R == null || L.val != R.val) {
            return false;
        }
        return recur(L.left, R.right) && recur(L.right, R.left);
    }
}

/**
 * 二叉树的层次遍历
 */
class LevelOrder {
    /**
     * 利用队列的特性.
     *
     * @param root
     * @return
     */
    public int[] levelOrder(TreeNode root) {
        if (root == null) {
            return new int[0];
        }
        Queue<TreeNode> queue = new LinkedList<TreeNode>() {{
            add(root);
        }};
        ArrayList<Integer> ans = new ArrayList<>();
        while (!queue.isEmpty()) {
            //1.可以利用队列的特性去除这个for循环.
            // int size = queue.size();
            // for (int i = 0; i < size; i++) {
            //     TreeNode node = queue.poll();
            //     ans.add(node.val);
            //     if (node.left != null) {
            //         queue.add(node.left);
            //     }
            //     if (node.right != null) {
            //         queue.add(node.right);
            //     }
            // }
            //2.充分利用队列的特性.
            TreeNode node = queue.poll();
            ans.add(node.val);
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
        int[] res = new int[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            res[i] = ans.get(i);
        }
        return res;
    }
}

/**
 * 树的深度.
 */
class TreeDeep {

    //递归深度优先搜索做法.
    public static int treeDeep(TreeNode treeNode) {
        if (treeNode == null) {
            return 0;
        }
        //写出来之后可以转换.
        int leftDeep = treeDeep(treeNode.left);
        int rightDeep = treeDeep(treeNode.right);
        //只有在左右都遍历之后,才能知道根节点的最大深度,所以是后序遍历.
        int deep = Math.max(leftDeep, rightDeep) + 1;
        return deep;
    }

    //层次遍历广度优先搜索做法
    public static int treeDeep2(TreeNode treeNode) {
        if (treeNode == null) {
            return 0;
        }
        List<TreeNode> queue = new LinkedList() {{
            add(treeNode);
        }};
        int res = 0;

        while (!queue.isEmpty()) {
            List<TreeNode> temp = new ArrayList<>();
            res++;
            for (TreeNode node : queue) {
                if (node.left != null) {
                    temp.add(node.left);
                }
                if (node.right != null) {
                    temp.add(node.right);
                }
            }
            //列表替换,丢弃上面的列表,指向新的列表.这样就使用了多个队列了,不过好像也问题不大.
            queue = temp;
        }
        return res;
    }

    //层次遍历广度优先搜索做法,重复使用一个列表.
    public static int treeDeep3(TreeNode treeNode) {
        if (treeNode == null) {
            return 0;
        }
        LinkedList<TreeNode> queue = new LinkedList() {{
            add(treeNode);
        }};
        int res = 0;
        List<TreeNode> temp = new ArrayList<>();
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode root = queue.poll();
                temp.add(root);
            }
            //当前层级已poll,++
            res++;
            //进入下一层级准备工作
            for (TreeNode node : temp) {
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            temp.clear();
        }
        return res;
    }

    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(3);
        TreeNode rootLeft = treeNode.left = new TreeNode(9);
        TreeNode rootRight = treeNode.right = new TreeNode(20);
        rootRight.left = new TreeNode(15);
        rootRight.right = new TreeNode(7);
        System.out.println(treeDeep3(treeNode));
    }
}

/**
 * 1.首先判断是分治,分析题意写逻辑(最为复杂)
 * 2.然后有左子树,右子树,会列出树状递归.分析应该在树状递归何处(前,中,后)进行剩余的逻辑.有时候在哪处理都是一样的.
 * todo:迭代做法
 */
class BuildTree {
    static Map<Integer, Integer> inMap = new HashMap<>();
    static Map<Integer, Integer> preMap = new HashMap<>();

    /**
     * 通过中序和前序重建树.
     */
    public static TreeNode buildTree(int[] preorder, int[] inorder) {
        for (int i = 0; i < inorder.length; i++) {
            inMap.put(inorder[i], i);
        }
        for (int i = 0; i < preorder.length; i++) {
            preMap.put(i, preorder[i]);
        }
        return tree(0, 0, inorder.length - 1);
    }

    private static TreeNode tree(int root, int left, int right) {
        if (left > right) {
            return null;
        }
        //获取根节点在中序遍历数组的位置.
        Integer i = inMap.get(preMap.get(root));
        //这实际上也是一棵树,只不过这个树的根节点做的事情比较复杂.
        TreeNode leftNode = tree(root + 1, left, i - 1);
        TreeNode rootNode = new TreeNode(preMap.get(root));
        TreeNode rightNode = tree(root + 1 + i - left, i + 1, right);
        rootNode.left = leftNode;
        rootNode.right = rightNode;
        return rootNode;
    }

    public static void main(String[] args) {
        // int[] preorder = {3, 9, 20, 15, 7};
        // int[] inorder = {9, 3, 15, 20, 7};
        // System.out.println(buildTree(preorder, inorder));
        int[] preorder = {8, 4, 5, 2, 9, 6, 7, 3, 1};
        int[] inorder = {8, 4, 2, 5, 1, 9, 6, 3, 7};
        System.out.println(buildTree2(preorder, inorder));
    }

    /**
     * 通过中序和后序重建树.
     */
    public static TreeNode buildTree2(int[] preorder, int[] inorder) {
        for (int i = 0; i < inorder.length; i++) {
            inMap.put(inorder[i], i);
        }
        for (int i = 0; i < preorder.length; i++) {
            preMap.put(i, preorder[i]);
        }
        return tree2(preorder.length - 1, 0, inorder.length - 1);
    }

    private static TreeNode tree2(int root, int left, int right) {
        if (left > right) {
            return null;
        }
        //获取当前根节点中序遍历数组位置
        Integer i = inMap.get(preMap.get(root));
        TreeNode leftNode = tree2(root - right + i - 1, left, i - 1);
        TreeNode rightNode = tree2(root - 1, i + 1, right);
        TreeNode rootNode = new TreeNode(preMap.get(root));
        rootNode.left = leftNode;
        rootNode.right = rightNode;
        return rootNode;
    }
}

class IsBalanced {
    public static boolean isBalanced(TreeNode root) {
        return deep(root) != -1;
    }

    private static int deep(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftDeep = deep(root.left);
        if (leftDeep == -1) {
            return -1;
        }
        int rightDeep = deep(root.right);
        if (rightDeep == -1) {
            return -1;
        }
        int deep = Math.max(leftDeep, rightDeep) + 1;
        if (Math.abs(leftDeep - rightDeep) <= 1) {
            return deep;
        }
        return -1;
    }
}

/**
 * 序列化和反序列化二叉树(层次遍历),其实用前中后序遍历也可以
 */
class Codec {
    //todo:以后再看,先用前序和后序遍历的方式来.
    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        if (root == null) {
            return null;
        }
        Queue<TreeNode> queue = new LinkedList<TreeNode>() {{
            add(root);
        }};
        List<Integer> nodes = new LinkedList<>();
        int deep = deep(root);
        while (deep != 0) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode poll = queue.poll();
                if (poll == null) {
                    nodes.add(null);
                    queue.offer(null);
                    queue.offer(null);
                } else {
                    nodes.add(poll.val);
                    queue.offer(poll.left);
                    queue.offer(poll.right);
                }
            }
            deep--;
        }
        return Arrays.toString(nodes.toArray());
    }

    // // Decodes your encoded data to tree.
    // public TreeNode deserialize(String data) {
    //     String nodeString = data.substring(1, data.length() - 1);
    //     String[] split = nodeString.split(",");
    //     LinkedList<Integer> queue = new LinkedList();
    //     for (String s : split) {
    //         queue.offer(Integer.valueOf(s));
    //     }
    //     int deep = log(2, data.length() + 1);
    //     Map<Integer,List> map = new HashMap<>();
    //     for (int i = 0; i < deep; i++) {
    //         LinkedList<Integer> subList = new LinkedList<>(queue.subList((int) Math.pow(2, i) - 1, (int) Math.pow(2, i + 1) - 1));
    //         map.put(i,subList);
    //     }
    //     for (int i = 0; i < map.entrySet().size(); i++) {
    //         List list = map.get(i);
    //         List listNext = map.get(i + 1);
    //         for (int i1 = 0; i1 < list.size(); i1++) {
    //             list.get(i1)
    //         }
    //     }


    private int deep(TreeNode root) {
        return root == null ? 0 : Math.max(deep(root.left), deep(root.right)) + 1;
    }

    public static int log(int basement, int n) {
        return (int) (Math.log(n) / Math.log(basement));
    }


    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(3);
        TreeNode rootLeft = treeNode.left = new TreeNode(9);
        TreeNode rootRight = treeNode.right = new TreeNode(20);
        rootRight.left = new TreeNode(15);
        rootRight.right = new TreeNode(7);
        Codec codec = new Codec();
        String serialize = codec.serialize1(treeNode);
        TreeNode treeNode1 = codec.deserialize1(serialize);
        System.out.println(serialize);
        System.out.println(treeNode1);
    }

    public String serialize1(TreeNode root) {
        helper(root);
        StringBuilder builder = new StringBuilder();
        for (Integer node : listSe) {
            builder.append(node);
            builder.append(",");
        }
        return builder.toString();
    }

    List<Integer> listSe = new LinkedList<>();

    private void helper(TreeNode root) {
        if (root == null) {
            listSe.add(null);
            return;
        }
        listSe.add(root.val);
        helper(root.left);
        helper(root.right);
    }

    public TreeNode deserialize1(String data) {
        String[] split = data.substring(0, data.length() - 1).split(",");
        LinkedList<String> listDese = new LinkedList(Arrays.asList(split));
        return deHelper(listDese);
    }

    private TreeNode deHelper(LinkedList<String> listDesc) {
        String poll = listDesc.poll();
        if (poll.equals("null")) {
            return null;
        }
        TreeNode treeNode = new TreeNode(Integer.parseInt(poll));
        TreeNode left = deHelper(listDesc);
        TreeNode right = deHelper(listDesc);
        treeNode.left = left;
        treeNode.right = right;
        return treeNode;
    }
}

/**
 * 二叉树的最近祖先
 */
class LowestCommonAncestor {
    TreeNode treeNode;
    Map<Integer, TreeNode> fatherMap = new HashMap<>();

    /**
     * @param root 二叉树
     * @param p    子二叉树p
     * @param q    子二叉树q
     * @return 最深祖先二叉树
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        helper(root, p, q);
        return treeNode;
    }

    /**
     * 递归(后序遍历) 可以用外部变量进行标记结果,不一定在返回值中,返回值更多的是为了递归本身服务的.
     */
    private boolean helper(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return false;
        }
        boolean left = helper(root.left, p, q);
        boolean right = helper(root.right, p, q);
        //判断自己是否是节点之一
        boolean self = root.val == p.val || root.val == q.val;
        if (left && right || (!right && self) || (!left && self)) {
            treeNode = root;
        }
        //自己是节点也返回true,此时递归还没有上到上一层,还在这一层.
        return left || right || self;
    }

    /**
     * 迭代(记录二叉树的所有父节点.和森林化的那个类似
     */
    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        findFather(root);
        List<Integer> pFatherTrace = new LinkedList<>();
        while (p != null) {
            pFatherTrace.add(p.val);
            p = fatherMap.get(p.val);
        }

        while (q != null) {
            if (pFatherTrace.contains(q.val)) {
                return q;
            }
            q = fatherMap.get(q.val);
        }
        return null;
    }

    private void findFather(TreeNode root) {
        if (root == null) {
            return;
        }
        findFather(root.left);
        findFather(root.right);
        if (root.left != null) {
            fatherMap.put(root.left.val, root);
        }
        if (root.right != null) {
            fatherMap.put(root.right.val, root);
        }
    }


}
