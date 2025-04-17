#Creating a pipeline


pipeline {
    agent any
    tools {
        maven 'Maven_3.8.6' 
        jdk 'JDK_23
    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/your-repo/weather-api-tests.git'
            }
        }
        stage('Run Tests') {
            steps {
                sh 'mvn clean test'
            }
        }
    }
    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}
