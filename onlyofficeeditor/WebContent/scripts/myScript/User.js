var User = {
	isLogin: false,
	id: "",
	password: "",
    /**
     * 用户登录
     * @param id 用户id
     * @param password 用户密码
     * @param $http $http对象
     * @param success 成功时回调的函数
     * @param error 失败时回调的函数
     */
	login: function (id, password, $http, success, error) {
        $http({
            method:"GET",
            url:'/CXF/REST/Misc/doLogin/'+id+'/'+password
        }).then(success, error)
    },
    /**
     * 用户退出
     */
    logOut: function () {
        User.isLogin = false;
        User.id = '';
        User.password = '';
    },
    /**
     * 用户修改密码
     * @param old 旧密码
     * @param now 新密码
     * @param $http $http对象
     * @param success 成功时回调的函数
     * @param error 失败时回调的函数
     */
    changePassword: function (uid,old, now, $http, success, error) {
        $http({
            method:"GET",
//            url:"/onlyofficeeditor/testResponse/changePwd/"+uid+"/"+old+"/"+now
            url:"./testResponse/changePwd"
        }).then(success, error);
    },
    /**
     * 用户注册
     * @param user 用户对象
     * @param $http $http对象
     * @param success 成功时回调的函数
     * @param error 失败时回调的函数
     */
    register: function (user, $http, success, error) {
        $http({
            method:"POST",
            url:"/CXF/REST/Misc/register",
            data:JSON.stringify(user)
        }).then(success, error);
    }
}