package linklist;

import java.util.regex.Pattern;

/**
 * @author zhaoyubo
 * @title 移除链表元素
 * @description
 * 题意：删除链表中等于给定值 val 的所有节点。
 * 示例 1： 输入：head = [1,2,6,3,4,5,6], val = 6 输出：[1,2,3,4,5]
 * 示例 2： 输入：head = [], val = 1 输出：[]
 * 示例 3： 输入：head = [7,7,7,7], val = 7 输出：[]
 * @create 2023/7/27 12:45
 **/
public class 移除链表元素 {
    public static void main(String[] args) {
        ListNode head = ListNode.of(1,ListNode.of(2,ListNode.of(3,ListNode.of(4,null))));
        removeListNode(head,2);
    }

    public static ListNode removeListNode(ListNode head,int val){
         //虚拟头结点
        ListNode dummy = ListNode.of(-1,head);
        if(head == null){
            return head;
        }
        ListNode pre = dummy;
        ListNode cur = head;
        while (cur != null){
            if(cur.val.equals(val)){
                //当前节点需要去除
                pre.next = cur.next;
            }else{
                pre = cur;
            }
            cur = cur.next;
        }
        return dummy.next;
    }
}
