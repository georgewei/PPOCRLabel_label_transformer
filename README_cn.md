# PPOCRLabel标注文件转换器
[英文](README.md)

本工具用于将PPOCRLabel生成的标注文件转换为适配同样由PPOCRLabel生成的切图的标注文件。

## 构建
安装JDK 1.8或以上版本；
安装maven；
克隆本项目到本地；
在命令行进入项目根目录，运行mvn package构建程序（项目根目录/target/transformer-with-dependencies.jar）。

## 下载
如果不想自己构建，可以在项目文件中下载已构建好的jar文件。
注：运行程序前需要安装JRE/JDK 1.8或以上版本。

## 用法
java -jar transformer-jar-with-dependencies.jar --image-dir &lt;PPOCRLabel生成的切图目录> --label-file &lt;PPOCRLabel生成的标注文件>