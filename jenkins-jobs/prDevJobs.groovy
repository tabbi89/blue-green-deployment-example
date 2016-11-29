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
            }
    }

    publishers {
        mergeGithubPullRequest {
            mergeComment('merged by Jenkins')
            onlyAdminsMerge()
            failOnNonMerge()
            deleteOnMerge()
        }
    }

    steps {
        shell 'composer install'
    }
}
