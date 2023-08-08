package tree;

/**
 * @author zhaoyubo
 * @title 翻转二叉树
 * @description 前序遍历进行翻转二叉树
 * @create 2023/7/28 15:02
 **/
public class 翻转二叉树 {
    public static void main(String[] args) {
        //构建一颗树
        Tree t = Tree.of("1",Tree.of("2",Tree.of("4",null,null),Tree.of("5",null,null)),Tree.of("3",Tree.of("6",null,null),Tree.of("7",null,null)));
        Tree result = mth01(t);
        System.out.println(result);
    }

    public static Tree mth01(Tree tree){
        if(tree == null){
            return null;
        }
        //前序遍历进行翻转二叉树
        swap(tree);
        mth01(tree.left);
        mth01(tree.right);
        return tree;
    }

    public static void swap(Tree tree){
        Tree temp = tree.left;
        tree.left = tree.right;
        tree.right = temp;
    }

}
