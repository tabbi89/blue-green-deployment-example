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
                node('digital-slave') {
                    git url: 'https://github.com/tabbi89/blue-green-deployment-example', branch: 'master', credentialsId: 'b2cad6ea-8de0-4d36-882a-87d586cfc9d0'

                    stage("Install dependencies") {

                    }

                    stage("Run tests") {

                    }

                    stage("Find non active node") {
                        ansiblePlaybook  playbook: 'ansible/deploy_find_non_active_node.yml', inventory: 'ansible/inventory/production'
                    }

                    stage("Deploy app artifact to non active node") {
                        ansiblePlaybook  playbook: 'ansible/deploy.yml', inventory: 'ansible/inventory/production', limit: '@ansible/deploy_to.yml'
                    }

                    stage("Health check of non active node") {
                        ansiblePlaybook  playbook: 'ansible/health-check.yml', inventory: 'ansible/inventory/production', limit: '@ansible/deploy_to.yml'
                    }

                    stage("Switch non active node to live") {
                        ansiblePlaybook  playbook: 'ansible/switch_env.yml', inventory: 'ansible/inventory/production'
                    }

                    stage("Save artifact") {
                        archiveArtifacts artifacts: 'ansible/builds/*.tar.gz, ansible/deploy_to.yml', excludes: 'output/*.md'
                    }
                }
            """.stripIndent())
        }
    }
}