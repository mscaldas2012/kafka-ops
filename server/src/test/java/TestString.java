import org.junit.jupiter.api.Test;

/**
 * @Created - 3/26/20
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
public class TestString {

    @Test
    public void testConcatenatingStringsIntegers() {
        int number = 8;
        String str = "blabhalbhal " + number + ".";
        System.out.println("str = " + str);
        MyObject myObj = new MyObject();
        String str2 = "blablabla " + myObj.getNumber() + ".";
        System.out.println("str2 = " + str2);

    }
}


class MyObject {
    private int aNumber = 9;

    public int getNumber() {
        return aNumber;
    }
}