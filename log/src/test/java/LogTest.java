import xyz.dongxiaoxia.commons.logging.Logger;

/**
 * @author dongxiaoxia
 * @create 2016-05-14 0:58
 */
public class LogTest {
    public static void main(String[] args) {
       try {
           throw new NullPointerException("sfasdfadsf");
       }catch (Exception e){
           Logger.error(e.getMessage(),e);
       }
    }
}
