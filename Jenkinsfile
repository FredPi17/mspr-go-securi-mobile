node {
    // uncomment these 2 lines and edit the name 'node-4.4.7' according to what you choose in configuration
    // def nodeHome = tool name: 'node-4.4.7', type: 'jenkins.plugins.nodejs.tools.NodeJSInstallation'
    // env.PATH = "${nodeHome}/bin:${env.PATH}"
    
    stage('code quality') {
        sh "mvn sonar:sonar -Dsonar.host.url=http://raspberrysqlserver.ddns.net:9000 -Dsonar.login=7cf535153d70f10c9fba90c8f2f9bd3a02bd464a"
    }
}
