package com.epic.cefts;

import com.epic.cefts.bean.ListenerSession;

public class JavaTestMain {


    public static void main(String[] args) {

        // non static method can call only by creating class object
//        JavaTestMain javaTestMain = new JavaTestMain();
//        javaTestMain.test();

//        static method can call within static or no static methods
//        and all static variables are class level cannot be defined in the method level

//        test2();

        // primitive types and string(immutable) pass by value - pass copy of the value
        String x = "abc";
        foo(x);
        System.out.println("x :" + x);
        // java object works as pass by reference - pass copy of the reference
        ListenerSession s = new ListenerSession();
        s.setSessionId("123456");
        foo2(s);
        System.out.println("ListenerSession :" + s.getSessionId());

    }

    public void test() {
        String test;
        System.out.println("test git command");
        test2();
    }

    public static void test2() {
        String test;
        System.out.println("sdgsdf");
//        test();
    }

    public static void foo(String x) {
        x = "123";
    }

    public static void foo2(ListenerSession s) {
        s.setSessionId("abcdefg");
    }
}