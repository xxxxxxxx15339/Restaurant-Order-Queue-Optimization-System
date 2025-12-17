pipeline {
    agent any

    tools {
        // Make sure a Maven tool named 'Maven-3.9' is configured in Jenkins (Global Tool Configuration)
        maven 'Maven-3.9'
    }

    environment {
        // Adjust image name if you prefer something else
        DOCKER_IMAGE = 'reservation-devices'
        DOCKER_TAG   = "${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo '📥 Checking out code from Git...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '🔨 Building Maven application...'
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Run Tests') {
            steps {
                echo '🧪 Running unit tests...'
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        // Optional: enable this when you add JaCoCo to your pom.xml
        /*
        stage('Code Coverage') {
            steps {
                echo '📊 Generating code coverage report...'
                sh 'mvn jacoco:report'
            }
        }
        */

        stage('Build Docker Image') {
            when {
                expression { return fileExists('Dockerfile') }
            }
            steps {
                echo '🐳 Building Docker image...'
                script {
                    sh 'docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .'
                    sh 'docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest'
                }
            }
        }

        stage('Deploy') {
            steps {
                echo '🚀 Application image built successfully'
                sh 'docker images | grep ${DOCKER_IMAGE} || true'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline completed successfully!'
        }
        failure {
            echo '❌ Pipeline failed! Check the logs.'
        }
        always {
            echo '🧹 Cleaning workspace...'
            cleanWs()
        }
    }
}
