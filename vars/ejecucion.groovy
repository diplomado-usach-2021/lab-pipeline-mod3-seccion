
def call(){

    pipeline {

    agent any

    stages{
       

                        stage("compile"){
                                steps{
                                    script {
                                      def pipelineType = verifyBranchName();
        
                                        if (pipelineType == "CI"){
                                           // CI()    
                                             CD()
                                        }else{
                                            CD()
                                        }

                                    }
                                }
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