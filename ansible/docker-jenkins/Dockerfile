FROM jenkinsci/jenkins:2.33

RUN echo 2.0 > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false -Djenkins.install.runSetupWizard=false

#Make ssh dir
RUN mkdir -p ~/.ssh

## Create known_hosts
#RUN touch ~/.ssh/known_hosts
## Add github (or your git server) fingerprint to known hosts
#RUN ssh-keyscan -t rsa github.com >> ~/.ssh/known_hosts

COPY jobs /usr/share/jenkins/ref/jobs/
COPY config.xml /usr/share/jenkins/ref/config.xml
COPY credentials.xml /usr/share/jenkins/ref/credentials.xml
COPY org.jenkinsci.plugins.ghprb.GhprbTrigger.xml /usr/share/jenkins/ref/org.jenkinsci.plugins.ghprb.GhprbTrigger.xml

RUN /usr/local/bin/install-plugins.sh git:3.0.1 ghprb:1.33.1 cloudbees-folder:5.13 simple-theme-plugin:0.3 digitalocean-plugin:0.12 build-pipeline-plugin:1.5.4 workflow-aggregator:2.4 job-dsl:1.53 copyartifact:1.38.1 ws-cleanup:0.32 workflow-scm-step:2.3 ansible:0.5