pipeline {
    agent {
        docker {
            image 'cprathap/maven-docker:latest'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
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
                    sh 'mvn sonar:sonar -Dsonar.projectKey=Santa -Dsonar.projectName=Santa'
                }
            }
        }

        stage('Code Build') {
            steps {
                sh 'mvn clean package -DskipTests'
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
