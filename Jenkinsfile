pipeline {
  options {
    mavenCache(enable: true)
    
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
