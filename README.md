#
轻芒杂志开发笔记

Jsoup + DataBinding + GreenDao + Glide + MVP + RxJava

[访问我访问GitHub主页](https://github.com/langsky/QingMang)，或者我的[Gitbook](https://www.gitbook.com/book/langsky/qingmang-notes/details)。
---

免责声明：

* QingMang阅读内容来自[轻芒阅读](qingmang.me)，我不是内容生产商，我是内容搬运工。
* 本工程完全开源，仅作交流学习用，请勿用于任何商业用途。

---

## 我为什么要做这个App {#我为什么要做这个app}

从2015年7月毕业到现在，我的日常工作都以解bug为主，主要对公司的Android手机项目中的Setting模块进行debug，因此我想尝试除去debug，去享受无到有开发一个App的乐趣。另一方面也是对我一年多的工作进行一个小总结。

## 我为什么选择轻芒 {#我为什么选择轻芒}

之前我喜欢使用《豌豆荚一览》来阅读资讯，后来它改名叫轻芒阅读。我点击进入它的官方网站后，发现里面都很多优质的杂志内容，分析了一下网页源代码，除了在”继续阅读“时使用的动态网页，其它的都是静态网页，如果结合Jsoup就能将网页内容爬下来，自己制作一个App，感觉一定很棒。

## 我是怎么获取API的 {#我是怎么获取api的}

众所周知，API一般有两种形式，一种是json，一种是xml。两种形式的使用方法google一下都有非常好的博客进行说明，我这里就不介绍了。但是作为个人开发者，我是拿不到《轻芒杂志》的API的（或者说它根本就没有开放）。所以我采取最笨的方法，就是解析它的网站代码，来获取我需要的信息。

## 我为什么使用MVP {#我为什么使用mvp}

Android的App原始架构应该是基于MVC的，其中Activity承担了VC的职责，这样会使Activity的代码变得臃肿。

MVVM适用于M向V提供显示信息而同时对V的操作向M写入信息。这个框架不适合各种仅提供阅读功能的App。

我选择MVP模式一方面是因为V仅作为展示信息的一个平台，V对M的交互不是很多，M信息的获取几乎和V无关，所以MVP很好地适用与我的《轻芒阅读》。

关于MVP MVC MVVM框架的区别，可以Google一下，有很多优秀的博文讲解，我也会在后期对这个问题进行解读。

## 我为什么使用DataBinding {#我为什么使用DataBinding}

我使用的DataBinding是Google官方提供的（还有三方的实现，比如说butter knife），

DataBinding在新闻阅读类App开发中十分好用，在layout文件中稍微做下处理，就能节省大量的工作量，同时使用DataBinding便于解耦。

不过使用DataBinding也有一个局限，就是在自定义View时很容易出错。同时使用了DataBinding的项目，不能使用Java 8新特性-Lambda表达式。这一点真是太遗憾了。

## 我为什么使用Glide {#我为什么使用glide}

GitHub上提供了很多优秀的图片加载缓存工具，最著名的应该就是Glide和Picasso了。两者的使用方法类似，但是Glide由于可以传入Activity的Context，Glide将能播放Picasso不能播放的GIF图片，基于此我选择了它。同时它使我不必考虑复杂的异步加载图片的细节，以及各种缓存机制。当然，这些细节和机制我还是需要了解的。

## 我为什么使用ＲxJava {#我为什么使用ｒxjava}

Android原生处理异步的方式有很多，但是总结起来它们都有一个缺点，那就是代码臃肿，不便阅读。RxJava的出现改变了这个情况，使用RxJava可以轻松的在各个线程间切换，同时它的链式书写结构避免了复杂的回调。

基于Rxjava的RxBus设计原理轻松解决了App内部（单个进程）消息广播问题。这对App各个组件的解耦至关重要。

## 我为什么使用ＧreenDao {#我为什么使用ｇreendao}

在使用SQLite的时候，我发现了一个规律，我们的大部分操作都是在 读取数据库--生成对象--修改对象--存入数据库这条线进行的，有没有一个方法可以直接将数据库和对象结合呢？我尝试过自己写这个东西，但是效果不好，后来我无意了解了ORM，知道了还有GreenDao这么一个东西，我在尝试使用它后就被它的便捷迷倒了。

此次我也会使用GreenDao来做本地信息存储。这将简化我的开发流程。
---
## UI

<img src="/GIF_20170125_163033.gif" width="49%"/>
<img src="/GIF_20170125_163454.gif" width="49%"/>
<img src="/GIF_20170125_163709.gif" width="49%"/>
<img src="/GIF_20170125_163843.gif" width="49%"/>
