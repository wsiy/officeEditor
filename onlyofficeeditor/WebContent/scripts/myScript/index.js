$(function() {
    //顶部导航切换
    $(".navbar a").click(function() {
        $(".navbar a.active").removeClass("active")
        $(this).addClass("active");
    })
})

/*获得url地址中'#'及其之后字符串*/
switch (window.location.hash) {
    case "#/majorPaper":
        $("#majorPaper").addClass("active");
        break;
    case "#/onlineWriting":
        $("#onlineWriting").addClass("active");
        break;
    case "#/paperAnalyse":
        $("#paperAnalyse").addClass("active");
        break;
    case "#/documentPreparation":
        $("#documentPreparation").addClass("active");
        break;
    case "#/dataBackup":
        $("#dataBackup").addClass("active");
        break;
    case "#/personalCenter":
        $("#personalCenter").addClass("active");
        break;
    default:
        $("#paperList").addClass("active");
}

var main = angular.module("mainApp", ['ngRoute', 'ngCookies', 'ngFileUpload']);

main.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when("/paperList", {
        controller: paperListCtrl,
        templateUrl: "./paperList.html"
    }).when("/majorPaper", {
        controller:majorPaperCtrl,
        templateUrl: "./majorPaper.html"
    }).when("/onlineWriting", {
        controller: onlineWritingCtrl,
        templateUrl: "./onlineWriting.html"
    }).when("/paperAnalyse", {
        //          controller:paperAnalyseCtrl,
        templateUrl: "./paperAnalyse.html"
    }).when("/documentPreparation", {
        //          controller:ducumentPreparationCtrl,
        templateUrl: "./documentPreparation.html"
    }).when("/dataBackup", {
        //          controller:dataBackupCtrl,      
        templateUrl: "./dataBackup.html"
    }).when("/personalCenter", {
        controller: personalCenterCtrl,
        templateUrl: "./personalCenter.html"
    }).otherwise({
        redirectTo: '/paperList'
    });
}]);


/*显示高亮*/
main.filter("highlight", function($sce){
	var fn = function(text){ 
	return $sce.trustAsHtml(text);
	};
	return fn;
});