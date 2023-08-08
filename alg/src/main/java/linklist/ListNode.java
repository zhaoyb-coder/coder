package linklist;

/**
 * @author zhaoyubo
 * @title ListNode
 * @description 链表节点
 * @create 2023/7/27 13:33
 **/
public class ListNode {
    public Integer val;
    public ListNode next;

    public static  ListNode of(int val,ListNode next){
        ListNode node = new ListNode();
        node.val = val;
        node.next = next;
        return node;
    }
}
