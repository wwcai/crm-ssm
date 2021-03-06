<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});

        $("#remarkBody").on("mouseover",".myHref",function(){
            $(this).children("span").css("color","#FF0000");
        })
        $("#remarkBody").on("mouseout",".myHref",function(){
            $(this).children("span").css("color","#E6E6E6");
        })
		
		// 页面加载完毕后，展现该市场活动相关联的备注信息列表
		showRemarkList();

        $("#remarkBody").on("mouseover",".remarkDiv",function(){
            $(this).children("div").children("div").show();
        })
        $("#remarkBody").on("mouseout",".remarkDiv",function(){
            $(this).children("div").children("div").hide();
        })

        // 为保存按钮添加事件
        $("#saveRemarkBtn").click(function () {
            $.ajax({
                url : "workbench/activity/saveRemark.do",
                data : {
                    "noteContent" : $.trim($("#remark").val()),
                    "activityId" : "${a.id}"
                },
                type : "post",
                dataType : "json",
                success : function (data) {

                    /*
                        data
                            {"success" : true/false,"ar":{备注}}
                     */
                    if(data.success) {
                        // 添加成功

                        // 清空textarea中的信息
                        $("#remark").val("");

                        // 在textarea上方添加一个新的div
                        var html = "";
                        html += '<div id="' + data.ar.id +
                            '" class="remarkDiv" style="height: 60px;">';
                        html += '<img title="zhangsan" src="image/user-thumbnail.png"style="width: 30px; height:30px;">';
                        html +=
                            '<div style="position: relative; top: -40px; left: 40px;" >';
                        html += '<h5 id="e' + data.ar.id + '">' +
                        data.ar.noteContent + '</h5>';
                        html +=
                            '<font color="gray">市场活动</font> <font color="gray">-</font> <b>${a.name}</b> <small style="color: gray;" id="e' + data.ar.id + '"> ' + data.ar.createTime + ' 由' + data.ar.createBy + '</small>';
                        html +=
                            '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
                        html +=
                            '<a class="myHref" href="javascript:void(0);"onclick="editRemark(\'' + data.ar.id + '\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>';
                        html += '&nbsp;&nbsp;&nbsp;&nbsp;';
                        html +=
                            '<a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\'' + data.ar.id + '\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>';
                        html += '</div>';
                        html += '</div>';
                        html += '</div>';

                        $("#remarkAdd").append(html);

                    } else {
                        alert("添加备注失败")
                    }

                }
            })

        })

        // 为备注更新按钮绑定事件
        $("#updateRemarkBtn").click(function () {
            var id = $("#remarkId").val();

            $.ajax({
                url : "workbench/activity/updateRemark.do",
                data : {

                    "noteContent" : $.trim($("#noteContent").val()),
                    "id" : id

                },
                type : "post",
                dataType : "json",
                success : function (data) {

                    /*
                        data
                            {"success":true/false, "ar":{备注}}
                     */
                    if(data.success) {
                    // 更新div中相应的信息 noteContent editTimt, editBy
                        $("#e" + id).html(data.ar.noteContent);
                        $("#s" + id).html(data.ar.editTime  + '由' +
                            data.ar.editBy);
                        // 更新后关闭模态窗口
                        $("#editRemarkModal").modal("hide");

                    } else {
                        alert("修改备注失败")
                    }

                }
            })

        })

        // 为编辑按钮添加事件，打开编辑的模态窗口
        $("#editBtn").click(function () {

            datetimepicker();

            $.ajax({
                url : "workbench/activity/getUserListAndActivity.do",
                data : {
                    "id" : "${a.id}"
                },
                type : "get",
                dataType : "json",
                success : function (data) {

                    /*
                         data
                             用户列表
                             市场活动对象

                             {"uList":[{},{},{}],"a":{市场活动}}
                     */
                    // 处理所有者下拉框
                    var html = "<option></option>";

                    $.each(data.uList, function (i, n) {

                        html += "<option value='" + n.id + "'>" + n.name +
                            "</option>";
                    })
                    $("#edit-owner").html(html);

                    // 处理单条activity
                    $("#edit-id").val(data.a.id);
                    $("#edit-name").val(data.a.name);
                    $("#edit-owner").val(data.a.owner);
                    $("#edit-startDate").val(data.a.startDate);
                    $("#edit-endDate").val(data.a.endDate);
                    $("#edit-cost").val(data.a.cost);
                    $("#edit-description").val(data.a.description);

                    // 打开修改操作的模态窗口
                    $("#editActivityModal").modal("show");

                }
            })


        })

        // 为更新按钮绑定事件
        $("#updateBtn").click(function () {

            $.ajax({
                url : "workbench/activity/update.do",
                data : {

                    "owner" : $.trim($("#edit-owner").val()),
                    "id" : $.trim($("#edit-id").val()),
                    "name" : $.trim($("#edit-name").val()),
                    "startDate" : $.trim($("#edit-startDate").val()),
                    "endDate" : $.trim($("#edit-endDate").val()),
                    "cost" : $.trim($("#edit-cost").val()),
                    "description" : $.trim($("#edit-description").val())

                },
                type : "post",
                dataType : "json",
                success : function (data) {
                    /*
                        data
                            {"success":true/false}
                     */

                    if(data.success) {


                        // 关闭添加操作的模态窗口
                        $("#editActivityModal").modal("hide");

                        window.location.reload();


                    } else {
                        alert("修改信息失败！");
                    }
                }
            })
        })

        // 为删除按钮绑定事件
        $("#deleteBtn").click(function () {

            if(confirm("你要确定删除该信息吗？")){
                // url:workbench/activity/delete.do?id=xxx&id=xxx...
                // 拼接参数

                $.ajax({
                    url : "workbench/activity/delete.do",
                    data : {
                       "id": "${a.id}"
                    },
                    type : "post",
                    dataType : "json",
                    success : function (data) {
                        /*
                            data
                                {"success":true/false}
                         */

                        if(data.success) {
                            // 删除成功
                            window.location.replace("workbench/activity/index.jsp");

                        } else {
                            alert("删除失败");
                        }
                    }
                })
            }

        })

    });
	
	function showRemarkList() {

		$.ajax({
			url : "workbench/activity/getRemarkListByAid.do",
			data : {
				"activityId" : "${a.id}"
			},
			type : "get",
			dataType : "json",
			success : function (data) {

				/*
				 	data
				 		[{备注1},{备注2}]
				 */

                /*
                       javascript:void(0)
                        将超链接禁用，只能以触发事件的方式来操作
                 */

				var html = "";
				$.each(data, function (i, n) {

				html += '<div id="' + n.id +
                    '" class="remarkDiv" style="height: 60px;">';
				html += '<img title="zhangsan" src="image/user-thumbnail.png"style="width: 30px; height:30px;">';
				html +=
						'<div style="position: relative; top: -40px; left: 40px;" >';
				html += '<h5 id="e' + n.id + '">' + n.noteContent + '</h5>';
				html +=
						'<font color="gray">市场活动</font> <font color="gray">-</font> <b>${a.name}</b> <small style="color: gray;" id="s' + n.id + '"> ' + (n.editFlag == 0 ? n.createTime : n.editTime) + ' 由' + (n.editFlag == 0 ? n.createBy : n.editBy) + '</small>';
				html +=
						'<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
				html +=
						'<a class="myHref" href="javascript:void(0);" onclick="editRemark(\'' + n.id + '\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>';
				html += '&nbsp;&nbsp;&nbsp;&nbsp;';
				html +=
						'<a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\'' + n.id + '\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>';
				html += '</div>';
				html += '</div>';
				html += '</div>';
				})

				$("#remarkDiv").before(html);

            }
		})
	}
	
	function deleteRemark(id) {
        $.ajax({
            url : "workbench/activity/deleteRemark.do",
            data : {
                "id" : id
            },
            type : "post",
            dataType : "json",
            success : function (data) {

                /*
                      data
                        {"success":true/false}
                 */

                if(data.success) {

                    // 删除后页面会保留删除前原有的数据
                    //showRemarkList();

                    $("#" + id).remove();
                } else {
                    alert("删除备注失败")
                }
            }
        })
    }


    function editRemark(id) {

	    // 模态窗口中隐藏域id赋值
        $("#remarkId").val(id);

	    // 找到指定的存放备注信息的<h5>标签
        var noteContent = $("#e" + id).html();

        // 将h5中展现出来的信息赋给修改模态窗口的文本框中
        $("#noteContent").val(noteContent);

	    $("#editRemarkModal").modal("show");

    }

    function datetimepicker() {
        $(".time").datetimepicker({
            minView: "month",
            language:  'zh-CN',
            format: 'yyyy-mm-dd',
            autoclose: true,
            todayBtn: true,
            pickerPosition: "bottom-left"
        });
    }

</script>

</head>
<body>
	
	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<%-- 备注的id --%>
		<input type="hidden" id="remarkId">
        <div class="modal-dialog" role="document" style="width: 40%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改备注</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="edit-describe" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="noteContent"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 修改市场活动的模态窗口 -->
    <div class="modal fade" id="editActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改市场活动</h4>
                </div>
                <div class="modal-body">

                    <form class="form-horizontal" role="form">
                        <input type="hidden" id="edit-id">
                        <div class="form-group">
                            <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <select class="form-control" id="edit-owner">

                                </select>
                            </div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control"
                                       id="edit-name" >
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-startTime"
                                   class="col-sm-2 control-label ">开始日期
                            </label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control time"
                                       id="edit-startDate" readonly>
                            </div>
                            <label for="edit-endTime"
                                   class="col-sm-2 control-label ">结束日期
                            </label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control time"
                                       id="edit-endDate" readonly >
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-cost" >
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3"
                                          id="edit-description"></textarea>
                            </div>
                        </div>

                    </form>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary"
                            id="updateBtn">更新
                    </button>
                </div>
            </div>
        </div>
    </div>

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.location.href=document.referrer;"><span
                class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>市场活动-${a.name} <small>${a.startDate} ~ ${a.endDate}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<button type="button" id="editBtn" class="btn btn-default"
                    ><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" id="deleteBtn" class="btn btn-danger"><span
                    class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top:
			-20px;"><b>${a.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top:
			-60px;"><b>${a.name}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>

		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top:
			-20px;"><b>${a.startDate}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top:
			-60px;"><b>${a.endDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">成本</div>
			<div style="width: 300px;position: relative; left: 200px; top:
			-20px;"><b>${a.cost}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top:
			-20px;"><b>${a.createBy}&nbsp;&nbsp;</b><small
			style="font-size: 10px; color: gray;">${a.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top:
			-20px;"><b>${a.editBy}&nbsp;&nbsp;</b><small
			style="font-size: 10px; color: gray;">${a.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${a.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div id="remarkBody" style="position: relative; top: 30px; left: 40px;">
		<div id="remarkAdd" class="page-header">
			<h4>备注</h4>
		</div>
		
		<!-- 备注1 -->
		<%--<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>--%>
		
		<!-- 备注2 -->
		<%--<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		--%>
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary"
                            id="saveRemarkBtn">保存</button>
				</p>
			</form>
		</div>
	</div>
	<div style="height: 200px;"></div>
</body>
</html>