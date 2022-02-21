pipeline {
    agent {
        docker {
            image "maven"
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
