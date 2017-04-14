/**
 * Created by Jackie Zhang on 2017/2/13.
 */

$(function () {
    //使用说明
    $('.issue').on('click', function () {
        swal({
            title: "编辑器说明",
            text: '类名必须为Main,不能引入第三方包，目前jdk为1.8。',
            type: 'info',
            closeOnConfirm: true,
            animation: "slide-from-top"
        });
    });
    //代码清空
    $('.reset').on('click', function () {
        swal({
            title: "清空代码!",
            text: '点击确定清空代码',
            type: 'error',
            closeOnConfirm: false,
            showCancelButton: true,
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            animation: "slide-from-top",
            showLoaderOnConfirm: true
        });
    });
    //代码高亮处理
    var editor = CodeMirror.fromTextArea($("#edit_text")[0], { //script_once_code为你的textarea的ID号
        lineNumbers: true,//是否显示行号
        mode: "text/x-java",　//默认脚本编码
        lineWrapping: true, //是否强制换行
    });
    editor.setValue('//初次使用，请查看右上角使用说明' + '\n' + 'class Main {' + '\n' + '\n' +
        '}');
    editor.setOption('theme', 'panda-syntax');
    editor.setSize('auto', '100%');


    $('.list').on('mouseover', function () {
        $('.proList').css('display', 'block');
        $('.proList').on('mouseover', function () {
            $('.proList').css('display', 'block');
        });
    });
    $('.list').on('mouseout', function () {
        $('.proList').css('display', 'none');
        $('.proList').on('mouseout', function () {
            $('.proList').css('display', 'none');
        });
    });

    $('.edit_trans').on('click', function () {
        alert('攻城狮正在奋力开发其他语言的支持');
    });
    //获取当前题目
    fetchCurentSubject();
});

function fetchCurentSubject() {
    $.ajax({
        type: 'get',
        url: '../Subject',
        success: function (data) {
            console.log(data);
        }
    })
}

//调试保存
$('.debug').on('click', function () {
    var ajaxData = {'answer': editor.getValue()};
    console.log(ajaxData);
    /* $.ajax({
     type: 'post',
     url: '../Subject',
     data: ajaxData,
     success: function (data) {
     console.log('debug');
     },
     error: function () {
     console.log('debugerr');
     }
     });*/
});

