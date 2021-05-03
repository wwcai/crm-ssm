SSM 整合开发：
SSM：SpringMvc + Spring + Mybatis

SpringMvc：视图层，界面层，负责接收请求，显示处理结果
Spring:业务层，管理 service、dao 工具类对象
Mybatis：持久层，访问数据可

用户发起请求--SpringMvc 接收--Spring 中的 service 对象-- Mybatis 处理数据

SSM 整合也叫做 SSI （IBatis 是 Mybatis 的前生），整合中有容器。
    1、第一个容器：SpringMvc 管理 Controller 控制器对象的
    2、第二个容器：Spring 容器，管理 Service、Dao 工具类对象

我们要做的是把使用的对象交给合适的容器创建、管理。把 Controller 还有 web 开发的相关对象
交给 springmvc 容器，这些 web 用的对象写在 springmvc 配置文件中

service、dao 对象在定义 spring 的配置文件中，让 spring 管理这些对象

springmvc 容器 和 spring 容器是有关系的，关系已经确定好了
springmvc 容器是 spring 容器的子容器，类似于继承。子可以访问父的内容
在子容器中的 Controller 可以访问父容器中的 Service 对象，就可以实现 controller 使用 service 对象了

步骤：
    0、使用 springdb MySQL 库 表：student
    1、新建 maven web 项目
    2、加入依赖
        springmvc、spring、mybatis 三个框架的依赖，Jackson 依赖、mysql 驱动、druid 连接池
        jsp、servlet 依赖
    3、写 web.xml
        1）注册 DispathcerServlet 目的 ① 创建 springmvc 容器对象，才能创建 Controller 对象
                                     ② 创建 servlet，才能接收用户请求
        2）注册 spring 的监听器：ContextLoaderListener 目的：创建 spring 的容器对象，才能创建 service、dao 等对象
        3）注册字符集过滤器，解决 post 请求乱码问题

    4、创建包，controller 包，service、dao 实体类包名
    5、写 springmvc、spring、mybatis 的配置文件
        1）springmvc 配置文件
        2）spring 配置文件
        3）mybatis 主配置文件
        4）数据库的属性配置文件

    6、写代码，dao 接口和 mapper 文件，service 和实现类，controller，实体类
    7、jsp 页面
