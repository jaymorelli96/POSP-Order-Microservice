node {

	def mvnHome = tool 'maven-3.8.4'

	def dockerImageTag = "com.jaymorelli.posp/order-service"
	def dockerImage
	
	environment {
		DOCKERHUB_CREDENTIALS=credentials('dockerhub-jaymorelli')
	}
	
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
	      dockerImage = docker.build("com.jaymorelli.posp/order-service")
	}		
	
	stage('Login Docker Hub') {
		bat 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u jaymorelli --password-stdin'
	}

	stage('Push Image') {
	        bat 'docker push jaymorelli/posp-order:latest'
	}
	



	post {
		always {
			cleanWs()
		}
	}
}
