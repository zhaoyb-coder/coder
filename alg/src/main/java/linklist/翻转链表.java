package linklist;

/**
 * @author zhaoyubo
 * @title 翻转链表
 * @description <TODO description class purpose>
 * @create 2023/7/28 15:07
 **/
public class 翻转链表 {
    public static void main(String[] args) {
        ListNode head = ListNode.of(1,ListNode.of(2,ListNode.of(3,ListNode.of(4,null))));
        ListNode result = mth01(head);
        System.out.println(result);
    }

    public static ListNode mth01(ListNode head){
        //虚拟头结点
        ListNode var1 = null;
        //快慢指针
        ListNode var2 = head;
        ListNode temp = null;
        while(var2 != null){
            temp = var2.next;
            var2.next = var1;
            var1 = var2;
            var2 = temp;
        }
        return var1;
    }
}
