# code-generator
代码生成器，生成的代码所需要的依赖有：springboot、mybatis-plus、lombok、mapstruct等框架

# 使用方法
- 直接运行启动类，访问http://localhost:8888
- 在网页上填写数据库信息和配置信息，连接数据库后选择需要生成的表对应的代码

# 生成的代码目录
```text
main
    |-java
        |-包名
            |-controller
            |-mapper
            |-model
                |-convert
                |-dto
                |-entity
            |-service
                |-impl
    |-resources
        |-mapper
```
