//核心文章
function majorPaperCtrl($scope, $http) {
	/*
	 * 根据时间顺序
	 */
	$scope.changeDirection = function() {
		$scope.pbc = {};
        if ($(".orderByTime").hasClass("my-btn-blue2")) {
            $(".orderByTime").removeClass("my-btn-blue2");
            $(".up").addClass("noDisplay");
            $(".down").removeClass("noDisplay");
            bool=false;
    		Document.ordercoreByTime(bool,$http, function (response) {
    			$scope.pbc = response['data'];
    			
    		}, function () {});
        } else {
            $(".orderByTime").addClass("my-btn-blue2");
            $(".down").addClass("noDisplay");
            $(".up").removeClass("noDisplay");
            bool=true;
    		Document.ordercoreByTime(bool,$http, function (response) {
    			$scope.pbc = response['data'];
    		}, function () {});
        }
    }

	
	 //初次加载时显示全部Document第一页
    $scope.initPage = function(){
    	$scope.pbc = {};
		Document.initCore($http, function (response) {
			$scope.pbc = response['data'];
			if($scope.pbc.totalRecord == 1){
				$scope.changeCoreFormat();
			}
		}, function () {})
		
    };
    
	$scope.initPage();//打开页面显示第一页文档
	
	
	// 首页
    $scope.firstPage = function(){
    	if($scope.pbc.totalRecord != 0){
	    	$scope.pbc.perList.splice(0,$scope.pbc.endIndex-$scope.pbc.startIndex+1);
	    	$scope.pbc.startIndex = 0 ;
	    	$scope.pbc.pageNum = 1;
	    	
	    	 if($scope.pbc.totalPage <= $scope.pbc.pageNum ) {
	    		 $scope.pbc.endIndex = $scope.pbc.totalRecord - 1;
	         }else {
	        	 $scope.pbc.endIndex = $scope.pbc.startIndex + $scope.pbc.pageSize - 1;
	         }
	    	 
	    	for (var i=$scope.pbc.startIndex;i<=$scope.pbc.endIndex;i++)
	    	{
	    		$scope.pbc.perList.push($scope.pbc.totalList[i]);
	    		if(typeof($scope.pbc.perList[i-$scope.pbc.startIndex])=="undefined"){ 
	    			$scope.pbc.perList.splice(i-$scope.pbc.startIndex,1)
	    		} 
	    	} 	
    	}
    };
    
    // 上一页
    $scope.previousPage = function(){
    	if($scope.pbc.totalRecord != 0){
    	$scope.pbc.perList.splice(0,$scope.pbc.endIndex-$scope.pbc.startIndex+1);
    	
    	 if($scope.pbc.pageNum == 1) {
    		 $scope.pbc.startIndex = 0 ;
    		 if($scope.pbc.totalPage <= $scope.pbc.pageNum ) {
        		 $scope.pbc.endIndex = $scope.pbc.totalRecord - 1;
             }else {
            	 $scope.pbc.endIndex = $scope.pbc.startIndex + $scope.pbc.pageSize - 1;
             }
         }else {
        	 $scope.pbc.pageNum = $scope.pbc.pageNum-1;
         	 $scope.pbc.startIndex = ($scope.pbc.pageNum-1)*$scope.pbc.pageSize ;
        	 $scope.pbc.endIndex = $scope.pbc.startIndex + $scope.pbc.pageSize - 1;
         }
    	 
    	for (var i=$scope.pbc.startIndex;i<=$scope.pbc.endIndex;i++)
    	{
    		$scope.pbc.perList.push($scope.pbc.totalList[i]);
    		if(typeof($scope.pbc.perList[i-$scope.pbc.startIndex])=="undefined"){ 
    			$scope.pbc.perList.splice(i-$scope.pbc.startIndex,1)
    		} 
    	} 	
    	}
    };
    
    // 下一页
    $scope.nextPage = function(){
    	if($scope.pbc.totalRecord != 0){
    	if($scope.pbc.pageNum < $scope.pbc.totalPage){//当前页是尾页，则下一页不变
    		$scope.pbc.perList.splice(0,$scope.pbc.endIndex-$scope.pbc.startIndex+1);
    		$scope.pbc.pageNum = $scope.pbc.pageNum+1;
    		$scope.pbc.startIndex = ($scope.pbc.pageNum-1)*$scope.pbc.pageSize ;
    		if($scope.pbc.pageNum == $scope.pbc.totalPage) {//当前页是倒数第二页，则下一页是尾页
    			$scope.pbc.endIndex = $scope.pbc.totalRecord - 1;
            }else{
            	$scope.pbc.endIndex = $scope.pbc.startIndex + $scope.pbc.pageSize - 1;
            }
    		
    		for (var i=$scope.pbc.startIndex;i<=$scope.pbc.endIndex;i++){
        		$scope.pbc.perList.push($scope.pbc.totalList[i]);
        		if(typeof($scope.pbc.perList[i-$scope.pbc.startIndex])=="undefined"){ 
        			$scope.pbc.perList.splice(i-$scope.pbc.startIndex,1)
        		} 
        	} 	
    	}
    	}
    	
    };

    // 尾页
    $scope.lastPage = function(){
    	if($scope.pbc.totalRecord != 0){
    		$scope.pbc.perList.splice(0,$scope.pbc.endIndex-$scope.pbc.startIndex+1);
        	$scope.pbc.startIndex = ($scope.pbc.totalPage-1)*$scope.pbc.pageSize ;
    		$scope.pbc.endIndex = $scope.pbc.totalRecord - 1;
            $scope.pbc.pageNum = $scope.pbc.totalPage;
            for (var i=$scope.pbc.startIndex;i<=$scope.pbc.endIndex;i++)
        	{
        		$scope.pbc.perList.push($scope.pbc.totalList[i]);
        		if(typeof($scope.pbc.perList[i-$scope.pbc.startIndex])=="undefined"){ 
        			$scope.pbc.perList.splice(i-$scope.pbc.startIndex,1)
        		} 
        	} 	
    	}
    };

	
	$scope.querycoreAll = function () {
		$scope.documents = {};
		Document.querycoreAll($http, function (response) {
			$scope.documents = response['data'];
		}, function () {})
	};

	/*
	 * 对一条数据格式从对象转为数组
	 */
	$scope.changeCoreFormat = function () {
		perCoreDoc=new Object();
		//核心文章
		perCoreDoc.ID = $scope.pbc.perList.ID;
		perCoreDoc.analyse = $scope.pbc.perList.analyse;
		perCoreDoc.characteristic = $scope.pbc.perList.characteristic;
		perCoreDoc.createDate = $scope.pbc.perList.createDate;
		perCoreDoc.content = $scope.pbc.perList.content;
		//公文
		perDoc=new Object();
		perDoc.author = $scope.pbc.perList.document.author;
		perDoc.ID = $scope.pbc.perList.document.ID;
		perDoc.collected = $scope.pbc.perList.document.collected;
		perDoc.createDate = $scope.pbc.perList.document.createDate;
		perDoc.fileName = $scope.pbc.perList.document.fileName;
		perDoc.inCore = $scope.pbc.perList.document.inCore;
		perDoc.sameNum = $scope.pbc.perList.document.sameNum;
		//公文类型
		type=new Object();
		type.ID=$scope.pbc.perList.document.documentType.ID;
		type.name=$scope.pbc.perList.document.documentType.name;
		perDoc.documentType = type;
		perCoreDoc.document = perDoc;
		//classfication
		classf=new Object();
		classf.ID=$scope.pbc.perList.classfication.ID;
		classf.name=$scope.pbc.perList.classfication.name;
		perCoreDoc.classfication = classf;
		//expression
		express=new Object();
		express.ID=$scope.pbc.perList.expression.ID;
		express.name=$scope.pbc.perList.expression.name;
		perCoreDoc.expression = express;
		
		$scope.pbc.perList =new Array(perCoreDoc);
	};
    /*
     * 查询文档分页信息
     */
	$scope.queryPart = function (pageNum,pageSize,$http, success, error) {
        $http({
            method:'GET',
            url:'./CXF/REST/Domain/PageBeanCoreDocument/'+pageNum+pageSize,
            headers:{
                Accept:'application/json',
            }
        }).then(success, error);
    },
    
	//按类型查询核心文章
	$scope.querycoreAllReport = function (keyword) {
    	$scope.pbc = {};
		Document.querycoreByType(keyword, $http, function (response) {
			$scope.pbc = response['data'];
			if($scope.pbc.totalRecord == 1){
				$scope.changeCoreFormat();
			}
		}, function () {});
	};
	//按名字查询
	$scope.querycorebyName = function () {
		$scope.pbc = {};
		keyword=$("#corefilesearch").val();
		Document.querycorebyName(keyword, $http, function (response) {
			$scope.pbc = response['data'];
			if($scope.pbc.totalRecord == 1){
				$scope.changeCoreFormat();
			}
		}, function () {});
	};
	//按时间范围查询
	$scope.querycorebyDate = function () {
		begin_date=$("#begin_date").val();
		end_date=$("#end_date").val();
		$scope.pbc = {};
		Document.querycorebyDate(begin_date,end_date, $http, function (response) {
			$scope.pbc = response['data'];
			if($scope.pbc.totalRecord == 1){
				$scope.changeCoreFormat();
			}
		}, function () {});
	};
	//按分类查询
	$scope.querycorebyclass = function (keyword) {
		$scope.pbc = {};
		Document.querycorebyclass(keyword, $http, function (response) {
			$scope.pbc = response['data'];
			if($scope.pbc.totalRecord == 1){
				$scope.changeCoreFormat();
			}
		}, function () {});
	};
	//按表述查询
	$scope.querycorebyexpress = function (keyword) {
		$scope.pbc = {};
		Document.querycorebyexpress(keyword, $http, function (response) {
			$scope.pbc = response['data'];
			if($scope.pbc.totalRecord == 1){
				$scope.changeCoreFormat();
			}
		}, function () {});
	};
	
	//在线查看某个文档
    $scope.viewOnline = function (name) {
		Document.viewOnline(name, $http, function () {
			document.getElementById('viewContainer').innerHTML =  response['data'];
		}, function () {
			alert("查看出错");
        })
    };
	//删除某个文档信息
    $scope.delete = function (index, id) {
    	var msg = "您真的确定要删除吗？\n\n请确认！";
    	if (window.confirm(msg)==true){
    		Document.removecore(id, $http, function () {
    			var current = $scope.pbc.startIndex+index+1;
    	        alert("文档"+current+"已删除");
    	        $scope.initPage();//打开页面显示第一页文档
            }, function () {
             alert("删除出错");
            })
    	}else{
    		return false;
    	}
    };
    //多选删除
    $scope.multidelete = function () {
    	var msg = "您真的确定要删除吗？\n\n请确认！";
    	if (window.confirm(msg)==true){
	    	var i,j;
	    	var count=document.getElementById("count").name;
	    	var temp=[];
	    	var t=0;
	    	for(i=0;i<count;i++){	
	    		if ($("#multi_"+i).is(':checked')){
	    			temp[t]=i;
	    			t++;          
	    		};
	    	}
	    	var location=-1;
	    	var ct=0;
	    	var k=0;
	    	for(j=0;j<temp.length;j++){
	    		alert("要删除文档"+document.getElementById("multi_"+temp[j]).name);
	        	index=document.getElementById("multi_"+temp[j]).value;
	    		var id=document.getElementById("multi_"+temp[j]).name;
	    		if(j>0){index=index-ct;}
	    		ct++;
	    		alert("已删除"+document.getElementById("multi_"+temp[j]).name);
				Document.removecore(id, $http, function () {			
		        }, function () {
		        	alert("删除出错");
		        });
/*				setTimeout(function () {
					alert("hhhh");
	            }, 1000);*/
	    	}
	    	alert("已全部删除");
	    	$scope.initPage();//打开页面显示第一页文档
    	}else{
			return false;
		}
    };
    
    
    function createOptions(parent, ops) { 
		  var o = new Option("请选择","0");
		  parent.appendChild(o);
		  for(var key in ops)
			 {
				 var o = new Option(ops[key],key);
		         parent.appendChild(o);
			      
			 }
	}
    var startvalue=10;
	var first=startvalue;//文件名,数字越大优先级越小
	var second=startvalue;//作者
	var third=startvalue;//文种
	
	$scope.initall = function() {
		first=startvalue;//文件名,数字越大优先级越小
		second=startvalue;//作者
		third=startvalue;//文种
		var filename="create_filename";
		var author="create_author";
		var type="create_type";
		var classi="create_class";
		var expre="create_expre";
		var jsonfilename={};
		var jsonauthor={};
		var jsontype={};
		var jsonfilename1={};
		var jsonauthor1={};
		var jsontype1={};
		var jsonclassi={
			"1":"提纲",
			"2":"观点",
			"3":"其他"
		}
		var jsonexpre={
				"1":"经验体会",
				"2":"重要意义",
				"3":"问题不足",
				"4":"认识举措",
				"5":"后勤保障"
		};
		 $("#"+filename).empty(); 
		 $("#"+author).empty();
		 $("#"+type).empty();
		 $("#"+classi).empty(); 
		 $("#"+expre).empty();
		 
	   Document.initall($http, function (response) {
			
			var documents = response['data'];
			var count1="1";
			var count2="1";
			var count3="1";
			//console.info(count+documents+type);
			 for (var i = 0; i < documents.length; i++) {
			        //result[i]表示获得第i个json对象即JSONObject
			        //result[i]通过.字段名称即可获得指定字段的值
				   if(!jsonfilename1.hasOwnProperty(documents[i].fileName)){
					   jsonfilename[count1]=documents[i].fileName;
					   count1++;
					   jsonfilename1[documents[i].fileName]="0";
				   }
				   if(!jsonauthor1.hasOwnProperty(documents[i].author)){
					   jsonauthor[count2]=documents[i].author;
					   count2++;
					   jsonauthor1[documents[i].author]="0";
				   }
				   if(!jsontype1.hasOwnProperty(documents[i].documentType.name)){
					   jsontype[count3]=documents[i].documentType.name;
					   count3++;
					   jsontype1[documents[i].documentType.name]="0";
				   }
				  console.info(i+documents[i].fileName+expre);					
			 }
			createOptions(document.getElementById(filename), jsonfilename);
			createOptions(document.getElementById(author), jsonauthor);
			createOptions(document.getElementById(type), jsontype);
			createOptions(document.getElementById(classi), jsonclassi);
			createOptions(document.getElementById(expre), jsonexpre);
		}, function () {})
		
    };
    
	$scope.changefilename = function() {		
		var filename=$("#create_filename").find("option:selected").text();
		var author=$("#create_author").find("option:selected").text();
		var type=$("#create_type").find("option:selected").text();
		if(first==startvalue){
			 if(second==startvalue&&third==startvalue){
				 first=1;
			 }else if(second!=startvalue&&third!=startvalue){
				 first=3;
			 }else{
				 first=2;
			 }
		 }
		Document.changefilename(first,second,third,filename,author,type,$http, function (response) {
			var jsonauthor={};
			var jsontype={};
			var jsonauthor1={};
			var jsontype1={};
			if(first>second&&first>third){
				
			}else{
				 var count2="1";
				 var count3="1";
				 var documents = response['data'];
				 for (var i = 0; i < documents.length; i++) {
					 if(!jsonauthor1.hasOwnProperty(documents[i].author)){
						   jsonauthor[count2]=documents[i].author;
						   count2++;
						   jsonauthor1[documents[i].author]="0";
					   }
					   if(!jsontype1.hasOwnProperty(documents[i].documentType.name)){
						   jsontype[count3]=documents[i].documentType.name;
						   count3++;
						   jsontype1[documents[i].documentType.name]="0";
					   }
				 }
			}
			console.info("filename: : "+first+","+second+","+third);
			if(first<second&&first<third){
				 $("#create_author").empty();
				  createOptions(document.getElementById("create_author"), jsonauthor);
				  $("#create_type").empty();
				  createOptions(document.getElementById("create_type"), jsontype);
			 }else if(first<second&&third<second){
				  $("#create_author").empty();
				  createOptions(document.getElementById("create_author"), jsonauthor);
			 }else if(first<third&&second<third){
				 $("#create_type").empty();
				  createOptions(document.getElementById("create_type"), jsontype);
			 }else{
				 
			 }			
			 
		}, function () {})
    };
    
    $scope.changeauthor = function() {
		var filename=$("#create_filename").find("option:selected").text();
		var author=$("#create_author").find("option:selected").text();
		var type=$("#create_type").find("option:selected").text();
		if(second==startvalue){
			 if(first==startvalue&&third==startvalue){
				 second=1;
			 }else if(first!=startvalue&&third!=startvalue){
				 second=3;
			 }else{
				 second=2;
			 }
		 }
		Document.changeauthor(first,second,third,filename,author,type,$http, function (response) {
			console.info("majorpaper");
			var jsonfilename={};
			var jsontype={};
			var jsonfilename1={};
			var jsontype1={};
            if(second>first&&second>third){
				
			}else{
				 var documents = response['data'];
				 var count1="1";
				 var count3="1";
				 for (var i = 0; i < documents.length; i++) {
				        //result[i]表示获得第i个json对象即JSONObject
				        //result[i]通过.字段名称即可获得指定字段的值
					  if(!jsonfilename1.hasOwnProperty(documents[i].fileName)){
						   jsonfilename[count1]=documents[i].fileName;
						   count1++;
						   jsonfilename1[documents[i].fileName]="0";
					   }
					  if(!jsontype1.hasOwnProperty(documents[i].documentType.name)){
						   jsontype[count3]=documents[i].documentType.name;
						   count3++;
						   jsontype1[documents[i].documentType.name]="0";
					   }
				 }
			}
            console.info("author: : "+first+","+second+","+third);
            if(second<first&&second<third){
            	    $("#create_filename").empty();
					createOptions(document.getElementById("create_filename"), jsonfilename);
					 $("#create_type").empty();
	    			 createOptions(document.getElementById("create_type"), jsontype);
            }else if(second<first&&third<first){
            	    $("#create_filename").empty();
					createOptions(document.getElementById("create_filename"), jsonfilename);
            }else if(second<third&&first<third){
            	 $("#create_type").empty();
    			 createOptions(document.getElementById("create_type"), jsontype);
            }else{
            	
            }
		}, function () {})
    };
    
    $scope.changetype = function() {
		var filename=$("#create_filename").find("option:selected").text();
		var author=$("#create_author").find("option:selected").text();
		var type=$("#create_type").find("option:selected").text();
		if(third==startvalue){
			 if(second==startvalue&&first==startvalue){
				 third=1;
			 }else if(second!=startvalue&&first!=startvalue){
				 third=3;
			 }else{
				 third=2;
			 }
		 }
		Document.changetype(first,second,third,filename,author,type,$http, function (response) {
			var jsonfilename={};
			var jsonauthor={};
			var jsonfilename1={};
			var jsonauthor1={};
            if(third>second&&third>first){
            	
			}else{
				 var documents = response['data'];
    			 var count1="1";
    			 var count2="1";
    			 for (var i = 0; i < documents.length; i++) {
    				 if(!jsonfilename1.hasOwnProperty(documents[i].fileName)){
  					   jsonfilename[count1]=documents[i].fileName;
  					   count1++;
  					   jsonfilename1[documents[i].fileName]="0";
  				     }
  				     if(!jsonauthor1.hasOwnProperty(documents[i].author)){
  					   jsonauthor[count2]=documents[i].author;
  					   count2++;
  					   jsonauthor1[documents[i].author]="0";
  				     }
    			 }
			}
            console.info("type: : "+first+","+second+","+third);
            if(third<first&&third<second){
            	 $("#create_filename").empty();
				 createOptions(document.getElementById("create_filename"), jsonfilename);
				 $("#create_author").empty();
	     		createOptions(document.getElementById("create_author"), jsonauthor);
            }else if(third<first&&second<first){
            	 $("#create_filename").empty();
				 createOptions(document.getElementById("create_filename"), jsonfilename);
            }else if(third<second&&first<second){
            	$("#create_author").empty();
     			createOptions(document.getElementById("create_author"), jsonauthor);
            }else{
            	
            }
		}, function () {})
    };
    
   /*
    * 新建
    */
    $scope.create = function(){
        $("#create_date").val("");
        $("#create_char").val("");
		$("#create_anay").val("");;
        $('#hideorshow').slideDown('slow');  
        $scope.initall();
      };
      
    /*
     * 取消新建
     */
  	$scope.create_cancel = function () {//取消
       $('#hideorshow').slideUp('slow');
  	};
  	
  	/*
  	 * 保存
  	 */
  	$scope.create_save = function () {
	    time=$("#create_date").val();
	    filename=$("#create_filename").find("option:selected").text();
	    author=$("#create_author").find("option:selected").text();
	    type=$("#create_type").find("option:selected").text();
	    classi=$("#create_class").find("option:selected").text();
	    expre=$("#create_expre").find("option:selected").text();
	    content=$("#create_content").val();
	    chara=$("#create_char").val();
	    anay=$("#create_anay").val();
	
	    data={time:time,filename:filename,author:author,type:type,classi:classi,expre:expre,content:content,chara:chara,anay:anay};
	    Document.create_save(data,$http, function (response) {//刷新
	        alert("保存成功！");
	        //$scope.querycoreAll();
	        $scope.initPage();//打开页面显示第一页文档
	    }, function () {
	    	alert("保存失败！");
    })
   
  };
}