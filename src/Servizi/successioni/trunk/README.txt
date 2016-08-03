Una volta preso il progetto dal repository è necessario :

In eclipse aggiungere in Preferences / Ant / Runtime / Classpath
la libreria catalina-ant.jar contenuta in tomcat

Inoltre fra i Task aggiungere un task chiamato Deploy che punti alla classe DeployTas della libreria di cui sopra.

Successivmente lanciare hb-build-pojos dal build.xml del progetto

