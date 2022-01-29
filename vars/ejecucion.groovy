
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
                                      def pipelineType = verifyBranchName();
        
                                        if (pipelineType == "CI"){
                                            CI()                                               
                                        }else{
                                            CD()
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