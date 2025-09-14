FROM jenkins/jenkins:lts-jdk17

USER root

# Install docker, github cli, argocd cli, jq, python3
RUN set -eux; \
  apt-get update; \
  apt-get install -y --no-install-recommends \
    curl unzip apt-transport-https ca-certificates gnupg lsb-release; \
  # Docker CLI
  curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg; \
  echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/debian $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list; \
  # GitHub CLI, jq, python3
  curl -fsSL https://cli.github.com/packages/githubcli-archive-keyring.gpg | dd of=/usr/share/keyrings/githubcli-archive-keyring.gpg; \
  chmod go+r /usr/share/keyrings/githubcli-archive-keyring.gpg; \
  echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/githubcli-archive-keyring.gpg] https://cli.github.com/packages stable main" > /etc/apt/sources.list.d/github-cli.list; \
  apt-get update; \
  apt-get install -y --no-install-recommends docker-ce-cli gh jq python3; \
  rm -rf /var/lib/apt/lists/*; \
  groupadd -g 989 docker; \
  usermod -aG docker jenkins; \
  # Install Argo CD CLI
  ARGOCD_VERSION="v2.10.7"; \
  curl -fsSL -o /usr/local/bin/argocd "https://github.com/argoproj/argo-cd/releases/download/${ARGOCD_VERSION}/argocd-linux-amd64"; \
  chmod +x /usr/local/bin/argocd

USER jenkins

# Copy plugins.txt into the image
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt

# Auto-install plugins at build time
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt
