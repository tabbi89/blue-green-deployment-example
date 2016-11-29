String basePath = 'weatherChecker'

folder(basePath) {
    description 'Deploy dev env'
}

pipelineJob("$basePath/DeployDev") {
    definition {
        cps {
            sandbox()
            script("""
                node('digital-slave') {
                    git url: 'https://github.com/tabbi89/blue-green-deployment-example'

                    stage("Install dependencies") {
                        sh "composer install"
                        sh "ansible-galaxy install -r ansible/requirements.yml"
                    }

                    stage("Deploy app artifact") {
                        ansiblePlaybook  playbook: 'ansible/deploy.yml', inventory: 'ansible/inventory/development', colorized: true
                    }

                    stage("Save artifact") {
                        archiveArtifacts artifacts: 'ansible/builds/*.tar.gz', excludes: 'output/*.md'
                    }
                }
            """.stripIndent())
        }
    }
}
