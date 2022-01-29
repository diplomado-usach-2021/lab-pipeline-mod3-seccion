

def call(){
    
        figlet 'devilery Continua'

                    stage("gitDiff"){
                                    println "Stage: ${env.STAGE_NAME}"
                    }


                    stage("nexusDownload"){
                                    println "Stage: ${env.STAGE_NAME}"
                    }

                    stage("run"){
                                    println "Stage: ${env.STAGE_NAME}"
                    }

                    stage("test"){
                                    println "Stage: ${env.STAGE_NAME}"
                    }


                    stage("gitMergeMaster"){
                                    println "Stage: ${env.STAGE_NAME}"
                    }

                    stage("gitMergeDevelop"){
                                    println "Stage: ${env.STAGE_NAME}"
                    }

                    stage("gitTagMaster"){
                                    println "Stage: ${env.STAGE_NAME}"
                    }

}

return this;