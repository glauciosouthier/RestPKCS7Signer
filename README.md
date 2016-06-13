# RestPKCS7Signer
to build:
mvn clean dependency:copy-dependencies package jarsigner:sign (configure .p12 cert im pom.xml)

to run :
java - jar RestPKCS7Signer.jar -host localhost -port 5000

to access:

in browser : 
http://localhost:5000/files/sign/?list=test1.pdf;test2.pdf  (files in /home/*.pdf)
\n or
http://localhost:5000/files/sign/?list=/home/test1.pdf;/home/test2.pdf


