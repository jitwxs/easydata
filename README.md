## Addax

[![Release](https://img.shields.io/github/v/release/jitwxs/addax.svg)](https://github.com/jitwxs/addax/releases)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

为简化测试用例开发而生，基于 Java SE 开发，引入 Jar 包即可使用。

### 使用指南

使用文档将在主要功能开发完毕且稳定后编写，如您有需要，可查看 `addax-sample` 项目测试用例。

### 开发进度

目前处于 Beta 阶段，待主要功能稳定后发布 Release 版本。

(1) 公共模块 Publiv Module

- [x] 所有主要模块支持 SPI 的横向扩展

(2) 数据加载模块 Data Loader Module 

- [x] 支持本地 CSV 文件解析
- [ ] 支持本地 JSON 文件解析
- [x] 支持远程 SELECT SQL 语句执行结果
- [ ] 支持本地 INSERT 文件解析
  
(3) 数据转换模块 Data Convert Module

- [x] 支持全部基本数据类型与 String 的转换
- [ ] 支持常用原生数据类型与 String 的转换
- [x] 支持复杂对象与 String 的转换
- [x] 对 protobuf 对象的支持

(4) 数据生成模块 Data Mock Module

- [ ] 支持基本数据类型的生成
- [ ] 支持复杂对象的生成，自动填充属性
- [ ] 支持对集合、Map 的支持
- [ ] 对 protobuf 对象的支持

(5) 数据验证模块 Data Verify Module

- [ ] 支持基本数据类型间的比较
- [ ] 支持复杂对象的比较
- [ ] 支持对集合、Map 的对象支持
- [ ] 对 protobuf 对象的支持

