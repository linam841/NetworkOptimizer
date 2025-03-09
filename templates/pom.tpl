<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.manilvit</groupId>

  <packaging>jar</packaging>
<artifactId>${artifact}</artifactId>
<version>${version}</version>
<name>java-${artifact}</name>
<description>${description}</description>

  <url>http://maven.apache.org</url>

  <properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>

    <!-- https://mvnrepository.com/artifact/com.amazonaws/aws-lambda-java-runtime-interface-client -->
    <dependency>
      <groupId>com.amazonaws</groupId>
      <artifactId>aws-lambda-java-runtime-interface-client</artifactId>
      <version>2.6.0</version>
    </dependency>


    <!-- https://mvnrepository.com/artifact/software.amazon.awssdk/lambda -->
    <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>lambda</artifactId>
      <version>2.30.33</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/software.amazon.awssdk/s3 -->
    <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>s3</artifactId>
      <version>2.30.34</version>
    </dependency>


    <!-- https://mvnrepository.com/artifact/com.amazonaws/aws-lambda-java-events -->
    <dependency>
      <groupId>com.amazonaws</groupId>
      <artifactId>aws-lambda-java-events</artifactId>
      <version>3.15.0</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/com.amazonaws/amazon-sqs-java-messaging-lib -->
    <dependency>
      <groupId>com.amazonaws</groupId>
      <artifactId>amazon-sqs-java-messaging-lib</artifactId>
      <version>2.1.4</version>
    </dependency>




    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.2.1</version>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>shade</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>
