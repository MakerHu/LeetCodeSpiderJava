# 爬虫实现力扣(LeetCode)本地刷题的快捷工具

[MakerHu的博客-爬虫实现力扣(LeetCode)本地刷题的快捷工具](https://blog.makerhu.com/posts/fe73207c/)

> 相信大家都在力扣上刷过题，力扣上虽然能在线写题，但在线编译运行的速度先对于本地运行还是比较慢，且一些自动补全的功能也不如自己的IDE优秀，因此，我们一般会在本地建个小项目解题，解完后再复制到力扣上提交。为了方便我们在本地快速搭建起力扣的解题环境，我写了一个小工具帮助我们把力扣的题目描述爬取到本地，并生成基础的代码，这样我们就能快速地专注于解题本身了。接下来我将展示一下工具的使用效果，并向大家提供源代码及部署方式。

# 效果演示

1. 复制路径中的题目英文名

![路径中的题目英文名](https://gitee.com/MakerHu/typora_images/raw/master/img/20210809214602.png)

2. 运行打包好的jar包

   ![运行打包好的jar包](https://gitee.com/MakerHu/typora_images/raw/master/img/20210809215022.png)

3. 打开本地项目

   ![打开本地项目](https://gitee.com/MakerHu/typora_images/raw/master/img/20210809215244.png)

4. 开始本地专注解题

   ![本地解题](https://gitee.com/MakerHu/typora_images/raw/master/img/20210809215417.png)

是不是非常地方便快捷😁

如果你也想拥有一个这样的工具就接着往下看吧，二十分钟之内你也能实现上述的效果哈！

# 项目部署与打包

## 准备

**环境要求：**

- jdk1.8（最好是1.8，其他版本我没有试过）
- Maven
- git
- IDEA（当然也能用你喜欢的集成开发环境）
- IDEA安装markdown插件（为的是能够有上述题目在本地的显示效果，否则可能显示的是纯文本）

具体的环境搭建我就不教学了，网上的教程非常多且详细，相信大家都能搭建起来。

## 克隆项目到本地

**项目地址：**

[MakerHu/LeetCodeSpiderJava: 爬取力扣的题目并在本地生成基础的项目文件 (github.com)](https://github.com/MakerHu/LeetCodeSpiderJava)

1. 打开IDEA，点击Git->Clone

   ![点击Git->Clone](https://gitee.com/MakerHu/typora_images/raw/master/img/20210809221315.png)

2. 点击上方的项目地址，复制仓库的url

   ![](https://gitee.com/MakerHu/typora_images/raw/master/img/20210809221617.png)

3. 将url粘贴到刚刚打开的Clone窗口中，并选择项目位置，点击Clone

   ![把项目克隆到本地](https://gitee.com/MakerHu/typora_images/raw/master/img/20210809222733.png)

4. 稍等一会儿，等项目依赖自动配置完后点击启动类中的运行

   ![运行项目](https://gitee.com/MakerHu/typora_images/raw/master/img/20210809233000.png)

5. 若显示下面的运行结果则说明项目运行成功了

   ![项目运行成功](https://gitee.com/MakerHu/typora_images/raw/master/img/20210809233111.png)

## 创建本地刷题项目

我们把刷题和爬取题目的项目分离开来，为的是解耦，也方便之后的迁移与重用。

这里我将演示创建一个基础的java项目，当然你也可以创建Maven项目等，因为爬虫工具的原理是在某个文件夹下创建文件夹，并将题目内容markdown文件及基础代码创建到该文件夹下。

1. 创建一个基础的java项目

   ![](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810110103.png)

   ![](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810110158.png)

   ![](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810110333.png)

## 在爬虫项目的配置文件中配置基本信息

**打开爬虫项目的配置文件**

![](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810113838.png)

配置文件settings.properties

```properties
# Java项目中要存放解题的根文件夹，如 E:/LeetCode/src/com/leetcode
question_location = E:/200_study/220_self_study/224_category/01_exercise/LeetCode/src/com/leetcode

# 解题所在的包 如：com.leetcode 若没有(也就是放在src文件夹下)则为空
base_package = com.leetcode

# 解题文件夹名称的前缀（可以是任意字符串，但不能为空，且要以小写字母开头）
folder.prefix = p
# 解题文件夹是否包含力扣题目名（true 或 false）
folder.include_question_name = true
# 解题文件夹是否按照时间分类（true 或 false）
sort_by_time = true

# 用来存储题目信息的markdown文件的前缀（可以是任意字符串）
question_markdown_filename.prefix = p
# 用来存储题目信息的markdown文件名是否包含题目编号（true 或 false）
question_markdown_filename.include_question_id = true

# 用来存储题目信息的markdown文件名是否包含题目名以及是否用中文题目名命名（true 或 false）
question_markdown_filename.include_question_name = true
question_markdown_filename.zh_cn = true
```



### 配置基本路径及基础包

**基本路径：question_location**

在settings.proterties配置文件中的属性`question_location`表示解题的根路径，爬虫创建的文件夹将会放到该路径下。

以上面示例中的`question_location = E:/000_collection/myLeetCode/src`为例，比如说爬取了题号为22的题目，则会在`E:/000_collection/myLeetCode/src`文件夹下创建`p22`文件夹，最终得到`E:/000_collection/myLeetCode/src/p22`

**注意：**配置的路径中的斜杠必须是正斜杠`/`，别写成反斜杠了！！！

**基础包：base_package**

在settings.proterties配置文件中的属性`base_package`表示解题所在的包，这要与基本路径`question_location`中`src`之后的路径对应，否则会出错。

示例1：

```bash
若 question_location = E:/000_collection/myLeetCode/src
则 base_package =
没错，base_package就等于空，因为question_location中的src就是包的根位置
```

示例2：

```bash
若 question_location = E:/000_collection/myLeetCode/src/com/leetcode
则 base_package = com.leetcode
```



对于上述的两个示例的情况，我分别实际演示一下如何配置。

**示例1：将题目爬取到`src`下**

1. 打开刚刚创建的**解题项目**，右键src->Copy Path->Absloute Path，也就是复制src的本地绝对路径，也可以直接到资源管理器中复制

   ![复制路径](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810155757.png)

   ![复制路径](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810155842.png)

2. 将刚复制的路径粘贴到**爬虫项目**的settings.properties配置文件中的`question_location`去

   ```properties
   # Java项目中要存放解题的根文件夹，如 E:/LeetCode/src/com/leetcode
   question_location = E:\000_collection\myLeetCode\src
   ```

   注意，此时的路径为反斜杠，将其都改为斜杠`/`

   修改后：

   ```properties
   # Java项目中要存放解题的根文件夹，如 E:/LeetCode/src/com/leetcode
   question_location = E:/000_collection/myLeetCode/src
   ```

3. 将**爬虫项目**的settings.properties配置文件中的`base_package`属性等号之后置为空

   ```properties
   # 解题所在的包 如：com.leetcode 若没有(也就是放在src文件夹下)则为空
   base_package = 
   ```

**示例2：将题目爬取到指定的包下**

假设我的解题项目中存在包com.leetcode，我要将题目爬取到这个包下。

1. 打开**解题项目**，右键leetcode->Copy Path->Absloute Path，也就是复制leetcode的本地绝对路径，也可以直接到资源管理器中复制

   ![复制路径](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810161348.png)

2. 将刚复制的路径粘贴到**爬虫项目**的settings.properties配置文件中的`question_location`去

   ```properties
   # Java项目中要存放解题的根文件夹，如 E:/LeetCode/src/com/leetcode
   question_location = E:\000_collection\myLeetCode\src\com\leetcode
   ```

   注意，此时的路径为反斜杠，将其都改为斜杠`/`

   修改后：

   ```properties
   # Java项目中要存放解题的根文件夹，如 E:/LeetCode/src/com/leetcode
   question_location = E:/000_collection/myLeetCode/src/com/leetcode
   ```

3. 将**爬虫项目**的settings.properties配置文件中的`base_package`属性设置为刚刚选择的包，也就是将src之后的路径中的斜杠改为点，或者你可以在刚刚选择的包（leetcode）中随便创建一个类，看一下第一行的package是什么就复制过来，别带分号就行。

   ```properties
   # 解题所在的包 如：com.leetcode 若没有(也就是放在src文件夹下)则为空
   base_package = com.leetcode
   ```

   不确定的话就随便在这个包下创建一个类，看一下第一行是啥，然后再把这个类删了

   ![查看包](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810162008.png)

### 配置其他属性

除了`question_location`和`base_package`的配置比较重要外，其他的配置都是与文件夹命名方式或文件命名方式有关的配置，使用默认的也挺好的。具体可以看配置文件中的注释尝试修改修改，并看看爬取后的效果，这里就不做详细的解释了，相信大家都看懂或者试出差别。这里就再把配置文件放出来吧。

settings.properties文件：

```properties
# Java项目中要存放解题的根文件夹，如 E:/LeetCode/src/com/leetcode
question_location = E:/200_study/220_self_study/224_category/01_exercise/LeetCode/src/com/leetcode

# 解题所在的包 如：com.leetcode 若没有(也就是放在src文件夹下)则为空
base_package = com.leetcode

# 解题文件夹名称的前缀（可以是任意字符串，但不能为空，且要以小写字母开头）
folder.prefix = p
# 解题文件夹是否包含力扣题目名（true 或 false）
folder.include_question_name = true
# 解题文件夹是否按照时间分类（true 或 false）
sort_by_time = true

# 用来存储题目信息的markdown文件的前缀（可以是任意字符串）
question_markdown_filename.prefix = p
# 用来存储题目信息的markdown文件名是否包含题目编号（true 或 false）
question_markdown_filename.include_question_id = true

# 用来存储题目信息的markdown文件名是否包含题目名以及是否用中文题目名命名（true 或 false）
question_markdown_filename.include_question_name = true
question_markdown_filename.zh_cn = true
```



## 爬取题目到本地

### 直接运行爬虫项目爬取

1. 运行项目

   ![运行爬虫](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810162906.png)

2. 输入力扣中题目的英文名（地址栏中的），回车

   ![](https://gitee.com/MakerHu/typora_images/raw/master/img/20210809214602.png)

   ![输入题目英文名](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810163246.png)

3. 完成

   ![题目生成完毕](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810163432.png)

### 将项目打成jar包后爬取

通过上面的方式已经能实现我们要的功能了，但是有个缺点就是每次都要打开项目运行，相对比较麻烦。我们可以将该项目打包成jar包，之后只要运行jar包就能运行了，且配置文件会从jar包中分离出来，可以随时修改配置，非常方便。

1. 回到爬虫项目中，首先双击右侧的`Maven->Lifecycle->clean`当运行结束后，再双击`Maven->Lifecycle->package`

   ![打包项目](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810164926.png)

2. 此时左侧会出现`target`文件夹，将打好的jar包（SNAPSHOT结尾的）复制出来，放到任意一个空文件夹里

   ![复制打好的jar包](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810165322.png)

3. 可以先将复制出来的jar包重命名个短点的名字，然后打开jar包所在的文件夹，在地址栏中输入`cmd`

   ![打开命令行窗口](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810170040.png)

4. 输入命令`java -jar 包名.jar`，因为我的包名重命名为`LeetCodesSpider`所以我输入的命令就是`java -jar LeetCodeSpider.jar`

   此时会报错，系统找不到指定的路径，因为我将配置文件从jar包中分离出来了，但我们还没将配置文件放到指定的位置，所以找不到。

   ```bash
   E:\000_collection\LeetCodeTool>java -jar LeetCodeSpider.jar
   java.io.FileNotFoundException: src\main\resources\settings.properties (系统找不到指定的路径。)
           at java.io.FileInputStream.open0(Native Method)
           at java.io.FileInputStream.open(Unknown Source)
           at java.io.FileInputStream.<init>(Unknown Source)
           at java.io.FileInputStream.<init>(Unknown Source)
           at config.Settings.<init>(Settings.java:14)
           at SpiderApplication.main(SpiderApplication.java:15)
           at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
           at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
           at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
           at java.lang.reflect.Method.invoke(Unknown Source)
           at org.springframework.boot.loader.MainMethodRunner.run(MainMethodRunner.java:48)
           at org.springframework.boot.loader.Launcher.launch(Launcher.java:87)
           at org.springframework.boot.loader.Launcher.launch(Launcher.java:51)
           at org.springframework.boot.loader.JarLauncher.main(JarLauncher.java:52)
   请输入LeetCode中题目的英文名（见浏览器路径，不带斜杠）：
   ```

   既然找不到，我们就创建对应文件夹并把配置文件放到里面去就行了。

   将报错中的`src\main\resources`复制下来。重新打开jar包所在的文件夹，在地址栏中输入`cmd`，之后输入命令创建文件夹

   `mkdir src\main\resources`

   现在打开jar包所在的文件夹我们就能看到刚创建好的文件夹了。

   ![](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810171255.png)

   接着我们将爬虫项目中的settings.properties文件复制到`src\main\resources`文件夹下

   ![复制配置文件到指定文件夹](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810171507.png)

   到这里还没有结束。由于我们爬取题目后会生成一些基础的代码，包括`Main.java`和`Solution.java`，而这两个文件是根据两个模板文件生成的，因此我们也要将两个模板文件放到`src\main\resources`中。

   进入爬虫项目，复制`resources`文件夹下的整个`template`文件夹，将其粘贴到刚刚在jar包同级目录中创建的`src\main\resources`文件夹下。

   ![复制template文件夹](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810172115.png)

   ![放到与settings.properties同级目录中](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810172205.png)

   再次在jar包所在目录打开`cmd`输入命令`java -jar 包名.jar`就能正常使用了！

5. 编辑配置文件

   由于配置文件与jar包分离，所以只要源代码没变，要想修改什么配置直接在jar包所在目录下的配置文件`src\main\resources\settings.properties`中修改配置属性就行了，可以用记事本vscode等任意你喜欢的文本编辑器打开修改。

# 常见问题

1. 解题时题目图片显示不出来，可能是IDEA开了代理，关了就行

   `File->settings->Appearance & Behavior->System Settings->HTTP Proxy`。

   ![关闭代理](https://gitee.com/MakerHu/typora_images/raw/master/img/20210810173331.png)


