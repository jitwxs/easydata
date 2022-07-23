<h1 align="center">easydata</h1>
<p align="center">致力于 Java 一站式数据处理，实现数据的加载,构造,转换,验证...</p>
<p align="center">Dedicated to Java one-stop data processing, support data load, mock, convert, verify...</p>
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

[快速开始 Quick start](https://github.com/jitwxs/easydata/wiki/Quick-Start) | [文档 Document](https://github.com/jitwxs/easydata/wiki) | [变更记录 Changelog](https://github.com/jitwxs/easydata/releases) | [代码示例 Code examples](https://github.com/jitwxs/easydata/tree/master/easydata-sample) | [报告问题 Report an issue](https://github.com/jitwxs/easydata/issues/new)

## Feature

easydata 的核心由以下几部分所构成：

**数据加载 Data Loading**

Data Loading 模块期望能够以更轻量级的方式读取本地文件或数据库中的结构性数据，并转换成 Java Bean。

The Data Loading module expects to be able to read structural data from a local file or database and convert it into Java beans in a more lightweight api.

- [x] 本地 CSV, JSON 文件加载 loading local csv, json file
- [x] 字符串数据加载 loading string data
- [x] 远程 MySQL 查询结果加载 loading remote mysql query result

**数据构造 Data Mock**

数据构造模块用于生成一个对象，并填充它的属性，这在单元测试中十分有用。

The Data Mock module is used to generate a mock object, which is often useful in unit testing.

- [x] 任意 java 或 protobuf 类型的对象生成 Object generation of any java or protobuf type
- [x] 支持数组、集合、泛型等复杂类型对象生成 Support for generating complex type objects such as arrays, collections, generics, and so on
- [x] 字符串数据支持个性化数据生成，例如：邮箱、身份证号、姓名等 String data supports personalized data generation, such as mailboxes, ID numbers, names, and so on

**数据转换 Data Convert**

Data Convert 模块实现将任意对象转换成另一类型的对象，如果它支持的话。

The Data Convert module implements converting any object into another type of object, if it is supported.

(4) 数据验证 Data Verify

Data Verify 模块是一个断言框架，基于 assertj 实现，但拥有更轻量级的 API 和更丰富的功能。

The Data Verify module is an assert framework, based on the [assertj](https://github.com/assertj/assertj-core) implementation, but with a lighter-weight API and richer functionality.

- [x] 任意两个对象（支持异构）的对象比较 An object comparison of any two objects that support heterogeneity
- [x] 数值类型对象比较，支持自定义精度误差 Numerical type object comparison, support custom precision error
- [x] 支持 MyBatis Mapper 文件、SQL 语句静态校验 Support MyBatis Mapper files, static validation of SQL statements

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

当您遇到问题，请在 issues 中提问，如果您有意愿贡献代码，也十分欢迎。

When you encounter a problem, please ask questions in issues, if you want to contribute the code, that also very welcome.

step1: 拷贝仓库 Fork repository.

step2: 贡献代码，需要包括单元测试 Contribution code, with unit test.

```bash
mvn clean package -Dmaven.test.skip=false -Pdevelop -B --file easydata/pom.xml
```

step3: 提交 PR 到 master 分支，等待管理员审批 Pull request to master branch, waiting administrator approval.

## Reference

本项目部分功能借鉴以下项目 Some feature of this project reference other projects:

- https://github.com/jsonzou/jmockdata
- https://github.com/binarywang/java-testdata-generator
- https://github.com/j-easy/easy-random

## Donate

如果本项目为您带来方便，欢迎 Star 来让更多人发现和使用它。本项目为个人维护项目，如果您愿意请作者喝一瓶可乐的话，欢迎打赏。

If this project brings you convenience, welcome to star to let more people discover and use it. This project is a personal maintenance project. If you are willing to ask the author to drink a bottle of cola, welcome to donate.

>如您选择打赏，记得备注您的昵称，我将为您登记到本页面中。
>
>If you choose to donate, remember to note your nickname, I will register to this page.

<div align="center">
    <img src="https://cdn.jsdelivr.net/gh/jitwxs/cdn/blog/configuration/alipay_donate_full.jpg" height="200" style="float:left;margin-right:20px;margin-left: 310px" alt="alipay_donate">
    <img src="https://cdn.jsdelivr.net/gh/jitwxs/cdn/blog/configuration/wechat_donate_full.jpg" height="200" style="float:left" alt="wechat_donate">
</div> 
