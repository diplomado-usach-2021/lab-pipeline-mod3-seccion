import helpers.*

def call(){

    pipeline {
    agent any


    environment{
        STAGE = 'casa'
        pipelineType = verifyBranchName()
    }

    
    parameters {
         choice choices: ['gradle', 'maven'], description: 'Indicar herramienta de construcción', name: 'builtTool'
         string defaultValue: '', description: 'Agregar stage a ejecutar separados por coma', name: 'etapasPipeline'
    }

    stages{
       

                        stage("pipeline"){
                                steps{
                                    script {

                                        if (params.builtTool == "gradle") {

                                        } else {

                                        }

                                        
                                        def etapasPipeline = params.etapasPipeline;
                                        def listaEtapas = etapasPipeline.split(',')
                                        println "listaEtapas  + ${listaEtapas}"

                                        def pipelineType = verifyBranchName();
        

                                                def etapasDefinidasMavenCI = ["","compile","unitTest","jar","sonar","nexusUpload"]
                                                def etapasDefinidasGradleCI =  ["","build","sonar","nexusUpload"]
                                                
                                                def etapasDefinidasMavenCD = ["","gitDiff","nexusDownload","run","test","gitMergeMaster","gitMergeDevelop","gitTagMaster"]
                                                def etapasDefinidasGradleCD =  ["","gitDiff","nexusDownload","run","test","gitMergeMaster","gitMergeDevelop","gitTagMaster"]
                                                
                                                def etapasNoExistente = "";
                                                def marca = false;

                                                def etapasDefinidas;
                                                if (params.builtTool == "gradle") {
                                                    if (pipelineType == 'CI'){
                                                        etapasDefinidas = etapasDefinidasGradleCI
                                                    }else{
                                                        etapasDefinidas = etapasDefinidasGradleCD
                                                    }
                                                    
                                                } else {
                                                    if (pipelineType == 'CI'){
                                                        etapasDefinidas = etapasDefinidasMavenCI
                                                    }else{
                                                        etapasDefinidas = etapasDefinidasMavenCD                                                     
                                                    }
                                                }


                                          
                                                    for(etapa in listaEtapas){
                                                            if (!etapasDefinidas.contains(etapa)){
                                                                marca = true;
                                                                if (etapasNoExistente == ""){
                                                                        etapasNoExistente = etapa;
                                                                }else{
                                                                        etapasNoExistente = etapasNoExistente + "," + etapa ;
                                                                }
                                                            }  
                                                        }
                                               
                                      
                                                if (marca == false){
                                                     // CI(listaEtapas)
                                                    if (params.builtTool == "gradle") {
                                                         gradle(listaEtapas,verifyBranchName())
                                                        } else {
                                                         maven(listaEtapas,verifyBranchName())
                                                    }
                                                     
                                                }else{
                                                    println "error no existe las siguientes etapas : + ${etapasNoExistente}"
                                                    slackSend (color: '#FF0000', message: "Build Failure Build Success [Víctor Menares] [${env.JOB_NAME}] [${params.builtTool}], las siguientes etapas  no existen : ${etapasNoExistente} ")
                                                  //  throw new Exception("${etapasNoExistente}")  
                                                   error "las siguientes etapas  no existen : ${etapasNoExistente} "
                                                }   
                                               
                                                                                         
                                        

                                    }
                                }
                        }



              }  
              
      post {
         
		success {
          
			   slackSend (color: '#00FF00', 
                     message: "[Grupo2][Pipeline pipeline-shared-library-laboratorio3 ${pipelineType}] [Rama: ${GIT_LOCAL_BRANCH}][Stage: ${STAGE} ][Resultado: Ok]"
               )
		}
		
		failure {
            slackSend (color: '#FF0000', 
                 message: "[Grupo2][Pipeline pipeline-shared-library-laboratorio3  ${pipelineType}  ][Rama: ${GIT_LOCAL_BRANCH}][Stage:  ${STAGE} ][Resultado: No OK].")
		
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