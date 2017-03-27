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

Make sure that [Java 8](http://www.oracle.com/technetwork/java/javase/) and [Maven 3](http://maven.apache.org/) are installed:
```bash
java -version
mvn -version
```

 * Compiling and installing:

```bash
mvn install
```

Example
=======

```java
import net.hep.ami.jami.Client;

...

try
{
	Client client = new Client("ami.in2p3.fr", "/AMI/servlet/net.hep.atlas.Database.Bookkeeping.AMI.Servlet.FrontEnd", 443);

	String result = client.execute("GetSessionInfo", new HashMap<String, String>());

	System.out.println(result);
}
catch(Exception e)
{
	System.err.println(e.getMessage());
}
```
