String basePath = 'what-week'
String repo = 'tabbi89/blue-green-deployment-example'

folder(basePath) {
    description 'Pull request builder'
}

job("$basePath/pr-builder") {
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

    steps {
        shell 'npm install'
        shell 'npm test'
    }
}
