# 蜘网图控件
蜘网图，又名雷达图，用于表示物体的属性技能。此库为使用在安卓设备上的蜘网图自定义控件.

# 效果图
# ![image](https://github.com/a-voyager/NetView/raw/master/screenshots/img1.png "效果图")

# 用法
## 1. 添加依赖
克隆本项目添加依赖或者在Gradle中添加依赖:
```gradle
    compile 'top.wuhaojie.view:netview:1.0.1'
```
 > Gradle方式可能暂时无法使用

## 2. 布局文件中定义
```xml
    <top.wuhaojie.view.NetView
        android:id="@+id/net_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

## 3. 添加数据
```java
 final NetView netView = (NetView) findViewById(R.id.net_view);
 if (netView != null) {
     netView.addData("技能 A", 0.8f);
     netView.addData("技能 B", 0.6f);
     netView.addData("技能 C", 0.8f);
     netView.addData("技能 D", 0.6f);
     netView.addData("技能 E", 0.8f);
     netView.addData("技能 F", 0.6f);
 }
```

# 更多特性
 -  xml属性

| 含义          | 属性          |
| ------------- |:-------------:|
|网格颜色       |netColor       |
|覆盖区颜色     | overlayColor  |
|覆盖区圆圈颜色 | overlayCircleColor|
|覆盖区圆圈半径 |overlayCircleRadius|
|覆盖区透明度   |overlayAlpha   |
|文本颜色       |textColor      |
|文本字体大小   |textSize       |
|内网数量       |intervalCount  |

> 例如:
```xml
<top.wuhaojie.view.NetView
    android:id="@+id/net_view"
    android:layout_width="match_parent"
    android:layout_height="0dp"
    android:layout_weight="1"
    app:intervalCount="5"
    app:netColor="@color/defNetColor"
    app:overlayAlpha="100"
    app:overlayCircleColor="@color/defOverlayCircleColor"
    app:overlayCircleRadius="5"
    app:overlayColor="@color/defOverlayColor"
    app:textColor="@color/defTextColor"
    app:textSize="36"/>
```

- 外部接口
NetView.addData():可动态添加数据
NetView.removeData():可动态移除数据

# License
    The MIT License (MIT)

    Copyright (c) 2015 WuHaojie(吴豪杰)

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
