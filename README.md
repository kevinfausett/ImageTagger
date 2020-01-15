# Image Tagger and Searchable Gallery
AWS web application to allow uploading images, ML-powered tagging of their contents, and full-text search.

Current features:
* Java / SpringBoot back end. Server-side rendered front end via Thymeleaf.
* Users can upload images to a shared gallery (AWS S3)
* After images are uploaded, the back end sends them to be tagged with their contents by leveraging an ML-powered computer vision API (AWS Rekognition)
* A new object is created combining the S3 URL and the Rekognition labels ("dog", "animal", "sitting" etc) and DynamoDBMapper is used as ORM to store these in database (AWS DynamoDB)
* DynamoDB is configured to publish a stream of updates, and an AWS Lambda function written in Python is triggered by this stream
* Lambda converts the updated record into search-friendly form and stores it in search engine (AWS ElasticSearch)
* Users can search for images with a given label or set of labels, pulled from ElasticSearch

Todo:
* Front end is very incomplete
* Add user authentication (AWS Cognito)
* Allow users to search for their own images rather than just a shared gallery
* Give it a permanent home rather than living on nameless EC2 servers that are only live while I'm developing
* Put servers behind load balancer
* Possibly further expand the data model. The current architecture is more powerful than necessary for the current model, but built to accommodate scale and rapid business need changes.
* Code polish and cleanup

If you pull this repo down and try to run it without configuring S3, ElasticSearch, Rekognition, Lambda, and DynamoDB, it will not work. Those are all prerequisites. Only the EC2 instance is public-facing in my setup, and I generate preauthorized URLs to let users access S3 content with a TTL.
