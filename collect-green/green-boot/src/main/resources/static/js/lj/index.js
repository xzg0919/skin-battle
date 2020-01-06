//时间选择控件
layui.use('laydate', function() {
  var laydate = layui.laydate;

  //执行一个laydate实例
  laydate.render({
    elem: '#test1', //指定元素
  });

  laydate.render({
    elem: '#test2', //指定元素
  });

  laydate.render({
    elem: '#test3', //指定元素
  });

  laydate.render({
    elem: '#test4', //指定元素
  });
  //年选择器
  laydate.render({
    elem: '#test',
    type: 'year' //month/date/time/datetime
  });

  //范围
  laydate.render({
    elem: '#test',
    type: 'year', //month/  /time/datetime
    range: true //或 range: '~' 来自定义分割字符
  });

  //格式
  laydate.render({
    elem: '#test',
    format: 'yyyy年MM月dd日' //可任意组合
  });

});


//搜索下拉框
layui.use(['form', 'layedit', 'laydate'], function () {
  var form = layui.form,
    layer = layui.layer,
    layedit = layui.layedit,
    laydate = layui.laydate;

  //创建一个编辑器
  var editIndex = layedit.build('LAY_demo_editor');

  //自定义验证规则
  form.verify({
    title: function (value) {
      if (value.length < 5) {
        return '标题至少得5个字符啊';
      }
    },
    pass: [/(.+){6,12}$/, '密码必须6到12位'],
    content: function (value) {
      layedit.sync(editIndex);
    }
  });

  //监听指定开关
  form.on('switch(switchTest)', function (data) {
    layer.msg('开关checked：' + (this.checked ? 'true' : 'false'), {
      offset: '6px'
    });
    layer.tips('温馨提示：请注意开关状态的文字可以随意定义，而不仅仅是ON|OFF', data.othis)
  });

  //监听提交
  form.on('submit(demo1)', function (data) {
    layer.alert(JSON.stringify(data.field), {
      title: '最终的提交信息'
    })
    return false;
  });
});