# PolicyLibDemo
PolicyLibDemo

权限申请和隐私政策弹窗提示库：

支持工信部规定的首次弹窗隐私政策和用户协议；
支持申请权限前弹窗提示说明权限用途；
支持申请权限时，用户点击拒绝并不再提示后再弹窗提示去系统手动授权；
内置权限申请方法EasyPermission。

引用库方法：
```
allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
```
```
dependencies {
	        implementation 'com.github.jaychou2012:PolicyLibDemo:1.0.1'
	}
```  

具体用例可以在MainActivity里查看用法。支持Activity和Fragment。

体验APK：
[点击下载](https://github.com/jaychou2012/PolicyLibDemo/blob/master/app-debug.apk?raw=true)

![隐私政策和用户协议首次弹窗](https://github.com/jaychou2012/PolicyLibDemo/blob/master/Screenshot_20191208-160231_PolicyLibDemo.jpg?raw=true)


![申请权限前弹窗提示说明权限用途](https://github.com/jaychou2012/PolicyLibDemo/blob/master/Screenshot_20191221-185304_PolicyLibDemo.jpg?raw=true)


![权限申请](https://github.com/jaychou2012/PolicyLibDemo/blob/master/Screenshot_20191208-160310_Package%20installer.jpg?raw=true)

![权限不再提示](https://github.com/jaychou2012/PolicyLibDemo/blob/master/Screenshot_20191221-185419_PolicyLibDemo.jpg?raw=true)





