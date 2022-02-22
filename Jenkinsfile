node {

	def mvnHome = tool 'maven-3.8.4'

	def dockerImageTag = "com.jaymorelli.posp/order-service"
	def dockerImage

	
	stage('Clone Repo') {
	      git branch: 'master',
		    credentialsId: 'dd2debc1-138f-41ce-b67d-0c59eda46b60',
		    url: 'https://github.com/jaymorelli96/POSP-Order-Microservice.git'
          
	      mvnHome = tool 'maven-3.8.4'
	    }

	stage('Build Jar') {
		bat "mvn clean install"
	}
	

	stage('Build Docker Image') {
	      dockerImage = docker.build("jaymorelli/posp-order")
	}		
	
	stage('Login Docker Hub') {
		docker.withRegistry( '', 'dockerhub-jaymorelli' ) { 
			dockerImage.push() 
		}
	}

	stage('Push Image') {
	        bat 'docker push jaymorelli/posp-order:latest'
	}
	
	stage('Cleaning up') { 
                bat "docker rmi jaymorelli/posp-order:latest" 
        } 
}
