FROM azul/zulu-openjdk:21
LABEL authors="botsuchi"

COPY *.jar app.jar
# 待重写
# 设置构建参数
#ARG PROFILE
#
#ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Shanghai","-Dfile.encoding=UTF-8", "app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]

# CMD ["--spring.profiles.active=${PROFILE}"]