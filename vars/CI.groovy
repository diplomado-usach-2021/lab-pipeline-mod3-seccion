

def call(){
             figlet 'Integración Continua'

                        stage("compile"){
                                        println "Stage: ${env.STAGE_NAME}"
                                        sh  "chmod +x mvnw "
                        }


                        stage("unitTest"){
                                        println "Stage: ${env.STAGE_NAME}"
                                        sh  " ./mvnw clean test -e "
                        }

                        stage("jar"){
                                        println "Stage: ${env.STAGE_NAME}"
                                        sh  " ./mvnw clean package -e "
                        }

                        stage("sonar"){
                                        println "Stage: ${env.STAGE_NAME}"
                                        def nombreRepo = env.GIT_URL.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
                                        def scannerHome = tool 'sonar-scanner';
                                        withSonarQubeEnv('sonarqube-server') { 
                                                        sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=${nombreRepo}-${env.GIT_BRANCH}-${env.BUILD_NUMBER} -Dsonar.sources=src -Dsonar.java.binaries=build "
                                        }

                        }

                        stage("nexusUpload"){
                                        println "Stage: ${env.STAGE_NAME}"           
                        }

                        stage("gitCreateRelease"){
                                        println "Stage: ${env.STAGE_NAME}"
                        }

}

return this;