# 定义全局统一异常处理
* 定义基础接口 IBaseError
* 定义状态枚举 ExceptionEnum
* 定义业务异常处理 CMSExceptionHandler
* 定义基础响应输出 BaseResponse
* 定义全局异常处理 GlobalExceptionHandler

# 继承Springboot jdbcTemplate模板连接mysql数据库
* 添加pom依赖
* 配置文件定义，连接数据库，修改application.yml文件
* 添加controller
* 添加service
* 添加实体类entity
* 添加数据库操作对象repository(dao)
* 注入jdbcTemplate，实现sql语句查询
