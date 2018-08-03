var elt = $('#tag');
var getTagUrl = elt.data("url");
var form = $("#suggestionForm");
$.get(getTagUrl, function(result){
    if(result &&  result.length > 0){
        elt.select2({
            data:$.map(result,function(item){
                return {
                    id:item.value,
                    text:item.text
                }
            })
        });
    }
}, "json");
form.bootstrapValidator({
    message: '输入值不合法',
    live:"submitted",
    feedbackIcons: {
        valid: 'glyphicon glyphicon-ok',
        invalid: 'glyphicon glyphicon-remove',
        validating: 'glyphicon glyphicon-refresh'
    },
    excluded: [':disabled'],
    submitButtons:"#suggestionButton",
    fields: {
        title: {
            message: '标题不合法',
            validators: {
                notEmpty: {
                    message: '标题不能为空'
                },
                stringLength: {
                    min: 3,
                    max: 30,
                    message: '请输入3到30个字符'
                },
                regexp: {
                    regexp: /^[a-zA-Z0-9_\. \u4e00-\u9fa5 ]+$/,
                    message: '用户名只能由字母、数字、点、下划线和汉字组成 '
                }
            }
        }, tag: {
            validators: {
                notEmpty: {
                    message: '分类不能为空'
                }
            }
        }, content: {
            validators: {
                notEmpty: {
                    message: '内容不能为空'
                },
                stringLength: {
                    min: 3,
                    message: '请输入3以上个字符'
                },
            }
        }
    }
}).on('success.form.bv', function(e) {
    e.preventDefault();
    $("#suggestionButton").prop("disabled",true);
    form.ajaxSubmit({
        success: function(data){
             data = eval("(" + data + ")");
             if(data.code === 1){
               form.find('.alert').addClass("alert-success").html(data.data).show();
             }else{
               form.find('.alert').addClass('alert-danger').html(data.data).show();
             }
        },
        error: function(XmlHttpRequest, textStatus, errorThrown){
            var json = XmlHttpRequest.responseJSON;
            if(json){
                var message = json.message;
                form.find('.alert').addClass('alert-danger').html(message).show();
            }
        }
    })
});
