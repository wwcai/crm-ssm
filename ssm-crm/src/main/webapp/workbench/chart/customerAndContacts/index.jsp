<%--
  Created by IntelliJ IDEA.
  User: 大白菜
  Date: 2021/3/31
  Time: 10:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";


%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>

    <script src="ECharts/echarts.min.js"></script>
    <script src="jquery/jquery-1.11.1-min.js"></script>

    <script type="text/javascript">

        $(function () {

            getCharts();

        })
        
        function getCharts() {

            $.ajax({
                url : "workbench/customer/getCharts.do",
                data : {

                },
                type : "get",
                dataType : "json",
                success : function (data) {

                    /*
                        data
                            {"total":100,"dataList":[{{value: 60, name: '访问'}},{{value: 60, name: '访问'}}]}
                     */




                    // 基于准备好的dom，初始化echarts实例
                    var myChart = echarts.init(document.getElementById('main'));

                    // 指定图表的配置项和数据(要画的图)
                    var option = {
                        title: {
                            text: '客户类型饼状图',
                            subtext: '统计客户类型数量',
                            left: 'center'
                        },
                        tooltip: {
                            trigger: 'item'
                        },
                        legend: {
                            orient: 'vertical',
                            left: 'left',
                        },
                        series: [
                            {
                                name: '访问来源',
                                type: 'pie',
                                radius: '50%',
                                data: data.dataList,
                                emphasis: {
                                    itemStyle: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                                    }
                                }
                            }
                        ]
                    };

                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);
                }
            })



        }
    </script>
</head>
<body>

    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="main" style="width: 600px;height:400px;"></div>

</body>
</html>
