# RestPKCS7Signer<br/>
to build:<br/>
mvn clean dependency:copy-dependencies package jarsigner:sign (configure .p12 cert im pom.xml)

to run :<br/>
java - jar RestPKCS7Signer.jar -host localhost -port 5000
<br/>
to configure your driver (ex):<br/>
/home/${user.home}/signer.properties<br/>
config.driver=[{"name":"SafeNet","path":"/usr/lib64/libeToken.so"}]
<br/>
to access:
<br/>
in browser : <br/>
http://localhost:5000/files/sign/?list=test1.pdf;test2.pdf  (files in /home/${user.home}/*.pdf)
<br/> or<br/>
http://localhost:5000/files/sign/?list=/home/username/test1.pdf;/home/username/test2.pdf


