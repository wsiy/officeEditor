//个人中心的controller
function personalCenterCtrl($scope, $http, $location) {
    /*左侧的“用户信息”按钮点击效果*/
    $scope.showUserInfo = function() {
        if ($("#function-btn").hasClass("activeBlock-Btn")) {
            $("#function-btn").removeClass("activeBlock-Btn");
            $("#function-btn").addClass("unactiveBlock-Btn");
            $("#funcWhite-img").addClass("noDisplay");
            $("#funcBlack-img").removeClass("noDisplay");
        }
        $("#userInf-btn").removeClass("unactiveBlock-Btn");
        $("#userInf-btn").addClass("activeBlock-Btn");
        $("#userWhite-img").removeClass("noDisplay");
        $("#userBlack-img").addClass("noDisplay");
        $scope.hideUserDiv = false;
        $scope.hideFuncDiv = true;
    }
    /*左侧的“功能权限”按钮点击效果*/
    $scope.showFunction = function() {
        if ($("#userInf-btn").hasClass("activeBlock-Btn")) {
            $("#userInf-btn").removeClass("activeBlock-Btn");
            $("#userInf-btn").addClass("unactiveBlock-Btn");
            $("#userWhite-img").addClass("noDisplay");
            $("#userBlack-img").removeClass("noDisplay");
        }
        $("#function-btn").removeClass("unactiveBlock-Btn");
        $("#function-btn").addClass("activeBlock-Btn");
        $("#funcWhite-img").removeClass("noDisplay");
        $("#funcBlack-img").addClass("noDisplay");
        $scope.hideUserDiv = true;
        $scope.hideFuncDiv = false;
    }
    /*页面初始化时就调用该函数*/
    $scope.showUserInfo();

    $scope.user = {}
    $scope.submit = function() {
        console.log($scope.user.id);
        console.log($scope.user.confirm);
        User.changePassword($scope.user.id, $scope.user.old, $scope.user.confirm, $http, function(response) {
            console.log(response['data']);
            if (response['data'] == true) {
                $scope.user = {};
                $.thSuccess("修改成功！");
            } else {
                $.thError("修改失败！");
            }
        }, function() {
            $.thError("修改失败！");
        });
    }
  
}