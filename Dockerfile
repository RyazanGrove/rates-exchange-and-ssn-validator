FROM panokurka/java16
COPY ./target/rates-exchange-and-ssn-validator-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "rates-exchange-and-ssn-validator-0.0.1-SNAPSHOT.jar"]