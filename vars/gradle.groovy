import helpers.*

/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(listaEtapas,pipelineType){
            figlet 'gradle'
            figlet pipelineType
                    if (pipelineType == 'CI'){
                        figlet 'Integración Continua'

                                if (listaEtapas.contains("build")  listaEtapas.contains("sonar")  ||  listaEtapas.contains("nexusUpload") ){ 
                                    stage("Build & unit test"){
                                                    STAGE = env.STAGE_NAME
                                                    println "Stage: ${env.STAGE_NAME}"
                                                    figlet "Stage: ${env.STAGE_NAME}"
                                                    sh " whoami; ls -ltr "
                                                    sh  "chmod +x gradlew "
                                                    sh "./gradlew clean build "
                                                    echo "${env.WORKSPACE}"
                                                    echo "${WORKSPACE}";                 
                                
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
                                        if (GIT_LOCAL_BRANCH == "develop"){
                                                stage("gitCreateRelease"){
                                                                figlet "Stage: ${env.STAGE_NAME}"
                                                                STAGE = env.STAGE_NAME
                                                                figlet "Stage: ${env.STAGE_NAME}"
                                                                
                                                                sh "  git ls-remote  --heads origin  release-v1-0-1-1-0 &&  git push origin --delete release-v1-0-1-1-0  && git branch -d  release-v1-0-1-1-0   "
                                                                sh " git branch release-v1-0-1-1-0 "
                                                                sh " git push origin release-v1-0-1-1-0  "
                                                            
                                                }
                                        }       



                            }else if (pipelineType == 'CD'){
                                if (listaEtapas.contains("gitDiff") || listaEtapas.contains("nexusDownload") 
                                            || listaEtapas.contains("run") || listaEtapas.contains("test") || listaEtapas.contains("gitMergeMaster") 
                                            || listaEtapas.contains("gitMergeDevelop") || listaEtapas.contains("gitTagMaster")  ){
                                                
                                                stage("gitDiff"){
                                                                figlet "Stage: ${env.STAGE_NAME}"
                                                                STAGE = env.STAGE_NAME
                                                                sh "git reset --hard HEAD; git checkout main; git pull origin main "
                                                                sh "git reset --hard HEAD; git checkout ${GIT_LOCAL_BRANCH }; git pull origin ${GIT_LOCAL_BRANCH }"
                                                                sh "git diff main "
                                                }

                                        }

                                    if (listaEtapas.contains("nexusDownload") 
                                            || listaEtapas.contains("run") || listaEtapas.contains("test") || listaEtapas.contains("gitMergeMaster") 
                                            || listaEtapas.contains("gitMergeDevelop") || listaEtapas.contains("gitTagMaster")  ){

                                                stage("nexusDownload"){
                                                    figlet "Stage: ${env.STAGE_NAME}"
                                                    STAGE = env.STAGE_NAME
                                                    sh    "curl -X GET -u admin:victor25 http://192.168.0.15:8083/repository/laboratorio3-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O"            
                                            
                                                }
                                            }

                                            if ( listaEtapas.contains("run") || listaEtapas.contains("test") || listaEtapas.contains("gitMergeMaster") 
                                            || listaEtapas.contains("gitMergeDevelop") || listaEtapas.contains("gitTagMaster")  ){

                                                    stage("run"){
                                                            figlet "Stage: ${env.STAGE_NAME}"
                                                            STAGE = env.STAGE_NAME
                                                            sh "nohup java -jar DevOpsUsach2020-0.0.1.jar &"
                                                            sleep 20
                                                    }
                                            }

                                            if (listaEtapas.contains("test") || listaEtapas.contains("gitMergeMaster") 
                                            || listaEtapas.contains("gitMergeDevelop") || listaEtapas.contains("gitTagMaster")  ){

                                                    stage("test"){
                                                            figlet "Stage: ${env.STAGE_NAME}"
                                                            STAGE = env.STAGE_NAME
                                                            sh  " curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing' "
                                                    }
                                            }

                                            if (listaEtapas.contains("gitMergeMaster") 
                                            || listaEtapas.contains("gitMergeDevelop") || listaEtapas.contains("gitTagMaster")  ){
                                                    stage("gitMergeMaster"){
                                                                    STAGE = env.STAGE_NAME
                                                                    figlet "Stage: ${env.STAGE_NAME}"
                                                                    def git = new helpers.Git();
                                                                    println env.GIT_BRANCH 
                                                                    println GIT_LOCAL_BRANCH 
                                                                    git.merge(GIT_LOCAL_BRANCH,"main");
                                                                    
                                                    }
                                            }

                                            if ( listaEtapas.contains("gitMergeDevelop") || listaEtapas.contains("gitTagMaster")  ){

                                                stage("gitMergeDevelop"){
                                                                figlet "Stage: ${env.STAGE_NAME}"
                                                                STAGE = env.STAGE_NAME
                                                            def git = new helpers.Git();
                                                            println env.GIT_BRANCH 
                                                            println GIT_LOCAL_BRANCH 
                                                            git.merge(GIT_LOCAL_BRANCH,"develop");
                                                            
                                                }
                                            }
                                                if (listaEtapas.contains("gitTagMaster")  ){

                                                    stage("gitTagMaster"){
                                                        figlet "Stage: ${env.STAGE_NAME}"
                                                        STAGE = env.STAGE_NAME
                                                        def git = new helpers.Git();
                                                        git.tag(GIT_LOCAL_BRANCH, 'main')
                                                            
                                                    }
                                                }
                                        }
 

    }

return this;