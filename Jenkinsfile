#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }

    stage('check java') {
        sh "java -version"
    }

    stage('clean') {
        //sh "./mvnw clean"
        sh "/usr/bin/mvn clean"
    }

    /*
    stage('backend tests') {
        try {
            //sh "./mvnw test"
            sh "/usr/bin/mvn test"
        } catch(err) {
            throw err
        } finally {
          
        }
    }
  */

    stage('packaging') {
        //sh "./mvnw package -Pprod -DskipTests"
        sh "/usr/bin/mvn package -Pprod -DskipTests"
        archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
    }

    // Uncomment the following block to add Sonar analysis.
    /*stage('quality analysis') {
        withSonarQubeEnv('Sonar Server') {
            sh "./mvnw sonar:sonar"
        }
    }*/
    
    stage ('docker build'){
      withCredentials([[$class: "UsernamePasswordMultiBinding", usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_PASS', credentialsId: 'dockerhub']]) {
      sh 'docker login --username $DOCKERHUB_USER --password $DOCKERHUB_PASS'
    }
    def serverImage = docker.build('snyamars007/customer360-service')
    serverImage.push('latest')
    sh 'docker logout'
   }
    
    
    stage 'notifyKubernetes'
     try{
      sh "kubectl delete deployment customer360-service1"
      }catch(e){
      println("no prior deployment exists")
     }
    
    try{
          sh "kubectl delete svc customer360-service1"   
       }catch(e){
         println("no prior service exists")
       }
   sh "sleep 3s"
   sh "kubectl run --image=snyamars007/customer360-service:latest customer360-service1  --port=8070"
   sh "kubectl expose deployment customer360-service1 "
    //test 2

}
