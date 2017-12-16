FROM openjdk:8-jre-alpine

ARG SHELL_VERSION=1.0.0

ADD https://oss.sonatype.org/service/local/repositories/releases/content/website/automate/shell/$SHELL_VERSION/shell-$SHELL_VERSION.jar /app.jar

RUN chmod +x /app.jar

COPY Dockerdir/run_app.sh /run_app.sh
RUN chmod +x /run_app.sh

ENTRYPOINT ["/run_app.sh"]
