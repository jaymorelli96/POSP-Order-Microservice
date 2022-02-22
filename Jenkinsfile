node {

	def mvnHome = tool 'maven-3.8.4'

	def dockerImageTag = "com.jaymorelli.posp/order-service"
	def dockerImage
	
	stage('Clone Repo') { // for display purposes
	      git branch: 'master',
		    credentialsId: 'dd2debc1-138f-41ce-b67d-0c59eda46b60',
		    url: 'https://github.com/jaymorelli96/POSP-Order-Microservice.git'
          
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
