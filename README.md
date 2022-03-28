## Addax

[![Release](https://img.shields.io/github/v/release/jitwxs/addax.svg)](https://github.com/jitwxs/addax/releases)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

### 前言

在日常工作中，不知道你有没有遇到过这些场景：

- 老板要求对数据库数据做一下加工，自己的 SQL 能力还不强，要是能把这些数据放到内存里，写个代码加工下就好了。
- 写单测光造数据就要花好多时间，特别是对象，还得一个个的塞属性，要是有工具能帮我生成一个随机的对象就好了。
- 单测结果的校验也太累了，为了验证期望对象和实际对象一致，每一个字段都得写一个断言。
- 单测结果中，有些字段的计算过程是低精度的，要是能指定一个精度差就好了。
- 分布式系统中，数据在传输过程中要序列化和反序列化。以 protobuf 为例，必须要 bean --> proto --> bean，如何保证后续需求中 bean 加了字段，而 convert 方法不会忘记加字段呢。
- Mybatis Mapper XML，总是会出现新加了字段，而 resultMap 或者 columns sql 漏了这个字段的情况，有什么办法能确保不会忘记加呢。
- Mybatis Mapper XML 中，写了个 SQL 语句，结果犯了个语法错误，导致开发时间又被浪费了一点，有什么办法能提早发现这个问题呢。

如果有的话，是不是已经血压升高了，毕竟写需求就已经很痛苦了，还得做这些劳心却没有技术含量的重复工作。为了解决这些问题，`Addax` 应运而生。花点时间往下了解一下，欢迎 Star 或 Issue 来一起支持和改进它吧。

[![Stargazers over time](https://starchart.cc/jitwxs/addax.svg)](https://starchart.cc/jitwxs/addax) 

### 主要功能

> 详细 WIKI 施工中

**数据加载模块**

- [x] 本地 CSV, JSON 文件解析
- [x] MySQL SELECT 查询结果解析
- [x] 本地 INSERT SQL 解析

**数据生成模块**

- [x] 基本数据类型、自定义对象，数据生成
- [x] Map、List 等集合、泛型类型数据生成
- [x] 支持生成 protobuf 类型数据
- [x] 个性化字符串数据生成【邮箱、身份证号、姓名等】

**数据验证模块**

- [x] 支持基本数据类型比较，自定义指定精度误差
- [x] 支持相同类型对象（普通 Java 对象、protobuf 对象）间比较
- [x] 支持不同类型的对象间比较
- [x] 支持 Map、List 等集合类型对象比较

**Mybatis 验证模块**

- [ ] 支持校验 ResultMap 和查询字段是否完全匹配
- [ ] 支持校验 ResultMap 与对应 POJO 实体字段是否完全匹配
- [ ] 支持简单的 SQL 语句静态校验

**数据转换模块**

- [x] 支持 String 类型与基本数据类型、普通对象的转换
- [x] 对 protobuf 对象的支持

#### 项目参考

Addax 参考了以下项目的代码:

- https://github.com/jsonzou/jmockdata
- https://github.com/binarywang/java-testdata-generator

