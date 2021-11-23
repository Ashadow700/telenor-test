# Developer's notes:


## How to run

### Run with jar

mvn clean install
java -jar ./target/telenor-test-0.0.1-SNAPSHOT.jar

### Run with maven

mvn spring-boot:run

### Run tests

mvn test

### Run with docker

mvn clean install
docker build -t telenor-test .
docker run -p 8080:8080 telenor-test


## Known issues

### Property
The way property is handled is super-awkward, to the point where I suspect I may have misunderstood the instructions. 
If this was a real project I would have had to ask for clarification. The fact that it is effectively two different filters
(subscription gb limit & phone color) for two completely different products (subscription & phone), but
they are handheld by the same parameter in the API, means that programmatically we cannot know for sure
if this property should be used to filter subscriptions or phones at the moment it is sent to the service.
This results in that, in the filtering step we are forced to check everytime what type of property we are dealing with,
if we received both the property parameter and the sub-property parameter, if it is a phone or subscription product we are
dealing with, etc. etc.
The only way I could figure out how to deal with this is by creating the horrible looking tree of if-statements under the
method filterProperty in the class ProductService.  This will both make the code more difficult more other developers 
to follow and more difficult to extend. If, for example, we ever wanted to add other products then just phones and
subscriptions, we would have to make big code changes in multiple different places.

A better solution would be to separate the property parameter into several other more clearly defined parameters.
For better extendability we could also consider turning the class products into an interface and implement it for every 
type of Product we would like to handle. I.e. having one Phone class, one Subscription class, and other classes for other
products, each with their own properties. Phone would have color as a property, Subscription would have gbLimit, etc.

I decided not to implement any of these ideas as the test specified I should do the absolute minimum work required, and
that I decided I should stick as close to the test description as possible. 

If this was a real project I would have argued that we should rethink this architecture before starting to build my
solution.



------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------

# Welcome to Telenor's take-home assignment
**Congratulations on making it this far! Great job!**
The purpose of this assignment is to give you an opportunity to demonstrate some code.
The requirement is simple, but it is important to demonstrate clean code and good test coverage.
Do the absolute minimum work required for the application. Out of the box configurations and in-memory DBs will do just fine.
There is no time limit, but it shouldn't take more than 60-90min. 

---

In a basic Dockerized Springboot Maven application, build a single REST API endpoint that returns a filtered set of products from the provided data in the data.csv file

`GET /product`

Query Parameter			     |       Description
--------------------------------------------------------------------------------
- type					     |   The productEntity type. (String. Can be 'phone' or 'subscription')
- min_price				     |   The minimum price in SEK. (Number)
- max_price				     |   The maximum price in SEK. (Number)
- city					     |   The city in which a store is located. (String)
- property				     |   The name of the property. (String. Can be 'color' or 'gb_limit')
- property:color		     |	 The color of the phone. (String)
- property:gb_limit_min      |	 The minimum GB limit of the subscription. (Number)
- property:gb_limit_max      |	 The maximum GB limit of the subscription. (Number)

The expected response is a JSON array with the products in a 'data' wrapper. 

Example: GET /product?type=subscription&max_price=1000&city=Stockholm
{
	data: [ 
		{
		    type: 'subscription',
		    properties: 'gb_limit:10',
		    price: '704.00',
		    store_address: 'Dana gärdet, Stockholm'
	  	},
	  	{
		    type: 'subscription',
		    properties: 'gb_limit:10',
		    price: '200.00',
		    store_address: 'Octavia gränden, Stockholm'
	  	}
	]
}

Your solution should correctly filter any combination of API parameters and use some kind of a datastore.
All parameters are optional, all minimum and maximum fields should be inclusive (e.g. min_price=100&max_price=1000 should return items with price 100, 200... or 1000 SEK). 
The applications does not need to support multiple values (e.g. type=phone,subscription or property.color=green,red).

We should be able to:
- build the application with Maven
- build the Docker image and run it
- make requests to verify the behavior

Please provide an archive with the source code and a list of the terminal commands to build and run the application.

## Tools
Following are the tools needed to build and run the project:
- Maven
- Docker

## How to run
Following are the commands that are needed to run from project root directory

    $ mvn clean package
    $ docker build -t springio/gs-spring-boot-docker .
    $ docker run -p 8080:8080 -t springio/gs-spring-boot-docker
    
After that one can test with sample requests. Example is given in the following section. After finish run following commands:
    
    $ docker ps
   
Now you have the container id. Run the following command with container id:
   
    $ docker stop container_id
    
In fact, you can use `ctrl+c` which will stop the container. 

## Sample Request
One can use postman to make the following request:

    http://localhost:8080/product?type=subscription&max_price=1000&city=Stockholm

Or can use curl from command line (given that curl is available in your terminal):
    
    $ curl -i http://localhost:8080/product?type=subscription&max_price=1000&city=Stockholm
    
## Test
For running test following is the command:
    
    $ mvn test


