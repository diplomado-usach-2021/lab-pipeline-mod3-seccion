

def call(listaEtapas){
    
        figlet 'devilery Continua'

                    stage("gitDiff"){
                                    figlet "Stage: ${env.STAGE_NAME}"
                    }


                    stage("nexusDownload"){
                          figlet "Stage: ${env.STAGE_NAME}"
                          sh    "curl -X GET -u admin:victor25 http://192.168.0.15:8083/repository/laboratorio3-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O"            
                   
                    }

                    stage("run"){
                             figlet "Stage: ${env.STAGE_NAME}"
                             sh "nohup java -jar DevOpsUsach2020-0.0.1.jar &"
                             sleep 20
                    }

                    stage("test"){
                            figlet "Stage: ${env.STAGE_NAME}"
                             sh  " curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing' "
                    }


                    stage("gitMergeMaster"){
                                    figlet "Stage: ${env.STAGE_NAME}"
                    }

                    stage("gitMergeDevelop"){
                                    figlet "Stage: ${env.STAGE_NAME}"
                    }

                    stage("gitTagMaster"){
                                    figlet "Stage: ${env.STAGE_NAME}"
                    }

}

return this;