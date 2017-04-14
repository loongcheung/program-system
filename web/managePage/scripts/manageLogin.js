/**
 * Created by Administrator on 2017/2/17.
 */
$('.submit').on('click', function () {

    if (!$('.user').val() || !$('.pwd').val()) {
        $('.alert').text('用户名或密码不能为空');
    } else {
        $('.alert').text(' ');
        var ajaxData = {
            username: $('.user').val(),
            password: $('.pwd').val()
        };
        $.ajax({
            type: 'post',
            url: '../ManageLogin',
            data: ajaxData,
            success: function (data) {
                if (data == 1) {
                    alert('登陆成功');
                    location.href = "http://localhost:8080/managePage/manageIndex.html";
                } else if (data == -1) {
                    $('.alert').text('密码有误');
                } else if (data == 0) {
                    $('.alert').text('不存在此用户');
                }
            }
        })
    }
});