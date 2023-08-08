package tree;

/**
 * @author zhaoyubo
 * @title Tree
 * @description 二叉树基本结构
 * @create 2023/7/28 14:35
 **/
public class Tree {
    public String val;

    public Tree left;

    public Tree right;

    public static Tree of(String val,Tree left,Tree right){
        Tree t = new Tree();
        t.val = val;
        t.left = left;
        t.right = right;
        return t;
    }
}
