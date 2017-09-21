node('node') {
  stage('Checkout') {
    checkout scm
  }

  stage('Compile') {
    sh './gradlew classes'
  }

  stage('Compile tests') {
    sh './gradlew testClasses'
  }

  stage('Test') {
    sh './gradlew test'
    junit 'build/reports/**/*.xml'
  }

  stage('Packaging') {
    sh './gradlew build'
  }

  stage('Archive') {
    archiveArtifacts artifacts: 'build/libs/*.jar'
  }
}
