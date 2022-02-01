

def call(listaEtapas){
             figlet 'Integración Continua'

                 if (listaEtapas.contains("compile") || listaEtapas.contains("unitTest") || listaEtapas.contains("jar") 
                     ||  listaEtapas.contains("sonar")  ||  listaEtapas.contains("nexusUpload") ){
                          stage("compile 2"){
                                        STAGE = env.STAGE_NAME
                                        figlet "Stage: ${env.STAGE_NAME}"
                                        sh  "chmod +x mvnw "
                        }
                 } 
           

                     if (listaEtapas.contains("unitTest") || listaEtapas.contains("jar") 
                        ||  listaEtapas.contains("sonar")  ||  listaEtapas.contains("nexusUpload") ){
                            stage("unitTest"){
                                             STAGE = env.STAGE_NAME
                                            figlet "Stage: ${env.STAGE_NAME}"                                         
                                            sh  " ./mvnw clean test -e "
                            }
                     }

                     if (listaEtapas.contains("jar") 
                         ||  listaEtapas.contains("sonar")  ||  listaEtapas.contains("nexusUpload") ){
                            stage("unitTest"){
                                stage("jar"){
                                                figlet "Stage: ${env.STAGE_NAME}"
                                                STAGE = env.STAGE_NAME
                                                sh  " ./mvnw clean package -e "
                                }
                            }
                         }

                        if ( listaEtapas.contains("sonar")  ||  listaEtapas.contains("nexusUpload") ){
                                stage("sonar"){
                                                figlet "Stage: ${env.STAGE_NAME}"
                                                STAGE = env.STAGE_NAME
                                                def nombreRepo = env.GIT_URL.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
                                                def scannerHome = tool 'sonar-scanner';
                                                withSonarQubeEnv('sonarqube-server') { 
                                                                sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=${nombreRepo}-${env.GIT_BRANCH}-${env.BUILD_NUMBER} -Dsonar.sources=src -Dsonar.java.binaries=build "
                                                }

                                }
                            }
                            if (listaEtapas.contains("nexusUpload") ){
                                    stage("nexusUpload"){
                                            figlet "Stage: ${env.STAGE_NAME}"
                                            STAGE = env.STAGE_NAME
                                            nexusPublisher nexusInstanceId: 'laboratorio3-nexus',
                                            nexusRepositoryId: 'laboratorio3-nexus',
                                            packages: [
                                                [
                                                    $class: 'MavenPackage',
                                                    mavenAssetList: [
                                                        [classifier: '', extension: '', filePath: "${env.WORKSPACE}/build/DevOpsUsach2020-0.0.1.jar"]
                                                    ],
                                                    mavenCoordinate: [
                                                        artifactId: 'DevOpsUsach2020',
                                                        groupId: 'com.devopsusach2020',
                                                        packaging: 'jar',
                                                        version: '0.0.1'
                                                    ]
                                                ]
                                            ]      
                                    }
                            }
                if (env.GIT_BRANCH == "develop"){
                        stage("gitCreateRelease"){
                                        figlet "Stage: ${env.STAGE_NAME}"
                                         STAGE = env.STAGE_NAME
                                         //   sh "git branch releaseV1-0-1"
                                      
                        }
                  }       

}

return this;