# PPOCRLabel标注文件转换器
[英文](README.md)

本工具用于将PPOCRLabel生成的标注文件转换为适配同样由PPOCRLabel生成的切图的标注文件。

## 版本
### V1.0

首版，仅支持转换标注文件。

### V1.1

增加支持在转换标注文件时扩展切图画布。

## 构建

安装JDK 1.8或以上版本；<br>
安装Maven；<br>
克隆本项目到本地；<br>
在命令行进入项目根目录，运行mvn package构建程序（项目根目录/target/transformer-with-dependencies.jar）。


## 下载

如果不想自己构建，可以在项目文件中下载已构建好的jar文件。<br>
注：运行程序前需要安装JRE/JDK 1.8或以上版本。


## 用法
java -jar transformer-jar-with-dependencies.jar &lt;options><br>
<br>
Options:<br>
&nbsp;&nbsp;--image-dir   (Required) PPOCRLabel保存切图的目录<br>
&nbsp;&nbsp;--label-file  (Required) PPOCRLabel生成的标注文件<br>
&nbsp;&nbsp;--width       (Optional) 扩展画布到指定宽度（像素）<br>
&nbsp;&nbsp;--height      (Optional) 扩展画布到指定高度（像素）<br>
&nbsp;&nbsp;--output-dir  (Optional) 保存修改后的切图的目录<br>
<br>
&nbsp;&nbsp;注: 如果打算在转换标注文件时扩展切图的画布, --width、--height和--output-dir都必须指定。