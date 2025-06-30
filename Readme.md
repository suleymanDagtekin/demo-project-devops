
---


# 🚀 Jenkins + Selenium Grid Automation Server on EC2


## ⚙️ Installation Steps

### 1. Install Java (JDK)


```bash
sudo apt update
sudo apt install -y openjdk-17-jdk
java -version
```


---

### 2. Install Git

```bash
sudo apt install -y git
git --version
```

---

### 3. Install Google Chrome

```bash
wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
sudo apt install -y ./google-chrome-stable_current_amd64.deb
google-chrome --version
```

---

### 4. Install Maven

```bash
sudo apt install -y maven
mvn -version
```

---

### 5. Install Docker (Official Method)

```bash
sudo apt update
sudo apt install -y \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | \
  sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
  https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

sudo usermod -aG docker $USER
newgrp docker
docker --version
```

---

### 6. Install Docker Compose

```bash
sudo curl -L "https://github.com/docker/compose/releases/download/v2.23.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
docker-compose --version
```

---

### 7. Install Jenkins (Official Website https://www.jenkins.io/download/)

```bash
wget -q -O - https://pkg.jenkins.io/debian/jenkins.io.key | sudo tee \
  /usr/share/keyrings/jenkins-keyring.asc > /dev/null

echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
  https://pkg.jenkins.io/debian binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null

sudo apt update
sudo apt install -y jenkins

sudo systemctl enable jenkins
sudo systemctl start jenkins
```

* Access Jenkins: `http://<EC2_PUBLIC_IP>:8080`
* Unlock Jenkins:

  ```bash
  sudo cat /var/lib/jenkins/secrets/initialAdminPassword
  ```

---

### 8. Setup Selenium Grid via Docker Compose

Clone the official Selenium Grid setup:

```bash
git clone https://github.com/CrazyDevo/docker-compose.git
cd docker-compose/simple/no_video
```

Start the grid:

```bash
docker-compose up -d
```

* Grid UI: `http://<EC2_PUBLIC_IP>:4444`

---

## ✅ Jenkins Configuration

1. Open Jenkins in browser
2. Install recommended plugins
3. Install extra plugins:

    * Allure
    * Cucumber



## ✅ Example Remote WebDriver Code

```java
WebDriver driver = new RemoteWebDriver(
  new URL("http://<your ec2 ip>:4444/wd/hub"),
  new ChromeOptions()
);
```

---

## 🧪 Tips

* Always wait for Grid and Jenkins to fully initialize before running tests
* Ensure your test framework is compatible with remote drivers
* Optionally mount volumes in Docker for Jenkins persistence

---

## 📌 References

* [Jenkins Official Website](https://www.jenkins.io/download/)
* [Selenium Grid Docker Compose by CrazyDevo](https://github.com/CrazyDevo/docker-compose)
* [Docker Documentation](https://docs.docker.com/engine/install/ubuntu/)
