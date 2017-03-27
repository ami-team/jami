all:
	mvn package

install:
	mvn package
	mvn install -U

deploy:
	mvn deploy -Dsonatype=true

clean:
	mvn clean
