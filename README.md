
## What is APlugin ？

 APlugin 是一个基于[ASM](https://asm.ow2.io/) 和 [Gradle Transform API](http://tools.android.com/tech-docs/new-build-system/transform-api)的在安卓项目构建过程中处理字节码的工具包
 > APlugin is based on [ASM](https://asm.ow2.io/) and [Gradle Transform API](http://tools.android.com/tech-docs/new-build-system/transform-api) A toolkit for handling bytecode during android project construction.

## Features
+ [Router](https://github.com/fanrunqi/APlugin/blob/master/ROUTER_README.md): 完全通过asm实现路由跳转、全类型参数注入、拦截器配置的安卓路由工具
> An Android tool that implements routing jump, all types of parameter injection, and interceptor configuration completely through asm

+ [Mock](https://github.com/fanrunqi/APlugin/blob/master/MOCK_README.md): 基于[okhttp](https://github.com/square/okhttp)的零代码侵入的网络请求mock工具
> Network request mock tool based on [okhttp](https://github.com/square/okhttp) with zero code intrusion.

## How to use 
+ 在根目录的build.gradle中添加插件
> Add the plugin in the build.gradle of the root directory
```
buildscript {
    repositories {
        maven {
            url 'https://jitpack.io'
        }
    }
    dependencies {
        classpath 'com.github.fanrunqi.APlugin:aplugin:0.08'
    }
}

```
然后点击Features中具体功能，查看如何使用
> Then click on the specific function in Features to see usage details

## License

    Copyright 2021 fanrunqi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
