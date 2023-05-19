import com.bosspvp.api.utils.MathUtils;
import org.junit.jupiter.api.Test;

public class TestMath {

    //I've got the following result:
    //Fast sin: 3
    //Default sin: 167
    @Test
    public void testSin(){
        //to initialize static variable
        MathUtils.fastSin(22);


        long timeStart = System.currentTimeMillis();
        for(int i=0;i<10000000;i++) {
            MathUtils.fastSin(0.54363);
        }
        System.out.println("Fast sin:"+(System.currentTimeMillis()-timeStart));

        timeStart = System.currentTimeMillis();
        for(int i=0;i<10000000;i++) {
            Math.sin(0.54363);
        }
        System.out.println("Default sin:"+(System.currentTimeMillis()-timeStart));

    }
}
