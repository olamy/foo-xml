pipeline {
  options {
    mavenCache()
    
  }  
  agent any
  stages {
    stage("Build") {
      steps {
        sh "mvn clean verify"
      }
    }
  }
}
