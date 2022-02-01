
def call(){

    pipeline {
    agent any

    environment{
        ETAPA_EJECUCION = '';
    }

    
    parameters {
         string defaultValue: '', description: 'Agregar stage a ejecutar separados por coma', name: 'etapasPipeline'
    }

    stages{
       

                        stage("compile"){
                                steps{
                                    script {

                                        def etapasPipeline = params.etapasPipeline;
                                        def listaEtapas = etapasPipeline.split(',')
                                        println "listaEtapas  + ${listaEtapas}"

                                        def pipelineType = verifyBranchName();
        
                                        if (pipelineType == "CI"){

                                                def etapasDefinidas = ["","compile","unitTest","jar","sonar","nexusUpload"]
                                                def etapasNoExistente = "";
                                                def marca = false;
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
                                                    CI(listaEtapas)
                                                }else{
                                                    println "error no existe las siguientes etapas : + ${etapasNoExistente}"
                                                    slackSend (color: '#FF0000', message: "Build Failure Build Success [Víctor Menares] [${env.JOB_NAME}] [${params.builtTool}], las siguientes etapas  no existen : ${etapasNoExistente} ")
                                                    throw new Exception("${etapasNoExistente}")  
                                                }   
                                               
                                                                                         
                                        }else{

                                            def etapasDefinidas = ["","gitDiff","nexusDownload","run","test","gitMergeMaster","gitMergeDevelop","gitTagMaster"]
                                            def etapasNoExistente = "";
                                            def marca = false;
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
                                                CD(listaEtapas)
                                            }else{
                                                println "error no existe las siguientes etapas : + ${etapasNoExistente}"
                                                slackSend (color: '#FF0000', message: "Build Failure Build Success [Víctor Menares] [${env.JOB_NAME}] [${params.builtTool}], las siguientes etapas  no existen : ${etapasNoExistente} ")
                                               // throw new Exception("${etapasNoExistente}")  
                                               error "las siguientes etapas  no existen : ${etapasNoExistente} "
                                            }  
                                          
                                        }

                                    }
                                }
                        }



              }  
              
      post {
		success {
             def pipelineType = verifyBranchName();
			   slackSend (color: '#00FF00', 
                     message: "[Grupo2][Pipeline pipeline-shared-library-laboratorio3 ${pipelineType}] [Rama: ${GIT_LOCAL_BRANCH}][Stage: ][Resultado: Ok]"
               )
		}
		
		failure {
              def pipelineType = verifyBranchName();
            slackSend (color: '#FF0000', 
                 message: "[Grupo2][Pipeline pipeline-shared-library-laboratorio3  ${pipelineType}  ][Rama: ${GIT_LOCAL_BRANCH}][Stage: ][Resultado: No OK].")
		
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