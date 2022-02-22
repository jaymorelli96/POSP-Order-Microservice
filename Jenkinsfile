node {

	def mvnHome = tool 'maven-3.8.4'

	def dockerImageTag = "com.jaymorelli.posp/order-service"
	def dockerImage
	
	stage('Clone Repo') { // for display purposes
	      // Get some code from a GitHub repository
	      git 'https://github.com/jaymorelli96/POSP-Order-Microservice.git'
	      // Get the Maven tool.
	      // ** NOTE: This 'maven-3.5.2' Maven tool must be configured
	      // **       in the global configuration.           
	      mvnHome = tool 'maven-3.8.4'
	    }

	stage("Build") {
	    steps {
		bat "mvn -version"
		bat "mvn clean install"
	    }
	}

	stage('Build Docker Image') {
	      // build docker image
	      dockerImage = docker.build("com.jaymorelli.posp/order-service")
	    }

	stage("Run image") {
	    bat "docker run -p 8081:8081 com.jaymorelli.posp/order-service"
	}



	post {
		always {
			cleanWs()
		}
	}
}
