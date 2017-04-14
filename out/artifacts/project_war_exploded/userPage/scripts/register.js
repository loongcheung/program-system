/**
 * Created by zhanglongyu on 2017/2/17.
 */

var usernamePattern = /^(\w){6,16}$/;
var passwordPattern = /^(\w){8,16}$/;


$('.register').on('click',validate);

function validate() {
    if (!usernamePattern.test($(".register_text").val())){
        $(".register_text").css('boxShadow', '0 0 2px 1px #f00');
        focusBlur('.register_text');
    }else if (!passwordPattern.test($(".register_pwd").val())){
        $(".register_pwd").css('boxShadow', '0 0 2px 1px #f00');
       focusBlur('.register_pwd');
    }else if ($('.register_pwd_rep').val()!=$('.register_pwd').val()){
        $(".register_pwd_rep").css('boxShadow', '0 0 2px 1px #f00');
       focusBlur('.register_pwd_rep');
       $('.register_pwd_rep').on('focus',function () {
           $('.pwd_rep').css('display','none');
       })
        $('.pwd_rep').css('display','block');
    }else {
        $.ajax({
            type: 'post',
            url : '../RegisterServlet',
            data: $('.register_form').serialize(),
            success: function (data) {
                if (data == 1){
                    alert ('注册成功');
                    location.href = "http://localhost:8080/userPage/login.html";
                }else if (data == -1){
                    alert ('用户名已经存在');
                }else if (data == 0) {
                    alert ('注册失败，发生异常');
                }
            }
        })
    }
}
//控制焦点
function focusBlur(dom,callback) {
    $(dom).on('focus',function () {
        $(this).css('boxShadow', '0 0 2px 1px #29bdb9');
    })
    $(dom).on('blur',function () {
        $(this).css('boxShadow', 'none');
    })
}
