import pkg1.ClassAnnotation;
import pkg1.MyAnnotation;

import javax.print.attribute.HashDocAttributeSet;

public class MyAnnoTest {

    @MyAnnotation
    private String myTestField;

    @MyAnnotation
    private String test1;

    @MyAnnotation
    private static String sss;

    public static void main(String[] args) {
        MyAnnoTest test = new MyAnnoTest();
        test.getTest1();
    }
    
}
