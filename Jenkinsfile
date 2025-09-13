pipeline {
    agent {
        docker {
            image 'maven:3.8.7-openjdk-17'
            args '-v /var/run/docker.sock:/var/run/docker.sock' // Access to Docker host
        }
    }

    environment {
        SCANNER_HOME = tool 'sonar-scanner'
        maven 'MAVEN3'
    }

    stages {

        stage('Git Checkout') {
            steps {
                git 'https://github.com/prathapchitra/secretsanta-generator.git'
            }
        }

        stage('Code Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('OWASP Dependency Check') {
            steps {
                dependencyCheck additionalArguments: ' --scan ./ ', odcInstallation: 'DC'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonar') {
                    sh '''$SCANNER_HOME/bin/sonar-scanner \
                        -Dsonar.projectName=Santa \
                        -Dsonar.projectKey=Santa \
                        -Dsonar.java.binaries=.'''
                }
            }
        }

        stage('Code Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    withDockerRegistry(credentialsId: 'docker-cred') {
                        sh 'docker build -t santa123 .'
                    }
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    withDockerRegistry(credentialsId: 'docker-cred') {
                        sh 'docker tag santa123 cprathap/santa:latest'
                        sh 'docker push cprathap/santa:latest'
                    }
                }
            }
        }

        stage('Docker Image Scan') {
            steps {
                sh 'trivy image cprathap/santa:latest'
            }
        }

        stage('K8s Deployment') {
            steps {
                sh 'kubectl apply -f deployment-service.yaml'
            }
        }
    }
}
