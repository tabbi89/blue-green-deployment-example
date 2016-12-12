String basePath = 'what-week'

folder(basePath) {
    description 'Deploy application to production'
}

pipelineJob("$basePath/deploy-to-production") {
    triggers {
        githubPush()
    }
    definition {
        cps {
            sandbox()
            script("""
                node {
                    git url: 'https://github.com/tabbi89/blue-green-deployment-example', branch: 'master'

                    stage("Install dependencies") {
                        sh 'npm install'
                    }

                    stage("Run tests") {
                        sh 'npm test'
                    }

                    stage("Find non active node") {
                        ansiblePlaybook  playbook: 'ansible/digital-find-non-active-node.yml', inventory: 'ansible/inventory/production', extraVars: [digital_api_token: [value: "${DIGITAL_API_TOKEN}", hidden: true], digital_floating_ip: "${DIGITAL_FLOATING_IP}"]
                    }

                    stage("Deploy app artifact to non active node") {
                        ansiblePlaybook  playbook: 'ansible/deploy.yml', inventory: 'ansible/inventory/production', limit: '@ansible/deploy_to.yml'
                    }

                    stage("Health check of non active node") {
                        ansiblePlaybook  playbook: 'ansible/health-check.yml', inventory: 'ansible/inventory/production', limit: '@ansible/deploy_to.yml'
                    }

                    stage("Switch non active node to live") {
                        ansiblePlaybook  playbook: 'ansible/digital-switch-production-env.yml', inventory: 'ansible/inventory/production', extraVars: [digital_api_token: [value: "${DIGITAL_API_TOKEN}", hidden: true], digital_floating_ip: "${DIGITAL_FLOATING_IP}"]
                    }

                    stage("Save artifact") {
                        archiveArtifacts artifacts: 'builds/*.tar.gz, ansible/deploy_to.yml', excludes: 'output/*.md'
                    }
                }
            """.stripIndent())
        }
    }
}
