String basePath = 'what-week'

folder(basePath) {
    description 'RollBack production'
}

pipelineJob("$basePath/rollback-production") {
    definition {
        cps {
            sandbox()
            script("""
                node {
                    git url: 'https://github.com/tabbi89/blue-green-deployment-example'

                    stage("Switch back non active node to live") {
                        ansiblePlaybook  playbook: 'ansible/digital-switch-production-env.yml', inventory: 'ansible/inventory/production', extraVars: [digital_api_token: [value: "${DIGITAL_API_TOKEN}", hidden: true], digital_floating_ip: "${DIGITAL_FLOATING_IP}"]
                    }
                }
            """.stripIndent())
        }
    }
}
