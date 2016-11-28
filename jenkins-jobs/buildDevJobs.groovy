String basePath = 'weatherChecker'

folder(basePath) {
    description 'This example shows basic folder/job creation.'
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
                    }

                    stage("Build binary") {

                    }

                    stage("Deploy binary") {
                    }
                }
            """.stripIndent())
        }
    }
}
