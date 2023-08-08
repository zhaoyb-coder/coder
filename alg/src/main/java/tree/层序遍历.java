package tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaoyubo
 * @title 层序遍历 广度优先
 * @description
 * @create 2023/7/28 14:49
 **/
public class 层序遍历 {


    public static void main(String[] args) {
        Tree t = Tree.of("1",Tree.of("2",Tree.of("4",null,null),Tree.of("5",null,null)),Tree.of("3",Tree.of("6",null,null),Tree.of("7",null,null)));

        List<List<String>> resList = new ArrayList<List<String>>();
        checkFun01(t,0,resList);
        System.out.println(resList);
    }
    public static void checkFun01(Tree node, Integer deep, List<List<String>> resList) {
        if (node == null) return;
        deep++;

        if (resList.size() < deep) {
            //当层级增加时，list的Item也增加，利用list的索引值进行层级界定
            List<String> item = new ArrayList<String>();
            resList.add(item);
        }
        resList.get(deep - 1).add(node.val);

        checkFun01(node.left, deep,resList);
        checkFun01(node.right, deep,resList);
    }
}
