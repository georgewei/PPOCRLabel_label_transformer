# PPOCRLabel Label Transformer
[Simplified Chinese](README_cn.md)

A Utility to transform label file generated by PPOCRLabel to suite cropped images generated by PPOCRLabel too.

## Build
Install JDK 1.8 or higher version;
Install maven;
Clone this project to local machine;
Open a command line/terminal window, enter the root folder of this project, run 'mvn package' to build the program.

## Download
If you won't build the program, you can download the built jar file in project assets.
Note: JRE/JDK 1.8 or higher version required to run this program.

## Usage
java -jar transformer-jar-with-dependencies.jar --image-dir <PPOCRLabel cropped image folder> --label-file <original PPOCRLabel label file>