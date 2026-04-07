## [Apache RocketMQ](https://github.com/apache/rocketmq) Dashboard 
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![CodeCov](https://codecov.io/gh/apache/rocketmq-dashboard/branch/master/graph/badge.svg)](https://codecov.io/gh/apache/rocketmq-dashboard)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/apache/rocketmq-dashboard.svg)](http://isitmaintained.com/project/apache/rocketmq-dashboard "Average time to resolve an issue")
[![Percentage of issues still open](http://isitmaintained.com/badge/open/apache/rocketmq-dashboard.svg)](http://isitmaintained.com/project/apache/rocketmq-dashboard "Percentage of issues still open")
[![Twitter Follow](https://img.shields.io/twitter/follow/ApacheRocketMQ?style=social)](https://twitter.com/intent/follow?screen_name=ApacheRocketMQ)
## Quick Start

### Run with docker

#### Pull from [docker hub(rocketmq-dashboard)](https://hub.docker.com/r/apacherocketmq/rocketmq-dashboard/tags)

```shell
docker pull apacherocketmq/rocketmq-dashboard:latest
```

#### Run it (use your own `rocketmq.namesrv.addr` and `port`)

```shell
docker run -d --name rocketmq-dashboard -e "JAVA_OPTS=-Drocketmq.namesrv.addr=127.0.0.1:9876" -p 8082:8082 -t apacherocketmq/rocketmq-dashboard:latest
```

### Run with source code


#### Prerequisite
1. 64bit OS, Linux/Unix/Mac is recommended;
2. 64bit JDK 17;
3. Maven 3.2.x;

#### Maven spring-boot run

```shell
mvn spring-boot:run
```
or

#### Maven build and run

```shell
mvn clean package -Dmaven.test.skip=true
java -jar target/rocketmq-dashboard-2.1.1-SNAPSHOT.jar
```

### GitHub Release packages

This fork provides a GitHub Actions release workflow in `.github/workflows/release.yml`.

- Push a tag like `v2.1.1` to build a GitHub Release automatically.
- The workflow uploads ready-to-run Linux bundles for `x64` and `aarch64`.
- Each bundle contains the dashboard jar, a bundled Temurin JRE 17 runtime, startup script, `LICENSE`, and `NOTICE`.

### Sync upstream

If you use this repository as a long-lived fork, add the Apache repository as `upstream` and periodically sync it before cutting a new release.

```shell
git remote add upstream https://github.com/apache/rocketmq-dashboard.git
git fetch upstream
git checkout master
git merge upstream/master
git push origin master
```

If you prefer to keep your fork history linear, use rebase instead of merge:

```shell
git fetch upstream
git checkout master
git rebase upstream/master
git push --force-with-lease origin master
```

After syncing upstream, create a new release tag in your fork:

```shell
git tag v2.1.1-fork.4
git push origin v2.1.1-fork.4
```

### How to use this fork

This fork supports two practical ways to run RocketMQ Dashboard:

1. Download a Linux release bundle from GitHub Releases and run it directly.
2. Build from source with Maven if you need to change the code.

#### Run from a GitHub Release bundle

Download the archive that matches your Linux architecture, then extract and start it:

```shell
tar -xzf rocketmq-dashboard-2.1.1-SNAPSHOT-linux-x64.tar.gz
cd rocketmq-dashboard-2.1.1-SNAPSHOT-linux-x64
export JAVA_OPTS="-Drocketmq.namesrv.addr=127.0.0.1:9876"
./bin/rocketmq-dashboard
```

The bundled runtime already contains JRE 17, so you do not need to install Java separately on the target machine.

#### Run the plain jar

If you already manage Java 17 yourself, you can also use the release jar:

```shell
java -Drocketmq.namesrv.addr=127.0.0.1:9876 -jar rocketmq-dashboard-2.1.1-SNAPSHOT.jar
```

#### Build from source

```shell
mvn clean package -DskipTests
java -Drocketmq.namesrv.addr=127.0.0.1:9876 -jar target/rocketmq-dashboard-2.1.1-SNAPSHOT.jar
```

### Installation example

The example below installs the `linux-x64` release into `/opt/rocketmq-dashboard` and manages it with `systemd`.

#### 1. Create a service account

```shell
sudo useradd --system --home /opt/rocketmq-dashboard --shell /sbin/nologin rocketmq-dashboard
```

#### 2. Install the release bundle

```shell
sudo mkdir -p /opt/rocketmq-dashboard
sudo tar -xzf rocketmq-dashboard-2.1.1-SNAPSHOT-linux-x64.tar.gz -C /opt/rocketmq-dashboard --strip-components=1
sudo chown -R rocketmq-dashboard:rocketmq-dashboard /opt/rocketmq-dashboard
```

#### 3. Create a systemd unit

Save the following as `/etc/systemd/system/rocketmq-dashboard.service`:

```ini
[Unit]
Description=RocketMQ Dashboard
After=network.target

[Service]
Type=simple
User=rocketmq-dashboard
Group=rocketmq-dashboard
WorkingDirectory=/opt/rocketmq-dashboard
Environment="JAVA_OPTS=-Drocketmq.namesrv.addr=127.0.0.1:9876 -Dserver.port=8082"
ExecStart=/opt/rocketmq-dashboard/bin/rocketmq-dashboard
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
```

#### 4. Enable and start the service

```shell
sudo systemctl daemon-reload
sudo systemctl enable --now rocketmq-dashboard
sudo systemctl status rocketmq-dashboard
```

#### 5. Open the dashboard

After startup, open:

```text
http://<your-server-ip>:8082
```

#### Tips
* If you download the package slowly, you can change maven's mirror(maven's settings.xml)

  ```
  <mirrors>
      <mirror>
            <id>alimaven</id>
            <name>aliyun maven</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
            <mirrorOf>central</mirrorOf>        
      </mirror>
  </mirrors>
  ```

* Change the rocketmq.config.namesrvAddr in resource/application.properties.(or you can change it in ops page)

## UserGuide

[English](https://github.com/apache/rocketmq-dashboard/blob/master/docs/1_0_0/UserGuide_EN.md)

[中文](https://github.com/apache/rocketmq-dashboard/blob/master/docs/1_0_0/UserGuide_CN.md)

## Contributing

We are always very happy to have contributions, whether for trivial cleanups or big new features. Please see the RocketMQ main website to read the [details](http://rocketmq.apache.org/docs/how-to-contribute/).

## License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) Copyright (C) Apache Software Foundation
