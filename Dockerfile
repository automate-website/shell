FROM java:openjdk-8-jre

COPY ./target/*.jar /

RUN ln -s /*.jar /app.jar \
  && chmod +x /app.jar

COPY Dockerdir/run_app.sh /run_app.sh
RUN chmod +x /run_app.sh

ENTRYPOINT ["/run_app.sh"]
