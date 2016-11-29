bg-deployment-example
=====================

A Symfony project created on November 28, 2016, 11:37 am.

    using "$basePath/DeployDev"
    parameters {
        stringParam('BRANCH', 'master')
    }
                        git url: 'https://github.com/tabbi89/blue-green-deployment-example', branch: "$BRANCH"