#CI/CD环境的搭建实践与思考

###关于作业的说明

* 使用Jenkins（http://jenkins-ci.org/）完成CI/CD的工作。
* Demo是利用spring boot框架、使用maven构建的简单网页app，其源码托管在github上。
* 环境： ubuntu16.04 jdk1.9 maven3.5.2

![figure1](https://github.com/Eric-Zhang-C/springDemo/blob/master/a.jpg "figure1")

###Continuous Integration

* 从输出内容可以看出构建的整个过程：先从 github 上拉取源码，然后调用maven命令进行构建，考虑到我们小组源码的更新频率，CI的频率设置为1次/100分钟。
* 意义：以后项目的Build工作就可以交由运维甚至测试人员直接来做了，程序员可从中解脱，单单提交代码即可。 
* Jenkins的console output:
```
Started by user zyh
Building in workspace /home/bian/.jenkins/workspace/spring-boot-demp
 > git rev-parse --is-inside-work-tree # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/Eric-Zhang-C/springDemo.git # timeout=10
Fetching upstream changes from https://github.com/Eric-Zhang-C/springDemo.git
 > git --version # timeout=10
using GIT_ASKPASS to set credentials 
 > git fetch --tags --progress https://github.com/Eric-Zhang-C/springDemo.git +refs/heads/*:refs/remotes/origin/*
 > git rev-parse refs/remotes/origin/master^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/master^{commit} # timeout=10
Checking out Revision 6aa3f5c99c15ab864036423401aca5a410b96c86 (refs/remotes/origin/master)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 6aa3f5c99c15ab864036423401aca5a410b96c86
Commit message: "Initial commit"
 > git rev-list --no-walk 6aa3f5c99c15ab864036423401aca5a410b96c86 # timeout=10
[spring-boot-demp] $ /etc/apache-maven-3.5.2/bin/mvn -f pom.xml clean package -Dmaven.test.skip=true
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] Building simple_java 0.1.0
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:3.0.0:clean (default-clean) @ simple_java ---
[INFO] Deleting /home/bian/.jenkins/workspace/spring-boot-demp/target
[INFO] 
[INFO] --- maven-resources-plugin:3.0.1:resources (default-resources) @ simple_java ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 1 resource
[INFO] Copying 3 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.7.0:compile (default-compile) @ simple_java ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 5 source files to /home/bian/.jenkins/workspace/spring-boot-demp/target/classes
[INFO] /home/bian/.jenkins/workspace/spring-boot-demp/src/main/java/hello/WebSecurityConfig.java: /home/bian/.jenkins/workspace/spring-boot-demp/src/main/java/hello/WebSecurityConfig.java uses or overrides a deprecated API.
[INFO] /home/bian/.jenkins/workspace/spring-boot-demp/src/main/java/hello/WebSecurityConfig.java: Recompile with -Xlint:deprecation for details.
[INFO] 
[INFO] --- maven-resources-plugin:3.0.1:testResources (default-testResources) @ simple_java ---
[INFO] Not copying test resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.7.0:testCompile (default-testCompile) @ simple_java ---
[INFO] Not compiling test sources
[INFO] 
[INFO] --- maven-surefire-plugin:2.20.1:test (default-test) @ simple_java ---
[INFO] Tests are skipped.
[INFO] 
[INFO] --- maven-jar-plugin:3.0.2:jar (default-jar) @ simple_java ---
[INFO] Building jar: /home/bian/.jenkins/workspace/spring-boot-demp/target/simple_java-0.1.0.jar
[INFO] 
[INFO] --- spring-boot-maven-plugin:2.0.0.RELEASE:repackage (default) @ simple_java ---
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 6.646 s
[INFO] Finished at: 2018-05-22T20:03:19+08:00
[INFO] Final Memory: 32M/194M
[INFO] ------------------------------------------------------------------------
```



###CD

####远程分发

* 打包上传（利用scp插件，以证书方式连接到远程服务器）
* Jenkins’s console output:

```
[spring-boot-demp] $ /bin/bash -xe /tmp/jenkins473632326594019983.sh
+ cd /home/bian/.jenkins/workspace/spring-boot-demp
+ cd target
+ tar zcf deploy.tar.gz simple_java-0.1.0.jar
[SCP] Connecting to deploy
[SCP] uploading file: '/opt/deploy/deploy.tar.gz'
Finished: SUCCESS
```
####远程分发

* 打包上传（利用scp插件，以证书方式连接到远程服务器）
* Jenkins’s console output:

```
[spring-boot-demp] $ /bin/bash -xe /tmp/jenkins473632326594019983.sh
+ cd /home/bian/.jenkins/workspace/spring-boot-demp
+ cd target
+ tar zcf deploy.tar.gz simple_java-0.1.0.jar
[SCP] Connecting to deploy
[SCP] uploading file: '/opt/deploy/deploy.tar.gz'
Finished: SUCCESS
```

####远程启动

* 在分发完成后，通过Jenkins远程调用服务器端准备好的shell脚本运行jar包，成功运行即意味着部署成功。
* Jenkins’s console output:

```
[SCP] Connecting to deploy
[SCP] uploading file: '/opt/deploy/deploy.tar.gz'
SSH: Connecting from host [BianTP.local]
SSH: Connecting with configuration [deploy] ...
SSH: EXEC: STDOUT/STDERR from command [/opt/deploy/deploy.sh] ...
/opt/app/jdk1.9.0_4
deploying...
simple_java-0.1.0.jar
tar: simple_java-0.1.0.jar: time stamp 2018-05-22 22:42:15 is 124623.12836869 s in the future
/opt/deploy/simple_java-0.1.0.jar
ok!
SSH: EXEC: completed after 2,320 ms
SSH: Disconnecting configuration [deploy] ...
SSH: Transferred 0 file(s)
Finished: SUCCESS
```

###思考

* 如果项目范围很小或包含无法测试的遗留代码，持续集成（CI）不一定有价值。
* 较大的团队意味着新的代码不断添加到集成队列中，因此跟踪交付（同时保持质量）很困难，而排队可能会降低每个人的速度。
* CD使组织能够更快速地向客户提供新软件这种能力有助于公司在竞争中领先一步。
