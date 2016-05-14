import xyz.dongxiaoxia.commons.logging.Log;
import xyz.dongxiaoxia.commons.logging.Logger;

/**
 * @author dongxiaoxia
 * @create 2016-05-14 1:36
 */
public class LogTest {
    public static void main(String[] args) {
        Logger.error(new NullPointerException());
        Log log = Logger.getLogger(LogTest.class);
        log.warn("saf");
    }
}
