String basePath = 'weatherChecker'
String repo = 'tabbi89/blue-green-deployment-example'

folder(basePath) {
    description 'Pull request'
}

job("$basePath/PRDev") {
    label "digital-slave"
    scm {
        git {
            remote {
                github repo
                refspec '+refs/pull/*:refs/remotes/origin/pr/*'
            }
            branch('${sha1}')
        }
    }
    triggers {
            githubPullRequest {
                admin 'tabbi89'
                triggerPhrase('OK to test')
                useGitHubHooks()
                cron('* * * * *')
                allowMembersOfWhitelistedOrgsAsAdmin()
                extensions {
                    buildStatus {
                        completedStatus('SUCCESS', 'There were no errors, go have a cup of coffee...')
                        completedStatus('FAILURE', 'There were errors, for info, please see...')
                        completedStatus('ERROR', 'There was an error in the infrastructure, please contact...')
                    }
                }
            }
    }

    steps {
        shell 'composer install'
    }
}
