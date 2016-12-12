FROM jenkinsci/jenkins:2.34

RUN echo 2.0 > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false -Djenkins.install.runSetupWizard=false

USER root

RUN echo "deb http://ppa.launchpad.net/ansible/ansible/ubuntu trusty main" >> /etc/apt/sources.list

RUN apt-get update && apt-get install -y --force-yes \
    apt-utils \
    python-minimal \
    python-pip \
    nodejs \
    npm \
    ansible

RUN ln -s /usr/bin/nodejs /usr/bin/node

USER jenkins

# Should not be used :)
RUN mkdir -p /var/jenkins_home/.ssh

USER root

COPY id_rsa /var/jenkins_home/.ssh/id_rsa
COPY config /var/jenkins_home/.ssh/config
RUN chown -R jenkins:jenkins /var/jenkins_home/.ssh/id_rsa
RUN chown -R jenkins:jenkins /var/jenkins_home/.ssh/config
RUN chmod 600 -R /var/jenkins_home/.ssh/id_rsa

USER jenkins

COPY jobs /usr/share/jenkins/ref/jobs/

RUN /usr/local/bin/install-plugins.sh git:3.0.1 ghprb:1.33.1 cloudbees-folder:5.13 simple-theme-plugin:0.3 digitalocean-plugin:0.12 build-pipeline-plugin:1.5.4 workflow-aggregator:2.4 job-dsl:1.53 copyartifact:1.38.1 ws-cleanup:0.32 workflow-scm-step:2.3 ansible:0.5