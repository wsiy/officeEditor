package main.java.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PageBeanCoreDocument")
public class PageBeanCoreDocument{
    //已知数据
    private int pageNum;    //跳转的页码。
    private int pageSize;    //每页显示的数据条数。
    private int totalRecord;    //总的记录条数。查询数据库得到的数据
    
    //需要计算得来
    private int totalPage;    //总页数，通过totalRecord和pageSize计算可以得来
    //开始索引，也就是我们在数据库中要从第几行数据开始拿，有了startIndex和endIndex，
    //就知道了limit语句的两个数据，就能获得每页需要显示的数据了
    private int startIndex;     
    private int endIndex;       
        
	//该筛选条件下总的list集合中
    private List<CoreDocument> totalList;
    //将每页要显示的数据放在list集合中
    private List<CoreDocument> perList;
    
    //分页显示的页数,比如在页面上显示1，2，3，4，5页，start就为1，end就为5，这个也是算过来的
    private int start;
    private int end;
    
    //通过pageNum，pageSize，totalRecord计算得来tatalPage和startIndex
    //构造方法中将pageNum，pageSize，totalRecord获得
    
     public PageBeanCoreDocument(List<CoreDocument> list,int pageNum) {
    	 System.out.println("开始");
        this.pageNum = pageNum;
        this.totalList = list;
        this.pageSize = 8;
        this.totalRecord = list.size();
        
        //totalPage 总页数
        if(totalRecord%pageSize==0){
            //说明整除，正好每页显示pageSize条数据，没有多余一页要显示少于pageSize条数据的
            this.totalPage = totalRecord / pageSize;
        }else{
            //不整除，就要在加一页，来显示多余的数据。
            this.totalPage = totalRecord / pageSize +1;
        }
        //开始索引
        this.startIndex = (pageNum-1)*pageSize ;
        //显示5页，这里自己可以设置，想显示几页就自己通过下面算法修改
        this.start = 1;
        this.end = 5;
        //显示页数的算法

        if(totalPage <=5){
            //总页数都小于5，那么end就为总页数的值了。
            this.end = this.totalPage;
        }else{
            //总页数大于5，那么就要根据当前是第几页，来判断start和end为多少了，
            this.start = pageNum - 2;
            this.end = pageNum + 2;
            
            if(start < 0){
                //比如当前页是第1页，或者第2页，那么就不如和这个规则，
                this.start = 1;
                this.end = 5;
            }
            if(end > this.totalPage){
                //比如当前页是倒数第2页或者最后一页，也同样不符合上面这个规则
                this.end = totalPage;
                this.start = end - 5;
            }
        }
        /*
         * endIndex
         */
       if(this.pageNum == this.totalPage) {
        	this.endIndex = totalRecord - 1;
        }else {
        	this.endIndex = startIndex + this.pageSize - 1;
        }
       /*
        * 得到返回页面的list
        */
       List<CoreDocument> listTemp = new ArrayList<CoreDocument>();
		for(int i = startIndex; i <= endIndex; i++ ) {
			listTemp.add(this.totalList.get(i));
		}
		this.perList = listTemp;
    }

     /*
      * 默认构造器
      */
	public PageBeanCoreDocument() {
	    this.pageNum = 1;
	    this.pageSize = 5;
	    
        this.totalList = null;
        this.perList = null;
        
        this.totalRecord =0;
        this.totalPage =1;
        
        this.startIndex =0 ;
        this.endIndex =0 ;
        
        this.start = 1;
        this.end = 5;
	}
	
	/*
     * get、set方法
     */
    @XmlElement
    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    
    @XmlElement
    public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	
    @XmlElement
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    @XmlElement
    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    @XmlElement
    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @XmlElement
    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    @XmlElement
    public List<CoreDocument> getTotalList() {
		return totalList;
	}

	public void setTotalList(List<CoreDocument> totalList) {
		this.totalList = totalList;
	}
	@XmlElement
	public List<CoreDocument> getPerList() {
		return perList;
	}

	public void setPerList(List<CoreDocument> perList) {
		this.perList = perList;
	}

	@XmlElement
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @XmlElement
    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}