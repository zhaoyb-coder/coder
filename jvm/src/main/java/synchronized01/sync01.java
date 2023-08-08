package synchronized01;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author zhaoyubo
 * @title sync01
 * @description 打印对象头信息
 * @create 2023/7/27 9:20
 **/
public class sync01 {
    public static void main(String[] args) {
        A a = new A();
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
    }
}
