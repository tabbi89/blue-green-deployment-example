String basePath = 'what-week'

folder(basePath) {
    description 'RollBack production'
}

pipelineJob("$basePath/rollback-production") {
    definition {
        cps {
            sandbox()
            script("""
                node('digital-slave') {
                    git url: 'https://github.com/tabbi89/blue-green-deployment-example', credentialsId: 'b90a4e44-d2f0-4f22-a4f4-ed20d1ff8609'

                    stage("Switch back non active node to live") {
                        ansiblePlaybook  playbook: 'ansible/switch_env.yml', inventory: 'ansible/inventory/production'
                    }
                }
            """.stripIndent())
        }
    }
}
