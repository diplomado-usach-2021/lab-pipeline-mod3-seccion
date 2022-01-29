
def call(){

    pipeline {

    agent any

    
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

                                                    if (marca == false){
                                                        CI(listaEtapas)
                                                    }else{
                                                      println "error no existe las siguientes etapas : + ${etapasNoExistente}"
                                                       slackSend (color: '#FF0000', message: "Build Failure Build Success [Víctor Menares] [${env.JOB_NAME}] [${params.builtTool}], las siguientes etapas  no existen : ${etapasNoExistente} ")
                                                       throw new Exception("${etapasNoExistente}")  
                                                    }                                               
                                        }else{

                                            def etapasDefinidas = ["","gitDiff","nexusDownload","run","test"]
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

                                                if (marca == false){
                                                    CD(listaEtapas)
                                                }else{
                                                    println "error no existe las siguientes etapas : + ${etapasNoExistente}"
                                                    slackSend (color: '#FF0000', message: "Build Failure Build Success [Víctor Menares] [${env.JOB_NAME}] [${params.builtTool}], las siguientes etapas  no existen : ${etapasNoExistente} ")
                                                    throw new Exception("${etapasNoExistente}")  
                                                }  
                                        }

                                    }
                                }
                        }



              }  
              
      post {
		success {
			   slackSend (color: '#00FF00', 
               message: "Build Success lab-pipeline-mod3-seccion2 Ejecución exitosa"
               )
		}
		
		failure {
            slackSend (color: '#FF0000', 
                 message: "Build Failure lab-pipeline-mod3-seccion2 Ejecución fallida en stage ")
		
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