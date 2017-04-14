/**
 * Created by 张龙玉 on 2017/1/24.
 */
var flagUser = true,
    flagPwd = true;

var Validate = {
    usernamePattern: /^(\w){6,16}$/,
    passwordPattern: /^(\w){8,20}$/,
    /*validate username*/
    usernameValidate: function (usernameValue) {
        if (!this.usernamePattern.test(usernameValue)) {
            $('.user_err').css('display', 'block');
            $('.login_text').css('boxShadow', '0 0 2px 1px #f00');
            $('.loginCnt').height(398 + 'px');
            $('.loginBox').height(264 + 'px');
            $('.login_text').on('focus', function () {
                $('.user_err').css('display', 'none');
                $(this).css('boxShadow', '0 0 2px 1px #29bdb9');
                $('.loginCnt').height(342 + 'px');
                $('.loginBox').height(215 + 'px');
            });
            flagUser = false;
        } else {
            flagUser = true;
        }
    },
    /*validate password*/
    passwordValidate: function (passwordValue) {
        if (!this.passwordPattern.test(passwordValue)) {
            $('.pwd_err').css('display', 'block');
            $('.login_pwd').css('boxShadow', '0 0 2px 1px #f00');
            $('.loginCnt').height(398 + 'px');
            $('.loginBox').height(264 + 'px');
            if (passwordValue == null) {
                $('.pwd_mis').css('display', 'block');
                $('.loginCnt').height(398 + 'px');
                $('.loginBox').height(264 + 'px');

            }
            $('.login_pwd').on('focus', function () {
                $('.pwd_err').css('display', 'none');
                $('.pwd_mis').css('display', 'none');
                $(this).css('boxShadow', '0 0 2px 1px #29bdb9');
                $('.loginCnt').height(342 + 'px');
                $('.loginBox').height(215 + 'px');
            });
            flagPwd = false;
        } else {
            flagPwd = true;
        }
    }

};

/*validate function*/
function validateFun(form, dom, type) {
    $('.submit').on('click', function () {
        if (type == 0) {
            Validate.usernameValidate($(dom).val());
        }
        if (type == 1) {
            Validate.passwordValidate($(dom).val());
        }
    });
}
validateFun('.login_form', '.login_text', 0);
validateFun('.login_form', '.login_pwd', 1);

$('.submit').on('click', function () {
    if (flagUser && flagPwd) {
        $.ajax({
            type: 'post',
            url: '../LoginServlet',
            data: $('.login_form').serialize(),
            success: function (data) {
                if (data == 1) {
                    remStatus();
                    swal({
                        title: "登录成功!",
                        text: '点击跳转到答题页面',
                        type: 'success',
                        closeOnConfirm: false,
                        animation: "slide-from-top",
                        showLoaderOnConfirm: true,
                    }, function () {
                        setTimeout(function () {
                            window.location.href = 'http://localhost:8080/userPage/program.html';
                        },2000);
                    });
                } else if (data == -1) {
                    swal({
                        title: "密码错误!",
                        text: '点击重新输入',
                        type: 'error',
                        closeOnConfirm: true,
                        animation: "slide-from-top"
                    });
                } else if (data == 0) {
                    swal({
                        title: "用户不存在!",
                        text: '点击确定进入注册页面',
                        type: 'error',
                        closeOnConfirm: false,
                        showCancelButton: true,
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        animation: "slide-from-top",
                        showLoaderOnConfirm: true,
                    }, function () {
                        setTimeout(function () {
                            window.location.href = 'http://localhost:8080/userPage/register.html';
                        }, 2000);
                    });
                }
            }
        })
    }
});

$(function () {
    if (document.cookie) {
        swal({
            title: "您已登录!",
            text: '点击进入答题界面',
            type: 'success',
            closeOnConfirm: false,
            showCancelButton: true,
            cancelButtonText: "取消",
            confirmButtonText: "确定",
            animation: "slide-from-top",
            showLoaderOnConfirm: true,
        }, function () {
            setTimeout(function () {
                window.location.href = "http://localhost:8080/userPage/program.html";
            }, 2000);
        });
    }
});

//记住登录状态
function remStatus() {
    var user = $('.login_text').val(),
        pwd = $('.login_pwd').val();

    if ($('.remember').is(':checked')) {
        console.log('cookie');
    }
    setCookie(user, pwd);
}

//设置cookie
function setCookie(name, value) {
    var Days = 30;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}
//读取cookies
function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");

    if (arr = document.cookie.match(reg))

        return unescape(arr[2]);
    else
        return null;
}

//删除cookies
function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}