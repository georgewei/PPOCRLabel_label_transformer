# PPOCRLabel标注文件转换器
[英文](README.md)

<pre>
本工具用于将PPOCRLabel生成的标注文件转换为适配同样由PPOCRLabel生成的切图的标注文件。
</pre>

## 版本
### V1.0
<pre>
首版，仅支持转换标注文件。
</pre>
### V1.1
<pre>
增加支持在转换标注文件时扩展切图画布。
</pre>
## 构建
<pre>
安装JDK 1.8或以上版本；
安装maven；
克隆本项目到本地；
在命令行进入项目根目录，运行mvn package构建程序（项目根目录/target/transformer-with-dependencies.jar）。
</pre>

## 下载
<pre>
如果不想自己构建，可以在项目文件中下载已构建好的jar文件。
注：运行程序前需要安装JRE/JDK 1.8或以上版本。
</pre>

## 用法
<pre>
java -jar transformer-jar-with-dependencies.jar --image-dir &lt;PPOCRLabel生成的切图目录> --label-file &lt;PPOCRLabel生成的标注文件> &#91;--width &lt;扩展画布到指定宽度 (px)> --height &lt;扩展画布到指定高度 (px)> --output-dir &lt;保存修改后的图片目录>]
</pre>