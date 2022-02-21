pipeline {
    agent any

    stages {
        stage("Build") {
            steps {
                bat "mvn -version"
                bat "mvn clean install"
            }
        }
    }


    post {
        always {
            cleanWs()
        }
    }
}
