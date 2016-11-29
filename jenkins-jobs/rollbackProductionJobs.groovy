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
                    git url: 'https://github.com/tabbi89/blue-green-deployment-example'

                    stage("Switch back non active node to live") {
                        ansiblePlaybook  playbook: 'ansible/switch_env.yml', inventory: 'ansible/inventory/production'
                    }
                }
            """.stripIndent())
        }
    }
}
