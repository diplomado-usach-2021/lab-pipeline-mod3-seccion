
def call(){
def pipelineType = verifyBranchName();
    pipeline {


    agent any

    stages{
         script { 
           println "pipelineType  + ${pipelineType}"
            if (pipelineType == 'CI'){
                figlet 'Integración Continua'

                        stage("compile"){
                                steps{
                                    script {
                                        println "Stage: ${env.STAGE_NAME}"
                                        sh  "chmod +x mvnw "
                                        sh " ./mvnw clean compile -e"
                                    }
                                }
                        }


                        stage("unitTest"){
                                steps{
                                    script {
                                        println "Stage: ${env.STAGE_NAME}"
                                        sh  " ./mvnw clean test -e "
                                    }
                                }
                        }

                        stage("jar"){
                                steps{
                                    script {
                                        println "Stage: ${env.STAGE_NAME}"
                                        sh  " ./mvnw clean package -e "
                                    }
                                }
                        }

                        stage("sonar"){
                                steps{
                                    script {
                                        println "Stage: ${env.STAGE_NAME}"
                                        def nombreRepo = env.GIT_URL.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
                                        def scannerHome = tool 'sonar-scanner';
                                        withSonarQubeEnv('sonarqube-server') { 
                                                        sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=${nombreRepo}-${env.GIT_BRANCH}-${env.BUILD_NUMBER} -Dsonar.sources=src -Dsonar.java.binaries=build "
                                        }
                                    }
                                }
                        }

                        stage("nexusUpload"){
                                steps{
                                    script {
                                        println "Stage: ${env.STAGE_NAME}"
                                    }
                                }
                        }

                        stage("gitCreateRelease"){
                                steps{
                                    script {
                                        println "Stage: ${env.STAGE_NAME}"
                                    }
                                }
                        }


              }else{
                    figlet 'devilery Continua'

                    stage("gitDiff"){
                            steps{
                                script {
                                    println "Stage: ${env.STAGE_NAME}"
                                }
                            }
                    }


                    stage("nexusDownload"){
                            steps{
                                script {
                                    println "Stage: ${env.STAGE_NAME}"
                                }
                            }
                    }

                    stage("run"){
                            steps{
                                script {
                                    println "Stage: ${env.STAGE_NAME}"
                                }
                            }
                    }

                    stage("test"){
                            steps{
                                script {
                                    println "Stage: ${env.STAGE_NAME}"
                                }
                            }
                    }


                    stage("gitMergeMaster"){
                            steps{
                                script {
                                    println "Stage: ${env.STAGE_NAME}"
                                }
                            }
                    }

                    stage("gitMergeDevelop"){
                            steps{
                                script {
                                    println "Stage: ${env.STAGE_NAME}"
                                }
                            }
                    }

                    stage("gitTagMaster"){
                            steps{
                                script {
                                    println "Stage: ${env.STAGE_NAME}"
                                }
                            }
                    }


              }         

     }
    }
  }  	
}


def verifyBranchName(){

    //def is_ci_or_cd = (env.GIT_BRANCH.contains('feature-')) ? 'CI' : 'CD'

    if (env.GIT_BRANCH.contains('feature-') || env.GIT_BRANCH.contains('develop')){
        return 'CI'
    } else{
        return 'CD'
    }


}

return this;