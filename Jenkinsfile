pipeline {
    agent none
    stages {
        stage('BuildAndTest') {
            matrix {
                agent any
                axes {
                    axis {
                        name 'TEST_CHAIN'
//                         values 'Ethereum', 'BSC', 'Avalanche', 'Polygon', 'Avalanche', 'Cronos', 'Fantom', 'Klaytn'
                        values 'Polygon','Avalanche'
                    }
                }
                stages {
                    stage('Test') {
                        steps {
                            sh """
                            echo Testing for CHAIN ${TEST_CHAIN}
                            mvn clean install -DTEST_CHAIN=${TEST_CHAIN}
                            """
                        }
                    }
                }
            }
        }
    }
}
