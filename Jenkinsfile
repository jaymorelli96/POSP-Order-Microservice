pipeline {
    agent {
        docker {
            image "maven:3.6.0-jdk-13"
            label "docker"
        }
    }

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
