String basePath = 'weatherChecker'

folder(basePath) {
    description 'RollBack production'
}

pipelineJob("$basePath/RollBack") {
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
                        ansiblePlaybook  playbook: 'ansible/deploy.yml', inventory: 'ansible/inventory', colorized: true
                    }

                    stage("Run healty check") {
                        ansiblePlaybook  playbook: 'ansible/deploy.yml', inventory: 'ansible/inventory', colorized: true
                    }
                }
            """.stripIndent())
        }
    }
}
