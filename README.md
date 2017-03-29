[![][License img]][License]
[![][Dependency Status img]][Dependency Status]
[![][Maven Central img]][Maven Central]

<a href="http://lpsc.in2p3.fr/" target="_blank">
	<img src="http://www.cern.ch/ami/images/logo_lpsc.gif" alt="LPSC" height="62" />
</a>
&nbsp;&nbsp;&nbsp;&nbsp;
<a href="http://www.in2p3.fr/" target="_blank">
	<img src="http://www.cern.ch/ami/images/logo_in2p3.gif" alt="IN2P3" height="72" />
</a>
&nbsp;&nbsp;&nbsp;&nbsp;
<a href="http://www.univ-grenoble-alpes.fr/" target="_blank">
	<img src="http://www.cern.ch/ami/images/logo_uga.png" alt="UGA" height="72" />
</a>
&nbsp;&nbsp;&nbsp;&nbsp;
<a href="http://home.cern/" target="_blank">
	<img src="http://www.cern.ch/ami/images/logo_cern.png" alt="CERN" height="72" />
</a>
&nbsp;&nbsp;&nbsp;&nbsp;
<a href="http://atlas.cern/" target="_blank">
	<img src="http://www.cern.ch/ami/images/logo_atlas.png" alt="ATLAS" height="87" />
</a>

JAMI
====

JAMI is an AMI JAVA client. It was originally developed for the A Toroidal LHC ApparatuS (ATLAS) experiment, one of the two general-purpose detectors at the Large Hadron Collider (LHC).

Installing JAMI
===============

 * Requierments:

Make sure that [Java 6 (or more)](http://www.oracle.com/technetwork/java/javase/) and [Maven 3](http://maven.apache.org/) are installed:
```bash
java -version
mvn -version
```

 * Compiling and installing:

```bash
mvn install
```

 * Configuring your Maven project:
```xml
<dependency>
	<groupId>net.hep.ami</groupId>
	<artifactId>jami</artifactId>
	<version>LATEST</version>
</dependency>
```

Documentation
=============

[Javadoc page](https://www.cern.ch/ami/clients/jami/).

Example
=======

Authenticating with login and password:
```java
import net.hep.ami.jami.Client;

...

try
{
	Client client = new Client("ami.in2p3.fr", "/AMI/servlet/net.hep.atlas.Database.Bookkeeping.AMI.Servlet.FrontEnd", 443);

	/**/

	Map<String, String> arguments1 = new HashMap<>();

	arguments1.put("AMIUser", "myLogin"); 
	arguments1.put("AMIPass", "myPassword");
	
	System.out.println(client.execute("GetSessionInfo", arguments1));

	/**/

	Map<String, String> arguments2 = new HashMap<>();

	arguments2.put("myArgument1", "myValue1"); 
	arguments2.put("myArgument2", "myValue2");
	
	System.out.println(client.execute("myCommand", arguments2));
}
catch(Exception e)
{
	System.err.println(e.getMessage());
}
```

Authenticating with X509 certificate:
```java
import net.hep.ami.jami.Client;

...

try
{
	Client client = new Client("ami.in2p3.fr", "/AMI/servlet/net.hep.atlas.Database.Bookkeeping.AMI.Servlet.FrontEnd", 443, "mySessionName", new KeyManager[] {myKeyManager});

	/**/

	Map<String, String> arguments = new HashMap<>();

	arguments.put("myArgument1", "myValue1"); 
	arguments.put("myArgument2", "myValue2");
	
	System.out.println(client.execute("myCommand", arguments));
}
catch(Exception e)
{
	System.err.println(e.getMessage());
}
```

[License]:http://www.cecill.info/licences/Licence_CeCILL-C_V1-en.txt
[License img]:https://img.shields.io/badge/license-CeCILL--C-blue.svg

[Dependency Status]:https://www.versioneye.com/user/projects/58d9287ed6c98d0044054477/
[Dependency Status img]:https://www.versioneye.com/user/projects/58d9287ed6c98d0044054477/badge.svg?style=flat

[Maven Central]:https://maven-badges.herokuapp.com/maven-central/net.hep.ami/jami
[Maven Central img]:https://maven-badges.herokuapp.com/maven-central/net.hep.ami/jami/badge.svg
