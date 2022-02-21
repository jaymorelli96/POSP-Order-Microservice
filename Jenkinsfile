pipeline {
    agent {
        docker {
            image "maven:3.8.4-jdk-11"
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
