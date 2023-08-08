package tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaoyubo
 * @title 前中后序遍历 深度优先
 * @description
 * @create 2023/7/28 14:35
 **/
public class 前中后序遍历 {

    public static void main(String[] args) {
        //构建一颗树
        Tree t = Tree.of("1",Tree.of("2",Tree.of("4",null,null),Tree.of("5",null,null)),Tree.of("3",Tree.of("6",null,null),Tree.of("7",null,null)));
        mth01(t);
    }

    /**
    * @description 前序遍历：中左右的顺序
    * @param tree
    * @return void
    * @author zhaoyubo
    * @time 2023/7/28 14:38
    **/
    public static List<String> mth01(Tree tree){
        List<String> result = new ArrayList<String>();
//        mth02(tree,result); // 1245367
//        mth03(tree,result); // 4251637
        mth04(tree,result); // 4526731
        return result;
    }
    
    /**
    * @description 前序遍历：中左右的顺序
    * @param tree 
     * @param result
    * @return void
    * @author zhaoyubo
    * @time 2023/7/28 14:44
    **/
    public static void mth02(Tree tree,List<String> result){
        if(tree == null){
            return;
        }
        result.add(tree.val);
        mth02(tree.left,result);
        mth02(tree.right,result);
    }

    /**
     * @description 中序遍历：左中右
     * @param tree
     * @param result
     * @return void
     * @author zhaoyubo
     * @time 2023/7/28 14:44
     **/
    public static void mth03(Tree tree,List<String> result){
        if(tree == null){
            return;
        }
        mth03(tree.left,result);
        result.add(tree.val);
        mth03(tree.right,result);
    }
    
    /**
    * @description 后序遍历：左右中
    * @param tree 
     * @param result
    * @return void
    * @author zhaoyubo
    * @time 2023/7/28 14:44
    **/
    public static void mth04(Tree tree,List<String> result){
        if(tree == null){
            return;
        }
        mth04(tree.left,result);
        mth04(tree.right,result);
        result.add(tree.val);
    }
}
