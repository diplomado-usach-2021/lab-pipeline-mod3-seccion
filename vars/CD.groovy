

def call(){
    
        figlet 'devilery Continua'

                    stage("gitDiff"){
                                    figlet "Stage: ${env.STAGE_NAME}"
                    }


                    stage("nexusDownload"){
                                    figlet "Stage: ${env.STAGE_NAME}"
                    }

                    stage("run"){
                                    figlet "Stage: ${env.STAGE_NAME}"
                    }

                    stage("test"){
                                    figlet "Stage: ${env.STAGE_NAME}"
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