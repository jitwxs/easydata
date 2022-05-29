<h1 align="center">easydata</h1>
<p align="center">在单元测试中更容易的处理数据，加载 / Mock / 转换 / 验证全流程覆盖...</p>
<p align="center">Easy process data in unit test, load/mock/convert/verify...</p>
<p align="center">
  <a href="https://github.com/jitwxs/easydata/actions">
    <img src="https://github.com/jitwxs/easydata/actions/workflows/build.yml/badge.svg?branch=master" />
  </a>
  <a href="https://mvnrepository.com/artifact/io.github.jitwxs/easydata">
    <img alt="mvnrepository" src="https://badgen.net/maven/v/maven-central/io.github.jitwxs/easydata" />
  </a>
  <a href="#">
    <img alt="license" src="https://badgen.net/github/license/jitwxs/easydata" />
  </a>
  <a href="#">
    <img alt="star" src="https://badgen.net/github/stars/jitwxs/easydata" />
  </a>
  <a href="#">
    <img alt="forks" src="https://badgen.net/github/forks/jitwxs/easydata" />
  </a>
</p>
<hr/>

[快速开始 Quick start](https://github.com/jitwxs/easydata/wiki/Quick-Start) | [文档 Documentation](https://github.com/jitwxs/easydata/wiki) | [变更记录 Changelog](https://github.com/jitwxs/easydata/releases) | [代码示例 Code examples](https://github.com/jitwxs/easydata/tree/master/easydata-sample) | [报告问题 Report an issue](https://github.com/jitwxs/easydata/issues/new)

## Feature

- [x] 本地 CSV, JSON 文件解析
- [x] 内存字符串数据解析
- [x] 远程 MySQL SELECT 查询结果解析
- [x] 任意类型对象生成
- [x] Map, List 等集合、泛型类型对象生成
- [x] 个性化字符串数据生成【邮箱、身份证号、姓名等】
- [x] 任意两个对象（支持异构）的对象比较
- [x] 数值类型对象比较，支持自定义精度误差
- [x] 支持任意对象的类型转换
- [ ] 支持校验 ResultMap 和查询字段是否完全匹配
- [ ] 支持校验 ResultMap 与对应 POJO 实体字段是否完全匹配
- [ ] 支持简单的 SQL 语句静态校验

## Design Background

在日常工作中，不知道你有没有遇到过这些场景：

- 老板要求对数据库数据做一下加工，自己的 SQL 能力还不强，要是能把这些数据放到内存里，写个代码加工下就好了。
- 写单测光造数据就要花好多时间，特别是对象，还得一个个的塞属性，要是有工具能帮我生成一个随机的对象就好了。
- 单测结果的校验也太累了，为了验证期望对象和实际对象一致，每一个字段都得写一个断言。
- 单测结果中，有些字段的计算过程是低精度的，要是能指定一个精度差就好了。
- 分布式系统中，数据在传输过程中要序列化和反序列化。以 protobuf 为例，必须要 bean --> proto --> bean，如何保证后续需求中 bean 加了字段，而 convert 方法不会忘记加字段呢。
- Mybatis Mapper XML，总是会出现新加了字段，而 resultMap 或者 columns sql 漏了这个字段的情况，有什么办法能确保不会忘记加呢。
- Mybatis Mapper XML 中，写了个 SQL 语句，结果犯了个语法错误，导致开发时间又被浪费了一点，有什么办法能提早发现这个问题呢。

如果有的话，是不是已经血压升高了，毕竟写需求就已经很痛苦了，还得做这些劳心却没有技术含量的重复工作。为了解决这些问题，`easydata` 应运而生。花点时间往下了解一下，欢迎 Star 或 Issue 来一起支持和改进它吧。

## Contribution

当您遇到问题，请在 issuse 中提问，如果您有意愿贡献代码，也十分欢迎。

When you encounter a problem, please ask questions in ISSUSE, if you want to contribute the code, that also very welcome.

step1: 拷贝仓库 Fork repositary.

step2: 贡献代码，需要包括单元测试 Contrubution code, with unit test.

```bash
mvn clean package -Dmaven.test.skip=false -Pdevelop -B --file easydata/pom.xml
```

step3: 提交 PR 到 master 分支，等待管理员审批 Pull request to master branch, wating administrator approval.

## Reference

本项目部分功能借鉴以下项目 Some feature of this project reference other projects:

- https://github.com/jsonzou/jmockdata
- https://github.com/binarywang/java-testdata-generator

## Donate

如果本项目为您带来方便，欢迎 Star 来让更多人发现和使用它。本项目为个人维护项目，如果您愿意请作者喝一瓶可乐的话，欢迎打赏。

If this project brings you convenience, welcome to star to let more people discover and use it. This project is a personal maintenance project. If you are willing to ask the author to drink a bottle of cola, welcome to donate.

>如您选择打赏，记得备注您的昵称，我将为您登记到本页面中。
>
>If you choose to donate, remember to note your nickname, I will register to this page.

<div align="center">
    <img src="https://cdn.jsdelivr.net/gh/jitwxs/cdn/blog/configuration/alipay_donate_full.jpg" height="200" style="float:left;margin-right:20px;margin-left: 310px">
    <img src="https://cdn.jsdelivr.net/gh/jitwxs/cdn/blog/configuration/wechat_donate_full.jpg" height="200" style="float:left">
</div> 
