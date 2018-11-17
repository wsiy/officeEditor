var Document = {
    /**
     * 查询文档信息
     * @param keyword 查询关键字
     * @param $http $http对象
     * @param success 成功时回调的函数
     * @param error 失败时回调的函数
     */
    queryByType: function (typeName, $http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/getDocumentsByType/'+typeName,
            headers:{
                Accept:'application/json',
            }
        }).then(success,error);
    },
    querycoreByType: function (typeName, $http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/getCoreDocumentsByType/'+typeName,
            headers:{
                Accept:'application/json',
            }
        }).then(success,error);
    },
    /**
     * 查询全部文档信息
     * @param $http $http对象
     * @param success 成功时回调的函数
     * @param error 失败时回调的函数
     */
    queryAll: function ($http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/getDocuments/',
            headers:{
                Accept:'application/json',
            }
        }).then(success, error);
    },
    
    /*
     * 查询文档分页信息
     */
    queryPart: function (totalList,$http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/findPartDocument/'+totalList,
            headers:{
                Accept:'application/json',
            }
        }).then(success, error);
    },
//    初始Document
    initQuery: function ($http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/findInitDocument/',
            headers:{
                Accept:'application/json',
            }
        }).then(success, error);
    },
    
//  初始CoreDocument 
    initCore: function ($http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/findInitCoreDocument/',
            headers:{
                Accept:'application/json',
            }
        }).then(success, error);
    },
    
    querycoreAll: function ($http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/getCoreDocuments/',
            headers:{
                Accept:'application/json',
            }
        }).then(success, error);
    },
    querybyName: function (name,$http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/getDocumentsByName/'+name,
            headers:{
                Accept:'application/json',
            }
        }).then(success, error);
    },
    querybyDate: function (date,$http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/getDocumentsByCreateDate/'+date,
            headers:{
                Accept:'application/json',
            }
        }).then(success, error);
    },
    querycorebyDate: function (begin_date,end_date,$http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/getCoreDocumentsByDate/'+begin_date+'/'+end_date,
            headers:{
                Accept:'application/json',
            }
        }).then(success, error);
    },
    querycorebyName: function (name,$http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/getCoreDocumentsByName/'+name,
            headers:{
                Accept:'application/json',
            }
        }).then(success, error);
    },
    querycorebyclass: function (name,$http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/getCoreDocumentsByClassfication/'+name,
            headers:{
                Accept:'application/json',
            }
        }).then(success, error);
    },
    querycorebyexpress: function (name,$http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/getCoreDocumentsByExpression/'+name,
            headers:{
                Accept:'application/json',
            }
        }).then(success, error);
    },
    /**
     * 删除文档信息
     * @param id
     * @param $http $http对象
     * @param success 成功时回调的函数
     * @param error 失败时回调的函数
     */
    remove: function (id, $http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/deleteDocumentByID/'+id,
        }).then(success, error);
    },
    
    
    removecore: function (id, $http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/deleteCoreDocumentByID/'+id,
            async: false,
        }).then(success, error);
    },
    /*
     * 查看文档
     */
    
    viewOnline: function (name, $http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/viewOnline/'+name,
        }).then(success, error);
    },
    
    /**
     * 收藏文档
     */
    collect: function (id, $http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/collectDocumentByID/'+id,
        }).then(success, error);
    },
    //按时间排序
    orderByTime:function (bool,$http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/getDocumentsByTimeOrder/'+bool,
        }).then(success, error);
    },
    
    ordercoreByTime:function (bool,$http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/getCoreDocumentsByTimeOrder/'+bool,
        }).then(success, error);
    },
    create_save: function (data, $http, success, error) {
        $http({
            method:'POST',
            url:'./CXF/REST/Domain/getDocumentsByType/',//待定
            data : data,
        dataType:"json",
           async: false, //请求是否异步，默认为异步，这也是ajax重要特性
            headers:{
                Accept:'application/json',
            }
        }).then(success,error);
    },
    initall: function ($http, success, error) {
		$http({
            method:'GET',
            url:'./CXF/REST/Domain/getDocuments/',
            headers:{
                Accept:'application/json',
            }
        }).then(success,error);
	},
	changefilename: function (first,second,third,filename,author, type,$http, success, error) {
		if(first<second&&first<third){
			//1.根据名字得到文种、作者——getDocumentsByName (String name)
			console.info(filename);
			$http({
	            method:'GET',
	            url:'./CXF/REST/Domain/getDocumentsByNameInCore/'+filename,
	            headers:{
	                Accept:'application/json',
	            }
	        }).then(success,error);
		}else if(first<second&&third<second){
			//2.根据文种、名字得到作者——getDocumentsByNameType(String name, String type)
			$http({
	            method:'GET',
	            url:'./CXF/REST/Domain/getDocumentsByNameType/'+filename+'/'+type,
	            headers:{
	                Accept:'application/json',
	            }
	        }).then(success,error);
		}else if(first<third&&second<third){
			//3.根据作者、名字得到文种——getDocumentsByNameAuthor(String name,String author)
			$http({
	            method:'GET',
	            url:'./CXF/REST/Domain/getDocumentsByNameAuthor/'+filename+'/'+author,
	            headers:{
	                Accept:'application/json',
	            }
	        }).then(success,error);
		}else{

		}			
	},
	changeauthor: function (first,second,third,filename,author, type,$http, success, error) {
		if(second<first&&second<third){
			console.info("changeauthor");
			//1.根据作者得到文种、名字——getDocumentsByAuthor(String author)
			console.info(filename);
			$http({
	            method:'GET',
	            url:'./CXF/REST/Domain/getDocumentsByAuthor/'+author,
	            headers:{
	                Accept:'application/json',
	            }
	        }).then(success,error);
		}else if(second<first&&third<first){
			//2.根据文种、作者得到名字——getDocumentsByTypeAuthor(String type,String author)
			$http({
	            method:'GET',
	            url:'./CXF/REST/Domain/getDocumentsByTypeAuthor/'+type+'/'+author,
	            headers:{
	                Accept:'application/json',
	            }
	        }).then(success,error);
		}else if(second<third&&first<third){
			//3.根据作者、名字得到文种——getDocumentsByNameAuthor(String name,String author)
			$http({
	            method:'GET',
	            url:'./CXF/REST/Domain/getDocumentsByNameAuthor/'+filename+'/'+author,
	            headers:{
	                Accept:'application/json',
	            }
	        }).then(success,error);
		}else{
			
		}			
	},
	changetype: function (first,second,third,filename,author, type,$http, success, error) {
		if(third<first&&third<second){
				//1.根据文种得到名字、作者——getDocumentsByType(String type)
				console.info(filename);
				$http({
		            method:'GET',
		            url:'./CXF/REST/Domain/getDocumentsByTypeInCore/'+type,
		            headers:{
		                Accept:'application/json',
		            }
		        }).then(success,error);
			}else if(third<first&&second<first){
				//2.根据文种、作者得到名字——getDocumentsByTypeAuthor(String type,String author)
				$http({
		            method:'GET',
		            url:'./CXF/REST/Domain/getDocumentsByTypeAuthor/'+type+'/'+author,
		            headers:{
		                Accept:'application/json',
		            }
		        }).then(success,error);
			
		}else if(third<second&&first<second){
			//3.根据文种、名字得到作者——getDocumentsByNameType(String name, String type)
			$http({
	            method:'GET',
	            url:'./CXF/REST/Domain/getDocumentsByNameType/'+filename+'/'+type,
	            headers:{
	                Accept:'application/json',
	            }
	        }).then(success,error);
		}else{
			
		}
	},
	getfilename: function (author, type,$http, success, error) {
		if(author==""){
			if(type==""){
				 //1.得到名字——getDocuments
				$http({
		            method:'GET',
		            url:'./CXF/REST/Domain/getDocuments/',
		            headers:{
		                Accept:'application/json',
		            }
		        }).then(success,error);
			}else{
				//2.根据文种得到名字、作者——getDocumentsByType(String type)
				$http({
		            method:'GET',
		            url:'./CXF/REST/Domain/getDocumentsByTypeInCore/'+type,
		            headers:{
		                Accept:'application/json',
		            }
		        }).then(success,error);
			}
		}else{
            if(type==""){
            	//3.根据作者得到文种、名字——getDocumentsByAuthor(String author)
    			$http({
    	            method:'GET',
    	            url:'./CXF/REST/Domain/getDocumentsByAuthor/'+author,
    	            headers:{
    	                Accept:'application/json',
    	            }
    	        }).then(success,error);
			}else{
				//4.根据文种、作者得到名字——getDocumentsByTypeAuthor(String type,String author)
				$http({
		            method:'GET',
		            url:'./CXF/REST/Domain/getDocumentsByTypeAuthor/'+type+'/'+author,
		            headers:{
		                Accept:'application/json',
		            }
		        }).then(success,error);
			}
		}
       
	
		
		
    },
	/**
	 * 新增保存
	 */
	create_save: function (data, $http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/addCoreDocument/'+JSON.stringify(data),
           /* data : data,
  	        headers: {  
  	    	        'Content-Type': 'application/x-www-form-urlencoded'  
  	    	    },  //当post方式提交的时候需要加上这段来解决后台获取不到数据的问题
  	    	    transformRequest: function ( data ) {  
  	    	        var str = '';  
  	    	        for( var i in data ) {  
  	    	            str += i + '=' + data[i] + '&';  
  	    	        }  
  	    	        return str.substring(0,str.length-1);  
  	    	    }//解析json对象的自定义函数
*/		        }).then(success,error);
    },
    
    /**
	 * 新增保存
	 */
	create_paper_save: function (data, $http, success, error) {
        $http({
            method:'GET',
            //url
            url:'./CXF/REST/Domain/addDocument/'+JSON.stringify(data),
	        }).then(success,error);
    },
    
    /**
     * 为给定文档（已经上传到存储路径）的文档建文本索引
     * */
    add_index_single: function (fileName, $http, success, error) {
        $http({
            method:'GET', 
            url:'./CXF/REST/RichText/updateSingleRichTextDocument/'+fileName,
	    }).then(success,error);
    },
    
    /**
     * 为给定文档（已经上传到存储路径）的文档删除文本索引
     * */
    remove_index_single: function (fileName, $http, success, error) {
        $http({
            method:'GET', 
            url:'./CXF/REST/RichText/deleteSingleRichTextDocument/'+fileName,
	    }).then(success,error);
    },
};