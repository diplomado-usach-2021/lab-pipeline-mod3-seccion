

def call(listaEtapas){
    
        figlet 'devilery Continua'

              if (listaEtapas.contains("gitDiff") || listaEtapas.contains("nexusDownload") 
                || listaEtapas.contains("run") || listaEtapas.contains("test") || listaEtapas.contains("gitMergeMaster") 
                 || listaEtapas.contains("gitMergeDevelop") || listaEtapas.contains("gitTagMaster")  ){
                    
                    stage("gitDiff"){
                                    figlet "Stage: ${env.STAGE_NAME}"
                                     STAGE = env.STAGE_NAME
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
                        }
                 }

                if ( listaEtapas.contains("gitMergeDevelop") || listaEtapas.contains("gitTagMaster")  ){

                    stage("gitMergeDevelop"){
                                    figlet "Stage: ${env.STAGE_NAME}"
                                    STAGE = env.STAGE_NAME
                    }
                 }
                    if (listaEtapas.contains("gitTagMaster")  ){

                        stage("gitTagMaster"){
                                        figlet "Stage: ${env.STAGE_NAME}"
                                        STAGE = env.STAGE_NAME
                        }
                    }

}

return this;