# CameraGravitySensor
仿华为相机旋转动画，配置界面支持360度旋转。

## 在实现这个需求的时候碰到了很多坑，因为Camera是固定竖屏，配置界面却要实现随重力横竖屏切换。一开始想到两种方案。
1、用透明的Activity，如果能实现的话，是最简单的方式。但是事与愿违。当透明的Activity旋转的时候，会导致下面的Activity也跟着旋转，不符合要求。
2、用透明Dialog，根据重力变化，旋转Dialog，同时重试Dialog窗口宽高来实现。具体实现过程请看代码。

## 相机界面是固定的竖屏显示，配置界面随重力感应可以360旋转

![image](https://github.com/aLittleGreens/CameraGravitySensor/blob/master/app/src/screencap/camera.gif?raw=true)

![image](https://github.com/aLittleGreens/CameraGravitySensor/blob/master/app/src/screencap/camera.gif?raw=true)

