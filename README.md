# Blue green deployment example

Sample deployment of what-week application using blue green technique. This example code was used during presentation.

Example uses [DigitalOcean](https://www.digitalocean.com/) as infrastructure provider. Deployment is made with Jenkins, Docker ans Ansible.

## Infrastructure

![alt tag](https://raw.githubusercontent.com/tabbi89/blue-green-deployment-example/master/documentation/images/infra.png)

In order to deploy sample application using blue green we need at least two nodes - production01 and production02
Jenkins node will be used to coordinate deployment. DigitalOcean floating IP will be used as "router" to easily switch nodes.

## Do it yourself

You need `ansible 2+` installed. On Mac OS X it can be installed through `brew`

### 1. Prepare configuration

In order to start we need digital api token, digital ssh key fingerprint that is used for creating droplets with ssh keys and private ssh key itself.
To add and check this necessarily data you can go to [https://cloud.digitalocean.com/settings/security](https://cloud.digitalocean.com/settings/security)

Create file `ansible_parameters.yml` in root directory with filled data

``` yaml
digital_api_token: PLACEHOLDER
digital_ssh_key_hex: PLACEHOLDER
ssh_key: PLACEHOLDER
```

### 2. Create infrastructure

You can create with ansible or do it manually using DigitalOcean panel

``` bash
$ ansible-playbook -i ansible/inventory/development ansible/digital-create-infrastructure.yml -e "@ansible_parameters.yml" --tags "with-removal,add-to-ssh-config,with-new-floating-ip"
```

When using `add-to-ssh-config` tag nodes configuration will be saved in `~/.ssh/config` with identity file located in `~/.ssh/id_rsa_digital`.

``` bash
# BEGIN production01
Host production01
   User root
   IdentityFile ~/.ssh/id_rsa_digital
   Hostname 46.XXX.XXX.XXX
# END production01
# BEGIN production02
Host production02
   User root
   IdentityFile ~/.ssh/id_rsa_digital
   Hostname 46.XXX.XXX.XXX
# END production02
# BEGIN jenkins
Host jenkins
   User root
   IdentityFile ~/.ssh/id_rsa_digital
   Hostname 46.XXX.XXX.XXX
# END jenkins
```

Example output of ansible tasks:

![alt tag](https://raw.githubusercontent.com/tabbi89/blue-green-deployment-example/master/documentation/images/infra_ansible.png)

### 3. Provision

After creating infrastructure you should note all data from ansible output from previous command (step 2. Create infrastructure) and create file `ansible_server_configuration.yml` in the root directory with filled data

``` yaml
jenkins_production_01_ip: PLACEHOLDER
jenkins_production_02_ip: PLACEHOLDER
digital_floating_ip: PLACEHOLDER
```

Next we can provision jenkins and production nodes:

``` bash
$ ansible-playbook -i ansible/inventory/development ansible/pre-setup.yml
$ ansible-playbook -i ansible/inventory/development ansible/jenkins.yml -e "@ansible_parameters.yml" -e "@ansible_server_configuration.yml"
```

``` bash
$ ansible-playbook -i ansible/inventory/production ansible/pre-setup.yml
$ ansible-playbook -i ansible/inventory/production ansible/webserver.yml
```

### 4. Deploy with jenkins

1. Open jenkins node ip with port `8080`
2. Run seed job. It will clone repository and create jobs written in `jenkins-jobs`. If you are not familiar with jobs dsl and pipelines you can find more about [Jenkins JOB DSL](https://jenkinsci.github.io/job-dsl-plugin) and [Jenkins Pipeline](https://jenkins.io/doc/book/pipeline/)
3. Run `deploy-to-production` in `what-week` project. It will deploy application using blue green.

![alt tag](https://raw.githubusercontent.com/tabbi89/blue-green-deployment-example/master/documentation/images/jenkins.png)


You can also trigger manually ansible tasks to start deployment

``` bash
$ ansible-playbook -i ansible/inventory/production ansible/digital-find-non-active-node.yml -e "@ansible_server_configuration.yml" -e "@ansible_parameters.yml"
$ ansible-playbook -i ansible/inventory/production ansible/deploy.yml -l "@ansible/deploy_to.yml"
$ ansible-playbook -i ansible/inventory/production ansible/digital-switch-production-env.yml -e "@ansible_server_configuration.yml" -e "@ansible_parameters.yml"
```