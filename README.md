# RestPKCS7Signer
to build:
mvn clean dependency:copy-dependencies package jarsigner:sign (configure .p12 cert im pom.xml)

to run :
java - jar RestPKCS7Signer.jar -host localhost -port 5000

to configure your driver (ex):
/home/${user.home}/signer.properties
config.driver=[{"name":"SafeNet","path":"/usr/lib64/libeToken.so"}]

to access:

in browser : 
http://localhost:5000/files/sign/?list=test1.pdf;test2.pdf  (files in /home/${user.home}/*.pdf)
<br/> or
http://localhost:5000/files/sign/?list=/home/username/test1.pdf;/home/username/test2.pdf


