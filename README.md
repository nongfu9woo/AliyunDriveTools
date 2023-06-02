# AliyunDriveTools
Java脚本实现本地每日签到阿里云盘,并推送签到结果。  
源代码为[bighammer](https://github.com/bighammer-link/Common-scripts)提供的python版本，刚好在学Java，就用来学习了。  
## 阿里云盘refresh_token获取  
* 登录[阿里云盘](https://www.aliyundrive.com/drive)  
* F12，进入开发者模式，依次点击进入 -> 存储/Application -> 本地存储/Local Storage -> https://www.aliyundrive.com -> token
* 就能看见refresh_token![refresh_token](https://github.com/nongfu9woo/AliyunDriveTools/blob/main/aliyundrive.png)
## 微信推送
* 登录[推送plus](https://www.pushplus.plus/)注册并获取token
