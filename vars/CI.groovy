

def call(){
             figlet 'Integración Continua'

                        stage("compile"){
                                        figlet "Stage: ${env.STAGE_NAME}"
                                        sh  "chmod +x mvnw "
                        }


                        stage("unitTest"){
                                        figlet "Stage: ${env.STAGE_NAME}"
                                        sh  " ./mvnw clean test -e "
                        }

                        stage("jar"){
                                        figlet "Stage: ${env.STAGE_NAME}"
                                        sh  " ./mvnw clean package -e "
                        }

                        stage("sonar"){
                                        figlet "Stage: ${env.STAGE_NAME}"
                                        def nombreRepo = env.GIT_URL.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
                                        def scannerHome = tool 'sonar-scanner';
                                        withSonarQubeEnv('sonarqube-server') { 
                                                        sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=${nombreRepo}-${env.GIT_BRANCH}-${env.BUILD_NUMBER} -Dsonar.sources=src -Dsonar.java.binaries=build "
                                        }

                        }

                        stage("nexusUpload"){
                                        figlet "Stage: ${env.STAGE_NAME}"           
                        }

                        stage("gitCreateRelease"){
                                        figlet "Stage: ${env.STAGE_NAME}"
                        }

}

return this;