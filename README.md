# code-generator
代码生成器，生成的代码所需要的依赖有：springboot、mybatis-plus、lombok、mapstruct

# 使用方法
- 修改application.yml中的数据源信息（仅支持mysql数据库）
- 修改generator.properties中的包名、表前缀、作者信息
- 运行启动类，访问http://localhost:8888，选择需要生成的表对应的代码

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
