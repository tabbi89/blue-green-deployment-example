String basePath = 'weatherChecker'

folder(basePath) {
    description 'Deploy to production'
}

pipelineJob("$basePath/DeployProduction") {
    definition {
        cps {
            sandbox()
            script("""
                node('digital-slave') {
                    git url: 'https://github.com/tabbi89/blue-green-deployment-example'

                    stage("Install dependencies") {
                        sh "SYMFONY_ENV=prod composer install --no-ansi --no-dev --no-interaction --no-progress --optimize-autoloader"
                        sh "ansible-galaxy install -r ansible/requirements.yml"
                    }

                    stage("Clean app") {
                        sh "rm -rf web/app_dev.php web/apple-touch-icon.png web/config.php web/bundles"
                    }

                    stage("Deploy app artifact to production") {
                        ansiblePlaybook  playbook: 'ansible/deploy.yml', inventory: 'ansible/inventory/production', colorized: true
                    }

                    stage("Run healty check") {
                        ansiblePlaybook  playbook: 'ansible/health-check.yml', inventory: 'ansible/inventory/production', colorized: true
                    }

                    stage("Save artifact") {
                        archiveArtifacts artifacts: 'ansible/builds/*.tar.gz', excludes: 'output/*.md'
                    }
                }
            """.stripIndent())
        }
    }
}
