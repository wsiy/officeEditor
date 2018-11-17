//公文列表
function paperListCtrl($scope, $http) {
	/*
	 * 根据时间顺序
	 */
    $scope.changeDirection = function() {
    	$scope.pb = {};
        if ($(".orderByTime").hasClass("my-btn-blue2")) {
            $(".orderByTime").removeClass("my-btn-blue2");
            $(".up").addClass("noDisplay");
            $(".down").removeClass("noDisplay");
            bool=false;
    		Document.orderByTime(bool,$http, function (response) {
    			$scope.pb = response['data'];
    			if($scope.pb.totalRecord == 1){
    				$scope.changeFormat();
    			}
    		}, function () {});
        } else {
            $(".orderByTime").addClass("my-btn-blue2");
            $(".down").addClass("noDisplay");
            $(".up").removeClass("noDisplay");
            bool=true;
    		Document.orderByTime(bool,$http, function (response) {
    			$scope.pb = response['data'];
    			if($scope.pb.totalRecord == 1){
    				$scope.changeFormat();
    			}
    		}, function () {});
        }
    }

    //初次加载时显示全部Document第一页
    $scope.initPage = function(){
    	$scope.pb = {};
		Document.initQuery($http, function (response) {
			$scope.pb = response['data'];
			if($scope.pb.totalRecord == 1){
				$scope.changeFormat();
			}
		}, function () {})
		
    };
    
	$scope.initPage();//打开页面显示第一页文档
/*	
	// 查询全部文档信息
	$scope.queryAll = function () {
		$scope.pb.list = {};
		Document.queryAll($http, function (response) {
			$scope.pb.list = response['data'];
		}, function () {})
	};*/
	
	//按类型查询
	$scope.queryAllReport = function (keyword) {
		$scope.pb = {};
		Document.queryByType(keyword, $http, function (response) {
			$scope.pb = response['data'];
			if($scope.pb.totalRecord == 1){
				$scope.changeFormat();
			}
		}, function () {});
	};
	
	//按名字查询
	$scope.querybyName = function () {
		$scope.pb = {};
		keyword=$("#filesearch").val();
		Document.querybyName(keyword, $http, function (response) {
			$scope.pb = response['data'];
			if($scope.pb.totalRecord == 1){
				$scope.changeFormat();
			}
		}, function () {});
	};
	
	
	/*
	 * 对一条数据格式从对象转为数组
	 */
	$scope.changeFormat = function () {
		perDoc=new Object();
		perDoc.author = $scope.pb.perList.author;
		perDoc.ID = $scope.pb.perList.ID;
		perDoc.collected = $scope.pb.perList.collected;
		perDoc.createDate = $scope.pb.perList.createDate;
		perDoc.fileName = $scope.pb.perList.fileName;
		perDoc.inCore = $scope.pb.perList.inCore;
		perDoc.sameNum = $scope.pb.perList.sameNum;
		
		type=new Object();
		type.ID=$scope.pb.perList.documentType.ID;
		type.name=$scope.pb.perList.documentType.name;
		
		perDoc.documentType = type;
		$scope.pb.perList =new Array(perDoc);
	};
	//按时间查询
	$scope.querybyDate = function () {
		
		keyword=$("#date").val();
		console.info(keyword);
		$scope.pb = {};
		Document.querybyDate(keyword, $http, function (response) {
			$scope.pb = response['data'];
			if($scope.pb.totalRecord == 1){
				$scope.changeFormat();
			}
		}, function () {});
	};
	
    //删除某个文档信息
    $scope.delete = function (index, id, fileName) {
    	var msg = "您真的确定要删除吗？\n\n请确认！";
    	if (window.confirm(msg)==true){
    		 Document.remove(id, $http, function () {
    			var current = $scope.pb.startIndex+index+1;
    	        alert("文档"+current+"已删除");
	            //remove index for the doc
   				Document.remove_index_single(fileName+".docx",$http, function (response) {
   					 console.log("文件索引已删除");
   				}, function () {
   		    	     alert("删除文件"+fileName+"索引失败！");
	    		});
   				 
	            $scope.initPage();//打开页面显示第一页文档
    	     }, function () {
    	         alert("删除出错");
	     	})
    	}else{
    		return false;
    	}
    };
    //收藏文档
    $scope.collect = function (index,id) {
         Document.collect(id, $http, function () {
          var collect_id="collect_"+id;
          var current = $scope.pb.startIndex+index+1;
          alert("文档"+current+"收藏成功");
          document.getElementById(collect_id).setAttribute("class","collected-btn common-btn");
         // $scope.initPage();//打开页面显示第一页文档
        }, function () {
        	alert("收藏失败");
        })
    };
    
    //下载文档，未实现！！！！！！！！！
    $scope.download = function (id) {
          alert("文档已下载");
    };


    // 首页
    $scope.firstPage = function(){
    	if($scope.pb.totalRecord != 0){
	    	$scope.pb.perList.splice(0,$scope.pb.endIndex-$scope.pb.startIndex+1);
	    	$scope.pb.startIndex = 0 ;
	    	$scope.pb.pageNum = 1;
	    	
	    	 if($scope.pb.totalPage <= $scope.pb.pageNum ) {
	    		 $scope.pb.endIndex = $scope.pb.totalRecord - 1;
	         }else {
	        	 $scope.pb.endIndex = $scope.pb.startIndex + $scope.pb.pageSize - 1;
	         }
	    	 
	    	for (var i=$scope.pb.startIndex;i<=$scope.pb.endIndex;i++)
	    	{
	    		$scope.pb.perList.push($scope.pb.totalList[i]);
	    		if(typeof($scope.pb.perList[i-$scope.pb.startIndex])=="undefined"){ 
	    			$scope.pb.perList.splice(i-$scope.pb.startIndex,1)
	    		} 
	    	} 	
    	}
    };
    
    // 上一页
    $scope.previousPage = function(){
    	if($scope.pb.totalRecord != 0){
	    	$scope.pb.perList.splice(0,$scope.pb.endIndex-$scope.pb.startIndex+1);
	    	
	    	 if($scope.pb.pageNum == 1) {
	    		 $scope.pb.startIndex = 0 ;
	    		 if($scope.pb.totalPage <= $scope.pb.pageNum ) {
	        		 $scope.pb.endIndex = $scope.pb.totalRecord - 1;
	             }else {
	            	 $scope.pb.endIndex = $scope.pb.startIndex + $scope.pb.pageSize - 1;
	             }
	         }else {
	        	 $scope.pb.pageNum = $scope.pb.pageNum-1;
	         	 $scope.pb.startIndex = ($scope.pb.pageNum-1)*$scope.pb.pageSize ;
	        	 $scope.pb.endIndex = $scope.pb.startIndex + $scope.pb.pageSize - 1;
	         }
	    	 
	    	for (var i=$scope.pb.startIndex;i<=$scope.pb.endIndex;i++)
	    	{
	    		$scope.pb.perList.push($scope.pb.totalList[i]);
	    		if(typeof($scope.pb.perList[i-$scope.pb.startIndex])=="undefined"){ 
	    			$scope.pb.perList.splice(i-$scope.pb.startIndex,1)
	    		} 
	    	} 
    	}
    };
    
    // 下一页
    $scope.nextPage = function(){
    	if($scope.pb.totalRecord != 0){
	    	if($scope.pb.pageNum < $scope.pb.totalPage){//当前页是尾页，则下一页不变
	    		$scope.pb.perList.splice(0,$scope.pb.endIndex-$scope.pb.startIndex+1);
	    		$scope.pb.pageNum = $scope.pb.pageNum+1;
	    		$scope.pb.startIndex = ($scope.pb.pageNum-1)*$scope.pb.pageSize ;
	    		if($scope.pb.pageNum == $scope.pb.totalPage) {//当前页是倒数第二页，则下一页是尾页
	    			$scope.pb.endIndex = $scope.pb.totalRecord - 1;
	            }else{
	            	$scope.pb.endIndex = $scope.pb.startIndex + $scope.pb.pageSize - 1;
	            }
	    		
	    		for (var i=$scope.pb.startIndex;i<=$scope.pb.endIndex;i++){
	        		$scope.pb.perList.push($scope.pb.totalList[i]);
	        		if(typeof($scope.pb.perList[i-$scope.pb.startIndex])=="undefined"){ 
	        			$scope.pb.perList.splice(i-$scope.pb.startIndex,1)
	        		} 
	        	} 	
	    	}
    	}
    };

    // 尾页
    $scope.lastPage = function(){
    	if($scope.pb.totalRecord != 0){
	    	$scope.pb.perList.splice(0,$scope.pb.endIndex-$scope.pb.startIndex+1);
	    	$scope.pb.startIndex = ($scope.pb.totalPage-1)*$scope.pb.pageSize ;
			$scope.pb.endIndex = $scope.pb.totalRecord - 1;
	        $scope.pb.pageNum = $scope.pb.totalPage;
	    	 
	    	for (var i=$scope.pb.startIndex;i<=$scope.pb.endIndex;i++)
	    	{
	    		$scope.pb.perList.push($scope.pb.totalList[i]);
	    		if(typeof($scope.pb.perList[i-$scope.pb.startIndex])=="undefined"){ 
	    			$scope.pb.perList.splice(i-$scope.pb.startIndex,1)
	    		} 
	    	} 
    	}
    };

    
    /*
     * 新增
     */
	  $scope.showdiv=function(){
		  var dialogParent = $('#my_dialog').parent();
		  var dialogOwn = $('#my_dialog').clone();
		  dialogOwn.hide();
			
		  $('#my_dialog').dialog({
			  modal:true,
			  width:"400",
	    	  height:"223",
	    	  close: function(){
	    			dialogOwn.appendTo(dialogParent);
	    			$(this).dialog("destroy").remove();
	    		}
		  	});
		  document.getElementById("my_dialog").style.display="block";
	  };
    	
	$scope.create_paper_cancel=function(){
		 console.info("取消");
   	     /*$("#create_name").val("");
		 $("#create_author").val("");
		 $("#create_type").empty(); 
		 var ops={
					"总结报告":"总结报告",
					"辅助授课":"辅助授课",
					"其他":"其他"
				};
		 var parent=document.getElementById("create_type");
		 for(var key in ops)
		 {
			 var o = new Option(ops[key],key);
	         parent.appendChild(o);
		      
		 }*/
   	     $('#my_dialog').dialog("close");
   	     $('#my_dialog').remove();
	};
    	
        $scope.create_paper_save=function(){
        	
        	$scope.create_name = document.getElementById("create_name").value; 
        	var create_author = document.getElementById("create_author").value;   
        	var create_type = document.getElementById("create_type").value;
        	console.log("####### before create dialog close, $scope.create_name="+$scope.create_name+",create_author="+create_author+",create_type="+create_type);
        	$('#my_dialog').dialog("close");
    		var data={filename:$scope.create_name,author:create_author,type:create_type};
    		Document.create_paper_save(data,$http, function (response) {
    		   // 原来代码的
    			newFileextend();
    	   }, function () {
    		   alert("保存失败！");
    	   })
    	};
    	
    	/*新建文件*/
    	$scope.newFile =function(){
    		 $scope.showdiv();
    		
    	};
    	/*公文列表界面上的导入文件*/
    	$scope.importFileOnPaperList =function(){
    		var onlinenHref = './index.html#/onlineWriting';
    	    location.href = onlinenHref;
    	    //导航栏的改变
    	    $(".navbar a.active").removeClass("active");
    	    $("#onlineWriting").addClass("active");
    	};
    	
       function newFileextend(){
    		var onlinenHref = './index.html#/onlineWriting';
    	    location.href = onlinenHref;
    	    //导航栏的改变
    	    $(".navbar a.active").removeClass("active");
    	    $("#onlineWriting").addClass("active");
    	    //打开编辑页面
    	    
            var url = UrlEditor + "?mode=edit&newFileName="+$scope.create_name+"&fileExt=docx";
        	$.ajax({
                url:url,
                type:"post",
                success:function(response, status){
                	responseData = response;
                	jq("#hiddenFileName").val("");
        	        jq.unblockUI();
        	        console.log("~~~~~~editorServlet return data is as below:~~~~~~~~~");
        	        console.log(response);//输出response内容
        	        сonnectEditor();//调用editor.js的内容
                }, error: function (data) {
                	console.log("error");
                }
        	});
    	    
        	 var сonnectEditor = function() {
     	    	var hostNameAndPort = window.location.host;
     	    	var pathName = window.location.pathname;
     	    	var projectName = pathName.substring(0,pathName.indexOf('.')-5);
     	    	console.log("~~~~~~~~~~~~~~~~~~~~~сonnectEditor~~~~~~~~~~~~~~~~~~~~");
     	    	console.log("responseData.file.documentType:"+responseData.file.documentType);
     	    	console.log(" callbackUrl: responseData.file.callBackUrl"+ responseData.file.callBackUrl);
     	        docEditor = new DocsAPI.DocEditor("iframeEditor",
     	            {
     	                width: "100%",
     	                height: "100%",
     	                type: responseData.type,
     	                documentType: responseData.file.documentType,
     	                document: {
     	                    title: responseData.file.fileName,
     	                    url: responseData.file.fileUri, //--Model.GetFileStorageURL()
     	                    fileType: responseData.file.fileType,
     	                    key: responseData.file.key,
     //  以上配置即可以打开文档服务器，虽然编辑完完页面虽然显示自动保存，但是它是保存在它自己配置的一个缓存数据库中，你下次打开它会从此数据库中获取，但是下载出来的仍然是编辑之前的。
     	                    info: {
     	                        author: "Me",
     	                        created: responseData.createdTime,
     	                    },

     	                    permissions: {
     	                        edit: responseData.permissionsEdit,
     	                        download: true,
     	                    }
     	                },
     	                editorConfig: {
     	                    mode: responseData.editorConfigMode,
     	                    lang: "zh-cmn-Hans",
     	                    //这里可以设置保存路径，callbackurl即为回调处理，把编辑后文件保存在本地的指定路径上
     	                    callbackUrl: responseData.file.callBackUrl,

     	                    user: {
     	                        id: responseData.curUserHostAddress,
     	                        name: "John Smith",
     	                    },

     	                    embedded: {
     	                        saveUrl: responseData.file.fileUri,
     	                        embedUrl: responseData.file.fileUri,
     	                        shareUrl: responseData.file.fileUri,
     	                        toolbarDocked: "top",
     	                    },

     	                    customization: {
     	                        about: false,//关于
     	                        compactToolbar: false,
     	                        feedback: false,//反馈支持
     	                        goback: {
     	                            url: "http://"+hostNameAndPort+pathName,
     	                            text:"公文列表"
     	                        },
     		                    //autosave是自动保存，默认值为true,这里设置为false改为手动保存。
     		                    autosave: false,
     		                    chat: false,
     		                    comments:false,
     		                    logo: {
//     		                    	image:"http://"+hostNameAndPort+projectName+"images/loginPage/blue.jpg",//蓝色纯色
     		                    	image:"http://www.golaxy.cn/data/upload/image/201704/ee4edb5774142f5c7eb39d3bb9549ed4.jpg",//中科天玑的黑色logo
     		                    	url: "http://"+hostNameAndPort+pathName
     		                    },
     	                        forcesave:true,	
     	                        zoom:-2,
     	                    },
     	                },

     	            });
     	    };
    	};
    	
    	
    	/*Edit a doc***Need to refine to reuse with newFile function**/
    	$scope.editFile = function(fileName){
    		var onlinenHref = './index.html#/onlineWriting';
    	    location.href = onlinenHref;
    	    //导航栏的改变
    	    $(".navbar a.active").removeClass("active");
    	    $("#onlineWriting").addClass("active");
    	    //打开编辑页面
console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~before all editorServlet ajax~~~~~~~~~~~~");
            var url = UrlEditor + "?mode=edit&fileName="+fileName;
            console.log("----in editFile---url to pass to EditorServlet:"+url);
        	$.ajax({
                url:url,
                type:"post",
                success:function(response, status){
                	responseData = response;
                	jq("#hiddenFileName").val("");
        	        jq.unblockUI();
        	        console.log(response);//输出response内容
        	        сonnectEditor();//调用editor.js的内容
                }, error: function (data) {
                	console.log("error");
                }
        	});
    	    
        	 var сonnectEditor = function() {
     	    	var hostNameAndPort = window.location.host;
     	    	var pathName = window.location.pathname;
     	    	var projectName = pathName.substring(0,pathName.indexOf('.')-5);
     	    	console.log("~~~~~~~~~~~~~~~~~~~~~сonnectEditor~~~~~~~~~~~~~~~~~~~~");
     	    	console.log("responseData.file.documentType:"+responseData.file.documentType);
     	    	console.log(" callbackUrl: responseData.file.callBackUrl"+ responseData.file.callBackUrl);
     	        docEditor = new DocsAPI.DocEditor("iframeEditor",
     	            {
     	                width: "100%",
     	                height: "100%",
     	                type: responseData.type,
     	                documentType: responseData.file.documentType,
     	                document: {
     	                    title: responseData.file.fileName,
     	                    url: responseData.file.fileUri, //--Model.GetFileStorageURL()
     	                    fileType: responseData.file.fileType,
     	                    key: responseData.file.key,
     //  以上配置即可以打开文档服务器，虽然编辑完完页面虽然显示自动保存，但是它是保存在它自己配置的一个缓存数据库中，你下次打开它会从此数据库中获取，但是下载出来的仍然是编辑之前的。
     	                    info: {
     	                        author: "Me",
     	                        created: responseData.createdTime,
     	                    },

     	                    permissions: {
     	                        edit: responseData.permissionsEdit,
     	                        download: true,
     	                    }
     	                },
     	                editorConfig: {
     	                    mode: responseData.editorConfigMode,
     	                    lang: "zh-cmn-Hans",
     	                    //这里可以设置保存路径，callbackurl即为回调处理，把编辑后文件保存在本地的指定路径上
     	                    callbackUrl: responseData.file.callBackUrl,

     	                    user: {
     	                        id: responseData.curUserHostAddress,
     	                        name: "John Smith",
     	                    },

     	                    embedded: {
     	                        saveUrl: responseData.file.fileUri,
     	                        embedUrl: responseData.file.fileUri,
     	                        shareUrl: responseData.file.fileUri,
     	                        toolbarDocked: "top",
     	                    },

     	                    customization: {
     	                        about: false,//关于
     	                        compactToolbar: false,
     	                        feedback: false,//反馈支持
     	                        goback: {
     	                            url: "http://"+hostNameAndPort+pathName,
     	                            text:"公文列表"
     	                        },
     		                    //autosave是自动保存，默认值为true,这里设置为false改为手动保存。
     		                    autosave: false,
     		                    chat: false,
     		                    comments:false,
     		                    logo: {
//     		                    	image:"http://"+hostNameAndPort+projectName+"images/loginPage/blue.jpg",//蓝色纯色
     		                    	image:"http://www.golaxy.cn/data/upload/image/201704/ee4edb5774142f5c7eb39d3bb9549ed4.jpg",//中科天玑的黑色logo
     		                    	url: "http://"+hostNameAndPort+pathName
     		                    },
     	                        forcesave:true,	
     	                        zoom:-2,
     	                    },
     	                },

     	            });
     	    };
    	};
}
