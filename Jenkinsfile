pipeline { 

    environment { 

        registry = "jaymorelli/posp-order" 

        registryCredential = 'dockerhub-jaymorelli' 

        dockerImage = '' 

    }

    agent any 

    stages { 
	    
        stage('Build Jar') {
            steps { 
                bat 'mvn clean install'
            }
        } 
	    


        stage('Build image') { 
            steps { 
                script { 
                    dockerImage = docker.build registry + ":$BUILD_NUMBER" 
                }
            } 
        }

        stage('Deploy our image') { 
            steps { 
                script { 
                    docker.withRegistry( '', registryCredential ) { 
                        dockerImage.push() 
                    }
                } 
            }
        } 

        stage('Cleaning up') { 
            steps { 
                bat "docker rmi $registry:$BUILD_NUMBER" 
            }
        } 

    }

	post {
		always {
			bat 'docker logout'
		}
	}

}
