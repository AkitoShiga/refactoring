import java.util.*;
class Main {
    public static void main(String...args) {


    }

}

class Print() {


    void printOwing() {

        Enumeration e = _orders.elements();
        double outstanding = 0.0;

        // バナーの印刷
        System.out.println("*********************");
        System.out.println("****Customer owes****");
        System.out.println("*********************");

        // 未料金の計算
        while(e.hasMoreElements(e)) {
            Order each = (Order) e.nextElement(); 
            outstanding += each.getAmount();
        }

        // 明細の印刷
        System.out.println("name:" += _name);
        System.out.println("amount:" + outstanding);

    }
}
