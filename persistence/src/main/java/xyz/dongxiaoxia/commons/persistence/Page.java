package xyz.dongxiaoxia.commons.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页
 *
 * @author dongxiaoxia
 * @create 2016-07-07 18:32
 */
public class Page<T> {

    private int pageNo = 1;//当前页码
    //    private int pageSize = Integer.valueOf(Global.getConfig("page.pageSize")); // 页面大小，设置为“-1”表示不进行分页（分页无效）
    private int pageSize = 20;// TODO: 2016/7/7
    private long count;//总记录数，设置为“-1”表示不查询总数
    private int first;//首页索引
    private int last;//尾页索引
    private int prev;//上一页索引
    private int next;//下一页索引

    private boolean firstPage;//是否是第一页
    private boolean lastPage;//是否是最后一页

    private int length = 8;//显示页面长度
    private int slider = 1;//前后显示页面长度

    private List<T> list = new ArrayList<T>();

    private String orderBy = "";//标准查询有效，示例：updateDate desc,name asc
    private String funcName = "page";//设置点击页码调用的js函数名称，默认为page,在一页有多个分页对象时使用。、
    private String funcParam = "";//函数的附加参数，第三个为参数值。

}
