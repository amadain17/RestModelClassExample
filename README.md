# examples

Examples of using model classes to generate REST requests and verify the responses.
Rest-assured is used to provide fluent REST requests.
Lombok is used to cut down the boilerplate code in the model classes.
Request examples are in both XML and JSON. Jackson libraries are used to generate both the XML, and the JSON bodies.

This approach is recommended for constructing REST request bodies for POST and PUT REST requests as it is:
* Easy to write, maintain and extend
* Readable
* Flexible (headers and bodies can be built to create both positive and negative testing of endpoints)
* Scalable

######To run all tests:

From the root of the project after cloning run:
    
    mvn test

######To run a particular test:

From the root of the project after cloning run:

    mvn -Dtest=[TESTNAME] test
    eg. mvn -Dtest=RestDataXmlAT test

######Supporting White-paper on this project:
https://docs.google.com/document/d/1SGgHTsIpgRLojipsI5eYGVv5SCgYi49N-8ySw3g1j0o/edit?usp=sharing
######White-paper on testing Rest in general
https://docs.google.com/document/d/1p4uZUJdU-xpn_M4leJgJAWTb46E3-PBI13BJQHaOqk0/edit?usp=sharing