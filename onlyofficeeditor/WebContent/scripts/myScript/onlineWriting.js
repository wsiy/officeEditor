//在线写作
function onlineWritingCtrl($scope, $http) {

//	/*点击导入按钮之后，清空文档编辑区的内容*/
//	$('#importFile').click(function(){
//		$("#hiddenFileName").val("");
////		сonnectEditor();
//	})
//	$(function(){  
//	    initEdit("index.docx");
//	});  
	$scope.import_author="";
	$scope.import_sort="";
	
	$scope.uploadFileFunc = function(){
		//$("#importFile").click();
		var dialogParent = $('#importDialog').parent();
		var dialogOwn = $('#importDialog').clone();
		dialogOwn.hide();
		
		$('#importDialog').dialog({
    		modal:true,
    		width:"400",
    		height:"185",
    		close: function(){
    			dialogOwn.appendTo(dialogParent);
    			$(this).dialog("destroy").remove();
    		}
    	});
     	document.getElementById("importDialog").style.display="block";
	};
	
	$scope.import_paper_cancel=function(){
		 /*console.info("Cancel the import!");
  	      $("#import_author").val("");
		  $("#import_type").empty(); 
		 var ops={
				  "总结报告":"总结报告",
				  "辅助授课":"辅助授课",
				  "其他":"其他"
				};
		 var parent=document.getElementById("import_type");
		 for(var key in ops)
		 {
			 var o = new Option(ops[key],key);
	         parent.appendChild(o);
		      
		 }*/
		  $('#importDialog').dialog("close");
		 $('#importDialog').remove();
  	   
	};
	
   $scope.import_paper_save=function(){
	   	$scope.import_author = jq("#import_author").val();
		$scope.import_sort= jq("#import_type").val();
	console.log("~~~~~~~~~~~~~~~ before import dialog close, import_sort="+$scope.import_sort+",$scope.import_author="+$scope.import_author);

		$('#importDialog').dialog("close");	
	   	$("#importFile").click();
	};
    /****************下面是上传文件的js(onlyOffic的jscript.js)*************************/
	if (typeof jQuery !== "undefined") {
	    jq = jQuery.noConflict();
	    jq(function () {
	        jq("#importFile").fileupload({
	            dataType: "json",
	            Type : 'POST',
	            autoUpload: false,//是否自动上传
	            url:"./IndexServlet?type=upload",
	            add: function (e, data) {
	                jq(".error").removeClass("error");
	                jq(".done").removeClass("done");
	                jq(".current").removeClass("current");
	                jq("#step1").addClass("current");
	                jq("#mainProgress .error-message").hide().find("span").text("");
	                jq("#mainProgress").removeClass("embedded");

	                jq.blockUI({
	                    theme: true,
	                    title: "文档导入中" + "<div class=\"dialog-close\"></div>",
	                    message: jq("#mainProgress"),
	                    overlayCSS: { "background-color": "#aaa" },
	                    themedCSS: { width: "656px", top: "20%", left: "50%", marginLeft: "-328px" }
	                });
	                jq("#beginEdit, #beginView, #beginEmbedded").addClass("disable");
	                console.log("1111");
	                data.submit();
	            },
	            always: function (e, data) {
	                if (!jq("#mainProgress").is(":visible")) {
	                    return;
	                }
	                console.log("3333");
	                console.log(data.jqXHR.responseText);
	                var response = data.result; 
	                console.log(response);
	                if (response.error){
	                    jq(".current").removeClass("current");
	                    jq(".step:not(.done)").addClass("error");
	                    jq("#mainProgress .error-message").show().find("span").text(response.error);
	                    jq('#hiddenFileName').val("");
	                    return;
	                }
	                jq("#hiddenFileName").val(response.filename);
	                
	                //---------add by liuyy----
	                //save author,doc type and filename into database once upload finished
	                //get file name without extension name
	                
	                var fileName = response.filename.split('.')[0];
	                var fileExt = response.filename.split('.')[1];
	                var data={filename:fileName,fileExt:fileExt,author:$scope.import_author,type:$scope.import_sort};
	    			Document.create_paper_save(data,$http, function (response) {
	    				 console.log("导入的文件记录已经存入数据库");
	    				 //add index for the imported file
	    				 Document.add_index_single(data.filename+"."+data.fileExt,$http, function (response) {
	    					 console.log("导入的文件已建立索引");
	    				 }, function () {
	    		    	       alert("为文档"+data.filename+"."+data.fileExt+"建立索引失败！");
		    		     });
	    				 
	    		      }, function () {
	    			    alert("导入过程出错，请查看日志！");
	    		    });
	                console.log("-----------data to be saved as below:");
	                console.log(data);
	                
	                jq("#step1").addClass("done").removeClass("current");
	                jq("#step2").addClass("current");
	                console.log("4444");
	                checkConvert();
	            }
	        });
	    });
	    
	  //获取图片地址
		function getUrl(file) {
			var url = null;
			if (window.createObjectURL != undefined) {
				url = window.createObjectURL(file);
			} else if (window.URL != undefined) {
				url = window.URL.createObjectURL(file);
			} else if (window.webkitURL != undefined) {
				url = window.webkitURL.createObjectURL(file);
			}
			return url;
		}

	    $("#importFile").change(function () {
	        $("#text").html($("#fileinp").val());
	    })
	    
	    var timer = null;
	    
	    
	    //检查文件是否能够转换
	    var checkConvert = function () {
            console.log("checkConvert");
	        if (timer !== null) {
	            clearTimeout(timer);
	        }

	        if (!jq("#mainProgress").is(":visible")) {
	            return;
	        }

	        var fileName = jq("#hiddenFileName").val();
	        var posExt = fileName.lastIndexOf(".");
	        posExt = 0 <= posExt ? fileName.substring(posExt).trim().toLowerCase() : "";

	        if (ConverExtList.indexOf(posExt) === -1) {
	            loadScripts();
	            return;
	        }

	        if (jq("#checkOriginalFormat").is(":checked")) {
	            loadScripts();
	            return;
	        }
            console.log("checkConvert2");
	        timer = setTimeout(function () {
	            var requestAddress = UrlConverter
	                + "&filename=" + encodeURIComponent(jq("#hiddenFileName").val());
	            console.log("***************in jsscript.js-->checkConvert,before send ajax request of convert file");

	            jq.ajax({
	                async: true,
	                contentType: "text/xml",
	                type: "get",
	                url: requestAddress,
	                complete: function (data) {
	                    var responseText = data.responseText;
	                    var response = jq.parseJSON(responseText);
	                    if (response.error) {
	                        jq(".current").removeClass("current");
	                        jq(".step:not(.done)").addClass("error");
	                        jq("#mainProgress .error-message").show().find("span").text(response.error);
	                        jq('#hiddenFileName').val("");
	                        return;
	                    }

	                    jq("#hiddenFileName").val(response.filename);

	                    if (response.step && response.step < 100) {
	                        checkConvert();
	                    } else {
	                        loadScripts();
	                    }
	                }
	            });
	        }, 1000);
	    };

	    var loadScripts = function () {
            console.log("loadScripts");
	        if (!jq("#mainProgress").is(":visible")) {
	            return;
	        }
	        jq("#step2").addClass("done").removeClass("current");
	        jq("#step3").addClass("current");

	        if (jq("#loadScripts").is(":empty")) {
	            var urlScripts = jq("#loadScripts").attr("data-docs");
	            var frame = "<iframe id=\"iframeScripts\" width=1 height=1 style=\"position: absolute; visibility: hidden;\" ></iframe>";
	            jq("#loadScripts").html(frame);
	            document.getElementById("iframeScripts").onload = onloadScripts;
	            jq("#loadScripts iframe").attr("src", urlScripts);
	        } else {
	            onloadScripts();
	        }
	    };

	    var onloadScripts = function () {
            console.log("onloadScripts");
	        if (!jq("#mainProgress").is(":visible")) {
	            return;
	        }
	        jq("#step3").addClass("done").removeClass("current");
	        jq("#beginView, #beginEmbedded").removeClass("disable");

	        var fileName = jq("#hiddenFileName").val();
	        console.log("fileName="+fileName);
	        var posExt = fileName.lastIndexOf(".");
	        posExt = 0 <= posExt ? fileName.substring(posExt).trim().toLowerCase() : "";

	        if (EditedExtList.indexOf(posExt) !== -1) {
	            jq("#beginEdit").removeClass("disable");
	        }
	    };
	    //	    下面的三个按钮之“编辑”
	    jq(document).on("click", "#beginEdit:not(.disable)", function () {
	    	edit();
	    });
	    // 下面的三个按钮之“查看文档”
	    jq(document).on("click", "#beginView:not(.disable)", function () {
	        view();
	    });

	    // 下面的三个按钮之“取消”
	    jq(document).on("click", "#cancelEdit, .dialog-close", function () {
	        jq('#hiddenFileName').val("");
	        jq("#embeddedView").attr("src", "");
	        jq.unblockUI();
	    });

	    jq.dropdownToggle({
	        switcherSelector: ".question",
	        dropdownID: "hint"
	    });
	    
	    var view = function(){
	    	var fileId = encodeURIComponent(jq("#hiddenFileName").val());
	        var url = UrlEditor + "?mode=view&fileName=" + fileId;//这里设置模式
	        console.log("----wsy:view()---url"+url);
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
	    }
	    
	    
	    var edit = function(){
	    	var fileId = encodeURIComponent(jq("#hiddenFileName").val());
	    	console.log("............................"+fileId);
	        var url = UrlEditor + "?mode=edit&fileName=" + fileId;//这里设置模式
	        console.log("----wsy:edit()---url"+url);
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
	    }
	    
	    var initEdit = function(fileId){
	    	var fileId = fileId;
	    	console.log("init......................"+fileId);
	        var url = UrlEditor + "?mode=edit&fileName=" + fileId;//这里设置模式
	        console.log("----wsy:edit()---url"+url);
	    	$.ajax({
                url:url,
                type:"post",
                success:function(response, status){
                	responseData = response;
                	jq("#hiddenFileName").val("");
//        	        jq.unblockUI();
        	        console.log(response);//输出response内容
        	        сonnectEditor();//调用editor.js的内容
                }, error: function (data) {
                	console.log("error");
                }
	    	});
	    }
	    /****************上面是上传文件的js(onlyOffic的jscript.js)*************************/	    
	    /***************************下面是editor.js******************************************/
	    var editorEnter = function() {
	    	if (window.addEventListener) {
	    		console.log("addEventListener");
	    	    window.addEventListener("load", connectEditor);
	    	} else if (window.attachEvent) {
	    		console.log("attachEvent");
	    	    window.attachEvent("load", connectEditor);
	    	}
	    }
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
		                    	image:"http://"+hostNameAndPort+projectName+"images/loginPage/blue.jpg",//蓝色纯色
		                    	//image:"http://www.golaxy.cn/data/upload/image/201704/ee4edb5774142f5c7eb39d3bb9549ed4.jpg",//中科天玑的黑色logo
		                    	url: "http://"+hostNameAndPort+pathName
		                    },
	                        forcesave:true,	
	                        zoom:-2,
	                    },
	                },

	            });
	    };
	    
	    var innerAlert = function (message) {
	    	console.log("~~~~~~~~~~~~~~~~~~~~~innerAlert~~~~~~~~~~~~~~~~~~~~");
	        if (console && console.log)
	            console.log(message);
	    };

	    var onReady = function () {
	        innerAlert("Document editor ready");
	    };

	    var onDocumentStateChange = function (event) {
	    	console.log("~~~~~~~~~~~~~~~~~~~~~onDocumentStateChange~~~~~~~~~~~~~~~~~~~~");
	        var title = document.title.replace(/\*$/g, "");
	        document.title = title + (event.data ? "*" : "");
	    };

	    var onRequestEditRights = function () {
        	console.log("~~~~~~~~~~~~~~~~wsy:onRequestEditRights~~~~~~~~~~~~~~~~~~~~~~");
	        location.href = location.href.replace(RegExp("action=view\&?", "i"), "");
	    };

	    var onError = function (event) {
	        if (event){
	        	console.log("~~~~~~~~~~~~~~~~wsy:onError~~~~~~~~~~~~~~~~~~~~~~");
	            innerAlert(event.data);
	        }
	    };

	    var onOutdatedVersion = function (event) {
	        location.reload(true);
	    };
	    

	    function getXmlHttp() {
	        var xmlhttp;
	        try {
	            xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
	        } catch (e) {
	            try {
	                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	            } catch (ex) {
	                xmlhttp = false;
	            }
	        }
	        if (!xmlhttp && typeof XMLHttpRequest !== "undefined") {
	            xmlhttp = new XMLHttpRequest();
	        }
	        return xmlhttp;
	    }
	    /***************************上面是editor.js******************************************/
	}
	
	// 查询全部文档信息
	$scope.queryAll = function () {
		$scope.documents = {};
		Document.queryAll($http, function (response) {
			$scope.documents = response['data'];
		}, function () {})
	};
	
	$scope.AISearch = function(){
		var AISearchSentence = $("#AISearchSentence").val();
		$scope.documents = {}; //设置成全局变量
		console.log("AISearchSentence="+AISearchSentence);
		AISearch.searchPassages(AISearchSentence,$http, function (response) {
			$scope.documents = response['data'];
			console.log(response['data']);
			if($scope.documents.size == 0){
				alert("未查询到相关文档");
			}
		}, function () {
			alert("未查询到相关文档");
			console.log("query error");
		})
	}
	
	
	$scope.showAllPassages = function(type){
		changeActive(type);
		var AISearchSentence = $("#AISearchSentence").val();
		console.log('查询关键字是：'+ AISearchSentence+'类型是'+type);
		AISearch.filterDocByType(type,AISearchSentence,$http, function (response) {
			$scope.documents = response['data'];
			if($scope.documents.size == 0){
				alert("未查询到相关文档");
			}
		}, function () {
			alert("未查询到相关文档");
			console.log("query error");
		})
	}
	
	changeActive = function(type){
		/*去掉active*/
		 $(".list-group button").click(function() {
		        $(".list-group button").removeClass("active")
		        $(this).addClass("active");
		    })
		/*添加active*/
		switch (type){
			case "全部":
		        $("#quanbu").addClass("active");
		        break;
			case "提纲":
				$("#quanbu").removeClass("active");
		        $("#tigang").addClass("active");
		        break;
			case "观点":
		        $("#guandian").addClass("active");
		        break;
			case "其他":
		        $("#qita").addClass("active");
		        break;
			case "我的收藏":
		        $("#wdsc").addClass("active");
		        break;
		    default:
		        $("#quanbu").addClass("active");
		    	break;
		}
	}
}
